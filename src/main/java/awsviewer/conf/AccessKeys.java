package awsviewer.conf;

import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.auth.StsAssumeRoleCredentialsProvider;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;

/**
 * CPC (Credentials Provider Chain) Factory.
 */
public class AccessKeys {
    /**
     * Get credentials from: 1. Named or default profile. 2. EC2 instance profile -
     * IAM EC2 role.
     */
    public static AwsCredentialsProviderChain getCpcByProfile(String profile) {
        ProfileCredentialsProvider pcp = null;
        if (profile.equals("default")) {
            pcp = ProfileCredentialsProvider.create();
        } else {
            pcp = ProfileCredentialsProvider.create(profile);
        }
        return AwsCredentialsProviderChain.builder().addCredentialsProvider(pcp)
                .addCredentialsProvider(InstanceProfileCredentialsProvider.create()).build();
    }

    /**
     * Get credentials from: 1. EC2 instance profile set by IAM EC2 role.
     */
    public static AwsCredentialsProviderChain getCpcByEc2Role() {
        return AwsCredentialsProviderChain.builder().addCredentialsProvider(InstanceProfileCredentialsProvider.create())
                .build();
    }

    /**
     * Get credentials from: 1. Temporary access key id, access secret, token.
     */
    public static AwsCredentialsProviderChain getCpcBySessionAkSkToken(String aKey, String aSecret, String token) {
        AwsSessionCredentials cred = AwsSessionCredentials.create(aKey, aSecret, token);
        return AwsCredentialsProviderChain.builder().addCredentialsProvider(StaticCredentialsProvider.create(cred))
                .build();
    }

    /**
     * Get credential from: 1. Assumed role.
     */
    public AwsCredentialsProviderChain getCpcByAssumeRole(StsClient sts, String roleArn, int durationSec,
            String policyJson, String roleSessionName) {
        StsAssumeRoleCredentialsProvider pcp = StsAssumeRoleCredentialsProvider.builder()
                .refreshRequest(AssumeRoleRequest.builder().durationSeconds(durationSec).policy(policyJson)
                        .roleSessionName(roleSessionName + "-1").roleArn(roleArn).build())
                .stsClient(sts).build();
        return AwsCredentialsProviderChain.builder().addCredentialsProvider(pcp).build();
    }
}