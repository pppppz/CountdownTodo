package com.app.countdowntodolist;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.countdowntodolist.Adapter.ListMenuAdapter;
import com.app.countdowntodolist.Listener.RecyclerItemClickListener;
import com.app.countdowntodolist.Model.Menu;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class User_Fragment extends Fragment {

    public LinearLayoutManager layoutManager;
    CircleImageView mProfileImage;
    TextView mUsername, mEmailID;
    ParseUser parseUser;
    private View view;
    private FragmentActivity fragmentActivity;
    RecyclerView.OnItemTouchListener recycleViewOnTouchListener = new RecyclerItemClickListener(fragmentActivity, new RecyclerItemClickListener.OnItemClickListener() {
        @Override
        public void onItemClick(View view, final int position) {

            Fragment fragment;
            Intent intent;

            switch (position) {
                case 0:
                    intent = new Intent(fragmentActivity, SettingsActivity.class);
                    startActivity(intent);
                case 1:
                    fragment = new TaskList_Fragment();
                    FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
                    fragmentManager.beginTransaction().add(R.id.frame_container, fragment).addToBackStack(null).commit();
                case 2:
                    Toast.makeText(fragmentActivity, "case : " + position, Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(fragmentActivity, "case : " + position, Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(fragmentActivity, "case : " + position, Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    ParseUser.logOut();
                    // show login screen
                    intent = new Intent(fragmentActivity, LoginActivity.class);
                    startActivity(intent);
                    fragmentActivity.finish();
            }
        }
    }
    );
    private LinearLayout Settings;
    private ListView listView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Menu> listMenu;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.user_fragment, container, false);
        fragmentActivity = getActivity();
        mProfileImage = (CircleImageView) view.findViewById(R.id.profile_image);

        /**declare list view and set adapter to list view (UI)**/
        mRecyclerView = (RecyclerView) view.findViewById(R.id.listView);
        mLayoutManager = new LinearLayoutManager(fragmentActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();

        /** Setting List **/
        listMenu = new ArrayList<>();
        Menu app_setting = new Menu("App Settings", R.drawable.ic_communities);
        Menu Photo = new Menu("Photo", R.drawable.ic_communities);
        Menu HelpCenter = new Menu("Help Center", R.drawable.ic_communities);
        Menu Report = new Menu("Report a problem", R.drawable.ic_communities);
        Menu About = new Menu("About", R.drawable.ic_communities);
        Menu Logout = new Menu("Logout", R.drawable.ic_communities);
        listMenu.add(app_setting);
        listMenu.add(Photo);
        listMenu.add(HelpCenter);
        listMenu.add(Report);
        listMenu.add(About);
        listMenu.add(Logout);

        /** create adapter for put on array from class task **/
        mAdapter = new ListMenuAdapter(fragmentActivity, listMenu);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new SlideInUpAnimator());
        mUsername = (TextView) view.findViewById(R.id.txt_name);

        /**set list view can listen the event when click some row **/
        mRecyclerView.addOnItemTouchListener(recycleViewOnTouchListener);


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

        mUsername.setText(parseUser.getString("name"));
    }

    public Menu getItem(int position) {
        return listMenu.get(position);
    }

}
