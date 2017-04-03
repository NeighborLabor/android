package com.example.andrew.neighborlabour.Services.user;

import android.util.Log;

import com.example.andrew.neighborlabour.Services.Utils.ListCB;
import com.example.andrew.neighborlabour.Services.Utils.UserCB;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;
import java.util.Queue;

/**
 * Created by chevalierc on 2/23/2017.
 */

public class UserManager {
    private static final String TAG = "UserManagment";

    public static void getListingsUserAppliedFor(final ListCB cb){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Listing");
        ParseUser currentUser =  ParseUser.getCurrentUser();
        query.whereEqualTo("applicants", currentUser); //This might not be the correct query

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> listings, ParseException e) {
                if(e == null){
                    cb.done(null, listings);
                }else{
                    cb.done(e + "", null);
                }
            }
        });
    }

    public static void getListingsUserPosted(final ListCB cb){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Listing");
        ParseUser user = ParseUser.getCurrentUser();
        query.whereEqualTo("createdBy", user); //This might not be the correct query

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> listings, ParseException e) {
                if(e == null){
                    cb.done(null, listings);
                }else{
                    cb.done(e + "", null);
                }
            }
        });
    }

    public static void getUser(String userId, final UserCB cb){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.whereEqualTo("objectId", userId);


        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null && !objects.isEmpty()){
                    cb.done(null, (ParseUser) objects.get(0));
                } else if(objects != null && objects.isEmpty()){
                    cb.done("Returned no results", null);
                } else{
                    cb.done(e +"", null);
                }
            }
        });
    }


}
