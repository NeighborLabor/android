package com.example.andrew.neighborlabour;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.andrew.neighborlabour.Utils.ListCB;
import com.example.andrew.neighborlabour.listings.Filter;
import com.example.andrew.neighborlabour.listings.ListingArrayAdapter;
import com.example.andrew.neighborlabour.listings.ListingManager;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

public class ListingsActivity extends AppCompatActivity {

    private static final String TAG = "ListingsActivity";

    ListView lvListings;
    ListingArrayAdapter listingAdapter;
    ArrayList<ParseObject> mlistings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listings_activity);
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

        lvListings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int i, long arg3) {
                Log.i(TAG, "listing clicked "+  i + mlistings.get(i).getString("title"));
                Intent intent = new Intent(ParseProject.getContext(), ListingActivity.class);
                intent.putExtra("ObjectId", mlistings.get(i).getObjectId());
                startActivity(intent);
            }
        });

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
