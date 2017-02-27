package com.example.andrew.neighborlabour.listings;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.example.andrew.neighborlabour.ParseProject;
import com.example.andrew.neighborlabour.Utils.ListCB;
import com.example.andrew.neighborlabour.Utils.ListingCB;
import com.example.andrew.neighborlabour.Utils.ParseObjectCB;
import com.example.andrew.neighborlabour.Utils.SuccessCB;
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

    public static ParseGeoPoint getLocationFromAddress(String strAddress){
        Log.i(TAG, "getting geo-location for " + strAddress);
        Geocoder coder = new Geocoder(ParseProject.getContext());
        List<Address> address;

        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address==null) return null;

            Address location=address.get(0);
            Log.i(TAG, "Created Location " + (double) (location.getLatitude()) + "," + (double) (location.getLongitude()));
            ParseGeoPoint point = new ParseGeoPoint((double) (location.getLatitude()), (double) (location.getLongitude()));
            return point;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void createListing(Listing listing, final SuccessCB cb){
        //TODO: Better content checking on inputs
        final ParseObject newListing = ParseObject.create("Listing");

        if(listing.compensation != null){
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
            ParseGeoPoint location = getLocationFromAddress(listing.address);
            if(location != null) newListing.put("geopoint", location );
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
                        String currentUserId = ParseUser.getCurrentUser().getObjectId();
                        listing.add("applicants", currentUserId);
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
                    if(userIds == null) userIds = new ArrayList<String>();//above call returns null instead of empty list
                    applicantsQuery.whereContainedIn("objectId", userIds);
                    applicantsQuery.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(final List<ParseUser> applicants, ParseException e) {
                            if(e == null){
                                listing.applicants = applicants;
                                //get Employer
                                ParseQuery<ParseUser> employerQuery = ParseUser.getQuery();
                                String employerId = parseListing.getString("createdBy");
                                employerQuery.whereEqualTo("objectId", employerId);
                                employerQuery.findInBackground(new FindCallback<ParseUser>() {
                                     @Override
                                     public void done(List<ParseUser> employers, ParseException e) {
                                         if(e == null){
                                             if(employers.size() != 0){
                                                 listing.employer = employers.get(0);
                                             }
                                             //get Worker
                                             ParseQuery<ParseUser> workerQuery = ParseUser.getQuery();
                                             String employerId = parseListing.getString("worker");
                                             workerQuery.whereEqualTo("objectId", employerId);
                                             workerQuery.findInBackground(new FindCallback<ParseUser>() {
                                                 @Override
                                                 public void done(List<ParseUser> workers, ParseException e) {
                                                     if(e == null){
                                                         if(workers.size() != 0){
                                                             listing.worker = workers.get(0);
                                                         }
                                                         cb.done(null, listing);
                                                     }else{
                                                         cb.done(e + "", null);
                                                     }
                                                 }
                                             });
                                             Listing listing = new Listing(parseListing);
                                         }else{
                                             cb.done(e + "", null);
                                         }
                                     }
                                 });
                            }else{
                                cb.done(e + "", null);
                            }
                        }
                    });
                } else {
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
