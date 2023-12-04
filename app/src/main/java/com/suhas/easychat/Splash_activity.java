package com.suhas.easychat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.suhas.easychat.model.UserModel;
import com.suhas.easychat.utils.AndroidUtil;
import com.suhas.easychat.utils.FireBaseUtil;

public class Splash_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(FireBaseUtil.isLoggedIn() && getIntent().getExtras()!=null){
//            form Notification
            String UserId = getIntent().getExtras().getString("userId");
            FireBaseUtil.AllUsercollectionReference().document(UserId).get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            UserModel model = task.getResult().toObject(UserModel.class);

                            Intent MainIntent = new Intent(this, MainActivity.class);
                            MainIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(MainIntent);

                            Intent intent = new Intent(this, ChatActivity.class);
                            AndroidUtil.passUserModelAsIntent(intent,model);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    });
        }else{
            new Handler().postDelayed(() -> {
                if(FireBaseUtil.isLoggedIn()){
                    startActivity(new Intent(Splash_activity.this, MainActivity.class));
                }else{
                    startActivity(new Intent(getApplicationContext(),Login_phone_num.class));

                }
                finish();
            },1000);
        }


    }
}