package com.craftsmen.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.craftsmen.Adapter.Requests_Adapter;
import com.craftsmen.Adapter.Workers_Adapter;
import com.craftsmen.AppManger.AndroidMultiPartEntity;
import com.craftsmen.AppManger.AppController;
import com.craftsmen.AppManger.Config;
import com.craftsmen.MapUtiles.LatLngInterpolator;
import com.craftsmen.MapUtiles.LocationReq;
import com.craftsmen.MapUtiles.markerAnimator;
import com.craftsmen.Models.Request_item_model;
import com.craftsmen.Models.User_model;
import com.craftsmen.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

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

public class WorkersActivity extends AppCompatActivity  implements OnMapReadyCallback{
    String type ;
    RecyclerView recyclerView ;
    Workers_Adapter workers_adapter ;
    ArrayList <User_model> users = new ArrayList<>();
    ArrayList <Marker> markers = new ArrayList<>();

    View layout_container  ;
    View dialog , add_img  ,add_request_fab;
    View send , cancel;
    private String uploaded_img_name="";
    TextView txtPercentage;
    ProgressBar progressBar ;
    View progressBarView ;
    TextInputLayout desc_layout ;
    EditText description ;
    ImageView picked_image ;
    String lat = "" , lung ="";
    private Button get_location;
    private GoogleMap mMap;
    LocationReq gpsTracker;
    private boolean manual = false;
    private static final int MY_PERMISSIONS_REQUEST_GPS = 66;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workers);

        if (savedInstanceState == null) no_storage_permission();

        picked_image =(ImageView) findViewById(R.id.picked_img);

        desc_layout = (TextInputLayout) findViewById(R.id.desc_layout);
        description = (EditText) findViewById(R.id.desc_input);

        txtPercentage = (TextView) findViewById(R.id.txtPercentage);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBarView = findViewById(R.id.progressBarView);
        dialog = findViewById(R.id.layout_dashboard);

        send = findViewById(R.id.send_btn);
        cancel = findViewById(R.id.cancel_btn);
        add_img = findViewById(R.id.add_img);
        add_request_fab = findViewById(R.id.add_request_fab);
        add_request_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opendialog();
            }
        });
        add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_img();
            }
        });

        get_location = (Button) findViewById(R.id.add_station);
        get_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getApplicationContext(), Get_my_location_activity.class) , Config.GET_LOCATION_REQUEST_CODDE);
            }
        });


        type = getIntent().getExtras().getString("type");


        gpsTracker = new LocationReq(getApplicationContext() , new LocationReq.update_location() {
            @Override
            public void update_location(Location location) {
                onCurrentLocationUpdated(location);
            }
        },1000);

        setupMap();


