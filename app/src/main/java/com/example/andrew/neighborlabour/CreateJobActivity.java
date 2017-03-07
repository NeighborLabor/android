package com.example.andrew.neighborlabour;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrew.neighborlabour.Services.Utils.SuccessCB;
import com.example.andrew.neighborlabour.Services.listings.Listing;
import com.example.andrew.neighborlabour.Services.listings.ListingManager;
import com.example.andrew.neighborlabour.Services.user.AuthManager;
import com.example.andrew.neighborlabour.Services.user.User;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateJobActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "CreateJob";

    TextView etTitle;
    TextView etDescription;
    TextView etAddress;
    TextView etCompensation;
    TextView etDuration;
    TextView etStartTime;

    Button Btcreate;
    Button btBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_job);

        etTitle = (TextView) findViewById(R.id.etTitle);
        etDescription = (TextView) findViewById(R.id.etDescription);
        etAddress = (TextView) findViewById(R.id.etAddress);
        etCompensation = (TextView) findViewById(R.id.etCompensation);
        etDuration = (TextView) findViewById(R.id.etDuration);

        Button btCreate = (Button)findViewById(R.id.btCreate);
        Button btBack = (Button)findViewById(R.id.btBack);

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }

        });

        btCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Listing newListing = new Listing();

                newListing.title = etTitle.getText().toString();
                newListing.description = etDescription.getText().toString();
                newListing.address = etAddress.getText().toString();
                newListing.compensation = parseCompensation(etCompensation);
                newListing.duration = parseDuration(etCompensation);
                newListing.startTime = new Date();
                ListingManager.createListing(newListing, new SuccessCB() {
                    @Override
                    public void done(String error, boolean success) {
                        if(error == null){
                            finish();
                        }else{
                            Toast.makeText(ParseProject.getContext(), error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });

    }

    public Date parseDate(TextView etStartTime){
        String sStartTime = etStartTime.getText().toString();
        DateFormat format = new SimpleDateFormat("M/dd/ K:mm a", Locale.ENGLISH);
        try {
            Date date = format.parse(sStartTime);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public Integer parseDuration(TextView etCompensation) {
        String stringDuration = etCompensation.getText().toString();
        if(stringDuration.contains(":")){
            String hours = stringDuration.split(":",2)[0];
            String minutes = stringDuration.split(":",2)[1];
            Double dHours =  Double.parseDouble(hours);
            Double dMinutes =  Double.parseDouble(minutes);
            int iMinutes = (int) Math.floor( (dHours * 60) + dMinutes);
        }else if(stringDuration.contains(".")){
            Double hours = Double.parseDouble(stringDuration);
            return (int) Math.floor(hours * 60);
        }else{
            Double hours = Double.parseDouble(stringDuration);
            return (int) Math.floor(hours * 60);
        }
        return 0;
    }

    public Double parseCompensation(TextView etCompensation) {
        String stringCompensation = etCompensation.getText().toString();
        return Double.parseDouble(stringCompensation);
    }


}
