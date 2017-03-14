package com.example.andrew.neighborlabour.Services.listings;

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
    public Integer maxDistance = 0;
    public double longitude = 0;
    public double latitude = 0;

    Date getToday(){
        final Calendar c = Calendar.getInstance();
        return c.getTime();
    }
}
