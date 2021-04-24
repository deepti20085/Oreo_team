package com.example.project.JackFunc;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.Services.MyService;
import com.example.project.TimerFunc.ApkInfoExtractor;

import java.util.List;

public class AppAdap extends RecyclerView.Adapter<AppAdap.ViewHolder> {

    Context context1;
    List<String> stringList;
    public AppAdap(Context context, List<String> list){

        context1 = context;

        stringList = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public CardView cardView;
        public ImageView imageView;
        public TextView textView_App_Name;
        public TextView textView_App_Package_Name;

        public ViewHolder (View view){

            super(view);

            cardView = (CardView) view.findViewById(R.id.one_app);
            imageView = (ImageView) view.findViewById(R.id.app_icon);
            textView_App_Name = (TextView) view.findViewById(R.id.app_name);
//            textView_App_Time =(TextView) view.findViewById(R.id.Apk_Time);
//            textView_App_Package_Name = (TextView) view.findViewById(R.id.Apk_Package_Name);
//            limitButton = (Button) view.findViewById(R.id.limit);
        }
    }

    @Override
    public AppAdap.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view2 = LayoutInflater.from(context1).inflate(R.layout.one_app,parent,false);

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
//        String ti=apkInfoExtractor.GetAppTime(ApplicationPackageName);
        Drawable drawable = apkInfoExtractor.getAppIconByPackageName(ApplicationPackageName);

        viewHolder.textView_App_Name.setText(ApplicationLabelName);

//        viewHolder.textView_App_Package_Name.setText(ApplicationPackageName);

        viewHolder.imageView.setImageDrawable(drawable);
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context1,ApplicationLabelName, Toast.LENGTH_LONG).show();
                Intent i1 = new Intent(context1, MyService.class);
                i1.putExtra("App",ApplicationPackageName);
                context1.startService(i1);
//                    Bundle bun = new Bundle();
//                    bun.putCharSequence("App",ApplicationLabelName);
//                    mainF fragobj = new mainF();
//                    fragobj.setArguments(bun);
//                FragmentManager manager=context1.getActivity.getFragmentManager();
//                FragmentTransaction transaction=manager.beginTransaction();
//                transaction.replace(,fragmentB).commit();
//                }
            }
        });
    }

    @Override
    public int getItemCount(){

        return stringList.size();
    }
}



