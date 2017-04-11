package com.durgaslist.andrew.durgaslist.Services.listings;

import android.location.Location;

import com.durgaslist.andrew.durgaslist.UI.MainActivity;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by chevalierc on 2/23/2017.
 */



public class Filter {
    public String searchTerm = null;
    public double minCompensation = 0;
    public double maxCompensation = 0;
    public Date startDate = getToday();
    public Date endDate = null;
    public String category = null;
    public Integer maxDistance = 5;
    public double longitude = -71.059770;
    public double latitude = 42.358430;

    void Filter(){
        Location userLocation = MainActivity.location;
        if (userLocation != null){
            longitude = userLocation.getLongitude();
            latitude = userLocation.getLatitude();
        }
    }

    Date getToday(){
        final Calendar c = Calendar.getInstance();
        return c.getTime();
    }
}
