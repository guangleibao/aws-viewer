package awsviewer.common;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import awsviewer.conf.Clients;
import awsviewer.conf.Helper;
import awsviewer.conf.Speaker;
import awsviewer.inf.CUtil;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeVpcsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeVpcsResponse;
import software.amazon.awssdk.services.ec2.model.Filter;
import software.amazon.awssdk.services.ec2.model.Vpc;
import software.amazon.awssdk.services.elasticache.ElastiCacheClient;
import software.amazon.awssdk.services.elasticache.model.CacheCluster;
import software.amazon.awssdk.services.elasticache.model.CacheSubnetGroup;
import software.amazon.awssdk.services.elasticache.model.DescribeCacheClustersResponse;
import software.amazon.awssdk.services.elasticache.model.DescribeCacheSubnetGroupsRequest;
import software.amazon.awssdk.services.elasticache.model.DescribeCacheSubnetGroupsResponse;
import software.amazon.awssdk.services.elasticache.model.DescribeReplicationGroupsRequest;
import software.amazon.awssdk.services.elasticache.model.ReplicationGroup;

public class Uelasticache implements CUtil {

    public static final Uelasticache build() {
        return new Uelasticache();
    }

    @Override
    public void printAllResource(Speaker skBranch) throws Exception {

    }

    @Override
    public void printVpcResource(String andOrOr, String mode, Speaker skBranch, Filter... filters) throws Exception {
        Speaker mSpeaker = skBranch.clone();
        ElastiCacheClient cache = (ElastiCacheClient) Clients.getClientByServiceClass(Clients.ELASTICACHE, mSpeaker.getProfile());
        Ec2Client ec2 = (Ec2Client) Clients.getClientByServiceClass(Clients.EC2, mSpeaker.getProfile());
        Uec2 uec2 = skBranch.getUec2();
        Iterator<DescribeVpcsResponse> iterVpcs = null;
        if (andOrOr.equals("OR")) {
            for (Filter f : filters) {
                iterVpcs = ec2.describeVpcsPaginator(DescribeVpcsRequest.builder().filters(f).build()).iterator();
                while (iterVpcs.hasNext()) {
                    List<Vpc> vpcs = iterVpcs.next().vpcs();
                    for (Vpc vpc : vpcs) {
                        this.printVpcCache(cache, ec2, uec2, vpc.vpcId(), mode, mSpeaker);
                    }
                }
            }
        } else if (andOrOr.equals("AND")) {
            iterVpcs = ec2.describeVpcsPaginator(DescribeVpcsRequest.builder().filters(filters).build()).iterator();
            while (iterVpcs.hasNext()) {
                List<Vpc> vpcs = iterVpcs.next().vpcs();
                for (Vpc vpc : vpcs) {
                    this.printVpcCache(cache, ec2, uec2, vpc.vpcId(), mode, mSpeaker);
                }
            }
        }
    }

