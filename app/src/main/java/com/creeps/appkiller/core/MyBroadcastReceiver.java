package com.creeps.appkiller.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.creeps.appkiller.core.services.ProcessBlockerService;

/**
 * Created by rohan on 18/1/18.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {
    public static final String BOOT_EVENT="android.intent.action.BOOT_COMPLETED";
    public static final String RESTART_SERVICE="com.creeps.appblocker.RESTART_SERVICE";
    private final static String TAG="myBroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        /* add more broadcast Strings here*/
        Log.d(TAG,"here");
        if(intent.getAction().equals(BOOT_EVENT) || intent.getAction().equals(RESTART_SERVICE)){
            Intent newIntent=new Intent(context, ProcessBlockerService.class);
            context.startService(newIntent);
        }
    }
}
