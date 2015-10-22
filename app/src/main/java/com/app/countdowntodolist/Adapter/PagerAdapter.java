package com.app.countdowntodolist.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.app.countdowntodolist.Event_Fragment;
import com.app.countdowntodolist.User_Fragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
 
    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }
 
    @Override
    public Fragment getItem(int position) {


        switch (position) {
            case 0:
                return new Event_Fragment();
            case 1:
                return new User_Fragment();
            default:
                return new Event_Fragment();
        }
    }
 
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}