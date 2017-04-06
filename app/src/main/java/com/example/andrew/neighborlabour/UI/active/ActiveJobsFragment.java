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
import com.example.andrew.neighborlabour.UI.listings.ListingArrayAdapter;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by chevalierc on 2/27/2017.
 */

public class ActiveJobsFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "ListingsActivity";

    ListView lvListings;
    static ActiveJobsArrayAdapter listingAdapter;
    static ArrayList<ParseObject> mlistings;
    static AppliedJobsArrayAdapter appliedListingAdapter;

    final static int APPLIED = 1;
    final static int POSTED = 2;

    static int ApplyOrPosted = APPLIED;

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
        ApplyOrPosted = APPLIED;


        btJobsAppliedFor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                appliedListingAdapter = new AppliedJobsArrayAdapter(ParseProject.getContext(), mlistings);
                lvListings.setAdapter(appliedListingAdapter);

                refreshJobsApplied();
                btJobsAppliedFor.setBackgroundColor(getResources().getColor(android.R.color.white));
                btJobsPosted.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                ApplyOrPosted = APPLIED;
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

                ApplyOrPosted = POSTED;
            }
        });



        refreshJobsApplied();
        btJobsAppliedFor.setBackgroundColor(getResources().getColor(android.R.color.white));
        btJobsPosted.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
    }

    public static void refresh(){
        if(ApplyOrPosted  == APPLIED){
            refreshJobsApplied();
        }
        if(ApplyOrPosted == POSTED){
            refreshJobsPosted();
        }
    }



    static void refreshJobsApplied(){
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

    static void refreshJobsPosted(){
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
