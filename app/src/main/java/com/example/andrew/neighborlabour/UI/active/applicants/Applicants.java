package com.example.andrew.neighborlabour.UI.active.applicants;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.andrew.neighborlabour.R;
import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by andrew on 3/20/17.
 */

public class Applicants extends ArrayAdapter<ParseUser>{



    public Applicants(@NonNull Context context, ArrayList<ParseUser> user) {
        super(context,0, user);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ParseUser user = getItem(position);

        if(convertView == null){
            ViewHolder viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.array_item_applicant, parent, false);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.applicant);
            convertView.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.textView.setText(user.get("name").toString());
        return convertView;
    }

    public class ViewHolder{
            TextView textView;

    }
}