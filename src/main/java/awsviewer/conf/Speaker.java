package awsviewer.conf;

import java.io.Serializable;
import java.util.Map;

import awsviewer.common.Uec2;

@SuppressWarnings("serial")
/**
 * The preferred system printer.
 */
public class Speaker implements Serializable {
    public enum RenderType {

        CONSOLE("console"), WEB("web"), MD("md");

        private String name;

        private RenderType(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public static char WARN = Character.toChars(9762)[0];
    public static char BUILDING = Character.toChars(9749)[0];
    public static char REMOVING = Character.toChars(10006)[0];
    public static char DESTROYING = Character.toChars(9762)[0];
    public static char STAR = Character.toChars(9733)[0];
    public static char AUTO_RECOVERY_EC2 = Character.toChars(9850)[0];
    public static char NONE_ASG_AR_EC2 = Character.toChars(8998)[0];
    public static char FIST = Character.toChars(9994)[0];
    public static char GW = Character.toChars(1055)[0];
    public static char RT = Character.toChars(8999)[0];
    public static char NACL = Character.toChars(9634)[0];
    public static char SG = Character.toChars(9635)[0];
    public static char ELB = Character.toChars(11264)[0];
    public static char ENDPOINT = Character.toChars(11610)[0];
    public static char VPCPEER = Character.toChars(9903)[0];
    public static char LAMBDA = Character.toChars(11414)[0];
    public static char ES = Character.toChars(11000)[0];
    public static char DB = Character.toChars(8577)[0];
    public static char EMR = Character.toChars(11274)[0];
    public static char EIP = Character.toChars(10132)[0];
    public static char BLOCK = Character.toChars(9654)[0];
    public static char KEYPAIR = Character.toChars(10702)[0];
    public static char AMI = Character.toChars(9099)[0];
    public static char EBS = Character.toChars(10064)[0];
    public static char SNAPSHOT = Character.toChars(8748)[0];
    public static char VPC = Character.toChars(9729)[0];
    public static char ASG = Character.toChars(7002)[0];
    public static char LC = Character.toChars(9071)[0];
    public static char DX = Character.toChars(10199)[0];
    public static char DMS = Character.toChars(9100)[0];
    public static char USER = Character.toChars(9731)[0];
    public static char GROUP = Character.toChars(11278)[0];
    public static char ROLE = Character.toChars(9786)[0];
    public static char INSTANCEPROFILE = Character.toChars(9816)[0];
    public static char OUTBOUND = Character.toChars(11013)[0];
    public static char INBOUND = Character.toChars(10154)[0];
    public static char R = Character.toChars(9617)[0];
    public static char CACHE = Character.toChars(8373)[0];
    public static char ECS = Character.toChars(8845)[0];

    // Render types
    private RenderType renderType;
    private String renderTypeName;

    // AWSCLI profile
    private String profile;

    // EC2 util
    private Uec2 uec2;

    // The printer indent level
    private int titleLevel = 0;
    private int resultLevel = 0;

    // The swtich to remember whether the web details is open or not
    private boolean detailsOpen = false;

    public String getProfile() {
        return this.profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Uec2 getUec2() {
        return this.uec2;
    }

    public void setUec2(Uec2 uec2) {
        this.uec2 = uec2;
    }

    public void setTitleLevel(int titleLevel, int resultLevel) {
        this.titleLevel = titleLevel;
        this.resultLevel = resultLevel;
    }

    public void setDetailsOpen(boolean detailsOpen) {
        this.detailsOpen = detailsOpen;
    }

    public boolean isDetailsOpen() {
        return this.detailsOpen;
    }

    private static final String WEB_NEWLINE = "<br>";
    private static final String WEB_INDENT = "&nbsp;&nbsp;&nbsp;&nbsp;";
    private static final String WEB_SPACE = "&nbsp;";
    private static final String[] WEB_TITLE_OPEN_LEVEL = new String[] { "<h2><span style='background: lightcoral;'>",
            "<h3><span style='background-color: yellow;'>", "<h4><span style='background-color: green;'>", "<h5><span>", "<h6><span>" };
    private static final String[] WEB_TITLE_CLOSE_LEVEL = new String[] { "</span></h2>", "</span></h3>", "</span></h4>", "</span></h5>", "</span></h6>" };
    private static final String[] WEB_RESULT_OPEN_LEVEL = new String[] { "", "", "", "", "" };
    private static final String[] WEB_RESULT_CLOSE_LEVEL = new String[] { "", "", "", "", "" };
    private static final String WEB_COLLAPSABLE_BLOCK_OPEN = "<details>";
    private static final String WEB_COLLAPSABLE_TITLE_OPEN = "<summary><span style='background-color: #CDCDCD;'>";
    private static final String WEB_COLLAPSABLE_TITLE_CLOSE = "</span></summary><div style='background-color: #CDCDCD;'>";
    private static final String WEB_COLLAPSABLE_BLOCK_CLOSE = "</div></details>";
    private static final String CONSOLE_NEWLINE = "\n";
    private static final String CONSOLE_INDENT = "\t";
    private static final String CONSOLE_TITLE_INDENT = "   ";
    private static final String CONSOLE_SPACE = " ";
    private static final String MD_NEWLINE = "\n";
    private static final String MD_INDENT = "\t";
    private static final String MD_SPACE = " ";
    private static final String[] MD_TITLE_LEVEL = new String[] { "## ", "### ", "#### ", "##### ", "###### " };
    private static final String[] MD_RESULT_LEVEL = new String[] { "+ ", MD_INDENT + "- ",
            MD_INDENT + MD_INDENT + "* " };

    private String newLine;
    private String indent;
    private String symbol;
    private String childChar;

    public static Speaker getConsoleInstance() {
        Speaker sk = new Speaker(Speaker.RenderType.CONSOLE, "#", ">>");
        return sk;
    }

    /**
     * Preferred method to initialize a console speaker.
     * 
     * @param profile
     * @return
     */
    public static Speaker getConsoleInstance(String profile) {
        Speaker sk = new Speaker(Speaker.RenderType.CONSOLE, "#", ">>");
        sk.setProfile(profile);
        return sk;
    }

    public static Speaker getMdInstance() {
        Speaker sk = new Speaker(Speaker.RenderType.MD, "", "");
        return sk;
    }

    /**
     * Preferred method to initialize a MD speaker.
     * 
     * @param profile
     * @return
     */
    public static Speaker getMdInstance(String profile) {
        Speaker sk = new Speaker(Speaker.RenderType.MD, "", "");
        sk.setProfile(profile);
        return sk;
    }

    public static Speaker getWebInstance() {
        Speaker sk = new Speaker(Speaker.RenderType.WEB, "", "");
        return sk;
    }

    /**
     * Preferred method to initialize a WEB speaker.
     * 
     * @param profile
     * @return
     */
    public static Speaker getWebInstance(String profile) {
        Speaker sk = new Speaker(Speaker.RenderType.WEB, "", "");
        sk.setProfile(profile);
        return sk;
    }

    /**
     * The constructor
     * 
     * @param renderType
     * @param symbol
     * @param childchar
     */
    public Speaker(RenderType renderType, String symbol, String childchar) {
        this.setRenderType(renderType);
        this.symbol = symbol;
        this.childChar = childchar;
    }

    /**
     * Get child prefix
     */
    public String getChild(int level) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < level; i++) {
            switch (this.renderType) {
            case CONSOLE:
                sb.append(CONSOLE_SPACE + CONSOLE_SPACE);
                break;
            case MD:
                sb.append(MD_SPACE + MD_SPACE);
                break;
            case WEB:
                sb.append(WEB_SPACE + WEB_SPACE);
                break;
            }
        }
        return new String(sb) + this.childChar + " ";
    }

