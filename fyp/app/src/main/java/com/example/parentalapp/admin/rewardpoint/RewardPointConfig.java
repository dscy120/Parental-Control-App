package com.example.parentalapp.admin.rewardpoint;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class RewardPointConfig {
    private SharedPreferences sharedPreferences;

    public static final String REWARD_POINTS = "reward_points";

    public RewardPointConfig(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public int getCurrentPoints(){
        return sharedPreferences.getInt(REWARD_POINTS, 0);
    }

    public void setRewardPoints(int points){
        sharedPreferences.edit().putInt(REWARD_POINTS, points).apply();
    }

    public void addRewardPoints(int points){
        sharedPreferences.edit().putInt(REWARD_POINTS, getCurrentPoints() + points).apply();
    }

    public void deductRewardPoints(int points){
        sharedPreferences.edit().putInt(REWARD_POINTS, getCurrentPoints() - points).apply();
    }

}
