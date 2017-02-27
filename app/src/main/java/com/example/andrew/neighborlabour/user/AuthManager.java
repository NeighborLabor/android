package com.example.andrew.neighborlabour.user;

import com.example.andrew.neighborlabour.Utils.ParseObjectCB;
import com.example.andrew.neighborlabour.Utils.SuccessCB;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
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

    public static void signIn(String username, String password, final ParseObjectCB cb){
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    cb.done(e+ "", null);
                } else {
                    cb.done(null, user);
                }
            }
        });
    }

    public static void resetPassword(String username, final ParseObjectCB cb){
        ParseUser.requestPasswordResetInBackground(username, new RequestPasswordResetCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    cb.done(null, null);
                } else {
                    cb.done(e + "", null);
                }
            }
        });
    }
}
