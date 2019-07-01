package awsviewer.common;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import awsviewer.conf.Clients;
import awsviewer.conf.Helper;
import awsviewer.conf.Speaker;
import awsviewer.inf.CUtil;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesRequest;
import software.amazon.awssdk.services.ec2.model.DescribeSubnetsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeVpcsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeVpcsResponse;
import software.amazon.awssdk.services.ec2.model.Filter;
import software.amazon.awssdk.services.ec2.model.Instance;
import software.amazon.awssdk.services.ec2.model.InstanceStateName;
import software.amazon.awssdk.services.ec2.model.NetworkInterface;
import software.amazon.awssdk.services.ec2.model.Reservation;
import software.amazon.awssdk.services.ec2.model.Vpc;
import software.amazon.awssdk.services.ecs.EcsClient;
import software.amazon.awssdk.services.ecs.model.Cluster;
import software.amazon.awssdk.services.ecs.model.ContainerInstance;
import software.amazon.awssdk.services.ecs.model.DescribeClustersRequest;
import software.amazon.awssdk.services.ecs.model.DescribeContainerInstancesRequest;
import software.amazon.awssdk.services.ecs.model.ListClustersRequest;
import software.amazon.awssdk.services.ecs.model.ListClustersResponse;
import software.amazon.awssdk.services.ecs.model.ListContainerInstancesRequest;
import software.amazon.awssdk.services.ecs.model.ListContainerInstancesResponse;

public class Uecs implements CUtil {

    public static final Uecs build() {
        return new Uecs();
    }

    @Override
    public void printAllResource(Speaker skBranch) {

    }

    @Override
    public void printVpcResource( String andOrOr, String mode, Speaker skBranch, Filter... filters)
            throws Exception {
        Speaker mSpeaker = skBranch.clone();
        EcsClient ecs = (EcsClient) Clients.getClientByServiceClass(Clients.ECS, mSpeaker.getProfile());
        Ec2Client ec2 = (Ec2Client) Clients.getClientByServiceClass(Clients.EC2, mSpeaker.getProfile());
        Uec2 uec2 = skBranch.getUec2();
        Iterator<DescribeVpcsResponse> iterVpcs = null;
        if (andOrOr.equals("OR")) {
            for (Filter f : filters) {
                iterVpcs = ec2.describeVpcsPaginator(DescribeVpcsRequest.builder().filters(f).build()).iterator();
                while (iterVpcs.hasNext()) {
                    List<Vpc> vpcs = iterVpcs.next().vpcs();
                    for (Vpc vpc : vpcs) {
                        this.printVpcCluster(ecs, ec2, uec2, vpc.vpcId(), mode, mSpeaker);
                    }
                }
            }
        } else if (andOrOr.equals("AND")) {
            iterVpcs = ec2.describeVpcsPaginator(DescribeVpcsRequest.builder().filters(filters).build()).iterator();
            while (iterVpcs.hasNext()) {
                List<Vpc> vpcs = iterVpcs.next().vpcs();
                for (Vpc vpc : vpcs) {
                    this.printVpcCluster(ecs, ec2, uec2, vpc.vpcId(), mode, mSpeaker);
                }
            }
        }
    }

