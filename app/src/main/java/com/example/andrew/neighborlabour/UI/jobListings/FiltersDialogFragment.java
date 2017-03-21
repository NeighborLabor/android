package com.example.andrew.neighborlabour.UI.jobListings;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrew.neighborlabour.MainActivity;
import com.example.andrew.neighborlabour.ParseProject;
import com.example.andrew.neighborlabour.R;
import com.example.andrew.neighborlabour.Services.listings.Filter;
import com.parse.Parse;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by chevalierc on 3/10/2017.
 */

public class FiltersDialogFragment extends DialogFragment {

    DatePickerDialog startDatePicker;
    int sYear = 0, sMonth = 0, sDay = 0;
    int eYear = 0, eMonth = 0, eDay = 0;

    EditText minCompensation;
    EditText maxCompensation;
    TextView startDate;
    EditText maxDistance;

    Button setFilter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_filters, null);

        setUpGUI(view);
        final Context context = getActivity();
        setFilter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!(maxDistance.getText().length() == 0 || maxDistance.getText().equals(""))) {
                    getFields();
                    getDialog().dismiss();
                }else {
                    Toast.makeText(getActivity(), "Please input a value for Max Distance", Toast.LENGTH_SHORT).show();
                }

            }
        });

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStartDatePicker();
            }
        });

        setFields(view);

        return view;
    }

    void setUpGUI(View view){
        minCompensation = (EditText) view.findViewById(R.id.minCompensation);
        maxCompensation = (EditText) view.findViewById(R.id.maxCompensation);
        setFilter = (Button ) view.findViewById(R.id.setFilter);
        startDate = (TextView) view.findViewById(R.id.startDate);
        maxDistance = (EditText) view.findViewById(R.id.maxDistance);
    }

    void showStartDatePicker(){
        startDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                sYear = year; sMonth = month; sDay = day;
                startDate.setText( sMonth+1 + "/" + sDay + "/" + sYear);
            }

        }, sYear, sMonth, sDay);
        startDatePicker.show();
    }

    void setFields(View v){
        Filter oldFilter = ListingsFragment.getFilter();

        if(oldFilter.minCompensation != 0){
            minCompensation.setText("" + oldFilter.minCompensation);
        }

        if(oldFilter.maxCompensation != 0){
            maxCompensation.setText("" + oldFilter.maxCompensation);
        }

        if( oldFilter.startDate != null){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(oldFilter.startDate);
            sYear = calendar.get(Calendar.YEAR);
            sMonth = calendar.get(Calendar.MONTH);
            sDay = calendar.get(Calendar.DAY_OF_MONTH);;
            startDate.setText( sMonth+1 + "/" + sDay + "/" + (sYear) );
        }
    }

    void getFields(){
        Filter newFilter = new Filter();

        String minCompensationText = minCompensation.getText().toString();
        if(minCompensationText.length() != 0){
            newFilter.minCompensation = Double.parseDouble( minCompensationText );
        }else{
            newFilter.minCompensation = 0;
        }

        String maxCompensationText = maxCompensation.getText().toString();
        if(maxCompensationText.length() != 0){
            newFilter.maxCompensation = Double.parseDouble( maxCompensationText );
        }else{
            newFilter.maxCompensation = 1000;
        }

        String distance = maxDistance.getText().toString();
        if(distance.length() != 0){
            newFilter.maxDistance = Integer.valueOf(distance);
        } else {
            newFilter.maxDistance = 10;
        }


        newFilter.startDate = new GregorianCalendar(sYear, sMonth, sDay).getTime();

        ListingsFragment.setFilter(newFilter);
    }
}
