package com.example.andrew.neighborlabour;

/**
 * Created by andrew on 2/17/17.
 */

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.andrew.neighborlabour.listings.Filter;
import com.example.andrew.neighborlabour.listings.Listing;
import com.example.andrew.neighborlabour.listings.ListingManager;
import com.example.andrew.neighborlabour.user.UserManager;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;


public class ParseProject extends Application {
    private final String TAG = "ParseProject";
    private static Context context = null;
    private static Location location = null;

    public static Context getContext() {
        return ParseProject.context;
    }

    public static Location getUserLocation() {
        return ParseProject.location;
    }


//    private final LocationListener mLocationListener = new LocationListener() {
//
//        @Override
//        public void onLocationChanged(Location location) {
//            ParseProject.location = location;
//        }
//
//        @Override
//        public void onStatusChanged(String s, int i, Bundle bundle) { }
//
//        @Override
//        public void onProviderEnabled(String s) { }
//
//        @Override
//        public void onProviderDisabled(String s) {}
//    };


    public void onCreate() {
        this.context = getApplicationContext();
        super.onCreate();

//        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5 * 60 * 1000, 1, mLocationListener);
//        }


        Parse.enableLocalDatastore(this);
        Parse.initialize(new Parse.Configuration.Builder(this)
            .applicationId("nl")
            .server("http://159.203.73.182:1337/parse").build());

        Log.e(TAG, "successfully connected to Parse");

        ParseAnonymousUtils.logIn(new LogInCallback(){
            @Override
            public void done(ParseUser user, ParseException e){
                if(e != null){
                    Log.e(TAG, "annon login failed", e);
                }else{

                    //Tests.createListing();
                    //Tests.markInterest();
                    //Tests.getListing();

                }
            }
        });

    }

}