    public void printVpcCluster(EcsClient ecs, Ec2Client ec2, Uec2 uec2, String vpcId, String mode, Speaker skBranch) {
        Speaker mSpeaker = skBranch.clone();
        Helper hp = new Helper();
        // -------------------- Begin ECS
        Set<String> validClusterArn = new TreeSet<String>();
        mSpeaker.printTitle("Elastic Container Service:");
        int ecsCount = 0;
        Iterator<ListClustersResponse> iterClusters = ecs.listClustersPaginator(ListClustersRequest.builder().build())
                .iterator();
        while (iterClusters.hasNext()) {
            List<String> clusterArns = iterClusters.next().clusterArns();
            for (String cArn : clusterArns) {
                Cluster ec = ecs
                        .describeClusters(DescribeClustersRequest.builder().clusters(cArn).build()).clusters().get(0);
                List<String> containerInstaneArns = ecs
                        .listContainerInstances(
                                ListContainerInstancesRequest.builder().cluster(ec.clusterName()).build())
                        .containerInstanceArns();
                for (String instanceArn : containerInstaneArns) {
                    List<ContainerInstance> cis = ecs
                            .describeContainerInstances(DescribeContainerInstancesRequest.builder()
                                    .cluster(ec.clusterName()).containerInstances(instanceArn).build())
                            .containerInstances();
                    for (ContainerInstance ci : cis) {
                        for (Reservation r : ec2
                                .describeInstances(
                                        DescribeInstancesRequest.builder().instanceIds(ci.ec2InstanceId()).build())
                                .reservations()) {
                            for (Instance i : r.instances()) {
                                if (!i.state().name().equals(InstanceStateName.TERMINATED)) {
                                    for (NetworkInterface eni : uec2.getEnisInVpc()) {
                                        if (eni.attachment() != null && eni.attachment().instanceId() != null
                                                && eni.attachment().instanceId().equals(i.instanceId())) {
                                            String subnetId = eni.subnetId();
                                            String vpcIdc = ec2.describeSubnets(
                                                    DescribeSubnetsRequest.builder().subnetIds(subnetId).build())
                                                    .subnets().get(0).vpcId();
                                            if (vpcIdc.equals(vpcId)) {
                                                validClusterArn.add(cArn);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        //System.out.println(validClusterArn.toString());
        Iterator<ListClustersResponse> iterClustersRound2 = ecs
                .listClustersPaginator(ListClustersRequest.builder().build()).iterator();
        while (iterClustersRound2.hasNext()) {
            List<String> clusterArns = iterClustersRound2.next().clusterArns();
            for (String cArn : clusterArns) {
                if (!validClusterArn.contains(cArn)) {
                    continue;
                }
                Speaker ecsSpeaker = mSpeaker.clone();
                software.amazon.awssdk.services.ecs.model.Cluster ec = ecs
                        .describeClusters(DescribeClustersRequest.builder().clusters(cArn).build()).clusters().get(0);
                ecsSpeaker.smartPrintResult(true,
                        Speaker.ECS + " ECS-" + (++ecsCount) + ": " + ec.clusterName() + ", " + ec.status()
                                + ", instances:" + ec.registeredContainerInstancesCount() + ", service:"
                                + ec.activeServicesCount() + ", task(run/pending):" + ec.runningTasksCount() + "/"
                                + ec.pendingTasksCount());
                Iterator<ListContainerInstancesResponse> iterContainerInstanceArns = ecs
                        .listContainerInstancesPaginator(
                                ListContainerInstancesRequest.builder().cluster(ec.clusterName()).build())
                        .iterator();
                while (iterContainerInstanceArns.hasNext()) {
                    List<String> containerInstanceArns = iterContainerInstanceArns.next().containerInstanceArns();
                    for (String instanceArn : containerInstanceArns) {
                        Speaker iSpeaker = ecsSpeaker.clone();
                        if (mode.equals(PLAIN)) {
                            iSpeaker.smartPrintResult(true, "instance: " + instanceArn);
                        } else {
                            iSpeaker.smartPrintResult(true, "instance: " + hp.redactArn(instanceArn));
                        }
                        List<ContainerInstance> cis = ecs
                                .describeContainerInstances(DescribeContainerInstancesRequest.builder()
                                        .cluster(ec.clusterName()).containerInstances(instanceArn).build())
                                .containerInstances();
                        for (ContainerInstance ci : cis) {
                            Speaker aSpeaker = iSpeaker.clone();
                            for (Reservation r : ec2
                                    .describeInstances(
                                            DescribeInstancesRequest.builder().instanceIds(ci.ec2InstanceId()).build())
                                    .reservations()) {
                                for (Instance i : r.instances()) {
                                    if (!i.state().name().equals(InstanceStateName.TERMINATED)) {
                                        if (mode.equals(PLAIN)) {
                                            aSpeaker.smartPrintResult(true, "Name:{" + uec2.getNameTagValueEc2(i.tags())
                                                    + "}, " + i.instanceId() + ", " + i.instanceType() + ", sriov:"
                                                    + (i.sriovNetSupport() == null ? "n/a" : i.sriovNetSupport())
                                                    + ", ena:" + i.enaSupport() + ", " + i.state().name() + ", "
                                                    + i.privateIpAddress() + ", "
                                                    + (i.publicIpAddress() == null ? "n/a" : i.publicIpAddress()));
                                        } else {
                                            aSpeaker.smartPrintResult(true, "Name:{" + uec2.getNameTagValueEc2(i.tags())
                                                    + "}, " + i.instanceId() + ", " + i.instanceType() + ", sriov:"
                                                    + (i.sriovNetSupport() == null ? "n/a" : i.sriovNetSupport())
                                                    + ", ena:" + i.enaSupport() + ", " + i.state().name() + ", "
                                                    + hp.redactIp(i.privateIpAddress()) + ", "
                                                    + (i.publicIpAddress() == null ? "n/a"
                                                            : hp.redactIp(i.publicIpAddress())));
                                        }
                                        for (NetworkInterface eni : uec2.getEnisInVpc()) {
                                            Speaker eniSpeaker = aSpeaker.clone();
                                            if (eni.attachment() != null && eni.attachment().instanceId() != null
                                                    && eni.attachment().instanceId().equals(i.instanceId())) {
                                                eniSpeaker.smartPrintResult(true,
                                                        "eni-" + eni.attachment().deviceIndex() + ": "
                                                                + eni.interfaceType() + ", " + eni.availabilityZone());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        mSpeaker.printResult(true, "TTL-ECS:" + ecsCount + "\n");
        // -------------------- End ECS
    }


}