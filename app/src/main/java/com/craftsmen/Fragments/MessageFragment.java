package com.craftsmen.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RetryPolicy;
import com.craftsmen.Adapter.Messages_Adapter;
import com.craftsmen.AppManger.Config;
import com.craftsmen.Models.Comements_model;
import com.craftsmen.Models.Message_model;
import com.craftsmen.R;

import java.util.ArrayList;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.craftsmen.AppManger.AppController;
import com.craftsmen.Models.User_model;
import com.craftsmen.R;
import com.craftsmen.RecyclerViewTouchHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 2/22/2017.
 */

public class MessageFragment extends Fragment {

    RecyclerView recyclerView ;
    public Messages_Adapter adapter;
    ArrayList<Message_model> item_model=new ArrayList<>();
    View layout , layoutmsg;
    String url = "http://experttk.com/craftsmen/login.php" ;
    TextView textView ;
    View noMessages ;

    View dialog ;
    View send , cancel;
    TextInputLayout desc_layout ;
    EditText description ;
    private String reciver_id = "";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(layout==null) {
            layout = inflater.inflate(R.layout.fragment_message, container, false);
            recyclerView = (RecyclerView) layout.findViewById(R.id.msg_view);
            adapter = new Messages_Adapter(getActivity(), item_model);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.addOnItemTouchListener(new RecyclerViewTouchHelper(getContext(), recyclerView, new RecyclerViewTouchHelper.recyclerViewTouchListner() {
                @Override
                public void onclick(View child, int postion) {

                }

                @Override
                public void onLongClick(View child, int postion) {

                    reciver_id = item_model.get(postion).getUser_model().getU_Id()  ;
                    opendialog();

                }
            }));

            noMessages = layout.findViewById(R.id.no_mess);

            dialog = layout.findViewById(R.id.layout_dashboard);
            send =  layout.findViewById(R.id.send_btn);
            cancel = layout.findViewById(R.id.cancel_btn);
            desc_layout = (TextInputLayout)layout. findViewById(R.id.desc_layout);
            description = (EditText) layout.findViewById(R.id.desc_input);

        }
        geMessages();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_mess_req(reciver_id) ;
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setVisibility(View.GONE);
            }
        });

        return layout;
    }


    private void send_mess_req(final String reciver_id) {
        if (!validate_description())
            return;
        layout.findViewById(R.id.loadingSpinner).setVisibility(View.VISIBLE);
        StringRequest user_req = new StringRequest(Request.Method.POST, Config.BASE_URL+"send_message.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("login response" , response) ;
                try {

                    if (response.trim().equals("Success")){
                        layout.findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
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
                parmas.put("receiver_id" , reciver_id) ;
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


    public void opendialog(){
        dialog.setVisibility(View.VISIBLE);
    }

    void geMessages(){
        StringRequest user_req = new StringRequest(Request.Method.POST, Config.BASE_URL+"outbox.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("mess response" , response) ;
                try {
                    noMessages.setVisibility(View.GONE);

                    if (response.equals("Empty")){
                    }else {
                        noMessages.setVisibility(View.GONE);
                        JSONArray fedarr = new JSONArray(response) ;
                        for (int i= (0); i<fedarr.length() ; i++){
                            noMessages.setVisibility(View.GONE);
                            JSONObject temp = fedarr.getJSONObject(i) ;

                            Message_model message_model = new Message_model() ;
                            message_model.setMessage(temp.getString("message"));
                            message_model.setDate(temp.getString("date"));

                            User_model user_model = new User_model() ;
                            user_model.setU_Id(temp.getString("sender"));
                            user_model.setU_Pic(temp.getString("user_image"));
                            user_model.setU_name(temp.getString("name"));
                            user_model.setU_p_num(temp.getString("phone"));
                            user_model.setU_type(temp.getString("type"));
                            user_model.setLat(temp.getString("lat"));
                            user_model.setLung(temp.getString("lung"));
                            user_model.setU_service(temp.getString("job"));
                            message_model.setUser_model(user_model);


                            item_model.add(message_model);
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
                parmas.put("user_id" , AppController.getInstance().getPrefManager().getUser().getU_Id()) ;
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
