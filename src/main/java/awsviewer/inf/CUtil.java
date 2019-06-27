package awsviewer.inf;

import awsviewer.conf.Speaker;
import software.amazon.awssdk.core.SdkClient;

public interface CUtil {
    final String REDACT = "redact";
    final String PLAIN = "plain";
    final String CONSOLE = "console";
    final String MARKDOWN = "markdown";
    public void printAllResource(SdkClient c, Speaker skBranch);
}