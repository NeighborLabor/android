package com.example.andrew.neighborlabour.Utils;

import com.example.andrew.neighborlabour.listings.Listing;

/**
 * Created by chevalierc on 2/25/2017.
 */

public interface ListingCB {
    void done(String error, Listing response);
}