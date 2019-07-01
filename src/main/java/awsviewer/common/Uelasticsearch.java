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
import software.amazon.awssdk.services.elasticsearch.ElasticsearchClient;
import software.amazon.awssdk.services.elasticsearch.model.DescribeElasticsearchDomainRequest;
import software.amazon.awssdk.services.elasticsearch.model.DomainInfo;
import software.amazon.awssdk.services.elasticsearch.model.ElasticsearchDomainStatus;
import software.amazon.awssdk.services.elasticsearch.model.ListDomainNamesRequest;

public class Uelasticsearch implements CUtil {

    public static final Uelasticsearch build() {
        return new Uelasticsearch();
    }

    @Override
    public void printAllResource(Speaker skBranch) throws Exception {

    }

    @Override
    public void printVpcResource(String andOrOr, String mode, Speaker skBranch, Filter... filters) throws Exception {
        Speaker mSpeaker = skBranch.clone();
        ElasticsearchClient es = (ElasticsearchClient) Clients.getClientByServiceClass(Clients.ELASTICSEARCH, mSpeaker.getProfile());
        Ec2Client ec2 = (Ec2Client) Clients.getClientByServiceClass(Clients.EC2, mSpeaker.getProfile());
        Uec2 uec2 = skBranch.getUec2();
        Iterator<DescribeVpcsResponse> iterVpcs = null;
        if (andOrOr.equals("OR")) {
            for (Filter f : filters) {
                iterVpcs = ec2.describeVpcsPaginator(DescribeVpcsRequest.builder().filters(f).build()).iterator();
                while (iterVpcs.hasNext()) {
                    List<Vpc> vpcs = iterVpcs.next().vpcs();
                    for (Vpc vpc : vpcs) {
                        this.printVpcDomain(es, ec2, uec2, vpc.vpcId(), mode, mSpeaker);
                    }
                }
            }
        } else if (andOrOr.equals("AND")) {
            iterVpcs = ec2.describeVpcsPaginator(DescribeVpcsRequest.builder().filters(filters).build()).iterator();
            while (iterVpcs.hasNext()) {
                List<Vpc> vpcs = iterVpcs.next().vpcs();
                for (Vpc vpc : vpcs) {
                    this.printVpcDomain(es, ec2, uec2, vpc.vpcId(), mode, mSpeaker);
                }
            }
        }
    }

    public void printVpcDomain(ElasticsearchClient es, Ec2Client ec2, Uec2 uec2, String vpcId, String mode, Speaker skBranch) {
        Speaker vSpeaker = skBranch.clone();
        Helper hp = new Helper();
        // -------------------- Begin ES
        vSpeaker.printTitle("ElasticSearch:");
        int esCount = 0;
        List<DomainInfo> dis = es.listDomainNames(ListDomainNamesRequest.builder().build()).domainNames();
        for (DomainInfo di : dis) {
            Speaker esSpeaker = vSpeaker.clone();
            ElasticsearchDomainStatus esde = es.describeElasticsearchDomain(
                    DescribeElasticsearchDomainRequest.builder().domainName(di.domainName()).build())
                    .domainStatus();
            if (esde.vpcOptions().vpcId().equals(vpcId)) {
                esSpeaker.smartPrintResult(true, Speaker.ES + " ES-Domain-" + (++esCount) + ": "
                        + di.domainName() + ", " + esde.elasticsearchVersion() + ", master-count:"
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
        vSpeaker.printResult(true, "TTL-ES:" + esCount + "\n");
        // -------------------- End ES
    }


}