package com.creeps.appkiller;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.creeps.appkiller.core.services.ProcessBlockerService;
import com.creeps.appkiller.core.services.SharedPreferenceHandler;
import com.creeps.appkiller.core.services.thread.ProcessLister;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    private final static String TAG="MainActivity";
    public final static String BLACKLIST="blackList";

    @Override
    protected void onStart(){
        super.onStart();
        Intent intent=new Intent(this, ProcessBlockerService.class);
        this.startService(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<String> arr=new ArrayList<>();
        arr.add("com.example.damian.fibview");
        arr.add("com.google.android.youtube");
        ProcessBlockerService.setList(arr);




    }




    interface UsageStatisticsWelder{
        public void weld();
    }
}
