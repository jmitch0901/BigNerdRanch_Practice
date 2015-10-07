package com.jonathanmitchell.bignerdranchpractice.CriminalIntent.Camera;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

import com.jonathanmitchell.bignerdranchpractice.*;
import com.jonathanmitchell.bignerdranchpractice.CriminalIntent.SingleFragmentActivity;

/**
 * Created by jonathanmitchell on 1/6/15.
 */
public class CrimeCameraActivity extends SingleFragmentActivity{

    @Override
    protected Fragment createFragment() {

        getSupportActionBar().hide();
        return new CrimeCameraFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Hide the window title.
        requestWindowFeature(Window.FEATURE_NO_TITLE); //This is most likely depricated?
        //getSupportActionBar().hide();

        //Hide the status bar and other OS-level chrome
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);



        super.onCreate(savedInstanceState);
    }
}
