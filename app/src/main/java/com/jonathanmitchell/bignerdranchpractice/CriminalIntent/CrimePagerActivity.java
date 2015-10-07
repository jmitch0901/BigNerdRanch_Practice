package com.jonathanmitchell.bignerdranchpractice.CriminalIntent;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import com.jonathanmitchell.bignerdranchpractice.*;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by jonathanmitchell on 1/2/15.
 */
public class CrimePagerActivity extends ActionBarActivity
    implements CrimeFragment.Callbacks{

    private ViewPager mViewPager;
    private ArrayList<Crime> mCrimes;

    @TargetApi(11)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            if(NavUtils.getParentActivityName(this)!=null){
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        mCrimes = CrimeLab.get(this).getCrimes();

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter( new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Crime aCrime = mCrimes.get(position);
                return CrimeFragment.newInstance(aCrime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        UUID crimeId = (UUID)getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        for(int i = 0 ; i < mCrimes.size(); i++){
            if(crimeId.equals(mCrimes.get(i).getId())){
                mViewPager.setCurrentItem(i);
                if(mCrimes.get(i).getTitle()!=null) {
                    setTitle(mCrimes.get(i).getTitle());
                }
                break;
            }
        }

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                Crime crime = mCrimes.get(position);
                if(crime.getTitle()!=null){
                    setTitle(crime.getTitle());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });



    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        //Do Nothing! This is because this activity is ONLY called on phones!
    }
}
