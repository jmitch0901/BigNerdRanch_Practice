package com.jonathanmitchell.bignerdranchpractice.CriminalIntent;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.jonathanmitchell.bignerdranchpractice.*;

/**
 * Created by jonathanmitchell on 1/2/15.
 */
public class CrimeListActivity extends SingleFragmentActivity
        implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks{

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutRedId() {

        return R.layout.activity_masterdetail;
    }

    @TargetApi(11)
    public boolean showSubtitle(MenuItem item){

        if(getSupportActionBar().getSubtitle()==null){
            getSupportActionBar().setSubtitle(R.string.subtitle);
            item.setTitle(R.string.hide_subtitle);
            return true;
        }
        else{
            getSupportActionBar().setSubtitle(null);
            item.setTitle(R.string.show_subtitle);
            return false;
        }

        //getSupportActionBar().setSubtitle(R.string.subtitle);
    }

    @TargetApi(11)
    public void makeSubtitleVisible(){
        getSupportActionBar().setSubtitle(R.string.subtitle);

    }


    @Override
    public void onCrimeSelected(Crime crime) {

        /*
            If it's not a phone it wont have the detailFragmentContainer
         */
        if(findViewById(R.id.detailFragmentContainer) == null){

            //Start an instance of CrimePagerActivity!
            Intent i = new Intent(this, CrimePagerActivity.class);
            i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
            startActivity(i);

        } else {

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            Fragment oldDetail = fm.findFragmentById(R.id.detailFragmentContainer);
            Fragment newDetail = CrimeFragment.newInstance(crime.getId());

            if(oldDetail != null){
                ft.remove(oldDetail);
            }

            ft.add(R.id.detailFragmentContainer, newDetail);
            ft.commit();
        }
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        FragmentManager fm = getSupportFragmentManager();
        CrimeListFragment listFragment = (CrimeListFragment)fm.findFragmentById(R.id.fragmentContainer);

        listFragment.updateUI();

    }
}
