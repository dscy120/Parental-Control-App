package com.example.parentalapp.reward.rewardhistory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.parentalapp.database.DatabaseOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class RewardHistoryDBHelper extends DatabaseOpenHelper {

    public static final String REWARD_RECORD_TABLE = "reward_purchase_record";
    public static final String REWARD_RECORD_ID = "id";
    public static final String REWARD_RECORD_ITEM_NAME = "itemName";
    public static final String REWARD_RECORD_ITEM_ID = "itemId";
    public static final String REWARD_RECORD_DATE = "date";

    public RewardHistoryDBHelper(Context context) {
        super(context);
    }

    public ArrayList<RewardRecordItem> getRewardRecord(){
        ArrayList<RewardRecordItem> recordList = new ArrayList<>();
        String sql = "SELECT * FROM " + REWARD_RECORD_TABLE;
        Cursor c = super.query(sql);

        if(c.moveToFirst()){
            do{
                int id = c.getInt(c.getColumnIndex(REWARD_RECORD_ID));
                String itemName = c.getString(c.getColumnIndex(REWARD_RECORD_ITEM_NAME));
                int itemId = c.getInt(c.getColumnIndex(REWARD_RECORD_ITEM_ID));
                String date = c.getString(c.getColumnIndex(REWARD_RECORD_DATE));

                RewardRecordItem rewardRecordItem = new RewardRecordItem(id, itemName, itemId, date);
                recordList.add(rewardRecordItem);
            }while (c.moveToNext());
        }
        c.close();

        return recordList;
    }

    public boolean addRecord(String itemName, int itemId){
        ContentValues cv = new ContentValues();
        cv.put(REWARD_RECORD_ITEM_NAME, itemName);
        cv.put(REWARD_RECORD_ITEM_ID, itemId);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentDate = simpleDateFormat.format(new Date());
        cv.put(REWARD_RECORD_DATE, currentDate);

        return super.insertSQL(REWARD_RECORD_TABLE, cv);
    }
}
