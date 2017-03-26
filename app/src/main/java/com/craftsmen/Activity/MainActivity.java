package com.craftsmen.Activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.craftsmen.Adapter.ViewPagerAdapter;
import com.craftsmen.AppManger.AppController;
import com.craftsmen.Fragments.DashboardFragment_User;
import com.craftsmen.Fragments.MessageFragment;
import com.craftsmen.Fragments.NotificationFragment;
import com.craftsmen.Fragments.OrdersFragment;
import com.craftsmen.R;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar ;
    ViewPager view_pager ;
    TabLayout tablayout ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // start ini
        toolbar = (Toolbar) findViewById(R.id.MainActivity_toolbar) ;
        setSupportActionBar(toolbar);

        view_pager = (ViewPager) findViewById(R.id.MainActivity_viewpager) ;
        tablayout = (TabLayout) findViewById(R.id.MainActivity_tabLayout) ;
        // end ini

        setupViewPager();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_ac_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile_action:
               startActivity(new Intent(getApplicationContext() , ProfileActivity.class));
                return true;
            case R.id.logout_action:
                AppController.getInstance().getPrefManager().clear();
                startActivity(new Intent(getApplicationContext() , login.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void setupViewPager(){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.setTitleEnabel(true);
        if (AppController.getInstance().getPrefManager().getUser().getU_type().equals("user")){
            viewPagerAdapter.addFragment(new DashboardFragment_User() , "Home Page");

        }else {
            viewPagerAdapter.addFragment(new OrdersFragment() , "Home Page");

        }
        viewPagerAdapter.addFragment(new MessageFragment() , "Messages");
        viewPagerAdapter.addFragment(new NotificationFragment() , "Notifications");

        view_pager.setAdapter(viewPagerAdapter);
        tablayout.setupWithViewPager(view_pager);
    }
}
