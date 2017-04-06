package com.example.andrew.neighborlabour.UI.active;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

/**
 * Created by chevalierc on 2/27/2017.
 */

public class ActiveJobsFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "ListingsActivity";

    ListView lvListings;
    static ListingArrayAdapter listingAdapter;
    static ArrayList<ParseObject> mlistings;
    Button btJobsAppliedFor;
    Button btJobsPosted;
    static String page = "posted";


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
        setupGui();
        setUpPosted();
    }

    void setupGui(){
        btJobsAppliedFor = (Button) getView().findViewById(R.id.btJobsAppliedFor);
        btJobsPosted = (Button) getView().findViewById(R.id.btJobsPosted);

        lvListings = (ListView) getView().findViewById(R.id.lvListings);
        mlistings = new ArrayList<>();

        lvListings.setTranscriptMode(1);
        listingAdapter = new ListingArrayAdapter(ParseProject.getContext(), mlistings);
        lvListings.setAdapter(listingAdapter);

        btJobsAppliedFor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpApplied();
            }
        });

        btJobsPosted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpPosted();
            }
        });
    }

    void setUpPosted(){
        page = "posted";
        refreshJobsPosted();
        btJobsAppliedFor.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        btJobsPosted.setBackgroundColor(getResources().getColor(android.R.color.white));
        lvListings.setOnItemClickListener(jobsPostedListener);
    }

    void setUpApplied(){
        page = "applied";
        refreshJobsApplied();
        btJobsAppliedFor.setBackgroundColor(getResources().getColor(android.R.color.white));
        btJobsPosted.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        lvListings.setOnItemClickListener(jobsAppliedListener);
    }

    AdapterView.OnItemClickListener jobsAppliedListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int i, long arg3) {
            AppliedListingDetailDialog appliedListingDetailDialog = new AppliedListingDetailDialog();
            String listingId = mlistings.get(i).getObjectId();
            Bundle args = new Bundle();
            args.putString("listingId", listingId);
            appliedListingDetailDialog.setArguments(args);
            appliedListingDetailDialog.show(getActivity().getFragmentManager(), "NoticeDialogFragment");
        }
    };

    AdapterView.OnItemClickListener jobsPostedListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int i, long arg3) {
            PostedListingDetailDialog postedListingDetailDialog = new PostedListingDetailDialog();
            String listingId = mlistings.get(i).getObjectId();
            Bundle args = new Bundle();
            args.putString("listingId", listingId);
            postedListingDetailDialog.setArguments(args);
            postedListingDetailDialog.show(getActivity().getFragmentManager(), "NoticeDialogFragment");
        }
    };

    public static void refresh(){
        if(listingAdapter != null){
            if (page == "posted") {
                refreshJobsPosted();
            }else{
                refreshJobsApplied();
            }
        }
    }

    static void refreshJobsApplied(){
        UserManager.getListingsUserAppliedFor( new ListCB() {
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
