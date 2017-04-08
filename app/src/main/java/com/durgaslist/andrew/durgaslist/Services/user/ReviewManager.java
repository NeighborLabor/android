package com.durgaslist.andrew.durgaslist.Services.user;

import android.util.Log;

import com.durgaslist.andrew.durgaslist.Services.Utils.ListCB;
import com.durgaslist.andrew.durgaslist.Services.Utils.ListingCB;
import com.durgaslist.andrew.durgaslist.Services.Utils.SuccessCB;
import com.durgaslist.andrew.durgaslist.Services.listings.Listing;
import com.durgaslist.andrew.durgaslist.Services.listings.ListingManager;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;


/**
 * Created by andrew on 4/4/17.
 */

public class ReviewManager {
    private final static String TAG = "ReviewManager";

    public static void PostReview(String listingId, String description, int rating, final SuccessCB cb) {
        final int reviewRating = rating;
        final String reviewDescription = description;
        Log.i(TAG, listingId);
        ListingManager.getListing(listingId, new ListingCB() {
            @Override
            public void done(String error, Listing nListing) {
                if(error != null){
                    cb.done(error + "", false);
                    return;
                }
                Log.i(TAG, nListing.id);
                final Listing listing = nListing;
                final String employerId = listing.employer.getObjectId();

                if(employerId.equals(ParseUser.getCurrentUser())){
                    listing.listing.put("employerReview", true);
                }else{
                    listing.listing.put("workerReview", true);
                }

                listing.listing.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        final ParseObject new_review = new ParseObject("Review");
                        new_review.put("rating", reviewRating);
                        new_review.put("descr", reviewDescription);

                        if(employerId.equals(ParseUser.getCurrentUser())){
                            new_review.getRelation("user").add(listing.worker);
                        }else{
                            new_review.getRelation("user").add(listing.employer);
                        }

                        new_review.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null) {
                                    cb.done(e + "", false);
                                } else {
                                    Log.d("ReviewManager", "success");
                                    cb.done(null, true);
                                }
                            }
                        });
                    }
                });
            }
        });

    }

    public static void getReviews(String userId, final ListCB cb){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.whereEqualTo("objectId", userId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> users, ParseException e) {
                if (users.isEmpty()){
                    cb.done("user not found", null);
                }else{
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Review");
                    query.whereEqualTo("user", (ParseUser) users.get(0));
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
            }
        });
    }

}
