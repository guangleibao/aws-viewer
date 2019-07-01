package awsviewer.common;

import java.util.Iterator;
import java.util.List;

import awsviewer.conf.Clients;
import awsviewer.conf.Helper;
import awsviewer.conf.Speaker;
import awsviewer.inf.CUtil;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeVpcsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeVpcsResponse;
import software.amazon.awssdk.services.ec2.model.Filter;
import software.amazon.awssdk.services.ec2.model.Vpc;
import software.amazon.awssdk.services.elasticloadbalancing.ElasticLoadBalancingClient;
import software.amazon.awssdk.services.elasticloadbalancing.model.AdditionalAttribute;
import software.amazon.awssdk.services.elasticloadbalancing.model.DescribeLoadBalancerAttributesRequest;
import software.amazon.awssdk.services.elasticloadbalancing.model.DescribeLoadBalancersResponse;
import software.amazon.awssdk.services.elasticloadbalancing.model.ListenerDescription;
import software.amazon.awssdk.services.elasticloadbalancing.model.LoadBalancerAttributes;
import software.amazon.awssdk.services.elasticloadbalancing.model.LoadBalancerDescription;

public class Uelasticloadbalancing implements CUtil {

    public static final Uelasticloadbalancing build() {
        return new Uelasticloadbalancing();
    }

    /**
     * Show all classic ELBs.
     * 
     * @throws Exception
     */
    @Override
    public void printAllResource(Speaker skBranch) throws Exception {
        ElasticLoadBalancingClient elb = (ElasticLoadBalancingClient) Clients.getClientByServiceClass(Clients.ELASTICLOADBALANCING, skBranch.getProfile());
        Speaker mSpeaker = skBranch.clone();
        mSpeaker.printResourceSubTitle(null);
        List<LoadBalancerDescription> lbds = elb.describeLoadBalancers().loadBalancerDescriptions();
        int elbCount = 0;
        for (LoadBalancerDescription lbd : lbds) {
            Speaker lbSpeaker = mSpeaker.clone();
            lbSpeaker.smartPrintResult(true, Speaker.ELB + " ELB-" + (++elbCount) + "-classic: "
                    + lbd.loadBalancerName() + ": " + lbd.scheme() + ", " + lbd.dnsName());
        }
    }

    @Override
    public void printVpcResource(String andOrOr, String mode, Speaker skBranch, Filter... filters)
            throws Exception {
        Speaker mSpeaker = skBranch.clone();
        ElasticLoadBalancingClient elb = (ElasticLoadBalancingClient) Clients.getClientByServiceClass(Clients.ELASTICLOADBALANCING, skBranch.getProfile());
        Ec2Client ec2 = (Ec2Client) Clients.getClientByServiceClass(Clients.EC2, mSpeaker.getProfile());
        Uec2 uec2 = skBranch.getUec2();
        Iterator<DescribeVpcsResponse> iterVpcs = null;
        if (andOrOr.equals("OR")) {
            for (Filter f : filters) {
                iterVpcs = ec2.describeVpcsPaginator(DescribeVpcsRequest.builder().filters(f).build()).iterator();
                while (iterVpcs.hasNext()) {
                    List<Vpc> vpcs = iterVpcs.next().vpcs();
                    for (Vpc vpc : vpcs) {
                        this.printVpcElb(elb, ec2, uec2, vpc.vpcId(), mode, mSpeaker);
                    }
                }
            }
        } else if (andOrOr.equals("AND")) {
            iterVpcs = ec2.describeVpcsPaginator(DescribeVpcsRequest.builder().filters(filters).build()).iterator();
            while (iterVpcs.hasNext()) {
                List<Vpc> vpcs = iterVpcs.next().vpcs();
                for (Vpc vpc : vpcs) {
                    this.printVpcElb(elb, ec2, uec2, vpc.vpcId(), mode, mSpeaker);
                }
            }
        }
    }

    public void printVpcElb(ElasticLoadBalancingClient elb, Ec2Client ec2, Uec2 uec2, String vpcId, String mode, Speaker skBranch){
        Helper hp = new Helper();
        // -------------------- Begin ELB
        Speaker vSpeaker = skBranch.clone();
        vSpeaker.printTitle("Elastic Load Balancing - Classic:");
        int elbCount = 0;
        Iterator<DescribeLoadBalancersResponse> iterLbs = elb
                .describeLoadBalancersPaginator(
                        software.amazon.awssdk.services.elasticloadbalancing.model.DescribeLoadBalancersRequest
                                .builder().build())
                .iterator();
        while (iterLbs.hasNext()) {
            List<LoadBalancerDescription> lbs = iterLbs
                    .next().loadBalancerDescriptions();
            for (LoadBalancerDescription desc : lbs) {
                List<ListenerDescription> lds = desc.listenerDescriptions();
                Speaker lbSpeaker = vSpeaker.clone();
                if (desc.vpcId().equals(vpcId)) {
                    List<String> subnets = desc.subnets();
                    if (mode.equals(PLAIN)) {
                        lbSpeaker.smartPrintResult(true,
                                Speaker.ELB + " ELB-" + (++elbCount) + "-classic: "
                                        + desc.loadBalancerName() + ", " + desc.scheme() + ", " + ", dns: "
                                        + desc.dnsName());
                        lbSpeaker.smartPrintResult(true, uec2.decodeSubnetsById(ec2, subnets));
                        for (ListenerDescription ld : lds) {
                            lbSpeaker.printResult(true,
                                    "listener: " + ld.listener().protocol() + ", "
                                            + ld.listener().loadBalancerPort() + ", ssl:"
                                            + ld.listener().sslCertificateId());
                        }
                    } else {
                        lbSpeaker.smartPrintResult(true,
                                Speaker.ELB + " ELB-" + (++elbCount) + "-classic: "
                                        + desc.loadBalancerName() + ", " + desc.scheme() + ", " + ", dns: "
                                        + hp.redact(desc.dnsName()));
                        lbSpeaker.smartPrintResult(true, uec2.decodeSubnetsById(ec2, subnets));
                        for (ListenerDescription ld : lds) {
                            lbSpeaker.printResult(true,
                                    "listener: " + ld.listener().protocol() + ", "
                                            + ld.listener().loadBalancerPort() + ", ssl:"
                                            + ld.listener().sslCertificateId());
                        }
                    }
                    lbSpeaker.printResult(true, this.getAttributes(elb, desc.loadBalancerName()));
                }
            }
        }
        vSpeaker.printResult(true, "TTL-ELB:" + elbCount + "\n");
        // -------------------- End ELB
    }

    /**
     * Get ELB attributes.
     */
    public String getAttributes(ElasticLoadBalancingClient elb, String elbName) {
        LoadBalancerAttributes attrs = elb
                .describeLoadBalancerAttributes(
                        DescribeLoadBalancerAttributesRequest.builder().loadBalancerName(elbName).build())
                .loadBalancerAttributes();
        List<AdditionalAttribute> aas = attrs.additionalAttributes();
        StringBuffer sb = new StringBuffer();
        for (AdditionalAttribute aa : aas) {
            sb.append(aa.key() + ":" + aa.value() + ", ");
        }
        return attrs.connectionDraining().toString() + ", " + attrs.connectionSettings().toString() + ", "
                + attrs.crossZoneLoadBalancing().toString() + ", " + attrs.accessLog().toString() + ", "
                + sb.toString();
    }

}