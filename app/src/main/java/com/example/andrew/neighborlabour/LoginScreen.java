package com.example.andrew.neighborlabour;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrew.neighborlabour.Services.Utils.SuccessCB;
import com.example.andrew.neighborlabour.Services.user.AuthManager;
import com.example.andrew.neighborlabour.Services.user.User;
import com.parse.ParseUser;

public class LoginScreen extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "LOGIN_SCREEN";

    TextView bio;
    TextView name;
    TextView password;

    TextView email;
    TextView phone;
    TextView confirm_password;

    Button createAccount2;
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

        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.Email);
        phone = (TextView) findViewById(R.id.phone);
        confirm_password = (TextView) findViewById(R.id.Confirm_Password);
        bio = (TextView) findViewById(R.id.Bio);
        password = (TextView)findViewById(R.id.Password);
        createAccount2 = (Button)findViewById(R.id.Create_Account);

    }

    public void logIn(View view){
       AuthManager.signIn(email.getText().toString(), password.getText().toString(),
               new SuccessCB() {
                   @Override
                   public void done(String error, boolean success) {
                       if(success == true){
                           toMainScreen();
                       }else{
                           Toast toast = Toast.makeText(ParseProject.getContext(), error, Toast.LENGTH_SHORT);
                           Log.d("Login", error);
                       }
                   }
               });
    }

    public void signUp(View veiw){
        confirm_password.setVisibility(View.VISIBLE);
        name.setVisibility(View.VISIBLE);
        email.setVisibility(View.VISIBLE);
        phone.setVisibility(View.VISIBLE);
        //login.setVisibility(View.INVISIBLE);
        createAccount2.setVisibility(View.VISIBLE);
        bio.setVisibility(View.VISIBLE);
    }

    public void createAccount(View view){
        //compare passwords
        String pass1 = password.getText().toString();
        String pass2 = confirm_password.getText().toString();
        if(!pass1.equals(pass2)){
            CharSequence message = "Password do not match";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            return;
        }
        User user = new User();
        user.bio = "";
        user.email = email.getText().toString();
        user.name = name.getText().toString();
        user.password = pass1;

        user.phone = phone.getText().toString();

        AuthManager.createUser(user,new SuccessCB() {
            @Override
            public void done(String error, boolean success) {
                if(error == null){
                    toMainScreen();
                }else{
                    Toast toast = Toast.makeText(ParseProject.getContext(), error, Toast.LENGTH_SHORT);
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
