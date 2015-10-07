package com.jonathanmitchell.bignerdranchpractice.RemoteControl;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;

import com.jonathanmitchell.bignerdranchpractice.SingleFragmentActivity;

/**
 * Created by jonathanmitchell on 1/8/15.
 */
public class RemoteControlActivity extends SingleFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment createFragment() {
        return new RemoteControlFragment();
    }
}
