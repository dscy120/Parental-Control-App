package com.example.parentalapp.reward.rewardhistory;

public class RewardRecordItem {

    private int id;
    private String itemName;
    private int itemId;
    private String date;

    public RewardRecordItem(int id, String itemName, int itemId, String date) {
        this.id = id;
        this.itemName = itemName;
        this.itemId = itemId;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public int getItemId() {
        return itemId;
    }

    public String getDate() {
        return date;
    }
}
