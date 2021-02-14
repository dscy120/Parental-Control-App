package com.example.parentalapp.admin.rewarditem.unresolves;

public class UnresolvedReward {
    private int id;
    private String rewardName;
    private int rewardID;
    private int quantity;
    private String date;
    private String status;

    public UnresolvedReward(int id, String rewardName, int rewardID, int quantity, String date, String status) {
        this.id = id;
        this.rewardName = rewardName;
        this.rewardID = rewardID;
        this.quantity = quantity;
        this.date = date;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getRewardName() {
        return rewardName;
    }

    public int getRewardID() {
        return rewardID;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}
