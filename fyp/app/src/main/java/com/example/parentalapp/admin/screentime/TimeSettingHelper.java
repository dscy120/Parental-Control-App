package com.example.parentalapp.admin.screentime;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class TimeSettingHelper {

    public static final String TIME = "screenTime";
    public static final String ACTIVE_HOUR_START = "Active_Hour_Start";
    public static final String ACTIVE_MINUTE_START = "Active_Minute_Start";
    public static final String ACTIVE_HOUR_END = "Active_Hour_End";
    public static final String ACTIVE_MINUTE_END = "Active_Minute_End";

    private SharedPreferences sharedPreferences;

    public TimeSettingHelper(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public long getRemainingTime(){
        return sharedPreferences.getLong(TIME, 0);
    }

    public void setRemainingScreenTime(long remainingScreenTime){
        sharedPreferences.edit().putLong(TIME, remainingScreenTime).apply();
    }

    public int convertHour(Long remainingTime){
        return (int) (remainingTime / (60 * 60));
    }

    public int convertMinute(Long remainingTime){
        return (int)((remainingTime % (60 * 60)) / 60);
    }

    public String getActiveHourStartString(){
        int hour = sharedPreferences.getInt(ACTIVE_HOUR_START, 0);
        int minute = sharedPreferences.getInt(ACTIVE_MINUTE_START, 0);
        String time = String.format("%02d", hour)+ " : " + String.format("%02d", minute);
        return time;
    }

    public String getActiveHourEndString(){
        int hour = sharedPreferences.getInt(ACTIVE_HOUR_END, 0);
        int minute = sharedPreferences.getInt(ACTIVE_MINUTE_END, 0);
        String time = String.format("%02d", hour)+ " : " + String.format("%02d", minute);
        return time;
    }

    public int getActiveHourStart(){
        return sharedPreferences.getInt(ACTIVE_HOUR_START, 0);
    }

    public void setActiveHourStart(int hourStart){
        sharedPreferences.edit().putInt(ACTIVE_HOUR_START, hourStart).apply();
    }

    public int getActiveHourEnd(){
        return sharedPreferences.getInt(ACTIVE_HOUR_END, 0);
    }

    public void setActiveHourEnd(int hourEnd){
        sharedPreferences.edit().putInt(ACTIVE_HOUR_END, hourEnd).apply();
    }

    public int getActiveMinuteStart(){
        return sharedPreferences.getInt(ACTIVE_MINUTE_START, 0);
    }

    public void setActiveMinuteStart(int minuteStart){
        sharedPreferences.edit().putInt(ACTIVE_MINUTE_START, minuteStart).apply();
    }

    public int getActiveMinuteEnd(){
        return sharedPreferences.getInt(ACTIVE_MINUTE_END, 0);
    }

    public void setActiveMinuteEnd(int minuteEnd){
        sharedPreferences.edit().putInt(ACTIVE_MINUTE_END, minuteEnd).apply();
    }
}
