package com.durgaslist.andrew.durgaslist.UI;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.durgaslist.andrew.durgaslist.ParseProject;
import com.andrew.durgaslist.R;
import com.durgaslist.andrew.durgaslist.Services.Utils.Conversions;
import com.durgaslist.andrew.durgaslist.Services.Utils.SuccessCB;
import com.durgaslist.andrew.durgaslist.Services.listings.Listing;
import com.durgaslist.andrew.durgaslist.Services.listings.ListingManager;
import com.durgaslist.andrew.durgaslist.UI.listings.ListingsFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CreateJobDialog extends DialogFragment{

    public final static String EXTRA_MESSAGE = "CreateJob";
    private final String TAG = "Create Job Dialog";

    final int MAX_DURATION = 4 * 8; //in 15 minute intervals
    final int MAX_COMPENSATION = 100;

    View view;
    SupportMapFragment mapFragment;

    DatePickerDialog datePicker;
    TimePickerDialog timePicker;

    TextView etTitle;
    TextView etDescription;
    TextView etAddress;
    TextView tvCompensation;
    TextView tvDuration;
    TextView tvStartDate;
    TextView tvStartTime;

    GoogleMap map;

    Button btCreate;

    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int hour = 12;
    int minute = 0;

    int duration = 15;
    int compensation = 10;

    @Override
    public void onStart(){
        super.onStart();
        Dialog dialog = getDialog();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_create_job, container);

        getMap(savedInstanceState);

        setUpGui(view);
        initTextValues();
        setButtonListeners();

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public void getMap(Bundle savedInstanceState){
        MapView mMapView = (MapView) view.findViewById(R.id.createJobMap);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                map = mMap;
            }
        });
    }

    void setUpGui(View view){
        etTitle = (TextView)  view.findViewById(R.id.etTitle);
        etDescription = (TextView)  view.findViewById(R.id.etDescription);
        etAddress = (TextView)  view.findViewById(R.id.etAddress);
        tvCompensation = (TextView)  view.findViewById(R.id.tvCompensation);
        tvDuration = (TextView)  view.findViewById(R.id.tvDuration);
        tvStartDate = (TextView)  view.findViewById(R.id.tvStartDate);
        tvStartTime = (TextView)  view.findViewById(R.id.tvStartTime);
        btCreate = (Button) view.findViewById(R.id.btCreate);
    }

    void initTextValues(){
        tvStartDate.setText( (month+1) + "/" + day + "/" + year);
        tvStartTime.setText( "12:00");
        tvCompensation.setText("$10");
        tvDuration.setText("0:15");
    }

    void setButtonListeners(){
        tvStartDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showStartDatePicker();
            }
        });
        tvDuration.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showDurationPicker();
            }
        });
        tvStartTime.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });
        tvCompensation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showCompensationPicker();
            }
        });
        btCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createJob();
            }
        });
        etAddress.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View view, boolean b) {
                updateMap();
            }
        });
    }

    void showStartDatePicker(){
        datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int nYear, int nMonth, int nDay) {
                year = nYear; month = nMonth; day = nDay;
                tvStartDate.setText( (month+1) + "/" + day + "/" + year);
            }

        }, year, month, day);
        datePicker.setTitle("Select Date");
        datePicker.show();
    }

    void showTimePicker(){
        timePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int newHour, int newMinute) {
                hour = newHour; minute = newMinute;
                tvStartTime.setText(newHour + ":" + newMinute);
            }
        },hour, minute, false);
        timePicker.setTitle("Select Start Time");
        timePicker.show();
    }

    void showDurationPicker(){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(ParseProject.getContext().LAYOUT_INFLATER_SERVICE);
        View npView = inflater.inflate(R.layout.dialog_number_picker, null);
        final NumberPicker np = (NumberPicker) npView.findViewById(R.id.numberPicker);
        np.setMaxValue(MAX_DURATION);
        np.setMinValue(0);
        String[] values = new String[MAX_DURATION];
        for(int i=0; i<values.length; i++){
            values[i] = Conversions.minutesToString(i * 15);
        }
        np.setWrapSelectorWheel(false);
        np.setDisplayedValues(values);
        new AlertDialog.Builder(getActivity())
            .setTitle("Duration:")
            .setView(npView)
            .setPositiveButton("Set",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Log.e("Value", np.getValue() + "");
                            duration = np.getValue() * 15;
                            tvDuration.setText(Conversions.minutesToString(duration));
                        }
                    })
            .show();
    }

    void showCompensationPicker(){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(ParseProject.getContext().LAYOUT_INFLATER_SERVICE);
        View npView = inflater.inflate(R.layout.dialog_number_picker, null);
        final NumberPicker np = (NumberPicker) npView.findViewById(R.id.numberPicker);
        np.setMaxValue(MAX_COMPENSATION);
        np.setMinValue(0);
        String[] values = new String[MAX_COMPENSATION];
        for(int i=0; i<values.length; i++)
            values[i] = "$" + Integer.toString(i);
        values[0] = "Free";
        np.setWrapSelectorWheel(false);
        np.setDisplayedValues(values);
        new AlertDialog.Builder(getActivity())
            .setTitle("Compensation:")
            .setView(npView)
            .setPositiveButton("Set",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            compensation = np.getValue();
                            tvCompensation.setText("$" + compensation);
                        }
                    })
            .show();
    }

    void updateMap(){
        String address =  etAddress.getText().toString();
        LatLng jobLocation = Conversions.getLocationFromAddress(address);
        if( jobLocation != null){
            map.clear();
            map.addMarker(new MarkerOptions().position(jobLocation).title("JobLocation"));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(jobLocation, 16));
        }
    }

    void createJob(){
        Listing newListing = new Listing();

        newListing.title = etTitle.getText().toString();
        newListing.description = etDescription.getText().toString();
        newListing.address = etAddress.getText().toString();
        newListing.duration = duration;
        newListing.compensation = compensation;
        newListing.startTime = new GregorianCalendar(year,month,day,hour,minute).getTime();

        ListingManager.createListing(newListing, new SuccessCB() {
            @Override
            public void done(String error, boolean success) {
                if(error == null){
                    Toast.makeText(ParseProject.getContext(), "Job Created!", Toast.LENGTH_SHORT).show();
                    ListingsFragment.refreshListings();
                    dismiss();
                }else{
                    Toast.makeText(ParseProject.getContext(), error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
