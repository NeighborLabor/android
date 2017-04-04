package com.example.andrew.neighborlabour.Services.Utils;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.example.andrew.neighborlabour.ParseProject;
import com.example.andrew.neighborlabour.UI.MainActivity;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by chevalierc on 3/22/2017.
 */

public class Conversions {

    public static String minutesToString(int totalMinutes){
        int hours = totalMinutes / 60;
        int minutes = totalMinutes - (hours * 60);
        if( minutes < 10){
            return hours + ":" + minutes + 0;
        }else{
            return hours + ":" + minutes;
        }
    }

    public static LatLng getLocationFromAddress(String strAddress){
        Geocoder coder = new Geocoder(ParseProject.getContext());
        List<Address> address;

        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address==null) return null;
            if(address.size() == 0) return null;

            Address location=address.get(0);
            LatLng point = new LatLng((double) (location.getLatitude()), (double) (location.getLongitude()));
            return point;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String dateAsString(Date date) {
        DateFormat df = new SimpleDateFormat("M/dd");
        return df.format(date);
    }

    public static String getDistanceFromUser(ParseObject listing){
        Location userLocation = MainActivity.location;
        if(userLocation != null){
            ParseGeoPoint parseListingLocation = listing.getParseGeoPoint("geopoint");
            Location listingLocation = new Location("");
            listingLocation.setLatitude( parseListingLocation.getLatitude() );
            listingLocation.setLongitude( parseListingLocation.getLongitude() );
            float distanceInKM = userLocation.distanceTo(listingLocation)/1000;
            return (Math.round(distanceInKM * 0.621371 * 10) / 10) + " miles";
        }else{
            return "error";
        }
    }
}
