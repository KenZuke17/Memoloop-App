package com.example.memoloop;

public class Task {
    private String title;
    private String dueDate;
    private String status;

    public Task(String title, String dueDate, String status) {
        this.title = title;
        this.dueDate = dueDate;
        this.status = status;
    }

    public String getTitle() { return title; }
    public String getDueDate() { return dueDate; }
    public String getStatus() { return status; }
}