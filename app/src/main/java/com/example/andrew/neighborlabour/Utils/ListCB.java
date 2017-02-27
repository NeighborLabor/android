package com.example.andrew.neighborlabour.Utils;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by chevalierc on 2/25/2017.
 */

public interface ListCB {
    public void done(String error, List<ParseObject> objects);
}