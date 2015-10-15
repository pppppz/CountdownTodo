package com.app.countdowntodolist;


import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.countdowntodolist.Adapter.ListNearbyAdapter;
import com.app.countdowntodolist.Foursquare.Criterias.VenuesCriteria;
import com.app.countdowntodolist.Foursquare.FoursquareConstants;
import com.app.countdowntodolist.Foursquare.Main.FoursquareVenuesNearbyRequest;
import com.app.countdowntodolist.Foursquare.Model.Venue;
import com.app.countdowntodolist.Function.getCurrentLocation;
import com.app.countdowntodolist.Listener.RecyclerViewOnScrollListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class Location_Fragment extends Fragment {

    public static LinearLayoutManager layoutManager;
    public static RecyclerView mRecyclerView;
    private String mAccessToken = "";
    private FragmentActivity fragmentActivity;
    private RecyclerView.Adapter mAdapter;
    private List<Venue> nearbyList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefresh;
    SwipeRefreshLayout.OnRefreshListener pullToRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            getLocation();
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
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        /**declare list view and set adapter to list view (UI)**/
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_location);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(fragmentActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();

        /** create adapter for put on array from class task **/
        mAdapter = new ListNearbyAdapter(fragmentActivity, nearbyList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new SlideInUpAnimator());


        /**set list view can listen the event when click some row **/
        // mRecyclerView.addOnItemTouchListener(recycleViewOnTouchListener);
        mRecyclerView.addOnScrollListener(new RecyclerViewOnScrollListener());
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container_location);
        swipeRefresh.setOnRefreshListener(pullToRefreshListener);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getLocation();
    }

    public void getVenuesNearby(VenuesCriteria criteria) {

        FoursquareVenuesNearbyRequest request = new FoursquareVenuesNearbyRequest(fragmentActivity, criteria);
        request.execute(getAccessToken());

        List<Venue> venues;
        try {
            venues = request.get();
            for (int i = 0; i < venues.size(); i++) {
                Venue v = venues.get(i);
                nearbyList.add(v);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private String getAccessToken() {
        if (mAccessToken.equals("")) {
            SharedPreferences settings = fragmentActivity.getSharedPreferences(FoursquareConstants.SHARED_PREF_FILE, 0);
            mAccessToken = settings.getString(FoursquareConstants.ACCESS_TOKEN, "");
        }
        return mAccessToken;
    }

    private void getLocation() {

        /** setting venues criteria **/
        VenuesCriteria venuesCriteria = new VenuesCriteria();
        venuesCriteria.setQuantity(5);
        venuesCriteria.setRadius(1000);
        venuesCriteria.setIntent(VenuesCriteria.VenuesCriteriaIntent.BROWSE);
        Location locationHere = getCurrentLocation.get(getActivity());
        venuesCriteria.setLocation(locationHere);

        /** get venues **/
        getVenuesNearby(venuesCriteria);
        mAdapter.notifyDataSetChanged();
    }
}
