package com.jonathanmitchell.bignerdranchpractice.CriminalIntent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;

import com.jonathanmitchell.bignerdranchpractice.*;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by jonathanmitchell on 1/2/15.
 */
public class DatePickerFragment extends DialogFragment {


    public static final String EXTRA_DATE = "com.jonathanmitchell.bignerdranchpractice.CriminalIntent.date";
    private Date mDate;

    public static DatePickerFragment newInstance(Date date){

        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATE,date);

        DatePickerFragment dateFrag = new DatePickerFragment();
        dateFrag.setArguments(args);

        return dateFrag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Get the arguments on creation.
        mDate = (Date)getArguments().getSerializable(EXTRA_DATE);

        //Also, Create a calendar to get the year, month, and day!
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);

        /*
            GOAL: Use the calendar class WITH my date to isolate the year, month, and day
            INDEPENDENTLY.
         */

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        //Inflate the View and set this view as the dialog we will use.
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_date,null);

        DatePicker datePicker = (DatePicker)v.findViewById(R.id.dialog_date_datePicker);
        datePicker.init(year,month,day,new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //Translate year,month, day into a Date object USING a calendar
                mDate = new GregorianCalendar(year,monthOfYear,dayOfMonth).getTime();

                //UPDATE argument to preserve selected value ON ROTATION!
                getArguments().putSerializable(EXTRA_DATE,mDate);
            }
        });


        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                //.setPositiveButton(android.R.string.ok,null)
                .setPositiveButton(
                        android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendResult(Activity.RESULT_OK);
                            }
                        }
                )
                .create();

    }

    private void sendResult(int resultCode){

        if(getTargetFragment()==null)
            return;

        Intent i = new Intent();
        i.putExtra(EXTRA_DATE,mDate);

        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,i);

    }



}
