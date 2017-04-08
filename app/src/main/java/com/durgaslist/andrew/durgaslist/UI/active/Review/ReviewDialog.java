package com.durgaslist.andrew.durgaslist.UI.active.Review;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.durgaslist.andrew.durgaslist.ParseProject;
import com.andrew.durgaslist.R;
import com.durgaslist.andrew.durgaslist.Services.Utils.ParseObjectCB;
import com.durgaslist.andrew.durgaslist.Services.Utils.SuccessCB;
import com.durgaslist.andrew.durgaslist.Services.listings.Listing;
import com.durgaslist.andrew.durgaslist.Services.user.ReviewManager;
import com.durgaslist.andrew.durgaslist.Services.user.UserManager;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by andrew on 4/2/17.
 */

public class ReviewDialog extends DialogFragment{

    final String TAG = this.getClass().getSimpleName().toString();

    TextView review_title;
    TextView text_review;
    Button submit_button;
    RatingBar ratingBar;
    static Listing listing;
    static ParseUser reviewed_user;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_review, container, true);

        final String user_id = getArguments().getString("USER_ID");
        final String listingId = getArguments().getString("LISTING_ID");

        review_title = (TextView) view.findViewById(R.id.review_name);
        submit_button = (Button) view.findViewById(R.id.submit_rating);
        text_review = (TextView) view.findViewById(R.id.text_review);
        ratingBar = (RatingBar) view.findViewById(R.id.rating);
        submit_button.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String textReview = text_review.getText().toString();
                int rating = ratingBar.getNumStars();

                ReviewManager.PostReview(listingId, textReview, rating, new SuccessCB() {
                    @Override
                    public void done(String error, boolean success) {
                        if(success && error == null){
                            Toast.makeText(ParseProject.getContext(), "Review Left!", Toast.LENGTH_SHORT).show();
                            endDialog();
                        }else {
                            Toast.makeText(ParseProject.getContext(), error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


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

        return view;
    }

        public void setText() {
            try {
                review_title.setText("Leave review for: " + reviewed_user.get("name"));
            } catch (Exception e) {
                Log.d("REVIEW_DIALOG", e.toString());
            }
        }

    public void endDialog(){
        dismiss();
    }
}
