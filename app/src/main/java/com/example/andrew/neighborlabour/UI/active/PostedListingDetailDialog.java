package com.example.andrew.neighborlabour.UI.active;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrew.neighborlabour.ParseProject;
import com.example.andrew.neighborlabour.R;
import com.example.andrew.neighborlabour.Services.Utils.Conversions;
import com.example.andrew.neighborlabour.Services.Utils.ListingCB;
import com.example.andrew.neighborlabour.Services.Utils.StringCB;
import com.example.andrew.neighborlabour.Services.chat.ChatManager;
import com.example.andrew.neighborlabour.Services.listings.Listing;
import com.example.andrew.neighborlabour.Services.listings.ListingManager;
import com.example.andrew.neighborlabour.UI.active.Review.ReviewDialog;
import com.example.andrew.neighborlabour.UI.active.applicants.ApplicantArrayAdapter;
import com.example.andrew.neighborlabour.UI.chat.ChatDialogFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by chevalierc on 3/29/2017.
 */

 public class PostedListingDetailDialog extends DialogFragment {
    final String TAG = "ListingActivity";

    View view;

    LinearLayout applicantsLayout;
    LinearLayout workerLayout;

    GoogleMap map;
    TextView tvTitle;
    TextView tvCompensation;
    TextView tvDuration;
    TextView tvEmployer;
    TextView tvDescription;
    TextView tvAddress;
    TextView tvDate;
    ListView workerList;

    Button btMessageWorker;
    Button btReview;
    ApplicantArrayAdapter applicantArrayAdapter;
    ArrayList<ParseObject> applicants;

    TextView tvWorker;

    public static String listingId;

    @Override
    public void onStart(){
        super.onStart();
        Dialog dialog = getDialog();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_posted_listing_detail, container, true);

        listingId = getArguments().getString("listingId");

        setUpGUI();
        setUpWorkerList();
        getMap(savedInstanceState);
        setValues(listingId);

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
        btMessageWorker = (Button) view.findViewById(R.id.btMessage);
        btReview = (Button) view.findViewById(R.id.btReview);
        workerList = (ListView) view.findViewById(R.id.select_worker_list);
        applicantsLayout = (LinearLayout) view.findViewById(R.id.applicants);
        workerLayout = (LinearLayout) view.findViewById(R.id.worker);
        tvWorker = (TextView) view.findViewById(R.id.tvWorker);
    }

    public void setValues(final String listingId){
        ListingManager.getListing(listingId, new ListingCB() {
            @Override
            public void done(String error, Listing theListing) {
                final Listing listing = theListing;
                tvTitle.setText(listing.title);
                if(listing.employer != null) tvEmployer.setText("Employer " + listing.employer.getString("name"));
                tvDescription.setText(listing.description);
                tvCompensation.setText( "$" + listing.compensation );
                tvDuration.setText( Conversions.minutesToString(listing.duration) );
                tvAddress.setText(listing.address);
                tvDate.setText(formatDateAsString(listing));
                setMapLocation(listing);
                if(listing.worker != null){

                    workerLayout.setVisibility(View.VISIBLE);
                    tvWorker.setText( "Worker: " + listing.worker.getString("name"));
                    btMessageWorker.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ChatManager.getOrCreateChatThread(listing.worker.getObjectId(), new StringCB() {
                                @Override
                                public void done(String error, String threadId) {
                                    if(error == null){
                                        Toast.makeText(ParseProject.getContext(), error, Toast.LENGTH_SHORT).show();
                                    }else{
                                        //TODO: open message dialog
                                        ChatDialogFragment chatDialog = new ChatDialogFragment();
                                        Bundle args = new Bundle();
                                        args.putString("threadId", threadId );
                                        chatDialog.setArguments(args);
                                        chatDialog.show(getFragmentManager(), "NoticeDialogFragment");
                                    }
                                }
                            });
                        }
                    });

                    btReview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ReviewDialog reviewDialog = new ReviewDialog();
                            String userId = listing.worker.getObjectId();
                            Bundle args = new Bundle();
                            args.putString("USER_ID", userId);
                            args.putString("LISTING_ID", listingId);
                            reviewDialog.setArguments(args);
                            reviewDialog.show(getActivity().getFragmentManager(), "NoticeDialogFragment");
                        }
                    });

                }else{
                    applicantsLayout.setVisibility(View.VISIBLE);
                    applicants.addAll(listing.applicants);
                    applicantArrayAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void setUpWorkerList(){
        applicants = new ArrayList<>();
        applicantArrayAdapter = new ApplicantArrayAdapter(ParseProject.getContext(), applicants, PostedListingDetailDialog.this);
        workerList.setAdapter(applicantArrayAdapter);
        applicantArrayAdapter.notifyDataSetChanged();
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

}
