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
import com.craftsmen.Activity.DetailsActivity;
import com.craftsmen.Adapter.Requests_Adapter;
import com.craftsmen.AppManger.AppController;
import com.craftsmen.AppManger.Config;
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

public class OrdersFragment extends Fragment {
    RecyclerView recyclerView ;
   public Requests_Adapter adapter;
    ArrayList<Request_item_model> item_models =new ArrayList<>();
    View layout ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       if (layout == null){
           getRequests() ;
           layout=inflater.inflate(R.layout.fragment_orders,container,false);
           recyclerView = (RecyclerView) layout.findViewById(R.id.orders_item);
           adapter = new Requests_Adapter(getActivity(), item_models, new Requests_Adapter.Adapter_item_click_listener() {
               @Override
               public void onClick(int postion) {
                   Intent intent = new Intent(getContext() , DetailsActivity.class) ;
                   intent.putExtra("extra_model" , item_models.get(postion)) ;
                   getContext().startActivity(intent);
               }

               @Override
               public void onLongClick(int postiion) {

               }
           });
           recyclerView.setAdapter(adapter);
           recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
       }
        return layout;
    }

    void getRequests(){
        StringRequest user_req = new StringRequest(Request.Method.POST, Config.BASE_URL+"search_request.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("login response" , response) ;
                try {

                    if (response.trim().equals("Empty")){
                        Toast.makeText(getContext(),"no requests found",Toast.LENGTH_LONG).show();
                    }else {
                        JSONArray fedarr = new JSONArray(response) ;
                        for (int i=0 ; i<fedarr.length() ; i++){
                            JSONObject temp = fedarr.getJSONObject(i) ;

                            Request_item_model request_item_model = new Request_item_model() ;
                            request_item_model.setReq_id(temp.getString("request_id"));
                            request_item_model.setUser_id(temp.getString("user_id"));
                            request_item_model.setDesc(temp.getString("description"));
                            request_item_model.setCreated_date(temp.getString("created_date"));
                            request_item_model.setRequesst_image(temp.getString("image"));

                            User_model user_model = new User_model() ;
                            user_model.setU_Pic(temp.getString("user_image"));
                            user_model.setU_name(temp.getString("name"));

                            user_model.setU_p_num(temp.getString("phone"));



                        try {
                            user_model.setLat(temp.getString("lat"));
                            user_model.setLung(temp.getString("lung"));
                        }catch (NullPointerException e){}


                            user_model.setU_service(temp.getString("job"));
                            request_item_model.setUser_model(user_model);

                            item_models.add(request_item_model);
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
//                parmas.put("city" , AppController.getInstance().getPrefManager().getUser().getU_city()) ;
                parmas.put("service_type" , AppController.getInstance().getPrefManager().getUser().getU_service()) ;
                parmas.put("lat" , AppController.getInstance().getPrefManager().getUser().getLat()) ;
                parmas.put("lung" , AppController.getInstance().getPrefManager().getUser().getLung()) ;
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
