package com.example.andrew.neighborlabour.UI.jobListings;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.andrew.neighborlabour.R;
import com.example.andrew.neighborlabour.Services.listings.Filter;

/**
 * Created by chevalierc on 3/10/2017.
 */

public class FiltersDialogFragment extends DialogFragment {

    void setFields(View v){
        Filter oldFilter = ListingsFragment.getFilter();

        EditText minCompensation = (EditText) v.findViewById(R.id.minCompensation);
        minCompensation.setText("" + oldFilter.minCompensation);

        EditText maxCompensation = (EditText) v.findViewById(R.id.maxCompensation);
        minCompensation.setText("" + oldFilter.maxCompensation);
    }


    void getFields(){
        Filter newFilter = new Filter();

        EditText minCompensation = (EditText) getDialog().findViewById(R.id.minCompensation);
        newFilter.minCompensation = Double.parseDouble( minCompensation.getText().toString() );

        EditText maxCompensation = (EditText) getDialog().findViewById(R.id.maxCompensation);
        newFilter.maxCompensation = Double.parseDouble( maxCompensation.getText().toString() );

        ListingsFragment.setFilter(newFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.dialog_filters, null);

        Button button = (Button)v.findViewById(R.id.setFilter);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getFields();
                getDialog().dismiss();
            }
        });

        setFields(v);

        return v;
    }

}