    /**
     * Make title prefix characters
     */
    private String makeTitlePrefix(String symbol, int symbolCount, int indent) {
        StringBuffer line = new StringBuffer();
        String prefix = null;
        if (this.renderType == RenderType.CONSOLE) {
            prefix = CONSOLE_TITLE_INDENT;
        } else {
            prefix = this.indent;
        }
        for (int i = 0; i < indent; i++) {
            line.append(prefix);
        } // end for
        for (int i = 0; i < symbolCount; i++) {
            line.append(symbol);
        } // end for
        return line.toString();
    }

    /**
     * Set the render type
     */
    public void setRenderType(RenderType renderType) {
        this.renderType = renderType;
        this.renderTypeName = this.renderType.getName();
        if (this.renderType.equals(RenderType.CONSOLE)) {
            this.newLine = CONSOLE_NEWLINE;
            this.indent = CONSOLE_INDENT;
        } else if (this.renderType.equals(RenderType.WEB)) {
            this.newLine = WEB_NEWLINE;
            this.indent = WEB_INDENT;
        } else if (this.renderType.equals(RenderType.MD)) {
            this.newLine = MD_NEWLINE;
            this.indent = MD_INDENT;
        } else {
            this.newLine = CONSOLE_NEWLINE;
            this.indent = CONSOLE_INDENT;
        }
    }

