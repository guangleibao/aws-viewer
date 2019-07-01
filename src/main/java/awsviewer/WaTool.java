package awsviewer;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import awsviewer.common.Uec2;
import awsviewer.common.Uecs;
import awsviewer.common.Uelasticache;
import awsviewer.common.Uelasticloadbalancing;
import awsviewer.common.Uelasticloadbalancingv2;
import awsviewer.common.Uelasticsearch;
import awsviewer.common.Ulambda;
import awsviewer.common.Urds;
import awsviewer.common.Uredshift;
import awsviewer.conf.Clients;
import awsviewer.conf.General;
import awsviewer.conf.Helper;
import awsviewer.conf.Speaker;
import awsviewer.inf.CUtil;
import software.amazon.awssdk.services.autoscaling.AutoScalingClient;
import software.amazon.awssdk.services.autoscaling.model.AutoScalingGroup;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.DescribeAlarmsRequest;
import software.amazon.awssdk.services.cloudwatch.model.Dimension;
import software.amazon.awssdk.services.cloudwatch.model.MetricAlarm;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesRequest;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesResponse;
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
import software.amazon.awssdk.services.ec2.model.Snapshot;
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
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.AliasListEntry;
import software.amazon.awssdk.services.kms.model.ListAliasesRequest;
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

        private static final Helper HELPER = new Helper();
        private static final Speaker SK = Speaker.getConsoleInstance(null);
        private final Speaker sk = Speaker.getConsoleInstance(null);

        public static final ArrayList<String> SKIPPED_METHODS = new ArrayList<String>();

        static {
                // TTL preferences.
                java.security.Security.setProperty("networkaddress.cache.ttl", "10");

                // The methods ignored by dynamic caller.
                SKIPPED_METHODS.add("main");
                SKIPPED_METHODS.add("coreV2");
        }

        /**
         * The true main.
         * 
         * @param args
         * @throws Exception
         */
        public static void coreV2(String[] args) throws Exception {
                // Show general help, but skip testing routing.
                if (args == null || args.length == 0) {
                        SK.smartPrintTitle(
                                        "Use -h option with following sub-command to get help, [] count shows how many parameters the sub-command requires to run:");
                        TreeSet<String> ts = new TreeSet<String>();
                        StringBuffer sb = null;
                        Method[] allMethods = WaTool.class.getDeclaredMethods();
                        ArrayList<Method> methods = new ArrayList<Method>();
                        for (Method m : allMethods) {
                                if (Modifier.isPublic(m.getModifiers()) && !m.getName().startsWith("test")) {
                                        methods.add(m);
                                }
                        }
                        String mn = null;
                        for (Method m : methods) {
                                sb = new StringBuffer();
                                mn = m.getName();
                                if (SKIPPED_METHODS.contains(mn)) {
                                        continue;
                                }
                                sb.append(mn + ": ");
                                int parameterCount = m.getParameterTypes().length;
                                for (int i = 0; i < parameterCount; i++) {
                                        sb.append("[]");
                                }
                                ts.add(new String(sb));
                        }
                        for (String s : ts) {
                                System.out.println(s);
                        }
                        SK.printLine();
                        return;
                }

                WaTool u = new WaTool();
                Class<?> clazz = u.getClass();
                Method[] allMethods = clazz.getDeclaredMethods();
                ArrayList<Method> methods = new ArrayList<Method>();
                for (Method m : allMethods) {
                        if (Modifier.isPublic(m.getModifiers())) {
                                methods.add(m);
                        }
                }
                if (SKIPPED_METHODS.contains(args[0])) {
                        return;
                }
                for (Method m : methods) {
                        if (m.getName().equals(args[0])) {
                                // Options filter
                                Class<?>[] paramTypes = m.getParameterTypes();
                                if (paramTypes == null || paramTypes.length == 0
                                                || paramTypes[0] != (new String[] { "XXX" }.getClass())) {
                                        int paramCount = paramTypes.length;
                                        String[] paramValues = new String[paramCount];
                                        // Take '-h' help into consideration.
                                        for (int i = 0; i < paramValues.length; i++) {
                                                if (args[1].equals("-h")) {
                                                        paramValues[0] = args[1];
                                                        for (int j = 1; j < paramValues.length; j++) {
                                                                paramValues[j] = null;
                                                        }
                                                        break;
                                                } else {
                                                        paramValues[i] = args[i + 1];
                                                }
                                        }
                                        m.invoke(u, (Object[]) paramValues);
                                } else {
                                        String[] mParameters = Arrays.copyOfRange(args, 1, args.length);
                                        System.out.println(m.getName() + ": " + Arrays.toString(mParameters));
                                        m.invoke(u, (Object[]) (mParameters));
                                }
                                return;
                        }
                }
                Helper.searh(args[0]);
                return;
        }

        public static void main(String[] args) throws Exception {
                Logger.getRootLogger().setLevel(Level.OFF);
                coreV2(args);
        }

        /**
         * Print resources in VPC by VPC name prefix or vpc-id in MD format.
         */
        public void showVpc(String prefix, String mode, String profile) throws Exception {
                // ------------- INIT
                HELPER.help(prefix, "<vpc-name-prefix|vpc-id> <mode: redact|plain> <profile>");

                // Define the method Speaker
                Speaker mSpeaker = Speaker.getMdInstance(profile);

                // Ready the SDK environment
                General.init(profile);

                // Load bullets - VPC
                List<CUtil> vpcUtil = new ArrayList<CUtil>();
                Uec2 uec2 = Uec2.build();
                vpcUtil.add(uec2);
                Uelasticloadbalancingv2 uelbv2 = Uelasticloadbalancingv2.build();
                vpcUtil.add(uelbv2);
                Uelasticloadbalancing uelb = Uelasticloadbalancing.build();
                vpcUtil.add(uelb);
                Uecs uecs = Uecs.build();
                vpcUtil.add(uecs);
                Uelasticsearch ues = Uelasticsearch.build();
                vpcUtil.add(ues);
                Ulambda ulambda = Ulambda.build();
                vpcUtil.add(ulambda);
                Uelasticache ucache = Uelasticache.build();
                vpcUtil.add(ucache);
                Urds urds = Urds.build();
                vpcUtil.add(urds);
                Uredshift urs = Uredshift.build();
                vpcUtil.add(urs);

                // Define VPCs
                Filter vpcF = uec2.createFilterEc2("tag:Name", prefix + "*");
                Filter vpcF2 = uec2.createFilterEc2("vpc-id", prefix);
                Filter[] tries = new Filter[] { vpcF, vpcF2 };

                // EC2 Core
                Ec2Client ec2 = (Ec2Client) Clients.getClientByServiceClass(Clients.EC2, profile);
                CloudWatchClient cw = (CloudWatchClient) Clients.getClientByServiceClass(Clients.CLOUDWATCH, profile);
                EmrClient emr = (EmrClient) Clients.getClientByServiceClass(Clients.EMR, profile);

                // -------------- Begin VPC
                // VPC Paginator
                for (Filter f : tries) {
                        Iterator<DescribeVpcsResponse> iterVpcs = ec2
                                        .describeVpcsPaginator(DescribeVpcsRequest.builder().filters(f).build())
                                        .iterator();
                        while (iterVpcs.hasNext()) {
                                List<Vpc> vpcs = iterVpcs.next().vpcs();
                                for (Vpc vpc : vpcs) {
                                        Speaker vSpeaker = mSpeaker.clone();
                                        Filter subnetF = Filter.builder().name("vpc-id").values(vpc.vpcId()).build();
                                        Filter eniF = Filter.builder().name("vpc-id").values(vpc.vpcId()).build();
                                        Filter routeTableF = Filter.builder().name("vpc-id").values(vpc.vpcId())
                                                        .build();
                                        String vpcName = uec2.getNameTagValueEc2(vpc.tags());
                                        vSpeaker.smartPrintTitle("VPC: " + vpcName + "|" + vpc.vpcId() + ", "
                                                        + vpc.cidrBlock() + ", tenancy: " + vpc.instanceTenancy());

                                        // -------------------- Begin Common
                                        uec2.populateSubnetsInVpc(ec2, subnetF);
                                        uec2.populateEnisInVpc(ec2, eniF);
                                        uec2.populateSubnetToRouteTable(ec2, routeTableF);
                                        uec2.populateInstanceId2Ec2Type(cw);
                                        vSpeaker.setUec2(uec2);
                                        // -------------------- End Common

                                        // Begin Print VPC Resources
                                        for (CUtil c : vpcUtil) {
                                                c.printVpcResource("AND", mode, vSpeaker, f);
                                        }
                                        // End Print VPC Resources

                                        // -------------------- Begin Subnet Resources
                                        int sc = 0;
                                        int ttlEc2 = 0;
                                        for (Subnet s : uec2.getSubnetsInVpc()) {
                                                Speaker sSpeaker = vSpeaker.clone();
                                                sSpeaker.smartPrintTitle("SUBNET-" + (++sc) + ": "
                                                                + uec2.decodeSubnetById(ec2, s.subnetId()) + " RT: "
                                                                + uec2.rtIdToRtTagOrId(ec2, (uec2
                                                                                .getSubnetToRouteTable()
                                                                                .get(s.subnetId()) == null ? uec2
                                                                                                .getSubnetToRouteTable()
                                                                                                .get("main")
                                                                                                : uec2.getSubnetToRouteTable()
                                                                                                                .get(s.subnetId())))
                                                                + ", NACL: "
                                                                + uec2.naclIdToNaclTagOrId(ec2,
                                                                                uec2.getSubnetIdToNaclId()
                                                                                                .get(s.subnetId())));
                                                ttlEc2 = ttlEc2 + uec2.printSubnetToolkit(ec2, emr, vpc.vpcId(),
                                                                s.subnetId(), mode, sSpeaker);
                                        }
                                        vSpeaker.printResult(true, "TTL-VPC-EC2:" + ttlEc2 + "\n");
                                        vSpeaker.smartPrintTitle("End VPC: " + vpcName);
                                } // End VPC
                        }
                }
        }
}