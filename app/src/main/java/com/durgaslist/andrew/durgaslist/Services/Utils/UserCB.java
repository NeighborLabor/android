package com.durgaslist.andrew.durgaslist.Services.Utils;

import com.parse.ParseUser;

/**
 * Created by andrew on 4/3/17.
 */

public interface UserCB {
    public void done(String error, ParseUser parseUser);
}
