package com.durgaslist.andrew.durgaslist;

/**
 * Created by andrew on 2/17/17.
 */

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.parse.Parse;


public class ParseProject extends Application {
    private final String TAG = "ParseProject";
    private static Context context = null;

    public static Context getContext() {
        return ParseProject.context;
    }

    public void onCreate() {
        this.context = getApplicationContext();
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(new Parse.Configuration.Builder(this)
            .applicationId("nl")
            .server("http://159.203.73.182:1337/parse").build());

        Log.e(TAG, "successfully connected to Parse");

    }



}
