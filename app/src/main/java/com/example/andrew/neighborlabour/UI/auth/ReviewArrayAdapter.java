package com.example.andrew.neighborlabour.UI.auth;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.andrew.neighborlabour.R;
import com.example.andrew.neighborlabour.Services.Utils.Conversions;
import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by chevalierc on 2/26/2017.
 */

public class ReviewArrayAdapter extends ArrayAdapter<ParseObject> {
    final String TAG = this.getClass().toString();

    public ReviewArrayAdapter(Context context, ArrayList<ParseObject> listings){
        super(context,0,listings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //create listing array view if it doesnt exist
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.array_item_review, parent, false);
            final ViewHolder holder = new ViewHolder();

            holder.body = (TextView) convertView.findViewById(R.id.tvBody);
            holder.stars = (TextView) convertView.findViewById(R.id.tvStars);

            convertView.setTag(holder);
        }

        //set
        final ParseObject review = getItem(position);
        final ViewHolder holder = (ViewHolder)convertView.getTag();

        //set text fields
        String starsString = "";
        for(int i = 0; i < review.getInt("stars") ; i++){
            starsString += "★";
        }

        holder.stars.setText( starsString );
        holder.body.setText( review.getString("body") );

        return convertView;
    }

    final class ViewHolder {
        public TextView stars;
        public TextView body;
    }

}
