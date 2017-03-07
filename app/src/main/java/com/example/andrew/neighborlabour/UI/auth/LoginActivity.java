package com.example.andrew.neighborlabour.UI.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrew.neighborlabour.MainActivity;
import com.example.andrew.neighborlabour.ParseProject;
import com.example.andrew.neighborlabour.R;
import com.example.andrew.neighborlabour.Services.Utils.SuccessCB;
import com.example.andrew.neighborlabour.Services.user.AuthManager;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "LOGIN_SCREEN";

    TextView username;
    TextView password;

    Button register;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser != null) {
            toMainScreen();
        }

        username = (TextView) findViewById(R.id.username);
        password = (TextView)findViewById(R.id.password);

        login = (Button)findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toAccountScreen(view);
            }

        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn(view);
            }
        });
    }

    public void logIn(View view){
       AuthManager.signIn(username.getText().toString(), password.getText().toString(),
               new SuccessCB() {
                   @Override
                   public void done(String error, boolean success) {
                       if(success == true){
                           Toast toast = Toast.makeText(ParseProject.getContext(), "Login Successful", Toast.LENGTH_SHORT);
                           toast.show();
                           toMainScreen();
                       }else{
                           Toast toast = Toast.makeText(ParseProject.getContext(), error, Toast.LENGTH_SHORT);
                           toast.show();
                           Log.d("Login", error);
                       }
                   }
               });
    }

    public void toAccountScreen(View view){
        Intent intent = new Intent(ParseProject.getContext(), RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    public void toMainScreen() {
        Intent intent = new Intent(ParseProject.getContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }


}
