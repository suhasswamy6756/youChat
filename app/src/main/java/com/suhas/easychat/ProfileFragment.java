package com.suhas.easychat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.suhas.easychat.model.UserModel;
import com.suhas.easychat.utils.AndroidUtil;
import com.suhas.easychat.utils.FireBaseUtil;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class ProfileFragment extends Fragment {
    ImageView profilePic;
    EditText usernameInput, PhoneInput;
    Button updateProfile;
    ProgressBar progressBar;
    TextView logoutBtn;
    UserModel currentUsermodel;
    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri selectedimageUri;

    public ProfileFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result->{
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data!=null && data.getData()!=null){
                            selectedimageUri = data.getData();
                            AndroidUtil.setProfilePic(getContext(),selectedimageUri,profilePic);
                        }
                    }
                });
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
        logoutBtn.setOnClickListener(view12 -> {
            FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        FireBaseUtil.logout();
                        Intent intent = new Intent(getContext(),Splash_activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
            });

        });
        profilePic.setOnClickListener(view13 -> {
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512,512)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            imagePickLauncher.launch(intent);
                            return null;
                        }
                    });
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

        if(selectedimageUri!=null){
            FireBaseUtil.getCurrentProfilePicStorageRef().putFile(selectedimageUri)
                    .addOnCompleteListener(task -> {
                        UpdateToFireStore();
                    });

        }else{
            UpdateToFireStore();
        }
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

        FireBaseUtil.getCurrentProfilePicStorageRef().getDownloadUrl()
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                Uri uri = task.getResult();
                                AndroidUtil.setProfilePic(getContext(),uri,profilePic);
                            }
                        });
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