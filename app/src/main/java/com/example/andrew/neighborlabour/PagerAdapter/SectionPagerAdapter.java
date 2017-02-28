package com.example.andrew.neighborlabour.PagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.andrew.neighborlabour.jobListings.ListingsActivity;

/**
 * Created by andrew on 2/24/17.
 */

public class SectionPagerAdapter extends FragmentPagerAdapter {

    public SectionPagerAdapter (FragmentManager fm){
        super(fm);
    }
    //Sets the Fragment
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
            default:
                return new ListingsActivity();
            case 1:
                return new Active_Jobs();
            case 2:
                return new Messages();

        }

    }

    //set the title of the Tabs
    @Override
    public CharSequence getPageTitle(int position){
        switch(position){
            case 0:
            default:
                return "Jobs";
            case 1:
                return "Active Jobs";
            case 2:
                return "Messages";
        }
    }


    @Override
    public int getCount() {
        return 3;
    }


}


