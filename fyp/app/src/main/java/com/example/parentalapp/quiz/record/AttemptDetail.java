package com.example.parentalapp.quiz.record;

public class AttemptDetail {
    private int id;
    private int attemptId;
    private String questionId;
    private String category;
    private String attemptAnswer;
    private String correctAnswer;
    private int score;
    private String explanation;

    public AttemptDetail(int id, int attemptId, String questionId, String category, String attemptAnswer, String correctAnswer, int score, String explanation) {
        this.id = id;
        this.attemptId = attemptId;
        this.questionId = questionId;
        this.category = category;
        this.attemptAnswer = attemptAnswer;
        this.correctAnswer = correctAnswer;
        this.score = score;
        this.explanation = explanation;
    }

    public int getId() {
        return id;
    }

    public int getAttemptId() {
        return attemptId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getCategory() {
        return category;
    }

    public String getAttemptAnswer() {
        return attemptAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public int getScore() {
        return score;
    }

    public String getExplanation() {
        return explanation;
    }
}
