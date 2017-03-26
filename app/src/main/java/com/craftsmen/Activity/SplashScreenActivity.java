package com.craftsmen.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.craftsmen.AppManger.AppController;
import com.craftsmen.R;

/**
 * Created by lenovo on 2/24/2017.
 */

public class SplashScreenActivity extends Activity
{
    private final int splash_display_length = 3000;

    @Override
    public void onCreate(Bundle icicle){
        super.onCreate(icicle);
        setContentView(R.layout.splashscreen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (AppController.getInstance().getPrefManager().getUser() == null){
                    Intent mainIntent = new Intent(SplashScreenActivity.this,login.class);
                    SplashScreenActivity.this.startActivity(mainIntent);
                    SplashScreenActivity.this.finish();
                }else {
                    Intent mainIntent = new Intent(SplashScreenActivity.this,MainActivity.class);
                    SplashScreenActivity.this.startActivity(mainIntent);
                    SplashScreenActivity.this.finish();
                }

            }
        },splash_display_length);
    }
}
