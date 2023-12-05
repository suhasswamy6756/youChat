package com.suhas.easychat;

import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Query;
import com.suhas.easychat.adapter.ChatRecyclerAdapter;
import com.suhas.easychat.model.ChatMessageModel;
import com.suhas.easychat.model.ChatRoomModel;
import com.suhas.easychat.model.UserModel;
import com.suhas.easychat.utils.AndroidUtil;
import com.suhas.easychat.utils.FireBaseUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {
    UserModel otherUsers;
    ImageButton back_button,message_send_btn;
    ChatRoomModel chatRoomModel;
    ChatRecyclerAdapter adapter;
    String chatRoomID;
//    ImageView profile_pic;
    EditText message_input;
    TextView otherUserName;
    RecyclerView recyclerView;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();

        otherUsers = AndroidUtil.getUserModelFrom_Intent(getIntent());
        chatRoomID = FireBaseUtil.getChatroomId(FireBaseUtil.currentUserId(),otherUsers.getUSerId());
        otherUserName.setText(otherUsers.getUsername());
        back_button.setOnClickListener(view -> onBackPressed());

        message_send_btn.setOnClickListener(view -> {
            String message = message_input.getText().toString().trim();
            if(message.isEmpty()) {
                return;
            }
            sendMessageToUser(message);
        });
        FireBaseUtil.getOtherProfilePicStorageRef(otherUsers.getUSerId()).getDownloadUrl()
                .addOnCompleteListener(t -> {
                    if(t.isSuccessful()){
                        Uri uri = t.getResult();
                        AndroidUtil.setProfilePic(this,uri,imageView);
                    }
                });

        getOrCreateChatroomModel();
        setUpChatRecyclerView();

    }
    void setUpChatRecyclerView(){
        Query query = FireBaseUtil.getChatRoomMessageReference(chatRoomID)
                .orderBy("timestamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ChatMessageModel> options = new FirestoreRecyclerOptions.Builder<ChatMessageModel>()
                .setQuery(query,ChatMessageModel.class).build();
        adapter = new ChatRecyclerAdapter(options,getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }
    void sendMessageToUser(String message){

        chatRoomModel.setLastmessageTimestamp(Timestamp.now());
        chatRoomModel.setLastMessageSenderId(FireBaseUtil.currentUserId());
        chatRoomModel.setLastMessage(message);
        FireBaseUtil.getChatroomReference(chatRoomID).set(chatRoomModel);

        ChatMessageModel chatMessageModel = new ChatMessageModel(Timestamp.now(),message,FireBaseUtil.currentUserId());
        FireBaseUtil.getChatRoomMessageReference(chatRoomID).add(chatMessageModel)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        message_input.setText(" ");
                        sendNotification(message);
                    }
                });
    }
    void getOrCreateChatroomModel(){
        FireBaseUtil.getChatroomReference(chatRoomID).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                chatRoomModel =task.getResult().toObject(ChatRoomModel.class);
                if(chatRoomModel==null){
//                    first time chat
                    chatRoomModel = new ChatRoomModel(
                            chatRoomID,
                            Arrays.asList(FireBaseUtil.currentUserId(),otherUsers.getUSerId()),
                            Timestamp.now(),
                            ""
                    );
                    FireBaseUtil.getChatroomReference(chatRoomID).set(chatRoomModel);
                }
            }
        });
    }
    void init(){
        message_input = findViewById(R.id.chat_message_input);
        otherUserName = findViewById(R.id.other_username);
        back_button = findViewById(R.id.back_btn);
        message_send_btn = findViewById(R.id.message_send_btn);
//        profile_pic = findViewById(R.id.profile_pic_layout);
        recyclerView = findViewById(R.id.chat_recycler_view);
        imageView = findViewById(R.id.profile_pic_image_view);
    }
    void sendNotification(String message){
        //current usernmae,message,currentUserid, otherusertoken
        FireBaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {

            if(task.isSuccessful()){
                UserModel currentUser =task.getResult().toObject(UserModel.class);

                try {
                    JSONObject jsonObject = new JSONObject();
                    JSONObject notificationObj = new JSONObject();

                    notificationObj.put("title",currentUser.getUsername());
                    notificationObj.put("body",message);

                    JSONObject dataObj = new JSONObject();
                    dataObj.put("userId",currentUser.getUSerId());

                    jsonObject.put("notification",notificationObj);
                    jsonObject.put("data",dataObj);
                    jsonObject.put("to",otherUsers.getFcmToken());
                    callApi(jsonObject);

                }catch(Exception e){

                }
            }

        });

    }
    void callApi(JSONObject jsonObject){
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        String url = "https://fcm.googleapis.com/fcm/send";
        RequestBody requestBody = RequestBody.create(jsonObject.toString(),JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .header("Authorization","Bearer API_KEY")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }
        });

    }



}
