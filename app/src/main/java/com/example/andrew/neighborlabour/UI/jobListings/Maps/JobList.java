package com.example.andrew.neighborlabour.UI.jobListings.Maps;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrew on 3/14/17.
 */

public class JobList implements Parcelable {
    private ArrayList<JobHolder> jobs;

    public JobList (ArrayList<JobHolder> jobs){
        this.jobs = jobs;
    }

    public JobList(Parcel in) {
        this.jobs = in.readArrayList(ClassLoader.getSystemClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(jobs);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<JobList> CREATOR = new Creator<JobList>() {
        @Override
        public JobList createFromParcel(Parcel in) {
            return new JobList(in);
        }

        @Override
        public JobList[] newArray(int size) {
            return new JobList[size];
        }
    };
}
