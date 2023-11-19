package com.suhas.easychat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.suhas.easychat.model.UserModel;
import com.suhas.easychat.utils.AndroidUtil;
import com.suhas.easychat.utils.FireBaseUtil;

import java.util.HashMap;
import java.util.Map;

public class loginUserName extends AppCompatActivity {
    ProgressBar progressBar;
    Button LetMein;
    EditText UserName;
    String phoneNumber;
    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user_name);
        init();

        phoneNumber = getIntent().getExtras().getString("phone");
        getUsername();
        LetMein.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUserName();
            }
        });


    }
    void init(){
        UserName = findViewById(R.id.login_username);
        LetMein = findViewById(R.id.let_me_in);
        progressBar = findViewById(R.id.login_progress_bar_3);
    }
    void setInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            LetMein.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            LetMein.setVisibility(View.VISIBLE);
        }

    }
    void setUserName(){
        String username = UserName.getText().toString();
        if(username.isEmpty() || username.length()<3){
            UserName.setError("username lenght should be >3");
            return;
        }
        setInProgress(true);

        if(userModel!=null){
            userModel.setUsername(username);
        }else{
            userModel = new UserModel(phoneNumber,username, Timestamp.now(),FireBaseUtil.currentUserId());
        }

        FireBaseUtil.currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setInProgress(false);
                if(task.isSuccessful()){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
    }
    void getUsername(){
        setInProgress(true);
        FireBaseUtil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                setInProgress(false);
                if(task.isSuccessful()){
                     userModel= task.getResult().toObject(UserModel.class);
                    if(userModel!=null){
                        UserName.setText(userModel.getUsername());
                    }
                }
            }
        });
    }
}