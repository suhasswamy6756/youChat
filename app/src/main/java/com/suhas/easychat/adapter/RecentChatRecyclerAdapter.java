package com.suhas.easychat.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
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
        Log.d("Adapter", "onBindViewHolder called for position: " + position);

            FireBaseUtil.getOtherUserFromChatRoom(model.getUserIds())
                    .get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            boolean lastMessageSendByMe = model.getLastMessageSenderId().equals(FireBaseUtil.currentUserId());


                            UserModel otherUserModel = task.getResult().toObject(UserModel.class);

                            FireBaseUtil.getOtherProfilePicStorageRef(otherUserModel.getUSerId()).getDownloadUrl()
                                    .addOnCompleteListener(t -> {
                                        if(t.isSuccessful()){
                                             Uri uri = t.getResult();
                                            AndroidUtil.setProfilePic(context,uri,holder.ProfilePic);
                                        }
                                    });

                            holder.UserName_text.setText(otherUserModel.getUsername());
                            if(lastMessageSendByMe)
                                holder.last_message_text.setText("You: "+model.getLastMessage());
                            else
                                holder.last_message_text.setText(model.getLastMessage());
                            holder.lastMessageTime.setText(FireBaseUtil.timeStampToString(model.getLastmessageTimestamp()));
                            holder.itemView.setOnClickListener(view -> {
//
                                Intent intent = new Intent(context, ChatActivity.class);
                                AndroidUtil.passUserModelAsIntent(intent,otherUserModel);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            });
                        }
                    });

    }

    @NonNull
    @Override
    public ChatRoomModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recent_chat_recycler,parent,false);

        return new ChatRoomModelViewHolder(view);
    }

    static class ChatRoomModelViewHolder extends RecyclerView.ViewHolder{
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
