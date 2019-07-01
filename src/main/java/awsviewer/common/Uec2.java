package awsviewer.common;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import awsviewer.conf.Clients;
import awsviewer.conf.Helper;
import awsviewer.conf.Speaker;
import awsviewer.inf.CUtil;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.DescribeAlarmsRequest;
import software.amazon.awssdk.services.cloudwatch.model.Dimension;
import software.amazon.awssdk.services.cloudwatch.model.MetricAlarm;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.Address;
import software.amazon.awssdk.services.ec2.model.DescribeImagesRequest;
import software.amazon.awssdk.services.ec2.model.DescribeKeyPairsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeNatGatewaysRequest;
import software.amazon.awssdk.services.ec2.model.DescribeNetworkAclsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeNetworkAclsResponse;
import software.amazon.awssdk.services.ec2.model.DescribeNetworkInterfacesRequest;
import software.amazon.awssdk.services.ec2.model.DescribeNetworkInterfacesResponse;
import software.amazon.awssdk.services.ec2.model.DescribeRouteTablesRequest;
import software.amazon.awssdk.services.ec2.model.DescribeRouteTablesResponse;
import software.amazon.awssdk.services.ec2.model.DescribeSecurityGroupsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeSecurityGroupsResponse;
import software.amazon.awssdk.services.ec2.model.DescribeSnapshotsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeSubnetsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeSubnetsResponse;
import software.amazon.awssdk.services.ec2.model.DescribeVolumesRequest;
import software.amazon.awssdk.services.ec2.model.DescribeVpcEndpointsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeVpcEndpointsResponse;
import software.amazon.awssdk.services.ec2.model.DescribeVpcPeeringConnectionsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeVpcPeeringConnectionsResponse;
import software.amazon.awssdk.services.ec2.model.DescribeVpcsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeVpcsResponse;
import software.amazon.awssdk.services.ec2.model.DescribeVpnGatewaysRequest;
import software.amazon.awssdk.services.ec2.model.DnsEntry;
import software.amazon.awssdk.services.ec2.model.Filter;
import software.amazon.awssdk.services.ec2.model.GroupIdentifier;
import software.amazon.awssdk.services.ec2.model.Image;
import software.amazon.awssdk.services.ec2.model.Instance;
import software.amazon.awssdk.services.ec2.model.InstanceStateName;
import software.amazon.awssdk.services.ec2.model.InternetGateway;
import software.amazon.awssdk.services.ec2.model.IpPermission;
import software.amazon.awssdk.services.ec2.model.IpRange;
import software.amazon.awssdk.services.ec2.model.KeyPairInfo;
import software.amazon.awssdk.services.ec2.model.NatGateway;
import software.amazon.awssdk.services.ec2.model.NetworkAcl;
import software.amazon.awssdk.services.ec2.model.NetworkAclAssociation;
import software.amazon.awssdk.services.ec2.model.NetworkAclEntry;
import software.amazon.awssdk.services.ec2.model.NetworkInterface;
import software.amazon.awssdk.services.ec2.model.NetworkInterfacePrivateIpAddress;
import software.amazon.awssdk.services.ec2.model.Reservation;
import software.amazon.awssdk.services.ec2.model.Route;
import software.amazon.awssdk.services.ec2.model.RouteOrigin;
import software.amazon.awssdk.services.ec2.model.RouteTable;
import software.amazon.awssdk.services.ec2.model.RouteTableAssociation;
import software.amazon.awssdk.services.ec2.model.SecurityGroup;
import software.amazon.awssdk.services.ec2.model.Snapshot;
import software.amazon.awssdk.services.ec2.model.Subnet;
import software.amazon.awssdk.services.ec2.model.Tag;
import software.amazon.awssdk.services.ec2.model.UserIdGroupPair;
import software.amazon.awssdk.services.ec2.model.Volume;
import software.amazon.awssdk.services.ec2.model.Vpc;
import software.amazon.awssdk.services.ec2.model.VpcEndpoint;
import software.amazon.awssdk.services.ec2.model.VpcEndpointType;
import software.amazon.awssdk.services.ec2.model.VpcPeeringConnection;
import software.amazon.awssdk.services.ec2.model.VpnGateway;
import software.amazon.awssdk.services.elasticache.model.SecurityGroupMembership;
import software.amazon.awssdk.services.elasticloadbalancingv2.model.AvailabilityZone;
import software.amazon.awssdk.services.emr.EmrClient;
import software.amazon.awssdk.services.emr.model.Application;
import software.amazon.awssdk.services.emr.model.ClusterState;
import software.amazon.awssdk.services.emr.model.ClusterSummary;
import software.amazon.awssdk.services.emr.model.DescribeClusterRequest;
import software.amazon.awssdk.services.rds.model.DBSubnetGroup;
import software.amazon.awssdk.services.rds.model.VpcSecurityGroupMembership;
import software.amazon.awssdk.services.redshift.RedshiftClient;
import software.amazon.awssdk.services.redshift.model.ClusterSubnetGroup;
import software.amazon.awssdk.services.redshift.model.DescribeClusterSubnetGroupsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesRequest;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesResponse;
import software.amazon.awssdk.services.ec2.model.DescribeInternetGatewaysRequest;

