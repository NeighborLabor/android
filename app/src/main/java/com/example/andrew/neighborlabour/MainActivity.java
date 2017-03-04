package com.example.andrew.neighborlabour;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.andrew.neighborlabour.UI.PagerAdapter.SectionPagerAdapter;
import com.parse.ParseUser;

public class MainActivity extends Fragment {

    private static final String TAG = "MainActivity";

    private ViewPager viewPager;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View x = inflater.inflate(R.layout.activity_main, null);
        Log.e(TAG, "Main Activity Created");



        viewPager = (ViewPager) x.findViewById(R.id.pager);

        SectionPagerAdapter pagerAdapter = new SectionPagerAdapter(getChildFragmentManager());


        viewPager.setAdapter(pagerAdapter);




        return x;
    }

    public void changeTab(int position){
        viewPager.setCurrentItem(position);
    }









}
