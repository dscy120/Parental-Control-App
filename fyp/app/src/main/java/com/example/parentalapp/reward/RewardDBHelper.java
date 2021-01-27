package com.example.parentalapp.reward;

import android.content.Context;
import android.database.Cursor;

import com.example.parentalapp.database.DatabaseOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class RewardDBHelper extends DatabaseOpenHelper {

    public static final String REWARD_ITEM_ID = "id";
    final public static String REWARD_ITEM_NAME = "name";
    final public static String REWARD_ITEM_POINT = "point";
    final public static String REWARD_EFFECT_ITEM = "effect_item";
    final public static String REWARD_EFFECT_VALUE = "effect_value";

    public RewardDBHelper(Context context) {
        super(context);
    }

    public ArrayList<RewardItem> getRewardItem(){
        ArrayList<RewardItem> rewardItemList = new ArrayList<>();
        String sql = "SELECT * FROM reward_item";
        Cursor c = super.query(sql);

        if(c.moveToFirst()){
            do{
                int id = c.getInt(c.getColumnIndex(REWARD_ITEM_ID));
                String name = c.getString(1);
                int point = c.getInt(2);
                RewardItem rewardItem = new RewardItem(id, name, point);
                rewardItemList.add(rewardItem);
            }while (c.moveToNext());
        }
        c.close();

        return rewardItemList;
    }

    public Cursor getItemDetail(String condition, String item){
        String sql = "SELECT * FROM reward_item WHERE " + condition + "=" + item;
        return super.query(sql);
    }
}
