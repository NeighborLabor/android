package com.example.andrew.neighborlabour.Services.user;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by chevalierc on 2/22/2017.
 */

@ParseClassName("Review")
public class Review extends ParseObject {
    public static final String RATING_KEY = "rating";
    public static final String DESCRIPTION_KEY = "descr";
    public static final String REVIEWER_KEY = "reviewer";
    public static final String LISTING_KEY = "listing";

    //RATING
    public int getRating(){
        return getInt(RATING_KEY);
    }

    public void setRateing(int rating){
        put(RATING_KEY, rating);
    }

    //DESCRIPTION
    public String getDescription(){
        return getString(DESCRIPTION_KEY);
    }

    public void setDescription(String description){
        put(DESCRIPTION_KEY, description);
    }

    //REVIEWER
    public ParseUser getReviewer(){
        return getParseUser(REVIEWER_KEY);
    }

    public void setReviewer(ParseUser reviewer){
        put(REVIEWER_KEY, reviewer);
    }

    //LISTING
    public ParseObject getListing(){
        return getParseObject(LISTING_KEY);
    }

    public void setListing(ParseObject listing){
        put(LISTING_KEY, listing);
    }
}
