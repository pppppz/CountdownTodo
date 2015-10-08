package com.app.countdowntodolist;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class User_Fragment extends Fragment {

    CircleImageView mProfileImage;
    TextView mUsername, mEmailID;
    ParseUser parseUser;
    private View view;
    private FragmentActivity fragmentActivity;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.user_fragment, container, false);
        fragmentActivity = getActivity();

        mProfileImage = (CircleImageView) view.findViewById(R.id.profile_image);

        mUsername = (TextView) view.findViewById(R.id.txt_name);
        mEmailID = (TextView) view.findViewById(R.id.txt_email);

        return view;
    }

    @Override
    public void onResume() {

        super.onResume();
        getUserDetailsFromParse();


    }

    public void getUserDetailsFromParse() {
        parseUser = ParseUser.getCurrentUser();

        //Fetch profile photo
        try {
            ParseFile parseFile = parseUser.getParseFile("profileThumb");
            byte[] data = parseFile.getData();
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            mProfileImage.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mEmailID.setText(parseUser.getEmail());
        mUsername.setText(parseUser.getUsername());

    }


}
