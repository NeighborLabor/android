package com.durgaslist.andrew.durgaslist.UI.auth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.durgaslist.andrew.durgaslist.ParseProject;
import com.andrew.durgaslist.R;
import com.durgaslist.andrew.durgaslist.Services.Utils.ListCB;
import com.durgaslist.andrew.durgaslist.Services.Utils.ParseObjectCB;
import com.durgaslist.andrew.durgaslist.Services.user.ReviewManager;
import com.durgaslist.andrew.durgaslist.Services.user.UserManager;
import com.parse.ParseObject;

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
    TextView reviewsHeader;
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
        reviewsHeader = (TextView) findViewById(R.id.tvReviewHeader);

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
                    //Toast.makeText(ParseProject.getContext(), error, Toast.LENGTH_SHORT).show();
                    reviewsHeader.setText("No Reviews For User");
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
                String bioText = user.getString("bio");
                if(bioText.length() == 0){
                    bio.setText("No Bio");
                }else{
                    bio.setText(bioText);
                }
                if( showPhone ) phone.setVisibility(View.VISIBLE);
            }
        });
    }

}
