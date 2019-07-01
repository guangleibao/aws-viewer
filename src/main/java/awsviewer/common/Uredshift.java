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
import software.amazon.awssdk.services.redshift.RedshiftClient;
import software.amazon.awssdk.services.redshift.model.Cluster;
import software.amazon.awssdk.services.redshift.model.DescribeClustersResponse;

public class Uredshift implements CUtil {

    public static final Uredshift build(){
        return new Uredshift();
    }

    @Override
    public void printAllResource(Speaker skBranch) throws Exception {

    }

    @Override
    public void printVpcResource(String andOrOr, String mode, Speaker skBranch, Filter... filters) throws Exception {
        Speaker mSpeaker = skBranch.clone();
        RedshiftClient rs = (RedshiftClient) Clients.getClientByServiceClass(Clients.REDSHIFT, mSpeaker.getProfile());
        Ec2Client ec2 = (Ec2Client) Clients.getClientByServiceClass(Clients.EC2, mSpeaker.getProfile());
        Uec2 uec2 = skBranch.getUec2();
        Iterator<DescribeVpcsResponse> iterVpcs = null;
        if (andOrOr.equals("OR")) {
            for (Filter f : filters) {
                iterVpcs = ec2.describeVpcsPaginator(DescribeVpcsRequest.builder().filters(f).build()).iterator();
                while (iterVpcs.hasNext()) {
                    List<Vpc> vpcs = iterVpcs.next().vpcs();
                    for (Vpc vpc : vpcs) {
                        this.printVpcDatabase(rs, ec2, uec2, vpc.vpcId(), mode, mSpeaker);
                    }
                }
            }
        } else if (andOrOr.equals("AND")) {
            iterVpcs = ec2.describeVpcsPaginator(DescribeVpcsRequest.builder().filters(filters).build()).iterator();
            while (iterVpcs.hasNext()) {
                List<Vpc> vpcs = iterVpcs.next().vpcs();
                for (Vpc vpc : vpcs) {
                    this.printVpcDatabase(rs, ec2, uec2, vpc.vpcId(), mode, mSpeaker);
                }
            }
        }
    }

    public void printVpcDatabase(RedshiftClient rs, Ec2Client ec2, Uec2 uec2, String vpcId, String mode, Speaker skBranch){
        Speaker vSpeaker = skBranch.clone();
        Helper hp = new Helper();
        // -------------------- Begin Redshfit
        vSpeaker.printTitle("Redshift:");
        int rsCount = 0;
        Iterator<DescribeClustersResponse> iterRss = rs.describeClustersPaginator().iterator();
        while (iterRss.hasNext()) {
            List<Cluster> rss = iterRss.next().clusters();
            for (Cluster c : rss) {
                Speaker rsSpeaker = vSpeaker.clone();
                if (c.vpcId().equals(vpcId)) {
                    if (mode.equals(PLAIN)) {
                        rsSpeaker.smartPrintResult(true, Speaker.DB + " REDSHIFT-" + (++rsCount) + ": "
                                + c.clusterIdentifier() + ", " + c.numberOfNodes() + " of " + c.nodeType()
                                + ", DB:" + c.dbName() + ", " + c.clusterVersion() + ", admin:"
                                + c.masterUsername() + ", auto-snap-retention:"
                                + c.automatedSnapshotRetentionPeriod() + ", status:" + c.clusterStatus()
                                + ", m-status: " + (c.modifyStatus() == null ? "n/a" : c.modifyStatus()));
                    } else {
                        rsSpeaker.smartPrintResult(true, Speaker.DB + " REDSHIFT-" + (++rsCount) + ": "
                                + c.clusterIdentifier() + ", " + c.numberOfNodes() + " of " + c.nodeType()
                                + ", DB:" + c.dbName() + ", " + c.clusterVersion() + ", admin:"
                                + hp.redact(c.masterUsername()) + ", auto-snap-retention:"
                                + c.automatedSnapshotRetentionPeriod() + ", status:" + c.clusterStatus()
                                + ", m-status: " + (c.modifyStatus() == null ? "n/a" : c.modifyStatus()));
                    }
                    if (c.endpoint() != null) {
                        if (mode.equals(PLAIN)) {
                            rsSpeaker.printResult(true, "endpoint: " + c.endpoint().address() + ":"
                                    + c.endpoint().port() + "/" + c.dbName());
                        } else {
                            rsSpeaker.printResult(true, "endpoint: " + hp.redact(
                                    c.endpoint().address() + ":" + c.endpoint().port() + "/" + c.dbName()));
                        }
                    }
                    rsSpeaker.printResult(true, "protected-by: "
                            + uec2.vpcSgMemberShipsToSgTagOrNameForRedshift(ec2, c.vpcSecurityGroups()));
                    rsSpeaker.printResult(true, "currently-at: " + c.availabilityZone());
                    rsSpeaker.printResult(true, c.clusterSubnetGroupName() + ": " + uec2
                            .decodeRedshiftClusterSubnetGroupName(ec2, rs, c.clusterSubnetGroupName()));
                }
            }
        }
        vSpeaker.printResult(true, "TTL-REDSHIFT:" + rsCount + "\n");
        // -------------------- End Redshift
    }

}