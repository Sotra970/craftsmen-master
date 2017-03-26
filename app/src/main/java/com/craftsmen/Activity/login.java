package com.craftsmen.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.craftsmen.AppManger.AppController;
import com.craftsmen.Models.User_model;
import com.craftsmen.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 2/22/2017.
 */

public class login extends Activity{
    EditText mob_num , password ;
    Button signin , register ;
    String type = "craftsmen";
    TextInputLayout user_name_container  , user_pass_container ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mob_num = (EditText) findViewById(R.id.P_num);
        password = (EditText) findViewById(R.id.pass);
        signin = (Button) findViewById(R.id.login_btn);
        register = (Button) findViewById(R.id.signup_btn);
        user_name_container = (TextInputLayout) findViewById(R.id.P_num_layout);
        user_pass_container = (TextInputLayout) findViewById(R.id.pass_layout);
        final Intent signup_user_page = new Intent(this,signup_user.class);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] title = {"Worker","User"};

                AlertDialog.Builder choose = new AlertDialog.Builder(login.this);
                choose.setTitle("Enter your type");

                choose.setSingleChoiceItems(title, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int postion) {
                        if (postion == 0 )
                        type = "craftsmen" ;
                        if (postion == 1)
                        type = "user" ;

                        Log.e("choosed item " , title[postion] + " " + type + " postion") ;
                    }
                });
                choose.setNegativeButton("cancel",new DialogInterface.OnClickListener(){
                    public void onClick (DialogInterface choose,int which){
                        choose.dismiss();
                    }
                });
                choose.setPositiveButton("ok",new DialogInterface.OnClickListener(){
                    public void onClick (DialogInterface choose,int which){
                        signup_user_page.putExtra("type" , type ) ;
                        startActivity(signup_user_page);
                    }
                });
               AlertDialog dialog =  choose.create() ;
                dialog.show();
                dialog.getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimaryDark));


            }

        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUser() ;


            }
        });

    }

    void getUser(){
        if (!validate_user_name())
            return;
        if (!validate_user_pass())
            return;
        findViewById(R.id.loadingSpinner).setVisibility(View.VISIBLE);
        final String name  = mob_num.getText().toString().trim();
        final String pass= password.getText().toString().trim();
        StringRequest user_req = new StringRequest(Request.Method.POST, "http://experttk.com/craftsmen/login.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("login response" , response) ;

                try {

                    if (response.equals("Empty")){
                        Toast.makeText(getApplicationContext(),"Email or Password is incorrect",Toast.LENGTH_LONG).show();
                        findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                    }else {
                        JSONArray fedarr = new JSONArray(response) ;
                        JSONObject user_obj = fedarr.getJSONObject(0) ;
                        Log.e("user_oobj" , user_obj.toString()) ;
                        User_model user_model = new User_model() ;
                        user_model.setU_Id(user_obj.getString("id"));
                        user_model.setU_name(user_obj.getString("name"));
                        user_model.setU_email(user_obj.getString("email"));
                        user_model.setU_p_num(user_obj.getString("phone"));
                        user_model.setLat(user_obj.getString("lat"));
                        user_model.setLung(user_obj.getString("lung"));
                        user_model.setU_service(user_obj.getString("job"));
                        user_model.setU_type(user_obj.getString("type"));
                        user_model.setU_Pic(user_obj.getString("user_image"));
                        user_model.setU_join_date(user_obj.getString("join_date"));
                        FirebaseMessaging.getInstance().subscribeToTopic("craftsmen");
                        AppController.getInstance().getPrefManager().storeUser(user_model);
                        startActivity(new Intent(getApplicationContext() , MainActivity.class));
                        findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                        finish();

                    }
                }catch (Exception e) {
                    Log.e("login response  err" , e.toString()) ;
                    findViewById(R.id.loadingSpinner).setVisibility(View.GONE);

                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("login err" , error.toString()) ;
                        findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"no internet connection" , Toast.LENGTH_LONG).show();

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String , String> parmas = new HashMap<>() ;
                parmas.put("username" , name) ;
                parmas.put("password" , pass) ;
                Log.e("params" , parmas.toString());
                return parmas;
            }

        };
        int socketTimeout = 10000; // 10 seconds. You can change it

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                10,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        user_req.setRetryPolicy(policy);
        AppController.getInstance().getRequestQueue().add(user_req) ;

    }

    boolean validate_user_name (){
        if (mob_num.getText().toString().trim().equals(null) || mob_num.getText().toString().trim().isEmpty()){
            user_name_container.setError("You have to enter a value");
            return false ;
        }
        user_name_container.setErrorEnabled(false);
        return true ;
    }

    boolean validate_user_pass(){
        if (password.getText().toString().trim().equals(null) || password.getText().toString().trim().isEmpty()){
            user_pass_container.setError("You have to enter a value");
            return false ;
        }
        user_pass_container.setErrorEnabled(false);
        return true ;
    }

}
