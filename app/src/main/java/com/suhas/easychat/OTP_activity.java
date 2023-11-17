package com.suhas.easychat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.suhas.easychat.utils.AndroidUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class OTP_activity extends AppCompatActivity {
    String phone_number;
    String verificationCode;
    PhoneAuthProvider.ForceResendingToken resendingToken;

    FirebaseAuth mAuth;
    Long timeOutSeconds = 60L;
    EditText otp_input;
    Button next_button;
    TextView resend_otp;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        init();


        phone_number = getIntent().getExtras().getString("phone");
        sendOTP(phone_number,false);

        next_button.setOnClickListener(view -> {
            String entered_otp = otp_input.getText().toString();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode,entered_otp);
            signIn(credential);
            setInProgress(true);
        });

        resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOTP(phone_number,true);
            }
        });



    }
    public void init(){
        otp_input = findViewById(R.id.login_otp);
        next_button = findViewById(R.id.login_next_btn);
        resend_otp = findViewById(R.id.resend_otp);
        progressBar = findViewById(R.id.login_progress_bar_2);
        mAuth = FirebaseAuth.getInstance();
    }

    void sendOTP(String phoneNumber,boolean isResend){
        startResendTimer();
        setInProgress(true);
        PhoneAuthOptions.Builder builder = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(timeOutSeconds, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signIn(phoneAuthCredential);
                        setInProgress(false);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        AndroidUtil.showToast(getApplicationContext(),"OTP verfication Failed");
                        setInProgress(false);
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationCode = s;
                        resendingToken = forceResendingToken;
                        AndroidUtil.showToast(getApplicationContext(),"OTP sent Succesfully");
                        setInProgress(false);
                    }
                });

        if(isResend){
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());

        }else{
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }



    }
    void setInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            next_button.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            next_button.setVisibility(View.VISIBLE);
        }
    }
    void signIn(PhoneAuthCredential phoneAuthCredential){
//        login and go to next Activity
    setInProgress(true);
    mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            setInProgress(false);
            if(task.isSuccessful()){

                Intent intent = new Intent(OTP_activity.this,loginUserName.class);
                intent.putExtra("phone",phone_number);
                startActivity(intent);
            }else {
                AndroidUtil.showToast(getApplicationContext(),"OTP verifcation failed");
            }
        }
    });

    }
    void startResendTimer() {
        resend_otp.setEnabled(false);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                timeOutSeconds--;

                runOnUiThread(() -> {
                    resend_otp.setText("Resend Otp in " + timeOutSeconds + " seconds");

                    if (timeOutSeconds <= 0) {
                        timeOutSeconds = 60L;
                        timer.cancel();
                        resend_otp.setEnabled(true);
                    }
                });
            }
        }, 0, 1000);
    }




}