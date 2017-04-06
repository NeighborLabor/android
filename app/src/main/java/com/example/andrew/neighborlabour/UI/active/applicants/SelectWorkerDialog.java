package com.example.andrew.neighborlabour.UI.active.applicants;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrew.neighborlabour.R;
import com.example.andrew.neighborlabour.Services.Utils.ListingCB;
import com.example.andrew.neighborlabour.Services.Utils.ParseObjectCB;
import com.example.andrew.neighborlabour.Services.listings.Listing;
import com.example.andrew.neighborlabour.Services.listings.ListingManager;
import com.example.andrew.neighborlabour.UI.active.ActiveJobsFragment;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

/**
 * Created by andrew on 3/17/17.
 */

public class SelectWorkerDialog extends Activity {

    private ListView list;

    String parseID;

    private Applicants arrayAdapter;

    private ArrayList<ParseObject> userList;

    TextView header;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userList = new ArrayList<>();

        arrayAdapter = new Applicants(this, userList);

        setContentView(R.layout.dialog_select_worker);

        list =  (ListView) findViewById(R.id.select_worker_list);

        header = (TextView) findViewById(R.id.select_worker_header);

        list.setAdapter(arrayAdapter);

        list.setOnItemClickListener(selectTheWorker);


        getUser();

    }


    private void getUser(){

        parseID = getIntent().getStringExtra("ObjectID");
        Log.d("SELECT_WORKER", "happened");

        ListingManager.getListing(parseID, new ListingCB() {

            @Override
            public void done(String error, Listing response) {

                if(error == null && response != null){
                    userList.addAll(response.applicants);
                    arrayAdapter.notifyDataSetChanged();
                    changeHeader(userList.size());
                    Log.d("SELECT_WORKER", "happened4");




                } else if(error != null){
                    thefinish(error + " ");
                    Log.d("SELECT_WORKER", "happened3");


                } else {
                    Log.d("SELECT_WORKER", "happened2");
                }

        }



        });
    }

    public void thefinish(String e) {
            Log.d("SELECT_WORKER", e);

        }

    private void closeWindow(){



        this.finish();
    }

    public void changeHeader(int amount){
        Log.d("SELECT_WORKER", String.valueOf(amount));
        if(amount == 0){

            header.setText("currently no applicants");
        }

    }

    public AdapterView.OnItemClickListener selectTheWorker = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(final AdapterView<?> parent, View view, int position, long id) {

            ParseUser selectedWorker = (ParseUser) userList.get(position);

            Log.d("SELECT_WORKER", selectedWorker.getObjectId() + " "+ parseID);

            ListingManager.selectWorker(parseID, selectedWorker, new ParseObjectCB() {
                @Override
                public void done(String error, ParseObject response) {
                    if(response != null){
                        ActiveJobsFragment.refresh();
                        Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT);
                        Log.d("SELECT_WORKER", response.toString());

                    } else if ( error != null){

                        Log.d("SELECT_WORKER 1", error);

                    }else {
                        Toast.makeText(getApplicationContext(),"Unkown Error", Toast.LENGTH_SHORT);
                        Log.d("SELECT_WORKER", "Unkown Error");
                    }
                }
            });
            closeWindow();
        }

    };



}
