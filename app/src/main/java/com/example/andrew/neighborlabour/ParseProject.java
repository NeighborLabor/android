package com.example.andrew.neighborlabour;

/**
 * Created by andrew on 2/17/17.
 */

import android.Manifest;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.andrew.neighborlabour.UI.MainActivity;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;


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
