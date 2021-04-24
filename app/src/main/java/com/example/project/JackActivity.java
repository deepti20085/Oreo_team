package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.project.JackFunc.mainF;

public class JackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jack);
        mainF s = new mainF();
        getSupportFragmentManager().beginTransaction().add(R.id.mainact,s).commit();
    }
}