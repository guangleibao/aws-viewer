package awsviewer.common;

import java.util.Iterator;
import java.util.List;

import awsviewer.conf.Clients;
import awsviewer.conf.Helper;
import awsviewer.conf.Speaker;
import awsviewer.inf.CUtil;
import software.amazon.awssdk.core.SdkClient;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeVpcsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeVpcsResponse;
import software.amazon.awssdk.services.ec2.model.Filter;
import software.amazon.awssdk.services.ec2.model.Vpc;
import software.amazon.awssdk.services.elasticloadbalancingv2.ElasticLoadBalancingV2Client;
import software.amazon.awssdk.services.elasticloadbalancingv2.model.AvailabilityZone;
import software.amazon.awssdk.services.elasticloadbalancingv2.model.DescribeListenersRequest;
import software.amazon.awssdk.services.elasticloadbalancingv2.model.DescribeLoadBalancerAttributesRequest;
import software.amazon.awssdk.services.elasticloadbalancingv2.model.DescribeLoadBalancersRequest;
import software.amazon.awssdk.services.elasticloadbalancingv2.model.DescribeLoadBalancersResponse;
import software.amazon.awssdk.services.elasticloadbalancingv2.model.Listener;
import software.amazon.awssdk.services.elasticloadbalancingv2.model.LoadBalancer;
import software.amazon.awssdk.services.elasticloadbalancingv2.model.LoadBalancerAttribute;

public class Uelasticloadbalancingv2 implements CUtil {

    public static final Uelasticloadbalancingv2 build() {
        return new Uelasticloadbalancingv2();
    }

    /**
     * Print show version 2 ELB.
     */
    @Override
    public void printAllResource(SdkClient c, Speaker skBranch) {
        ElasticLoadBalancingV2Client elb = (ElasticLoadBalancingV2Client) c;
        Speaker mSpeaker = skBranch.clone();
        mSpeaker.printResourceSubTitle(null);
        List<LoadBalancer> lbs = elb.describeLoadBalancers().loadBalancers();
        int elbv2Count = 0;
        for (LoadBalancer l2 : lbs) {
            Speaker lbv2Speaker = mSpeaker.clone();
            lbv2Speaker.smartPrintResult(true, Speaker.ELB+" ELB-" + (++elbv2Count) + "-" + l2.type() + ": "
							+ l2.loadBalancerName() + ", " + l2.scheme() + ", "+l2.ipAddressTypeAsString()+", "+l2.state().codeAsString()+", "+ l2.dnsName());
            lbv2Speaker.smartPrintResult(true, this.getAttributes(elb, l2.loadBalancerArn(),0,3));    
            lbv2Speaker.printResult(true, this.getAttributes(elb, l2.loadBalancerArn(),3,100));
        }
    }

    public void printVpcResource(SdkClient c, Filter[] filters, String andOrOr, String mode, Speaker skBranch)
            throws Exception {
        Speaker mSpeaker = skBranch.clone();
        ElasticLoadBalancingV2Client elbv2 = (ElasticLoadBalancingV2Client) c;
        Ec2Client ec2 = (Ec2Client) Clients.getClientByServiceClass(Clients.EC2, mSpeaker.getProfile());
        Uelasticloadbalancingv2 uelbv2 = Uelasticloadbalancingv2.build();
        Uec2 uec2 = Uec2.build();
        Iterator<DescribeVpcsResponse> iterVpcs = null;
        if (andOrOr.equals("OR")) {
            for (Filter f : filters) {
                iterVpcs = ec2.describeVpcsPaginator(DescribeVpcsRequest.builder().filters(f).build()).iterator();
                while (iterVpcs.hasNext()) {
                    List<Vpc> vpcs = iterVpcs.next().vpcs();
                    for (Vpc vpc : vpcs) {
                        this.printVpcElbv2(elbv2, ec2, uelbv2, uec2, vpc.vpcId(), mode, mSpeaker);
                    }
                }
            }
        } else if (andOrOr.equals("AND")) {
            iterVpcs = ec2.describeVpcsPaginator(DescribeVpcsRequest.builder().filters(filters).build()).iterator();
            while (iterVpcs.hasNext()) {
                List<Vpc> vpcs = iterVpcs.next().vpcs();
                for (Vpc vpc : vpcs) {
                    this.printVpcElbv2(elbv2, ec2, uelbv2, uec2, vpc.vpcId(), mode, mSpeaker);
                }
            }
        }
    }

