package com.example.andrew.neighborlabour.UI.activeJobs;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrew.neighborlabour.ParseProject;
import com.example.andrew.neighborlabour.R;
import com.example.andrew.neighborlabour.UI.activeJobs.applicats.SelectWorkerDialog;
import com.parse.Parse;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by chevalierc on 2/26/2017.
 */

public class ActiveJobsArrayAdapter extends ArrayAdapter<ParseObject> {
    final String TAG = "ListingArrayAdapter";

    ListView listView;





    public ActiveJobsArrayAdapter(Context context, ArrayList<ParseObject> listings){

        super(context,0,listings);


    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent ){


        //create view if it doesnt exist
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.active_listing_item, parent, false);
            final ViewHolder holder = new ViewHolder();
            holder.body = (TextView) convertView.findViewById(R.id.active_tvBody);
            holder.title = (TextView) convertView.findViewById(R.id.active_tvTitle);
            holder.compensationDuration = (TextView) convertView.findViewById(R.id.active_tvCompensationDuration);
            holder.address = (TextView) convertView.findViewById(R.id.active_tvAddress);
            convertView.setTag(holder);
        }

        //set
        final ParseObject listing = getItem(position);
        final ViewHolder holder = (ViewHolder)convertView.getTag();


        holder.allApps = (Button) convertView.findViewById(R.id.active_seeApps);
        holder.messaging = (Button) convertView.findViewById(R.id.active_Message);


        if(listing.get("worker") == null){
            //if this is a job you posted that you haven't selected a worker
            holder.allApps.setVisibility(View.VISIBLE);
            holder.messaging.setVisibility(View.GONE);

        } else if(listing.get("worker") != null) {
            //jobs you posted posted and have selected a worker
            holder.allApps.setVisibility(View.GONE);
            holder.messaging.setVisibility(View.VISIBLE);
        }


        holder.body.setText(listing.getString("descr"));
        holder.title.setText(listing.getString("title"));
        holder.compensationDuration.setText(getCompensationDuration(listing));
        holder.address.setText( listing.getString("address") + " ( 1mile");

        holder.messaging.setOnClickListener(mMessagingClickListener);

        holder.allApps.setOnClickListener(mAllAppsClickListener);

        listView = (ListView) parent;

        return convertView;
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
    public View.OnClickListener mAllAppsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int position = listView.getPositionForView((View) v.getParent());
            ParseObject object = getItem(position);


            Intent intent = new Intent(getContext(), SelectWorkerDialog.class);

            intent.putExtra("ObjectID", object.getObjectId().toString());

            getContext().startActivity(intent);

        }
    };

    public View.OnClickListener mMessagingClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int position = listView.getPositionForView((View) v.getParent());
            ParseObject object = getItem(position);
            Toast.makeText(getContext(), object.get("title").toString(), Toast.LENGTH_SHORT).show();

        }
    };


    final class ViewHolder {
        public TextView title;
        public TextView body;
        public TextView compensationDuration;
        public TextView address;
        public Button allApps;
        public Button messaging;
    }

}