    public void printVpcCache(ElastiCacheClient cache, Ec2Client ec2, Uec2 uec2, String vpcId, String mode, Speaker skBranch){
        Speaker vSpeaker = skBranch.clone();
        Helper hp = new Helper();
        // -------------------- Begin ElastiCache
        vSpeaker.printTitle("ElastiCache:");
        int mdCount = 0;
        int rdCount = 0;
        Map<String, String> cacheSubnetGroupNameToSubnetAzs = new Hashtable<String, String>();
        Iterator<DescribeCacheSubnetGroupsResponse> iterCacheSubnetGroups = cache
                .describeCacheSubnetGroupsPaginator(DescribeCacheSubnetGroupsRequest.builder().build())
                .iterator();
        while (iterCacheSubnetGroups.hasNext()) {
            List<CacheSubnetGroup> cacheSubnetGroups = iterCacheSubnetGroups.next().cacheSubnetGroups();
            for (CacheSubnetGroup csg : cacheSubnetGroups) {
                if (csg.vpcId().equals(vpcId)) {
                    cacheSubnetGroupNameToSubnetAzs.put(csg.cacheSubnetGroupName(),
                            uec2.decodeCacheSubnets(ec2, csg.subnets()));
                }
            }
        }
        String repGroupId = null;
        String cddGroupId = null;
        Iterator<DescribeCacheClustersResponse> iterCacheClusters = cache.describeCacheClustersPaginator()
                .iterator();
        while (iterCacheClusters.hasNext()) {
            List<CacheCluster> cacheClusters = iterCacheClusters.next().cacheClusters();
            for (CacheCluster cc : cacheClusters) {
                Speaker ccSpeaker = vSpeaker.clone();
                String engine = cc.engine();
                if (engine.equals("redis")
                        && cacheSubnetGroupNameToSubnetAzs.containsKey(cc.cacheSubnetGroupName())) {
                    cddGroupId = cc.replicationGroupId();
                    if (repGroupId == null || !cddGroupId.equals(repGroupId)) {
                        repGroupId = cddGroupId;
                        ReplicationGroup rg = cache
                                .describeReplicationGroups(DescribeReplicationGroupsRequest.builder()
                                        .replicationGroupId(repGroupId).build())
                                .replicationGroups().get(0);
                        ccSpeaker.smartPrintResult(true, Speaker.CACHE + " REDIS-" + (++rdCount) + ": "
                                + rg.replicationGroupId() + ", " + cc.engine() + ", " + cc.engineVersion()
                                + ", " + rg.cacheNodeType() + ", " + cc.cacheClusterStatus()
                                + ", atRestEnc:" + cc.atRestEncryptionEnabled() + ", transitEnc:"
                                + cc.transitEncryptionEnabled()
                                + (rg.nodeGroups().size() == 0 ? ""
                                        : ", shard:" + rg.nodeGroups().size() + ", replica:"
                                                + rg.nodeGroups().get(0).nodeGroupMembers().size()));
                        if (rg.configurationEndpoint() != null) {
                            if (mode.equals(PLAIN)) {
                                ccSpeaker.printResult(true,
                                        "endpoint: " + rg.configurationEndpoint().address() + ":"
                                                + rg.configurationEndpoint().port());
                            } else {
                                ccSpeaker.printResult(true,
                                        "endpoint: " + hp.redact(rg.configurationEndpoint().address() + ":"
                                                + rg.configurationEndpoint().port()));
                            }
                        }
                        ccSpeaker.printResult(true, "maintenance-utc: " + cc.preferredMaintenanceWindow());
                        ccSpeaker.printResult(true, "protected-by: "
                                + uec2.sgMemberShipsToSgTagOrName(ec2, cc.securityGroups()));
                        ccSpeaker.printResult(true, cc.cacheSubnetGroupName() + ": "
                                + cacheSubnetGroupNameToSubnetAzs.get(cc.cacheSubnetGroupName()));
                    } else {
                        continue;
                    }
                } else if (engine.equals("memcached")
                        && cacheSubnetGroupNameToSubnetAzs.containsKey(cc.cacheSubnetGroupName())) {
                    ccSpeaker.smartPrintResult(true,
                            Speaker.CACHE + " MEMCACHED-" + (++mdCount) + ": " + cc.cacheClusterId() + ", "
                                    + cc.engine() + ", " + cc.engineVersion() + ", " + cc.cacheNodeType()
                                    + ", " + cc.cacheClusterStatus() + ", atRestEnc:"
                                    + cc.atRestEncryptionEnabled() + ", transitEnc:"
                                    + cc.transitEncryptionEnabled() + ", nodes: " + cc.numCacheNodes());
                    if (cc.configurationEndpoint() != null) {
                        if (mode.equals(PLAIN)) {
                            ccSpeaker.printResult(true, "endpoint: " + cc.configurationEndpoint().address()
                                    + ":" + cc.configurationEndpoint().port());
                        } else {
                            ccSpeaker.printResult(true,
                                    "endpoint: " + hp.redact(cc.configurationEndpoint().address() + ":"
                                            + cc.configurationEndpoint().port()));
                        }
                    }
                    ccSpeaker.printResult(true, "maintenance-utc: " + cc.preferredMaintenanceWindow());
                    ccSpeaker.printResult(true,
                            "protected-by: " + uec2.sgMemberShipsToSgTagOrName(ec2, cc.securityGroups()));
                    ccSpeaker.printResult(true, cc.cacheSubnetGroupName() + ": "
                            + cacheSubnetGroupNameToSubnetAzs.get(cc.cacheSubnetGroupName()));
                }
            }
        }
        vSpeaker.printResultParseDetailsClose(true, "TTL-MEMCACHE:" + mdCount);
        vSpeaker.printResult(true, "TTL-REDIS:" + rdCount + "\n");
        // -------------------- End EalstiCache
    }

}