    /**
     * Return the formtted title, and set the chrome symbol, and set the indent
     * content.
     * 
     * @param content     The title content.
     * @param symbol      The title delimiter symbol.
     * @param indentCount The indent count.
     * @return
     */
    public String makeTitle(int indentCount, String symbol, String content) {
        String ret = null;
        if (this.renderType.equals(RenderType.MD)) {
            String titlePrefix = Speaker.MD_TITLE_LEVEL[this.titleLevel];
            ret = titlePrefix + content;
        } else if (this.renderType.equals(RenderType.WEB)) {
            if (this.titleLevel > 1) {
                ret =
                        // Speaker.WEB_TITLE_OPEN_LEVEL[this.titleLevel]+
                        Speaker.WEB_COLLAPSABLE_BLOCK_OPEN + Speaker.WEB_COLLAPSABLE_TITLE_OPEN + content
                                + Speaker.WEB_COLLAPSABLE_TITLE_CLOSE;
                // Speaker.WEB_TITLE_CLOSE_LEVEL[this.titleLevel];
                this.detailsOpen = true;
            } else {
                ret = Speaker.WEB_TITLE_OPEN_LEVEL[this.titleLevel] + content
                        + Speaker.WEB_TITLE_CLOSE_LEVEL[this.titleLevel];
            }
        } else { // Console
            this.symbol = symbol;
            String CR = this.newLine;
            String space = null;
            if (this.renderType == RenderType.CONSOLE) {
                space = CONSOLE_TITLE_INDENT;
            } else {
                space = this.indent;
            }
            int contentLength = content.length();
            final int ceilingLength = 10 + contentLength;
            String ceil = makeTitlePrefix(this.symbol, ceilingLength, indentCount);
            String leading = makeTitlePrefix(this.symbol, 2, indentCount);
            String tail = makeTitlePrefix(this.symbol, 2, 0);
            String centerContent = leading + space + content + space + tail.trim();
            if (this.renderType.equals(RenderType.WEB)) {
                ret = centerContent + CR;
            } else {
                ret = ceil + CR + centerContent + CR + ceil + CR;
            }
        }
        return this.newLine + ret;
    }

    public void printLine() {
        System.out.println(this.getNewLine());
    }

    public void printTitle(int indentCount, String symbol, String content) {
        System.out.println(this.makeTitle(indentCount, symbol, content));
    }

    /**
     * Return the formatted title, and set the indent count.
     * 
     * @param content     The title content.
     * @param indentCount The indent count.
     * @return
     */
    public String makeTitle(int indentCount, String content) {
        return makeTitle(indentCount, this.symbol, content);
    }

    /**
     * Print the title on screen, manually specify the indent count.
     * 
     * @param indentCount
     * @param content
     */
    public void printTitle(int indentCount, String content) {
        System.out.println(this.makeTitle(indentCount, content));
    }

    /**
     * Print title and incr the title level.
     * 
     * @param content
     */
    public void smartPrintTitle(String content) {
        this.printTitle(this.titleLevel, content);
        this.titleLevel++;
    }

    public void smartPrintTitleRestrict(String content) {
        this.printTitleRestrict(this.titleLevel, content);
        this.titleLevel++;
    }

