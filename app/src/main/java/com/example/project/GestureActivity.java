package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.project.Gesture.Torch;
import com.example.project.Gesture.cam;
import com.example.project.Gesture.doubletap;

public class GestureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture);
        Button bneto=(Button) findViewById(R.id.torchon);
        bneto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(GestureActivity.this, Torch.class);
                startActivity(intent1);
            }
        });
        Button bneto1=(Button) findViewById(R.id.lockon);
        bneto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(GestureActivity.this, doubletap.class);
                startActivity(intent1);
            }
        });
        Button bneto2=(Button) findViewById(R.id.cam);
        bneto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(GestureActivity.this, cam.class);
                startActivity(intent1);
            }
        });

    }
}