package com.example.andrew.neighborlabour.UI.jobListings;

import android.content.Context;
import android.graphics.Typeface;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.andrew.neighborlabour.ParseProject;
import com.example.andrew.neighborlabour.R;
import com.example.andrew.neighborlabour.Services.Utils.Conversions;
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.array_item_listing, parent, false);
            final ViewHolder holder = new ViewHolder();

            holder.body = (TextView) convertView.findViewById(R.id.tvBody);
            holder.title = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.compensation = (TextView) convertView.findViewById(R.id.tvCompensation);
            holder.duration = (TextView) convertView.findViewById(R.id.tvDuration);
            holder.distance = (TextView) convertView.findViewById(R.id.tvDistance);
            holder.date = (TextView) convertView.findViewById(R.id.tvDate);

            convertView.setTag(holder);
        }

        //set
        final ParseObject listing = getItem(position);
        final ViewHolder holder = (ViewHolder)convertView.getTag();


        //set text fields
        holder.body.setText( listing.getString("descr") );
        holder.title.setText( listing.getString("title") );
        holder.compensation.setText( "$" + listing.getInt("compensation") );
        holder.duration.setText(Conversions.minutesToString(listing.getInt("duration") ));
        holder.distance.setText(getDistanceFromUser(listing));
        holder.date.setText(formatDateAsString(listing));

        return convertView;
    }

    private String formatDateAsString(ParseObject listing) {
        Date date = listing.getDate("startTime");
        DateFormat df = new SimpleDateFormat("M/dd");
        return df.format(date);
    }

    public String getDistanceFromUser(ParseObject listing){
        Location userLocation = ParseProject.getUserLocation();
        Log.i(TAG, userLocation + "");
        if(userLocation != null){
            ParseGeoPoint parseListingLocation = listing.getParseGeoPoint("geopoint");
            Location listingLocation = new Location("");
            listingLocation.setLatitude( parseListingLocation.getLatitude() );
            listingLocation.setLongitude( parseListingLocation.getLongitude() );

            Log.i(TAG, parseListingLocation.getLatitude() +", " + parseListingLocation.getLongitude() );
            Log.i(TAG, userLocation.getLatitude() + ", " + userLocation.getLongitude() );

            float distanceInKM = userLocation.distanceTo(listingLocation)/1000;
            return (Math.round(distanceInKM * 0.621371 * 10) / 10) + " miles";
        }
        return null;
    }


    final class ViewHolder {
        public TextView title;
        public TextView body;
        public TextView duration;
        public TextView distance;
        public TextView date;
        public TextView compensation;
    }

}
