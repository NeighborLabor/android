package com.example.andrew.neighborlabour.UI.active.Review;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.example.andrew.neighborlabour.R;
import com.example.andrew.neighborlabour.Services.user.UserManager;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by andrew on 4/2/17.
 */

public class ReviewDialog extends Activity{

    TextView review_title;

    static ParseUser reviewed_user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_review);

        review_title = (TextView) findViewById(R.id.review_name);

        String user_id = getIntent().getStringExtra("USER_ID");



    }
}
