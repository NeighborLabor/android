package com.example.andrew.neighborlabour;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.andrew.neighborlabour.Utils.ListCB;
import com.example.andrew.neighborlabour.listings.Filter;
import com.example.andrew.neighborlabour.listings.ListingArrayAdapter;
import com.example.andrew.neighborlabour.listings.ListingManager;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

public class ListingsActivity extends AppCompatActivity {

    private static final String TAG = "ListingActivity";

    ListView lvListings;

    ListingArrayAdapter listingAdapter;
    ArrayList<ParseObject> mlistings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listing_activity);
        Log.i(TAG, "Main Activity Created");
        setupListings();
    }

    void setupListings(){
        lvListings = (ListView) findViewById(R.id.lvListings);
        mlistings = new ArrayList<>();

        lvListings.setTranscriptMode(1);
        listingAdapter = new ListingArrayAdapter(ListingsActivity.this, mlistings);
        lvListings.setAdapter(listingAdapter);

        Log.i(TAG, "Listing Adapter Setup");
        refreshListings();
    }

    void refreshListings(){
        ListingManager.getListings(new Filter(), new ListCB() {
            @Override
            public void done(String error, List<ParseObject> listings) {
                if(error == null){
                    mlistings.clear();
                    mlistings.addAll(listings);
                    listingAdapter.notifyDataSetChanged();
                    Log.i(TAG, "Listing Adapter Data Set. Items found: " + listings.size());
                }else{
                    Log.e("message", "Error Loading Listings: " + error);
                }
            }
        });

    }

}