//        recyclerView = (RecyclerView) findViewById(R.id.workers_list);
//        workers_adapter = new Workers_Adapter(this, users);
//        recyclerView.setAdapter(workers_adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));



    }

    private void setupMap() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.setBuildingsEnabled(true);
        mMap.setIndoorEnabled(true);

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                int x = markers.indexOf(marker);

                open_profile(users.get(x));
            }
        });
        createCurrentPosMarker();

        getUsers(type);





    }

    void open_profile(User_model userModel ){
        Intent intent = new Intent(getApplicationContext() , ProfileActivity.class) ;
        intent.putExtra("extra" , userModel) ;
        startActivity(intent);
    }


    private void onCurrentLocationUpdated(Location location) {
        if (gpsTracker.getLatitude() != 0) {
            LatLng currentLoc = new LatLng(location.getLatitude(), location.getLongitude());
            //currentPosIcon.setRotation((float) SphericalUtil.computeHeading(currentPosIcon.getPosition(),currentLoc));




            if (!manual){
             //   moveCamera(currentLoc);
            gpsTracker.disconnect();
            }



        }
    }

    private void moveCamera(LatLng current) {
        // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(current)      // Sets the center of the map to Mountain View
                .zoom(13)                   // Sets the zoom
                .bearing(30)                // Sets the orientation of the camera to east
                .tilt(90)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 3000, null);
    }

    @Override
    protected void onDestroy() {
        gpsTracker.disconnect();
        super.onDestroy();
    }

    void getUsers(final String type ){
        findViewById(R.id.progressBarh).setVisibility(View.VISIBLE);
        StringRequest user_req = new StringRequest(Request.Method.POST, Config.BASE_URL+"workers.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("res worker" , response) ;
                try {

                    if (response.trim().equals("Empty")){
                        Toast.makeText(getApplicationContext(),"no workers nearby",Toast.LENGTH_LONG).show();
                    }else {
                        JSONArray fedarr = new JSONArray(response) ;
                        for (int i=0 ; i<fedarr.length() ; i++){
                            JSONObject temp = fedarr.getJSONObject(i) ;

                            User_model user_model = new User_model() ;
                            user_model.setU_Id(temp.getString("id"));
                            user_model.setU_Pic(temp.getString("user_image"));
                            user_model.setU_name(temp.getString("name"));

                            user_model.setU_p_num(temp.getString("phone"));
                            user_model.setU_type(temp.getString("type"));
                            double d = Double.parseDouble(temp.getString("distance"));
                            int dm = (int) (d*1000) ;
                            user_model.setDistance(dm + " meter ");





                            try {
                                user_model.setLat(temp.getString("lat"));
                                user_model.setLung(temp.getString("lung"));
                            }catch (NullPointerException e){}


                            user_model.setU_service(temp.getString("job"));

                            Marker temp_marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(
                                            Double.valueOf(user_model.getLat() )
                                            ,Double.valueOf(user_model.getLung() )
                                        )
                                    )
                                    .title(user_model.getU_name())
                                    .snippet(user_model.getDistance()) );
                            markers.add(temp_marker);
                            temp_marker.showInfoWindow();
                            users.add(user_model);
                        }
                        findViewById(R.id.progressBarh).setVisibility(View.GONE);



                    }
                }catch (Exception e) {
                    Log.e("login response  err" , e.toString()) ;
                    findViewById(R.id.progressBarh).setVisibility(View.GONE);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("login err" , error.toString()) ;
                        findViewById(R.id.progressBarh).setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"no internet connection" , Toast.LENGTH_LONG).show();

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String , String> parmas = new HashMap<>() ;
//                parmas.put("city" , AppController.getInstance().getPrefManager().getUser().getU_city()) ;
                parmas.put("service_type" ,type) ;
                parmas.put("lat" ,AppController.getInstance().getPrefManager().getUser().getLat()) ;
                parmas.put("lung" ,AppController.getInstance().getPrefManager().getUser().getLung()) ;
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


    public void opendialog(){
        lat = AppController.getInstance().getPrefManager().getUser().getLat() ;
        lung = AppController.getInstance().getPrefManager().getUser().getLung() ;
        dialog.setVisibility(View.VISIBLE);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_request(type);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setVisibility(View.GONE);
            }
        });
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
            File outputDir = WorkersActivity.this.getCacheDir(); // context being the Activity pointer
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
                picked_image.setImageBitmap(resized_bitmap);
                validateImg();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(WorkersActivity.this);
            builder.setMessage(message).setTitle("Response from Servers")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // do nothing
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            alert.getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimaryDark));



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

    boolean validate_lat (){
        if (lat.trim().equals(null) || lat.trim().isEmpty()){
            findViewById(R.id.loc_err).setVisibility(View.VISIBLE);
            return false ;
        }
        findViewById(R.id.loc_err).setVisibility(View.GONE);
        return true ;
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

    void get_img(){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, Config.RESULT_LOAD_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /// select from gallery section
        if (requestCode == Config.RESULT_LOAD_IMAGE && resultCode == WorkersActivity.this.RESULT_OK && data!=null){

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Log.e("Uri",selectedImage + "");
            Log.e("filePathColumn",MediaStore.Images.Media.DATA + "");

            Cursor cursor = WorkersActivity.this.getContentResolver().query(selectedImage,
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


    void add_request (final String type) {

        if (!validate_description())
            return;

        if (!validateImg()) {
            uploaded_img_name ="";
        }

        if (!validate_lat()) {
            return;
        }
        findViewById(R.id.loadingSpinner).setVisibility(View.VISIBLE);

        StringRequest requesturl = new StringRequest(Request.Method.POST, Config.BASE_URL+"add_request.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                dialog.setVisibility(View.GONE);
                description.setText("");
                picked_image.setImageBitmap(null);
                lat = AppController.getInstance().getPrefManager().getUser().getLat() ;
                lung = AppController.getInstance().getPrefManager().getUser().getLung() ;

                Log.e("login response  " , response.toString()) ;


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("login response  err" , error.toString()) ;
            }
        }
        ) {

            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", AppController.getInstance().getPrefManager().getUser().getU_Id());
                params.put("service_type", type);
                params.put("description", description.getText().toString().trim());
                params.put("lat", lat);
                params.put("lung", lung);
                params.put("image",uploaded_img_name);
                Log.e("request prams  err" , params.toString()) ;
                return params;
            }
        };
        int socketTimeout = 10000; // 10 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                10,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        requesturl.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(WorkersActivity.this);
        requestQueue.add(requesturl);

    }

    boolean validate_description (){
        if (description.getText().toString().trim().equals(null) || description.getText().toString().trim().isEmpty()){
            desc_layout.setError("You have to enter a city");
            return false ;
        }
        desc_layout.setErrorEnabled(false);
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
                    Toast.makeText(WorkersActivity.this, "Permission needed to run your app", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            case MY_PERMISSIONS_REQUEST_GPS: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    //  no_gps_permition();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permission needed to run your app", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    private void no_storage_permission() {
        // Here, thisActivity is the current activity
        if (ActivityCompat.checkSelfPermission(WorkersActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                ){



            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(WorkersActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION }
                    , Config.MY_PERMISSIONS_REQUEST_STORAGE);


        }


    }


    private void createCurrentPosMarker() {
        BitmapDrawable home =(BitmapDrawable) ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_home_pin);
        home.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimaryDarkd), PorterDuff.Mode.SRC_ATOP);

        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(home.getBitmap());
            LatLng latLng = new LatLng(
                    Double.valueOf(AppController.getInstance().getPrefManager().getUser().getLat())
                    ,Double.valueOf(AppController.getInstance().getPrefManager().getUser().getLung()));
        MarkerOptions markerOptions = null;
        markerOptions = new MarkerOptions().position(latLng).icon(icon);


        moveCamera(latLng);
       mMap.addMarker(markerOptions);
    }


}
