package com.creeps.appkiller.core.services.model;

/**
 * Created by rohan on 3/3/18.
 * A model class for packages belonging to a particular Profile
 */

public class ProfilePackages {
    public final static String PACKAGE_ID="id";
    public final static String PACKAGE_PROFILE_ID="pr_id";
    public final static String PACKAGE_NAME="pkg_name";
    private int id;
    private int profileId;
    private String packageName;
    public ProfilePackages(int packageId,int profileId,String packageName){
        this.id=packageId;
        this.profileId=profileId;
        this.packageName=packageName;
    }

    public int getId() {
        return id;
    }

    public int getProfileId() {
        return profileId;
    }

    public String getPackageName() {
        return packageName;
    }


    @Override
    public String toString(){
        return this.id+" "+this.packageName+" "+this.profileId;
    }


}
