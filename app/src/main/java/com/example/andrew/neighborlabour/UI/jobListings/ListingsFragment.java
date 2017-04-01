package com.example.andrew.neighborlabour.UI.jobListings;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.andrew.neighborlabour.MainActivity;
import com.example.andrew.neighborlabour.ParseProject;
import com.example.andrew.neighborlabour.R;
import com.example.andrew.neighborlabour.Services.Utils.ListCB;
import com.example.andrew.neighborlabour.Services.listings.Filter;
import com.example.andrew.neighborlabour.Services.listings.ListingManager;

import com.example.andrew.neighborlabour.UI.jobListings.Maps.JobHolder;
import com.example.andrew.neighborlabour.UI.jobListings.Maps.MapDialogFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

public class ListingsFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient mGoogleAPI;

    private static final String TAG = "ListingsActivity";

    ListView lvListings;
    static ListingArrayAdapter listingAdapter;
    static ArrayList<ParseObject> mlistings;
    FiltersDialogFragment filtersDialog = new FiltersDialogFragment();

    static Filter filter = new Filter();

    ImageView btFilters;
    ImageView btSearch;
    ImageView btMap;
    EditText etSearch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mGoogleAPI == null) {
            mGoogleAPI = new GoogleApiClient.Builder(getActivity()).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstance) {
        return inflater.inflate(R.layout.fragment_job_listings, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        setupListings();
        setUpButtonListeners();

        refreshListings();

       mGoogleAPI.connect();

    }

    void setUpButtonListeners() {
        btMap = (ImageView) getView().findViewById(R.id.BtMap);
        btMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<JobHolder> jobs = new ArrayList<>();

                for (int i = 0; i < mlistings.size(); i++) {
                    ParseGeoPoint geoPoint = mlistings.get(i).getParseGeoPoint("geopoint");
                    String ObjectId = mlistings.get(i).getObjectId();
                    String Name = mlistings.get(i).get("title").toString();

                    Log.d("ALL_JOBS", geoPoint.toString());
                    if (geoPoint != null) {
                        jobs.add(new JobHolder(ObjectId, Name, geoPoint.getLongitude(), geoPoint.getLatitude(), (String) mlistings.get(i).get("address"),
                                (int) mlistings.get(i).get("compensation"), (int) mlistings.get(i).get("duration")));
                    }
                }
                Bundle args = new Bundle();

                args.putParcelableArrayList("ALL_LISTINGS", jobs);

                MapDialogFragment mapDialogFragment = new MapDialogFragment();
                mapDialogFragment.setArguments(args);
                mapDialogFragment.show(getActivity().getFragmentManager(), "MapDialog");
            }


        });
        btFilters = (ImageView) getView().findViewById(R.id.BtFilters);
        btFilters.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                filtersDialog.show(getActivity().getFragmentManager(), "NoticeDialogFragment");
            }
        });
        btSearch = (ImageView) getView().findViewById(R.id.BtSearch);
        etSearch = (EditText) getView().findViewById(R.id.EtSearch);
        btSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ListingsFragment.filter.searchTerm = etSearch.getText().toString();
                refreshListings();
            }
        });
    }

    static void setFilter(Filter filter) {
        String searchTerm = filter.searchTerm;
        ListingsFragment.filter = filter;
        filter.searchTerm = searchTerm;
        refreshListings();
    }

    static Filter getFilter() {
        return ListingsFragment.filter;
    }

    void setupListings() {
        lvListings = (ListView) getView().findViewById(R.id.lvListings);
        mlistings = new ArrayList<>();

        lvListings.setTranscriptMode(1);
        listingAdapter = new ListingArrayAdapter(ParseProject.getContext(), mlistings);
        lvListings.setAdapter(listingAdapter);

        Log.i(TAG, "Listing Adapter Setup");

        lvListings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int i, long arg3) {
                ListingDetailDialog listingDetailDialog = new ListingDetailDialog();
                String listingId = mlistings.get(i).getObjectId();
                Bundle args = new Bundle();
                args.putString("listingId", listingId);
                listingDetailDialog.setArguments(args);
                listingDetailDialog.show(getActivity().getFragmentManager(), "NoticeDialogFragment");

//                Intent intent = new Intent(ParseProject.getContext(), ListingDetailActivity.class);
//                intent.putExtra("ObjectId", mlistings.get(i).getObjectId());
//                startActivity(intent);
            }
        });
    }

    static void refreshListings() {
        filter.latitude = MainActivity.location.getLatitude();
        filter.longitude = MainActivity.location.getLongitude();
        ListingManager.getListings(ListingsFragment.filter, new ListCB() {
            @Override
            public void done(String error, List<ParseObject> listings) {
                if (error == null) {
                    mlistings.clear();
                    mlistings.addAll(listings);
                    listingAdapter.notifyDataSetChanged();
                    Log.i(TAG, "Listing Adapter Data Set. Items found: " + listings.size());
                } else {
                    Toast.makeText(ParseProject.getContext(), error, Toast.LENGTH_SHORT).show();
                    Log.e("message", "Error Loading Listings: " + error);
                }
            }
        });

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleAPI.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onStop() {
        mGoogleAPI.disconnect();
        super.onStop();
    }



}
