package com.app.countdowntodolist.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.countdowntodolist.Model.Current;
import com.app.countdowntodolist.R;
import com.parse.ParseFile;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

public class ListEventAdapter extends RecyclerView.Adapter<ListEventAdapter.ViewHolder> {

    private List<Current> currentList;
    private Context mContext;


    public ListEventAdapter(Context context, List<Current> dataset) {
        currentList = dataset;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_row_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {


        Current current = currentList.get(position);
        DateTime dateTime = new DateTime(current.getCreatedAt());
        DateTimeFormatter fmt;
        if (DateFormat.is24HourFormat(mContext)) {
            fmt = DateTimeFormat.forPattern("HH:mm");
        } else {
            fmt = DateTimeFormat.forPattern("hh:mm a");
        }
        String dt = dateTime.toString(fmt);
        viewHolder.time.setText(dt);
        viewHolder.title.setText(current.getName());
        viewHolder.address.setText(current.getAddress());
        viewHolder.temp.setText(current.getTemp());


        try {
            ParseFile parseFile = current.getParseFile("image");
            byte[] data = parseFile.getData();
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            viewHolder.img.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return currentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView time;
        public TextView title;
        public TextView address;
        public TextView temp;
        public ImageView img;

        public ViewHolder(View view) {
            super(view);
            time = (TextView) view.findViewById(R.id.event_time);
            title = (TextView) view.findViewById(R.id.event_title);
            address = (TextView) view.findViewById(R.id.event_address);
            temp = (TextView) view.findViewById(R.id.event_temp);
            img = (ImageView) view.findViewById(R.id.event_img);
        }
    }
}