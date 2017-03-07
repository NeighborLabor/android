package com.example.andrew.neighborlabour.Services.user;

import com.example.andrew.neighborlabour.Services.Utils.SuccessCB;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

/**
 * Created by chevalierc on 2/23/2017.
 */

public class AuthManager {
    private static String TAG = "AuthManager";

    public static void createUser(User user, final SuccessCB cb){
        //TODO: better content checking
        final ParseUser newUser = new ParseUser();

        if(user.email != null & user.email.length() >= 6){
            newUser.setUsername(user.email);
            newUser.setEmail(user.email);
        }else{
            cb.done("ERROR: Email doesn't meet requirments", false);
        }

        if(user.password != null & user.password.length() >= 6){
            newUser.setPassword(user.password);
        }else{
            cb.done("ERROR: Password doesn't meet requirments", false);
        }

        if(user.phone != null & user.phone.length() >= 7){
            newUser.put("phone", user.phone);
        }else{
            cb.done("ERROR: PHhone doesn't meet requirments", false);
        }

        if(user.name != null){
            newUser.put("name", user.name);
        }else{
            cb.done( "ERROR: Name doesn't meet requirments", false);
        }

        if(user.bio != null){
            newUser.put("bio", user.bio);
        }

        if(user.skillSet != null){
            newUser.addAll("bio", user.skillSet);
        }

        newUser.signUpInBackground(new SignUpCallback(){
            @Override
            public void done(ParseException e){
                if(e == null){
                    cb.done(null, true);
                }else{
                    cb.done(e + "", false);
                }
            }
        });

    }

    public static void updateUser(User user, final SuccessCB cb){
        //TODO: better content checking
        final ParseUser newUser = ParseUser.getCurrentUser();

        if(user.email != null & user.email.length() >= 6){
            newUser.setUsername(user.email);
            newUser.setEmail(user.email);
        }else{
            cb.done("ERROR: Email doesn't meet requirements", false);
        }

        if(user.phone != null & user.phone.length() >= 7){
            newUser.put("phone", user.phone);
        }else{
            cb.done("ERROR: Phone doesn't meet requirements", false);
        }

        if(user.name != null){
            newUser.put("name", user.name);
        }else{
            cb.done( "ERROR: Name doesn't meet requirements", false);
        }

        if(user.password != null & user.password.length() >= 6){
            newUser.setPassword(user.password);
        }else{
            cb.done("ERROR: Password doesn't meet requirments", false);
        }

        if(user.bio != null){
            newUser.put("bio", user.bio);
        }

        if(user.skillSet != null){
            newUser.addAll("bio", user.skillSet);
        }

        newUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    cb.done(e+ "", false);
                } else {
                    cb.done(null, true);
                }
            }
        });

    }

    public static void signIn(String username, String password, final SuccessCB cb){
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    cb.done(e+ "", false);
                } else {
                    cb.done(null, true);
                }
            }
        });
    }

    public static void resetPassword(String username, final SuccessCB cb){
        ParseUser.requestPasswordResetInBackground(username, new RequestPasswordResetCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    cb.done(null, true);
                } else {
                    cb.done(e + "", false);
                }
            }
        });
    }
}
