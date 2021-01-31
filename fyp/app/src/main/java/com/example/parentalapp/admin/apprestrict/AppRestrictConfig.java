package com.example.parentalapp.admin.apprestrict;

import android.content.Context;
import android.content.SharedPreferences;

public class AppRestrictConfig {
    SharedPreferences sharedPreferences;
    Context context;

    public AppRestrictConfig(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("appPreference", context.MODE_PRIVATE);
    }

    public void changePermission(String appName, boolean allow){
        sharedPreferences.edit().putBoolean(appName, allow).apply();
    }
}
