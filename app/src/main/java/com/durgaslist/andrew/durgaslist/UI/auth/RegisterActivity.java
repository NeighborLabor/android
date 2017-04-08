package com.durgaslist.andrew.durgaslist.UI.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.durgaslist.andrew.durgaslist.UI.MainActivity;
import com.durgaslist.andrew.durgaslist.ParseProject;
import com.andrew.durgaslist.R;
import com.durgaslist.andrew.durgaslist.Services.Utils.SuccessCB;
import com.durgaslist.andrew.durgaslist.Services.user.AuthManager;
import com.durgaslist.andrew.durgaslist.Services.user.User;

public class RegisterActivity extends AppCompatActivity {

    public final static String TAG = "SIGNIN_SCREEN";

    TextView username;
    TextView password;
    TextView password2;
    TextView name;
    TextView phone;
    TextView bio;

    Button register;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (TextView) findViewById(R.id.username);
        password = (TextView)findViewById(R.id.password);
        password2 = (TextView) findViewById(R.id.password2);
        name = (TextView) findViewById(R.id.name);
        phone = (TextView) findViewById(R.id.phone);
        bio = (TextView) findViewById(R.id.bio);

        register = (Button) findViewById(R.id.register);
        back = (ImageView) findViewById(R.id.btBack);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup(view);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toLoginScreen();
            }
        });
    }

    public void signup(View view){
        User user = new User();
        user.email = username.getText().toString();
        user.name = name.getText().toString();
        user.phone = phone.getText().toString();
        user.bio = bio.getText().toString();
        user.password = password.getText().toString();

        String p1 = password.getText().toString();
        String p2 = password2.getText().toString();

        if( !p1.equals(p2) ){
            Toast toast = Toast.makeText(ParseProject.getContext(), "Passwords Don't Match", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

       AuthManager.createUser(user,
               new SuccessCB() {
                   @Override
                   public void done(String error, boolean success) {
                       if(success == true){
                           Toast toast = Toast.makeText(ParseProject.getContext(), "Signup Successful", Toast.LENGTH_SHORT);
                           toast.show();
                           toMainScreen();
                       }else{
                           Toast toast = Toast.makeText(ParseProject.getContext(), error, Toast.LENGTH_SHORT);
                           toast.show();
                           Log.d(TAG, error);
                       }
                   }
               });
    }


    public void toMainScreen() {
        Intent intent = new Intent(ParseProject.getContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void toLoginScreen(){
        Intent intent = new Intent(ParseProject.getContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }


}
