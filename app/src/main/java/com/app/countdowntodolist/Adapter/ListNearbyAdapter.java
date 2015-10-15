package com.app.countdowntodolist.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.countdowntodolist.Foursquare.Model.Venue;
import com.app.countdowntodolist.R;

import java.util.List;

public class ListNearbyAdapter extends RecyclerView.Adapter<ListNearbyAdapter.ViewHolder> {

    private List<Venue> venueList;
    private Context mContext;


    public ListNearbyAdapter(Context context, List<Venue> dataset) {
        venueList = dataset;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_row_4sq, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        Venue venue = venueList.get(position);
        viewHolder.venueTitle.setText(venue.getName());
        viewHolder.address.setText(venue.getAddress());
        viewHolder.distance.setText(venue.getDistance().toString());


    }

    @Override
    public int getItemCount() {
        return venueList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView venueTitle;
        public TextView address;
        public TextView distance;

        public ViewHolder(View view) {
            super(view);
            venueTitle = (TextView) view.findViewById(R.id.venue_title);
            address = (TextView) view.findViewById(R.id.address_4sq);
            distance = (TextView) view.findViewById(R.id.distance);
        }
    }
}