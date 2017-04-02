package com.example.andrew.neighborlabour.Services.listings;

import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.example.andrew.neighborlabour.ParseProject;
import com.example.andrew.neighborlabour.Services.Utils.Conversions;
import com.example.andrew.neighborlabour.Services.Utils.ListCB;
import com.example.andrew.neighborlabour.Services.Utils.ListingCB;
import com.example.andrew.neighborlabour.Services.Utils.ParseObjectCB;
import com.example.andrew.neighborlabour.Services.Utils.SuccessCB;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chevalierc on 2/23/2017.
 */

public class ListingManager {
    public static final String TAG = "ListingManager";


    public static void createListing(Listing listing, final SuccessCB cb){
        //TODO: Better content checking on inputs
        final ParseObject newListing = ParseObject.create("Listing");

        if(listing.compensation != 0){
            newListing.put("compensation", listing.compensation);
        }else{
            cb.done("Error: compensation doesn't meet requirements", false);
            return;
        }

        if(listing.duration >= 5){
            newListing.put("duration", listing.duration);
        }else{
            cb.done("Error: duration doesn't meet requirements", false);
            return;
        }

        if(listing.title != null && listing.title.length() >= 6){
            newListing.put("title", listing.title);
        }else{
            cb.done("Error: Title doesn't meet requirements", false);
            return;
        }

        if(listing.startTime != null){
            newListing.put("startTime", listing.startTime);
        }else{
            cb.done("Error: StartTime doesn't meet requirements", false);
            return;
        }

        if(listing.description != null && listing.description.length() >= 6){
            newListing.put("descr", listing.description);
        }else{
            cb.done("Error: Description doesn't meet requirements", false);
            return;
        }

        if(listing.address != null && listing.address.length() >= 6){
            newListing.put("address", listing.address);
            LatLng location = Conversions.getLocationFromAddress(listing.address);
            if (location != null){
                ParseGeoPoint pLocation = new ParseGeoPoint(location.latitude, location.longitude);
                newListing.put("geopoint", pLocation );
            }
        }else{
            cb.done("Error: Address doesn't meet requirements", false);
            return;
        }

        //set employer
        ParseObject currentUser = ParseUser.getCurrentUser();
        String userId = currentUser.getObjectId();
        newListing.put("createdBy", ParseObject.createWithoutData("_User", userId));
        //set active to false
        newListing.put("active", false);

        newListing.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    cb.done( null, true);
                }else{
                    cb.done( e + "", false);
                }
            }
        });
    }

    public static void markInterest(String listingId, final SuccessCB cb){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Listing");
        query.whereEqualTo("objectId", listingId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null) {
                    cb.done(e+ "", false);
                } else {
                    if(objects.size() != 0){
                        final ParseObject listing = objects.get(0);
                        ParseUser currentUser = ParseUser.getCurrentUser();
                        listing.getRelation("applicants").add(currentUser);
                        listing.saveInBackground(new SaveCallback(){
                            @Override
                            public void done(ParseException e) {
                                if( e == null){
                                    Log.i(TAG, "Marked interest for " + listing.getString("title"));
                                    cb.done(null, true);
                                }else{
                                    cb.done(e + "", false);
                                }
                            }
                        });
                    }else{
                        cb.done("No listing found with that ID", false);
                    }
                }
            }
        });
    }

    public static void selectWorker(String listingId, final String workerId, final ParseObjectCB cb){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Listing");
        query.whereEqualTo("objectId", listingId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null) {
                    cb.done(e+ "", null);
                } else {
                    final ParseObject listing = objects.get(0);
                    Log.d("SELECT_WORKER", String.valueOf(objects.size()));
                    String currentUserId = (String) ParseUser.getCurrentUser().get("objectId");
                    listing.put("worker", workerId);
                    listing.put("active", false);
                    listing.saveInBackground(new SaveCallback(){
                        @Override
                        public void done(ParseException e) {
                            if( e == null){
                                cb.done(null, listing);


                            }else{
                                cb.done(e.toString(), listing);
                            }
                        }
                    });
                }
            }
        });
    }

    //get a listing as well as relations(applicants,employer)
    public static void getListing(String listingId, final ListingCB cb){
        //get listing
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Listing");
        query.getInBackground(listingId, new GetCallback<ParseObject>() {
            public void done(final ParseObject parseListing, ParseException e) {
                if (e == null) {
                    final Listing listing = new Listing(parseListing);

                    //get applicants
                    ParseQuery<ParseUser> applicantsQuery = ParseUser.getQuery();
                    List<String> userIds = parseListing.getList("applicants");
                    if(userIds == null){
                        Log.i(TAG, "ApplicantIDS not found");
                        userIds = new ArrayList<String>();//above call returns null instead of empty list
                    }
                    applicantsQuery.whereContainedIn("objectId", userIds);
                    applicantsQuery.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(final List<ParseUser> applicants, ParseException e) {
                            if(e == null) {

                                //set applicants
                                if (applicants == null) {
                                    Log.i(TAG, "applicants not found");
                                } else {
                                    listing.applicants = applicants;
                                }

                                //get Employer
                                parseListing.getParseUser("createdBy").fetchIfNeededInBackground(new GetCallback<ParseUser>() {
                                    @Override
                                    public void done(ParseUser employer, ParseException e) {

                                        //set employer
                                        if(e == null){
                                            listing.setEmployer(employer);
                                        }else{
                                            Log.i(TAG, "employer not found");
                                        }

                                        if(parseListing.getParseUser("worker") == null){
                                            cb.done(null, listing);
                                            return;
                                        }

                                        //get worker
                                        parseListing.getParseUser("worker").fetchIfNeededInBackground(new GetCallback<ParseUser>() {
                                            @Override
                                            public void done(ParseUser worker, ParseException e) {

                                                //set worker
                                                if(e == null){
                                                    listing.setWorker(worker);
                                                }else{
                                                    Log.i(TAG, "employer not found");
                                                }

                                                cb.done(null, listing);
                                                return;
                                            }
                                        });

                                    }
                                });
                            }
                        }
                    });
                } else {
                    Log.i(TAG, "Listing Not Found");
                    cb.done(e + "", null);
                }
            }
        });
    }

    public static void getListings(Filter filter, final ListCB cb){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Listing");

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
            query.whereGreaterThan("startTime", filter.startDate);
        }

        if(filter.latitude != 0 && filter.longitude != 0){
            ParseGeoPoint userLocation = new ParseGeoPoint( filter.latitude, filter.longitude);
            query.whereWithinMiles("geopoint", userLocation, filter.maxDistance.doubleValue());
        }

        if(filter.searchTerm != null && filter.searchTerm.length() >  0){
            query.whereContains("title", filter.searchTerm);
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
