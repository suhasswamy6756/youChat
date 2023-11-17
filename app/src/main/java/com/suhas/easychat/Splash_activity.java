package com.suhas.easychat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.suhas.easychat.utils.FireBaseUtil;

public class Splash_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(FireBaseUtil.isLoggedIn()){
                    startActivity(new Intent(Splash_activity.this, MainActivity.class));
                }else{
                    startActivity(new Intent(getApplicationContext(),Login_phone_num.class));

                }
                finish();
            }
        },1000);
    }
}