package awsviewer.common;

import java.util.Iterator;
import java.util.List;

import awsviewer.conf.Clients;
import awsviewer.conf.Speaker;
import awsviewer.inf.CUtil;
import software.amazon.awssdk.services.autoscaling.AutoScalingClient;
import software.amazon.awssdk.services.autoscaling.model.AutoScalingGroup;
import software.amazon.awssdk.services.autoscaling.model.DescribeAutoScalingGroupsRequest;
import software.amazon.awssdk.services.autoscaling.model.DescribeLaunchConfigurationsRequest;
import software.amazon.awssdk.services.autoscaling.model.Filter;
import software.amazon.awssdk.services.autoscaling.model.LaunchConfiguration;
import software.amazon.awssdk.services.autoscaling.model.Tag;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeSubnetsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeVpcsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeVpcsResponse;
import software.amazon.awssdk.services.ec2.model.Vpc;

public class Uautoscaling implements CUtil {

    public static final Uautoscaling build() {
        return new Uautoscaling();
    }

    @Override
    public void printAllResource(Speaker skBranch) throws Exception {
        AutoScalingClient asg = (AutoScalingClient) Clients.getClientByServiceClass(Clients.AUTOSCALING,
                skBranch.getProfile());
        Speaker mSpeaker = skBranch.clone();
        mSpeaker.printResourceSubTitle(null);
        Speaker lcSpeaker = mSpeaker.clone();
        List<AutoScalingGroup> asgs = asg.describeAutoScalingGroups(DescribeAutoScalingGroupsRequest.builder().build())
                .autoScalingGroups();
        int asgCount = 0;
        for (AutoScalingGroup g : asgs) {
            Speaker asgSpeaker = mSpeaker.clone();
            asgSpeaker.smartPrintResult(false, Speaker.ASG + " ASG-" + (++asgCount) + ": " + g.autoScalingGroupName());
            asgSpeaker.printRaw(true, ", launch-config: " + g.launchConfigurationName());
        }
        int lcCount = 0;
        lcSpeaker.printResourceSubTitle("launch-config");
        List<LaunchConfiguration> lcs = asg
                .describeLaunchConfigurations(DescribeLaunchConfigurationsRequest.builder().build())
                .launchConfigurations();
        for (LaunchConfiguration lc : lcs) {
            lcSpeaker.printResult(true, Speaker.LC + " LC-" + (++lcCount) + ": " + lc.launchConfigurationName());
        }
    }

    @Override
    public void printVpcResource(String andOrOr, String mode, Speaker skBranch,
            software.amazon.awssdk.services.ec2.model.Filter... filters) throws Exception{
                Speaker mSpeaker = skBranch.clone();
        AutoScalingClient asg = (AutoScalingClient) Clients.getClientByServiceClass(Clients.AUTOSCALING, mSpeaker.getProfile());
        Ec2Client ec2 = (Ec2Client) Clients.getClientByServiceClass(Clients.EC2, mSpeaker.getProfile());
        Uec2 uec2 = skBranch.getUec2();
        Iterator<DescribeVpcsResponse> iterVpcs = null;
        if (andOrOr.equals("OR")) {
            for (software.amazon.awssdk.services.ec2.model.Filter f : filters) {
                iterVpcs = ec2.describeVpcsPaginator(DescribeVpcsRequest.builder().filters(f).build()).iterator();
                while (iterVpcs.hasNext()) {
                    List<Vpc> vpcs = iterVpcs.next().vpcs();
                    for (Vpc vpc : vpcs) {
                        this.printVpcAsg(asg, ec2, uec2, vpc.vpcId(), mSpeaker);
                    }
                }
            }
        } else if (andOrOr.equals("AND")) {
            iterVpcs = ec2.describeVpcsPaginator(DescribeVpcsRequest.builder().filters(filters).build()).iterator();
            while (iterVpcs.hasNext()) {
                List<Vpc> vpcs = iterVpcs.next().vpcs();
                for (Vpc vpc : vpcs) {
                    this.printVpcAsg(asg, ec2, uec2, vpc.vpcId(), mSpeaker);
                }
            }
        }
    }

    public void printVpcAsg(AutoScalingClient asg, Ec2Client ec2, Uec2 uec2, String vpcId, Speaker skBranch){
        Speaker vSpeaker = skBranch.clone();
        // -------------------- Begin Auto Scaling Group
        vSpeaker.printTitle("Auto Scaling Group:");
        int asgCount = 0;
        Iterator<AutoScalingGroup> iterAsg = asg.describeAutoScalingGroupsPaginator().autoScalingGroups()
                .iterator();
        AutoScalingGroup as = null;
        Speaker asSpeaker = null;
        while (iterAsg.hasNext()) {
            as = iterAsg.next();
            asSpeaker = vSpeaker.clone();
            String subnetString = as.vpcZoneIdentifier();
            String[] subnets = subnetString.split(",");
            if (vpcId
                    .equals(ec2
                            .describeSubnets(DescribeSubnetsRequest.builder().subnetIds(subnets[0]).build())
                            .subnets().get(0).vpcId())) {
                asSpeaker.smartPrintResult(true,
                        Speaker.ASG + " ASG-" + (++asgCount) + ": " + as.autoScalingGroupName() + ", lc:"
                                + as.launchConfigurationName() + ", status:" + as.status() + ", hc-type:"
                                + as.healthCheckType());
                asSpeaker.printResult(true, "az:" + as.availabilityZones());
                List<software.amazon.awssdk.services.autoscaling.model.Instance> instances = as.instances();
                asSpeaker.printResult(true, "desired:" + as.desiredCapacity() + ", max:" + as.maxSize()
                        + ", min:" + as.minSize() + ", registered:" + instances.size());
                for (software.amazon.awssdk.services.autoscaling.model.Instance instance : instances) {
                    uec2.getInstanceId2Ec2Type().put(instance.instanceId(), "ASG");
                }
            }
        }
        vSpeaker.printResult(true, "TTL-ASG:" + asgCount + "\n");
        // -------------------- End Auto Scaling Group
    }

    /**
     * Get the value from Name tag from all tags. Nullable.
     */
    public String getNameTagValueAsg(List<Tag> tags) {
        String nameTag = null;
        for (Tag tag : tags) {
            if (tag.key().equals("Name")) {
                nameTag = tag.value();
            }
        }
        return nameTag;
    }

    /**
     * Create an ASG filter.
     */
    public Filter createFilterAsg(String name, String value) {
        Filter f = Filter.builder().name(name).values(value).build();
        return f;
    }

}