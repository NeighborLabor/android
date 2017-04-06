package com.example.andrew.neighborlabour.UI.active.Review;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrew.neighborlabour.R;
import com.example.andrew.neighborlabour.Services.Utils.ListingCB;
import com.example.andrew.neighborlabour.Services.Utils.ParseObjectCB;
import com.example.andrew.neighborlabour.Services.Utils.SuccessCB;
import com.example.andrew.neighborlabour.Services.Utils.UserCB;
import com.example.andrew.neighborlabour.Services.listings.Listing;
import com.example.andrew.neighborlabour.Services.listings.ListingManager;
import com.example.andrew.neighborlabour.Services.user.Reviews.Review;
import com.example.andrew.neighborlabour.Services.user.Reviews.ReviewManager;
import com.example.andrew.neighborlabour.Services.user.UserManager;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by andrew on 4/2/17.
 */

public class ReviewDialog extends Activity{

    TextView review_title;

    TextView text_review;

    Button submit_button;

    RatingBar ratingBar;

    static Listing listing;

    static ParseUser reviewed_user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_review);

        review_title = (TextView) findViewById(R.id.review_name);

        submit_button = (Button) findViewById(R.id.submit_rating);

        text_review = (TextView) findViewById(R.id.text_review);

        ratingBar = (RatingBar) findViewById(R.id.rating);

        submit_button.setOnClickListener(submit_review);


        String user_id = getIntent().getStringExtra("USER_ID");

        UserManager.getUser(user_id, new ParseObjectCB() {
            @Override
            public void done(String error, ParseObject parseUser) {
                if (error == null) {
                    reviewed_user = (ParseUser) parseUser;
                    setText();
                } else {
                    Log.d("USER_CB", error);
                }
            }
        });

        final String listing_id = getIntent().getStringExtra("Listing_ID");



        ListingManager.getListing(listing_id, new ListingCB() {
            @Override
            public void done(String error, Listing response) {
                if(error == null && response != null){
                    listing = response;
                } else {
                    Log.d("LISTING_CB", error);
                }
            }
        });
    }

        public void setText() {
        try {
            review_title.setText("Leave review for: " + reviewed_user.get("name"));
        } catch (Exception e) {
            Log.d("REVIEW_DIALOG", e.toString());
         }
        }

        public View.OnClickListener submit_review = new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Review review = new Review();

                review.textReview = text_review.getText().toString();

                review.rating = ratingBar.getNumStars();



                ReviewManager.PostReview(review, reviewed_user, new SuccessCB() {
                    @Override
                    public void done(String error, boolean success) {
                        if(success && error == null){
                            Log.d("ReviewDialog", "Success");
                            endDialog();

                        }else {
                            Log.d("ReviewDialog", error);
                            Toast.makeText(getBaseContext(), "Unable to leave Review", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        };

    public void endDialog(){
        this.finish();
    }
}
