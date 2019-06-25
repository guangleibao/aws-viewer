package awsviewer.inf;

import awsviewer.conf.Speaker;
import software.amazon.awssdk.core.SdkClient;

public interface CUtil {
    public void printAllResource(SdkClient c, Speaker skBranch);
}