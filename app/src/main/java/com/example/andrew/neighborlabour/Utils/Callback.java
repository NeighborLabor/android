package com.example.andrew.neighborlabour.Utils;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;

/**
 * Created by chevalierc on 2/23/2017.
 */

//Callback to be used from manager classes to UI
public interface Callback {
    void done(String error, ParseObject response);
}
