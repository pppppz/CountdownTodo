package com.app.countdowntodolist;


import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.countdowntodolist.Function.switchFragment;
import com.app.countdowntodolist.Model.Task;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.joda.time.DateTime;

import java.util.Calendar;

public class AddTask_Fragment extends Fragment {

    private View view;
    private EditText TaskInput, DesInput;
    private TextView tvDateset, tvTimeset;
    private Button btnSetdate, btnSettime;
    private FloatingActionButton mFab;
    private int year, day, month, hour, minute;
    private FragmentActivity fragmentActivity;
    FloatingActionButton.OnClickListener floatingButtonListener = new FloatingActionButton.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (TaskInput.getText().length() > 0) {

                // Set up a progress dialog
                final ProgressDialog dialog = new ProgressDialog(fragmentActivity);
                dialog.setMessage("updating please wait");
                dialog.show();

                //get data from task_input and set data by method in task.class
                Task t = new Task();
                t.setACL(new ParseACL(ParseUser.getCurrentUser()));
                t.setUser(ParseUser.getCurrentUser());
                t.setTitle(TaskInput.getText().toString());
                t.setCompleted(false);
                t.setDescription(DesInput.getText().toString());
                year = year - 1900;
                DateTime date = new DateTime(year, month, day, hour, minute);
                t.setDeadline(date.toDate());
                //  t.saveEventually(); // save in to parse.com

                t.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        dialog.dismiss();
                        Toast.makeText(fragmentActivity, "Add task completed", Toast.LENGTH_SHORT).show();
                        //finish this class and swap to Main class
                        Fragment fragment = new MainList_Fragment();
                        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();

                        //hidden soft keyboard before swap fragment
                        fragmentActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        new switchFragment(fragment, fragmentManager).doSwitch();

                    }
                });

            }

        }
    };
    Button.OnClickListener btnSetTimeListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogFragment tp = new TimePicker_Fragment() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
                    // Do something with the time chosen by the user
                    String min;
                    if (minutes < 10) {
                        min = "0" + String.valueOf(minutes);

                    } else {
                        min = String.valueOf(minutes);
                    }
                    String hour_text = String.valueOf(hourOfDay);
                    tvTimeset.setText(hour_text + ":" + min);
                    hour = hourOfDay;
                    minute = minutes;
                }
            };
            tp.show(fragmentActivity.getFragmentManager(), "timePicker");
        }
    };
    Button.OnClickListener btnSetDateListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogFragment dp = new DatePicker_Fragment() {
                @Override
                public void onDateSet(DatePicker view, int years, int months, int days) {
                    String d = String.valueOf(day);
                    String m = String.valueOf(month);
                    String y = String.valueOf(year);
                    tvDateset.setText(m + "/" + d + "/" + y);

                    day = days;
                    month = months;
                    year = years;
                }
            };
            dp.show(fragmentActivity.getFragmentManager(), "datePicker");

        }
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.addtask_fragment, container, false);
        fragmentActivity = getActivity();
        TaskInput = (EditText) view.findViewById(R.id.task_input);
        DesInput = (EditText) view.findViewById(R.id.etDescription);
        tvDateset = (TextView) view.findViewById(R.id.tvDateSet);
        tvTimeset = (TextView) view.findViewById(R.id.tvTimeSet);
        btnSetdate = (Button) view.findViewById(R.id.btnSetDate);
        btnSettime = (Button) view.findViewById(R.id.btnSettime);

        //declare and config floating button
        mFab = (FloatingActionButton) view.findViewById(R.id.FAB_ADD_EDIT);


        //set data into
        btnSetdate.setBackgroundResource(R.drawable.ic_time);
        btnSettime.setBackgroundResource(R.drawable.ic_datebtn);

        btnSetdate.setOnClickListener(btnSetDateListener);
        btnSettime.setOnClickListener(btnSetTimeListener);
        mFab.setOnClickListener(floatingButtonListener);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);

        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        month = c.get(Calendar.MONTH);
        minute = c.get(Calendar.MINUTE);
        day = day + 1; // for at least will set deadline tomorrow

        //set into text field
        tvDateset.setText(month + "/" + day + "/" + year);
        tvTimeset.setText(hour + ":" + minute);


    }


}
