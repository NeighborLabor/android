package com.example.andrew.neighborlabour.Services.listings;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;

/**
 * Created by chevalierc on 2/23/2017.
 */

public class Listing {
    public String title = null;
    public String description = null;
    public String address = null;
    public Integer duration = 0;
    public int compensation = 0;
    public Double latitude = 0.0;
    public Double longitude = 0.0;
    public Date startTime = null;
    public List<ParseObject> applicants = null;
    public ParseUser employer = null;
    public ParseUser worker = null;
    public String id;

    public Listing(){}

    public Listing(ParseObject listing){
        id = listing.getObjectId();
        if(listing.getString("title") != null){
            this.title = listing.getString("title");
        }
        if(listing.getString("descr") != null){
            this.description = listing.getString("descr");
        }
        if(listing.getString("address") != null){
            this.address = listing.getString("address");
        }
        if(listing.getInt("duration") != 0){
            this.duration = listing.getInt("duration");
        }
        if(listing.getInt("compensation") != 0){
            this.compensation = listing.getInt("compensation");
        }
        if(listing.getDate("startTime") != null){
            this.startTime = listing.getDate("startTime");
        }
        if(listing.getParseGeoPoint("geopoint") != null){
            this.longitude = listing.getParseGeoPoint("geopoint").getLongitude();
            this.latitude = listing.getParseGeoPoint("geopoint").getLatitude();
        }
    }

    /**public void setApplicant(List<ParseUser> applicants){
        this.applicants = applicants;
    }**/

    public void setEmployer(ParseUser employer){
        this.employer = employer;
    }

    public void setWorker(ParseUser worker){
        this.worker = worker;
    }

    public String toString(){
        String string = this.title;
        if(this.employer != null) string += "\nemployer: " + this.employer.getString("username");
        if(this.worker != null) string += "\nworker: " + this.worker.getString("username");
        if(this.applicants != null){
            string += "\napplicants: ";
            for(int i = 0; i < this.applicants.size();i++) {
                string += this.applicants.get(i).getString("username");
            }
        }
        return string;
    }
}
