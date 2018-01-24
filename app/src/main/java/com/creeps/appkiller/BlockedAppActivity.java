package com.creeps.appkiller;

import android.app.ActivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.creeps.appkiller.core.services.ProcessBlockerService;

/**
 * Created by rohan on 18/1/18.
 */

public class BlockedAppActivity extends AppCompatActivity {

    private String currentBlackListedPackage;
    private ActivityManager activityManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_app);
        Bundle b=getIntent().getExtras();
        this.currentBlackListedPackage=(String)b.get(ProcessBlockerService.BLACKLIST_APP);
        activityManager=(ActivityManager) this.getSystemService(ACTIVITY_SERVICE);

    }

    private void killTheApp(){
        this.activityManager.killBackgroundProcesses(this.currentBlackListedPackage);

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.killTheApp();
    }
}
