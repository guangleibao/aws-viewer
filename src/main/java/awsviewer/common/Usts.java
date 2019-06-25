package awsviewer.common;

import awsviewer.conf.Clients;
import awsviewer.conf.Speaker;
import awsviewer.inf.CUtil;
import software.amazon.awssdk.core.SdkClient;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.DecodeAuthorizationMessageRequest;
import software.amazon.awssdk.services.sts.model.GetCallerIdentityRequest;
import software.amazon.awssdk.services.sts.model.GetCallerIdentityResponse;

public class Usts implements CUtil {

    public static final Usts build() {
        return new Usts();
    }

    @Override
    public void printAllResource(SdkClient c, Speaker skBranch) {
    }

    /**
     * Decode authorization message.
     * @param sts
     * @param message
     * @return
     */
    public String decodeAuthorizationMessage(StsClient sts, String message){
        return sts.decodeAuthorizationMessage(DecodeAuthorizationMessageRequest.builder().encodedMessage(message).build()).decodedMessage();
    }

    /**
     * Get account id.
     */
    public String getAccountId(String profile) throws Exception {
        StsClient sc = (StsClient) Clients.getClientByServiceClass(Clients.STS, profile);
        String accountId = sc.getCallerIdentity(GetCallerIdentityRequest.builder().build()).account();
        return accountId;
    }

    /**
     * Return the caller id response.
     */
    public GetCallerIdentityResponse getCallerId(StsClient sts) {
        return sts.getCallerIdentity();
    }
}