package com.example.andrew.neighborlabour.UI.PagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.andrew.neighborlabour.UI.active.ActiveJobsFragment;
import com.example.andrew.neighborlabour.UI.chat.ChatFragment;
import com.example.andrew.neighborlabour.UI.listings.ListingsFragment;

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
            default:
            case 0:
                ListingsFragment.refreshListings();
                return new ListingsFragment();
            case 1:
                return new ActiveJobsFragment();
            case 2:
                return new ChatFragment();
        }

    }

    //set the title of the Tabs
    @Override
    public CharSequence getPageTitle(int position){
        switch(position){
            case 0:
            default:
                return "Job Listings";
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


