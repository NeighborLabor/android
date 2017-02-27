package com.example.andrew.neighborlabour;

/**
 * Created by andrew on 2/17/17.
 */

import android.app.Application;
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


public class ParseProject extends Application{
    private final String TAG = "ParseProject";

    public void onCreate(){
        super.onCreate();

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

                    Tests.createListing();
                    //Tests.markInterest();
                    //Tests.getListing();

                }
            }
        });

    }

}
