package com.craftsmen.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.craftsmen.Activity.signup_user;
import com.craftsmen.Adapter.Notification_Adapter;
import com.craftsmen.Adapter.Requests_Adapter;
import com.craftsmen.AppManger.AppController;
import com.craftsmen.AppManger.Config;
import com.craftsmen.Models.Comements_model;
import com.craftsmen.Models.Notification_model;
import com.craftsmen.Models.Request_item_model;
import com.craftsmen.Models.User_model;
import com.craftsmen.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 2/22/2017.
 */

public class NotificationFragment extends Fragment {

    RecyclerView recyclerView;
    public Notification_Adapter adapter;
    ArrayList<Notification_model> item_model=new ArrayList<>();
    View layout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       if(layout==null) {
           layout = inflater.inflate(R.layout.fragment_notification, container, false);
           recyclerView = (RecyclerView) layout.findViewById(R.id.noti_item);
           adapter = new Notification_Adapter(getActivity(), item_model);
           recyclerView.setAdapter(adapter);
           recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
           getComments();
       }

        return layout;
    }
    void getComments(){
        Log.e("get comments " , "strat") ;

        StringRequest user_req = new StringRequest(Request.Method.POST, Config.BASE_URL+"notify.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("get comments  response" , response) ;
                try {

                    if (response.equals("Empty")){
                    }else {
                        JSONArray fedarr = new JSONArray(response) ;
                        for (int i= (0); i<fedarr.length() ; i++){
                            JSONObject temp = fedarr.getJSONObject(i) ;

                            Notification_model notificationModel = new Notification_model() ;
                            notificationModel.setName(temp.getString("notify_text"));
                            notificationModel.setDate(temp.getString("date"));



                            item_model.add(notificationModel);
                        }

                        adapter.notifyDataSetChanged();

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
                        Toast.makeText(getContext(),"no internet connection" , Toast.LENGTH_LONG).show();

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String , String> parmas = new HashMap<>() ;
                parmas.put("user_id" ,AppController.getInstance().getPrefManager().getUser().getU_Id()) ;
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
