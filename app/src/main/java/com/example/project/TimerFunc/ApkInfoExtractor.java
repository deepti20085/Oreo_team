package com.example.project.TimerFunc;

import android.annotation.SuppressLint;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.os.Build;
import android.provider.Settings;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.example.project.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class ApkInfoExtractor {

    Context context1;
    private UsageStatsManager mUsageStatsManager;
    public ApkInfoExtractor(Context context2){

        context1 = context2;
    }

    public List<String> GetAllInstalledApkInfo(){

        List<String> ApkPackageName = new ArrayList<>();

        Intent intent = new Intent(Intent.ACTION_MAIN,null);

        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED );

        List<ResolveInfo> resolveInfoList = context1.getPackageManager().queryIntentActivities(intent,0);

        for(ResolveInfo resolveInfo : resolveInfoList){

            ActivityInfo activityInfo = resolveInfo.activityInfo;

            if(!isSystemPackage(resolveInfo)){

                ApkPackageName.add(activityInfo.applicationInfo.packageName);
            }
        }
//System.out.println("list "+ApkPackageName);
        return ApkPackageName;

    }

    public boolean isSystemPackage(ResolveInfo resolveInfo){

        return ((resolveInfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    public Drawable getAppIconByPackageName(String ApkTempPackageName){

        Drawable drawable;

        try{
            drawable = context1.getPackageManager().getApplicationIcon(ApkTempPackageName);

        }
        catch (PackageManager.NameNotFoundException e){

            e.printStackTrace();

            drawable = ContextCompat.getDrawable(context1, R.mipmap.ic_launcher);
        }
        return drawable;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public String GetAppTime(String ApkPackageName){
        StringBuilder t = new StringBuilder("");
        //DateUtils t=new DateUtils();
        @SuppressLint("WrongConstant") UsageStatsManager mUsageStatsManager = (UsageStatsManager) context1.getSystemService("usagestats");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        long Time = System.currentTimeMillis();
        ApplicationInfo applicationInfo;
        PackageManager packageManager = context1.getPackageManager();
        List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST,
                cal.getTimeInMillis(), System.currentTimeMillis());
        boolean isEmpty = stats.isEmpty();
        if (isEmpty) {
            context1.startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }
        try {
            applicationInfo = packageManager.getApplicationInfo(ApkPackageName, 0);
            if(applicationInfo!=null){
                if(stats != null)
                {
                    final int statCount = stats.size();
                    for (int i = 0; i < statCount; i++) {

                        if (stats.get(i).getPackageName().equals(ApkPackageName)) {
                            long TimeInforground = 500;

                            int minutes = 500, seconds = 500, hours = 500;

                            TimeInforground = stats.get(i).getTotalTimeInForeground();

                            minutes = (int) ((TimeInforground / (1000 * 60)) % 60);

                            seconds = (int) (TimeInforground / 1000) % 60;

                            hours = (int) ((TimeInforground / (1000 * 60 * 60)) % 24);

                            Log.i("BAC", "PackageName is" + stats.get(i).getPackageName() + "Time is: " + hours + "h" + ":" + minutes + "m" + seconds + "s");
                            t.append(hours);
                            t.append(":");
                            t.append(minutes);
                            t.append(":");
                            t.append(seconds);
                            System.out.println(t);
                            break;
                        }

                    }

                }}

        }catch (PackageManager.NameNotFoundException e1) {
            // Log.i("alerr","i m here 5");
            e1.printStackTrace();
        }
        return String.valueOf(t);
    }

    public String GetAppName(String ApkPackageName){

        String Name = "";

        ApplicationInfo applicationInfo;

        PackageManager packageManager = context1.getPackageManager();

        try {

            applicationInfo = packageManager.getApplicationInfo(ApkPackageName, 0);

            if(applicationInfo!=null){

                Name = (String)packageManager.getApplicationLabel(applicationInfo);

                //Toast.makeText(context1,"i used "+ us.getTotalTimeInForeground(),Toast.LENGTH_SHORT).show();

            }

        }catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();
        }
        return Name;
    }
}