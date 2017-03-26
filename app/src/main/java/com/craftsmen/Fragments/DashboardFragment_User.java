package com.craftsmen.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.craftsmen.Activity.WorkersActivity;
import com.craftsmen.AppManger.AndroidMultiPartEntity;
import com.craftsmen.AppManger.AppController;
import com.craftsmen.AppManger.Config;
import com.craftsmen.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by lenovo on 2/22/2017.
 */

public class DashboardFragment_User extends Fragment {
    View carpenter ,mechanic,plumber,electricity,n2ash,tblit;
    View layout_container  ;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout_container = inflater.inflate(R.layout.fragment_dashboard_user, container, false);



        carpenter =  layout_container.findViewById(R.id.user_cardview_carpenter);
        mechanic = layout_container.findViewById(R.id.user_cardview_mech);
        plumber = layout_container.findViewById(R.id.user_cardview_plumber);
        electricity = layout_container.findViewById(R.id.user_cardview_electricity);
        n2ash = layout_container.findViewById(R.id.user_cardview_n2ash);
        tblit = layout_container.findViewById(R.id.user_cardview_tblit);


        carpenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 startWorkersActivity("Carpentery");

            }
        });

        mechanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 startWorkersActivity("Mechanics");

            }
        });

        plumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 startWorkersActivity("Plumbing");

            }
        });

        electricity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 startWorkersActivity("Electricity");

            }
        });

        n2ash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 startWorkersActivity("Paint");

            }
        });

        tblit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 startWorkersActivity("Carpentry");

            }
        });


        return layout_container;

    }

    void startWorkersActivity(String type){
        Intent intent =new Intent(getContext(), WorkersActivity.class);
        intent.putExtra("type" , type);
        startActivity(intent);
    }

}
