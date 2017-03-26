package com.craftsmen.Models;

import java.io.Serializable;

/**
 * Created by lenovo on 2/24/2017.
 */

public class User_model implements Serializable {
   String U_name , U_service  , U_password, U_p_num, U_Pic,U_Id, U_type;
    String U_email ,U_join_date  ;
    String lat="", lung = "" ;
    String distance ;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

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

    public String getU_email() {
        return U_email;
    }

    public void setU_email(String u_email) {
        U_email = u_email;
    }


    public String getU_join_date() {
        return U_join_date;
    }

    public void setU_join_date(String u_join_date) {
        this.U_join_date = u_join_date;
    }

    public String getU_Id() {
        return U_Id;
    }

    public void setU_Id(String u_Id) {
        U_Id = u_Id;
    }

    public String getU_type() {
        return U_type;
    }

    public void setU_type(String u_type) {
        U_type = u_type;
    }

    public String getU_name() {
        return U_name;
    }

    public void setU_name(String u_name) {
        U_name = u_name;
    }

    public String getU_service() {
        return U_service;
    }

    public void setU_service(String u_service) {
        U_service = u_service;
    }

    public String getU_password() {
        return U_password;
    }

    public void setU_password(String u_password) {
        U_password = u_password;
    }

    public String getU_p_num() {
        return U_p_num;
    }

    public void setU_p_num(String u_p_num) {
        U_p_num = u_p_num;
    }

    public String getU_Pic() {
        return U_Pic;
    }

    public void setU_Pic(String u_Pic) {
        U_Pic = u_Pic;
    }
}
