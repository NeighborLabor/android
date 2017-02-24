package com.example.andrew.neighborlabour.Utils;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chevalierc on 2/23/2017.
 */

public interface ListCallback {
    public void done(String error, List<ParseObject> objects);
}
