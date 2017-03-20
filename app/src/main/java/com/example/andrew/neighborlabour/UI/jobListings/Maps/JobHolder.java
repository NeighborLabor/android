package com.example.andrew.neighborlabour.UI.jobListings.Maps;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by andrew on 3/14/17.
 */

public class JobHolder implements Parcelable {
    String Name;
    String ObjectId;
    Double longitude;
    Double latitude;

    public JobHolder(String ObjectId, String name, Double longitude, Double latitude){
        this.Name = name;
        this.ObjectId = ObjectId;
        this.longitude = longitude;
        this.latitude = latitude;
    }


    public JobHolder(Parcel in) {

        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
    }

    public static final Creator<JobHolder> CREATOR = new Creator<JobHolder>() {
        @Override
        public JobHolder createFromParcel(Parcel in) {
            return new JobHolder(in);
        }

        @Override
        public JobHolder[] newArray(int size) {
            return new JobHolder[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
