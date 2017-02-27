package com.example.andrew.neighborlabour;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.andrew.neighborlabour.Utils.ListingCB;
import com.example.andrew.neighborlabour.listings.Listing;
import com.example.andrew.neighborlabour.listings.ListingManager;

/**
 * Created by chevalierc on 2/26/2017.
 */

public class ListingActivity extends AppCompatActivity {
    final String TAG = "ListingActivity";
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title = (TextView)findViewById(R.id.tvTitle);

        Intent intent = getIntent();
        final String listingId = intent.getStringExtra("ObjectId");

        setContentView(R.layout.listing_activity);
        Log.e(TAG, "listingId: " + listingId);

        title.setText("listingId");

        ListingManager.getListing(listingId, new ListingCB() {
            @Override
            public void done(String error, Listing listing) {
                //title.setText("listingId");
            }
        });
    }
}
