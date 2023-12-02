package com.suhas.easychat.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.suhas.easychat.model.UserModel;

public class AndroidUtil {
    public static void showToast(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
    public static void passUserModelAsIntent(Intent intent, UserModel model){
        intent.putExtra("username",model.getUsername());
        intent.putExtra("phone",model.getPhone());
        intent.putExtra("userId",model.getUSerId());
    }
    public static UserModel getUserModelFrom_Intent(Intent intent){
        UserModel userModel = new UserModel();
        userModel.setUsername(intent.getStringExtra("username"));
        userModel.setPhone(intent.getStringExtra("phone"));
        userModel.setUSerId(intent.getStringExtra("userId"));
        return userModel;
    }
}
