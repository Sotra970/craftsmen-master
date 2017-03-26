package com.craftsmen.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.craftsmen.Adapter.Details_Adapter;
import com.craftsmen.AppManger.AppController;
import com.craftsmen.AppManger.Config;
import com.craftsmen.Models.Comements_model;
import com.craftsmen.Models.Request_item_model;
import com.craftsmen.Models.User_model;
import com.craftsmen.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 2/23/2017.
 */

public class DetailsActivity extends Activity {
    RecyclerView recyclerView ;
    public Details_Adapter adapter;
    ArrayList<Comements_model> item_model=new ArrayList<>();
    TextView name , desc , time , loc;
    ImageView picture;
    ImageView req_img;
    Request_item_model extra_model ;
    ImageButton send_comment ;
    EditText comment ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_order);

        extra_model = (Request_item_model) getIntent().getExtras().get("extra_model") ;

        name = (TextView) findViewById(R.id.request_item_creator_name);
        time = (TextView) findViewById(R.id.request_item_time);
        loc = (TextView) findViewById(R.id.request_item_loc);
        desc = (TextView) findViewById(R.id.request_item_desc);
        picture = (ImageView) findViewById(R.id.request_item_creator_img);
        req_img = (ImageView) findViewById(R.id.req_img);
        send_comment = (ImageButton) findViewById(R.id.send_comment);

        comment = (EditText) findViewById(R.id.write_comment)  ;

        send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!comment.getText().toString().trim().isEmpty())
                    send_comment_req();
            }
        });

        
        name.setText(extra_model.getUser_model().getU_name());
        desc.setText(extra_model.getDesc());
//        loc.setText(extra_model.getLocation());
        time.setText(extra_model.getCreated_date());
        Glide.with(getApplicationContext()).load(Config.img_url  +extra_model.getUser_model().getU_Pic()) .fitCenter() . into(picture) ;
        Glide.with(getApplicationContext()).load(Config.img_url  +extra_model.getRequesst_image()) .fitCenter() . into(req_img) ;


        recyclerView = (RecyclerView) findViewById(R.id.comment_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL , false) ;
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new Details_Adapter(getApplicationContext(), item_model);
        recyclerView.setAdapter(adapter);
        getComments();
    }

    private void send_comment_req() {
        findViewById(R.id.loadingSpinner).setVisibility(View.VISIBLE);
            StringRequest user_req = new StringRequest(Request.Method.POST, Config.BASE_URL+"add_comment.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("login response" , response) ;
                    try {

                        if (response.trim().equals("Success")){
                            getComments();
                        }
                    }catch (Exception e) {
                        findViewById(R.id.loadingSpinner).setVisibility(View.GONE);

                        Log.e("login response  err" , e.toString()) ;
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("login err" , error.toString()) ;
                            Toast.makeText(getApplicationContext(),"no internet connection" , Toast.LENGTH_LONG).show();
                        }
                    }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String , String> parmas = new HashMap<>() ;
                    parmas.put("user_id" , AppController.getInstance().getPrefManager().getUser().getU_Id()) ;
                    parmas.put("request_id" , extra_model.getReq_id()) ;
                    parmas.put("comment" ,comment.getText().toString().trim() ) ;
                    Log.e("params" , parmas.toString());
                    return parmas;
                }

            };

            AppController.getInstance().getRequestQueue().add(user_req) ;

    }

    void getComments(){
        Log.e("get comments " , "strat") ;

        StringRequest user_req = new StringRequest(Request.Method.POST, Config.BASE_URL+"comments.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("get comments  response" , response) ;
                try {

                    if (response.equals("Empty")){
                    }else {
                        findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                        JSONArray fedarr = new JSONArray(response) ;
                        for (int i= (0); i<fedarr.length() ; i++){
                            JSONObject temp = fedarr.getJSONObject(i) ;

                            Comements_model comements_model = new Comements_model() ;
                            comements_model.setComment(temp.getString("comment"));
                            comements_model.setDate(temp.getString("created_date"));

                            User_model user_model = new User_model() ;

                            user_model.setU_Id(temp.getString("user_id"));
                            user_model.setU_Pic(temp.getString("user_image"));
                            user_model.setU_name(temp.getString("name"));
                            user_model.setU_p_num(temp.getString("phone"));
                            user_model.setU_type(temp.getString("type"));
                            user_model.setU_service(temp.getString("job"));
                            user_model.setLat(temp.getString("lat"));
                            user_model.setLung(temp.getString("lung"));
                            comements_model.setUser_model(user_model);


                            item_model.add(comements_model);
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
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String , String> parmas = new HashMap<>() ;
                parmas.put("request_id" ,extra_model.getReq_id()) ;
                Log.e("params" , parmas.toString());
                return parmas;
            }

        };

        AppController.getInstance().getRequestQueue().add(user_req) ;

    }


//        public ArrayList<Comements_model> getdata(){
//            String [] name={"heba", "mona" ,"reda"};
//            String [] desc={"hvasashdhagdsjgdjadg","kjkjkkj","jhjhjhjh"};
//            int [] picture ={R.drawable.plumber,R.drawable.hacksaw,R.drawable.hacksaw};
//            for(int i=0;i<name.length;i++){
//                Comements_model current=new Comements_model();
//                User_model user = new User_model() ;
//                user.setU_name(name[i]);
////                user.setU_Pic(picture[i]);
//                current.setUser_model(user);
//                current.setComment(desc[i]);
//                item_model.add(current);
//            }
//            return item_model;
//
//        }


}
