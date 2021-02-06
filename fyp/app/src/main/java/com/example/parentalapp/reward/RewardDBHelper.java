package com.example.parentalapp.reward;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.parentalapp.database.DatabaseOpenHelper;

import java.util.ArrayList;

public class RewardDBHelper extends DatabaseOpenHelper {

    public static final String REWARD_ITEM_TABLE = "reward_item";
    public static final String REWARD_ITEM_ID = "id";
    public static final String REWARD_ITEM_NAME = "name";
    public static final String REWARD_ITEM_POINT = "point";
    public static final String REWARD_EFFECT_ITEM = "effect_item";
    public static final String REWARD_EFFECT_VALUE = "effect_value";

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
                String effectItem = c.getString(3);
                String effectValue = c.getString(4);
                RewardItem rewardItem = new RewardItem(id, name, point, effectItem, effectValue);
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

    public boolean addItem(String name, int point, String effectItem, String effectValue){
        ContentValues cv = new ContentValues();
        cv.put(REWARD_ITEM_NAME, name);
        cv.put(REWARD_ITEM_POINT, point);
        cv.put(REWARD_EFFECT_ITEM, effectItem);
        cv.put(REWARD_EFFECT_VALUE, effectValue);
        return super.insertSQL(REWARD_ITEM_TABLE, cv);
    }

    public boolean deleteItem(int id){
        return super.deleteSQL(REWARD_ITEM_TABLE, REWARD_ITEM_ID, String.valueOf(id));
    }
}
