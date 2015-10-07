package com.app.countdowntodolist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Location_Fragment extends Fragment {

    private View view;
    private FragmentActivity fragmentActivity;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_location, container, false);
        fragmentActivity = getActivity();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}
