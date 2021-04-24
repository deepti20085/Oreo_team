package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.project.TimerFunc.ApkInfoExtractor;
import com.example.project.TimerFunc.AppsAdapter;
import com.example.project.TimerFunc.limitApp;
import com.example.project.broadcastreceivers.MyBroadcastReceivaer;

import java.util.ArrayList;
import java.util.List;

public class TimerActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    Button startService;
    List<limitApp> ApksListToLimit = new ArrayList<limitApp>();
    int j=0;
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String appname = intent.getStringExtra("appname");
            String packagename = intent.getStringExtra("packagename");
            String limit = intent.getStringExtra("limit");
            System.out.println("MAINACTIVITYYYYYY:  "+limit);
            ApksListToLimit.add(new limitApp(appname,packagename,limit));
            for(int i=0; i<ApksListToLimit.size();i++) {
                System.out.println(ApksListToLimit.get(i).getApkName() + ApksListToLimit.get(i).getPackageName() + ApksListToLimit.get(i).getLimit());
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer2);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        startService = (Button) findViewById(R.id.startService);
        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAlert(savedInstanceState);

            }
        });

        recyclerViewLayoutManager = new GridLayoutManager(TimerActivity.this, 1);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        adapter = new AppsAdapter(TimerActivity.this, new ApkInfoExtractor(TimerActivity.this).GetAllInstalledApkInfo());
        recyclerView.setAdapter(adapter);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("apps details"));

    }

    public void startAlert(Bundle savedInstanceState){

        int i=120;
        Intent intent = new Intent(getApplicationContext(), MyBroadcastReceivaer.class);

        intent.putExtra("size",ApksListToLimit.size());
        intent.putExtra("j",j);
        for (; j <ApksListToLimit.size();j++){
            System.out.println(ApksListToLimit.get(j).getApkName());
            intent.putExtra("name"+j,ApksListToLimit.get(j).getApkName());
            intent.putExtra("pkname"+j,ApksListToLimit.get(j).getPackageName());
            intent.putExtra("limit"+j,ApksListToLimit.get(j).getLimit());
        }
//        234324243
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(), 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ (i), pendingIntent);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+10,
                100,pendingIntent);
        Toast.makeText(this, "Alarm set in " + i + " seconds",Toast.LENGTH_LONG).show();
    }
}