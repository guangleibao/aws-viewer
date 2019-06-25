package awsviewer.common;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import awsviewer.conf.Clients;
import awsviewer.conf.Speaker;
import awsviewer.inf.CUtil;
import software.amazon.awssdk.core.SdkClient;
import software.amazon.awssdk.services.autoscaling.AutoScalingClient;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.Address;
import software.amazon.awssdk.services.ec2.model.DescribeImagesRequest;
import software.amazon.awssdk.services.ec2.model.DescribeKeyPairsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeNetworkAclsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeNetworkInterfacesRequest;
import software.amazon.awssdk.services.ec2.model.DescribeRouteTablesRequest;
import software.amazon.awssdk.services.ec2.model.DescribeSecurityGroupsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeSnapshotsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeSubnetsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeVolumesRequest;
import software.amazon.awssdk.services.ec2.model.Filter;
import software.amazon.awssdk.services.ec2.model.GroupIdentifier;
import software.amazon.awssdk.services.ec2.model.Image;
import software.amazon.awssdk.services.ec2.model.Instance;
import software.amazon.awssdk.services.ec2.model.InstanceStateName;
import software.amazon.awssdk.services.ec2.model.IpRange;
import software.amazon.awssdk.services.ec2.model.KeyPairInfo;
import software.amazon.awssdk.services.ec2.model.NetworkAcl;
import software.amazon.awssdk.services.ec2.model.NetworkInterface;
import software.amazon.awssdk.services.ec2.model.NetworkInterfacePrivateIpAddress;
import software.amazon.awssdk.services.ec2.model.Reservation;
import software.amazon.awssdk.services.ec2.model.RouteTable;
import software.amazon.awssdk.services.ec2.model.SecurityGroup;
import software.amazon.awssdk.services.ec2.model.Snapshot;
import software.amazon.awssdk.services.ec2.model.Subnet;
import software.amazon.awssdk.services.ec2.model.Tag;
import software.amazon.awssdk.services.ec2.model.Volume;
import software.amazon.awssdk.services.elasticache.model.SecurityGroupMembership;
import software.amazon.awssdk.services.elasticloadbalancing.ElasticLoadBalancingClient;
import software.amazon.awssdk.services.elasticloadbalancingv2.ElasticLoadBalancingV2Client;
import software.amazon.awssdk.services.elasticloadbalancingv2.model.AvailabilityZone;
import software.amazon.awssdk.services.rds.model.DBSubnetGroup;
import software.amazon.awssdk.services.rds.model.VpcSecurityGroupMembership;
import software.amazon.awssdk.services.redshift.RedshiftClient;
import software.amazon.awssdk.services.redshift.model.ClusterSubnetGroup;
import software.amazon.awssdk.services.redshift.model.DescribeClusterSubnetGroupsRequest;

/**
 * Class name must refer showServiceAll.
 */
public class Uec2 implements CUtil {

    public static final Uec2 build() {
        return new Uec2();
    }

