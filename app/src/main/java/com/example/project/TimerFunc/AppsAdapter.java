package com.example.project.TimerFunc;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;

import java.util.ArrayList;
import java.util.List;

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.ViewHolder>{

    Context context1;
    List<String> stringList;

    public AppsAdapter(Context context, List<String> list){

        context1 = context;

        stringList = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public CardView cardView;
        public ImageView imageView;
        public TextView textView_App_Name;
        public TextView textView_App_Package_Name;
        public TextView textView_App_Time;
        public Button limitButton;

        public ViewHolder (View view){

            super(view);

            cardView = (CardView) view.findViewById(R.id.card_view);
            imageView = (ImageView) view.findViewById(R.id.imageview);
            textView_App_Name = (TextView) view.findViewById(R.id.Apk_Name);
            textView_App_Time =(TextView) view.findViewById(R.id.Apk_Time);
            textView_App_Package_Name = (TextView) view.findViewById(R.id.Apk_Package_Name);
            limitButton = (Button) view.findViewById(R.id.limit);
        }
    }

    @Override
    public AppsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view2 = LayoutInflater.from(context1).inflate(R.layout.cardview_layout,parent,false);

        ViewHolder viewHolder = new ViewHolder(view2);

        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position){

        ApkInfoExtractor apkInfoExtractor = new ApkInfoExtractor(context1);

        final String ApplicationPackageName = (String) stringList.get(position);
        String ApplicationLabelName = apkInfoExtractor.GetAppName(ApplicationPackageName);
        // System.out.println(ApplicationLabelName);
        String ti=apkInfoExtractor.GetAppTime(ApplicationPackageName);
        Drawable drawable = apkInfoExtractor.getAppIconByPackageName(ApplicationPackageName);

        viewHolder.textView_App_Name.setText(ApplicationLabelName);

        viewHolder.textView_App_Package_Name.setText(ApplicationPackageName);

        viewHolder.imageView.setImageDrawable(drawable);
        viewHolder.textView_App_Time.setText(ti);
        viewHolder.limitButton.setOnClickListener(new View.OnClickListener() {
            int t1hour,t1min;
            long timili;
            @Override
            public void onClick(View v) {
                TimePickerDialog tpd = new TimePickerDialog(context1, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        t1hour = hourOfDay;
                        t1min = minute;

                        Intent intent = new Intent("apps details");
                        intent.putExtra("appname",viewHolder.textView_App_Name.getText().toString());
                        intent.putExtra("packagename",viewHolder.textView_App_Package_Name.getText().toString());
                        timili = t1hour*60*60*1000+t1min*60*1000;
                        System.out.println("APPADAPTERRRRR:  "+timili);
                        intent.putExtra("limit",String.valueOf(timili));
                        LocalBroadcastManager.getInstance(context1).sendBroadcast(intent);
                    }
                },24,0,true);
                tpd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                tpd.updateTime(t1hour,t1min);
                tpd.show();
                //ApksListToLimit.add(new limitApp(viewHolder.textView_App_Name.getText().toString(),viewHolder.textView_App_Package_Name.getText().toString(),"5"));
                //System.out.println(ApksListToLimit.get(0).ApkName +ApksListToLimit.get(0).PackageName +ApksListToLimit.get(0).limit );
            }
        });

        //Adding click listener on CardView to open clicked application directly from here .
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = context1.getPackageManager().getLaunchIntentForPackage(ApplicationPackageName);
                if(intent != null){

                    context1.startActivity(intent);

                }
                else {

                    Toast.makeText(context1,ApplicationPackageName + " Error, Please Try Again.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount(){

        return stringList.size();
    }
}
