package com.example.andrew.neighborlabour.PagerAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.andrew.neighborlabour.R;

/**
 * Created by andrew on 2/24/17.
 */

public class Jobs extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstance){
        return inflater.inflate(R.layout.jobs, container, false);


    }
}
