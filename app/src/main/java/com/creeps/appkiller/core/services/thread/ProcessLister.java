package com.creeps.appkiller.core.services.thread;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static android.content.Context.USAGE_STATS_SERVICE;

/**
 * Created by rohan on 15/1/18.
 * A Singleton Handler Thread, to be used by the service.
 */
public class ProcessLister extends HandlerThread implements Handler.Callback,ProcessListerConstants{
    private Context mContext;
    private Runnable mRunnable;
    private Handler mHandler;
    private ProcessListerCallback processListerCallback;

    private static ProcessLister mProcessLister;

    private final static String TAG="ProcessListerThread";
    private ProcessLister(String name, final Context context){
        super(name);
        this.mContext=context;
        this.mRunnable=new Runnable() {
            @Override
            public void run() {
                Object x = getUsageAccessData(ProcessLister.this.mContext, false);
                if (x != null) {
                    SortedMap<Long, UsageStats> sortedMap = (SortedMap)x;
                /* todo i)check in blacklist ii) add check for android version since getPackageName is available only from API-LEVEL 21(Lollipop)*/

                    String currentPackageName=sortedMap.get(sortedMap.lastKey()).getPackageName();
                    Log.d(TAG, "currently running " + currentPackageName);


                    if (ProcessLister.this.processListerCallback != null )
                        processListerCallback.call(currentPackageName);

                }
                this.reque();
            }

            private void reque(){
                if (ProcessLister.this.mHandler != null)
                    ProcessLister.this.mHandler.postDelayed(this, ProcessLister.TIMEOUT);

            }
        };
    }


    public static ProcessLister prepareInstance(Context context,String name){
        return mProcessLister = mProcessLister==null?new ProcessLister(name,context):mProcessLister;
    }
    public static ProcessLister getInstance(){
        return mProcessLister;
    }


    @Override
    protected void onLooperPrepared() {
        this.mHandler=new Handler(this.getLooper(),this);
        this.mHandler.postDelayed(this.mRunnable,TIMEOUT);
    }

    @Override
    public boolean handleMessage(Message msg) {
        Log.d(TAG,"message handled");
        return true;
    }

    public void setProcessListerCallback(ProcessListerCallback callback){
        this.processListerCallback= callback;
    }

    /* This function is to be called whenever you need the package list or when you want to know which app is currently running
            * @param getList - if set returns the list only. Other wise returns a SortedTree
            * @param Context - to obtain the system Service*/
    public static Object getUsageAccessData(Context context,boolean getList){
        String x;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();

            ArrayList<UsageStats> stats=null;
            try {
                //the following line of code leads to classCastException whenever youre multitasking
                 stats= (ArrayList<UsageStats>) usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time);
                if (getList) return stats;

                if(stats!=null){
                SortedMap<Long, UsageStats> map=new TreeMap<>();
                for(UsageStats usageStats:stats){
                    map.put(usageStats.getLastTimeUsed(),usageStats);

                }
                if(!map.isEmpty()){
                    x=map.get(map.lastKey()).getPackageName();
                    Log.d(TAG,"current "+x);
                }
                return map;

            }
            }catch (ClassCastException c){
                Log.d(TAG,"couldnt return any value prolly because youre multitasking");
                return null;
            }
        }
        return null;
    }
}
interface ProcessListerConstants{
    public final static int TIMEOUT=1000;
}
