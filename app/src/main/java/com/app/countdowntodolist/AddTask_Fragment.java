package com.app.countdowntodolist;


import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.app.countdowntodolist.Model.Task;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.joda.time.DateTime;

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

                /**  Set up a progress dialog **/
                final ProgressDialog dialog = new ProgressDialog(fragmentActivity);
                dialog.setMessage("updating please wait");
                dialog.show();

                /** get data from task_input and set data by method in task.class **/
                Task t = new Task();
                t.setACL(new ParseACL(ParseUser.getCurrentUser()));
                t.setUser(ParseUser.getCurrentUser());
                t.setTitle(TaskInput.getText().toString());
                t.setCompleted(false);
                t.setDescription(DesInput.getText().toString());


                DateTime date = new DateTime(year, month, day, hour, minute);
                t.setDeadline(date.toDate());
                t.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        /** hidden soft keyboard before swap fragment **/
                        InputMethodManager inputManager = (InputMethodManager) fragmentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        dialog.dismiss();

                        /** finish this class and swap to Main class **/
                        TaskList_Fragment.fabBtn.show();
                        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
                        fragmentManager.beginTransaction().remove(AddTask_Fragment.this).commit();
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
                    Log.e("onTimeSet ", hourOfDay + ":" + minutes);
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
        TaskList_Fragment.fabBtn.hide();
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

        DateTime dateTime = DateTime.now();
        year = dateTime.getYear();

        day = dateTime.getDayOfMonth();
        hour = dateTime.getHourOfDay();
        month = dateTime.getMonthOfYear();
        minute = dateTime.getMinuteOfHour();
        day = day + 1; // for at least will set deadline tomorrow


        //set into text field
        tvDateset.setText(month + "/" + day + "/" + year);
        if (minute < 10 ){

            tvTimeset.setText(hour + ":0" + minute);

        }else{
            tvTimeset.setText(hour + ":" + minute);
        }

    }


}
