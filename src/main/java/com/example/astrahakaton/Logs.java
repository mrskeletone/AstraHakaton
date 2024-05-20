package com.example.astrahakaton;

import javafx.beans.property.SimpleStringProperty;

public class Logs {
    private SimpleStringProperty  date;
    private SimpleStringProperty user;
    private SimpleStringProperty  type;
    private SimpleStringProperty  comment;

    public String getDate() {
        return date.get();
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getUser() {
        return user.get();
    }

    public void setUser(String user) {
        this.user.set(user);
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getComment() {
        return comment.get();
    }

    public void setComment(String comment) {
        this.comment.set(comment);
    }

    public Logs(String date, String user, String type, String comment) {
        this.date = new SimpleStringProperty(date);
        this.user = new SimpleStringProperty(user);
        this.type = new SimpleStringProperty(type);
        this.comment = new SimpleStringProperty(comment);
    }
}
