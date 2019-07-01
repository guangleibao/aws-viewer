package awsviewer.conf;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.TreeSet;

import awsviewer.WaTool;

public class Helper {

	public static final Speaker SK = Speaker.getConsoleInstance();

	/**
	 * Redact input string.
	 */
	public String redact(String in) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < in.length(); i++) {
			sb.append(Speaker.R);
		}
		return sb.toString();
	}

	/**
	 * Redact ip address.
	 * 
	 * @param ip
	 * @return
	 */
	public String redactIp(String ip) {
		String h = ip.substring(0, ip.indexOf(".") + 1);
		String t = ip.substring(ip.indexOf(".") + 1);
		return h + this.redact(t);
	}

	/**
	 * Redact ARN.
	 */
	public String redactArn(String arn) {
		return arn.replaceAll("[0-9]{12}", this.redact("111111111111"));
	}

	// Enable the '-h' option for methods which call help.
	public void help(String help, String helpMessage) {
		if (help.equals("-h")) {
			StackTraceElement element = Thread.currentThread().getStackTrace()[2];
			System.out.println("\n" + element.getMethodName() + " " + helpMessage + "\n");
			System.exit(0);
		}
	}

	// Help those who cannot remember command names.
	public static void searh(String commandPrefix) {
		Method[] allMethods = WaTool.class.getDeclaredMethods();
		TreeSet<String> ts = new TreeSet<String>();
		for (Method m : allMethods) {
			if (WaTool.SKIPPED_METHODS.contains(m.getName())) {
				continue;
			}
			StringBuffer sb = new StringBuffer();
			if (Modifier.isPublic(m.getModifiers()) && m.getName().startsWith(commandPrefix)) {
				sb.append(m.getName() + ": ");
				int parameterCount = m.getParameterTypes().length;
				for (int i = 0; i < parameterCount; i++) {
					sb.append("[]");
				}
				ts.add(new String(sb));
				// SK.printResult(true,m.getName());
			}
		}
		for (String s : ts) {
			SK.printResult(true, s);
		}
	}

}
