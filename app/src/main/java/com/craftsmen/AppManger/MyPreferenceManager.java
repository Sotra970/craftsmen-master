package com.craftsmen.AppManger;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.craftsmen.Models.User_model;



public class MyPreferenceManager {

    private String TAG = MyPreferenceManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "craftsmen";

    // All Shared Preferences Keys
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_PHONE = "phone";
    private static final String KEY_USER_TYPE = "user_type";
    private static final String KEY_USER_service_ = "service";
    private static final String KEY_USER_pic = "pic";
    private static final String KEY_USER_LAT = "lat";
    private static final String KEY_USER_LUNG = "lung";


    // Constructor
    public MyPreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void storeUser(User_model user) {
        editor.clear();
        editor.commit();
        editor.putString(KEY_USER_ID, user.getU_Id());
        editor.putString(KEY_USER_NAME, user.getU_name());
        editor.putString(KEY_USER_TYPE , user.getU_type());
        editor.putString(KEY_USER_PHONE , user.getU_p_num());
        editor.putString(KEY_USER_EMAIL , user.getU_email());
        editor.putString(KEY_USER_service_ , user.getU_service());
        editor.putString(KEY_USER_pic , user.getU_Pic());
        editor.putString(KEY_USER_LAT , user.getLat());
        editor.putString(KEY_USER_LUNG , user.getLung());
        editor.commit();


        Log.e(TAG, "User is stored in shared preferences. " + user.getU_name() );
    }

    public User_model getUser() {
        if (pref.getString(KEY_USER_ID, null) != null) {
            String id, name,type,phone ,email , lat , lung , service , pic ;
            id = pref.getString(KEY_USER_ID, null);
            name = pref.getString(KEY_USER_NAME, null);
            phone = pref.getString(KEY_USER_PHONE, null);
            type = pref.getString(KEY_USER_TYPE, null);
            email = pref.getString(KEY_USER_EMAIL, null);
            lat = pref.getString(KEY_USER_LAT, null);
            lung = pref.getString(KEY_USER_LUNG, null);
            service = pref.getString(KEY_USER_service_, null);
            pic = pref.getString(KEY_USER_pic, null);


            User_model user_model = new User_model() ;
            user_model.setU_Id(id);
            user_model.setU_name(name);
            user_model.setU_p_num(phone);
            user_model.setU_type(type) ;
            user_model.setU_service(service); ;
            user_model.setU_email(email); ;
            user_model.setLat(lat); ;
            user_model.setLung(lung); ;
            user_model.setU_Pic(pic); ;
            return user_model;
        }
        return null;
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }
}
