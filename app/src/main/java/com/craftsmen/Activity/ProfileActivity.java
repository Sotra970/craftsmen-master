package com.craftsmen.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.craftsmen.Adapter.Requests_Adapter;
import com.craftsmen.AppManger.AppController;
import com.craftsmen.AppManger.Config;
import com.craftsmen.Models.Request_item_model;
import com.craftsmen.Models.User_model;
import com.craftsmen.R;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    RecyclerView prev_request ;
    String type = "craftsmen" ;
    Requests_Adapter request_adapter ;
    ArrayList<Request_item_model>  pref_req_data = new ArrayList<>();
    User_model userModel ;
    TextView name   , phone  ;
    View dialog ;
    View send , cancel;
    TextInputLayout desc_layout ;
    EditText description ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        CollapsingToolbarLayout profile_collapsing = (CollapsingToolbarLayout) findViewById(R.id.profile_collapsing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dialog = findViewById(R.id.layout_dashboard);
        send =  findViewById(R.id.send_btn);
        cancel = findViewById(R.id.cancel_btn);
        desc_layout = (TextInputLayout) findViewById(R.id.desc_layout);
        description = (EditText) findViewById(R.id.desc_input);

        name  = (TextView) findViewById(R.id.profle_name);
//        gender  = (TextView) findViewById(R.id.profle_gender);
        phone  = (TextView) findViewById(R.id.profle_phone);
//        city  = (TextView) findViewById(R.id.profle_city);
//        adress  = (TextView) findViewById(R.id.profle_adress);
        try {
            Log.e("extra user",getIntent().getExtras().toString() +"");
                userModel = (User_model) getIntent().getExtras().get("extra") ;
            if (userModel.getU_type().equals("user"))
                getRequests();
        }catch (NullPointerException e){
            userModel = AppController.getInstance().getPrefManager().getUser() ;
            if (userModel.getU_type().equals("user"))
                getRequests();
            // get user from pref
        }

        profile_collapsing.setTitle(userModel.getU_name());
        name.setText(userModel.getU_name());
        phone.setText(userModel.getU_p_num());
        ImageView profile_img  = (ImageView) findViewById(R.id.profile_image) ;
        Glide.with(getApplicationContext()) .load(Config.img_url+userModel.getU_Pic()).fitCenter().into(profile_img) ;

            prev_request = (RecyclerView) findViewById(R.id.profile_prev_request) ;
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL , false) ;
            prev_request.setLayoutManager(linearLayoutManager);
            request_adapter  = new Requests_Adapter(getApplicationContext(), pref_req_data, new Requests_Adapter.Adapter_item_click_listener() {
                @Override
                public void onClick(int postion) {
                    Intent intent = new Intent(getApplicationContext() , DetailsActivity.class) ;
                    intent.putExtra("extra_model" , pref_req_data.get(postion)) ;
                    startActivity(intent);
                }

                @Override
                public void onLongClick(final int postiion) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this)
                            .setMessage("Are you want to delete this post ")
                            .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    delete_request(pref_req_data.get(postiion));
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog dia = builder.create() ;
                    dia.show();
                    dia.getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimaryDark));


                }
            }) ;
            prev_request.setAdapter(request_adapter);

        
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_mess_req() ; 
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setVisibility(View.GONE);
            }
        });
            

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!AppController.getInstance().getPrefManager().getUser().getU_Id().equals(userModel.getU_Id())){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.profile_menu, menu);

            Drawable drawable = menu.findItem(R.id.send_mess_action).getIcon();

            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, ContextCompat.getColor(this,R.color.colorScondaryText));
            menu.findItem(R.id.send_mess_action).setIcon(drawable);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       if (item.getItemId() == R.id.send_mess_action)
       {
           opendialog();
           return true ;
       }
        return false ;
    }

    public void opendialog(){
        dialog.setVisibility(View.VISIBLE);
    }

    void getRequests(){
        StringRequest user_req = new StringRequest(Request.Method.POST, Config.BASE_URL+"myrequest.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("login response" , response) ;
                try {

                    if (response.equals("Empty")){
                        Toast.makeText(getApplicationContext(),"no previous requests",Toast.LENGTH_LONG).show();
                    }else {
                        JSONArray fedarr = new JSONArray(response) ;
                        for (int i=0 ; i<fedarr.length() ; i++){
                            JSONObject temp = fedarr.getJSONObject(i) ;
                            Request_item_model request_item_model = new Request_item_model() ;
                            request_item_model.setReq_id(temp.getString("request_id"));
                            request_item_model.setUser_id(temp.getString("user_id"));
                            request_item_model.setDesc(temp.getString("description"));
//                            request_item_model.setLocation(temp.getString("location"));
                            request_item_model.setCreated_date(temp.getString("created_date"));
                            request_item_model.setRequesst_image(temp.getString("image"));

                            User_model user_model = new User_model() ;
                            user_model.setU_Pic(temp.getString("user_image"));
                            user_model.setU_name(temp.getString("name"));
                            user_model.setU_p_num(temp.getString("phone"));
                            user_model.setLat(temp.getString("lat"));
                            user_model.setLung(temp.getString("lung"));
                            user_model.setU_service(temp.getString("job"));

                            request_item_model.setUser_model(user_model);


                            pref_req_data.add(request_item_model);
                        }

                        request_adapter.notifyDataSetChanged();

                    }
                }catch (Exception e) {
                    Log.e("login response  err" , e.toString()) ;
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("login err" , error.toString()) ;
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String , String> parmas = new HashMap<>() ;
                parmas.put("user_id" , userModel.getU_Id()) ;
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

    private void send_mess_req() {
        if (!validate_description())
            return;
        findViewById(R.id.loadingSpinner).setVisibility(View.VISIBLE);
        StringRequest user_req = new StringRequest(Request.Method.POST, Config.BASE_URL+"send_message.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("login response" , response) ;
                try {

                    if (response.trim().equals("Success")){
                        findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                       dialog.setVisibility(View.GONE);

                    }
                }catch (Exception e) {
                    Log.e("login response  err" , e.toString()) ;
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("login err" , error.toString()) ;
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String , String> parmas = new HashMap<>() ;
                parmas.put("sender_id" , AppController.getInstance().getPrefManager().getUser().getU_Id()) ;
                parmas.put("receiver_id" , userModel.getU_Id()) ;
                parmas.put("message" ,description.getText().toString().trim() ) ;
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


    boolean validate_description (){
        if (description.getText().toString().trim().equals(null) || description.getText().toString().trim().isEmpty()){
            return false ;
        }
        return true ;
    }


    void delete_request(final Request_item_model model){
        findViewById(R.id.loadingSpinner).setVisibility(View.VISIBLE);
        StringRequest user_req = new StringRequest(Request.Method.POST, Config.BASE_URL+"delete_req.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("login response" , response) ;

                try {

                    if (response.equals("success")){
                        Toast.makeText(getApplicationContext(),"request deleted",Toast.LENGTH_LONG).show();
                        pref_req_data.remove(model);
                        request_adapter.notifyDataSetChanged();
                        findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                    }else {
                        Toast.makeText(getApplicationContext(),"operation failed please  try again",Toast.LENGTH_LONG).show();

                    }
                }catch (Exception e) {
                    Log.e("login response  err" , e.toString()) ;
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("login err" , error.toString()) ;
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String , String> parmas = new HashMap<>() ;
                parmas.put("request_id" , model.getReq_id()) ;
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


}
