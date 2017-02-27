package com.example.andrew.neighborlabour;

import android.util.Log;

import com.example.andrew.neighborlabour.Utils.ListCB;
import com.example.andrew.neighborlabour.Utils.ListingCB;
import com.example.andrew.neighborlabour.Utils.ParseObjectCB;
import com.example.andrew.neighborlabour.Utils.SuccessCB;
import com.example.andrew.neighborlabour.listings.Filter;
import com.example.andrew.neighborlabour.listings.Listing;
import com.example.andrew.neighborlabour.listings.ListingManager;
import com.example.andrew.neighborlabour.user.UserManager;
import com.parse.ParseObject;

import java.util.Date;
import java.util.List;

/**
 * Created by chevalierc on 2/25/2017.
 */

public class Tests {
    final static String TAG = "TESTS";

    public static void getListing(){
        ListingManager.getListing("j8B9TBcDBS", new ParseObjectCB() {
            @Override
            public void done(String error, ParseObject response) {
                Log.i(TAG, "Listing: " + response.toString());
            }
        });
    }

    public static void getMyListings(){
        UserManager.getListingsUserPosted(new ListCB(){
            @Override
            public void done(String error, List<ParseObject> objects) {
                for(int i = 0; i < objects.size(); i++){
                    ParseObject listing = objects.get(i);
                    Log.i(TAG,i + " My listing " + listing.getString("title"));
                }
            }
        });
    }

    public static void markInterest(){
        ListingManager.markInterest("j8B9TBcDBS", new SuccessCB() {
            @Override
            public void done(String error, boolean success) {
                Log.i(TAG, "marked Interest" + error);
                UserManager.getListingsUserAppliedFor(new ListCB(){
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

    public static void printListings(){
        Log.e(TAG, "Running Print Listings");
        Filter filter = new Filter();
        ListingManager.getListings(filter, new ListCB(){
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

    public static void createListing(){
        Log.e(TAG, "Running Test Function");
        Listing listing = new Listing();
        listing.title = "Walk a Poodle";
        listing.description = "Need someone to walk my poodle. Possible more work to come if Rex likes you.";
        listing.compensation = 20.00;
        listing.startTime = new Date();
        listing.duration = 45;
        listing.address = "555 huntingtonn ave. Boston, MA";
        ListingManager.createListing(listing, new SuccessCB(){
            @Override
            public void done(String error, boolean success) {
                Log.e(TAG, "Create Listing: " + error);
            }
        });
    }

}
