package com.example.andrew.neighborlabour.Services.Utils;

import com.parse.ParseUser;

/**
 * Created by andrew on 4/3/17.
 */

public interface UserCB {
    public void done(String error, ParseUser parseUser);
}
