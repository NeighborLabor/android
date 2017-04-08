package com.durgaslist.andrew.durgaslist.UI.listings.Maps;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.durgaslist.andrew.durgaslist.UI.MainActivity;
import com.andrew.durgaslist.R;
import com.durgaslist.andrew.durgaslist.Services.Utils.Conversions;
import com.durgaslist.andrew.durgaslist.Services.listings.Listing;
import com.durgaslist.andrew.durgaslist.UI.listings.ListingDetailDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.logging.Filter;

/**
 * Created by andrew on 3/12/17.
 */

public class MapDialogFragment  extends DialogFragment implements GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback{
    double currentLongitude = 0;
    double currentLatitude = 0;

    static Listing theJob;

    private MapView mMapView;

    private Filter filter;

    ArrayList<JobHolder> jobs;

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    public void onStart() {
        mMapView.onStart();
        super.onStart();
    }

    public void onResume(){

        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        Bundle args = getArguments();

        jobs = args.getParcelableArrayList("ALL_LISTINGS");

        if(MainActivity.location != null){
            currentLatitude = MainActivity.location.getLatitude();
            currentLongitude = MainActivity.location.getLongitude();
        }


        Log.d("MAP_LISTING_SIZE", String.valueOf(jobs.size()));

        super.onCreate(savedInstanceState);


    }




    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_map, null);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView = (MapView) v.findViewById(R.id.map);
        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync((OnMapReadyCallback) this);

        return v;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.setInfoWindowAdapter(new InfoWindowAdapter());

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                ListingDetailDialog listingDetailDialog = new ListingDetailDialog();

                Bundle args = new Bundle();

                JobHolder jobHolder = jobs.get(Integer.valueOf(marker.getSnippet()));
                args.putString("listingId", jobHolder.ObjectId);
                listingDetailDialog.setArguments(args);
                listingDetailDialog.show(getActivity().getFragmentManager(), "NoticeDialogFragment");
            }
        });


        //the marker stores the position of which place this specific job is in the Array
        for(int i = 0; i < jobs.size(); i++){
            JobHolder aJob = jobs.get(i);
            googleMap.addMarker(new MarkerOptions().position(new LatLng(aJob.latitude, aJob.longitude)).title(aJob.Name).snippet(String.valueOf(i)));
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLatitude, currentLongitude) ,10));
    }

    @Override
    public void onPause() {
       mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        mMapView.onStop();
        super.onStop();

    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        ListingDetailDialog listingDetailDialog = new ListingDetailDialog();

        Bundle args = new Bundle();

        JobHolder jobHolder = jobs.get(Integer.valueOf(marker.getSnippet()));
        args.putString("listingId", jobHolder.ObjectId);
        listingDetailDialog.setArguments(args);
        listingDetailDialog.show(getActivity().getFragmentManager(), "NoticeDialogFragment");
    }




    public class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private View InfoWidow;

        public InfoWindowAdapter(){
            InfoWidow = getActivity().getLayoutInflater().inflate(R.layout.map_info_window_view, null);
        }


        @Override
        public View getInfoWindow(Marker marker) {


            return render(marker, InfoWidow);
        }

        private View render(Marker marker, View InfoWindow){
            String ObjectPostion = marker.getSnippet();

            JobHolder jobHolder = new JobHolder();

            try {

                jobHolder = jobs.get(Integer.valueOf(ObjectPostion));
            } catch(Exception e){
                //if the array is out of bounds it will display unable to get job

                TextView job_name = (TextView) InfoWindow.findViewById(R.id.Name_of_job);
                job_name.setText("Unable to find job");
                TextView address = (TextView) InfoWindow.findViewById(R.id.address);
                address.setText("");
                TextView compensation = (TextView) InfoWindow.findViewById(R.id.compensation);
                compensation.setText("");
                TextView duration = (TextView) InfoWindow.findViewById(R.id.duration);
                duration.setText("");

                return InfoWindow;

            }

            try {
                TextView job_name = (TextView) InfoWindow.findViewById(R.id.Name_of_job);
                job_name.setText(jobHolder.Name);
                TextView address = (TextView) InfoWindow.findViewById(R.id.address);
                address.setText(jobHolder.address);
                TextView compensation = (TextView) InfoWindow.findViewById(R.id.compensation);
                compensation.setText("$"+Integer.toString(jobHolder.compensation));
                TextView duration = (TextView) InfoWindow.findViewById(R.id.duration);
                duration.setText(Conversions.minutesToString(jobHolder.duration));
            } catch (Exception e){

            }


            return InfoWidow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return InfoWidow;
        }
    }

}
