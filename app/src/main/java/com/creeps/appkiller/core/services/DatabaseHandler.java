package com.creeps.appkiller.core.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.creeps.appkiller.core.services.model.Profile;
import com.creeps.appkiller.core.services.model.ProfilePackages;

import java.util.ArrayList;

/**
 * Created by rohan on 3/3/18.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private final static String TAG="DatabaseHandler";
    private final static String DATABASE_NAME="appblocker";
    private final static int DATABASE_VERSION=3;
    private final static String TABLE_PROFILES="profiles";
    private final static String TABLE_PKG="pkgs";
    private final static String GET_ALL="SELECT * FROM ";
    private final static String CREATE_PROFILES_TABLE="CREATE TABLE "+TABLE_PROFILES+" ("+ Profile.ID_KEY+" INTEGER PRIMARY KEY AUTOINCREMENT , "+Profile.PNAME_KEY
            +" TEXT , "+Profile.ST_KEY+" TEXT , "+Profile.ET_KEY+" TEXT, "+Profile.ACTIVE_KEY+" INTEGER )";
    private final static String CREATE_PROFILE_PACKAGE="CREATE TABLE "+TABLE_PKG+" ("+ ProfilePackages.PACKAGE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            ProfilePackages.PACKAGE_PROFILE_ID+" INTEGER , "+ProfilePackages.PACKAGE_NAME+" TEXT)";


    private static DatabaseHandler ref;
    /* TODO add a predefined profile to the database */

    //fired when active profile is changed
    private ActiveProfileCallback activeCallback;
    private DatabaseHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    public static DatabaseHandler getInstance(Context context){
        return ref = ref==null?new DatabaseHandler(context):ref;
    }

    public void setActiveCallback(ActiveProfileCallback callback){
        this.activeCallback=callback;
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        Log.d(TAG,"onDowngrade db");

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG,"inOncreated b");
        db.execSQL(CREATE_PROFILES_TABLE);
        db.execSQL(CREATE_PROFILE_PACKAGE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG,"onupgrade db");

    }
    public synchronized void addProfile(String profileName,long startTime,long endTime){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Profile.PNAME_KEY,profileName);
        values.put(Profile.ST_KEY,startTime);
        values.put(Profile.ET_KEY,endTime);
        database.insert(TABLE_PROFILES,null,values);
        database.close();
    }
    public ArrayList<Profile> getAllProfiles(){
        ArrayList<Profile> profiles=new ArrayList<>();
        String query=GET_ALL+TABLE_PROFILES;
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                int id=(cursor.getInt(0));
                String profileName=cursor.getString(1);
                long startTime=cursor.getLong(2);
                long endTime=cursor.getLong(3);
                int ia=cursor.getInt(4);
                profiles.add(new Profile(id,startTime,endTime,profileName,ia));
            }while (cursor.moveToNext());

        }
        return profiles;
    }

    /* adds a list of ProfilePackages to the database*/
    public synchronized void addPackagesToProfile(int packageId,ArrayList<String> packageName){
        SQLiteDatabase database=this.getWritableDatabase();
        String query="INSERT INTO "+TABLE_PKG+" ("+ProfilePackages.PACKAGE_PROFILE_ID+","+ProfilePackages.PACKAGE_NAME+") VALUES ";
        StringBuilder stringBuilder=new StringBuilder(query);
        for(String pk:packageName)
            stringBuilder.append("("+packageId+",\""+pk+"\"),");

        stringBuilder.delete(stringBuilder.length()-1,stringBuilder.length());
        Log.d(TAG,stringBuilder.toString());
        database.execSQL(stringBuilder.toString());
    }
    /* Returns an arraylist of ProfilePackage belonging to given profileId*/
    public ArrayList<ProfilePackages> readAllPackages(int id){
        ArrayList<ProfilePackages> arrayList=new ArrayList<>();
        String query=GET_ALL+TABLE_PKG+" WHERE "+ProfilePackages.PACKAGE_PROFILE_ID+" ="+id;
        SQLiteDatabase database=this.getWritableDatabase();
        Cursor cursor=database.rawQuery(query,null);
        if(cursor.moveToFirst())
            do{
                int pkid=cursor.getInt(0);
                int prid=cursor.getInt(1);
                String pkName=cursor.getString(2);
                arrayList.add(new ProfilePackages(pkid,prid,pkName));

            }while (cursor.moveToNext());
        return arrayList;
    }
    /* deletes a record from ProfilePackages table*/
    public synchronized void deleteProfilePackageById(int profileId,int packageId){
        String query="DELETE FROM "+TABLE_PKG+" WHERE "+ProfilePackages.PACKAGE_PROFILE_ID+" = "+profileId +" AND "+ProfilePackages.PACKAGE_ID+" = "+packageId;
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        sqLiteDatabase.execSQL(query);
    }

    /* will first data from ProfilePackage and then from Profile*/
    public synchronized void deleteProfile(int profileId) {
        String query = "DELETE FROM " + TABLE_PKG + " WHERE " + ProfilePackages.PACKAGE_PROFILE_ID + " = " + profileId;
        String otherQuery = "DELETE FROM " + TABLE_PROFILES + " WHERE " + Profile.ID_KEY + " = " + profileId;
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL(query);
        database.execSQL(otherQuery);
    }
    /* retrieves the currently active profile. NOTE A CALL TO setProfileActive must be given */
    public Profile getCurrentlyActiveProfile(){
        SQLiteDatabase database=this.getReadableDatabase();
        String query="SELECT * FROM "+TABLE_PROFILES+" WHERE "+Profile.ACTIVE_KEY+" = "+1;
        Cursor cursor=database.rawQuery(query,null);
        Profile pr=null;
        try {
            if (cursor.moveToFirst()) {
                int id = (cursor.getInt(0));
                String profileName = cursor.getString(1);
                long startTime = cursor.getLong(2);
                long endTime = cursor.getLong(3);
                int ia = cursor.getInt(4);
                pr=new Profile(id, startTime, endTime, profileName, ia);
            }
        }catch (Exception npe){
            npe.printStackTrace();
        }finally {

            return pr;
        }
    }
    /* sets a profile as active. Sets the older ones as inactive first
    * A CALL to this function must be GIVEN
    * */
    public synchronized void setProfileActive(int profileId){
        SQLiteDatabase database=this.getWritableDatabase();
        String query1="UPDATE "+TABLE_PROFILES+" SET "+Profile.ACTIVE_KEY+" = 0";
        String query2="UPDATE "+TABLE_PROFILES+" SET "+Profile.ACTIVE_KEY+" = 1 WHERE "+Profile.ID_KEY+" = "+profileId;
        database.execSQL(query1);
        database.execSQL(query2);
        if(this.activeCallback!=null)
            this.activeCallback.profileNotify();
    }







}
