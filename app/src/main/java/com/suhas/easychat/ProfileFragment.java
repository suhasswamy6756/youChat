package com.suhas.easychat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.suhas.easychat.model.UserModel;
import com.suhas.easychat.utils.AndroidUtil;
import com.suhas.easychat.utils.FireBaseUtil;


public class ProfileFragment extends Fragment {
    ImageView profilePic;
    EditText usernameInput, PhoneInput;
    Button updateProfile;
    ProgressBar progressBar;
    TextView logoutBtn;
    UserModel currentUsermodel;


    public ProfileFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profilePic = view.findViewById(R.id.profile_image_view);
        usernameInput = view.findViewById(R.id.profile_username);
        PhoneInput = view.findViewById(R.id.profile_phoneNumber);
        updateProfile = view.findViewById(R.id.profile_update_btn);
        progressBar = view.findViewById(R.id.profile_progress_bar);
        logoutBtn = view.findViewById(R.id.logout_btn);

        getUserData();
        updateProfile.setOnClickListener(view1 -> {
            updateBtnClick();
        });
        return view;
    }
    void updateBtnClick(){
        String NewUsername = usernameInput.getText().toString();
        if(NewUsername.isEmpty() || NewUsername.length()<3){
            usernameInput.setError("username lenght should be >3");
            return;
        }
        currentUsermodel.setUsername(NewUsername);
        setInProgress(true);
        UpdateToFireStore();
    }
    void UpdateToFireStore(){
        FireBaseUtil.currentUserDetails().set(currentUsermodel)
                .addOnCompleteListener(task -> {
                    setInProgress(false);
                    if(task.isSuccessful()){
                        AndroidUtil.showToast(getContext(),"Updated Succesfully");
                    }else{

                        AndroidUtil.showToast(getContext(),"Something went Wrong");

                    }
                });
    }
    void getUserData(){
        setInProgress(true);
        FireBaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            setInProgress(false);
            currentUsermodel = task.getResult().toObject(UserModel.class);
            usernameInput.setText(currentUsermodel.getUsername());
            PhoneInput.setText(currentUsermodel.getPhone());

        });
    }
    void setInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            updateProfile.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            updateProfile.setVisibility(View.VISIBLE);
        }

    }
}