    public void printVpcElbv2(ElasticLoadBalancingV2Client elbv2, Ec2Client ec2, Uelasticloadbalancingv2 uelbv2,
        Uec2 uec2, String vpcId, String mode, Speaker skBranch) {
        Speaker mSpeaker = skBranch.clone();
        Helper hp = new Helper();
        // -------------------- Begin ELB v2
        mSpeaker.printTitle("Elastic Load Balancing V2 - Application & Network:");
        int elbv2Count = 0;
        Iterator<DescribeLoadBalancersResponse> iterLbv2s = elbv2
                .describeLoadBalancersPaginator(DescribeLoadBalancersRequest.builder().build()).iterator();
        while (iterLbv2s.hasNext()) {
            List<LoadBalancer> lbv2s = iterLbv2s.next().loadBalancers();
            for (LoadBalancer l2 : lbv2s) {
                Speaker lbv2Speaker = mSpeaker.clone();
                if (l2.vpcId().equals(vpcId)) {
                    List<AvailabilityZone> azs = l2.availabilityZones();
                    List<Listener> ls = elbv2
                            .describeListeners(
                                    DescribeListenersRequest.builder().loadBalancerArn(l2.loadBalancerArn()).build())
                            .listeners();
                    if (mode.equals(PLAIN)) {
                        lbv2Speaker.smartPrintResult(true,
                                Speaker.ELB + " ELB-" + (++elbv2Count) + "-" + l2.type() + ": " + l2.loadBalancerName()
                                        + ", " + l2.schemeAsString() + ", " + l2.ipAddressTypeAsString() + ", "
                                        + l2.state().codeAsString() + ", " + l2.dnsName());
                        lbv2Speaker.smartPrintResult(true, uec2.decodeElbv2AZs(ec2, azs));
                    } else {
                        lbv2Speaker.smartPrintResult(true,
                                Speaker.ELB + " ELB-" + (++elbv2Count) + "-" + l2.type() + ": " + l2.loadBalancerName()
                                        + ", " + l2.schemeAsString() + ", " + l2.ipAddressTypeAsString() + ", "
                                        + l2.state().codeAsString() + ", dns: " + hp.redact(l2.dnsName()));
                        lbv2Speaker.smartPrintResult(true, uec2.decodeElbv2AZs(ec2, azs));
                    }
                    for (Listener l : ls) {
                        lbv2Speaker.printResult(true,
                                "listener: " + l.protocolAsString() + ", " + l.port() + ", ssl:" + l.sslPolicy());
                    }
                    lbv2Speaker.printResult(true, uelbv2.getAttributes(elbv2, l2.loadBalancerArn(), 0, 3));
                    lbv2Speaker.printResult(true, uelbv2.getAttributes(elbv2, l2.loadBalancerArn(), 3, 100));
                }
            }
        }
        mSpeaker.printResult(true, "TTL-ELBv2:" + elbv2Count + "\n");
        // -------------------- End ELB v2
    }

    /**
     * Get ELBv2 attributes.
     */
    public String getAttributes(ElasticLoadBalancingV2Client elbv2, String elbv2Arn, int startIdx, int count){
        List<LoadBalancerAttribute> attrs = elbv2.describeLoadBalancerAttributes(DescribeLoadBalancerAttributesRequest.builder().loadBalancerArn(elbv2Arn).build()).attributes();
        StringBuffer sb = new StringBuffer();
        int i=0;
        int c=0;
        for(LoadBalancerAttribute attr:attrs){
            if(i>=startIdx && c<count){
                sb.append(attr.key()+":"+attr.value()+", ");
                c++;
            }
            i++;
            if(c==count){
                break;
            }
        }
        return sb.toString();
    }

}