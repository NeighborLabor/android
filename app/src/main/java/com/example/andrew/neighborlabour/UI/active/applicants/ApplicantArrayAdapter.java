package com.example.andrew.neighborlabour.UI.active.applicants;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrew.neighborlabour.ParseProject;
import com.example.andrew.neighborlabour.R;
import com.example.andrew.neighborlabour.Services.Utils.ParseObjectCB;
import com.example.andrew.neighborlabour.Services.Utils.SuccessCB;
import com.example.andrew.neighborlabour.Services.chat.ChatManager;
import com.example.andrew.neighborlabour.Services.listings.ListingManager;
import com.example.andrew.neighborlabour.UI.active.ActiveJobsFragment;
import com.example.andrew.neighborlabour.UI.active.PostedListingDetailDialog;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by andrew on 3/20/17.
 */

public class ApplicantArrayAdapter extends ArrayAdapter<ParseObject>{

    ParseUser user;
    String listingId;

    public ApplicantArrayAdapter(@NonNull Context context, ArrayList<ParseObject> user) {
        super(context,0, user);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull final ViewGroup parent) {

        user = (ParseUser) getItem(position);

        if(convertView == null){
            ViewHolder viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.array_item_applicant, parent, false);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.applicant);
            viewHolder.ivMessage = (ImageView) convertView.findViewById(R.id.ivMessage);
            viewHolder.ivAcceptWorker = (ImageView) convertView.findViewById(R.id.ivAccept);

            viewHolder.ivMessage.setOnClickListener(messageListener);
            viewHolder.ivAcceptWorker.setOnClickListener(acceptWorkerListener);

            convertView.setTag(viewHolder);
        }

        listingId = PostedListingDetailDialog.listingId;


        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.textView.setText(user.get("name").toString());
        return convertView;
    }

    View.OnClickListener messageListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ChatManager.createChatThread(user.getObjectId(), new SuccessCB() {
                @Override
                public void done(String error, boolean success) {
                    if(success == false){
                        Toast.makeText(ParseProject.getContext(), error, Toast.LENGTH_SHORT).show();
                    }else{
                        //TODO: open message dialog
                        Toast.makeText(ParseProject.getContext(),"Success",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    };

    View.OnClickListener acceptWorkerListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ListingManager.selectWorker(listingId, user, new ParseObjectCB() {
                @Override
                public void done(String error, ParseObject response) {
                    if(response != null){
                        Toast.makeText(ParseProject.getContext(), "Worker selcted!", Toast.LENGTH_SHORT);
                        Log.d("SELECT_WORKER", response.toString());
                        //todo: dismiss view
                    } else if ( error != null){
                        Toast.makeText(ParseProject.getContext(), error, Toast.LENGTH_SHORT);
                    }
                }
            });
        }
    };

    public class ViewHolder{
        TextView textView;
        ImageView ivMessage;
        ImageView ivAcceptWorker;
    }
}
