package com.example.project.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.project.GestureActivity;
import com.example.project.JackActivity;
import com.example.project.R;
import com.example.project.SingleFragment;
import com.example.project.TimerActivity;
import com.example.project.driveUpload.photoUploader;
import com.example.project.greetingswisher.SmsWisher;

public class HomeFragment extends Fragment {

    private Button BtnGreetingWisher, BtnphotoUploader;
    private Button BtnTimer;
    private Button Btnges;
    private Button Btnjck;
    private HomeViewModel homeViewModel;
    Fragment fragment;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        //On click listener for Greeting wisher functionality
        BtnGreetingWisher=(Button) root.findViewById(R.id.Btn_greetingWisher);
        BtnGreetingWisher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new SmsWisher();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        //On click listener for photo upload to GDrive functionality
        BtnphotoUploader=(Button) root.findViewById(R.id.Btn_autoUpload);
        BtnphotoUploader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new photoUploader();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        BtnTimer=(Button) root.findViewById(R.id.Btn_timer);
        BtnTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(getActivity(), TimerActivity.class);
                startActivity(i1);
            }
        });

        Btnges=(Button) root.findViewById(R.id.btn_gesture);
        Btnges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(getActivity(), GestureActivity.class);
                startActivity(i1);
            }
        });

        Btnjck=(Button) root.findViewById(R.id.btn_jk);
        Btnjck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(getActivity(), JackActivity.class);
                startActivity(i1);
            }
        });

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
            }
        });
        return root;
    }

}