package com.app.countdowntodolist.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.countdowntodolist.Model.Task;
import com.app.countdowntodolist.R;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.List;

public class ListTaskAdapter extends RecyclerView.Adapter<ListTaskAdapter.ViewHolder> {

    private List<Task> tasks;
    private Context mContext;


    public ListTaskAdapter(Context context, List<Task> dataset) {
        tasks = dataset;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        Task task = tasks.get(position);
        viewHolder.titleTask.setText(task.getTitle());

        /// Calculate countdown time
        task.getdateDeadline();
        DateTime thisTime = DateTime.now();
        DateTime thatTime = new DateTime(task.getdateDeadline());

        // Seconds seconds = Seconds.secondsBetween(thisTime, thatTime);
        final Duration duration = new Duration(thisTime, thatTime);


        final long durationMili = duration.getMillis();
        int countTime;

        if (durationMili > 172800000) { // more than 2 day reduce every 1 hrs
            countTime = 3600000;
        } else if (durationMili > 3600000) { // more than 1 hrs reduce reduce every 1 minute
            countTime = 3600000;
        } else if (durationMili > 60000) { // more than 1 minute reduce every 1 minutes
            countTime = 60000;
        } else { // below 1 minute count every second
            countTime = 1000;
        }


        new CountDownTimer(durationMili, countTime) {

            public void onTick(long millisUntilFinished) {


                if (durationMili > 172800000) { // if  more than  2 days
                    long hour = duration.getStandardHours() % 24;
                    viewHolder.dateTask.setText(duration.getStandardDays() + " Days " + hour + " hours");
                    viewHolder.dateTask.setTextColor(Color.parseColor("#FFFFFF"));
                } else if (durationMili > 3600000) { //if more than 1 hours
                    long minute = duration.getStandardMinutes() % 60;
                    viewHolder.dateTask.setText(duration.getStandardHours() + " Hours " + minute + " minutes");
                    viewHolder.dateTask.setTextColor(Color.parseColor("#C62828"));
                } else if (durationMili > 60000) { // if more than 1 Minutes
                    viewHolder.dateTask.setTextColor(Color.parseColor("#C62828"));
                    viewHolder.dateTask.setText(duration.getStandardMinutes() + " Minutes");
                } else {
                    viewHolder.dateTask.setTextColor(Color.parseColor("#C62828"));
                    viewHolder.dateTask.setText(duration.getStandardSeconds() + " Seconds");
                }

            }

            public void onFinish() {
                viewHolder.dateTask.setText("done!");
                viewHolder.dateTask.setTextColor(Color.parseColor("#4CAF50"));
            }
        }.start();

        if (task.isCompleted()) {
            viewHolder.dateTask.setPaintFlags(viewHolder.titleTask.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.dateTask.setPaintFlags(viewHolder.titleTask.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            viewHolder.dateTask.setPaintFlags(viewHolder.titleTask.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            viewHolder.dateTask.setPaintFlags(viewHolder.titleTask.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTask;
        public TextView dateTask;

        public ViewHolder(View view) {
            super(view);
            titleTask = (TextView) view.findViewById(R.id.task_title);
            dateTask = (TextView) view.findViewById(R.id.task_time);
        }
    }
}