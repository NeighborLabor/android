package com.durgaslist.andrew.durgaslist.Services.Utils;

import com.durgaslist.andrew.durgaslist.Services.listings.Listing;

/**
 * Created by chevalierc on 2/25/2017.
 */

public interface ListingCB {
    void done(String error, Listing response);
}