package com.example.nganth.restaurantapp;

public class Comments {
    private String comment_id;
    private String time, date, mail, comment;

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Comments(String comment_id, String time, String date, String mail, String comment) {
        this.comment_id = comment_id;
        this.time = time;
        this.date = date;
        this.mail = mail;
        this.comment = comment;
    }

    public Comments() {
    }
}
