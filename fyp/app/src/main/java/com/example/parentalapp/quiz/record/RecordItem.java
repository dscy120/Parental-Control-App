package com.example.parentalapp.quiz.record;

public class RecordItem {
    private int id;
    private String category;
    private int score;

    public RecordItem(int id, String category, int score) {
        this.id = id;
        this.category = category;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public int getScore() {
        return score;
    }
}
