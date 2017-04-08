package com.durgaslist.andrew.durgaslist.UI.listings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.andrew.durgaslist.R;
import com.durgaslist.andrew.durgaslist.Services.Utils.Conversions;
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

        //create listing array view if it doesnt exist
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.array_item_listing, parent, false);
            final ViewHolder holder = new ViewHolder();

            holder.body = (TextView) convertView.findViewById(R.id.tvBody);
            holder.title = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.compensation = (TextView) convertView.findViewById(R.id.tvCompensation);
            holder.duration = (TextView) convertView.findViewById(R.id.tvDuration);
            holder.distance = (TextView) convertView.findViewById(R.id.tvDistance);
            holder.date = (TextView) convertView.findViewById(R.id.tvDate);

            convertView.setTag(holder);
        }

        //set
        final ParseObject listing = getItem(position);
        final ViewHolder holder = (ViewHolder)convertView.getTag();


        //set text fields
        holder.title.setText( listing.getString("title") );
        holder.body.setText( listing.getString("descr") );
        holder.compensation.setText( "$" + listing.getInt("compensation") );
        holder.duration.setText(Conversions.minutesToString(listing.getInt("duration") ));
        holder.distance.setText(Conversions.getDistanceFromUser(listing));
        holder.date.setText(Conversions.dateAsString(listing.getDate("startTime")));

        return convertView;
    }

    final class ViewHolder {
        public TextView title;
        public TextView body;
        public TextView duration;
        public TextView distance;
        public TextView date;
        public TextView compensation;
    }

}