    public void printTitleRestrict(int indentCount, String content) {
        System.out.println(this.makeTitleRestrict(indentCount, content));
    }

    public String makeTitleRestrict(int indentCount, String content) {
        return makeTitleRestrict(indentCount, this.symbol, content);
    }

    /**
     * Make a non collapsable title
     */
    public String makeTitleRestrict(int indentCount, String symbol, String content) {
        String ret = null;
        if (this.renderType.equals(RenderType.MD)) {
            String titlePrefix = Speaker.MD_TITLE_LEVEL[this.titleLevel];
            ret = titlePrefix + content;
        } else if (this.renderType.equals(RenderType.WEB)) {
            ret = Speaker.WEB_TITLE_OPEN_LEVEL[this.titleLevel] + content
                    + Speaker.WEB_TITLE_CLOSE_LEVEL[this.titleLevel];
        } else {
            this.symbol = symbol;
            String CR = this.newLine;
            String space = null;
            if (this.renderType == RenderType.CONSOLE) {
                space = CONSOLE_TITLE_INDENT;
            } else {
                space = this.indent;
            }
            int contentLength = content.length();
            final int ceilingLength = 10 + contentLength;
            String ceil = makeTitlePrefix(this.symbol, ceilingLength, indentCount);
            String leading = makeTitlePrefix(this.symbol, 2, indentCount);
            String tail = makeTitlePrefix(this.symbol, 2, 0);
            String centerContent = leading + space + content + space + tail.trim();
            if (this.renderType.equals(RenderType.WEB)) {
                ret = centerContent + CR;
            } else {
                ret = ceil + CR + centerContent + CR + ceil + CR;
            }
        }
        return this.newLine + ret;
    }

    /**
     * Print title in the current level.
     */
    public void printTitle(String content) {
        this.printTitle(this.titleLevel, content);
    }

    /**
     * Clone a new console Speaker with the same title and result levels.
     * 
     * @return
     */
    @Override
    public Speaker clone() {
        Speaker s = Speaker.getConsoleInstance(this.profile);
        s.setTitleLevel(this.titleLevel, this.resultLevel);
        s.setIndent(this.getIndent());
        s.setNewLine(this.getNewLine());
        s.setRenderType(this.getRenderType());
        s.setUec2(this.getUec2());
        s.setDetailsOpen(this.isDetailsOpen());
        return s;
    }

    /**
     * Return a formatted result, set the indent count, and specify whether newline
     * is appended.
     * 
     * @param <E>
     * @param e           The result content.
     * @param indentCount The indent count.
     * @param withNewLine With or without NewLine.
     * @return
     */
    public <E> String makeResult(int indentCount, boolean withNewLine, E e) {
        String ret = null;
        String CR = null;
        if (this.renderType.equals(RenderType.MD)) {
            int gap = this.resultLevel - Speaker.MD_RESULT_LEVEL.length;
            if (gap >= 0) {
                StringBuffer prefix = new StringBuffer();
                for (int i = -1; i < gap; i++) {
                    prefix.append(MD_INDENT);
                }
                ret = prefix + Speaker.MD_RESULT_LEVEL[2] + e.toString() + (withNewLine ? MD_NEWLINE : "");
            } else {
                ret = Speaker.MD_RESULT_LEVEL[this.resultLevel] + e.toString() + (withNewLine ? MD_NEWLINE : "");
            }
        } else if (this.renderType.equals(RenderType.WEB)) {
            StringBuffer pre = new StringBuffer();
            for (int i = 0; i < indentCount; i++) {
                pre.append(this.indent);
            }
            ret = pre.toString() + Speaker.WEB_RESULT_OPEN_LEVEL[this.resultLevel] + e.toString()
                    + (withNewLine ? this.newLine : "") + Speaker.WEB_RESULT_CLOSE_LEVEL[this.resultLevel];
        } else {
            if (withNewLine) {
                CR = this.newLine;
            } else {
                CR = "";
            }
            String prefix = this.indent;
            StringBuffer pre = new StringBuffer();
            for (int i = 0; i < indentCount; i++) {
                pre.append(prefix);
            }
            ret = pre.toString() + this.getChild(1) + " " + e + CR;

        }
        return ret;
    }

