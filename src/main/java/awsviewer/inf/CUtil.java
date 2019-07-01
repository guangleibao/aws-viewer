package awsviewer.inf;

import awsviewer.conf.Speaker;
import software.amazon.awssdk.services.ec2.model.Filter;

public interface CUtil {
    final String REDACT = "redact";
    final String PLAIN = "plain";
    final String CONSOLE = "console";
    final String MARKDOWN = "markdown";
    // Regional scope
    public void printAllResource(Speaker skBranch) throws Exception;
    // Filter printer with VPC valid filters.
    public void printVpcResource(String andOrOr, String mode, Speaker skBranch, Filter... filters) throws Exception;

}