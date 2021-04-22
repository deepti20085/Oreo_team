package com.example.project.Gesture;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.Services.camService;

import static androidx.core.app.ActivityCompat.requestPermissions;

public class cam extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);
        Button bStart=(Button) findViewById(R.id.cameraon);
        Button bStop=(Button) findViewById(R.id.cameraoff);
        bStart.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(v.getContext(), camService.class);
                if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.CAMERA) !=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) v.getContext(), new String[] {Manifest.permission.CAMERA},
                            50); }

                startService(intent1);

            }
        });
        bStop.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(v.getContext(), camService.class);
                stopService(intent1); }
        });

    }
}