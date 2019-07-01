package awsviewer.common;

import java.util.List;

import awsviewer.conf.Clients;
import awsviewer.conf.Speaker;
import awsviewer.inf.CUtil;
import software.amazon.awssdk.services.ec2.model.Filter;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.iam.model.Group;
import software.amazon.awssdk.services.iam.model.InstanceProfile;
import software.amazon.awssdk.services.iam.model.ListVirtualMfaDevicesRequest;
import software.amazon.awssdk.services.iam.model.Role;
import software.amazon.awssdk.services.iam.model.User;
import software.amazon.awssdk.services.iam.model.VirtualMFADevice;

public class Uiam implements CUtil {

    public static final Uiam build() {
        return new Uiam();
    }
    

    @Override
    public void printAllResource(Speaker skBranch) throws Exception {
        IamClient iam = (IamClient) Clients.getClientByServiceClass(Clients.IAM, skBranch.getProfile());
        Speaker mSpeaker = skBranch.clone();
        mSpeaker.printResourceSubTitle(null);
        int uCount = 0;
        int gCount = 0;
        int rCount = 0;
        int iCount = 0;
        Speaker uSpeaker = mSpeaker.clone();
        uSpeaker.printResourceSubTitle("user");
        for(User u:iam.listUsers().users()){
            uSpeaker.printResult(true,Speaker.USER+" USER-"+(++uCount)+": "+u.userName());
        }
        Speaker gSpeaker = mSpeaker.clone();
        gSpeaker.printResourceSubTitle("group");
        for(Group g:iam.listGroups().groups()){
            gSpeaker.printResult(true,Speaker.GROUP+" GROUP-"+(++gCount)+": "+g.groupName());
        }
        Speaker rSpeaker = mSpeaker.clone();
        rSpeaker.printResourceSubTitle("role");
        for(Role r:iam.listRoles().roles()){
            rSpeaker.printResult(true,Speaker.ROLE+" ROLE-"+(++rCount)+": "+r.roleName());
        }
        Speaker iSpeaker = mSpeaker.clone();
        iSpeaker.printResourceSubTitle("ec2-instance-profile");
        for(InstanceProfile ip: iam.listInstanceProfiles().instanceProfiles()){
			iSpeaker.printResult(true,Speaker.INSTANCEPROFILE+" INSTANCE-PROFILE-"+(iCount)+": "+ip.instanceProfileName());
		}
    }

    @Override
    public void printVpcResource(String andOrOr, String mode, Speaker skBranch, Filter... filters) {

    }

    /**
     * Get all virtual mfa devices.
     */
    public List<VirtualMFADevice> getVirtualMfaDevices(IamClient iam){
        List<VirtualMFADevice> devices = iam.listVirtualMFADevices(ListVirtualMfaDevicesRequest.builder().build()).virtualMFADevices();
        return devices;
    }



}