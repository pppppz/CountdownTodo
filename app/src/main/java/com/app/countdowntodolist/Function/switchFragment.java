package com.app.countdowntodolist.Function;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.app.countdowntodolist.R;


public class switchFragment extends FragmentActivity {

    Fragment fragment;
    FragmentManager fragment_manager;


    public switchFragment(Fragment f, FragmentManager fm) {
        this.fragment = f;
        this.fragment_manager = fm;
    }

    public void doSwitch() {

        //switch fragment & add to back stack (for user press back then go back to before fragment)
        fragment_manager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
        Log.e(switchFragment.class.getName() , "Switching to " + fragment.getClass().getName() + "complete");
    }
}
