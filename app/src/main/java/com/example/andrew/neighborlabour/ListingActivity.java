package com.example.andrew.neighborlabour;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.andrew.neighborlabour.Utils.ListingCB;
import com.example.andrew.neighborlabour.Utils.ParseObjectCB;
import com.example.andrew.neighborlabour.listings.Listing;
import com.example.andrew.neighborlabour.listings.ListingManager;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by chevalierc on 2/26/2017.
 */

public class ListingActivity extends AppCompatActivity {
    final String TAG = "ListingActivity";
    TextView title;
    TextView compensationDuration;
    TextView tvEmployer;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        final String listingId = intent.getStringExtra("ObjectId");
        setContentView(R.layout.listing_activity);

        title = (TextView)findViewById(R.id.tvTitle);
        compensationDuration = (TextView)findViewById(R.id.tvCompensationDuration);
        tvEmployer = (TextView)findViewById(R.id.tvEmployer);

        Log.e(TAG, "listingId: " + listingId);

        title.setText("listingId");

        ListingManager.getListing(listingId, new ListingCB() {
            @Override
            public void done(String error, Listing listing) {
                title.setText(listing.title);
            }
        });
    }

}
