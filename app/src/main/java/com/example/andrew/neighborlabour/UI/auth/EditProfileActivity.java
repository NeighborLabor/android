package com.example.andrew.neighborlabour.UI.auth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrew.neighborlabour.ParseProject;
import com.example.andrew.neighborlabour.R;
import com.example.andrew.neighborlabour.Services.Utils.SuccessCB;
import com.example.andrew.neighborlabour.Services.user.AuthManager;
import com.example.andrew.neighborlabour.Services.user.User;
import com.parse.ParseUser;

/**
 * Created by chevalierc on 3/7/2017.
 */

public class EditProfileActivity extends AppCompatActivity {

    public final static String TAG = "PROFILE_ACTIVITY";

    TextView username;
    TextView password;
    TextView password2;
    TextView name;
    TextView phone;
    TextView bio;

    Button reset;
    Button update;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        username = (TextView) findViewById(R.id.username);
        password = (TextView)findViewById(R.id.password);
        password2 = (TextView) findViewById(R.id.password2);
        name = (TextView) findViewById(R.id.name);
        phone = (TextView) findViewById(R.id.phone);
        bio = (TextView) findViewById(R.id.bio);

        reset = (Button) findViewById(R.id.reset);
        update = (Button) findViewById(R.id.update);
        back = (ImageView) findViewById(R.id.btBack);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFields();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update(view);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setFields();
    }

    public void setFields(){
        ParseUser user = ParseUser.getCurrentUser();
        username.setText(user.getUsername());
        name.setText(user.getString("name"));
        phone.setText(user.getString("phone"));
        bio.setText(user.getString("bio"));
    }

    public void update(View view) {
        User user = new User();
        user.email = username.getText().toString();
        user.name = name.getText().toString();
        user.phone = phone.getText().toString();
        user.bio = bio.getText().toString();
        user.password = password.getText().toString();

        String p1 = password.getText().toString();
        String p2 = password2.getText().toString();

        if (!p1.equals(p2)) {
            Toast toast = Toast.makeText(ParseProject.getContext(), "Passwords Don't Match", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        AuthManager.updateUser(user,
                new SuccessCB() {
                    @Override
                    public void done(String error, boolean success) {
                        if(success == true){
                            Toast toast = Toast.makeText(ParseProject.getContext(), "Update Successful", Toast.LENGTH_SHORT);
                            toast.show();
                            finish();
                        }else{
                            Toast toast = Toast.makeText(ParseProject.getContext(), error, Toast.LENGTH_SHORT);
                            toast.show();
                            Log.d(TAG, error);
                        }
                    }
                });
    }

}
