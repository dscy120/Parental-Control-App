package com.example.parentalapp.reward;

public class RewardItem {
    private int id;
    private String name;
    private int point;

    public RewardItem(int id, String name, int point) {
        this.id = id;
        this.name = name;
        this.point = point;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPoint() {
        return point;
    }
}
