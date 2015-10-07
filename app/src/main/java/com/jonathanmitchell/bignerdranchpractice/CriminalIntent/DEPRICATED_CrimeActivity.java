package com.jonathanmitchell.bignerdranchpractice.CriminalIntent;
import com.jonathanmitchell.bignerdranchpractice.*;

import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;


import java.util.UUID;

/*
    UNUSED!!!
 */

public class DEPRICATED_CrimeActivity extends SingleFragmentActivity{

    @Override
    protected Fragment createFragment() {
        //return new CrimeFragment();
        UUID crimeId = (UUID)getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        return CrimeFragment.newInstance(crimeId);
    }




}
