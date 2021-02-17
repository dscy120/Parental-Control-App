package com.example.parentalapp.admin.apprestrict;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.ArrayList;
import java.util.List;

public class AppRestrictConfig {
    SharedPreferences sharedPreferences;
    Context context;

    public AppRestrictConfig(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("appPreference", context.MODE_PRIVATE);
    }

    public ArrayList<ResolveInfo> getApplicationListAll(){
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> pkgAppsList = pm.queryIntentActivities( mainIntent, 0);
        ArrayList<ResolveInfo>newList = new ArrayList<>();

        // Check for allowed apps
        for(ResolveInfo r : pkgAppsList){
            ActivityInfo a = r.activityInfo;
            String appname = pm.getApplicationLabel(a.applicationInfo).toString();
            if (sharedPreferences.getBoolean(appname, false)){
                newList.add(r);
            }
        }

        return newList;
    }

    public ArrayList<ResolveInfo> getApplicationList(boolean allow){
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> pkgAppsList = pm.queryIntentActivities( mainIntent, 0);
        ArrayList<ResolveInfo>newList = new ArrayList<>();

        // Check for allowed apps
        for(ResolveInfo r : pkgAppsList){
            ActivityInfo a = r.activityInfo;
            String appname = pm.getApplicationLabel(a.applicationInfo).toString();
            if (sharedPreferences.getBoolean(appname, false) == allow){
                newList.add(r);
            }
        }

        return newList;
    }

    public void changePermission(String appName, boolean allow){
        sharedPreferences.edit().putBoolean(appName, allow).apply();
    }
}
