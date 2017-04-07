package com.example.andrew.neighborlabour.Services.chat;

import android.util.Log;
import android.widget.Toast;

import com.example.andrew.neighborlabour.ParseProject;
import com.example.andrew.neighborlabour.Services.Utils.ListCB;
import com.example.andrew.neighborlabour.Services.Utils.StringCB;
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

    public static void getOrCreateChatThread(String otherUserId, final StringCB cb){
        //Get Other user as parseObject
        ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
        query.whereEqualTo("objectId", otherUserId);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if(users.size() == 0 ){
                    cb.done( "User not found", null);
                }else if(e == null){
                    final ParseUser otherUser = users.get(0);
                    Log.i(TAG, otherUser.getString("name"));
                    //Check that thread doesn't exist allready
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Thread");
                    query.whereEqualTo("participants", ParseUser.getCurrentUser() );
                    query.whereEqualTo("participants", otherUser );
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> threads, ParseException e) {
                            if(threads.size() != 0 ){
                                String threadId = threads.get(0).getObjectId();
                                cb.done( "Thread Exists", threadId);
                            }else if(e == null){
                                //Create thread
                                final ParseObject newThread = ParseObject.create("Thread");
                                newThread.getRelation("participants").add( ParseUser.getCurrentUser() );
                                newThread.getRelation("participants").add( otherUser );
                                final String threadId = newThread.getObjectId();
                                newThread.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if(e == null){
                                            cb.done(null, threadId);
                                        }else{
                                            cb.done(e + "", null);
                                        }
                                    }
                                });
                            }else{
                                cb.done(e + "", null);
                            }
                        }
                    });
                }else{
                    cb.done(e + "", null);
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

    public static void getThread(String userId, final ListCB cb){
        //implement
    }

    public static void sendMessage(String threadId, String body, String userId, final SuccessCB cb){
        ParseObject message = new ParseObject("Message");
        message.put("threadId", threadId);
        message.put("body", body);
        message.put("userId", userId);
        message.saveInBackground(new SaveCallback(){
            @Override
            public void done(ParseException e){
                if(e == null) {
                    cb.done(null, true);
                }else{
                    cb.done(e + "", false);
                }
            }
        });
    }

    public static void getThreadTitle(String threadId, final StringCB cb){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Thread");
        query.whereEqualTo("objectId", threadId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> threads, ParseException e) {
                ParseObject chatThread = threads.get(0);
                ParseQuery<ParseObject> query = chatThread.getRelation("participants").getQuery();
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> participants, ParseException e) {
                        String currentUserId = ParseUser.getCurrentUser().getObjectId();
                        for(int i = 0; i < participants.size(); i++ ){
                            String userId = participants.get(i).getObjectId();
                            if (!currentUserId.equals(userId) ){
                                String title = participants.get(i).getString("name");
                                if(e == null && title!= null){
                                    cb.done(null, title );
                                }else{
                                    cb.done(e + "", "error");
                                }
                            }
                        }
                    }
                });
            }
        });
    }

    public static void getMessages(String threadId, final ListCB cb){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
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
