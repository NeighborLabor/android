package com.example.andrew.neighborlabour.UI.active.Review;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.example.andrew.neighborlabour.R;
import com.example.andrew.neighborlabour.Services.Utils.ParseObjectCB;
import com.example.andrew.neighborlabour.Services.Utils.SuccessCB;
import com.example.andrew.neighborlabour.Services.Utils.UserCB;
import com.example.andrew.neighborlabour.Services.listings.Listing;
import com.example.andrew.neighborlabour.Services.listings.ListingManager;
import com.example.andrew.neighborlabour.Services.user.UserManager;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by andrew on 4/2/17.
 */

public class ReviewDialog extends Activity{

    TextView review_title;
    String user_id;

    static ParseUser reviewed_user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_review);

        review_title = (TextView) findViewById(R.id.review_name);

        user_id = getIntent().getStringExtra("USER_ID");

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
    }

    public void setText() {
        try {
            review_title.setText("Leave review for: " + reviewed_user.get("name"));
        } catch (Exception e) {
            Log.d("REVIEW_DIALOG", e.toString());
        }
    }

    public void sendReview(){
        String body = "";
        int stars = 5;
        UserManager.writeReview(user_id, stars, body, new SuccessCB() {
            @Override
            public void done(String error, boolean success) {

            }
        });
    }



}
