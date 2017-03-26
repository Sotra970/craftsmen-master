package com.craftsmen.Activity;

import android.Manifest;
import android.app.Activity;
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
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.craftsmen.AppManger.AndroidMultiPartEntity;
import com.craftsmen.AppManger.AppController;
import com.craftsmen.AppManger.Config;
import com.craftsmen.Models.Notification_model;
import com.craftsmen.R;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by lenovo on 2/22/2017.
 */

public class signup_user extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText signup_name ,signup_pass , signup_phone ;
    EditText signup_email   ;
    Button signup_btn , upload_img , get_location;
    TextInputLayout namecontainer , passcontainer , phonecontainer  ;
    TextInputLayout   emailcontainer  ;
    String uploaded_img_name = "" ;
    TextView txtPercentage;
    ProgressBar progressBar ;
    View progressBarView ;
    Spinner signup_job   ;
    ArrayList<String> city = new ArrayList<>() , job = new ArrayList<>();
    View joberr , cityerr ;
    String type  ;
    String ch_job =""  ;
    private ArrayAdapter adaptercity, adapterjob;
    String lat="",lung="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_user);

        type = getIntent().getExtras().getString("type") ;
        if (savedInstanceState == null) no_storage_permission();



        txtPercentage = (TextView) findViewById(R.id.txtPercentage);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBarView = findViewById(R.id.progressBarView);

        namecontainer = (TextInputLayout) findViewById(R.id.signup_name_layout);
        passcontainer = (TextInputLayout) findViewById(R.id.signup_pass_layout);
        phonecontainer = (TextInputLayout) findViewById(R.id.signup_phone_layout);
        emailcontainer = (TextInputLayout) findViewById(R.id.signup_email_layout);

        signup_name = (EditText)findViewById(R.id.signup_user_name);
        signup_pass = (EditText)findViewById(R.id.signup_user_pass);
        signup_phone = (EditText)findViewById(R.id.signup_user_phone);
        signup_email = (EditText)findViewById(R.id.signup_user_email);

        signup_job = (Spinner)findViewById(R.id.signup_user_job);







        signup_btn = (Button) findViewById(R.id.signup_user_btn);
        upload_img = (Button) findViewById(R.id.upload_img);
        upload_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_img();
            }
        });
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view==signup_btn);
                signup_check ();
            }
        });


        get_location = (Button) findViewById(R.id.add_station);

        get_location.setVisibility(View.VISIBLE);
        get_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getApplicationContext(), Get_my_location_activity.class) , Config.GET_LOCATION_REQUEST_CODDE);
            }
        });



        if (!type.equals("user")) {

            adapterjob = new ArrayAdapter(this, R.layout.simple_spinner_item, job);
            adapterjob.setDropDownViewResource(R.layout.simple_spinner_item);
            signup_job.setAdapter(adapterjob);
            signup_job.setVisibility(View.VISIBLE);
            signup_job.setOnItemSelectedListener(this);

            getJob();

        }



    }


