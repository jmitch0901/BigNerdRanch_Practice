package com.jonathanmitchell.bignerdranchpractice.CriminalIntent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jonathanmitchell.bignerdranchpractice.*;

import java.util.ArrayList;

/**
 * Created by jonathanmitchell on 1/2/15.
 */
public class CrimeListFragment extends ListFragment{

    private static final String TAG="CrimeListFragment";
    private static final int REQUEST_CRIME=1;


    private boolean mSubtitleVisible;

    private ArrayList<Crime> mCrimes;

    private Callbacks mCallbacks;


    /*
        Required interface for hosting activities.
     */
    public interface Callbacks{
        void onCrimeSelected(Crime crime);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        setRetainInstance(true);
        mSubtitleVisible=false;

        getActivity().setTitle(R.string.crimes_title);

        mCrimes = CrimeLab.get(getActivity()).getCrimes();
        CrimeAdapter adapter = new CrimeAdapter(mCrimes);
        setListAdapter(adapter);

    }

    @TargetApi(11)
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            v = inflater.inflate(R.layout.fragment_crime_list,container,false);
        } else {
            v = super.onCreateView(inflater, container, savedInstanceState);
        }


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            if(mSubtitleVisible)
                ((CrimeListActivity)getActivity()).makeSubtitleVisible();
        }

        ListView lv = (ListView)v.findViewById(android.R.id.list);

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
            registerForContextMenu(lv);
        }else{
            lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            lv.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {}

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater menuInflater = mode.getMenuInflater();
                    menuInflater.inflate(R.menu.crime_list_item_context,menu);

                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

                    switch (item.getItemId()){
                        case R.id.menu_item_delete_crime:
                            CrimeAdapter adapter = (CrimeAdapter)getListAdapter();
                            CrimeLab crimeLab = CrimeLab.get(getActivity());
                            for(int i = adapter.getCount() - 1; i >= 0; i--){
                                if(getListView().isItemChecked(i)){
                                    crimeLab.deleteCrime(adapter.getItem(i));
                                }
                            }
                            mode.finish();
                            adapter.notifyDataSetChanged();
                            crimeLab.saveCrimes();
                            return true;
                        default:
                            return false;
                    }

                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {}
            });
        }

        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        Crime c = ((CrimeAdapter)getListAdapter()).getItem(position);
        mCallbacks.onCrimeSelected(c);
        //Either start a new activity or just inflate a fragment view for the crime!


        //Crime c = (Crime)(getListAdapter().getItem(position));


    /*    Crime c = ((CrimeAdapter)getListAdapter()).getItem(position);

       // Intent i = new Intent(getActivity(), CrimeActivity.class);

        Intent i = new Intent(getActivity(),CrimePagerActivity.class);
        i.putExtra(CrimeFragment.EXTRA_CRIME_ID,c.getId());
        startActivityForResult(i, REQUEST_CRIME);*/
    }

    private class CrimeAdapter extends ArrayAdapter<Crime>{

        public CrimeAdapter(ArrayList<Crime> crimes){
            super(getActivity(), 0, crimes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView==null){
                convertView=getActivity().getLayoutInflater().inflate(R.layout.list_item_crime,null);
            }

            Crime c = getItem(position);

            TextView titleText = (TextView)convertView.findViewById(R.id.crime_list_item_titleTextView);
            titleText.setText(c.getTitle());

            TextView dateText = (TextView)convertView.findViewById(R.id.crime_list_item_dateViewText);
            dateText.setText(c.getDate().toString());

            CheckBox solvedCheckBox = (CheckBox)convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
            solvedCheckBox.setChecked(c.isSolved());

            return convertView;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((CrimeAdapter)this.getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==REQUEST_CRIME){

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list,menu);

        MenuItem showSubtitle = menu.findItem(R.id.menu_item_show_subtitle);
        if(mSubtitleVisible && showSubtitle!= null){
            showSubtitle.setTitle(R.string.hide_subtitle);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                //Intent i = new Intent(getActivity(),CrimePagerActivity.class);
                //i.putExtra(CrimeFragment.EXTRA_CRIME_ID,crime.getId());
                //startActivityForResult(i,0);
                ((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
                mCallbacks.onCrimeSelected(crime);
                return true;
            case android.R.id.home:
                if(NavUtils.getParentActivityName(getActivity())!=null){
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            case R.id.menu_item_show_subtitle:
                //Might need to change the typecast so fragment is reusable
                mSubtitleVisible = ((CrimeListActivity) getActivity()).showSubtitle(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        int position = info.position;
        CrimeAdapter adapter = (CrimeAdapter)getListAdapter();
        Crime crime = adapter.getItem(position);

        switch (item.getItemId()){
            case R.id.menu_item_delete_crime:
                CrimeLab.get(getActivity()).deleteCrime(crime);
                adapter.notifyDataSetChanged();
                CrimeLab.get(getActivity()).saveCrimes();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    public void updateUI(){
        ((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
    }
}
