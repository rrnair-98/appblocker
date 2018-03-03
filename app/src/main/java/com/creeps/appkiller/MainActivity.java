package com.creeps.appkiller;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.creeps.appkiller.core.services.DatabaseHandler;
import com.creeps.appkiller.core.services.ProcessBlockerService;
import com.creeps.appkiller.R;
import com.creeps.appkiller.core.services.thread.ProcessLister;

import java.util.ArrayList;
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
        DatabaseHandler ref=DatabaseHandler.getInstance(this);
        Log.d(TAG,ref.getCurrentlyActiveProfile().toString());
    }





    interface UsageStatisticsWelder{
        public void weld();
    }
}
