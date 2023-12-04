package com.suhas.easychat.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.suhas.easychat.ChatActivity;
import com.suhas.easychat.R;
import com.suhas.easychat.model.UserModel;
import com.suhas.easychat.utils.AndroidUtil;
import com.suhas.easychat.utils.FireBaseUtil;

public class SearchUserRecyclerAdapter extends FirestoreRecyclerAdapter<UserModel, SearchUserRecyclerAdapter.UserModeViewHolder> {

    Context context;
    public SearchUserRecyclerAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options,Context context) {
        super(options);
        this.context=context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull UserModeViewHolder holder, int position, @NonNull UserModel model) {
        holder.UserName_text.setText(model.getUsername());
        holder.phone_text.setText(model.getPhone());
        if(model.getUSerId()!=null && model.getUSerId().equals(FireBaseUtil.currentUserId())){
            holder.UserName_text.setText(model.getUsername()+"(Me)");

        }

        FireBaseUtil.getOtherProfilePicStorageRef(model.getUSerId()).getDownloadUrl()
                .addOnCompleteListener(t -> {
                    if(t.isSuccessful()){
                        Uri uri = t.getResult();
                        AndroidUtil.setProfilePic(context,uri,holder.ProfilePic);
                    }
                });

        holder.itemView.setOnClickListener(view -> {
//            navigate to chat Activity...
            Intent intent = new Intent(context, ChatActivity.class);
            AndroidUtil.passUserModelAsIntent(intent,model);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public UserModeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_user_recyler_view,parent,false);

        return new UserModeViewHolder(view);
    }

    class UserModeViewHolder extends RecyclerView.ViewHolder{
        TextView UserName_text;
        TextView phone_text;
        ImageView ProfilePic;
        public UserModeViewHolder(@NonNull View itemView) {
            super(itemView);
            UserName_text = itemView.findViewById(R.id.username_text);
            phone_text = itemView.findViewById(R.id.phone_text);
            ProfilePic=itemView.findViewById(R.id.profile_pic_image_view);
        }
    }
}
