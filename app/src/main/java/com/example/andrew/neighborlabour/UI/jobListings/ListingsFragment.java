package com.example.andrew.neighborlabour.UI.jobListings;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.andrew.neighborlabour.ParseProject;
import com.example.andrew.neighborlabour.R;
import com.example.andrew.neighborlabour.Services.Utils.ListCB;
import com.example.andrew.neighborlabour.Services.listings.Filter;
import com.example.andrew.neighborlabour.Services.listings.ListingManager;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

public class ListingsFragment extends Fragment {

    private static final String TAG = "ListingsActivity";

    ListView lvListings;
    ListingArrayAdapter listingAdapter;
    ArrayList<ParseObject> mlistings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.listings_activity);
        Log.i(TAG, "Main Activity Created");
        //setupListings();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstance){
        return inflater.inflate(R.layout.listings_activity, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        setupListings();
    }

    void setupListings(){
        lvListings = (ListView) getView().findViewById(R.id.lvListings);
        mlistings = new ArrayList<>();

        lvListings.setTranscriptMode(1);
        listingAdapter = new ListingArrayAdapter(ParseProject.getContext(), mlistings);
        //listingAdapter = new ListingArrayAdapter(ListingsActivity.this, mlistings);
        lvListings.setAdapter(listingAdapter);

        Log.i(TAG, "Listing Adapter Setup");

        lvListings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int i, long arg3) {
                Intent intent = new Intent(ParseProject.getContext(), ListingDetailActivity.class);
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
                    Toast.makeText(ParseProject.getContext(), error, Toast.LENGTH_SHORT).show();
                    Log.e("message", "Error Loading Listings: " + error);
                }
            }
        });

    }

}
