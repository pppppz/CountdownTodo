package com.app.countdowntodolist;

import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.countdowntodolist.Adapter.ListTaskAdapter;
import com.app.countdowntodolist.Function.switchFragment;
import com.app.countdowntodolist.Listener.RecyclerItemClickListener;
import com.app.countdowntodolist.Listener.RecyclerViewOnScrollListener;
import com.app.countdowntodolist.Model.Task;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MainList_Fragment extends Fragment {


    public static SwipeRefreshLayout PullToRefresh;
    public static RecyclerView mRecyclerView;
    public static LinearLayoutManager layoutManager;
    private View view;
    private FragmentActivity fragmentActivity;
    private FloatingActionButton fabBtn;
    View.OnClickListener fabOnClick = new FloatingActionButton.OnClickListener() {

        @Override
        public void onClick(View v) {
            new switchFragment(new AddTask_Fragment(), fragmentActivity.getSupportFragmentManager()).doSwitch();
            fabBtn.hide();
        }
    };
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Task> mData = new ArrayList<>();

    RecyclerView.OnItemTouchListener recycleViewOnTouchListener = new RecyclerItemClickListener(fragmentActivity, new RecyclerItemClickListener.OnItemClickListener() {
        @Override
        public void onItemClick(View view, final int position) {

            final Task task = getItem(position);
            final TextView taskTitle = (TextView) view.findViewById(R.id.task_title);
            final TextView taskTime = (TextView) view.findViewById(R.id.task_time);


            final CharSequence[] items = {"Set paint flag", "Delete task", "Edit Task"};

            final AlertDialog.Builder builder = new AlertDialog.Builder(fragmentActivity);
            builder.setTitle("Make your selection");
            AlertDialog.Builder builder1 = builder.setItems(items, new DialogInterface.OnClickListener() {


                public void onClick(DialogInterface dialog, int item) {
                    //check if equal = set paint flag
                    if (items[item] == items[0]) {
                        // set opposite boolean
                        task.setCompleted(!task.isCompleted());

                        // if boolean = complete set flag -----------
                        if (task.isCompleted()) {
                            taskTitle.setPaintFlags(taskTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            taskTime.setPaintFlags(taskTime.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            Toast.makeText(fragmentActivity, "Set task completed", Toast.LENGTH_SHORT).show();
                        } else {
                            taskTitle.setPaintFlags(taskTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                            taskTime.setPaintFlags(taskTime.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                            Toast.makeText(fragmentActivity, "Set task not complete", Toast.LENGTH_SHORT).show();
                        }
                        //save it to parse.com
                        task.saveEventually();

                    }

                    // check if equal edit task
                    else if (items[item] == items[2]) {
                        //get objectid
                        String object = task.getObjectId();
                        Log.e("MainList_Fragment ", object);

                        //start new activity and swap it

                        Bundle bundle = new Bundle(); //  bundle function is management resource , state
                        bundle.putString("objectID", object);
                        Fragment fragment = new EditTaskFragment();
                        fragment.setArguments(bundle);
                        new switchFragment(fragment, fragmentActivity.getSupportFragmentManager()).doSwitch();
                    } else {

                        // create dialog for ask to delete
                        AlertDialog.Builder builder = new AlertDialog.Builder(fragmentActivity);
                        builder.setTitle("Delete");
                        builder.setMessage("Are you sure?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        try {
                                            task.delete();
                                            Toast.makeText(fragmentActivity, "Deleted", Toast.LENGTH_SHORT).show();
                                            mData.remove(position);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        updateData();
                                        Log.e("items ", "deleted");
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }
    );
    SwipeRefreshLayout.OnRefreshListener pullToRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

            updateData();

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

        updateData();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public Task getItem(int position) {
        return mData.get(position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mainlist_fragment, container, false);
        fragmentActivity = getActivity();

        fabBtn = (FloatingActionButton) view.findViewById(R.id.FAB_MAIN);
        fabBtn.setOnClickListener(fabOnClick);

        PullToRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);

        PullToRefresh.setOnRefreshListener(pullToRefreshListener);
        PullToRefresh.setColorSchemeColors(R.color.orange, R.color.green, R.color.blue);
        PullToRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_red_light, android.R.color.white); //set theme loading when pull


        // declare list view and set adapter to list view (UI)
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(fragmentActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();


        // create adapter for put on array from class task
        mAdapter = new ListTaskAdapter(fragmentActivity, mData);
        mAdapter.notifyDataSetChanged(); //set for check if data in array list change it's ll refresh
        mRecyclerView.setAdapter(mAdapter);


        //set list view can listen the event when click some row
        mRecyclerView.addOnItemTouchListener(recycleViewOnTouchListener);
        mRecyclerView.addOnScrollListener(new RecyclerViewOnScrollListener());

        return view;
    }

    public void updateData() {

        //pattern query
        ParseQuery<Task> query = ParseQuery.getQuery(Task.class);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.addDescendingOrder("createdAt");
        //  query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);

        query.findInBackground(new FindCallback<Task>() {
            @Override
            public void done(List<Task> tasks, ParseException error) {
                //check if list task have some data = clear
                mData.clear();
                if (error == null) {
                    for (int i = 0; i < tasks.size(); i++) {
                        mData.add(tasks.get(i));
                    }
                } else {
                    Log.e("ParseException : ", String.valueOf(error));
                }
            }
        });
        PullToRefresh.setRefreshing(false);
        mAdapter.notifyDataSetChanged();


    }

}
