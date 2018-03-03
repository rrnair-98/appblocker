package com.creeps.appkiller.core.services.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by rohan on 3/3/18.
 * A SQLite model class for the table Profiles
 */

public class Profile {

    /* These constants are the column names present in the database*/
    public final static String ID_KEY="profile_id";
    public final static String PNAME_KEY="profile_name";
    public final static String ST_KEY="start_time";
    public final static String ET_KEY="end_time";
    public final static String ACTIVE_KEY="currently_active";
    private int id;
    private Date startTime,endTime;
    private String profileName;
    private int isActive=0;
    //could use a HashMap of packageName to ProfilePackages for o(1) retrievals.
    private ArrayList<ProfilePackages> packages;
    public Profile(int id,long startTime,long endTime,String profileName,int isActive){
        this.id=id;
        this.startTime=new Date(startTime);this.endTime=new Date(endTime);
        this.profileName=profileName;
        this.isActive=isActive;
    }
    public int getId(){return this.id;}

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }
    public void setPackages(ArrayList<ProfilePackages> packages){
        this.packages=packages;
    }

    public ArrayList<ProfilePackages> getPackages() {
        return packages;
    }

    public boolean contains(String packageName){
        int i=-1;
        if(this.packages!=null) {
            while (++i < this.packages.size() && !(this.packages.get(i)).getPackageName().equals(packageName))
                ;
            return i != this.packages.size();
        }
        return false;
    }
    @Override
    public String toString(){
        return this.id+" "+this.profileName+" active: "+this.isActive;
    }



}