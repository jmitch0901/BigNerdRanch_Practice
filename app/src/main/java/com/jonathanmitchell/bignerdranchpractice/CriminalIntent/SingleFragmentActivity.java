package com.jonathanmitchell.bignerdranchpractice.CriminalIntent;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.jonathanmitchell.bignerdranchpractice.*;

/**
 * Created by jonathanmitchell on 1/2/15.
 */
public abstract class SingleFragmentActivity extends ActionBarActivity {

    protected abstract Fragment createFragment();


    protected int getLayoutRedId(){
        return R.layout.activity_fragment;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_fragment);
        setContentView(getLayoutRedId());

        FragmentManager fm = getSupportFragmentManager();
        Fragment aFrag = fm.findFragmentById(R.id.fragmentContainer);

            if(aFrag == null){
            aFrag = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer,aFrag)
                    .commit();
        }
    }




}
