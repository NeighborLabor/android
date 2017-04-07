package com.example.andrew.neighborlabour.UI.auth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrew.neighborlabour.ParseProject;
import com.example.andrew.neighborlabour.R;
import com.example.andrew.neighborlabour.Services.Utils.ListCB;
import com.example.andrew.neighborlabour.Services.Utils.ParseObjectCB;
import com.example.andrew.neighborlabour.Services.Utils.UserCB;
import com.example.andrew.neighborlabour.Services.user.ReviewManager;
import com.example.andrew.neighborlabour.Services.user.UserManager;
import com.example.andrew.neighborlabour.UI.listings.ListingArrayAdapter;
import com.example.andrew.neighborlabour.UI.listings.ListingDetailDialog;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chevalierc on 3/7/2017.
 */

public class ProfileActivity extends AppCompatActivity {

    public final static String TAG = "PROFILE_ACTIVITY";

    TextView name;
    TextView phone;
    TextView bio;
    ListView lvReviews;
    String userId;
    Boolean showPhone = false;

    ReviewArrayAdapter reviewsAdapter;
    ArrayList<ParseObject> reviews;

    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userId = getIntent().getStringExtra("userId");
        Log.i(TAG, userId);
        showPhone = getIntent().getBooleanExtra("showPhone", false);

        getGuiRefs();
        setUpReviews();
        setFields();
    }

    void getGuiRefs(){
        name = (TextView) findViewById(R.id.tvName);
        phone = (TextView) findViewById(R.id.tvPhone);
        bio = (TextView) findViewById(R.id.tvBio);

        back = (ImageView) findViewById(R.id.btBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    void setUpReviews() {
        lvReviews = (ListView) findViewById(R.id.lvReviews);
        reviews = new ArrayList<>();

        lvReviews.setTranscriptMode(1);
        reviewsAdapter = new ReviewArrayAdapter(ParseProject.getContext(), reviews);
        lvReviews.setAdapter(reviewsAdapter);

        ReviewManager.getReviews(userId, new ListCB() {
            @Override
            public void done(String error, List<ParseObject> newReviews) {
                if(newReviews == null || error != null ){
                    Toast.makeText(ParseProject.getContext(), error, Toast.LENGTH_SHORT).show();
                }else{
                    Log.i(TAG, newReviews.size() + "");
                    reviews.clear();
                    reviews.addAll(newReviews);
                    reviewsAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void setFields(){
        UserManager.getUser(userId, new ParseObjectCB() {
            @Override
            public void done(String error, ParseObject user) {
                name.setText(user.getString("name"));
                phone.setText(user.getString("phone"));
                bio.setText(user.getString("bio"));
                if( showPhone ) phone.setVisibility(View.VISIBLE);
            }
        });
    }

}
