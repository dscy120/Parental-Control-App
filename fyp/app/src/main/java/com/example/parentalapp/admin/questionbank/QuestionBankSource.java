package com.example.parentalapp.admin.questionbank;

public class QuestionBankSource {
    private int id;
    private String subject;
    private String level;
    private String title;
    private String publisher;
    private String downloadLink;

    public QuestionBankSource(int id, String subject, String level, String title, String publisher, String downloadLink) {
        this.id = id;
        this.subject = subject;
        this.level = level;
        this.title = title;
        this.publisher = publisher;
        this.downloadLink = downloadLink;
    }

    public int getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public String getLevel() {
        return level;
    }

    public String getTitle() {
        return title;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getDownloadLink() {
        return downloadLink;
    }
}
