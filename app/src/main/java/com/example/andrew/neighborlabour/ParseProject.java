package com.example.andrew.neighborlabour;

/**
 * Created by andrew on 2/17/17.
 */

import android.app.Application;
import android.util.Log;

import com.example.andrew.neighborlabour.Utils.Callback;
import com.example.andrew.neighborlabour.Utils.ListCallback;
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

                    //createListing();
                    getMyListings();

                }
            }
        });

    }

    public void getMyListings(){
        UserManager.getListingsUserPosted(new ListCallback(){
            @Override
            public void done(String error, List<ParseObject> objects) {
                for(int i = 0; i < objects.size(); i++){
                    ParseObject listing = objects.get(i);
                    Log.i(TAG,i + " My listing " + listing.getString("title"));
                }
            }
        });
    }

    public void markInterest(){
        ListingManager.markInterest("xD0a7YLiwN", new Callback() {
            @Override
            public void done(String error, ParseObject response) {
                Log.i(TAG, "marked Interest" + error);
                UserManager.getListingsUserAppliedFor(new ListCallback(){
                    @Override
                    public void done(String error, List<ParseObject> response) {
                        for(int i = 0; i < response.size(); i++){
                            ParseObject listing = response.get(i);
                            Log.i(TAG, "Am Intefrested in:" + listing.getString("title"));
                        }

                    }
                });
            }
        });
    }

    public void printListings(){
        Log.e(TAG, "Running Print Listings");
        Filter filter = new Filter();
        ListingManager.getListings(filter, new ListCallback(){
            @Override
            public void done(String error, List<ParseObject> objects) {
                if(error != null) Log.e(TAG, error);
                Log.i(TAG, objects.size() + "items found in get query");
                for(int i = 0; i < objects.size(); i++){
                    ParseObject listing = objects.get(i);
                    Log.i(TAG,i + listing.getString("title"));
                }
            }

        });
    }

    public void createListing(){
        Log.e(TAG, "Running Test Function");
        Listing listing = new Listing();
        listing.title = "walk a doog";
        listing.description = "walk a doog";
        listing.compensation = 5.00;
        listing.startTime = new Date();
        listing.length = 50;
        listing.address = "555 hiuntingtonn";
        ListingManager.createListing(listing, new Callback(){
            @Override
            public void done(String error, ParseObject response) {
                Log.e(TAG, "Callback Called");
                ParseObject listing = (ParseObject) response;
                String title = listing.getString("title");
                Log.i(TAG, title);
            }

        });
    }

}
