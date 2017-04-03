package com.example.andrew.neighborlabour.UI.chat;


import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrew.neighborlabour.ParseProject;
import com.example.andrew.neighborlabour.R;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

/**
 * Created by chevalierc on 2/20/2017.
 */
public class ChatMessageListAdapter extends ArrayAdapter<ParseObject> {

    public ChatMessageListAdapter(Context context, List<ParseObject> messages){
        super(context,0,messages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.array_item_chat_item, parent, false);
            final ViewHolder holder = new ViewHolder();

            holder.imageOther = (ImageView) convertView.findViewById(R.id.ivProfileOther);
            holder.imageMe = (ImageView) convertView.findViewById(R.id.ivProfileMe);
            holder.body = (TextView)convertView.findViewById(R.id.tvBody);

            convertView.setTag(holder);
        }

        ParseObject message = getItem(position);
        ViewHolder holder = (ViewHolder)convertView.getTag();
        final boolean isMe = message.getString("userId") != null && message.getString("userId").equals(ParseUser.getCurrentUser());

        if(isMe){
            holder.imageMe.setVisibility(View.VISIBLE);
            holder.imageOther.setVisibility(View.GONE);
            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        }else{
            holder.imageOther.setVisibility(View.VISIBLE);
            holder.imageMe.setVisibility(View.GONE);
            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        }

        final ImageView profileView = isMe ? holder.imageMe: holder.imageOther;
        holder.body.setText(message.getString("body"));
        Picasso.with(getContext()).load(getProfileUrl(message.getString("userId"))).into(profileView);
        return convertView;
    }

    private static String getProfileUrl(final String userId){
        String hex = "";
        try{
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final byte[] hash = digest.digest(userId.getBytes());
            final BigInteger bigInt = new BigInteger(hash);
            hex = bigInt.abs().toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "http://www.gravatar.com/avatar/" + hex + "?d=identicon";
    }

    final class ViewHolder {
        public ImageView imageOther;
        public ImageView imageMe;
        public TextView body;
    }

}
