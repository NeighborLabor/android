package com.example.andrew.neighborlabour.UI.jobListings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrew.neighborlabour.ParseProject;
import com.example.andrew.neighborlabour.R;
import com.example.andrew.neighborlabour.Services.Utils.ListingCB;
import com.example.andrew.neighborlabour.Services.Utils.SuccessCB;
import com.example.andrew.neighborlabour.Services.listings.Listing;
import com.example.andrew.neighborlabour.Services.listings.ListingManager;

/**
 * Created by chevalierc on 2/26/2017.
 */

public class ListingDetailActivity extends AppCompatActivity {
    final String TAG = "ListingActivity";
    TextView title;
    TextView tvCompensationDuration;
    TextView tvEmployer;
    TextView tvDescription;
    TextView tvAddress;
    Button btApply;
    Button btBack;

    public String getCompensationDuration(Listing listing){
        String compensationDuration = "$" + listing.compensation + " / ";
        int duration = listing.duration;
        if(duration > 59){
            int hours = ((duration+1) % 60);
            int minutes = duration - (hours * 60);
            compensationDuration +=  hours;
            if(minutes != 0) compensationDuration = "."  + Math.round(minutes/60 * 10) / 10 ;
            compensationDuration += hours == 1 ? " hr" : " hrs";
        }else{
            compensationDuration += duration + " mins";
        }
        return compensationDuration;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_listing);

        final String listingId = intent.getStringExtra("ObjectId");

        //get references to gui elements
        title = (TextView)findViewById(R.id.tvTitle);
        tvCompensationDuration = (TextView)findViewById(R.id.tvCompensationDuration);
        tvEmployer = (TextView)findViewById(R.id.tvEmployer);
        tvDescription = (TextView)findViewById(R.id.tvDescription);
        tvAddress = (TextView)findViewById(R.id.tvAddress);
        btApply = (Button)findViewById(R.id.btApply);
        btBack = (Button)findViewById(R.id.btBack);

        //get info
        ListingManager.getListing(listingId, new ListingCB() {
            @Override
            public void done(String error, Listing listing) {
                title.setText(listing.title);
                tvEmployer.setText(listing.employer.getString("name"));
                tvDescription.setText(listing.description);
                tvCompensationDuration.setText(getCompensationDuration(listing));
                tvAddress.setText(listing.address);
            }
        });

        btBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

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
