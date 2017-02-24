package com.example.andrew.neighborlabour.listings;

import android.util.Log;

import com.example.andrew.neighborlabour.Utils.Callback;
import com.example.andrew.neighborlabour.Utils.ListCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

/**
 * Created by chevalierc on 2/23/2017.
 */

public class ListingManager {
    public static final String TAG = "ListingManager";

    public static void createListing(Listing listing, final Callback cb){
        //TODO: Better content checking on inputs
        final ParseObject newListing = ParseObject.create("listing");

        if(listing.compensation != null){
            newListing.put("compensation", listing.compensation);
        }else{
            cb.done("Error: compensation doesn't meet requirements", newListing);
            return;
        }

        if(listing.length >= 5){
            newListing.put("length", listing.length);
        }else{
            cb.done("Error: length doesn't meet requirements", newListing);
            return;
        }

        if(listing.title != null && listing.title.length() >= 6){
            newListing.put("title", listing.title);
        }else{
            cb.done("Error: Title doesn't meet requirements", newListing);
            return;
        }

        if(listing.description != null && listing.description.length() >= 6){
            newListing.put("descr", listing.description);
        }else{
            cb.done("Error: Description doesn't meet requirements", newListing);
            return;
        }

        if(listing.address != null && listing.address.length() >= 6){
            newListing.put("address", listing.address);
        }else{
            cb.done("Error: Address doesn't meet requirements", newListing);
            return;
        }

        newListing.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    ParseObject currentUser = ParseUser.getCurrentUser();
                    currentUser.add("listings", newListing);
                    currentUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){
                                cb.done( null, newListing);
                            }else{
                                cb.done( e + "", newListing);
                            }
                        }
                    });
                    cb.done( null, newListing);
                }else{
                    cb.done( e + "", newListing);
                }
            }
        });
    }

    public static void markInterest(String listingId, final Callback cb){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("listing");
        query.whereEqualTo("objectId", listingId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null) {
                    cb.done(e+ "", null);
                } else {
                    final ParseObject listing = objects.get(0);
                    Log.i(TAG, "Got listing for " + listing.getString("title"));
                    String currentUserId = ParseUser.getCurrentUser().getObjectId();
                    Log.i(TAG, "current UserOld: " + currentUserId);
                    listing.add("applicants", currentUserId);
                    listing.saveInBackground(new SaveCallback(){
                        @Override
                        public void done(ParseException e) {
                            if( e == null){
                                Log.i(TAG, "Marked interest for " + listing.getString("title"));
                                cb.done(null, listing);
                            }else{
                                cb.done(e + "", listing);
                            }
                        }
                    });
                }
            }
        });
    }

    public static void selectWorker(String listingId, final String workerId, final Callback cb){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("listing");
        query.whereEqualTo("objectId", listingId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    cb.done(e+ "", null);
                } else {
                    final ParseObject listing = objects.get(0);
                    String currentUserId = (String) ParseUser.getCurrentUser().get("objectId");
                    listing.put("worker", workerId);
                    listing.put("active", false);
                    listing.saveInBackground(new SaveCallback(){
                        @Override
                        public void done(ParseException e) {
                            if( e == null){
                                cb.done(null, listing);
                            }else{
                                cb.done(e + "", listing);
                            }
                        }
                    });
                }
            }
        });
    }

    public static void getListing(String listingId, final Callback cb){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("listing");
        query.getInBackground(listingId, new GetCallback<ParseObject>() {
            public void done(ParseObject listing, ParseException e) {
                if (e == null) {
                    cb.done(null, listing);
                } else {
                    cb.done(e + "", listing);
                }
            }
        });
    }

    public static void getListings(Filter filter, final ListCallback cb){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("listing");

        if(filter.maxCompensation != 0){
            query.whereLessThan("compensation", filter.maxCompensation);
        }
        if(filter.minCompensation != 0){
            query.whereGreaterThan("compensation", filter.minCompensation);
        }
        if(filter.category != null){
            query.whereEqualTo("category", filter.category);
        }
        if(filter.startDate != null){
            query.whereGreaterThan("startDate", filter.startDate);
        }
        if(filter.endDate != null){
            query.whereLessThan("endDate", filter.endDate);
        }
        if(filter.maxDistance != 0 && filter.longitude != 0 && filter.latitude != 0){
            ParseGeoPoint userLocation = new ParseGeoPoint(filter.longitude, filter.latitude);
            query.whereWithinMiles("geopoint", userLocation, filter.maxDistance.doubleValue());
        }

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
