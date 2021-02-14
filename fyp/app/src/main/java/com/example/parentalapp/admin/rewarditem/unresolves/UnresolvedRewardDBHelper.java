package com.example.parentalapp.admin.rewarditem.unresolves;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.parentalapp.database.DatabaseOpenHelper;

import java.util.ArrayList;

public class UnresolvedRewardDBHelper extends DatabaseOpenHelper{

    public static final String UNRESOLVED_REWARD_TABLE = "unresolved_reward";
    public static final String UNRESOLVED_REWARD_ID = "id";
    public static final String UNRESOLVED_REWARD_NAME = "reward_name";
    public static final String UNRESOLVED_REWARD_ITEM_ID = "reward_id";
    public static final String UNRESOLVED_REWARD_QUANTITY = "quantity";
    public static final String UNRESOLVED_REWARD_DATE = "date";
    public static final String UNRESOLVED_REWARD_STATUS = "status";
    public static final String UNRESOLVED_REWARD_STATUS_UNRESOLVED = "unresolved";
    public static final String UNRESOLVED_REWARD_STATUS_RESOVLED = "resolved";

    public UnresolvedRewardDBHelper(Context context) {
        super(context);
    }

    public ArrayList<UnresolvedReward> getUnresolvedReward(){
        ArrayList<UnresolvedReward> unresolvedRewardList = new ArrayList<>();
        String sql = "SELECT * FROM " + UNRESOLVED_REWARD_TABLE + " WHERE " + UNRESOLVED_REWARD_STATUS + "=\'" + UNRESOLVED_REWARD_STATUS_UNRESOLVED + "\'";
        Cursor c = super.query(sql);

        if(c.moveToFirst()){
            do{
                int id = c.getInt(c.getColumnIndex(UNRESOLVED_REWARD_ID));
                String itemName = c.getString(c.getColumnIndex(UNRESOLVED_REWARD_NAME));
                int rewardID = c.getInt(c.getColumnIndex(UNRESOLVED_REWARD_ITEM_ID));
                int quantity = c.getInt(c.getColumnIndex(UNRESOLVED_REWARD_QUANTITY));
                String date = c.getString(c.getColumnIndex(UNRESOLVED_REWARD_DATE));
                String status = c.getString(c.getColumnIndex(UNRESOLVED_REWARD_STATUS));
                UnresolvedReward unresolvedReward = new UnresolvedReward(id, itemName, rewardID, quantity, date, status);
                unresolvedRewardList.add(unresolvedReward);
            }while(c.moveToNext());
        }
        c.close();

        return unresolvedRewardList;
    }

    public boolean addUnresolvedReward(String rewardName, int rewardID, int quantity, String date, String status){
        ContentValues cv = new ContentValues();
        cv.put(UNRESOLVED_REWARD_NAME, rewardName);
        cv.put(UNRESOLVED_REWARD_ITEM_ID, rewardID);
        cv.put(UNRESOLVED_REWARD_QUANTITY, quantity);
        cv.put(UNRESOLVED_REWARD_DATE, date);
        cv.put(UNRESOLVED_REWARD_STATUS, status);
        return super.insertSQL(UNRESOLVED_REWARD_TABLE, cv);
    }

    public boolean changeStatus(int id, String status){
        ContentValues cv = new ContentValues();
        cv.put(UNRESOLVED_REWARD_STATUS, status);
        return super.updateSQL(UNRESOLVED_REWARD_TABLE, cv, id) > 0;
    }

    public boolean deleteUnresolvedReward(int id){
        return super.deleteSQL(UNRESOLVED_REWARD_TABLE, UNRESOLVED_REWARD_ID, String.valueOf(id));
    }
}
