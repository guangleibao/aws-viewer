package awsviewer.common;

import java.util.List;

import awsviewer.conf.Speaker;
import awsviewer.inf.CUtil;
import software.amazon.awssdk.core.SdkClient;
import software.amazon.awssdk.services.elasticloadbalancing.ElasticLoadBalancingClient;
import software.amazon.awssdk.services.elasticloadbalancing.model.AdditionalAttribute;
import software.amazon.awssdk.services.elasticloadbalancing.model.DescribeLoadBalancerAttributesRequest;
import software.amazon.awssdk.services.elasticloadbalancing.model.LoadBalancerAttributes;
import software.amazon.awssdk.services.elasticloadbalancing.model.LoadBalancerDescription;

public class Uelasticloadbalancing implements CUtil {

    public static final Uelasticloadbalancing build() {
        return new Uelasticloadbalancing();
    }

    /**
     * Show all classic ELBs.
     */
    @Override
    public void printAllResource(SdkClient c, Speaker skBranch) {
        ElasticLoadBalancingClient elb = (ElasticLoadBalancingClient) c;
        Speaker mSpeaker = skBranch.clone();
        mSpeaker.printResourceSubTitle(null);
        List<LoadBalancerDescription> lbds = elb.describeLoadBalancers().loadBalancerDescriptions();
        int elbCount = 0; 
        for(LoadBalancerDescription lbd:lbds){
            Speaker lbSpeaker = mSpeaker.clone();
            lbSpeaker.smartPrintResult(true, Speaker.ELB+" ELB-"+(++elbCount)+"-classic: "+lbd.loadBalancerName()+": "+lbd.scheme()+", "+lbd.dnsName());
        }
   }

    /**
     * Get ELB attributes.
     */
    public String getAttributes(ElasticLoadBalancingClient elb, String elbName){
        LoadBalancerAttributes attrs = elb.describeLoadBalancerAttributes(DescribeLoadBalancerAttributesRequest.builder().loadBalancerName(elbName).build()).loadBalancerAttributes();
        List<AdditionalAttribute> aas = attrs.additionalAttributes();
        StringBuffer sb = new StringBuffer();
        for(AdditionalAttribute aa:aas){
            sb.append(aa.key()+":"+aa.value()+", ");
        }
        return attrs.connectionDraining().toString()+", "+
            attrs.connectionSettings().toString()+", "+
            attrs.crossZoneLoadBalancing().toString()+", "+
            attrs.accessLog().toString()+", "+
            sb.toString();
    }

}