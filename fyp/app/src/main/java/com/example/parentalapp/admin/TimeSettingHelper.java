package com.example.parentalapp.admin;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class TimeSettingHelper {

    public static final String ACTIVE_HOUR_START = "Active_Hour_Start";
    public static final String ACTIVE_MINUTE_START = "Active_Minute_Start";
    public static final String ACTIVE_HOUR_END = "Active_Hour_End";
    public static final String ACTIVE_MINUTE_END = "Active_Minute_End";

    private SharedPreferences sharedPreferences;

    public TimeSettingHelper(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public int convertHour(Long remainingTime){
        return (int) (remainingTime / (60 * 60));
    }

    public int convertMinute(Long remainingTime){
        return (int)((remainingTime % (60 * 60)) / 60);
    }

    public String getActiveHourStart(){
        int hour = sharedPreferences.getInt(ACTIVE_HOUR_START, 0);
        int minute = sharedPreferences.getInt(ACTIVE_MINUTE_START, 0);
        String time = String.format("%02d", hour)+ " : " + String.format("%02d", minute);
        return time;
    }

    public String getActiveHourEnd(){
        int hour = sharedPreferences.getInt(ACTIVE_HOUR_END, 0);
        int minute = sharedPreferences.getInt(ACTIVE_MINUTE_END, 0);
        String time = String.format("%02d", hour)+ " : " + String.format("%02d", minute);
        return time;
    }
}
