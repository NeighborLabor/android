package com.example.andrew.neighborlabour.Services.user.Reviews;

import android.util.Log;

import com.example.andrew.neighborlabour.Services.Utils.SuccessCB;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

/**
 * Created by andrew on 4/4/17.
 */

public class ReviewManager {



    public static void PostReview(Review review, final ParseUser reviewee,final SuccessCB cb) {
        final ParseObject new_review = new ParseObject("Review");


        new_review.put("rating", review.rating);

        new_review.put("descr", review.textReview);


        new_review.getRelation("user").add(reviewee);



        new_review.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d("ReviewManager", e.toString());
                    cb.done(null, false);
                } else {
                    Log.d("ReviewManager", "success");

                }
            }
        });

    }



}
