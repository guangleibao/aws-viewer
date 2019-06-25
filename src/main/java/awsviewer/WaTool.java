package awsviewer;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import awsviewer.common.Uec2;
import awsviewer.common.Uelasticloadbalancing;
import awsviewer.common.Uelasticloadbalancingv2;
import awsviewer.conf.Clients;
import awsviewer.conf.General;
import awsviewer.conf.Helper;
import awsviewer.conf.Speaker;
import software.amazon.awssdk.services.autoscaling.AutoScalingClient;
import software.amazon.awssdk.services.autoscaling.model.AutoScalingGroup;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.DescribeAlarmsRequest;
import software.amazon.awssdk.services.cloudwatch.model.Dimension;
import software.amazon.awssdk.services.cloudwatch.model.MetricAlarm;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesRequest;
import software.amazon.awssdk.services.ec2.model.DescribeInternetGatewaysRequest;
import software.amazon.awssdk.services.ec2.model.DescribeNatGatewaysRequest;
import software.amazon.awssdk.services.ec2.model.DescribeNetworkAclsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeNetworkAclsResponse;
import software.amazon.awssdk.services.ec2.model.DescribeNetworkInterfacesRequest;
import software.amazon.awssdk.services.ec2.model.DescribeNetworkInterfacesResponse;
import software.amazon.awssdk.services.ec2.model.DescribeRouteTablesRequest;
import software.amazon.awssdk.services.ec2.model.DescribeRouteTablesResponse;
import software.amazon.awssdk.services.ec2.model.DescribeSecurityGroupsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeSecurityGroupsResponse;
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
import software.amazon.awssdk.services.ec2.model.Instance;
import software.amazon.awssdk.services.ec2.model.InstanceStateName;
import software.amazon.awssdk.services.ec2.model.InternetGateway;
import software.amazon.awssdk.services.ec2.model.IpPermission;
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
import software.amazon.awssdk.services.ec2.model.Subnet;
import software.amazon.awssdk.services.ec2.model.UserIdGroupPair;
import software.amazon.awssdk.services.ec2.model.Volume;
import software.amazon.awssdk.services.ec2.model.Vpc;
import software.amazon.awssdk.services.ec2.model.VpcEndpoint;
import software.amazon.awssdk.services.ec2.model.VpcEndpointType;
import software.amazon.awssdk.services.ec2.model.VpcPeeringConnection;
import software.amazon.awssdk.services.ec2.model.VpnGateway;
import software.amazon.awssdk.services.ecs.EcsClient;
import software.amazon.awssdk.services.ecs.model.ContainerInstance;
import software.amazon.awssdk.services.ecs.model.DescribeClustersRequest;
import software.amazon.awssdk.services.ecs.model.DescribeContainerInstancesRequest;
import software.amazon.awssdk.services.ecs.model.ListClustersRequest;
import software.amazon.awssdk.services.ecs.model.ListClustersResponse;
import software.amazon.awssdk.services.ecs.model.ListContainerInstancesRequest;
import software.amazon.awssdk.services.ecs.model.ListContainerInstancesResponse;
import software.amazon.awssdk.services.elasticache.ElastiCacheClient;
import software.amazon.awssdk.services.elasticache.model.CacheCluster;
import software.amazon.awssdk.services.elasticache.model.CacheSubnetGroup;
import software.amazon.awssdk.services.elasticache.model.DescribeCacheClustersResponse;
import software.amazon.awssdk.services.elasticache.model.DescribeCacheSubnetGroupsRequest;
import software.amazon.awssdk.services.elasticache.model.DescribeCacheSubnetGroupsResponse;
import software.amazon.awssdk.services.elasticache.model.DescribeReplicationGroupsRequest;
import software.amazon.awssdk.services.elasticache.model.ReplicationGroup;
import software.amazon.awssdk.services.elasticloadbalancing.ElasticLoadBalancingClient;
import software.amazon.awssdk.services.elasticloadbalancing.model.ListenerDescription;
import software.amazon.awssdk.services.elasticloadbalancingv2.ElasticLoadBalancingV2Client;
import software.amazon.awssdk.services.elasticloadbalancingv2.model.AvailabilityZone;
import software.amazon.awssdk.services.elasticloadbalancingv2.model.DescribeListenersRequest;
import software.amazon.awssdk.services.elasticloadbalancingv2.model.DescribeLoadBalancersRequest;
import software.amazon.awssdk.services.elasticloadbalancingv2.model.DescribeLoadBalancersResponse;
import software.amazon.awssdk.services.elasticloadbalancingv2.model.Listener;
import software.amazon.awssdk.services.elasticloadbalancingv2.model.LoadBalancer;
import software.amazon.awssdk.services.elasticsearch.ElasticsearchClient;
import software.amazon.awssdk.services.elasticsearch.model.DescribeElasticsearchDomainRequest;
import software.amazon.awssdk.services.elasticsearch.model.DomainInfo;
import software.amazon.awssdk.services.elasticsearch.model.ElasticsearchDomainStatus;
import software.amazon.awssdk.services.elasticsearch.model.ListDomainNamesRequest;
import software.amazon.awssdk.services.emr.EmrClient;
import software.amazon.awssdk.services.emr.model.Application;
import software.amazon.awssdk.services.emr.model.ClusterState;
import software.amazon.awssdk.services.emr.model.ClusterSummary;
import software.amazon.awssdk.services.emr.model.DescribeClusterRequest;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.FunctionConfiguration;
import software.amazon.awssdk.services.lambda.model.ListFunctionsResponse;
import software.amazon.awssdk.services.rds.RdsClient;
import software.amazon.awssdk.services.rds.model.DBInstance;
import software.amazon.awssdk.services.rds.model.DescribeDbInstancesResponse;
import software.amazon.awssdk.services.redshift.RedshiftClient;
import software.amazon.awssdk.services.redshift.model.Cluster;
import software.amazon.awssdk.services.redshift.model.DescribeClustersResponse;

public class WaTool {

    private final Speaker sk = Speaker.getConsoleInstance(null);
    private final Helper hp = new Helper();

    static {
        // TTL preferences.
        java.security.Security.setProperty("networkaddress.cache.ttl", "10");
	}
	
	public static void main(String[] args) throws Exception{
		Logger.getRootLogger().setLevel(Level.OFF);
		
		if(args.length>0 && args[0].equals("showVpc")){
			WaTool wt = new WaTool();
			wt.showVpc(args[1], args[2], args[3]);
		}
		else{
			System.out.println("Usage:");
			System.out.println("showVpc <vpc-name-prefix> <mode: plain|redact> <profile>");
		}
	}

