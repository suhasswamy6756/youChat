package com.suhas.easychat;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.hbb20.CountryCodePicker;

public class Login_phone_num extends AppCompatActivity {

    CountryCodePicker countryCodePicker;
    EditText phoneInput;
    Button sendOTP;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone_num);

        countryCodePicker = findViewById(R.id.login_counter_code);
        sendOTP = findViewById(R.id.send_otp_btn);
        phoneInput = findViewById(R.id.login_mobile_number);
        progressBar = findViewById(R.id.login_progress_bar_1);

        countryCodePicker.registerCarrierNumberEditText(phoneInput);

        progressBar.setVisibility(View.GONE);

        sendOTP.setOnClickListener(new View.OnClickListener() {
            //            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(!countryCodePicker.isValidFullNumber()){
                    phoneInput.setText("phone Number not valid");
                    return;
                }
                Intent intent = new Intent(getApplicationContext(), OTP_activity.class);
                intent.putExtra("phone",countryCodePicker.getFullNumberWithPlus());
                startActivity(intent);
            }
        });

    }
}