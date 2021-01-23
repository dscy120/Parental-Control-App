package com.example.parentalapp.reward;

import android.content.Context;
import android.database.Cursor;

import com.example.parentalapp.database.DatabaseOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class RewardDBHelper extends DatabaseOpenHelper {

    public RewardDBHelper(Context context) {
        super(context);
    }

    public ArrayList<RewardItem> getRewardItem(){
        ArrayList<RewardItem> rewardItemList = new ArrayList<>();
        String sql = "SELECT * FROM reward_item";
        Cursor c = super.query(sql);

        if(c.moveToFirst()){
            do{
                int id = c.getInt(0);
                String name = c.getString(1);
                int point = c.getInt(2);
                RewardItem rewardItem = new RewardItem(id, name, point);
                rewardItemList.add(rewardItem);
            }while (c.moveToNext());
        }
        c.close();

        return rewardItemList;
    }
}