    /**
     * Print result to screen, manually specify the indent level.
     * 
     * @param indentCount
     * @param withNewLine
     * @param e
     */
    public <E> void printResult(int indentCount, boolean withNewLine, E e) {
        System.out.print(this.makeResult(indentCount, withNewLine, e));
    }

    /**
     * Print raw string out.
     * 
     * @param withNewLine
     * @param e
     */
    public <E> void printRaw(boolean withNewLine, E e) {
        if (withNewLine) {
            System.out.print(e + this.newLine);
        } else {
            System.out.print(e);
        }
    }

    /**
     * Print result to screen and leave result level untouched.
     * 
     * @param withNewLine
     * @param e
     */
    public <E> void printResult(boolean withNewLine, E e) {
        this.printResult(this.resultLevel, withNewLine, e);
    }

    /**
     * Parsing details should be closed or not version of "printResult".
     */
    public <E> void printResultParseDetailsClose(boolean withNewLine, E e) {
        if (this.detailsOpen) {
            System.out.println(Speaker.WEB_COLLAPSABLE_BLOCK_CLOSE);
            this.detailsOpen = false;
        }
        this.printResult(this.resultLevel, withNewLine, e);
    }

    /**
     * Print result to screen and incr the result level.
     * 
     * @param withNewLine
     * @param e
     */
    public <E> void smartPrintResult(boolean withNewLine, E e) {
        this.printResult(this.resultLevel, withNewLine, e);
        this.resultLevel++;
    }

    public String getNewLine() {
        return this.newLine;
    }

    public void setNewLine(String newLine) {
        this.newLine = newLine;
    }

    public String getIndent() {
        return indent;
    }

    public void setIndent(String indent) {
        this.indent = indent;
    }

    public String getRenderTypeByName() {
        return renderTypeName;
    }

    public RenderType getRenderType() {
        return this.renderType;
    }

    /**
     * Place holder for icon of instance types.
     */
    public char getIconForEC2InstanceType(String instanceId, Map<String, String> instanceId2Ec2Type) {
        String ec2Type = instanceId2Ec2Type.get(instanceId);
        char ret = Speaker.NONE_ASG_AR_EC2;
        if (ec2Type != null && ec2Type.equals("ASG")) {
            ret = Speaker.STAR;
            instanceId2Ec2Type.put("ASG", String.valueOf(Integer.parseInt(instanceId2Ec2Type.get("ASG")) + 1));
        } else if (ec2Type != null && ec2Type.equals("AR")) {
            ret = Speaker.AUTO_RECOVERY_EC2;
            instanceId2Ec2Type.put("AR", String.valueOf(Integer.parseInt(instanceId2Ec2Type.get("AR")) + 1));
        } else {
            instanceId2Ec2Type.put("NONE-ASG-AR",
                    String.valueOf(Integer.parseInt(instanceId2Ec2Type.get("NONE-ASG-AR")) + 1));
        }
        return ret;
    }

    /**
     * Should be used in CUtil class's printAllResource() method, more specifically
     * - the mSpeaker which clones skBranch.
     */
    public void printResourceSubTitle(String overrideResourceName) {
        String cUtilName = null;
        if (overrideResourceName != null) {
            cUtilName = overrideResourceName;
        } else {
            cUtilName = Thread.currentThread().getStackTrace()[2].getClassName().replaceAll("bglutil.common.U", "");
        }
        this.smartPrintResult(true, Speaker.BLOCK + " Checking ||| " + cUtilName + " |||");
    }

    /**
     * Should be used in CUtil class's destroyResourceByNamePrefix() method, more
     * specifically - the mSpeaker which clones skBranch.
     */
    public void printDestroyResourceByNamePrefix(String overrideResourceName, String prefix) {
        String cUtilName = null;
        if (overrideResourceName != null) {
            cUtilName = overrideResourceName;
        } else {
            cUtilName = Thread.currentThread().getStackTrace()[2].getClassName().replaceAll("bglutil.common.U", "");
        }
        this.smartPrintResult(true, Speaker.REMOVING + " Deleting " + cUtilName + " with name prefix " + prefix);
    }
}
