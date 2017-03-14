package com.example.andrew.neighborlabour.UI.jobListings;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.andrew.neighborlabour.ParseProject;
import com.example.andrew.neighborlabour.R;
import com.example.andrew.neighborlabour.Services.Utils.ListCB;
import com.example.andrew.neighborlabour.Services.listings.Filter;
import com.example.andrew.neighborlabour.Services.listings.ListingManager;
import com.example.andrew.neighborlabour.UI.jobListings.Maps.MapDialogFragment;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

public class ListingsFragment extends Fragment {

    private static final String TAG = "ListingsActivity";

    ListView lvListings;
    static ListingArrayAdapter listingAdapter;
    static ArrayList<ParseObject> mlistings;
    FiltersDialogFragment filtersDialog = new FiltersDialogFragment();
    MapDialogFragment mapDialogFragment = new MapDialogFragment();
    static Filter filter = new Filter();

    ImageView BtFilters;
    ImageView BtSearch;
    EditText EtSearch;
    Button BtMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstance){
        return inflater.inflate(R.layout.activity_job_listings, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        setupListings();
        setUpButtons();
    }

    void setUpButtons(){
        BtMap = (Button) getView().findViewById(R.id.BtMap);
        BtMap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mapDialogFragment.show(getActivity().getFragmentManager(), "MapDialog");
            }


        });
        BtFilters = (ImageView) getView().findViewById(R.id.BtFilters);
        BtFilters.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {;BtMap = (Button) getView().findViewById(R.id.BtMap);
                filtersDialog.show(getActivity().getFragmentManager(), "NoticeDialogFragment");
            }
        });
        BtSearch = (ImageView) getView().findViewById(R.id.BtSearch);
        EtSearch = (EditText) getView().findViewById(R.id.EtSearch);
        BtSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ListingsFragment.filter.searchTerm = EtSearch.getText().toString();
                refreshListings();
            }
        });
    }

    static void setFilter(Filter filter){
        String searchTerm = filter.searchTerm;
        ListingsFragment.filter = filter;
        filter.searchTerm = searchTerm;
        refreshListings();
    }

    static Filter getFilter(){
        return ListingsFragment.filter;
    }

    void setupListings(){
        lvListings = (ListView) getView().findViewById(R.id.lvListings);
        mlistings = new ArrayList<>();

        lvListings.setTranscriptMode(1);
        listingAdapter = new ListingArrayAdapter(ParseProject.getContext(), mlistings);
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

    static void refreshListings(){
        ListingManager.getListings(ListingsFragment.filter, new ListCB() {
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
