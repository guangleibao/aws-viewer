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
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.FunctionConfiguration;
import software.amazon.awssdk.services.lambda.model.ListFunctionsResponse;

public class Ulambda implements CUtil {

    public static final Ulambda build() {
        return new Ulambda();
    }

    @Override
    public void printAllResource(Speaker skBranch) throws Exception {

    }

    @Override
    public void printVpcResource(String andOrOr, String mode, Speaker skBranch, Filter... filters) throws Exception {
        Speaker mSpeaker = skBranch.clone();
        LambdaClient lambda = (LambdaClient) Clients.getClientByServiceClass(Clients.LAMBDA, mSpeaker.getProfile());
        Ec2Client ec2 = (Ec2Client) Clients.getClientByServiceClass(Clients.EC2, mSpeaker.getProfile());
        Uec2 uec2 = skBranch.getUec2();
        Iterator<DescribeVpcsResponse> iterVpcs = null;
        if (andOrOr.equals("OR")) {
            for (Filter f : filters) {
                iterVpcs = ec2.describeVpcsPaginator(DescribeVpcsRequest.builder().filters(f).build()).iterator();
                while (iterVpcs.hasNext()) {
                    List<Vpc> vpcs = iterVpcs.next().vpcs();
                    for (Vpc vpc : vpcs) {
                        this.printVpcFunction(lambda, ec2, uec2, vpc.vpcId(), mode, mSpeaker);
                    }
                }
            }
        } else if (andOrOr.equals("AND")) {
            iterVpcs = ec2.describeVpcsPaginator(DescribeVpcsRequest.builder().filters(filters).build()).iterator();
            while (iterVpcs.hasNext()) {
                List<Vpc> vpcs = iterVpcs.next().vpcs();
                for (Vpc vpc : vpcs) {
                    this.printVpcFunction(lambda, ec2, uec2, vpc.vpcId(), mode, mSpeaker);
                }
            }
        }
    }

    public void printVpcFunction(LambdaClient lambda, Ec2Client ec2, Uec2 uec2, String vpcId, String mode, Speaker skBranch){
        Speaker vSpeaker = skBranch.clone();
        Helper hp = new Helper();
        // -------------------- Begin Lambda
        vSpeaker.printTitle("Lambda:");
        int lCount = 0;
        Iterator<ListFunctionsResponse> iterFcs = lambda.listFunctionsPaginator().iterator();
        while (iterFcs.hasNext()) {
            List<FunctionConfiguration> fcs = iterFcs.next().functions();
            for (FunctionConfiguration fc : fcs) {
                Speaker fSpeaker = vSpeaker.clone();
                if (fc.vpcConfig() != null && fc.vpcConfig().vpcId().equals(vpcId)) {
                    if (mode.equals(PLAIN)) {
                        fSpeaker.smartPrintResult(true,
                                Speaker.LAMBDA + " LAMBDA-" + (++lCount) + ": " + fc.functionName() + ", "
                                        + fc.functionArn() + ", " + fc.version() + ", runtime:"
                                        + fc.runtime() + ", handler:" + fc.handler() + ", mem:"
                                        + fc.memorySize() + "m, ttl:" + fc.timeout() + "s");
                        fSpeaker.printResult(true, "role: " + fc.role());
                    } else {
                        fSpeaker.smartPrintResult(true,
                                Speaker.LAMBDA + " LAMBDA-" + (++lCount) + ": " + fc.functionName() + ", "
                                        + hp.redactArn(fc.functionArn()) + ", " + fc.version()
                                        + ", runtime:" + fc.runtime() + ", handler:" + fc.handler()
                                        + ", mem:" + fc.memorySize() + "m, ttl:" + fc.timeout() + "s");
                        fSpeaker.printResult(true, "role: " + hp.redactArn(fc.role()));
                    }
                    fSpeaker.printResult(true,
                            "protected-by: " + uec2.decodeSgsByIds(ec2, fc.vpcConfig().securityGroupIds()));
                    fSpeaker.printResult(true, uec2.decodeSubnetsById(ec2, fc.vpcConfig().subnetIds()));
                }
            }
        }
        vSpeaker.printResult(true, "TTL-LAMBDA:" + lCount + "\n");
        // -------------------- End Lambda
    }

}