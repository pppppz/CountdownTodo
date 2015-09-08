package com.app.countdowntodolist;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePicker_Fragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    int hours;
    int minutes;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        hours = c.get(Calendar.HOUR_OF_DAY);
        minutes = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hours, minutes,
                DateFormat.is24HourFormat(getActivity()));
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
