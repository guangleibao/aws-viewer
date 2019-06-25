package awsviewer.conf;

public class Helper {

	public static final Speaker SK = Speaker.getConsoleInstance();

	/**
	 * Redact input string.
	 */
	public String redact(String in){
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<in.length();i++){
			sb.append(Speaker.R);
		}
		return sb.toString();
	}

	/**
	 * Redact ip address.
	 * @param ip
	 * @return
	 */
	public String redactIp(String ip){
		String h = ip.substring(0, ip.indexOf(".")+1);
		String t = ip.substring(ip.indexOf(".")+1);
		return h+this.redact(t);
	}

	/**
	 * Redact ARN.
	 */
	public String redactArn(String arn){
		return arn.replaceAll("[0-9]{12}",this.redact("111111111111"));
	}

}
