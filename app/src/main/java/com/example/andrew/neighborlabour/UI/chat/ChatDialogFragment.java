package com.example.andrew.neighborlabour.UI.chat;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.andrew.neighborlabour.ParseProject;
import com.example.andrew.neighborlabour.R;
import com.example.andrew.neighborlabour.Services.Utils.ListCB;
import com.example.andrew.neighborlabour.Services.Utils.SuccessCB;
import com.example.andrew.neighborlabour.Services.chat.ChatManager;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chevalierc on 3/10/2017.
 */

public class ChatDialogFragment extends DialogFragment {
    final String TAG = "ChatDialogFragment";
    static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;
    static final int POLL_INTERVAL = 1000;

    String threadId;

    EditText etMessage;
    Button btSend;
    ListView lvChat;

    ChatMessageListAdapter mAdapter;
    ArrayList<ParseObject> mMessages;
    boolean mFirstLoad;

    Handler mHandler = new Handler();
    Runnable mRefreshMessagesRunnable = new Runnable() {
        @Override
        public void run(){
            refreshMessages();
            mHandler.postDelayed(this, POLL_INTERVAL);
        }
    };

    @Override
    public void onStart(){
        super.onStart();
        Dialog dialog = getDialog();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_chat, container, true);
        threadId = getArguments().getString("threadId");

        setupMessagePosting(view);

        mHandler.postDelayed(mRefreshMessagesRunnable, POLL_INTERVAL);

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }


    void setupMessagePosting(View view) {
        etMessage = (EditText) view.findViewById(R.id.etMessage);
        btSend = (Button) view.findViewById(R.id.btSend);
        lvChat = (ListView) view.findViewById(R.id.lvChat);
        mMessages = new ArrayList<>();

        lvChat.setTranscriptMode(1);
        mFirstLoad = true;
        final String userId = ParseUser.getCurrentUser().getObjectId();
        mAdapter = new ChatMessageListAdapter(ParseProject.getContext(), mMessages);
        lvChat.setAdapter(mAdapter);

        //when user clicks
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = ParseUser.getCurrentUser().getObjectId();
                String body = etMessage.getText().toString();
                ChatManager.sendMessage(threadId, body, userId, new SuccessCB() {
                    @Override
                    public void done(String error, boolean success) {
                        if(success){
                            etMessage.setText(null);
                            refreshMessages();
                            Toast.makeText(ParseProject.getContext(), "success", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(ParseProject.getContext(), error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    void refreshMessages(){
        ChatManager.getMessages(threadId, new ListCB() {
            @Override
            public void done(String error, List<ParseObject> messages) {
                if(error == null){
                    mMessages.clear();
                    mMessages.addAll(messages);
                    mAdapter.notifyDataSetChanged();

                    if(mFirstLoad){
                        lvChat.setSelection(mAdapter.getCount() - 1);
                        mFirstLoad = false;
                    }
                }else{
                    Toast.makeText(ParseProject.getContext(), error, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}
