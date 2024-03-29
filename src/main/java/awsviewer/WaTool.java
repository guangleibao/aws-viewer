package awsviewer;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.InstanceMarketOptionsRequest;
import com.amazonaws.services.ec2.model.MarketType;
import com.amazonaws.services.ec2.model.RunInstancesRequest;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.reflections.Reflections;

import awsviewer.common.Uautoscaling;
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
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.regions.ServiceMetadata;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeVpcsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeVpcsResponse;
import software.amazon.awssdk.services.ec2.model.Filter;
import software.amazon.awssdk.services.ec2.model.Image;
import software.amazon.awssdk.services.ec2.model.InstanceType;
import software.amazon.awssdk.services.ec2.model.Subnet;
import software.amazon.awssdk.services.ec2.model.Vpc;
import software.amazon.awssdk.services.emr.EmrClient;

public class WaTool {

        private static final Helper HELPER = new Helper();
        private static final Speaker SK = Speaker.getConsoleInstance(null);
        private final Speaker sk = Speaker.getConsoleInstance(null);
        private final Helper helper = new Helper();

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

        /**
         * Main.
         */
        public static void main(String[] args) throws Exception {
                Logger.getRootLogger().setLevel(Level.OFF);
                coreV2(args);
        }

        /**
         * Print resources in VPC by VPC name prefix or vpc-id in HTML format.
         */
        public void showVpc(String prefix, String mode, String profile) throws Exception {
                // ------------- INIT
                HELPER.help(prefix, "<vpc-name-prefix|vpc-id> <mode: redact|plain> <profile>");

                // Define the method Speaker
                Speaker mSpeaker = Speaker.getWebInstance(profile);
                System.out.println("<body style = 'font-family:monospace;' >");

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
                Uautoscaling uasg = Uautoscaling.build();
                vpcUtil.add(uasg);

                // Define VPCs
                Filter[] tries = null;
                /*
                if (prefix.equals("?")) {
                        Filter vpcF = uec2.createFilterEc2("vpc-id", "*");
                        tries = new Filter[] { vpcF };
                } else {*/
                        Filter vpcF = uec2.createFilterEc2("tag:Name", prefix + "*");
                        Filter vpcF2 = uec2.createFilterEc2("vpc-id", prefix);
                        tries = new Filter[] { vpcF, vpcF2 };
                //}

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
                                        vSpeaker.smartPrintTitleRestrict("VPC: " + vpcName + "|" + vpc.vpcId() + ", "
                                                        + vpc.cidrBlock() + ", tenancy: " + vpc.instanceTenancy());

                                        // -------------------- Begin Common
                                        uec2.populateSubnetsInVpc(ec2, subnetF);
                                        uec2.populateEnisInVpc(ec2, eniF);
                                        uec2.populateSubnetToRouteTable(ec2, routeTableF);
                                        uec2.populateInstanceId2Ec2ARType(cw);
                                        vSpeaker.setUec2(uec2);
                                        // -------------------- End Common

                                        // Begin Print VPC Resources
                                        Speaker vpcResourceSpeaker = vSpeaker.clone();
                                        vpcResourceSpeaker.smartPrintTitle("VPC Common");
                                        for (CUtil c : vpcUtil) {
                                                c.printVpcResource("AND", mode, vpcResourceSpeaker, f);
                                        }
                                        // End Print VPC Resources

                                        // -------------------- Begin Subnet Resources
                                        int sc = 0;
                                        int ttlEc2 = 0;
                                        for (Subnet s : uec2.getSubnetsInVpc()) {
                                                Speaker sSpeaker = vSpeaker.clone();
                                                sSpeaker.smartPrintTitleRestrict("SUBNET-" + (++sc) + ": "
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
                                        vSpeaker.smartPrintTitleRestrict("End VPC: " + vpcName + ", TTL EC2: " + ttlEc2
                                                        + ", In ASG:" + uec2.getInstanceId2Ec2Type().get("ASG")
                                                        + ", Protected by Auto Recovery:"
                                                        + uec2.getInstanceId2Ec2Type().get("AR") + ", NONE-ASG-AR:"
                                                        + uec2.getInstanceId2Ec2Type().get("NONE-ASG-AR"));
                                } // End VPC
                        }
                }
        }

        /**
         * Show instance type by profile
         */
        public void showInstanceType(String count, String profile) throws Exception {
                HELPER.help(count, "<instance-count> <profile>");
                General.init(profile);
                int i = 1;
                Speaker mSpeaker = Speaker.getConsoleInstance(profile);
                Speaker allTypeSpeaker = mSpeaker.clone();
                allTypeSpeaker.smartPrintTitle("All Instance Type");
                List<String> its = new ArrayList<String>();
                for (InstanceType type : InstanceType.values()) {
                        if (type.toString() != null && !type.toString().equals("null")) {
                                mSpeaker.printResult(1, true, i++ + ") " + type.toString());
                                its.add(type.toString());
                        }
                }
                Speaker validTypeSpeaker = mSpeaker.clone();
                validTypeSpeaker.smartPrintTitle("Valid Instance Type for " + General.PROFILE_REGION.get(profile).id());
                Ec2Client ec2 = (Ec2Client) Clients.getClientByServiceClass(Clients.EC2, profile);
                Uec2 uec2 = Uec2.build();
                Image ami = uec2.getOneOfAmazonLinuxAmi(ec2);
                AWSCredentialsProviderChain ec2Profile = new AWSCredentialsProviderChain(
                                new ProfileCredentialsProvider(profile));
                AmazonEC2 ec2o = AmazonEC2ClientBuilder.standard().withCredentials(ec2Profile)
                                .withRegion(General.PROFILE_REGION.get(profile).id()).build();
                for (String it : its) {
                        helper.waitFor(300);
                        RunInstancesRequest realreq = new RunInstancesRequest().withImageId(ami.imageId())
                                        .withInstanceType(it).withMinCount(Integer.parseInt(count))
                                        .withMaxCount(Integer.parseInt(count));
                        validTypeSpeaker.printResult(false, it);
                        try {
                                ec2o.dryRun(realreq).getDryRunResponse();
                                validTypeSpeaker.printRaw(true, " => Supported - HVM");
                        } catch (AmazonClientException ex) {
                                if (ex.getMessage().contains("Unrecognized service response for the dry-run request")) {
                                        if (ex.getCause().getMessage().matches(
                                                        "^Your requested instance type .* is not supported in this region.*")) {
                                                System.out.println(" => Not Supported (Instance-type)");
                                        } else if (ex.getCause().getMessage().contains(
                                                        "The requested configuration is currently not supported. Please check the documentation")) {
                                                System.out.println(" => Not Supported (Config)");
                                        } else if (ex.getCause().getMessage().contains(
                                                        "Non-Windows instances with a virtualization type of 'hvm' are currently not supported for this instance type")) {
                                                System.out.println(" => Supported - PV");
                                        } else if (ex.getCause().getMessage()
                                                        .contains("than your current instance limit")) {
                                                System.out.println(" => Supported - HVM - check soft limit");
                                        } else if (ex.getCause().getMessage().contains(
                                                        "Enhanced networking with the Elastic Network Adapter (ENA) is required")) {
                                                System.out.println(" => Supported - HVM - ENA required");
                                        } else {
                                                System.out.println(" => " + ex.getCause().getMessage());
                                        }
                                } else {
                                        System.out.println(" => " + ex.getMessage());
                                }
                        }
                }
        }

        /**
         * Show services comparison between two regions.
         * 
         * @param regionCode1
         * @param regionCode2
         */
        public void showServiceCompareByRegionCode(String regionCode1, String regionCode2) throws Exception {
                HELPER.help(regionCode1,
                                "<region-code1>|<? to get all region codes> <region-code2>|<? to get all region codes>");
                if (regionCode1.equals("?")) {
                        this.showRegionCode();
                } else {
                        this.sk.printResult(true, regionCode1.toUpperCase() + " |-------------> "
                                        + regionCode2.toUpperCase() + " (if you see " + Speaker.REMOVING
                                        + " it means the service is not available or the SDK needs updated)");
                        this.sk.printLine();
                        Reflections reflections = new Reflections("software.amazon.awssdk.regions.servicemetadata");
                        Set<Class<? extends ServiceMetadata>> smClasses = reflections
                                        .getSubTypesOf(ServiceMetadata.class);
                        TreeSet<String> serviceMetadataClassNames = new TreeSet<String>();
                        TreeSet<String> serviceRealNames1 = new TreeSet<String>();
                        TreeSet<String> serviceRealNames2 = new TreeSet<String>();
                        TreeSet<String> serviceRealNames3 = new TreeSet<String>();
                        for (Class<? extends ServiceMetadata> clazz : smClasses) {
                                String className = clazz.getName();
                                serviceMetadataClassNames.add(className);
                        }
                        int charLongSize = 0;
                        for (String className : serviceMetadataClassNames) {
                                String serviceNameReal = className
                                                .replaceAll("software.amazon.awssdk.regions.servicemetadata.", "")
                                                .replaceAll("ServiceMetadata$", "");
                                charLongSize = (serviceNameReal.length() > charLongSize) ? serviceNameReal.length()
                                                : charLongSize;
                                ServiceMetadata instance = (ServiceMetadata) Class.forName(className).newInstance();
                                for (Region r : instance.regions()) {
                                        if (r.id().equals(regionCode1)) {
                                                serviceRealNames1.add(serviceNameReal);
                                                serviceRealNames3.add(serviceNameReal);
                                        }
                                        if (r.id().equals(regionCode2)) {
                                                serviceRealNames2.add(serviceNameReal);
                                                serviceRealNames3.add(serviceNameReal);
                                        }
                                }
                        }
                        int sCount = 0;
                        for (String s : serviceRealNames3) {
                                if (serviceRealNames1.contains(s) && serviceRealNames2.contains(s)) {
                                        this.sk.printResult(true, (++sCount) + ") " + s + " |-------------> " + s);
                                } else if (serviceRealNames1.contains(s) && !serviceRealNames2.contains(s)) {
                                        this.sk.printResult(true,
                                                        (++sCount) + ") " + s + " |-------------> " + Speaker.REMOVING
                                                                        + " " + Speaker.REMOVING + " "
                                                                        + Speaker.REMOVING);
                                } else if (!serviceRealNames1.contains(s) && serviceRealNames2.contains(s)) {
                                        this.sk.printResult(true,
                                                        (++sCount) + ") " + Speaker.REMOVING + " " + Speaker.REMOVING
                                                                        + " " + Speaker.REMOVING + " |-------------> "
                                                                        + s);
                                }
                        }
                }
                this.sk.printLine();
        }

        /**
         * Show all region codes.
         */
        public void showRegionCode() {
                this.sk.smartPrintTitle("AWS Region Infrastructure");
                int i = 0;
                for (String key : General.ALL_REGIONS.keySet()) {
                        this.sk.printResult(true, "[" + (++i) + "] " + key + " = "
                                        + General.ALL_REGIONS.get(key).metadata().description());
                }
                this.sk.printLine();
        }

        /**
         * Show services provided in a specific region.
         */
        public void showServiceByRegionCode(String regionCode) throws Exception {
                HELPER.help(regionCode, "<region-code>|<? to get all region codes>");
                if (regionCode.equals("?")) {
                        this.showRegionCode();
                } else {
                        this.sk.smartPrintTitle("Services provided by " + regionCode);
                        Reflections reflections = new Reflections("software.amazon.awssdk.regions.servicemetadata");
                        Set<Class<? extends ServiceMetadata>> smClasses = reflections
                                        .getSubTypesOf(ServiceMetadata.class);
                        TreeSet<String> serviceMetadataClassNames = new TreeSet<String>();
                        TreeSet<String> serviceRealNames = new TreeSet<String>();
                        for (Class<? extends ServiceMetadata> clazz : smClasses) {
                                String className = clazz.getName();
                                serviceMetadataClassNames.add(className);
                        }
                        for (String className : serviceMetadataClassNames) {
                                String serviceNameReal = className
                                                .replaceAll("software.amazon.awssdk.regions.servicemetadata.", "")
                                                .replaceAll("ServiceMetadata$", "");
                                ServiceMetadata instance = (ServiceMetadata) Class.forName(className).newInstance();
                                for (Region r : instance.regions()) {
                                        if (r.id().equals(regionCode)) {
                                                serviceRealNames.add(serviceNameReal);
                                        }
                                }
                        }
                        int sCount = 0;
                        for (String s : serviceRealNames) {
                                this.sk.printResult(true, (++sCount) + ") " + s);
                        }
                }
                this.sk.printLine();
        }

        /**
         * Dry run EC2. A good tool to test the instance type and capacity in region.
         * 
         * @param instanceType
         * @param count
         * @param profile
         * @throws Exception
         */
        public void dryRunEc2(String instanceType, String count, String profile) throws Exception {
                HELPER.help(instanceType, "<instance-type> <instance-count> <profile>");
                General.init(profile);
                Ec2Client ec2 = (Ec2Client) Clients.getClientByServiceClass(Clients.EC2, profile);
                Uec2 uec2 = Uec2.build();
                Image ami = uec2.getOneOfAmazonLinuxAmi(ec2);
                AWSCredentialsProviderChain ec2Profile = new AWSCredentialsProviderChain(
                                new ProfileCredentialsProvider(profile));
                AmazonEC2 ec2o = AmazonEC2ClientBuilder.standard().withCredentials(ec2Profile)
                                .withRegion(General.PROFILE_REGION.get(profile).id()).build();
                RunInstancesRequest realreq = new RunInstancesRequest().withImageId(ami.imageId())
                                .withInstanceType(instanceType).withMinCount(Integer.parseInt(count))
                                .withMaxCount(Integer.parseInt(count));
                try {
                        AmazonServiceException drr = ec2o.dryRun(realreq).getDryRunResponse();
                        sk.printResult(true, drr.getMessage());
                        //sk.printResult(true, drr.getCause().getMessage());
                } catch (AmazonClientException ex) {
                        sk.printResult(true, ex.getCause().getMessage());
                }
        }

        /**
         * Dry run EC2 spot. A good tool to test the instance type and capacity in
         * region. Be noted the test AMI is Linux.
         */
        public void dryRunEc2Spot(String instanceType, String count, String profile) throws Exception {
                HELPER.help(instanceType, "<instance-type> <instance-count> <profile>");
                General.init(profile);
                Ec2Client ec2 = (Ec2Client) Clients.getClientByServiceClass(Clients.EC2, profile);
                Uec2 uec2 = Uec2.build();
                Image ami = uec2.getOneOfAmazonLinuxAmi(ec2);
                AWSCredentialsProviderChain ec2Profile = new AWSCredentialsProviderChain(
                                new ProfileCredentialsProvider(profile));
                AmazonEC2 ec2o = AmazonEC2ClientBuilder.standard().withCredentials(ec2Profile)
                                .withRegion(General.PROFILE_REGION.get(profile).id()).build();
                RunInstancesRequest realreq = new RunInstancesRequest().withImageId(ami.imageId())
                                .withInstanceMarketOptions(
                                                new InstanceMarketOptionsRequest().withMarketType(MarketType.Spot))
                                .withInstanceType(instanceType).withMinCount(Integer.parseInt(count))
                                .withMaxCount(Integer.parseInt(count));
                try {
                        AmazonServiceException drr = ec2o.dryRun(realreq).getDryRunResponse();
                        sk.printResult(true, drr.getMessage());
                        //sk.printResult(true, drr.getCause().getMessage());
                } catch (AmazonClientException ex) {
                        sk.printResult(true, ex.getCause().getMessage());
                }
        }
}