package com.creeps.appkiller.core.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import com.creeps.appkiller.BlockedAppActivity;
import com.creeps.appkiller.core.MyBroadcastReceiver;
import com.creeps.appkiller.core.services.thread.ProcessLister;
import com.creeps.appkiller.core.services.thread.ProcessListerCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static android.content.ContentValues.TAG;

/**
 * Created by rohan on 15/1/18.
 */

public class ProcessBlockerService extends Service implements ProcessListerCallback,SharedPreferenceHandler.SharedPreferenceHandlerConstants,SharedPreferenceHandler.SharedPreferencesCallback {


    private final static String THREAD_NAME="ProcessHandler";
    private final IBinder mBinder= new LocalBinder();
    public final static String BLACKLIST_APP="blacklistedApp";

    private static List<String> mBlackList;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        SharedPreferenceHandler preferenceHandler=SharedPreferenceHandler.getInstance(this.getApplicationContext());
        preferenceHandler.setSharedPreferencesCallback(this);
        this.initList(preferenceHandler.get(BLACKLIST));
        ProcessLister processLister=ProcessLister.prepareInstance(this.getApplicationContext(),THREAD_NAME);
        processLister.setProcessListerCallback(this);
        if (processLister.getState() == Thread.State.NEW){
            processLister.start();
        }
        return START_STICKY;
    }
    private void initList(String blacklist){
        if(blacklist!=null) {
            mBlackList = new ArrayList<>();
            Scanner scanner = new Scanner(blacklist);
            scanner.useDelimiter(",");
            while (scanner.hasNext())
                mBlackList.add(scanner.next());
        }

    }

    public class LocalBinder extends Binder{

        public ProcessBlockerService getService(){
            return ProcessBlockerService.this;
        }
    }

    public static void setList(ArrayList<String> list){
        mBlackList=list;
    }


    @Override
    public void onDestroy(){
        /* ToDO restart the service ie send a broadcast*/
        this.sendBroadcast(new Intent(MyBroadcastReceiver.RESTART_SERVICE));
        Log.d(TAG,"service being destroyed and prolly restarted");
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }


    /* Overriding ProcessListerCallback*/

    @Override
    public void call(String currentPackageName) {
        /* todo check blacklist and display activity*/

        Log.d(TAG,"received "+currentPackageName);

        if(mBlackList!=null) Log.d(TAG,"list "+mBlackList.toString());
        /*Log.d(TAG,SharedPreferenceHandler.getInstance(this.getApplicationContext()).get(BLACKLIST));*/
        if(mBlackList!=null && mBlackList.contains(currentPackageName)){
            Log.d(TAG,"blockedActivityIntent");
            Intent blockeActivityIntent=new Intent(this, BlockedAppActivity.class);
                    blockeActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            blockeActivityIntent.putExtra(BLACKLIST_APP,currentPackageName);
            startActivity(blockeActivityIntent);
        }
    }

    /* Overrding SharedPreferenceCallback*/
    @Override
    public void latestAddition(String key){
        /* todo reinit the blacklist if new strings were added*/
        Log.d(TAG,"latestAddition");
        if(key.equals(BLACKLIST))
            initList(SharedPreferenceHandler.getInstance(this.getApplicationContext()).get(BLACKLIST));
    }

}
