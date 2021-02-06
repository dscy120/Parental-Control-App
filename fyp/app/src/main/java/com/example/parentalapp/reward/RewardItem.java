package com.example.parentalapp.reward;

public class RewardItem {
    private int id;
    private String name;
    private int point;
    private String effectItem;
    private String effectValue;

    public RewardItem(int id, String name, int point, String effectItem, String effectValue) {
        this.id = id;
        this.name = name;
        this.point = point;
        this.effectItem = effectItem;
        this.effectValue = effectValue;
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

    public String getEffectItem() {
        return effectItem;
    }

    public String getEffectValue() {
        return effectValue;
    }
}
