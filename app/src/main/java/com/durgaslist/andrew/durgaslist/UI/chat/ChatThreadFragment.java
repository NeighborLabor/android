package com.durgaslist.andrew.durgaslist.UI.chat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.durgaslist.andrew.durgaslist.ParseProject;
import com.andrew.durgaslist.R;
import com.durgaslist.andrew.durgaslist.Services.Utils.ListCB;
import com.durgaslist.andrew.durgaslist.Services.chat.ChatManager;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

public class ChatThreadFragment extends Fragment {

    private static final String TAG = "ChatThreadFragment";

    ListView lvThreads;
    static ChatThreadArrayAdapter threadAdapter;
    static ArrayList<ParseObject> threads = new ArrayList<ParseObject>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstance) {
        return inflater.inflate(R.layout.fragment_chat_threads, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        setupThreadArrayAdapter();
        refresh();
    }

    private void setupThreadArrayAdapter() {
        lvThreads = (ListView) getView().findViewById(R.id.lvThreads);
        lvThreads.setTranscriptMode(1);
        threadAdapter = new ChatThreadArrayAdapter(ParseProject.getContext(), (ArrayList<ParseObject>) threads);
        lvThreads.setAdapter(threadAdapter);
        lvThreads.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int i, long arg3) {
                ChatMessageDialogFragment chatDialog = new ChatMessageDialogFragment();
                String threadId = threads.get(i).getObjectId();
                Bundle args = new Bundle();
                args.putString("threadId", threadId );
                chatDialog.setArguments(args);
                chatDialog.show(getActivity().getFragmentManager(), "NoticeDialogFragment");
            }
        });
    }

    public static void refresh(){
        ChatManager.getChatThreads(new ListCB() {
            @Override
            public void done(String error, List<ParseObject> newThreads) {
                Log.i(TAG, "Listing Adapter Setup");
                threads.clear();
                threads.addAll(newThreads);
                threadAdapter.notifyDataSetChanged();
            }
        });
    }


}
