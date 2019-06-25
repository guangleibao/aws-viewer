package awsviewer.common;

import java.util.List;

import awsviewer.conf.Speaker;
import awsviewer.inf.CUtil;
import software.amazon.awssdk.core.SdkClient;
import software.amazon.awssdk.services.elasticloadbalancingv2.ElasticLoadBalancingV2Client;
import software.amazon.awssdk.services.elasticloadbalancingv2.model.DescribeLoadBalancerAttributesRequest;
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