//  public  void getsignup_info (){
//        String username = signup_name.getText().toString().trim();
//        String password = signup_pass.getText().toString().trim();
//        String phone = signup_phone.getText().toString().trim();
//        String city = signup_city.getText().toString().trim();
//        String address = signup_address.getText().toString().trim();
//        String gender = signup_gender.getText().toString().trim();
//        String email = signup_email.getText().toString().trim();
//        String job = signup_job.getText().toString().trim();
//        String type = signup_type.getText().toString().trim();
//    }
    boolean validate_user_name (){
        if (signup_name.getText().toString().trim().equals(null) || signup_name.getText().toString().trim().isEmpty()){
            namecontainer.setError("You have to enter a right name");
            return false ;
        }
        namecontainer.setErrorEnabled(false);
        return true ;
    }

    boolean validate_user_pass (){
        if (signup_pass.getText().toString().trim().equals(null) || signup_pass.getText().toString().trim().isEmpty()){
            passcontainer.setError("Wrong password");
            return false ;
        }
        passcontainer.setErrorEnabled(false);
        return true ;
    }
    boolean validate_user_email (){
            if (signup_email.getText().toString().trim().equals(null) || signup_email.getText().toString().trim().isEmpty()){
                emailcontainer.setError("Please Enter an Email");
                return false ;
            }
            emailcontainer.setErrorEnabled(false);
            return true ;
        }

    boolean validate_user_phone (){
            if (signup_phone.getText().toString().trim().equals(null) || signup_phone.getText().toString().trim().isEmpty()){
                phonecontainer.setError("Enter a working phone Number");
                return false ;
            }
            phonecontainer.setErrorEnabled(false);
            return true ;
        }

    boolean validate_user_job (){
                if (ch_job.trim().equals(null) || ch_job.trim().isEmpty()){
                    findViewById(R.id.job_err).setVisibility(View.VISIBLE);
                    return false ;
                }
        findViewById(R.id.job_err).setVisibility(View.GONE);
                return true ;
            }


    boolean validate_lat (){
        if (lat.trim().equals(null) || lat.trim().isEmpty()){
            findViewById(R.id.loc_err).setVisibility(View.VISIBLE);
            return false ;
        }
        findViewById(R.id.loc_err).setVisibility(View.GONE);
        return true ;
    }


    void signup_check () {
        if (!validate_user_name())
                return;

            if (!validate_user_email())
                return;
        if (!validate_user_pass())
            return;

            if (!validate_user_phone())
                return;


        if (!validateImg()) {
            uploaded_img_name = "";
        }
        if (!type.equals("user")){

            if (!validate_user_job())
                return;




        }

        if (!validate_lat())
            return;
        findViewById(R.id.loadingSpinner).setVisibility(View.VISIBLE);


        StringRequest requesturl = new StringRequest(Request.Method.POST, Config.BASE_URL+"register.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                    Log.e("login response  err" , response.toString()) ;
                    if (response.trim().equals("Success")){
                        supportFinishAfterTransition();
                    }else {
                        findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("login response  err" , error.toString()) ;
                    findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"no internet connection" , Toast.LENGTH_LONG).show();
                }
            }
            ) {

                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("name", signup_name.getText().toString().trim());
                    params.put("email", signup_email.getText().toString().trim());
                    params.put("password", signup_pass.getText().toString().trim());
                    params.put("phone", signup_phone.getText().toString().trim());
                    params.put("job", ch_job);
                    params.put("type", type);
                    params.put("img",uploaded_img_name);
                    params.put("lat",lat);
                    params.put("lung",lung);

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);

        int socketTimeout = 10000; // 10 seconds. You can change it

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                10,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        requesturl.setRetryPolicy(policy);
            requestQueue.add(requesturl);

    }



    public class ResizeImage extends AsyncTask<String, Integer, String> {
        protected String  filePath,fileName ;
        long totalSize = 0;
        Boolean ok = false ;
        Bitmap resized_bitmap ;


        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);
            progressBarView.setVisibility(View.VISIBLE);
            // updating progress bar value
            progressBar.setProgress(progress[0]);

            // updating percentage value
            txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        // Decode image in background.
        @Override
        protected String doInBackground(String... params) {
            filePath = params[0];
            File uploadFile = null ;
            String response = null;
            try {
                    resized_bitmap =  decodeSampledBitmapFromPath(filePath, 1080*2, 1920*2) ;

                uploadFile = bitmaptofile(resized_bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

            if (uploadFile == null){
                Log.e("category","file"+"null");
                uploadFile = new File(filePath);
            }
            fileName = uploadFile.getName();
            response = uploadFile(uploadFile);


            return    response ;
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(String result) {
            showAlert(result);
            progressBarView.setVisibility(View.GONE);
        }

        protected File  bitmaptofile(Bitmap bitmap) throws IOException {
            File outputDir = getApplicationContext().getCacheDir(); // context being the Activity pointer
            File outputFile = File.createTempFile(currentDateFormat(),".jpg" , outputDir);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(outputFile);

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return outputFile;
        }
        /**
         * Method to show alert dialog
         * */
        protected void showAlert(String message) {
            if (ok){
                String filePathName = new File(filePath).getName();
                Log.e("category","filename  "+fileName +"file path   " + filePathName);
                uploaded_img_name = fileName;
                validateImg();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(signup_user.this);
            builder.setMessage(message).setTitle("Response from Servers")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // do nothing
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            alert.getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));




        }

        @SuppressWarnings("deprecation")
        protected String uploadFile(File sourceFile) {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Config.BASE_URL+"upload_img.php");
            Log.e("category","url"+httppost.getURI());
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                Log.e("transferd" , num +"");
                                int precentage = (int) ((num / (float) totalSize) * 100);
                                if (precentage <=99){
                                    publishProgress(precentage);
                                }else publishProgress(99);
                            }
                        });


                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));


                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                    ok=true ;
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

    }

    public static String currentDateFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.ENGLISH);
        String  currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }
    public static Bitmap decodeSampledBitmapFromPath( String FilePath,
                                                      int reqWidth, int reqHeight) {


        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile( FilePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile( FilePath, options);
    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        Log.e("sapleSize" , inSampleSize + "");
        return inSampleSize;
    }
    protected boolean validateImg() {
        if ( uploaded_img_name.equals("null") || uploaded_img_name.isEmpty() ){
//            findViewById(R.id.img_err).setVisibility(View.VISIBLE);
            return false ;
        }else {
//            findViewById(R.id.img_err).setVisibility(View.GONE);
        }
        return true ;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Config.MY_PERMISSIONS_REQUEST_STORAGE: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    //  no_gps_permition();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(signup_user.this, "Permission needed to run your app", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    private void no_storage_permission() {
        // Here, thisActivity is the current activity
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {



            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Config.MY_PERMISSIONS_REQUEST_STORAGE);


        }
    }
    void get_img(){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, Config.RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /// select from gallery section
        if (requestCode == Config.RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data!=null){

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Log.e("Uri",selectedImage + "");
            Log.e("filePathColumn",MediaStore.Images.Media.DATA + "");

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);

            cursor.close();

            Log.e("picturePath",picturePath + "");
            new ResizeImage().execute(picturePath);

        }

        if (requestCode == Config.GET_LOCATION_REQUEST_CODDE && resultCode == 200 && data!=null){
            lat = data.getExtras().getString("lat");
            lung = data.getExtras().getString("lung");
        }
    }

    void getCities(){
        Log.e("get comments " , "strat") ;

        StringRequest user_req = new StringRequest(Request.Method.POST, Config.BASE_URL+"city.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("get comments  response" , response) ;
                try {

                    if (response.equals("Empty")){
                    }else {
                        JSONArray fedarr = new JSONArray(response) ;
                        for (int i= (0); i<fedarr.length() ; i++){
                            JSONObject temp = fedarr.getJSONObject(i) ;

                            city.add(temp.getString("name"));



                        }

                        adaptercity.notifyDataSetChanged();

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
                Log.e("params" , parmas.toString());
                return parmas;
            }

        };

        AppController.getInstance().getRequestQueue().add(user_req) ;

    }
    void getJob(){
        Log.e("get comments " , "strat") ;

        StringRequest user_req = new StringRequest(Request.Method.POST, Config.BASE_URL+"services.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("get comments  response" , response) ;
                try {

                    if (response.equals("Empty")){
                    }else {
                        JSONArray fedarr = new JSONArray(response) ;
                        for (int i= (0); i<fedarr.length() ; i++){
                            JSONObject temp = fedarr.getJSONObject(i) ;

                            job.add(temp.getString("name"));



                        }

                        adapterjob.notifyDataSetChanged();

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {

        switch (parent.getId()){
            case R.id.signup_user_job:
                //Do something
                ch_job=job.get(pos);
                break;

        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}
