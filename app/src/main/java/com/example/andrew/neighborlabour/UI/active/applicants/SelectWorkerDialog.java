package com.example.andrew.neighborlabour.UI.active.applicants;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.andrew.neighborlabour.R;
import com.example.andrew.neighborlabour.Services.Utils.ListingCB;
import com.example.andrew.neighborlabour.Services.Utils.ParseObjectCB;
import com.example.andrew.neighborlabour.Services.listings.Listing;
import com.example.andrew.neighborlabour.Services.listings.ListingManager;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by andrew on 3/17/17.
 */

public class SelectWorkerDialog extends Activity {

    private ListView list;

    String parseID;

    private Applicants arrayAdapter;

    private ArrayList<ParseUser> userList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_worker);

        getUser();

        userList = new ArrayList<>();

        list =  (ListView) findViewById(R.id.select_worker_list);

        arrayAdapter = new Applicants(this, userList);

        list.setAdapter(arrayAdapter);

        list.setOnItemClickListener(selectTheWorker);
    }


    private void getUser(){

        parseID = getIntent().getStringExtra("ObjectID");

        ListingManager.getListing(parseID, new ListingCB() {

            @Override
            public void done(String error, Listing response) {
                if(error == null && response != null){
                    userList.addAll(response.applicants);
                    arrayAdapter.notifyDataSetChanged();

                    Log.d("USER_LIST", String.valueOf(userList.size()));



                } else if(error != null){
                    thefinish(error + " ");

                }
        }
        });
    }

    public void thefinish(String e) {

            Toast.makeText(this,e, Toast.LENGTH_SHORT).show();

        }

    public AdapterView.OnItemClickListener selectTheWorker = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(final AdapterView<?> parent, View view, int position, long id) {

            ParseUser selectedWorker = userList.get(position);

            Log.d("SELECT_WORKER", selectedWorker.getObjectId() + " "+ parseID);

            ListingManager.selectWorker(parseID, selectedWorker.getObjectId(), new ParseObjectCB() {
                @Override
                public void done(String error, ParseObject response) {
                    if(response != null){
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
        }


    };



}