	private void showVpc(String prefix, String mode, String profile) throws Exception {
        // ------------- INIT
        //final String REDACT = "redact"; // Safe option, "redact" is the default option.
        final String PLAIN = "plain";
        General.init(profile);
        Uec2 uec2 = Uec2.build();
        Uelasticloadbalancingv2 uelbv2 = Uelasticloadbalancingv2.build();
        Uelasticloadbalancing uelb = Uelasticloadbalancing.build();
        Filter vpcF = uec2.createFilterEc2("tag:Name", prefix + "*");
        Speaker mSpeaker = this.sk.clone();
        // mSpeaker.smartPrintTitle("Show VPC: ");
        Ec2Client ec2 = (Ec2Client) Clients.getClientByServiceClass(Clients.EC2, profile);
        ElasticLoadBalancingClient elb = (ElasticLoadBalancingClient) Clients
                .getClientByServiceClass(Clients.ELASTICLOADBALANCING, profile);
        ElasticLoadBalancingV2Client elbv2 = (ElasticLoadBalancingV2Client) Clients
                .getClientByServiceClass(Clients.ELASTICLOADBALANCINGV2, profile);
        EcsClient ecs = (EcsClient) Clients.getClientByServiceClass(Clients.ECS, profile);
        LambdaClient lambda = (LambdaClient) Clients.getClientByServiceClass(Clients.LAMBDA, profile);
        ElastiCacheClient cache = (ElastiCacheClient) Clients.getClientByServiceClass(Clients.ELASTICACHE, profile);
        RdsClient rds = (RdsClient) Clients.getClientByServiceClass(Clients.RDS, profile);
        RedshiftClient rs = (RedshiftClient) Clients.getClientByServiceClass(Clients.REDSHIFT, profile);
        EmrClient emr = (EmrClient) Clients.getClientByServiceClass(Clients.EMR, profile);
        ElasticsearchClient es = (ElasticsearchClient) Clients.getClientByServiceClass(Clients.ELASTICSEARCH, profile);
        AutoScalingClient asg = (AutoScalingClient) Clients.getClientByServiceClass(Clients.AUTOSCALING, profile);
        CloudWatchClient cw = (CloudWatchClient) Clients.getClientByServiceClass(Clients.CLOUDWATCH, profile);
        // -------------- Begin VPC
        // List<Vpc> vpcs =
        // ec2.describeVpcs(DescribeVpcsRequest.builder().filters(vpcF).build()).vpcs();
        // VPC Paginator
        Iterator<DescribeVpcsResponse> iterVpcs = ec2
                .describeVpcsPaginator(DescribeVpcsRequest.builder().filters(vpcF).build()).iterator();
        while (iterVpcs.hasNext()) {
            List<Vpc> vpcs = iterVpcs.next().vpcs();
            for (Vpc vpc : vpcs) {
                Filter subnetF = Filter.builder().name("vpc-id").values(vpc.vpcId()).build();
                Filter eniF = Filter.builder().name("vpc-id").values(vpc.vpcId()).build();
                List<Subnet> subnetsInVpc = new ArrayList<Subnet>();
                List<NetworkInterface> enisInVpc = new ArrayList<NetworkInterface>();
                Iterator<DescribeSubnetsResponse> iterSubnetsInVpc = ec2.describeSubnetsPaginator(DescribeSubnetsRequest.builder().filters(subnetF).build()).iterator();
                while(iterSubnetsInVpc.hasNext()){
                    List<Subnet> subnets = iterSubnetsInVpc.next().subnets();
                    subnetsInVpc.addAll(subnets);
                }
                Iterator<DescribeNetworkInterfacesResponse> iterEnis = ec2.describeNetworkInterfacesPaginator(DescribeNetworkInterfacesRequest.builder().filters(eniF).build()).iterator();
                while(iterEnis.hasNext()){
                    List<NetworkInterface> enis = iterEnis.next().networkInterfaces();
                    enisInVpc.addAll(enis);
                }
                Speaker vSpeaker = mSpeaker.clone();
                String vpcName = uec2.getNameTagValueEc2(vpc.tags());
                vSpeaker.smartPrintTitle("VPC: " + vpcName + "|" + vpc.vpcId() + ", " + vpc.cidrBlock() + ", tenancy: "
                        + vpc.instanceTenancy());
                // -------------------- Begin IGW
                vSpeaker.printTitle("Internet Gateway:");
                Filter igwF = uec2.createFilterEc2("attachment.vpc-id", vpc.vpcId());
                List<InternetGateway> igws = ec2
                        .describeInternetGateways(DescribeInternetGatewaysRequest.builder().filters(igwF).build())
                        .internetGateways();
                int igwCount = 0;
                for (InternetGateway igw : igws) {
                    Speaker igwSpeaker = vSpeaker.clone();
                    igwSpeaker.smartPrintResult(true, Speaker.GW + " IGW-" + (++igwCount) + ": "
                            + uec2.getNameTagValueEc2(igw.tags()) + "|" + igw.internetGatewayId());
                }
                vSpeaker.printResult(true, "#TTL-IGW:" + igwCount + "\n");
                // -------------------- End IGW
                // -------------------- Begin VGW
                vSpeaker.printTitle("Virtual Private Gateway:");
                Filter vgwF = uec2.createFilterEc2("attachment.vpc-id", vpc.vpcId());
                List<VpnGateway> vgws = ec2
                        .describeVpnGateways(DescribeVpnGatewaysRequest.builder().filters(vgwF).build()).vpnGateways();
                int vgwCount = 0;
                for (VpnGateway vgw : vgws) {
                    Speaker vgwSpeaker = vSpeaker.clone();
                    vgwSpeaker.smartPrintResult(true,
                            Speaker.GW + " VGW-" + (++vgwCount) + ": " + uec2.getNameTagValueEc2(vgw.tags()) + "|"
                                    + vgw.vpnGatewayId() + ", " + vgw.type() + ", ASN:" + vgw.amazonSideAsn() + ", "
                                    + vgw.state());
                }
                vSpeaker.printResult(true, "#TTL-VGW:" + vgwCount + "\n");
                // -------------------- End VGW
                // -------------------- Begin Route Table
                vSpeaker.printTitle("Route Table:");
                Filter rtF = uec2.createFilterEc2("vpc-id", vpc.vpcId());
                int rtCount = 0;
                Map<String, String> subnetToRoutetable = new Hashtable<String, String>();
                Iterator<DescribeRouteTablesResponse> iterRts = ec2
                        .describeRouteTablesPaginator(DescribeRouteTablesRequest.builder().filters(rtF).build())
                        .iterator();
                while (iterRts.hasNext()) {
                    List<RouteTable> rts = iterRts.next().routeTables();
                    for (RouteTable rt : rts) {
                        List<RouteTableAssociation> rtas = rt.associations();
                        for (RouteTableAssociation rta : rtas) {
                            if (rta.main()) {
                                subnetToRoutetable.put("main", rta.routeTableId());
                            } else {
                                subnetToRoutetable.put(rta.subnetId(), rta.routeTableId());
                            }
                        }
                        Speaker rtSpeaker = vSpeaker.clone();
                        String mainRt = null;
                        if (subnetToRoutetable.get("main") != null) {
                            mainRt = subnetToRoutetable.get("main");
                        }
                        rtSpeaker.smartPrintResult(true,
                                Speaker.RT + " RT-" + (++rtCount) + ": " + uec2.getNameTagValueEc2(rt.tags()) + "|"
                                        + rt.routeTableId() + ", main:"
                                        + (mainRt.equals(rt.routeTableId()) ? "Yes" : "No"));
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
                vSpeaker.printResult(true, "#TTL-Route-Table:" + rtCount + "\n");
                // -------------------- End Route Table
                // -------------------- Begin NACL
                vSpeaker.printTitle("Network ACL:");
                Filter naclF = uec2.createFilterEc2("vpc-id", vpc.vpcId());
                int naclCount = 0;
                Iterator<DescribeNetworkAclsResponse> iterNacls = ec2
                        .describeNetworkAclsPaginator(DescribeNetworkAclsRequest.builder().filters(naclF).build())
                        .iterator();
                Map<String, String> subnetIdToNaclId = new Hashtable<String, String>();
                while (iterNacls.hasNext()) {
                    List<NetworkAcl> nacls = iterNacls.next().networkAcls();
                    for (NetworkAcl nacl : nacls) {
                        Speaker naclSpeaker = vSpeaker.clone();
                        naclSpeaker.smartPrintResult(true, Speaker.NACL + " NACL-" + (++naclCount) + ": "
                                + uec2.decodeNacl(ec2, nacl) + ", default:" + (nacl.isDefault() ? "Yes" : "No"));
                        List<NetworkAclEntry> esy = nacl.entries();
                        for (NetworkAclEntry e : esy) {
                            naclSpeaker.printResult(true,
                                    (!e.egress() ? Speaker.INBOUND : Speaker.OUTBOUND) + " " + e.ruleNumber()
                                            + ", IcmpTypeCode:" + e.icmpTypeCode() + ", " + e.protocol()
                                            + ", PortRange:" + e.portRange() + ", " + (!e.egress() ? "Src: " : "Dsc: ")
                                            + e.cidrBlock() + ", " + e.ruleAction());
                        }
                        List<NetworkAclAssociation> naas = nacl.associations();
                        for (NetworkAclAssociation naa : naas) {
                            subnetIdToNaclId.put(naa.subnetId(), naa.networkAclId());
                        }
                    }
                }
                vSpeaker.printResult(true, "#TTL-NACL:" + naclCount + "\n");
                // -------------------- End NACL
                // -------------------- Begin SG
                vSpeaker.printTitle("Security Group:");
                int sgCount = 0;
                Filter sgF = uec2.createFilterEc2("vpc-id", vpc.vpcId());
                Iterator<DescribeSecurityGroupsResponse> iterSgs = ec2
                        .describeSecurityGroupsPaginator(DescribeSecurityGroupsRequest.builder().filters(sgF).build())
                        .iterator();
                while (iterSgs.hasNext()) {
                    List<SecurityGroup> sgs = iterSgs.next().securityGroups();
                    for (SecurityGroup sg : sgs) {
                        Speaker sgSpeaker = vSpeaker.clone();
                        sgSpeaker.smartPrintResult(true,
                                Speaker.SG + " SG-" + (++sgCount) + ": " + uec2.decodeSgById(ec2, sg.groupId()));
                        List<IpPermission> inbounds = sg.ipPermissions();
                        List<IpPermission> outbounds = sg.ipPermissionsEgress();
                        for (IpPermission p : inbounds) {
                            List<UserIdGroupPair> uigps = p.userIdGroupPairs();
                            StringBuffer sbu = new StringBuffer();
                            for (UserIdGroupPair uigp : uigps) {
                                sbu.append(uec2.sgIdToSgTagOrName(ec2, uigp.groupId()) + ", ");
                            }
                            sgSpeaker.printResult(true,
                                    Speaker.INBOUND + " protocol: " + p.ipProtocol() + ", " + p.fromPort() + "-"
                                            + p.toPort() + ", Src: " + (p.ipRanges().size() == 0 ? sbu.toString()
                                                    : uec2.decodeIpv4Ranges(ec2, p.ipRanges())));
                        }
                        for (IpPermission p : outbounds) {
                            List<UserIdGroupPair> uigps = p.userIdGroupPairs();
                            StringBuffer sbu = new StringBuffer();
                            for (UserIdGroupPair uigp : uigps) {
                                sbu.append(uec2.sgIdToSgTagOrName(ec2, uigp.groupId()) + ", ");
                            }
                            sgSpeaker.printResult(true,
                                    Speaker.OUTBOUND + " protocol: " + p.ipProtocol() + ", " + p.fromPort() + "-"
                                            + p.toPort() + ", Dsc: " + (p.ipRanges().size() == 0 ? sbu.toString()
                                                    : uec2.decodeIpv4Ranges(ec2, p.ipRanges())));
                        }
                    }
                }
                vSpeaker.printResult(true, "#TTL-SG:" + sgCount + "\n");
                // -------------------- End SG
                // -------------------- Begin ELB v2
                vSpeaker.printTitle("Elastic Load Balancing V2 - Application & Network:");
                int elbv2Count = 0;
                // List<LoadBalancer> lbv2s =
                // elbv2.describeLoadBalancers(DescribeLoadBalancersRequest.builder().build()).loadBalancers();
                Iterator<DescribeLoadBalancersResponse> iterLbv2s = elbv2
                        .describeLoadBalancersPaginator(DescribeLoadBalancersRequest.builder().build()).iterator();
                while (iterLbv2s.hasNext()) {
                    List<LoadBalancer> lbv2s = iterLbv2s.next().loadBalancers();
                    for (LoadBalancer l2 : lbv2s) {
                        Speaker lbv2Speaker = vSpeaker.clone();
                        if (l2.vpcId().equals(vpc.vpcId())) {
                            List<AvailabilityZone> azs = l2.availabilityZones();
                            List<Listener> ls = elbv2.describeListeners(
                                    DescribeListenersRequest.builder().loadBalancerArn(l2.loadBalancerArn()).build())
                                    .listeners();
                            if (mode.equals(PLAIN)) {
                                lbv2Speaker.smartPrintResult(true,
                                        Speaker.ELB + " ELB-" + (++elbv2Count) + "-" + l2.type() + ": "
                                                + l2.loadBalancerName() + ", " + l2.schemeAsString() + ", "
                                                + l2.ipAddressTypeAsString() + ", " + l2.state().codeAsString() + ", "
                                                + l2.dnsName());
                                lbv2Speaker.smartPrintResult(true, uec2.decodeElbv2AZs(ec2, azs));
                            } else {
                                lbv2Speaker.smartPrintResult(true,
                                        Speaker.ELB + " ELB-" + (++elbv2Count) + "-" + l2.type() + ": "
                                                + l2.loadBalancerName() + ", " + l2.schemeAsString() + ", "
                                                + l2.ipAddressTypeAsString() + ", " + l2.state().codeAsString()
                                                + ", dns: " + hp.redact(l2.dnsName()));
                                lbv2Speaker.smartPrintResult(true, uec2.decodeElbv2AZs(ec2, azs));
                            }
                            for (Listener l : ls) {
                                lbv2Speaker.printResult(true, "listener: " + l.protocolAsString() + ", " + l.port()
                                        + ", ssl:" + l.sslPolicy());
                            }
                            lbv2Speaker.printResult(true, uelbv2.getAttributes(elbv2, l2.loadBalancerArn(), 0, 3));
                            lbv2Speaker.printResult(true, uelbv2.getAttributes(elbv2, l2.loadBalancerArn(), 3, 100));
                        }
                    }
                }
                vSpeaker.printResult(true, "#TTL-ELBv2:" + elbv2Count + "\n");
                // -------------------- End ELB v2
                // -------------------- Begin ELB
                vSpeaker.printTitle("Elastic Load Balancing - Classic:");
                int elbCount = 0;
                Iterator<software.amazon.awssdk.services.elasticloadbalancing.model.DescribeLoadBalancersResponse> iterLbs = elb
                        .describeLoadBalancersPaginator(
                                software.amazon.awssdk.services.elasticloadbalancing.model.DescribeLoadBalancersRequest
                                        .builder().build())
                        .iterator();
                while (iterLbs.hasNext()) {
                    List<software.amazon.awssdk.services.elasticloadbalancing.model.LoadBalancerDescription> lbs = iterLbs
                            .next().loadBalancerDescriptions();
                    for (software.amazon.awssdk.services.elasticloadbalancing.model.LoadBalancerDescription desc : lbs) {
                        List<ListenerDescription> lds = desc.listenerDescriptions();
                        Speaker lbSpeaker = vSpeaker.clone();
                        if (desc.vpcId().equals(vpc.vpcId())) {
                            List<String> subnets = desc.subnets();
                            if (mode.equals(PLAIN)) {
                                lbSpeaker.smartPrintResult(true,
                                        Speaker.ELB + " ELB-" + (++elbCount) + "-classic: " + desc.loadBalancerName()
                                                + ", " + desc.scheme() + ", " + ", dns: " + desc.dnsName());
                                lbSpeaker.smartPrintResult(true, uec2.decodeSubnetsById(ec2, subnets));
                                for (ListenerDescription ld : lds) {
                                    lbSpeaker.printResult(true,
                                            "listener: " + ld.listener().protocol() + ", "
                                                    + ld.listener().loadBalancerPort() + ", ssl:"
                                                    + ld.listener().sslCertificateId());
                                }
                            } else {
                                lbSpeaker.smartPrintResult(true,
                                        Speaker.ELB + " ELB-" + (++elbCount) + "-classic: " + desc.loadBalancerName()
                                                + ", " + desc.scheme() + ", " + ", dns: " + hp.redact(desc.dnsName()));
                                lbSpeaker.smartPrintResult(true, uec2.decodeSubnetsById(ec2, subnets));
                                for (ListenerDescription ld : lds) {
                                    lbSpeaker.printResult(true,
                                            "listener: " + ld.listener().protocol() + ", "
                                                    + ld.listener().loadBalancerPort() + ", ssl:"
                                                    + ld.listener().sslCertificateId());
                                }
                            }
                            lbSpeaker.printResult(true, uelb.getAttributes(elb, desc.loadBalancerName()));
                        }
                    }
                }
                vSpeaker.printResult(true, "#TTL-ELB:" + elbCount + "\n");
                // -------------------- End ELB
                // -------------------- Begin Endpoint
                vSpeaker.printTitle("VPC Endpoint - Gateway & Interface:");
                int vpceCount = 0;
                Filter vpcEndpointF = Filter.builder().name("vpc-id").values(vpc.vpcId()).build();
                Iterator<DescribeVpcEndpointsResponse> iterVpces = ec2.describeVpcEndpointsPaginator(
                        DescribeVpcEndpointsRequest.builder().filters(vpcEndpointF).build()).iterator();
                while (iterVpces.hasNext()) {
                    List<VpcEndpoint> vpces = iterVpces.next().vpcEndpoints();
                    for (VpcEndpoint p : vpces) {
                        Speaker vpceSpeaker = vSpeaker.clone();
                        vpceSpeaker.smartPrintResult(true,
                                Speaker.ENDPOINT + " VPC-Endpoint-" + (++vpceCount) + "-" + p.vpcEndpointTypeAsString()
                                        + ": " + uec2.getNameTagValueEc2(p.tags()) + "|" + p.vpcEndpointId()
                                        + ", to-svc:" + p.serviceName() + ", state:" + p.state());
                        if (!p.vpcEndpointType().equals(VpcEndpointType.GATEWAY)) {
                            vpceSpeaker.smartPrintResult(true, uec2.decodeSubnetsById(ec2, p.subnetIds()));
                            List<String> nicids = p.networkInterfaceIds();
                            List<NetworkInterface> enis = ec2.describeNetworkInterfaces(
                                    DescribeNetworkInterfacesRequest.builder().networkInterfaceIds(nicids).build())
                                    .networkInterfaces();
                            int iCount = 0;
                            if (mode.equals(PLAIN)) {
                                for (NetworkInterface ni : enis) {
                                    Speaker niSpeaker = vpceSpeaker.clone();
                                    niSpeaker.smartPrintResult(true, "eni-" + (++iCount) + "-"
                                            + ni.interfaceTypeAsString() + ": " + ni.availabilityZone() + ", primary:{"
                                            + ni.privateDnsName() + ", " + ni.privateIpAddress() + ", "
                                            + (ni.association() == null ? "n/a" : ni.association().publicDnsName())
                                            + ", " + (ni.association() == null ? "n/a" : ni.association().publicIp())
                                            + "}");
                                    Speaker gSpeaker = niSpeaker.clone();
                                    gSpeaker.printResult(true,
                                            "protected-by: " + uec2.groupIdentifierToSgTagOrName(ec2, ni.groups()));
                                }
                            } else {
                                for (NetworkInterface ni : enis) {
                                    Speaker niSpeaker = vpceSpeaker.clone();
                                    niSpeaker.smartPrintResult(true,
                                            "eni-" + (++iCount) + "-" + ni.interfaceTypeAsString() + ": "
                                                    + ni.availabilityZone() + ", primary:{"
                                                    + hp.redact(ni.privateDnsName()) + ", "
                                                    + hp.redactIp(ni.privateIpAddress()) + ", "
                                                    + (ni.association() == null ? "n/a"
                                                            : hp.redact(ni.association().publicDnsName()))
                                                    + ", " + (ni.association() == null ? "n/a"
                                                            : hp.redactIp(ni.association().publicIp()))
                                                    + "}");
                                    Speaker gSpeaker = niSpeaker.clone();
                                    gSpeaker.printResult(true,
                                            "protected-by: " + uec2.groupIdentifierToSgTagOrName(ec2, ni.groups()));
                                }
                            }
                        } else if (p.vpcEndpointType().equals(VpcEndpointType.GATEWAY)) {
                            vpceSpeaker.smartPrintResult(true,
                                    "published-in: " + uec2.decodeRouteTablesById(ec2, p.routeTableIds()));
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
                vSpeaker.printResult(true, "#TTL-ENDPOINT:" + vpceCount + "\n");
                // -------------------- End Endpoint
                // -------------------- Begin Peering
                vSpeaker.printTitle("VPC Peering Connection - Requester & Accepter:");
                int vpcprCount = 0;
                Filter filterForVpcPeeringRequester = Filter.builder().name("requester-vpc-info.vpc-id")
                        .values(vpc.vpcId()).build();
                Filter filterForVpcPeeringAccepter = Filter.builder().name("accepter-vpc-info.vpc-id")
                        .values(vpc.vpcId()).build();
                Iterator<DescribeVpcPeeringConnectionsResponse> iterC1s = ec2.describeVpcPeeringConnectionsPaginator(
                        DescribeVpcPeeringConnectionsRequest.builder().filters(filterForVpcPeeringRequester).build())
                        .iterator();
                while (iterC1s.hasNext()) {
                    List<VpcPeeringConnection> c1s = iterC1s.next().vpcPeeringConnections();
                    for (VpcPeeringConnection c : c1s) {
                        Speaker vpcprSpeaker = vSpeaker.clone();
                        vpcprSpeaker.smartPrintResult(true,
                                Speaker.VPCPEER + " PEER-Request-" + (++vpcprCount) + ": "
                                        + uec2.getNameTagValueEc2(c.tags()) + ", " + c.vpcPeeringConnectionId() + " to "
                                        + c.accepterVpcInfo().cidrBlock() + " of " + c.accepterVpcInfo().vpcId() + ", "
                                        + c.status().message());
                    }
                }
                int vpcpaCount = 0;
                Iterator<DescribeVpcPeeringConnectionsResponse> iterC2s = ec2.describeVpcPeeringConnectionsPaginator(
                        DescribeVpcPeeringConnectionsRequest.builder().filters(filterForVpcPeeringAccepter).build())
                        .iterator();
                while (iterC2s.hasNext()) {
                    List<VpcPeeringConnection> c2s = iterC2s.next().vpcPeeringConnections();
                    for (VpcPeeringConnection c : c2s) {
                        Speaker vpcpaSpeaker = vSpeaker.clone();
                        vpcpaSpeaker.printResult(true,
                                Speaker.VPCPEER + " PEER-Accept-" + (++vpcpaCount) + ": "
                                        + uec2.getNameTagValueEc2(c.tags()) + ", " + c.vpcPeeringConnectionId()
                                        + " from " + c.requesterVpcInfo().cidrBlock() + " of "
                                        + c.requesterVpcInfo().vpcId() + ", " + c.status().message());
                    }
                }
                vSpeaker.printResult(true, "#TTL-PEERING-REQ:" + vpcprCount);
                vSpeaker.printResult(true, "#TTL-PEERING-ACC:" + vpcpaCount + "\n");
                // -------------------- End Peering
                // -------------------- Begin ECS
                List<String> validClusterArn = new ArrayList<String>();
                vSpeaker.printTitle("Elastic Container Service:");
                int ecsCount = 0;
                Iterator<ListClustersResponse> iterClusters = ecs
                        .listClustersPaginator(ListClustersRequest.builder().build()).iterator();
                while (iterClusters.hasNext()) {
                    List<String> clusterArns = iterClusters.next().clusterArns();
                    for (String cArn : clusterArns) {
                        software.amazon.awssdk.services.ecs.model.Cluster ec = ecs
                                .describeClusters(DescribeClustersRequest.builder().clusters(cArn).build()).clusters()
                                .get(0);
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
                                for (Reservation r : ec2.describeInstances(
                                        DescribeInstancesRequest.builder().instanceIds(ci.ec2InstanceId()).build())
                                        .reservations()) {
                                    for (Instance i : r.instances()) {
                                        if (!i.state().name().equals(InstanceStateName.TERMINATED)) {
                                            for (NetworkInterface eni : enisInVpc) {
                                                if (eni.attachment() != null && eni.attachment().instanceId() != null
                                                        && eni.attachment().instanceId().equals(i.instanceId())) {
                                                    String subnetId = eni.subnetId();
                                                    String vpcId = ec2
                                                            .describeSubnets(DescribeSubnetsRequest.builder()
                                                                    .subnetIds(subnetId).build())
                                                            .subnets().get(0).vpcId();
                                                    if (vpcId.equals(vpc.vpcId())) {
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
                Iterator<ListClustersResponse> iterClustersRound2 = ecs
                        .listClustersPaginator(ListClustersRequest.builder().build()).iterator();
                while (iterClustersRound2.hasNext()) {
                    List<String> clusterArns = iterClustersRound2.next().clusterArns();
                    for (String cArn : clusterArns) {
                        if (!validClusterArn.contains(cArn)) {
                            continue;
                        }
                        Speaker ecsSpeaker = vSpeaker.clone();
                        software.amazon.awssdk.services.ecs.model.Cluster ec = ecs
                                .describeClusters(DescribeClustersRequest.builder().clusters(cArn).build()).clusters()
                                .get(0);
                        ecsSpeaker.smartPrintResult(true,
                                "ECS-" + (++ecsCount) + ": " + ec.clusterName() + ", " + ec.status() + ", instances:"
                                        + ec.registeredContainerInstancesCount() + ", service:"
                                        + ec.activeServicesCount() + ", task(run/pending):" + ec.runningTasksCount()
                                        + "/" + ec.pendingTasksCount());
                        Iterator<ListContainerInstancesResponse> iterContainerInstanceArns = ecs.listContainerInstancesPaginator(
                                        ListContainerInstancesRequest.builder().cluster(ec.clusterName()).build()).iterator();
                        while(iterContainerInstanceArns.hasNext()){
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
                                    for (Reservation r : ec2.describeInstances(
                                            DescribeInstancesRequest.builder().instanceIds(ci.ec2InstanceId()).build())
                                            .reservations()) {
                                        for (Instance i : r.instances()) {
                                            if (!i.state().name().equals(InstanceStateName.TERMINATED)) {
                                                if (mode.equals(PLAIN)) {
                                                    aSpeaker.smartPrintResult(true, "Name:{"
                                                            + uec2.getNameTagValueEc2(i.tags()) + "}, " + i.instanceId()
                                                            + ", " + i.instanceType() + ", sriov:"
                                                            + (i.sriovNetSupport() == null ? "n/a" : i.sriovNetSupport())
                                                            + ", ena:" + i.enaSupport() + ", " + i.state().name() + ", "
                                                            + i.privateIpAddress() + ", "
                                                            + (i.publicIpAddress() == null ? "n/a" : i.publicIpAddress()));
                                                } else {
                                                    aSpeaker.smartPrintResult(true,
                                                            "Name:{" + uec2.getNameTagValueEc2(i.tags()) + "}, "
                                                                    + i.instanceId() + ", " + i.instanceType() + ", sriov:"
                                                                    + (i.sriovNetSupport() == null ? "n/a"
                                                                            : i.sriovNetSupport())
                                                                    + ", ena:" + i.enaSupport() + ", " + i.state().name()
                                                                    + ", " + hp.redactIp(i.privateIpAddress()) + ", "
                                                                    + (i.publicIpAddress() == null ? "n/a"
                                                                            : hp.redactIp(i.publicIpAddress())));
                                                }
                                                for (NetworkInterface eni : enisInVpc) {
                                                    Speaker eniSpeaker = aSpeaker.clone();
                                                    if (eni.attachment() != null && eni.attachment().instanceId() != null
                                                            && eni.attachment().instanceId().equals(i.instanceId())) {
                                                        eniSpeaker.smartPrintResult(true,
                                                                "eni-" + eni.attachment().deviceIndex() + ": "
                                                                        + eni.interfaceType() + ", "
                                                                        + eni.availabilityZone());
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
                vSpeaker.printResult(true, "#TTL-ECS:" + ecsCount + "\n");
                // -------------------- End ECS
                // -------------------- Begin ES
                vSpeaker.printTitle("ElasticSearch:");
                int esCount = 0;
                List<DomainInfo> dis = es.listDomainNames(ListDomainNamesRequest.builder().build()).domainNames();
                for (DomainInfo di : dis) {
                    Speaker esSpeaker = vSpeaker.clone();
                    ElasticsearchDomainStatus esde = es
                            .describeElasticsearchDomain(
                                    DescribeElasticsearchDomainRequest.builder().domainName(di.domainName()).build())
                            .domainStatus();
                    if (esde.vpcOptions().vpcId().equals(vpc.vpcId())) {
                        esSpeaker.smartPrintResult(true,
                                Speaker.ES + " ES-Domain-" + (++esCount) + ": " + di.domainName() + ", "
                                        + esde.elasticsearchVersion() + ", master-count:"
                                        + esde.elasticsearchClusterConfig().dedicatedMasterCount() + ", master-type:"
                                        + esde.elasticsearchClusterConfig().dedicatedMasterType() + ", instance-count:"
                                        + esde.elasticsearchClusterConfig().instanceCount() + ", instance-type:"
                                        + esde.elasticsearchClusterConfig().instanceType() + ", "
                                        + esde.ebsOptions().volumeType() + ", " + esde.ebsOptions().volumeSize() + "Gx"
                                        + esde.elasticsearchClusterConfig().instanceCount() + ", iops:"
                                        + esde.ebsOptions().iops());
                        for (String ename : esde.endpoints().keySet()) {
                            String endpoint = esde.endpoints().get(ename);
                            if (mode.equals(PLAIN)) {
                                esSpeaker.printResult(true, "endpoint-" + ename + ": " + endpoint);
                            } else {
                                esSpeaker.printResult(true, "endpoint-" + ename + ": " + hp.redact(endpoint));
                            }
                        }
                        esSpeaker.printResult(true,
                                "protected-by: " + uec2.decodeSgsByIds(ec2, esde.vpcOptions().securityGroupIds()));
                        esSpeaker.printResult(true, uec2.decodeSubnetsById(ec2, esde.vpcOptions().subnetIds()));
                    }
                }
                vSpeaker.printResult(true, "#TTL-ES:" + esCount + "\n");
                // -------------------- End ES
                // -------------------- Begin Lambda
                vSpeaker.printTitle("Lambda:");
                int lCount = 0;
                //List<FunctionConfiguration> fcs = lambda.listFunctions().functions();
                Iterator<ListFunctionsResponse> iterFcs = lambda.listFunctionsPaginator().iterator();
                while(iterFcs.hasNext()){
                    List<FunctionConfiguration> fcs = iterFcs.next().functions();
                    for (FunctionConfiguration fc : fcs) {
                        Speaker fSpeaker = vSpeaker.clone();
                        if (fc.vpcConfig() != null && fc.vpcConfig().vpcId().equals(vpc.vpcId())) {
                            if (mode.equals(PLAIN)) {
                                fSpeaker.smartPrintResult(true,
                                        Speaker.LAMBDA + " LAMBDA-" + (++lCount) + ": " + fc.functionName() + ", "
                                                + fc.functionArn() + ", " + fc.version() + ", runtime:" + fc.runtime()
                                                + ", handler:" + fc.handler() + ", mem:" + fc.memorySize() + "m, ttl:"
                                                + fc.timeout() + "s");
                            } else {
                                fSpeaker.smartPrintResult(true,
                                        Speaker.LAMBDA + " LAMBDA-" + (++lCount) + ": " + fc.functionName() + ", "
                                                + hp.redactArn(fc.functionArn()) + ", " + fc.version() + ", runtime:"
                                                + fc.runtime() + ", handler:" + fc.handler() + ", mem:" + fc.memorySize()
                                                + "m, ttl:" + fc.timeout() + "s");
                            }
                            fSpeaker.printResult(true, "role: " + fc.role());
                            fSpeaker.printResult(true,
                                    "protected-by: " + uec2.decodeSgsByIds(ec2, fc.vpcConfig().securityGroupIds()));
                            fSpeaker.printResult(true, uec2.decodeSubnetsById(ec2, fc.vpcConfig().subnetIds()));
                        }
                    }
                }
                vSpeaker.printResult(true, "#TTL-LAMBDA:" + lCount + "\n");
                // -------------------- End Lambda
                // -------------------- Begin ElastiCache
                vSpeaker.printTitle("ElastiCache:");
                int mdCount = 0;
                int rdCount = 0;
                Map<String, String> cacheSubnetGroupNameToSubnetAzs = new Hashtable<String, String>();
                Iterator<DescribeCacheSubnetGroupsResponse> iterCacheSubnetGroups = cache.describeCacheSubnetGroupsPaginator(DescribeCacheSubnetGroupsRequest.builder().build()).iterator();
                while(iterCacheSubnetGroups.hasNext()){
                    List<CacheSubnetGroup> cacheSubnetGroups = iterCacheSubnetGroups.next().cacheSubnetGroups();
                    for (CacheSubnetGroup csg : cacheSubnetGroups) {
                        if (csg.vpcId().equals(vpc.vpcId())) {
                            cacheSubnetGroupNameToSubnetAzs.put(csg.cacheSubnetGroupName(),
                                    uec2.decodeCacheSubnets(ec2, csg.subnets()));
                        }
                    }
                }
                String repGroupId = null;
                String cddGroupId = null;
                Iterator<DescribeCacheClustersResponse> iterCacheClusters = cache.describeCacheClustersPaginator().iterator();
                while(iterCacheClusters.hasNext()){
                    List<CacheCluster> cacheClusters = iterCacheClusters.next().cacheClusters();
                    for (CacheCluster cc : cacheClusters) {
                        Speaker ccSpeaker = vSpeaker.clone();
                        String engine = cc.engine();
                        if (engine.equals("redis")
                                && cacheSubnetGroupNameToSubnetAzs.containsKey(cc.cacheSubnetGroupName())) {
                            cddGroupId = cc.replicationGroupId();
                            if (repGroupId == null || !cddGroupId.equals(repGroupId)) {
                                repGroupId = cddGroupId;
                                ReplicationGroup rg = cache.describeReplicationGroups(
                                        DescribeReplicationGroupsRequest.builder().replicationGroupId(repGroupId).build())
                                        .replicationGroups().get(0);
                                ccSpeaker.smartPrintResult(true,
                                        "REDIS-" + (++rdCount) + ": " + rg.replicationGroupId() + ", " + cc.engine() + ", "
                                                + cc.engineVersion() + ", " + rg.cacheNodeType() + ", "
                                                + cc.cacheClusterStatus() + ", atRestEnc:" + cc.atRestEncryptionEnabled()
                                                + ", transitEnc:" + cc.transitEncryptionEnabled()
                                                + (rg.nodeGroups().size() == 0 ? ""
                                                        : ", shard:" + rg.nodeGroups().size() + ", replica:"
                                                                + rg.nodeGroups().get(0).nodeGroupMembers().size()));
                                if (rg.configurationEndpoint() != null) {
                                    if (mode.equals(PLAIN)) {
                                        ccSpeaker.printResult(true, "endpoint: " + rg.configurationEndpoint().address()
                                                + ":" + rg.configurationEndpoint().port());
                                    } else {
                                        ccSpeaker.printResult(true,
                                                "endpoint: " + hp.redact(rg.configurationEndpoint().address() + ":"
                                                        + rg.configurationEndpoint().port()));
                                    }
                                }
                                ccSpeaker.printResult(true, "maintenance-utc: " + cc.preferredMaintenanceWindow());
                                ccSpeaker.printResult(true,
                                        "protected-by: " + uec2.sgMemberShipsToSgTagOrName(ec2, cc.securityGroups()));
                                ccSpeaker.printResult(true, cc.cacheSubnetGroupName() + ": "
                                        + cacheSubnetGroupNameToSubnetAzs.get(cc.cacheSubnetGroupName()));
                            } else {
                                continue;
                            }
                        } else if (engine.equals("memcached")
                                && cacheSubnetGroupNameToSubnetAzs.containsKey(cc.cacheSubnetGroupName())) {
                            ccSpeaker.smartPrintResult(true, "MEMCACHED-" + (++mdCount) + ": " + cc.cacheClusterId() + ", "
                                    + cc.engine() + ", " + cc.engineVersion() + ", " + cc.cacheNodeType() + ", "
                                    + cc.cacheClusterStatus() + ", atRestEnc:" + cc.atRestEncryptionEnabled()
                                    + ", transitEnc:" + cc.transitEncryptionEnabled() + ", nodes: " + cc.numCacheNodes());
                            if (cc.configurationEndpoint() != null) {
                                if (mode.equals(PLAIN)) {
                                    ccSpeaker.printResult(true, "endpoint: " + cc.configurationEndpoint().address() + ":"
                                            + cc.configurationEndpoint().port());
                                } else {
                                    ccSpeaker.printResult(true,
                                            "endpoint: " + hp.redact(cc.configurationEndpoint().address() + ":"
                                                    + cc.configurationEndpoint().port()));
                                }
                            }
                            ccSpeaker.printResult(true, "maintenance-utc: " + cc.preferredMaintenanceWindow());
                            ccSpeaker.printResult(true,
                                    "protected-by: " + uec2.sgMemberShipsToSgTagOrName(ec2, cc.securityGroups()));
                            ccSpeaker.printResult(true, cc.cacheSubnetGroupName() + ": "
                                    + cacheSubnetGroupNameToSubnetAzs.get(cc.cacheSubnetGroupName()));
                        }
                    }
                }
                vSpeaker.printResult(true, "#TTL-MEMCACHE:" + mdCount);
                vSpeaker.printResult(true, "#TTL-REDIS:" + rdCount + "\n");
                // -------------------- End EalstiCache
                // -------------------- Begin RDS
                vSpeaker.printTitle("RDS:");
                int rdsCount = 0;
                Iterator<DescribeDbInstancesResponse> iterRdss = rds.describeDBInstancesPaginator().iterator();
                while(iterRdss.hasNext()){
                    List<DBInstance> rdss = iterRdss.next().dbInstances();
                    for (DBInstance instance : rdss) {
                        Speaker rdsSpeaker = vSpeaker.clone();
                        String oneSgId = instance.vpcSecurityGroups().get(0).vpcSecurityGroupId();
                        String oneVpcId = ec2
                                .describeSecurityGroups(DescribeSecurityGroupsRequest.builder().groupIds(oneSgId).build())
                                .securityGroups().get(0).vpcId();
                        if (oneVpcId.equals(vpc.vpcId())) {
                            String iops = null;
                            if (instance.storageType().equals("gp2")) {
                                iops = "gp2-rule";
                            }
                            if (mode.equals(PLAIN)) {
                                rdsSpeaker.smartPrintResult(true,
                                        Speaker.DB + " RDS-" + (++rdsCount) + ": " + instance.dbInstanceIdentifier()
                                                + ", DB:" + instance.dbName() + ", " + instance.engine() + ", "
                                                + instance.engineVersion() + ", status:" + instance.dbInstanceStatus()
                                                + ", multi-AZ:" + instance.multiAZ() + ", admin:"
                                                + instance.masterUsername() + ", size:" + instance.allocatedStorage() + "G"
                                                + ", " + instance.storageType() + ", iops:"
                                                + (instance.storageType().equals("gp2") ? iops : instance.iops()));
                            } else {
                                rdsSpeaker.smartPrintResult(true, Speaker.DB + " RDS-" + (++rdsCount) + ": "
                                        + instance.dbInstanceIdentifier() + ", DB:" + instance.dbName() + ", "
                                        + instance.engine() + ", " + instance.engineVersion() + ", status:"
                                        + instance.dbInstanceStatus() + ", multi-AZ:" + instance.multiAZ() + ", admin:"
                                        + hp.redact(instance.masterUsername()) + ", size:" + instance.allocatedStorage()
                                        + "G" + ", " + instance.storageType() + ", iops:"
                                        + (instance.storageType().equals("gp2") ? iops : instance.iops()));
                            }
                            if (instance.endpoint() != null) {
                                if (mode.equals(PLAIN)) {
                                    rdsSpeaker.printResult(true, "endpoint: " + instance.endpoint().address() + ":"
                                            + instance.endpoint().port() + "/" + instance.dbName());
                                } else {
                                    rdsSpeaker.printResult(true, "endpoint: " + hp.redact(instance.endpoint().address()
                                            + ":" + instance.endpoint().port() + "/" + instance.dbName()));
                                }
                            }
                            rdsSpeaker.printResult(true, "maintenance-utc: " + instance.preferredMaintenanceWindow()
                                    + ", backup-utc:" + instance.preferredBackupWindow());
                            rdsSpeaker.printResult(true, "protected-by: "
                                    + uec2.vpcSgMemberShipsToSgTagOrNameForRds(ec2, instance.vpcSecurityGroups()));
                            rdsSpeaker.printResult(true, "currently-at: " + instance.availabilityZone());
                            rdsSpeaker.printResult(true, instance.dbSubnetGroup().dbSubnetGroupName() + ": "
                                    + uec2.decodeDbSubnetGroup(ec2, instance.dbSubnetGroup()));
                        }
                    }
                }
                vSpeaker.printResult(true, "#TTL-RDS:" + rdsCount + "\n");
                // -------------------- End RDS
                // -------------------- Begin Redshfit
                vSpeaker.printTitle("Redshift:");
                int rsCount = 0;
                Iterator<DescribeClustersResponse> iterRss = rs.describeClustersPaginator().iterator();
                while(iterRss.hasNext()){
                    List<Cluster> rss = iterRss.next().clusters();
                    for (Cluster c : rss) {
                        Speaker rsSpeaker = vSpeaker.clone();
                        if (c.vpcId().equals(vpc.vpcId())) {
                            if (mode.equals(PLAIN)) {
                                rsSpeaker.smartPrintResult(true,
                                        Speaker.DB + " REDSHIFT-" + (++rsCount) + ": " + c.clusterIdentifier() + ", "
                                                + c.numberOfNodes() + " of " + c.nodeType() + ", DB:" + c.dbName() + ", "
                                                + c.clusterVersion() + ", admin:" + c.masterUsername()
                                                + ", auto-snap-retention:" + c.automatedSnapshotRetentionPeriod()
                                                + ", status:" + c.clusterStatus() + ", m-status: "
                                                + (c.modifyStatus() == null ? "n/a" : c.modifyStatus()));
                            } else {
                                rsSpeaker.smartPrintResult(true,
                                        Speaker.DB + " REDSHIFT-" + (++rsCount) + ": " + c.clusterIdentifier() + ", "
                                                + c.numberOfNodes() + " of " + c.nodeType() + ", DB:" + c.dbName() + ", "
                                                + c.clusterVersion() + ", admin:" + hp.redact(c.masterUsername())
                                                + ", auto-snap-retention:" + c.automatedSnapshotRetentionPeriod()
                                                + ", status:" + c.clusterStatus() + ", m-status: "
                                                + (c.modifyStatus() == null ? "n/a" : c.modifyStatus()));
                            }
                            if (c.endpoint() != null) {
                                if (mode.equals(PLAIN)) {
                                    rsSpeaker.printResult(true, "endpoint: " + c.endpoint().address() + ":"
                                            + c.endpoint().port() + "/" + c.dbName());
                                } else {
                                    rsSpeaker.printResult(true, "endpoint: " + hp
                                            .redact(c.endpoint().address() + ":" + c.endpoint().port() + "/" + c.dbName()));
                                }
                            }
                            rsSpeaker.printResult(true, "protected-by: "
                                    + uec2.vpcSgMemberShipsToSgTagOrNameForRedshift(ec2, c.vpcSecurityGroups()));
                            rsSpeaker.printResult(true, "currently-at: " + c.availabilityZone());
                            rsSpeaker.printResult(true, c.clusterSubnetGroupName() + ": "
                                    + uec2.decodeRedshiftClusterSubnetGroupName(ec2, rs, c.clusterSubnetGroupName()));
                        }
                    }
                }
                vSpeaker.printResult(true, "#TTL-REDSHIFT:" + rsCount + "\n");
                // -------------------- End Redshift
                // -------------------- Begin Silent Instance Status Check Markers
                Hashtable<String, String> instanceId2Ec2Type = new Hashtable<String, String>();
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
                // -------------------- End Sillent Instance Status Check Markers
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
                    if (vpc.vpcId()
                            .equals(ec2.describeSubnets(DescribeSubnetsRequest.builder().subnetIds(subnets[0]).build())
                                    .subnets().get(0).vpcId())) {
                        asSpeaker.smartPrintResult(true,
                                "ASG-" + (++asgCount) + ": " + as.autoScalingGroupName() + ", lc:"
                                        + as.launchConfigurationName() + ", status:" + as.status() + ", hc-type:"
                                        + as.healthCheckType());
                        asSpeaker.printResult(true, "az:" + as.availabilityZones());
                        List<software.amazon.awssdk.services.autoscaling.model.Instance> instances = as.instances();
                        asSpeaker.printResult(true, "desired:" + as.desiredCapacity() + ", max:" + as.maxSize()
                                + ", min:" + as.minSize() + ", registered:" + instances.size());
                        for (software.amazon.awssdk.services.autoscaling.model.Instance instance : instances) {
                            instanceId2Ec2Type.put(instance.instanceId(), "ASG");
                        }
                    }
                }
                vSpeaker.printResult(true, "#TTL-ASG:" + asgCount + "\n");
                // -------------------- End Auto Scaling Group
                // -------------------- Begin Subnet Resources
                int ec2Count = 0;
                int emrCount = 0;
                int natCount = 0;
                int sc = 0;
                for (Subnet s : subnetsInVpc) {
                    Filter subnetIdFilter = Filter.builder().name("subnet-id").values(s.subnetId()).build();
                    Speaker sSpeaker = vSpeaker.clone();
                    sSpeaker.smartPrintTitle("SUBNET-" + (++sc) + ": " + uec2.decodeSubnetById(ec2, s.subnetId())
                            + " RT: "
                            + uec2.rtIdToRtTagOrId(ec2,
                                    (subnetToRoutetable.get(s.subnetId()) == null ? subnetToRoutetable.get("main")
                                            : subnetToRoutetable.get(s.subnetId())))
                            + ", NACL: " + uec2.naclIdToNaclTagOrId(ec2, subnetIdToNaclId.get(s.subnetId())));
                    // Nat Gateway
                    sSpeaker.printTitle("NAT Gateway:");
                    for (NatGateway nat : ec2
                            .describeNatGateways(
                                    DescribeNatGatewaysRequest.builder().filter(vpcF, subnetIdFilter).build())
                            .natGateways()) {
                        Speaker ngSpeaker = sSpeaker.clone();
                        ngSpeaker.smartPrintResult(true,
                                Speaker.GW + " NATGW-" + (++natCount) + ": " + uec2.getNameTagValueEc2(nat.tags())
                                        + ", " + nat.natGatewayId() + ", " + nat.state() + ", provisioned-bandwidth:"
                                        + (nat.provisionedBandwidth() == null ? "n/a" : nat.provisionedBandwidth()));
                    }
                    sSpeaker.printResult(true, "#TTL-NATGW:" + natCount + "\n");
                    // EMR
                    sSpeaker.printTitle("EMR:");
                    Iterator<software.amazon.awssdk.services.emr.model.ListClustersResponse> iterEmrs = emr.listClustersPaginator().iterator();
                    while(iterEmrs.hasNext()){
                        List<ClusterSummary> css = iterEmrs.next().clusters();
                        for (ClusterSummary cs : css) {
                            if (!cs.status().state().equals(ClusterState.TERMINATED)) {
                                software.amazon.awssdk.services.emr.model.Cluster c = emr
                                        .describeCluster(DescribeClusterRequest.builder().clusterId(cs.id()).build())
                                        .cluster();
                                String ec2SnId = c.ec2InstanceAttributes().ec2SubnetId();
                                if (ec2SnId.equals(s.subnetId())) {
                                    Speaker emrSpeaker = sSpeaker.clone();
                                    emrSpeaker.smartPrintResult(true,
                                            "EMR-" + (++emrCount) + ": " + cs.name() + ", " + cs.id() + ", release:"
                                                    + c.releaseLabel() + ", " + cs.status().stateAsString()
                                                    + ", security-config:" + c.securityConfiguration());
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
                    sSpeaker.printResult(true, "#TTL-EMR:" + emrCount + "\n");
                    // EC2
                    sSpeaker.printTitle("EC2: Auto Scaling = " + Speaker.STAR + "    Auto Recovery = " + Speaker.AUTO_RECOVERY_EC2
                            + "    NONE-AS-AR = " + Speaker.NONE_ASG_AR_EC2);
                    int subnetEc2Count = 0;
                    Filter vpcInstanceFilter = Filter.builder().name("vpc-id").values(vpc.vpcId()).build();
                    for (Reservation r : ec2
                            .describeInstances(DescribeInstancesRequest.builder().filters(vpcInstanceFilter).build())
                            .reservations()) {
                        for (Instance i : r.instances()) {
                            Speaker eSpeaker = sSpeaker.clone();
                            if (!i.state().name().equals(InstanceStateName.TERMINATED)
                                    && i.subnetId().equals(s.subnetId())) {
                                subnetEc2Count++;
                                if (mode.equals(PLAIN)) {
                                    eSpeaker.smartPrintResult(true,
                                            sSpeaker.getIconForEC2InstanceType(i.instanceId(), instanceId2Ec2Type)
                                                    + "  EC2-" + (++ec2Count) + ": Name:{"
                                                    + uec2.getNameTagValueEc2(i.tags()) + "}, " + i.instanceId() + ", "
                                                    + i.instanceType() + ", sriov:"
                                                    + (i.sriovNetSupport() == null ? "n/a" : i.sriovNetSupport())
                                                    + ", ena:" + i.enaSupport() + ", " + i.state().name() + ", "
                                                    + i.privateIpAddress() + ", "
                                                    + (i.publicIpAddress() == null ? "n/a" : i.publicIpAddress()));
                                } else {
                                    eSpeaker.smartPrintResult(true, sSpeaker.getIconForEC2InstanceType(i.instanceId(),
                                            instanceId2Ec2Type) + "  EC2-" + (++ec2Count) + ": Name:{"
                                            + uec2.getNameTagValueEc2(i.tags()) + "}, " + i.instanceId() + ", "
                                            + i.instanceType() + ", sriov:"
                                            + (i.sriovNetSupport() == null ? "n/a" : i.sriovNetSupport()) + ", ena:"
                                            + i.enaSupport() + ", " + i.state().name() + ", "
                                            + hp.redactIp(i.privateIpAddress()) + ", "
                                            + (i.publicIpAddress() == null ? "n/a" : hp.redactIp(i.publicIpAddress())));
                                    eSpeaker.printResult(true, "role: " + (i.iamInstanceProfile() == null ? "n/a"
                                            : hp.redactArn(i.iamInstanceProfile().arn())));
                                    eSpeaker.printResult(true, "key: " + hp.redact(i.keyName()));
                                }
                                Filter ebsFilter = Filter.builder().name("attachment.instance-id")
                                        .values(i.instanceId()).build();
                                List<Volume> volumes = ec2
                                        .describeVolumes(DescribeVolumesRequest.builder().filters(ebsFilter).build())
                                        .volumes();
                                for (Volume v : volumes) {
                                    eSpeaker.printResult(true,
                                            "ebs: tag-name:" + uec2.getNameTagValueEc2(v.tags()) + ", size:" + v.size()
                                                    + "G" + ", type:" + v.volumeType() + ", iops:" + v.iops() + ", enc:"
                                                    + v.encrypted() + ", device:" + v.attachments().get(0).device());
                                }
                                for (NetworkInterface eni : enisInVpc) {
                                    Speaker eniSpeaker = eSpeaker.clone();
                                    if (eni.attachment() != null && eni.attachment().instanceId() != null
                                            && eni.attachment().instanceId().equals(i.instanceId())) {
                                        if (mode.equals(PLAIN)) {
                                            eniSpeaker.smartPrintResult(true, "eni-" + eni.attachment().deviceIndex()
                                                    + ": " + eni.interfaceType() + ", " + eni.availabilityZone()
                                                    + ", primary:{" + eni.privateDnsName() + ", "
                                                    + eni.privateIpAddress() + ", "
                                                    + (eni.association() == null ? "n/a"
                                                            : eni.association().publicDnsName())
                                                    + ", "
                                                    + (eni.association() == null ? "n/a" : eni.association().publicIp())
                                                    + "}");
                                            eniSpeaker.printResult(true, "protected-by: "
                                                    + uec2.groupIdentifierToSgTagOrName(ec2, eni.groups()));
                                            for (NetworkInterfacePrivateIpAddress addr : eni.privateIpAddresses()) {
                                                eniSpeaker.printResult(true,
                                                        "address-association: " + addr.privateIpAddress() + ", "
                                                                + (addr.association() != null
                                                                        ? addr.association().publicIp()
                                                                        : "no-public-ip"));
                                            }
                                        } else {
                                            eniSpeaker
                                                    .smartPrintResult(true,
                                                            "eni-" + eni.attachment().deviceIndex() + ": "
                                                                    + eni.interfaceType() + ", "
                                                                    + eni.availabilityZone() + ", primary:{"
                                                                    + hp.redact(eni.privateDnsName()) + ", "
                                                                    + hp.redactIp(eni.privateIpAddress()) + ", "
                                                                    + (eni.association() == null ? "n/a"
                                                                            : hp.redact(
                                                                                    eni.association().publicDnsName()))
                                                                    + ", "
                                                                    + (eni.association() == null ? "n/a"
                                                                            : hp.redactIp(eni.association().publicIp()))
                                                                    + "}");
                                            eniSpeaker.printResult(true, "protected-by: "
                                                    + uec2.groupIdentifierToSgTagOrName(ec2, eni.groups()));
                                            for (NetworkInterfacePrivateIpAddress addr : eni.privateIpAddresses()) {
                                                eniSpeaker.printResult(true,
                                                        "address-association: " + hp.redactIp(addr.privateIpAddress())
                                                                + ", "
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
                    sSpeaker.printResult(true, "#TTL-SUBNET-EC2:" + subnetEc2Count + "\n");
                } // for Subnet
                  // -------------------- End Subnet Resources
                vSpeaker.printResult(true, "#TTL-VPC-EC2:" + ec2Count + "\n");
                vSpeaker.smartPrintTitle("End VPC: " + vpcName);
            } // End VPC
        }
    }


}