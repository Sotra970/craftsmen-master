package com.craftsmen.Models;

/**
 * Created by lenovo on 2/24/2017.
 */

public class Comements_model  {
    String comment;
    String Date ;
    User_model user_model ;

    public User_model getUser_model() {
        return user_model;
    }

    public void setUser_model(User_model user_model) {
        this.user_model = user_model;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
