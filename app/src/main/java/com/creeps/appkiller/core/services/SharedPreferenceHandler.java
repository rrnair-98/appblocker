package com.creeps.appkiller.core.services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by rohan on 1/10/17.
 * A helper class for using SharedPreferences.
 */

public class SharedPreferenceHandler {
    private Context mContext;
    private static SharedPreferenceHandler mSharedPreferenceHandler;/* to be used for singleton*/
    final SharedPreferences sharedPreferences;/* to be used for persisting essential data across sessions*/
    private final String SHARED_PREF_NAME="AppBlockerPreferences";/* name of the sharePref file*/
    private SharedPreferencesCallback sharedPreferencesCallback;

    private SharedPreferenceHandler(Context context){
        this.mContext=context;
        this.sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
    }



    public static SharedPreferenceHandler getInstance(Context context){
        return mSharedPreferenceHandler==null?new SharedPreferenceHandler(context):mSharedPreferenceHandler;
    }

    public String get(String x){
        return this.sharedPreferences.getString(x,null);
    }
    public void add(String key,String value){
        SharedPreferences.Editor editor=this.sharedPreferences.edit();
        editor.putString(key,value);
        editor.apply();/* commit asynchronously*/
        if(this.sharedPreferencesCallback!=null)
            sharedPreferencesCallback.latestAddition(key);
    }



    public void remove(String key){
        SharedPreferences.Editor editor=this.sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public void setSharedPreferencesCallback(SharedPreferencesCallback sharedPreferencesCallback){
        this.sharedPreferencesCallback=sharedPreferencesCallback;
    }
    public interface SharedPreferenceHandlerConstants{
        /* base details to be used as keys for sharedPreferences... */
        public final static String BLACKLIST="blacklist";
    }
    public interface SharedPreferencesCallback{
        public void latestAddition(String key);
    }



}
