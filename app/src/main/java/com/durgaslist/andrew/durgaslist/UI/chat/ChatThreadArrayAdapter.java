package com.durgaslist.andrew.durgaslist.UI.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.andrew.durgaslist.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chevalierc on 2/26/2017.
 */

public class ChatThreadArrayAdapter extends ArrayAdapter<ParseObject> {
    final String TAG = "ListingArrayAdapter";

    public ChatThreadArrayAdapter(Context context, ArrayList<ParseObject> listings){
        super(context,0,listings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //create listing array view if it doesnt exist
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.array_item_chat_thread, parent, false);
            final ViewHolder holder = new ViewHolder();

            holder.name = (TextView) convertView.findViewById(R.id.tvName);

            convertView.setTag(holder);
        }

        //set
        final ParseObject chatThread = getItem(position);
        final ViewHolder holder = (ViewHolder)convertView.getTag();


        ParseQuery<ParseObject> query = chatThread.getRelation("participants").getQuery();

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> participants, ParseException e) {
                String currentUserId = ParseUser.getCurrentUser().getObjectId();
                for(int i = 0; i < participants.size(); i++ ){
                    String userId = participants.get(i).getObjectId();
                    if (!currentUserId.equals(userId) ){
                        holder.name.setText( participants.get(i).getString("name") );
                    }
                }
            }
        });

        return convertView;
    }



    final class ViewHolder {
        public TextView name;
    }

}
