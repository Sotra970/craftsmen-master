package com.craftsmen.Models;

import android.location.Location;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lenovo on 2/22/2017.
 */

public class Request_item_model  implements Serializable{
    String  desc , req_id , user_id;
    String created_date ,  requesst_image ;
    String lat , lung  ;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLung() {
        return lung;
    }

    public void setLung(String lung) {
        this.lung = lung;
    }

    User_model user_model ;

    public User_model getUser_model() {
        return user_model;
    }

    public void setUser_model(User_model user_model) {
        this.user_model = user_model;
    }

    public String getReq_id() {
        return req_id;
    }

    public void setReq_id(String req_id) {
        this.req_id = req_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }



    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getRequesst_image() {
        return requesst_image;
    }

    public void setRequesst_image(String requesst_image) {
        this.requesst_image = requesst_image;
    }

}
