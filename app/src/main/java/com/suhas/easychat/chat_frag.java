package com.suhas.easychat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link chat_frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class chat_frag extends Fragment {



    public chat_frag() {

    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_chat_frag, container, false);
    }
}