package com.creeps.appkiller.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.creeps.appkiller.core.services.ProcessBlockerService;

/**
 * Created by rohan on 18/1/18.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {
    public static final String BOOT_EVENT="android.intent.action.BOOT_COMPLETED";
    public static final String RESTART_SERVICE="restartService";
    @Override
    public void onReceive(Context context, Intent intent) {
        /* add more broadcast Strings here*/
        if(intent.getAction().equals(BOOT_EVENT) || intent.getAction().equals(RESTART_SERVICE)){
            Intent newIntent=new Intent(context, ProcessBlockerService.class);
            context.startService(newIntent);
        }
    }
}
