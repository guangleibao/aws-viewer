package awsviewer.common;

import java.util.List;

import awsviewer.conf.Speaker;
import awsviewer.inf.CUtil;
import software.amazon.awssdk.core.SdkClient;
import software.amazon.awssdk.services.autoscaling.AutoScalingClient;
import software.amazon.awssdk.services.autoscaling.model.AutoScalingGroup;
import software.amazon.awssdk.services.autoscaling.model.DescribeAutoScalingGroupsRequest;
import software.amazon.awssdk.services.autoscaling.model.DescribeLaunchConfigurationsRequest;
import software.amazon.awssdk.services.autoscaling.model.Filter;
import software.amazon.awssdk.services.autoscaling.model.LaunchConfiguration;
import software.amazon.awssdk.services.autoscaling.model.Tag;

public class Uautoscaling implements CUtil {

    public static final Uautoscaling build() {
        return new Uautoscaling();
    }

    @Override
    public void printAllResource(SdkClient c, Speaker skBranch) {
        AutoScalingClient asg = (AutoScalingClient) c;
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