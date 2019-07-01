package awsviewer.common;

import java.util.Iterator;
import java.util.List;

import awsviewer.conf.Clients;
import awsviewer.conf.Helper;
import awsviewer.conf.Speaker;
import awsviewer.inf.CUtil;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeSecurityGroupsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeVpcsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeVpcsResponse;
import software.amazon.awssdk.services.ec2.model.Filter;
import software.amazon.awssdk.services.ec2.model.Vpc;
import software.amazon.awssdk.services.rds.RdsClient;
import software.amazon.awssdk.services.rds.model.DBInstance;
import software.amazon.awssdk.services.rds.model.DescribeDbInstancesResponse;

public class Urds implements CUtil {

    public static final Urds build() {
        return new Urds();
    }

    @Override
    public void printAllResource(Speaker skBranch) throws Exception {

    }

    @Override
    public void printVpcResource(String andOrOr, String mode, Speaker skBranch, Filter... filters) throws Exception {
        Speaker mSpeaker = skBranch.clone();
        RdsClient rds = (RdsClient) Clients.getClientByServiceClass(Clients.RDS, mSpeaker.getProfile());
        Ec2Client ec2 = (Ec2Client) Clients.getClientByServiceClass(Clients.EC2, mSpeaker.getProfile());
        Uec2 uec2 = skBranch.getUec2();
        Iterator<DescribeVpcsResponse> iterVpcs = null;
        if (andOrOr.equals("OR")) {
            for (Filter f : filters) {
                iterVpcs = ec2.describeVpcsPaginator(DescribeVpcsRequest.builder().filters(f).build()).iterator();
                while (iterVpcs.hasNext()) {
                    List<Vpc> vpcs = iterVpcs.next().vpcs();
                    for (Vpc vpc : vpcs) {
                        this.printVpcDatabase(rds, ec2, uec2, vpc.vpcId(), mode, mSpeaker);
                    }
                }
            }
        } else if (andOrOr.equals("AND")) {
            iterVpcs = ec2.describeVpcsPaginator(DescribeVpcsRequest.builder().filters(filters).build()).iterator();
            while (iterVpcs.hasNext()) {
                List<Vpc> vpcs = iterVpcs.next().vpcs();
                for (Vpc vpc : vpcs) {
                    this.printVpcDatabase(rds, ec2, uec2, vpc.vpcId(), mode, mSpeaker);
                }
            }
        }
    }

    public void printVpcDatabase(RdsClient rds, Ec2Client ec2, Uec2 uec2, String vpcId, String mode, Speaker skBranch){
        Speaker vSpeaker = skBranch.clone();
        Helper hp = new Helper();
        // -------------------- Begin RDS
        vSpeaker.printTitle("RDS:");
        int rdsCount = 0;
        Iterator<DescribeDbInstancesResponse> iterRdss = rds.describeDBInstancesPaginator().iterator();
        while (iterRdss.hasNext()) {
            List<DBInstance> rdss = iterRdss.next().dbInstances();
            for (DBInstance instance : rdss) {
                Speaker rdsSpeaker = vSpeaker.clone();
                String oneSgId = instance.vpcSecurityGroups().get(0).vpcSecurityGroupId();
                String oneVpcId = ec2
                        .describeSecurityGroups(
                                DescribeSecurityGroupsRequest.builder().groupIds(oneSgId).build())
                        .securityGroups().get(0).vpcId();
                if (oneVpcId.equals(vpcId)) {
                    String iops = null;
                    if (instance.storageType().equals("gp2")) {
                        iops = "gp2-rule";
                    }
                    if (mode.equals(PLAIN)) {
                        rdsSpeaker.smartPrintResult(true,
                                Speaker.DB + " RDS-" + (++rdsCount) + ": " + instance.dbInstanceIdentifier()
                                        + ", DB:" + instance.dbName() + ", " + instance.engine() + ", "
                                        + instance.engineVersion() + ", status:"
                                        + instance.dbInstanceStatus() + ", multi-AZ:" + instance.multiAZ()
                                        + ", admin:" + instance.masterUsername() + ", size:"
                                        + instance.allocatedStorage() + "G" + ", " + instance.storageType()
                                        + ", iops:"
                                        + (instance.storageType().equals("gp2") ? iops : instance.iops()));
                    } else {
                        rdsSpeaker.smartPrintResult(true,
                                Speaker.DB + " RDS-" + (++rdsCount) + ": " + instance.dbInstanceIdentifier()
                                        + ", DB:" + instance.dbName() + ", " + instance.engine() + ", "
                                        + instance.engineVersion() + ", status:"
                                        + instance.dbInstanceStatus() + ", multi-AZ:" + instance.multiAZ()
                                        + ", admin:" + hp.redact(instance.masterUsername()) + ", size:"
                                        + instance.allocatedStorage() + "G" + ", " + instance.storageType()
                                        + ", iops:"
                                        + (instance.storageType().equals("gp2") ? iops : instance.iops()));
                    }
                    if (instance.endpoint() != null) {
                        if (mode.equals(PLAIN)) {
                            rdsSpeaker.printResult(true, "endpoint: " + instance.endpoint().address() + ":"
                                    + instance.endpoint().port() + "/" + instance.dbName());
                        } else {
                            rdsSpeaker.printResult(true,
                                    "endpoint: " + hp.redact(instance.endpoint().address() + ":"
                                            + instance.endpoint().port() + "/" + instance.dbName()));
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
        vSpeaker.printResult(true, "TTL-RDS:" + rdsCount + "\n");
        // -------------------- End RDS
    }

}