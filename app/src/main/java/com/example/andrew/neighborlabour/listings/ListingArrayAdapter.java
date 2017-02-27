package com.example.andrew.neighborlabour.listings;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.andrew.neighborlabour.R;
import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by chevalierc on 2/26/2017.
 */

public class ListingArrayAdapter extends ArrayAdapter<ParseObject> {
    final String TAG = "ListingArrayAdapter";

    public ListingArrayAdapter(Context context, ArrayList<ParseObject> listings){
        super(context,0,listings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.listing_item, parent, false);
            final ViewHolder holder = new ViewHolder();
            holder.body = (TextView) convertView.findViewById(R.id.tvBody);
            holder.title = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.compensationDuration = (TextView) convertView.findViewById(R.id.tvCompensationDuration);
            convertView.setTag(holder);
        }

        final ParseObject listing = getItem(position);
        final ViewHolder holder = (ViewHolder)convertView.getTag();

        holder.body.setText(listing.getString("descr"));
        holder.title.setText(listing.getString("title"));
        String compensationDuration = "$" + listing.getDouble("compensation") + " / ";
        int duration = listing.getInt("duration");
        if(duration > 59){
            int hours = ((duration+1) % 60);
            int minutes = duration - (hours * 60);
            compensationDuration +=  hours;
            if(minutes != 0) compensationDuration = "."  + Math.round(minutes/60 * 10) / 10 ;
            compensationDuration += hours == 1 ? " hr" : " hrs";
        }else{
            compensationDuration += duration + " mins";
        }
        holder.compensationDuration.setText(compensationDuration);

        return convertView;
    }


    final class ViewHolder {
        public TextView title;
        public TextView body;
        public TextView compensationDuration;
    }

}
