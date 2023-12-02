package com.suhas.easychat;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.suhas.easychat.adapter.RecentChatRecyclerAdapter;
import com.suhas.easychat.adapter.SearchUserRecyclerAdapter;
import com.suhas.easychat.model.ChatRoomModel;
import com.suhas.easychat.model.UserModel;
import com.suhas.easychat.utils.FireBaseUtil;


public class ChatFragment extends Fragment {

    RecyclerView recyclerView;
    RecentChatRecyclerAdapter adapter;
    public ChatFragment() {

    }
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_chat_frag, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_chat_frag);
        setUpRecyclerView();
        return view;
    }

    void setUpRecyclerView(){

        Query query = FireBaseUtil.allChatRoomCollectionReference()
                .whereArrayContains("userIds",FireBaseUtil.currentUserId())
                .orderBy("lastmessageTimestamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ChatRoomModel> options = new FirestoreRecyclerOptions.Builder<ChatRoomModel>()
                .setQuery(query,ChatRoomModel.class).build();
        adapter = new RecentChatRecyclerAdapter(options,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }


    @Override
    public void onStart() {
        super.onStart();
        if(adapter!=null){
            adapter.startListening();
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if(adapter!=null){
            adapter.stopListening();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter!=null){
            adapter.startListening();
        }
    }
}