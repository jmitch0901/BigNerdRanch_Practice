package com.jonathanmitchell.bignerdranchpractice.CriminalIntent;

import com.jonathanmitchell.bignerdranchpractice.*;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TimePicker;

/**
 * Created by jonathanmitchell on 1/3/15.
 */
public class TimePickerFragment extends DialogFragment {



    /*
        Come Back Later....
     */



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_time,null);
        TimePicker timePicker = (TimePicker)v.findViewById(R.id.dialog_date_timePicker);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.time_picker_title)
                .setPositiveButton(
                        android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }
                )
                .create();

    }


}
