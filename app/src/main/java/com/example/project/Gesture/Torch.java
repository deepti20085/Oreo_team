package com.example.project.Gesture;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.project.R;

public class Torch extends AppCompatActivity {
    boolean isON=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_torch);
        Button bStart=(Button) findViewById(R.id.torchg);
        Button bStop=(Button) findViewById(R.id.torchgs);
        bStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(v.getContext(), shakedetection.class);
                startService(intent1); }
        });
        bStop.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(v.getContext(), shakedetection.class);
                stopService(intent1); }
        });

    }
    public void ToggleTorch(View v) throws CameraAccessException {
        Button b1=(Button)v;
        if(b1.getText().toString().equals("Torch On")){
            b1.setText("Torch Off");
            tog("On");
        }
        else {
            b1.setText("Torch On");
            tog("Off");
        }
    }

    private void tog(String ty) throws CameraAccessException {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            CameraManager cameraManager=(CameraManager) getSystemService(Context.CAMERA_SERVICE);
            String cid=null;
            if(cameraManager != null){
                cid=cameraManager.getCameraIdList()[0];
            }
            if (cameraManager != null){
                if(ty.equals("On")){
                    cameraManager.setTorchMode(cid,true);
                    isON=true;
                }
                else{
                    cameraManager.setTorchMode(cid,false);
                    isON=false;
                }

            }
        }
    }
}