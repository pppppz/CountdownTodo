package com.app.countdowntodolist;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import org.joda.time.DateTime;

import java.util.Calendar;

public class TimePicker_Fragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    int hours;
    int minutes;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker

        DateTime dateTime = DateTime.now();
        hours = dateTime.getHourOfDay();
        minutes = dateTime.getMinuteOfHour();

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hours, minutes,
                //DateFormat.is24HourFormat(getActivity()));
                false);
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
        // Do something with the time chosen by the user
        String min;


        if (minutes < 10) {
            min = "0" + String.valueOf(minutes);

        } else {
            min = String.valueOf(minutes);
        }

        TextView txt = (TextView) getActivity().findViewById(R.id.tvTimeSet);
        String hour = String.valueOf(hourOfDay);

        txt.setText(hour + ":" + min);



    }

}
