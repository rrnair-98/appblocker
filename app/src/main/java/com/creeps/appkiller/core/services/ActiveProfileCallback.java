package com.creeps.appkiller.core.services;

import com.creeps.appkiller.core.services.model.Profile;

/**
 * Created by rohan on 3/3/18.
 * This callback is fired by the DatabaseHandler whenever a profile is set as active
 */

public interface ActiveProfileCallback {
    public void profileNotify();
}
