package com.example.andrew.neighborlabour.UI.active.applicants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.andrew.neighborlabour.Services.Utils.StringCB;
import com.example.andrew.neighborlabour.Services.chat.ChatManager;
import com.example.andrew.neighborlabour.Services.listings.ListingManager;
import com.example.andrew.neighborlabour.UI.active.PostedListingDetailDialog;
import com.example.andrew.neighborlabour.UI.auth.ProfileActivity;
import com.example.andrew.neighborlabour.UI.chat.ChatDialogFragment;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by andrew on 3/20/17.
 */

public class ApplicantArrayAdapter extends ArrayAdapter<ParseObject>{

    final String TAG = this.getClass().toString();

    String listingId;
    PostedListingDetailDialog parentDialog;

    public ApplicantArrayAdapter(@NonNull Context context, ArrayList<ParseObject> user, PostedListingDetailDialog dialog) {
        super(context,0, user);
        this.parentDialog = dialog;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        final ParseUser user = (ParseUser) getItem(position);

        Log.i(TAG, user.getObjectId() + " exists");

        if(convertView == null){
            ViewHolder viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.array_item_applicant, parent, false);
            viewHolder.applicantName = (TextView) convertView.findViewById(R.id.applicant);
            viewHolder.ivMessage = (ImageView) convertView.findViewById(R.id.ivMessage);
            viewHolder.ivAcceptWorker = (ImageView) convertView.findViewById(R.id.ivAccept);

            viewHolder.ivMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ChatManager.getOrCreateChatThread(user.getObjectId(), new StringCB() {
                        @Override
                        public void done(String error, String threadId) {
                            if(error == null){
                                Toast.makeText(ParseProject.getContext(), error, Toast.LENGTH_SHORT).show();
                            }else{
                                //TODO: open message dialog
                                ChatDialogFragment chatDialog = new ChatDialogFragment();
                                Bundle args = new Bundle();
                                args.putString("threadId", threadId );
                                chatDialog.setArguments(args);
                                chatDialog.show(parentDialog.getFragmentManager(), "NoticeDialogFragment");
                            }
                        }
                    });
                }
            });

            viewHolder.ivAcceptWorker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ListingManager.selectWorker(listingId, user, new ParseObjectCB() {
                        @Override
                        public void done(String error, ParseObject response) {
                            if(response != null){
                                Toast.makeText(ParseProject.getContext(), "Worker selcted!", Toast.LENGTH_SHORT);
                                Log.d("SELECT_WORKER", response.toString());
                            } else if ( error != null){
                                Toast.makeText(ParseProject.getContext(), error, Toast.LENGTH_SHORT);
                            }
                            parentDialog.dismiss();
                        }
                    });
                }
            });

            viewHolder.applicantName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ParseProject.getContext(), ProfileActivity.class);
                    intent.putExtra("userId", user.getObjectId());
                    Log.i(TAG, user.getObjectId() + " clicked");
                    intent.putExtra("showPhone", true);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ParseProject.getContext().startActivity(intent);
                }
            });

            convertView.setTag(viewHolder);
        }

        listingId = PostedListingDetailDialog.listingId;


        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.applicantName.setText(user.get("name").toString());
        return convertView;
    }


    public class ViewHolder{
        TextView applicantName;
        ImageView ivMessage;
        ImageView ivAcceptWorker;
    }
}