    @Override
    public void printAllResource(SdkClient c, Speaker skBranch) {
        Ec2Client ec2 = (Ec2Client) c;
        Speaker mSpeaker = skBranch.clone();
        mSpeaker.printResourceSubTitle(null);
        Speaker ec2Speaker = mSpeaker.clone();
        ec2Speaker.printResourceSubTitle("EC2 instance");
        int ec2Count = 0;
        List<NetworkInterface> enisInRegion = ec2
                .describeNetworkInterfaces(DescribeNetworkInterfacesRequest.builder().build()).networkInterfaces();
        for (Reservation r : ec2.describeInstances().reservations()) {
            for (Instance i : r.instances()) {
                Speaker iSpeaker = ec2Speaker.clone();
                if (i.state().name().equals(InstanceStateName.TERMINATED)) {
                    continue;
                }
                iSpeaker.smartPrintResult(true,
                        Speaker.STAR + "  EC2-" + (++ec2Count) + ": Name:" + this.getNameTagValueEc2(i.tags()) + ", "
                                + i.instanceId() + ", " + i.instanceType() + ", sriov:"
                                + (i.sriovNetSupport() == null ? "n/a" : i.sriovNetSupport()) + ", ena:"
                                + i.enaSupport() + ", " + i.state().name() + ", " + i.privateIpAddress() + ", "
                                + (i.publicIpAddress() == null ? "n/a" : i.publicIpAddress()));
                iSpeaker.printResult(true,
                        "role: " + (i.iamInstanceProfile() == null ? "n/a" : i.iamInstanceProfile().arn()));
                iSpeaker.printResult(true, "key: " + i.keyName());
                Filter ebsFilter = this.createFilterEc2("attachment.instance-id", i.instanceId());
                List<Volume> volumes = ec2.describeVolumes(DescribeVolumesRequest.builder().filters(ebsFilter).build())
                        .volumes();
                for (Volume v : volumes) {
                    iSpeaker.printResult(true,
                            "ebs: tag-name:" + this.getNameTagValueEc2(v.tags()) + ", size:" + v.size() + "G"
                                    + ", type:" + v.volumeType() + ", iops:" + v.iops() + ", device:"
                                    + v.attachments().get(0).device());
                }
                for (NetworkInterface eni : enisInRegion) {
                    Speaker eniSpeaker = iSpeaker.clone();
                    if (eni.attachment() != null && eni.attachment().instanceId() != null
                            && eni.attachment().instanceId().equals(i.instanceId())) {
                        eniSpeaker.smartPrintResult(true,
                                "eni-" + eni.attachment().deviceIndex() + ": " + eni.interfaceType() + ", "
                                        + eni.availabilityZone() + ", primary:{" + eni.privateDnsName() + ", "
                                        + eni.privateIpAddress() + ", "
                                        + (eni.association() == null ? "n/a" : eni.association().publicDnsName()) + ", "
                                        + (eni.association() == null ? "n/a" : eni.association().publicIp()) + "}");

                        for (NetworkInterfacePrivateIpAddress addr : eni.privateIpAddresses()) {
                            eniSpeaker.printResult(true, "address-association: " + addr.privateIpAddress() + ", "
                                    + (addr.association() != null ? addr.association().publicIp() : "no-public-ip"));
                        }
                    }
                }
            }
        }
        Speaker ebsSpeaker = mSpeaker.clone();
        int ebsCount = 0;
        ebsSpeaker.printResourceSubTitle("EBS");
        for (Volume v : ec2.describeVolumes(DescribeVolumesRequest.builder().build()).volumes()) {
            ebsSpeaker.printResult(true,
                    Speaker.EBS + " EBS-" + (++ebsCount) + ": " + v.volumeId() + ", " + v.volumeType() + ", " + v.size()
                            + "GB, " + v.availabilityZone() + ", "
                            + ((v.attachments().size() == 0) ? "null" : v.attachments().get(0).instanceId()) + ", Name:"
                            + this.getNameTagValueEc2(v.tags()));
        }
        Speaker ssSpeaker = mSpeaker.clone();
        int ssCount = 0;
        ssSpeaker.printResourceSubTitle("SNAPSHOT");
        String ssName = null;
        String accountId = null;
        try {
            accountId = Usts.build().getAccountId(skBranch.getProfile());
        } catch (Exception e) {
            e.printStackTrace();
        }
        DescribeSnapshotsRequest dsr = DescribeSnapshotsRequest.builder().ownerIds(accountId).build();
        for (Snapshot s : ec2.describeSnapshots(dsr).snapshots()) {
            ssSpeaker.printResult(true,
                    Speaker.SNAPSHOT + " SNAPSHOT-" + (++ssCount) + ": " + s.snapshotId() + ", " + s.volumeSize()
                            + "GB, " + (s.encrypted() ? "Encrypted" : "Plain" + ", " + s.state() + ", Name:" + ssName));
        }
        Speaker amiSpeaker = mSpeaker.clone();
        int amiCount = 0;
        amiSpeaker.printResourceSubTitle("AMI");
        TreeMap<String, Image> imageNameTm = new TreeMap<String, Image>();
        for (Image image : ec2.describeImages(DescribeImagesRequest.builder().owners("self").build()).images()) {
            imageNameTm.put(image.name(), image);
        }
        for (String imageName : imageNameTm.keySet()) {
            amiSpeaker.printResult(true,
                    Speaker.AMI + " AMI-" + (++amiCount) + ": " + imageName + ", "
                            + imageNameTm.get(imageName).imageId() + ", "
                            + (imageNameTm.get(imageName).platformAsString() != null ? "Windows" : "Linux"));
        }
        Speaker eipSpeaker = mSpeaker.clone();
        int eipCount = 0;
        eipSpeaker.printResourceSubTitle("EIP");
        for (Address address : ec2.describeAddresses().addresses()) {
            eipSpeaker.printResult(true,
                    Speaker.EIP + " EIP-" + (++eipCount) + ": " + address.publicIp() + ", (allocation) "
                            + address.allocationId() + ", (association) " + address.associationId() + ", (private) "
                            + address.privateIpAddress() + ", (instance) " + address.instanceId());
        }
        Speaker keySpeaker = mSpeaker.clone();
        int keyCount = 0;
        keySpeaker.printResourceSubTitle("Key-Pair");
        for (KeyPairInfo kpi : ec2.describeKeyPairs(DescribeKeyPairsRequest.builder().build()).keyPairs()) {
            keySpeaker.printResult(true, Speaker.KEYPAIR + " KEY-PAIR-" + (++keyCount) + ": " + kpi.keyName());
        }
        AutoScalingClient asg = null;
        try {
            asg = (AutoScalingClient) Clients.getClientByServiceClass(Clients.AUTOSCALING, skBranch.getProfile());
            Uautoscaling.build().printAllResource(asg, mSpeaker);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ElasticLoadBalancingClient elb = null;
        try {
            elb = (ElasticLoadBalancingClient) Clients.getClientByServiceClass(Clients.ELASTICLOADBALANCING,
                    skBranch.getProfile());
            Uelasticloadbalancing.build().printAllResource(elb, mSpeaker);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ElasticLoadBalancingV2Client elbv2 = null;
        try {
            elbv2 = (ElasticLoadBalancingV2Client) Clients.getClientByServiceClass(Clients.ELASTICLOADBALANCINGV2,
                    skBranch.getProfile());
            Uelasticloadbalancingv2.build().printAllResource(elbv2, mSpeaker);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Map a list of SGID to SG name tag value first, then try SG name.
     */
    public String groupIdentifierToSgTagOrName(Ec2Client ec2, List<GroupIdentifier> gids) {
        StringBuffer sb = new StringBuffer();
        for (GroupIdentifier gid : gids) {
            sb.append(this.sgIdToSgTagOrName(ec2, gid.groupId()) + ", ");
        }
        return sb.toString();
    }

    /**
     * Map SGID to SG name tag value first, then try SG name.
     */
    public String sgIdToSgTagOrName(Ec2Client ec2, String sgId) {
        SecurityGroup sg = ec2.describeSecurityGroups(DescribeSecurityGroupsRequest.builder().groupIds(sgId).build())
                .securityGroups().get(0);
        String tag = this.getNameTagValueEc2(sg.tags());
        String ret = null;
        if (tag != null) {
            ret = tag;
        } else {
            ret = sg.groupName();
        }
        return ret;
    }

    /**
     * Get the value from Name tag from all tags. Nullable.
     */
    public String getNameTagValueEc2(List<Tag> tags) {
        String nameTag = null;
        for (Tag tag : tags) {
            if (tag.key().equals("Name")) {
                nameTag = tag.value();
            }
        }
        return nameTag;
    }

    /**
     * Create filter with name and value. This method should appear in every
     * Uxxx.java.
     */
    public Filter createFilterEc2(String name, String value) {
        Filter f = Filter.builder().name(name).values(value).build();
        return f;
    }

    /**
     * Route table ID translation.
     */
	public String rtIdToRtTagOrId(Ec2Client ec2, String rtId) {
		RouteTable rt = ec2.describeRouteTables(DescribeRouteTablesRequest.builder().routeTableIds(rtId).build())
				.routeTables().get(0);
		String tag = this.getNameTagValueEc2(rt.tags());
		String ret = null;
		if (tag != null) {
			ret = tag;
		} else {
			ret = rt.routeTableId();
		}
		return ret;
    }

    /**
     * NACL ID translation.
     */
    public String naclIdToNaclTagOrId(Ec2Client ec2, String naclId) {
		NetworkAcl nacl = ec2.describeNetworkAcls(DescribeNetworkAclsRequest.builder().networkAclIds(naclId).build())
				.networkAcls().get(0);
		String tag = this.getNameTagValueEc2(nacl.tags());
		String ret = null;
		if (tag != null) {
			ret = tag;
		} else {
			ret = nacl.networkAclId();
		}
		return ret;
	}

    /**
     * Decode NACL into human readable messages.
     * 
     * @param ec2
     * @param nacl
     * @return
     */
    public String decodeNacl(Ec2Client ec2, NetworkAcl nacl) {
        return this.getNameTagValueEc2(nacl.tags()) + "|" + nacl.networkAclId();
    }

    /**
     * Decode Security Group ID into human readable messages.
     */
    public String decodeSgById(Ec2Client ec2, String sgId) {
        SecurityGroup sg = ec2.describeSecurityGroups(DescribeSecurityGroupsRequest.builder().groupIds(sgId).build())
                .securityGroups().get(0);
        return "{" + this.getNameTagValueEc2(sg.tags()) + "|" + sg.groupName() + "|" + sg.groupId() + "}";
    }

    /**
     * Decode IPv4 Ranges.
     */
    public String decodeIpv4Ranges(Ec2Client ec2, List<IpRange> irs) {
        StringBuffer sb = new StringBuffer();
        for (IpRange ir : irs) {
            sb.append(ir.cidrIp() + ", ");
        }
        return sb.toString();
    }

    /**
     * Decode ELBv2's AZs to human readable messages.
     */
    public String decodeElbv2AZs(Ec2Client ec2, List<AvailabilityZone> azs) {
        StringBuffer sb = new StringBuffer();
        Subnet subnet = null;
        for (AvailabilityZone az : azs) {
            subnet = ec2.describeSubnets(DescribeSubnetsRequest.builder().subnetIds(az.subnetId()).build()).subnets()
                    .get(0);
            List<Tag> tags = subnet.tags();
            sb.append("(" + this.getNameTagValueEc2(tags) + ")(" + subnet.cidrBlock() + ")(" + az.subnetId() + ")("
                    + az.zoneName() + "); ");
        }
        return "@" + sb.toString();
    }

    /**
     * Decode cluster subnet group name into human readable messages.
     */
    public String decodeRedshiftClusterSubnetGroupName(Ec2Client ec2, RedshiftClient rs,
            String clusterSubnetGroupName) {
        ClusterSubnetGroup csg = rs.describeClusterSubnetGroups(
                DescribeClusterSubnetGroupsRequest.builder().clusterSubnetGroupName(clusterSubnetGroupName).build())
                .clusterSubnetGroups().get(0);
        StringBuffer sb = new StringBuffer();
        for (software.amazon.awssdk.services.redshift.model.Subnet sn : csg.subnets()) {
            sb.append(this.decodeSubnetById(ec2, sn.subnetIdentifier()));
        }
        return sb.toString();
    }

    /**
     * Decode Subnet with IDs to human readable messages.
     */
    public String decodeSubnetsById(Ec2Client ec2, List<String> subnetids) {
        List<Subnet> subnets = ec2.describeSubnets(DescribeSubnetsRequest.builder().subnetIds(subnetids).build())
                .subnets();
        StringBuffer sb = new StringBuffer();
        for (Subnet subnet : subnets) {
            List<Tag> tags = subnet.tags();
            sb.append("(" + this.getNameTagValueEc2(tags) + ")(" + subnet.cidrBlock() + ")(" + subnet.subnetId() + ")("
                    + subnet.availabilityZone() + "); ");
        }
        return "@" + sb.toString();
    }

    /**
     * Decode Route table IDs to human readable messages.
     */
    public String decodeRouteTablesById(Ec2Client ec2, List<String> rtids) {
        Filter[] fs = new Filter[rtids.size()];
        List<RouteTable> rts = new ArrayList<RouteTable>();
        for (int i = 0; i < fs.length; i++) {
            fs[i] = Filter.builder().name("route-table-id").values(rtids.get(i)).build();
            List<RouteTable> tempRts = ec2
                    .describeRouteTables(DescribeRouteTablesRequest.builder().filters(fs[i]).build()).routeTables();
            for (RouteTable rt : tempRts) {
                rts.add(rt);
            }
        }
        StringBuffer sb = new StringBuffer();
        for (RouteTable rt : rts) {
            sb.append(this.getNameTagValueEc2(rt.tags()) + "|" + rt.routeTableId() + ", ");
        }
        return sb.toString();
    }

    /**
     * Decode Security Group IDs to human readable messages.
     */
    public String decodeSgsByIds(Ec2Client ec2, List<String> sgIds) {
        StringBuffer sb = new StringBuffer();
        for (String sgid : sgIds) {
            sb.append(this.decodeSgById(ec2, sgid) + ", ");
        }
        return sb.toString();
    }

    /**
     * Decode Subnet ID into human readable messages.
     */
    public String decodeSubnetById(Ec2Client ec2, String subnetid) {
        Subnet subnet = ec2.describeSubnets(DescribeSubnetsRequest.builder().subnetIds(subnetid).build()).subnets()
                .get(0);
        List<Tag> tags = subnet.tags();
        return "@(" + this.getNameTagValueEc2(tags) + ")(" + subnet.cidrBlock() + ")(" + subnet.subnetId() + ")("
                + subnet.availabilityZone() + "); ";
    }

    /**
     * Decode Cache Subnets into human readable messages.
     */
    public String decodeCacheSubnets(Ec2Client ec2,
            List<software.amazon.awssdk.services.elasticache.model.Subnet> subnets) {
        StringBuffer sb = new StringBuffer();
        for (software.amazon.awssdk.services.elasticache.model.Subnet subnet : subnets) {
            String snid = subnet.subnetIdentifier();
            sb.append(this.decodeSubnetById(ec2, snid));
        }
        return sb.toString();
    }

    /**
     * Decode RDS SubnetGroup into human readable message.
     */
    public String decodeDbSubnetGroup(Ec2Client ec2, DBSubnetGroup group) {
        List<String> subnetIds = new ArrayList<String>();
        for (software.amazon.awssdk.services.rds.model.Subnet subnet : group.subnets()) {
            subnetIds.add(subnet.subnetIdentifier());
        }
        List<Subnet> subnets = ec2.describeSubnets(DescribeSubnetsRequest.builder().subnetIds(subnetIds).build())
                .subnets();
        return this.decodeSubnets(ec2, subnets);
    }

    /**
     * Decode Subnets into human readable messages.
     */
    public String decodeSubnets(Ec2Client ec2, List<Subnet> subnets) {
        StringBuffer sb = new StringBuffer();
        for (Subnet subnet : subnets) {
            List<Tag> tags = subnet.tags();
            sb.append("(" + this.getNameTagValueEc2(tags) + ")(" + subnet.cidrBlock() + ")(" + subnet.subnetId() + ")("
                    + subnet.availabilityZone() + "); ");
        }
        return "@" + sb.toString();
    }

    /**
     * Decode SG memberships to SG Tag for Redshift.
     */
    public String vpcSgMemberShipsToSgTagOrNameForRedshift(Ec2Client ec2,
            List<software.amazon.awssdk.services.redshift.model.VpcSecurityGroupMembership> sgms) {
        StringBuffer sb = new StringBuffer();
        for (software.amazon.awssdk.services.redshift.model.VpcSecurityGroupMembership sgm : sgms) {
            sb.append(this.sgIdToSgTagOrName(ec2, sgm.vpcSecurityGroupId()) + ", ");
        }
        return sb.toString();
    }

    /**
     * Decode SG memberships to SG Tag for RDS.
     */
    public String vpcSgMemberShipsToSgTagOrNameForRds(Ec2Client ec2, List<VpcSecurityGroupMembership> sgms) {
        StringBuffer sb = new StringBuffer();
        for (VpcSecurityGroupMembership sgm : sgms) {
            sb.append(this.sgIdToSgTagOrName(ec2, sgm.vpcSecurityGroupId()) + ", ");
        }
        return sb.toString();
    }

    /**
     * Decode SG membership to SG Tag or Name for Cache.
     */
    public String sgMemberShipsToSgTagOrName(Ec2Client ec2, List<SecurityGroupMembership> sgms) {
        StringBuffer sb = new StringBuffer();
        for (SecurityGroupMembership sgm : sgms) {
            sb.append(this.sgIdToSgTagOrName(ec2, sgm.securityGroupId()) + ", ");
        }
        return sb.toString();
    }

}