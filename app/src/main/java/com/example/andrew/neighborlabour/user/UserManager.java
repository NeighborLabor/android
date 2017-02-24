package com.example.andrew.neighborlabour.user;

import android.util.Log;

import com.example.andrew.neighborlabour.Utils.ListCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by chevalierc on 2/23/2017.
 */

public class UserManager {
    private static final String TAG = "UserManagment";

    public static void getListingsUserAppliedFor(final ListCallback cb){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("listing");
        String currentUserId = (String) ParseUser.getCurrentUser().getObjectId();
        query.whereContains("applicants", currentUserId); //This might not be the correct query

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> listings, ParseException e) {
                if(e == null){
                    cb.done(null, listings);
                }else{
                    cb.done(e + "", listings);
                }
            }
        });
    }

    public static void getListingsUserPosted(final ListCallback cb){
        ParseUser curUser = ParseUser.getCurrentUser();
        List<ParseObject> listingsIds = curUser.getList("listings");

        Log.i(TAG, "listings " + listingsIds.get(0));

        ParseQuery<ParseObject> query = ParseQuery.getQuery("listing");
        query.whereContainedIn("objectId", listingsIds);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> listings, ParseException e) {
                if(e == null){
                    cb.done(null, listings);
                }else{
                    cb.done(e + "", listings);
                }
            }
        });
    }
}
