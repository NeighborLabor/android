package com.example.andrew.neighborlabour.Services.user;

import android.util.Log;

import com.example.andrew.neighborlabour.Services.Utils.ListCB;
import com.example.andrew.neighborlabour.Services.Utils.ParseObjectCB;
import com.example.andrew.neighborlabour.Services.Utils.SuccessCB;
import com.example.andrew.neighborlabour.Services.Utils.UserCB;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

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

    public static void writeReview(String userId, int stars, String body, final SuccessCB cb){
        ParseObject review = new ParseObject("Review");
        review.put("userId", userId);
        review.put("stars", stars);
        review.put("body", body);
        review.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null ){
                    cb.done(null, true);
                } else{
                    cb.done(e + "", false);
                }
            }
        });
    }

    public static void getReviews(String userId, final ListCB cb){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Review");
        //query.whereEqualTo("user", userId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> reviews, ParseException e) {
                if(reviews != null && !reviews.isEmpty()){
                    cb.done(null, reviews);
                } else{
                    cb.done(e +"", null);
                }
            }
        });
    }

    public static void getUser(String userId, final ParseObjectCB cb){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.whereEqualTo("objectId", userId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> users, ParseException e) {
                if(e == null && !users.isEmpty()){
                    cb.done(null, (ParseUser) users.get(0));
                } else if(users != null && users.isEmpty()){
                    cb.done("Returned no results", null);
                } else{
                    cb.done(e +"", null);
                }
            }
        });
    }


}
