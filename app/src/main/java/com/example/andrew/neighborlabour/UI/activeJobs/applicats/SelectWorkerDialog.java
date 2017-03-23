package com.example.andrew.neighborlabour.UI.activeJobs.applicats;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrew.neighborlabour.R;
import com.example.andrew.neighborlabour.Services.Utils.ListingCB;
import com.example.andrew.neighborlabour.Services.listings.Listing;
import com.example.andrew.neighborlabour.Services.listings.ListingManager;
import com.example.andrew.neighborlabour.Services.user.UserManager;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrew on 3/17/17.
 */

public class SelectWorkerDialog extends Activity {

    private ListView list;


    private Applicats arrayAdapter;

    private ArrayList<ParseUser> userList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_worker_dialog);

        getUser();

        userList = new ArrayList<>();

        list =  (ListView) findViewById(R.id.select_worker_list);

        arrayAdapter = new Applicats(this, userList);

        list.setAdapter(arrayAdapter);
    }


    private void getUser(){

        final String parseID = getIntent().getStringExtra("ObjectID");

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


}