/**
 * Class name must refer showServiceAll.
 */
public class Uec2 implements CUtil {

    private List<Subnet> subnetsInVpc = new ArrayList<Subnet>();
    private Map<String, String> subnetToRoutetable = new Hashtable<String, String>();
    private Map<String, String> subnetIdToNaclId = new Hashtable<String, String>();
    private List<NetworkInterface> enisInVpc = new ArrayList<NetworkInterface>();
    private Hashtable<String, String> instanceId2Ec2Type = new Hashtable<String, String>();

    public Map<String, String> getSubnetIdToNaclId(){
        return this.subnetIdToNaclId;
    }

    public Map<String, String> getInstanceId2Ec2Type() {
        return this.instanceId2Ec2Type;
    }

    public Map<String, String> getSubnetToRouteTable() {
        return this.subnetToRoutetable;
    }

    public List<Subnet> getSubnetsInVpc() {
        return this.subnetsInVpc;
    }

    public List<NetworkInterface> getEnisInVpc() {
        return this.enisInVpc;
    }

    public void populateInstanceId2Ec2ARType(CloudWatchClient cw) {
        this.instanceId2Ec2Type.put("ASG","0");
        this.instanceId2Ec2Type.put("AR","0");
        this.instanceId2Ec2Type.put("NONE-ASG-AR","0");
        Iterator<MetricAlarm> iterAlarms = cw.describeAlarmsPaginator(DescribeAlarmsRequest.builder().build())
                .metricAlarms().iterator();
        MetricAlarm ma = null;
        while (iterAlarms.hasNext()) {
            ma = iterAlarms.next();
            if (ma.metricName().startsWith("StatusCheckFailed") && ma.namespace().equals("AWS/EC2")
                    && ma.actionsEnabled()) {
                for (String arn : ma.alarmActions()) {
                    if (arn.endsWith("recover")) {
                        for (Dimension d : ma.dimensions()) {
                            if (d.name().equals("InstanceId")) {
                                instanceId2Ec2Type.put(d.value(), "AR");
                                break;
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    public void populateSubnetToRouteTable(Ec2Client ec2, Filter vpcFilter) {
        Iterator<DescribeRouteTablesResponse> iterRts = ec2
                .describeRouteTablesPaginator(DescribeRouteTablesRequest.builder().filters(vpcFilter).build())
                .iterator();
        while (iterRts.hasNext()) {
            List<RouteTable> rts = iterRts.next().routeTables();
            for (RouteTable rt : rts) {
                List<RouteTableAssociation> rtas = rt.associations();
                for (RouteTableAssociation rta : rtas) {
                    if (rta.main()) {
                        this.subnetToRoutetable.put("main", rta.routeTableId());
                    } else {
                        this.subnetToRoutetable.put(rta.subnetId(), rta.routeTableId());
                    }
                }
            }
        }
    }

    public void populateSubnetsInVpc(Ec2Client ec2, Filter vpcFilter) {
        Iterator<DescribeSubnetsResponse> iterSubnetsInVpc = ec2
                .describeSubnetsPaginator(DescribeSubnetsRequest.builder().filters(vpcFilter).build()).iterator();
        while (iterSubnetsInVpc.hasNext()) {
            List<Subnet> subnets = iterSubnetsInVpc.next().subnets();
            this.subnetsInVpc.addAll(subnets);
        }
    }

    public void populateEnisInVpc(Ec2Client ec2, Filter vpcFilter) {
        Iterator<DescribeNetworkInterfacesResponse> iterEnis = ec2.describeNetworkInterfacesPaginator(
                DescribeNetworkInterfacesRequest.builder().filters(vpcFilter).build()).iterator();
        while (iterEnis.hasNext()) {
            List<NetworkInterface> enis = iterEnis.next().networkInterfaces();
            this.enisInVpc.addAll(enis);
        }
    }

    public static final Uec2 build() {
        return new Uec2();
    }

    @Override
    public void printAllResource(Speaker skBranch) throws Exception {
        Ec2Client ec2 = (Ec2Client) Clients.getClientByServiceClass(Clients.EC2, skBranch.getProfile());
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
                    Filter ssFilter = Filter.builder().name("volume-id").values(v.volumeId()).build();
                    List<Snapshot> ss = ec2
                            .describeSnapshots(DescribeSnapshotsRequest.builder().filters(ssFilter).build())
                            .snapshots();
                    iSpeaker.printResult(true,
                            "ebs: tag-name:" + this.getNameTagValueEc2(v.tags()) + ", size:" + v.size() + "G"
                                    + ", type:" + v.volumeType() + ", iops:" + v.iops() + ", enc:" + v.encrypted()
                                    + ", device:" + v.attachments().get(0).device() + ", snapshot:"
                                    + ((ss.size() == 0) ? "NO" : "YES"));
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
        try {
            Uautoscaling.build().printAllResource(mSpeaker);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Uelasticloadbalancing.build().printAllResource(mSpeaker);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Uelasticloadbalancingv2.build().printAllResource(mSpeaker);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void printVpcResource(String andOrOr, String mode, Speaker skBranch, Filter... filters) throws Exception {
        Speaker mSpeaker = skBranch.clone();
        Ec2Client ec2 = (Ec2Client) Clients.getClientByServiceClass(Clients.EC2, mSpeaker.getProfile());
        Iterator<DescribeVpcsResponse> iterVpcs = null;
        if (andOrOr.equals("OR")) {
            for (Filter f : filters) {
                iterVpcs = ec2.describeVpcsPaginator(DescribeVpcsRequest.builder().filters(f).build()).iterator();
                while (iterVpcs.hasNext()) {
                    List<Vpc> vpcs = iterVpcs.next().vpcs();
                    for (Vpc vpc : vpcs) {
                        this.printVpcToolkit(ec2, vpc.vpcId(), mode, mSpeaker);
                    }
                }
            }
        } else if (andOrOr.equals("AND")) {
            iterVpcs = ec2.describeVpcsPaginator(DescribeVpcsRequest.builder().filters(filters).build()).iterator();
            while (iterVpcs.hasNext()) {
                List<Vpc> vpcs = iterVpcs.next().vpcs();
                for (Vpc vpc : vpcs) {
                    this.printVpcToolkit(ec2, vpc.vpcId(), mode, mSpeaker);
                }
            }
        }
    }

    public void printVpcToolkit(Ec2Client ec2, String vpcId, String mode, Speaker skBranch) {
        Speaker mSpeaker = skBranch.clone();
        Helper hp = new Helper();
        // -------------------- Begin IGW
        mSpeaker.printTitle("Internet Gateway:");
        Filter igwF = this.createFilterEc2("attachment.vpc-id", vpcId);
        List<InternetGateway> igws = ec2
                .describeInternetGateways(DescribeInternetGatewaysRequest.builder().filters(igwF).build())
                .internetGateways();
        int igwCount = 0;
        for (InternetGateway igw : igws) {
            Speaker igwSpeaker = mSpeaker.clone();
            igwSpeaker.smartPrintResult(true, Speaker.GW + " IGW-" + (++igwCount) + ": "
                    + this.getNameTagValueEc2(igw.tags()) + "|" + igw.internetGatewayId());
        }
        mSpeaker.printResultParseDetailsClose(true, "TTL-IGW:" + igwCount + "\n");
        // -------------------- End IGW
        // -------------------- Begin VGW
        mSpeaker.printTitle("Virtual Private Gateway:");
        Filter vgwF = this.createFilterEc2("attachment.vpc-id", vpcId);
        List<VpnGateway> vgws = ec2.describeVpnGateways(DescribeVpnGatewaysRequest.builder().filters(vgwF).build())
                .vpnGateways();
        int vgwCount = 0;
        for (VpnGateway vgw : vgws) {
            Speaker vgwSpeaker = mSpeaker.clone();
            vgwSpeaker.smartPrintResult(true,
                    Speaker.GW + " VGW-" + (++vgwCount) + ": " + this.getNameTagValueEc2(vgw.tags()) + "|"
                            + vgw.vpnGatewayId() + ", " + vgw.type() + ", ASN:" + vgw.amazonSideAsn() + ", "
                            + vgw.state());
        }
        mSpeaker.printResultParseDetailsClose(true, "TTL-VGW:" + vgwCount + "\n");
        // -------------------- End VGW
        // -------------------- Begin Route Table
        mSpeaker.printTitle("Route Table:");
        Filter rtF = this.createFilterEc2("vpc-id", vpcId);
        int rtCount = 0;
        Iterator<DescribeRouteTablesResponse> iterRts = ec2
                .describeRouteTablesPaginator(DescribeRouteTablesRequest.builder().filters(rtF).build()).iterator();
        while (iterRts.hasNext()) {
            List<RouteTable> rts = iterRts.next().routeTables();
            for (RouteTable rt : rts) {
                Speaker rtSpeaker = mSpeaker.clone();
                String mainRt = null;
                if (this.subnetToRoutetable.get("main") != null) {
                    mainRt = this.subnetToRoutetable.get("main");
                }
                if (mainRt != null) {
                    rtSpeaker.smartPrintResult(true,
                            Speaker.RT + " RT-" + (++rtCount) + ": " + this.getNameTagValueEc2(rt.tags()) + "|"
                                    + rt.routeTableId() + ", main:"
                                    + (mainRt.equals(rt.routeTableId()) ? "Yes" : "No"));
                } else {
                    rtSpeaker.smartPrintResult(true, Speaker.RT + " RT-" + (++rtCount) + ": "
                            + this.getNameTagValueEc2(rt.tags()) + "|" + rt.routeTableId());
                }
                List<Route> routes = rt.routes();
                int ruleCount = 0;
                for (Route r : routes) {
                    String target = null;
                    if (r.gatewayId() != null) {
                        target = r.gatewayId();
                    } else if (r.egressOnlyInternetGatewayId() != null) {
                        target = "EOIGW:" + r.egressOnlyInternetGatewayId();
                    } else if (r.instanceId() != null) {
                        target = "EC2:" + r.instanceId();
                    } else if (r.natGatewayId() != null) {
                        target = "NGW:" + r.natGatewayId();
                    } else if (r.networkInterfaceId() != null) {
                        target = "ENI:" + r.networkInterfaceId();
                    } else if (r.transitGatewayId() != null) {
                        target = "TGW:" + r.transitGatewayId();
                    } else if (r.vpcPeeringConnectionId() != null) {
                        target = "VPC:" + r.vpcPeeringConnectionId();
                    }
                    rtSpeaker.printResult(true, "#" + (++ruleCount) + ": "
                            + (r.destinationCidrBlock() == null ? r.destinationPrefixListId()
                                    : r.destinationCidrBlock())
                            + ", " + target + ", " + r.state() + ", propagated:"
                            + (r.origin().equals(RouteOrigin.ENABLE_VGW_ROUTE_PROPAGATION) ? "Yes" : "No"));
                }
            }
        }
        mSpeaker.printResultParseDetailsClose(true, "TTL-Route-Table:" + rtCount + "\n");
        // -------------------- End Route Table
        // -------------------- Begin NACL
        mSpeaker.printTitle("Network ACL:");
        Filter naclF = this.createFilterEc2("vpc-id", vpcId);
        int naclCount = 0;
        Iterator<DescribeNetworkAclsResponse> iterNacls = ec2
                .describeNetworkAclsPaginator(DescribeNetworkAclsRequest.builder().filters(naclF).build()).iterator();
        while (iterNacls.hasNext()) {
            List<NetworkAcl> nacls = iterNacls.next().networkAcls();
            for (NetworkAcl nacl : nacls) {
                Speaker naclSpeaker = mSpeaker.clone();
                naclSpeaker.smartPrintResult(true, Speaker.NACL + " NACL-" + (++naclCount) + ": "
                        + this.decodeNacl(ec2, nacl) + ", default:" + (nacl.isDefault() ? "Yes" : "No"));
                List<NetworkAclEntry> esy = nacl.entries();
                for (NetworkAclEntry e : esy) {
                    naclSpeaker.printResult(true,
                            (!e.egress() ? Speaker.INBOUND : Speaker.OUTBOUND) + " " + e.ruleNumber()
                                    + ", IcmpTypeCode:" + e.icmpTypeCode() + ", " + e.protocol() + ", PortRange:"
                                    + e.portRange() + ", " + (!e.egress() ? "Src: " : "Dsc: ") + e.cidrBlock() + ", "
                                    + e.ruleAction());
                }
                List<NetworkAclAssociation> naas = nacl.associations();
                for (NetworkAclAssociation naa : naas) {
                    this.subnetIdToNaclId.put(naa.subnetId(), naa.networkAclId());
                }
            }
        }
        mSpeaker.printResultParseDetailsClose(true, "TTL-NACL:" + naclCount + "\n");
        // -------------------- End NACL
        // -------------------- Begin SG
        mSpeaker.printTitle("Security Group:");
        int sgCount = 0;
        Filter sgF = this.createFilterEc2("vpc-id", vpcId);
        Iterator<DescribeSecurityGroupsResponse> iterSgs = ec2
                .describeSecurityGroupsPaginator(DescribeSecurityGroupsRequest.builder().filters(sgF).build())
                .iterator();
        while (iterSgs.hasNext()) {
            List<SecurityGroup> sgs = iterSgs.next().securityGroups();
            for (SecurityGroup sg : sgs) {
                Speaker sgSpeaker = mSpeaker.clone();
                sgSpeaker.smartPrintResult(true,
                        Speaker.SG + " SG-" + (++sgCount) + ": " + this.decodeSgById(ec2, sg.groupId()));
                List<IpPermission> inbounds = sg.ipPermissions();
                List<IpPermission> outbounds = sg.ipPermissionsEgress();
                for (IpPermission p : inbounds) {
                    List<UserIdGroupPair> uigps = p.userIdGroupPairs();
                    StringBuffer sbu = new StringBuffer();
                    for (UserIdGroupPair uigp : uigps) {
                        sbu.append(this.sgIdToSgTagOrName(ec2, uigp.groupId()) + ", ");
                    }
                    sgSpeaker.printResult(true, Speaker.INBOUND + " protocol: " + p.ipProtocol() + ", " + p.fromPort()
                            + "-" + p.toPort() + ", Src: "
                            + (p.ipRanges().size() == 0 ? sbu.toString() : this.decodeIpv4Ranges(ec2, p.ipRanges())));
                }
                for (IpPermission p : outbounds) {
                    List<UserIdGroupPair> uigps = p.userIdGroupPairs();
                    StringBuffer sbu = new StringBuffer();
                    for (UserIdGroupPair uigp : uigps) {
                        sbu.append(this.sgIdToSgTagOrName(ec2, uigp.groupId()) + ", ");
                    }
                    sgSpeaker.printResult(true, Speaker.OUTBOUND + " protocol: " + p.ipProtocol() + ", " + p.fromPort()
                            + "-" + p.toPort() + ", Dsc: "
                            + (p.ipRanges().size() == 0 ? sbu.toString() : this.decodeIpv4Ranges(ec2, p.ipRanges())));
                }
            }
        }
        mSpeaker.printResultParseDetailsClose(true, "TTL-SG:" + sgCount + "\n");
        // -------------------- End SG
        // -------------------- Begin Endpoint
        mSpeaker.printTitle("VPC Endpoint - Gateway & Interface:");
        int vpceCount = 0;
        Filter vpcEndpointF = Filter.builder().name("vpc-id").values(vpcId).build();
        Iterator<DescribeVpcEndpointsResponse> iterVpces = ec2
                .describeVpcEndpointsPaginator(DescribeVpcEndpointsRequest.builder().filters(vpcEndpointF).build())
                .iterator();
        while (iterVpces.hasNext()) {
            List<VpcEndpoint> vpces = iterVpces.next().vpcEndpoints();
            for (VpcEndpoint p : vpces) {
                Speaker vpceSpeaker = mSpeaker.clone();
                vpceSpeaker.smartPrintResult(true,
                        Speaker.ENDPOINT + " VPC-Endpoint-" + (++vpceCount) + "-" + p.vpcEndpointTypeAsString() + ": "
                                + this.getNameTagValueEc2(p.tags()) + "|" + p.vpcEndpointId() + ", to-svc:"
                                + p.serviceName() + ", state:" + p.state());
                if (!p.vpcEndpointType().equals(VpcEndpointType.GATEWAY)) {
                    vpceSpeaker.smartPrintResult(true, this.decodeSubnetsById(ec2, p.subnetIds()));
                    List<String> nicids = p.networkInterfaceIds();
                    List<NetworkInterface> enis = ec2
                            .describeNetworkInterfaces(
                                    DescribeNetworkInterfacesRequest.builder().networkInterfaceIds(nicids).build())
                            .networkInterfaces();
                    int iCount = 0;
                    if (mode.equals(PLAIN)) {
                        for (NetworkInterface ni : enis) {
                            Speaker niSpeaker = vpceSpeaker.clone();
                            niSpeaker.smartPrintResult(true, "eni-" + (++iCount) + "-" + ni.interfaceTypeAsString()
                                    + ": " + ni.availabilityZone() + ", primary:{" + ni.privateDnsName() + ", "
                                    + ni.privateIpAddress() + ", "
                                    + (ni.association() == null ? "n/a" : ni.association().publicDnsName()) + ", "
                                    + (ni.association() == null ? "n/a" : ni.association().publicIp()) + "}");
                            Speaker gSpeaker = niSpeaker.clone();
                            gSpeaker.printResult(true,
                                    "protected-by: " + this.groupIdentifierToSgTagOrName(ec2, ni.groups()));
                        }
                    } else {
                        for (NetworkInterface ni : enis) {
                            Speaker niSpeaker = vpceSpeaker.clone();
                            niSpeaker.smartPrintResult(true, "eni-" + (++iCount) + "-" + ni.interfaceTypeAsString()
                                    + ": " + ni.availabilityZone() + ", primary:{" + hp.redact(ni.privateDnsName())
                                    + ", " + hp.redactIp(ni.privateIpAddress()) + ", "
                                    + (ni.association() == null ? "n/a" : hp.redact(ni.association().publicDnsName()))
                                    + ", "
                                    + (ni.association() == null ? "n/a" : hp.redactIp(ni.association().publicIp()))
                                    + "}");
                            Speaker gSpeaker = niSpeaker.clone();
                            gSpeaker.printResult(true,
                                    "protected-by: " + this.groupIdentifierToSgTagOrName(ec2, ni.groups()));
                        }
                    }
                } else if (p.vpcEndpointType().equals(VpcEndpointType.GATEWAY)) {
                    vpceSpeaker.smartPrintResult(true,
                            "published-in: " + this.decodeRouteTablesById(ec2, p.routeTableIds()));
                }
                if (mode.equals(PLAIN)) {
                    for (DnsEntry de : p.dnsEntries()) {
                        vpceSpeaker.printResult(true, "dns: " + de.dnsName());
                    }
                } else {
                    for (DnsEntry de : p.dnsEntries()) {
                        vpceSpeaker.printResult(true, "dns: " + hp.redact(de.dnsName()));
                    }
                }
            }
        }
        mSpeaker.printResultParseDetailsClose(true, "TTL-ENDPOINT:" + vpceCount + "\n");
        // -------------------- End Endpoint
        // -------------------- Begin Peering
        mSpeaker.printTitle("VPC Peering Connection - Requester & Accepter:");
        int vpcprCount = 0;
        Filter filterForVpcPeeringRequester = Filter.builder().name("requester-vpc-info.vpc-id").values(vpcId).build();
        Filter filterForVpcPeeringAccepter = Filter.builder().name("accepter-vpc-info.vpc-id").values(vpcId).build();
        Iterator<DescribeVpcPeeringConnectionsResponse> iterC1s = ec2
                .describeVpcPeeringConnectionsPaginator(
                        DescribeVpcPeeringConnectionsRequest.builder().filters(filterForVpcPeeringRequester).build())
                .iterator();
        while (iterC1s.hasNext()) {
            List<VpcPeeringConnection> c1s = iterC1s.next().vpcPeeringConnections();
            for (VpcPeeringConnection c : c1s) {
                Speaker vpcprSpeaker = mSpeaker.clone();
                vpcprSpeaker.smartPrintResult(true,
                        Speaker.VPCPEER + " PEER-Request-" + (++vpcprCount) + ": " + this.getNameTagValueEc2(c.tags())
                                + ", " + c.vpcPeeringConnectionId() + " to " + c.accepterVpcInfo().cidrBlock() + " of "
                                + c.accepterVpcInfo().vpcId() + ", " + c.status().message());
            }
        }
        int vpcpaCount = 0;
        Iterator<DescribeVpcPeeringConnectionsResponse> iterC2s = ec2
                .describeVpcPeeringConnectionsPaginator(
                        DescribeVpcPeeringConnectionsRequest.builder().filters(filterForVpcPeeringAccepter).build())
                .iterator();
        while (iterC2s.hasNext()) {
            List<VpcPeeringConnection> c2s = iterC2s.next().vpcPeeringConnections();
            for (VpcPeeringConnection c : c2s) {
                Speaker vpcpaSpeaker = mSpeaker.clone();
                vpcpaSpeaker.printResult(true,
                        Speaker.VPCPEER + " PEER-Accept-" + (++vpcpaCount) + ": " + this.getNameTagValueEc2(c.tags())
                                + ", " + c.vpcPeeringConnectionId() + " from " + c.requesterVpcInfo().cidrBlock()
                                + " of " + c.requesterVpcInfo().vpcId() + ", " + c.status().message());
            }
        }
        mSpeaker.printResultParseDetailsClose(true, "TTL-PEERING-REQ:" + vpcprCount);
        mSpeaker.printResult(true, "TTL-PEERING-ACC:" + vpcpaCount + "\n");
        // -------------------- End Peering
    }

    public int printSubnetToolkit(Ec2Client ec2, EmrClient emr, String vpcId, String subnetId, String mode,
            Speaker skBranch) {
        Speaker sSpeaker = skBranch.clone();
        Filter vpcIdFilter = Filter.builder().name("vpc-id").values(vpcId).build();
        Filter subnetIdFilter = Filter.builder().name("subnet-id").values(subnetId).build();
        Helper hp = new Helper();
        // Nat Gateway
        int natCount = 0;
        sSpeaker.printTitle("NAT Gateway:");
        for (NatGateway nat : ec2
                .describeNatGateways(DescribeNatGatewaysRequest.builder().filter(vpcIdFilter, subnetIdFilter).build())
                .natGateways()) {
            Speaker ngSpeaker = sSpeaker.clone();
            ngSpeaker.smartPrintResult(true,
                    Speaker.GW + " NATGW-" + (++natCount) + ": " + this.getNameTagValueEc2(nat.tags()) + ", "
                            + nat.natGatewayId() + ", " + nat.state() + ", provisioned-bandwidth:"
                            + (nat.provisionedBandwidth() == null ? "n/a" : nat.provisionedBandwidth()));
        }
        sSpeaker.printResultParseDetailsClose(true, "TTL-NATGW:" + natCount + "\n");
        // EMR
        int emrCount = 0;
        sSpeaker.printTitle("EMR:");
        Iterator<software.amazon.awssdk.services.emr.model.ListClustersResponse> iterEmrs = emr.listClustersPaginator()
                .iterator();
        while (iterEmrs.hasNext()) {
            List<ClusterSummary> css = iterEmrs.next().clusters();
            for (ClusterSummary cs : css) {
                if (!cs.status().state().equals(ClusterState.TERMINATED)) {
                    software.amazon.awssdk.services.emr.model.Cluster c = emr
                            .describeCluster(DescribeClusterRequest.builder().clusterId(cs.id()).build()).cluster();
                    String ec2SnId = c.ec2InstanceAttributes().ec2SubnetId();
                    if (ec2SnId.equals(subnetId)) {
                        Speaker emrSpeaker = sSpeaker.clone();
                        emrSpeaker.smartPrintResult(true,
                                Speaker.EMR + " EMR-" + (++emrCount) + ": " + cs.name() + ", " + cs.id() + ", release:"
                                        + c.releaseLabel() + ", " + cs.status().stateAsString() + ", security-config:"
                                        + c.securityConfiguration());
                        int fA = 0;
                        for (Application a : c.applications()) {
                            if (fA == 0) {
                                emrSpeaker.printResult(false, "application: " + a.name() + " " + a.version());
                                fA++;
                            }
                            System.out.print(", " + a.name() + " " + a.version());
                        }
                        System.out.println();
                        if (mode.equals(PLAIN)) {
                            emrSpeaker.printResult(true, "master: " + c.masterPublicDnsName());
                        } else {
                            emrSpeaker.printResult(true, "master: " + hp.redact(c.masterPublicDnsName()));
                        }
                        emrSpeaker.printResult(true, "service-role: " + c.serviceRole());
                        emrSpeaker.printResult(true, "auto-scaling-role: " + c.autoScalingRole());
                    }
                }
            }
        }
        sSpeaker.printResultParseDetailsClose(true, "TTL-EMR:" + emrCount + "\n");
        // EC2
        System.out.println("");
        int ec2Count = 0;
        sSpeaker.printTitle("EC2: Auto Scaling = " + Speaker.STAR + " ,    Auto Recovery = " + Speaker.AUTO_RECOVERY_EC2
                + " ,    NONE-AS-AR = " + Speaker.NONE_ASG_AR_EC2);
        int subnetEc2Count = 0;
        Filter vpcInstanceFilter = Filter.builder().name("vpc-id").values(vpcId).build();
        Iterator<DescribeInstancesResponse> iterReservations = ec2
                .describeInstancesPaginator(DescribeInstancesRequest.builder().filters(vpcInstanceFilter).build())
                .iterator();
        while (iterReservations.hasNext()) {
            List<Reservation> ress = iterReservations.next().reservations();
            for (Reservation r : ress) {
                for (Instance i : r.instances()) {
                    Speaker eSpeaker = sSpeaker.clone();
                    if (!i.state().name().equals(InstanceStateName.TERMINATED) && i.subnetId().equals(subnetId)) {
                        subnetEc2Count++;
                        if (mode.equals(PLAIN)) {
                            eSpeaker.smartPrintResult(true,
                                    sSpeaker.getIconForEC2InstanceType(i.instanceId(), this.getInstanceId2Ec2Type())
                                            + "  EC2-" + (++ec2Count) + ": Name:{" + this.getNameTagValueEc2(i.tags())
                                            + "}, " + i.instanceId() + ", " + i.instanceType() + ", sriov:"
                                            + (i.sriovNetSupport() == null ? "n/a" : i.sriovNetSupport()) + ", ena:"
                                            + i.enaSupport() + ", " + i.state().name() + ", " + i.privateIpAddress()
                                            + ", " + (i.publicIpAddress() == null ? "n/a" : i.publicIpAddress()));
                        } else {
                            eSpeaker.smartPrintResult(true,
                                    sSpeaker.getIconForEC2InstanceType(i.instanceId(), this.getInstanceId2Ec2Type())
                                            + "  EC2-" + (++ec2Count) + ": Name:{" + this.getNameTagValueEc2(i.tags())
                                            + "}, " + i.instanceId() + ", " + i.instanceType() + ", sriov:"
                                            + (i.sriovNetSupport() == null ? "n/a" : i.sriovNetSupport()) + ", ena:"
                                            + i.enaSupport() + ", " + i.state().name() + ", "
                                            + hp.redactIp(i.privateIpAddress()) + ", "
                                            + (i.publicIpAddress() == null ? "n/a" : hp.redactIp(i.publicIpAddress())));
                            eSpeaker.printResult(true, "role: " + (i.iamInstanceProfile() == null ? "n/a"
                                    : hp.redactArn(i.iamInstanceProfile().arn())));
                            eSpeaker.printResult(true, "key: " + hp.redact(i.keyName()));
                        }
                        Filter ebsFilter = Filter.builder().name("attachment.instance-id").values(i.instanceId())
                                .build();
                        List<Volume> volumes = ec2
                                .describeVolumes(DescribeVolumesRequest.builder().filters(ebsFilter).build()).volumes();
                        for (Volume v : volumes) {
                            Filter ssFilter = Filter.builder().name("volume-id").values(v.volumeId()).build();
                            List<Snapshot> ss = ec2
                                    .describeSnapshots(DescribeSnapshotsRequest.builder().filters(ssFilter).build())
                                    .snapshots();
                            eSpeaker.printResult(true,
                                    "ebs: tag-name:" + this.getNameTagValueEc2(v.tags()) + ", size:" + v.size() + "G"
                                            + ", type:" + v.volumeType() + ", iops:" + v.iops() + ", enc:"
                                            + v.encrypted() + ", device:" + v.attachments().get(0).device()
                                            + ", snapshot:" + ((ss.size() == 0) ? "NO" : "YES"));
                        }
                        for (NetworkInterface eni : this.getEnisInVpc()) {
                            Speaker eniSpeaker = eSpeaker.clone();
                            if (eni.attachment() != null && eni.attachment().instanceId() != null
                                    && eni.attachment().instanceId().equals(i.instanceId())) {
                                if (mode.equals(PLAIN)) {
                                    eniSpeaker.smartPrintResult(true, "eni-" + eni.attachment().deviceIndex() + ": "
                                            + eni.interfaceType() + ", " + eni.availabilityZone() + ", primary:{"
                                            + eni.privateDnsName() + ", " + eni.privateIpAddress() + ", "
                                            + (eni.association() == null ? "n/a" : eni.association().publicDnsName())
                                            + ", " + (eni.association() == null ? "n/a" : eni.association().publicIp())
                                            + "}");
                                    eniSpeaker.printResult(true,
                                            "protected-by: " + this.groupIdentifierToSgTagOrName(ec2, eni.groups()));
                                    for (NetworkInterfacePrivateIpAddress addr : eni.privateIpAddresses()) {
                                        eniSpeaker.printResult(true,
                                                "address-association: " + addr.privateIpAddress() + ", "
                                                        + (addr.association() != null ? addr.association().publicIp()
                                                                : "no-public-ip"));
                                    }
                                } else {
                                    eniSpeaker.smartPrintResult(true,
                                            "eni-" + eni.attachment().deviceIndex() + ": " + eni.interfaceType() + ", "
                                                    + eni.availabilityZone() + ", primary:{"
                                                    + hp.redact(eni.privateDnsName()) + ", "
                                                    + hp.redactIp(eni.privateIpAddress()) + ", "
                                                    + (eni.association() == null ? "n/a"
                                                            : hp.redact(eni.association().publicDnsName()))
                                                    + ", " + (eni.association() == null ? "n/a"
                                                            : hp.redactIp(eni.association().publicIp()))
                                                    + "}");
                                    eniSpeaker.printResult(true,
                                            "protected-by: " + this.groupIdentifierToSgTagOrName(ec2, eni.groups()));
                                    for (NetworkInterfacePrivateIpAddress addr : eni.privateIpAddresses()) {
                                        eniSpeaker.printResult(true,
                                                "address-association: " + hp.redactIp(addr.privateIpAddress()) + ", "
                                                        + (addr.association() != null
                                                                ? hp.redactIp(addr.association().publicIp())
                                                                : "no-public-ip"));
                                    }
                                }
                            }
                        }
                    }
                }
            } // for EC2
        }
        sSpeaker.printResultParseDetailsClose(true, "TTL-SUBNET-EC2:" + subnetEc2Count);
        return ec2Count;
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