package com.example.andrew.neighborlabour.UI.active;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.andrew.neighborlabour.ParseProject;
import com.example.andrew.neighborlabour.R;
import com.example.andrew.neighborlabour.Services.Utils.ListCB;
import com.example.andrew.neighborlabour.Services.user.UserManager;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chevalierc on 2/27/2017.
 */

public class ActiveJobsFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "ListingsActivity";



    ListView lvListings;
    ActiveJobsArrayAdapter listingAdapter;
    ArrayList<ParseObject> mlistings;
    AppliedJobsArrayAdapter appliedListingAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Main Activity Created");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstance){
        return inflater.inflate(R.layout.fragment_active_jobs, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        setupListings();
    }

    void setupListings(){
        lvListings = (ListView) getView().findViewById(R.id.lvListings);
        mlistings = new ArrayList<>();



        final Button btJobsAppliedFor = (Button) getView().findViewById(R.id.btJobsAppliedFor);
        final Button btJobsPosted = (Button) getView().findViewById(R.id.btJobsPosted);

        lvListings.setTranscriptMode(1);
        appliedListingAdapter = new AppliedJobsArrayAdapter(ParseProject.getContext(), mlistings);
        lvListings.setAdapter(appliedListingAdapter);


        btJobsAppliedFor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                appliedListingAdapter = new AppliedJobsArrayAdapter(ParseProject.getContext(), mlistings);
                lvListings.setAdapter(appliedListingAdapter);

                refreshJobsApplied();
                btJobsAppliedFor.setBackgroundColor(getResources().getColor(android.R.color.white));
                btJobsPosted.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            }
        });

        btJobsPosted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listingAdapter = new ActiveJobsArrayAdapter(ParseProject.getContext(), mlistings);
                lvListings.setAdapter(listingAdapter);

                refreshJobsPosted();
                btJobsAppliedFor.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                btJobsPosted.setBackgroundColor(getResources().getColor(android.R.color.white));
            }
        });



        refreshJobsApplied();
        btJobsAppliedFor.setBackgroundColor(getResources().getColor(android.R.color.white));
        btJobsPosted.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
    }



    void refreshJobsApplied(){
        UserManager.getListingsUserAppliedFor( new ListCB() {
            @Override
            public void done(String error, List<ParseObject> listings) {
                if(error == null){
                    mlistings.clear();
                    mlistings.addAll(listings);
                    appliedListingAdapter.notifyDataSetChanged();
                    Log.i(TAG, "Listing Adapter Data Set. Items found: " + listings.size());
                }else{
                    Toast.makeText(ParseProject.getContext(), error, Toast.LENGTH_SHORT).show();
                    Log.e("message", "Error Loading Listings: " + error);
                }
            }
        });
    }

    void refreshJobsPosted(){
        UserManager.getListingsUserPosted( new ListCB() {
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
