package com.durgaslist.andrew.durgaslist.UI.listings;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.durgaslist.andrew.durgaslist.ParseProject;
import com.andrew.durgaslist.R;
import com.durgaslist.andrew.durgaslist.Services.listings.Filter;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by chevalierc on 3/10/2017.
 */

public class FiltersDialogFragment extends DialogFragment {
    final int MAX_DISTANCE = 50;
    final int MAX_COMPENSATION = 500;

    DatePickerDialog startDatePicker;
    int sYear = 0, sMonth = 0, sDay = 0;

    TextView tvMinCompensation;
    TextView tvMaxCompensation;
    TextView startDate;
    TextView tvMaxDistance;

    int minCompensation;
    int maxDistance;
    int maxCompensation;

    Button setFilter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_filters, container, true);

        setUpGUI(view);
        setListeners();
        setFieldsFromFilter(view);

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    void setListeners(){
        setFilter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createFilterFromFields();
                getDialog().dismiss();
            }
        });
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStartDatePicker();
            }
        });
        tvMaxDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDistancePicker();
            }
        });
        tvMaxCompensation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMaxCompensationPicker();
            }
        });
        tvMinCompensation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMinCompensationPicker();
            }
        });
    }

    void setUpGUI(View view){
        tvMinCompensation = (TextView) view.findViewById(R.id.tvMinCompensation);
        tvMaxCompensation = (TextView) view.findViewById(R.id.tvMaxCompensation);
        setFilter = (Button ) view.findViewById(R.id.setFilter);
        startDate = (TextView) view.findViewById(R.id.startDate);
        tvMaxDistance = (TextView) view.findViewById(R.id.tvMaxDistance);
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

    void showMinCompensationPicker(){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(ParseProject.getContext().LAYOUT_INFLATER_SERVICE);
        View npView = inflater.inflate(R.layout.dialog_number_picker, null);
        final NumberPicker np = (NumberPicker) npView.findViewById(R.id.numberPicker);
        np.setMaxValue(MAX_COMPENSATION);
        np.setMinValue(0);
        String[] values = new String[MAX_COMPENSATION];
        for(int i=0; i<values.length; i++){
            values[i] = "$" + i;
        }
        np.setWrapSelectorWheel(false);
        np.setDisplayedValues(values);
        new AlertDialog.Builder(getActivity())
                .setTitle("Min Compensation:")
                .setView(npView)
                .setPositiveButton("Set",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                minCompensation = np.getValue();
                                tvMinCompensation.setText("$" + minCompensation);
                            }
                        })
                .show();
    }

    void showMaxCompensationPicker(){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(ParseProject.getContext().LAYOUT_INFLATER_SERVICE);
        View npView = inflater.inflate(R.layout.dialog_number_picker, null);
        final NumberPicker np = (NumberPicker) npView.findViewById(R.id.numberPicker);
        np.setMaxValue(MAX_COMPENSATION);
        np.setMinValue(0);
        String[] values = new String[MAX_COMPENSATION];
        for(int i=0; i<values.length; i++){
            values[i] = "$" + i;
        }
        np.setWrapSelectorWheel(false);
        np.setDisplayedValues(values);
        new AlertDialog.Builder(getActivity())
                .setTitle("Max Compensation:")
                .setView(npView)
                .setPositiveButton("Set",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                maxCompensation = np.getValue();
                                tvMaxCompensation.setText("$" + maxCompensation);
                            }
                        })
                .show();
    }

    void showDistancePicker(){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(ParseProject.getContext().LAYOUT_INFLATER_SERVICE);
        View npView = inflater.inflate(R.layout.dialog_number_picker, null);
        final NumberPicker np = (NumberPicker) npView.findViewById(R.id.numberPicker);
        np.setMaxValue(MAX_DISTANCE);
        np.setMinValue(0);
        String[] values = new String[MAX_DISTANCE];
        for(int i=0; i<values.length; i++){
            values[i] = i + " miles";
        }
        np.setWrapSelectorWheel(false);
        np.setDisplayedValues(values);
        new AlertDialog.Builder(getActivity())
                .setTitle("Max Distance:")
                .setView(npView)
                .setPositiveButton("Set",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Log.e("Value", np.getValue() + "");
                                maxDistance = np.getValue();
                                tvMaxDistance.setText(maxDistance + " miles");
                            }
                        })
                .show();
    }

    void setFieldsFromFilter(View v){
        Filter oldFilter = ListingsFragment.getFilter();

        tvMinCompensation.setText("$" + oldFilter.minCompensation);
        minCompensation = (int) oldFilter.minCompensation;

        tvMaxCompensation.setText("$" + oldFilter.maxCompensation);
        maxCompensation = (int) oldFilter.maxCompensation;

        tvMaxDistance.setText(oldFilter.maxDistance + " miles");
        maxDistance = oldFilter.maxDistance;

        if( oldFilter.startDate != null){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(oldFilter.startDate);
            sYear = calendar.get(Calendar.YEAR);
            sMonth = calendar.get(Calendar.MONTH);
            sDay = calendar.get(Calendar.DAY_OF_MONTH);;
            startDate.setText( sMonth+1 + "/" + sDay + "/" + (sYear) );
        }
    }

    void createFilterFromFields(){
        Filter newFilter = new Filter();

        newFilter.minCompensation = (double) minCompensation;
        newFilter.maxCompensation = (double) maxCompensation;

        newFilter.maxDistance = maxDistance;
        newFilter.startDate = new GregorianCalendar(sYear, sMonth, sDay).getTime();

        ListingsFragment.setFilter(newFilter);
    }
}
