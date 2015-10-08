package com.app.countdowntodolist;


import android.app.DialogFragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.countdowntodolist.Function.switchFragment;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.joda.time.DateTime;

import java.util.Calendar;

public class EditTaskFragment extends Fragment {

    private View view;
    private EditText TaskInput, DesInput;
    private TextView tvDateset, tvTimeset;
    private Button btnSetdate, btnSettime;
    private FloatingActionButton mFab;
    private int year, day, month, hour, minute;
    private FragmentActivity fragmentActivity;
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
    private String objectID;
    FloatingActionButton.OnClickListener floatingButtonListener = new FloatingActionButton.OnClickListener() {
        @Override
        public void onClick(View v) {

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");


// Retrieve the object by id
            query.getInBackground(objectID, new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    if (e == null) {
                        // Now let's update it with some new data. In this case, only cheatMode and score
                        // will get sent to the Parse Cloud. playerName hasn't changed.
                        object.put("title", TaskInput.getText().toString());
                        object.put("description", DesInput.getText().toString());
                        DateTime n_date = new DateTime(year, month, day, hour, minute);
                        // Date n_date = new Date(year - 1900, month, day, hour, minute);
                        object.put("deadlineAt", n_date);
                        object.saveInBackground();

                    }
                }
            });


            Toast.makeText(fragmentActivity, "Update completed", Toast.LENGTH_SHORT).show();

            //finish this class and swap to Main class
            Fragment fragment = new TaskList_Fragment();
            FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
            new switchFragment(fragment, fragmentManager).doSwitch();


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
        savedInstanceState = getArguments();
        if (savedInstanceState != null) {
            // then you have arguments
            objectID = getArguments().getString("objectID");
            Log.e("location start ", objectID);
            retrieveData(objectID);
        } else {
            Log.e("location & round", " not set");
        }


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

    public void retrieveData(final String objectID) {

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
        query.getInBackground(objectID, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    // object will be your object data
                    String title = object.getString("title");
                    DateTime date = new DateTime(object.getDate("deadlineAt"));
                    String Description = object.getString("description");

                    Log.e(EditTaskFragment.class.getName(), title);
                    Log.e(EditTaskFragment.class.getName(), String.valueOf(date));
                    Log.e(EditTaskFragment.class.getName(), Description);

                    day = date.getDayOfMonth();
                    month = date.getMonthOfYear();
                    year = date.getYear();
                    hour = date.getHourOfDay();
                    minute = date.getMinuteOfHour();

                    TaskInput.setText(title);
                    DesInput.setText(Description);
                    tvDateset.setText(month + "/" + day + "/" + year);
                    tvTimeset.setText(hour + ":" + minute);


                } else {
                    // something went wrong
                    Log.e("Edittask error ", e.getLocalizedMessage());
                }
            }
        });

    }


}
