package com.example.andrew.neighborlabour.UI.chat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.andrew.neighborlabour.ParseProject;
import com.example.andrew.neighborlabour.R;
import com.example.andrew.neighborlabour.Services.Utils.ListCB;
import com.example.andrew.neighborlabour.Services.chat.ChatManager;
import com.example.andrew.neighborlabour.Services.listings.ListingManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

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
                ChatDialogFragment chatDialog = new ChatDialogFragment();
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
