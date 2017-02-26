package com.example.andrew.neighborlabour;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.andrew.neighborlabour.R;
import com.example.andrew.neighborlabour.Utils.Callback;
import com.example.andrew.neighborlabour.user.AuthManager;
import com.example.andrew.neighborlabour.user.User;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;

public class LoginScreen extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "LOGIN_SCREEN";

    TextView userName;

    TextView password;

    TextView email;
    TextView phone;

    Button createAccount2;
    Button register;
    Button login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        email = (TextView) findViewById(R.id.Email);

        phone = (TextView) findViewById(R.id.phone);

        userName = (TextView)findViewById(R.id.UserName);

        password = (TextView)findViewById(R.id.Password);

        createAccount2 = (Button)findViewById(R.id.Create_Account);



    }

    public void logIn(View view){
        Intent intent = new Intent(this, MainActivity.class);


        startActivity(intent);

    }

    public void signUp(View veiw){


        email.setVisibility(View.VISIBLE);
        phone.setVisibility(View.VISIBLE);
        createAccount2.setVisibility(View.VISIBLE);

    }

    public void createAccount(View view){
        User user = new User();
        user.bio = "";
        user.email = email.getText().toString();

        user.name = userName.getText().toString();
        user.password = password.getText().toString();

        user.phone = phone.getText().toString();

        AuthManager.createUser(user,new Callback() {
            @Override
            public void done(String error, ParseObject response) {
                if(error == null){
                    toMainScreen();
                }else{
                    Log.d("Login", error);
                }
            }
        });

    }

    public void toMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);


        startActivity(intent);

    }


}
