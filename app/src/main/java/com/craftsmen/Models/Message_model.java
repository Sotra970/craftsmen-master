package com.craftsmen.Models;

import android.widget.ImageView;

/**
 * Created by lenovo on 2/23/2017.
 */

public class Message_model {
    String  message , date;
        User_model user_model ;

    public User_model getUser_model() {
        return user_model;
    }

    public void setUser_model(User_model user_model) {
        this.user_model = user_model;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
