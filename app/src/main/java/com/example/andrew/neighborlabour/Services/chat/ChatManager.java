package com.example.andrew.neighborlabour.Services.chat;

import android.util.Log;

import com.example.andrew.neighborlabour.Services.Utils.ListCB;
import com.example.andrew.neighborlabour.Services.Utils.SuccessCB;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

/**
 * Created by chevalierc on 4/2/2017.
 */

public class ChatManager {
    public static final String TAG = "ChatManager";

    public static void createChatThread(String otherUserId, final SuccessCB cb){
        //Get Other user as parseObject
        ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
        query.whereEqualTo("objectId", otherUserId);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if(users.size() == 0 ){
                    cb.done( "User not found", false);
                }else if(e == null){
                    final ParseUser otherUser = users.get(0);
                    Log.i(TAG, otherUser.getString("name"));
                    //Check that thread doesn't exist allready
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Thread");
                    query.whereEqualTo("participants", ParseUser.getCurrentUser() );
                    query.whereEqualTo("participants", otherUser );
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> users, ParseException e) {
                            Log.i(TAG, "" + users.size());
                            if(users.size() != 0 ){
                                cb.done( "Thread Exists", false);
                            }else if(e == null){
                                //Create thread
                                final ParseObject newThread = ParseObject.create("Thread");
                                newThread.getRelation("participants").add( ParseUser.getCurrentUser() );
                                newThread.getRelation("participants").add( otherUser );
                                newThread.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if(e == null){
                                            cb.done(null, true);
                                        }else{
                                            cb.done(e + "", false);
                                        }
                                    }
                                });
                            }else{
                                cb.done(e + "", false);
                            }
                        }
                    });
                }else{
                    cb.done(e + "", false);
                }
            }
        });
    }

    public static void getChatThreads(final ListCB cb){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Thread");
        query.whereEqualTo("participants", ParseUser.getCurrentUser() );

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> threads, ParseException e) {
                if(e == null){
                    cb.done(null, threads);
                }else{
                    cb.done(e + "", threads);
                }
            }
        });
    }

    public static void getMessages(String threadId, final ListCB cb){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Messages");
        query.whereEqualTo("threadId",threadId );

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> messages, ParseException e) {
                if(e == null){
                    cb.done(null, messages);
                }else{
                    cb.done(e + "", messages);
                }
            }
        });
    }


}
