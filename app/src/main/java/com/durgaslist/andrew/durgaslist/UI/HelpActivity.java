package com.durgaslist.andrew.durgaslist.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.andrew.durgaslist.R;
import com.durgaslist.andrew.durgaslist.ParseProject;
import com.durgaslist.andrew.durgaslist.Services.Utils.ListCB;
import com.durgaslist.andrew.durgaslist.Services.Utils.ParseObjectCB;
import com.durgaslist.andrew.durgaslist.Services.user.ReviewManager;
import com.durgaslist.andrew.durgaslist.Services.user.UserManager;
import com.durgaslist.andrew.durgaslist.UI.auth.ReviewArrayAdapter;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chevalierc on 3/7/2017.
 */

public class HelpActivity extends AppCompatActivity {

    public final static String TAG = "PROFILE_ACTIVITY";

    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        back = (ImageView) findViewById(R.id.btBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

}
