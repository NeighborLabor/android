package com.example.andrew.neighborlabour.UI.jobListings;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.andrew.neighborlabour.ParseProject;
import com.example.andrew.neighborlabour.R;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by chevalierc on 2/26/2017.
 */

public class ListingArrayAdapter extends ArrayAdapter<ParseObject> {
    final String TAG = "ListingArrayAdapter";

    public ListingArrayAdapter(Context context, ArrayList<ParseObject> listings){
        super(context,0,listings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //create listing array view if it doesnt exist
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.listing_item, parent, false);
            final ViewHolder holder = new ViewHolder();
            holder.body = (TextView) convertView.findViewById(R.id.tvBody);
            holder.title = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.compensationDuration = (TextView) convertView.findViewById(R.id.tvCompensationDuration);
            holder.address = (TextView) convertView.findViewById(R.id.tvAddress);
            holder.date = (TextView) convertView.findViewById(R.id.tvDate);
            convertView.setTag(holder);
        }

        //set
        final ParseObject listing = getItem(position);
        final ViewHolder holder = (ViewHolder)convertView.getTag();

        //set text fields
        holder.body.setText(listing.getString("descr"));
        holder.title.setText(listing.getString("title"));
        holder.compensationDuration.setText(getCompensationDuration(listing));
        holder.address.setText( listing.getString("address") + " (1 mile)"); //getDistanceFromUser(listing)
        holder.date.setText( formatDateAsString(listing));

        return convertView;
    }

    private String formatDateAsString(ParseObject listing) {
        Date date = listing.getDate("startTime");
        DateFormat df = new SimpleDateFormat("M/dd/ K:mm a");
        return df.format(date);
    }

    public String getCompensationDuration(ParseObject listing){
        String compensationDuration = "$" + listing.getDouble("compensation") + " / ";
        int duration = listing.getInt("duration");
        if(duration > 59){
            int hours = ((duration+1) % 60);
            int minutes = duration - (hours * 60);
            compensationDuration +=  hours;
            if(minutes != 0) compensationDuration = "."  + Math.round(minutes/60 * 10) / 10 ;
            compensationDuration += hours == 1 ? " hr" : " hrs";
        }else{
            compensationDuration += duration + " mins";
        }
        return compensationDuration;
    }

    public String getDistanceFromUser(ParseObject listing){
        Location userLocation = ParseProject.getUserLocation();
        if(userLocation != null){
            ParseGeoPoint parseListingLocation = listing.getParseGeoPoint("geopoint");
            Location listingLocation = new Location("");
            listingLocation.setLatitude(parseListingLocation.getLatitude());
            listingLocation.setLongitude(parseListingLocation.getLongitude());
            float distanceInMeters = userLocation.distanceTo(listingLocation);
            return (Math.round(distanceInMeters * 1609.34 * 10) / 10) + " miles";
        }
        return null;
    }


    final class ViewHolder {
        public TextView title;
        public TextView body;
        public TextView compensationDuration;
        public TextView address;
        public TextView date;
    }

}
