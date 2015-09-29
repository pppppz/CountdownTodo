package com.app.countdowntodolist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class User_Fragment extends Fragment {

    private View view;
    private TextView tvDateset, tvTimeset;
    private FragmentActivity fragmentActivity;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.user_fragment, container, false);
        fragmentActivity = getActivity();

        tvDateset = (TextView) view.findViewById(R.id.tvDateSet);




        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}
