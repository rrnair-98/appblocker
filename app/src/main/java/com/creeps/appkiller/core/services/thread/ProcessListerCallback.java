package com.creeps.appkiller.core.services.thread;

/**
 * Created by rohan on 18/1/18.
 * This callback is to be implemented by the service or the class that wants
 */

public interface ProcessListerCallback {
    public void call(String currentlyRunningPackage);
}
