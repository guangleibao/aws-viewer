package awsviewer.conf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import software.amazon.awssdk.regions.Region;

public class General {

    public static String CRED_FILE;

    public static Map<String, Region> GLOBAL_CS_REGIONS = new TreeMap<String, Region>();
    public static Map<String, Region> CHINA_CS_REGIONS = new TreeMap<String, Region>();
    public static Map<String, Region> ALL_REGIONS = new TreeMap<String, Region>();
    public static Map<String, Region> PROFILE_REGION = new TreeMap<String, Region>();

    static {
        // Fill GLOBAL_CS_REGIONS
        for (Region r : Region.regions()) {
            if (!r.id().contains("global") && !r.id().startsWith("cn-")) {
                //System.out.println("GLOBAL_CS_REGIONS: "+r.id());
                GLOBAL_CS_REGIONS.put(r.id(), r);
            }
        }
        // Fill CHINA_CS_REGIONS
        for (Region r : Region.regions()) {
            if (r.id().startsWith("cn-")) {
                //System.out.println("CHINA_CS_REGIONS: "+r.id());
                CHINA_CS_REGIONS.put(r.id(), r);
            }
        }
        // Fill ALL_REGIONS
        for (Region r : Region.regions()) {
            if (!r.id().contains("global")) {
                //System.out.println("ALL_REGIONS: "+r.id());
                ALL_REGIONS.put(r.id(), r);
            }
        }
    }
    /**
     * Call this method before initiating AWS client.
     */
    public static void init(String profileName) {
        CRED_FILE = Config.HOME + "/.aws/credentials";
        File config = new File(Config.HOME + "/.aws/config");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(config));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = null;
        try {
            line = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int profileReady = 0;
        String profile = null;
        String region = null;
        while (line != null) {
            if (line.matches("^\\[ {0,}profile.*\\]$")) {
                profile = line.replaceAll("\\[ {0,}profile +", "").replaceAll(" {0,}\\] {0,}$", "");
                profileReady = 1;
            } else if (line.matches("^\\[ {0,}default {0,}]$")) {
                profile = "default";
                profileReady = 1;
            }
            try {
                line = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (line != null && line.matches("^ {0,}region {0,}=.*$")) {
                region = line.replaceAll(" {0,}region {0,}= {0,}", "").replaceAll(" {0,}$", "");
                if (profileReady == 1) {
                    if (profile.equals(profileName)) {
                        PROFILE_REGION.put(profile, Region.of(region));
                    }
                    profileReady = 0;
                }
            }
        }
    }
}