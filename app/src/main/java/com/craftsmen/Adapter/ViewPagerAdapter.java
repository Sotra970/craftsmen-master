package com.craftsmen.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by omar_pc on 2/20/2017.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> fragments = new ArrayList<>() ;
    ArrayList<String> titles = new ArrayList<>() ;
    boolean isTitleEnabel = false ;
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public boolean isTitleEnabel() {
        return isTitleEnabel;
    }

    public void setTitleEnabel(boolean titleEnabel) {
        isTitleEnabel = titleEnabel;
    }

    public  void addFragment(Fragment fragment , String title){
        fragments.add(fragment) ;
        titles.add(title) ;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (isTitleEnabel)
        return titles.get(position);
        else return "" ;
    }
}
