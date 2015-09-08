package com.app.countdowntodolist;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class DatePicker_Fragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    int years;
    int months;
    int days;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        years = c.get(Calendar.YEAR);
        months = c.get(Calendar.MONTH);
        days = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, years, months, days + 1);
    }

    public void onDateSet(DatePicker view, int years, int months, int days) {
        // Do something with the date chosen by the user
        TextView txt = (TextView) getActivity().findViewById(R.id.tvDateSet);
        String d = String.valueOf(days);
        String m = String.valueOf(months);
        String y = String.valueOf(years);
        txt.setText(m + "/" + d + "/" + y);
    }

}
