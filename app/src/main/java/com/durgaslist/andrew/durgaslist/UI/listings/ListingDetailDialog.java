package com.durgaslist.andrew.durgaslist.UI.listings;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.durgaslist.andrew.durgaslist.ParseProject;
import com.andrew.durgaslist.R;
import com.durgaslist.andrew.durgaslist.Services.Utils.Conversions;
import com.durgaslist.andrew.durgaslist.Services.Utils.ListingCB;
import com.durgaslist.andrew.durgaslist.Services.Utils.SuccessCB;
import com.durgaslist.andrew.durgaslist.Services.listings.Listing;
import com.durgaslist.andrew.durgaslist.Services.listings.ListingManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chevalierc on 3/29/2017.
 */

public class ListingDetailDialog extends DialogFragment {
    final String TAG = "ListingActivity";

    View view;

    GoogleMap map;
    TextView tvTitle;
    TextView tvCompensation;
    TextView tvDuration;
    TextView tvEmployer;
    TextView tvDescription;
    TextView tvAddress;
    TextView tvDate;
    Button btApply;

    @Override
    public void onStart(){
        super.onStart();
        Dialog dialog = getDialog();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_listing_detail, container, true);

        String listingId = getArguments().getString("listingId");

        setUpGUI();
        getMap(savedInstanceState);
        setValues(listingId);
        setListeners(listingId);

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public void getMap(Bundle savedInstanceState){
        MapView mMapView = (MapView) view.findViewById(R.id.createJobMap);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                map = mMap;
            }
        });
    }

    public void setUpGUI() {
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvCompensation = (TextView) view.findViewById(R.id.tvCompensation);
        tvDuration = (TextView) view.findViewById(R.id.tvDuration);
        tvEmployer = (TextView) view.findViewById(R.id.tvEmployer);
        tvDescription = (TextView) view.findViewById(R.id.tvDescription);
        tvAddress = (TextView) view.findViewById(R.id.tvAddress);
        tvDate = (TextView) view.findViewById(R.id.tvDate);
        btApply = (Button) view.findViewById(R.id.btApply);
    }

    public void setValues(String listingId){
        ListingManager.getListing(listingId, new ListingCB() {
            @Override
            public void done(String error, Listing listing) {
                tvTitle.setText(listing.title);
                if(listing.employer != null) tvEmployer.setText("Employer " + listing.employer.getString("name"));
                tvDescription.setText(listing.description);
                tvCompensation.setText( "$" + listing.compensation );
                tvDuration.setText( Conversions.minutesToString(listing.duration) );
                tvAddress.setText(listing.address);
                tvDate.setText(formatDateAsString(listing));
                setMapLocation(listing);
                //hide message employer if your the meployer
                if(!listing.employer.getObjectId().equals(ParseUser.getCurrentUser().getObjectId() )){
                    btApply.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private String formatDateAsString(Listing listing) {
        Date date = listing.startTime;
        DateFormat df = new SimpleDateFormat("M/dd h:mm a");
        return df.format(date);
    }

    private void setMapLocation(Listing listing){
        LatLng jobLocation = new LatLng(listing.latitude, listing.longitude);
        map.addMarker(new MarkerOptions().position(jobLocation).title("JobLocation"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(jobLocation, 12));
    }

    public void setListeners(String curListingId){
        final String listingId = curListingId;
        btApply.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ListingManager.markInterest(listingId, new SuccessCB() {
                    @Override
                    public void done(String error, boolean success) {
                        String text;
                        if(error == null){
                            text = "Successfully applied to Job!";
                        }else{
                            text = error;
                        }
                        Toast toast = Toast.makeText(ParseProject.getContext(), text, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            }
        });
    }

}
