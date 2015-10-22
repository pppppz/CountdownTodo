package com.app.countdowntodolist;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.countdowntodolist.Adapter.ListEventAdapter;
import com.app.countdowntodolist.Listener.RecyclerViewOnScrollListener;
import com.app.countdowntodolist.Model.Current;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;


public class Event_Fragment extends Fragment {


    public static RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private FragmentActivity fragmentActivity;
    View.OnClickListener fabOnClick = new FloatingActionButton.OnClickListener() {

        @Override
        public void onClick(View v) {

            /**addToBackStack for when add task activity has completed will be back into this page.**/
            FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
            Fragment fragment = new AddEvent_Fragment();
            fragmentManager.beginTransaction().add(R.id.frame_container, fragment).addToBackStack(null).commit();
        }
    };
    private SwipeRefreshLayout swipeRefresh;
    private FloatingActionButton fabBtn;
    private String TAG = "Event_Fragment";
    private List<Current> mData = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    SwipeRefreshLayout.OnRefreshListener pullToRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

            swipeRefresh.setRefreshing(true);
            updateData();
            swipeRefresh.setRefreshing(false);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentActivity = getActivity();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_fragment, container, false);


        /**declare list view and set adapter to list view (UI)**/
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_event);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(fragmentActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();

        /** create adapter for put on array from class task **/
        //   mAdapter = new ListNearbyAdapter(fragmentActivity, nearbyList);
        // mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new SlideInUpAnimator());


        /**set list view can listen the event when click some row **/
        // mRecyclerView.addOnItemTouchListener(recycleViewOnTouchListener);
        mRecyclerView.addOnScrollListener(new RecyclerViewOnScrollListener());
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container_event);
        swipeRefresh.setOnRefreshListener(pullToRefreshListener);

        /** create adapter for put on array from class task **/
        mAdapter = new ListEventAdapter(fragmentActivity, mData);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new SlideInUpAnimator());

        /**floating button**/
        fabBtn = (FloatingActionButton) view.findViewById(R.id.Fab_Event);
        fabBtn.setOnClickListener(fabOnClick);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateData();

    }

    private void updateData() {

//        dialog.show();
        //pattern query
        ParseQuery<Current> query = ParseQuery.getQuery(Current.class);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.addDescendingOrder("createdAt");


        query.findInBackground(new FindCallback<Current>() {
            @Override
            public void done(List<Current> currents, ParseException error) {
                //check if list task have some data = clear
                mData.clear();
                if (error == null) {
                    for (int i = 0; i < currents.size(); i++) {
                        Current c = currents.get(i);
                        Log.d("ptest", "current " + c.getTitle());
                        mData.add(currents.get(i));
                    }
                    //   swipeRefresh.setRefreshing(false);
                    mAdapter.notifyDataSetChanged();
                    Log.d("ptest", "updated data");
                } else {

                    Log.e("ParseException : ", String.valueOf(error));
                }
                //   dialog.dismiss();

            }
        });

    }

}
