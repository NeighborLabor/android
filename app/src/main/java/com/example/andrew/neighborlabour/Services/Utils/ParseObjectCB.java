package com.example.andrew.neighborlabour.Services.Utils;

import com.parse.ParseObject;

/**
 * Created by chevalierc on 2/25/2017.
 */

public interface ParseObjectCB {
    void done(String error, ParseObject response);
}