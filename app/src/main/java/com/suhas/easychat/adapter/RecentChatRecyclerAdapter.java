package com.suhas.easychat.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.suhas.easychat.ChatActivity;
import com.suhas.easychat.R;
import com.suhas.easychat.model.ChatRoomModel;
import com.suhas.easychat.model.UserModel;
import com.suhas.easychat.utils.AndroidUtil;
import com.suhas.easychat.utils.FireBaseUtil;

public class RecentChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatRoomModel, RecentChatRecyclerAdapter.ChatRoomModelViewHolder> {

    Context context;
    public RecentChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatRoomModel> options,Context context) {
        super(options);
        this.context=context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull ChatRoomModelViewHolder holder, int position, @NonNull ChatRoomModel model) {
        FireBaseUtil.getOtherUserFromChatRoom(model.getUserIds())
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        UserModel otherUserModel = task.getResult().toObject(UserModel.class);
                        holder.UserName_text.setText(otherUserModel.getUsername());
                        holder.last_message_text.setText(model.getLastMessage());
                        holder.lastMessageTime.setText(FireBaseUtil.timeStampToString(model.getLastmessageTimestamp()));
                    }
                });
    }

    @NonNull
    @Override
    public ChatRoomModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recent_chat_recycler,parent,false);

        return new ChatRoomModelViewHolder(view);
    }

    class ChatRoomModelViewHolder extends RecyclerView.ViewHolder{
        TextView UserName_text;
        TextView last_message_text;
        TextView lastMessageTime;
        ImageView ProfilePic;
        public ChatRoomModelViewHolder(@NonNull View itemView) {
            super(itemView);
            UserName_text = itemView.findViewById(R.id.username_text);
            last_message_text = itemView.findViewById(R.id.last_message_text);
            lastMessageTime = itemView.findViewById(R.id.last_message_time_text);
            ProfilePic=itemView.findViewById(R.id.profile_pic_image_view);
        }
    }
}