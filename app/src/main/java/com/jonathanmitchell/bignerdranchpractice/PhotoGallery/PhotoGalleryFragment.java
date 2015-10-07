package com.jonathanmitchell.bignerdranchpractice.PhotoGallery;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.jonathanmitchell.bignerdranchpractice.*;
import com.jonathanmitchell.bignerdranchpractice.PhotoGallery.Networking.FlickrFetchr;
import com.jonathanmitchell.bignerdranchpractice.PhotoGallery.Networking.GalleryItem;
import com.jonathanmitchell.bignerdranchpractice.PhotoGallery.Services.PollService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathanmitchell on 1/9/15.
 */
public class PhotoGalleryFragment extends Fragment {

    private GridView mGridView;

    private static final String TAG = "PhotoGalleryFragment";

    ArrayList<GalleryItem> mItems;
    ThumbnailDownloader<ImageView> mThumbnailThread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        setHasOptionsMenu(true);


        //new FetchItemsTask().execute();
        updateItems();

        //getActivity().startService(new Intent(getActivity(), PollService.class));
        //PollService.setServiceAlarm(getActivity(),true);
        //mThumbnailThread = new ThumbnailDownloader<ImageView>();

        mThumbnailThread = new ThumbnailDownloader<ImageView>(new Handler()); // Assigns a new Handler for the LOOPER for the THREAD it was created ON!
        mThumbnailThread.setListener(new ThumbnailDownloader.Listener<ImageView>(){

           @Override
           public void onThumbnailDownloaded(ImageView imgView, Bitmap thumbNail){
                if(isVisible()){
                    imgView.setImageBitmap(thumbNail);
                }
            }
        });

        mThumbnailThread.start();
        mThumbnailThread.getLooper();
        Log.i(TAG,"Background thread started");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_photo_gallery,container,false);

        mGridView = (GridView)v.findViewById(R.id.gridView);

        setUpAdapter();


        return v;
    }

    private class FetchItemsTask extends AsyncTask<Void,Void,ArrayList<GalleryItem>>{


        @Override
        protected ArrayList<GalleryItem> doInBackground(Void... params) {

            Activity activity = getActivity();
            if(activity==null){
                return new ArrayList<GalleryItem>();
            }

            String query = PreferenceManager.getDefaultSharedPreferences(activity)
                    .getString(FlickrFetchr.PREF_SEARCH_QUERY,null);

            //new FlickrFetchr().fetchItems();



            if(query != null){
                return new FlickrFetchr().search(query);
            } else {
                return new FlickrFetchr().fetchItems();
            }
        }

        @Override
        protected void onPostExecute(ArrayList<GalleryItem> galleryItems) {
            mItems = galleryItems;
            setUpAdapter();
        }
    }

    void setUpAdapter(){

        if(getActivity() == null || mGridView == null)
            return;

        if(mItems != null){
            mGridView.setAdapter(new GalleryItemAdapter(mItems));
        } else {
            mGridView.setAdapter(null);
        }
    }

    private class GalleryItemAdapter extends ArrayAdapter<GalleryItem>{

        public GalleryItemAdapter(ArrayList<GalleryItem> items) {
            super(getActivity(), 0, items);
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.gallery_item, parent, false);
            }

            ImageView imageView = (ImageView)convertView.findViewById(R.id.gallery_item_imageView);

            imageView.setImageResource(R.drawable.brian_up_close);

            mThumbnailThread.queueThumbnail(imageView,getItem(position).getUrl());

            return convertView;
        }

    }

    @Override
    @TargetApi(11)
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_photo_gallery, menu);



        //This doesnt work????

        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){

            //Pull out searchview
            MenuItem searchItem = menu.findItem(R.id.menu_item_search);
            SearchView searchView = (SearchView)searchItem.getActionView();

            //Get the data from our searchable.xml as a SearchableInfo
            SearchManager searchManager = (SearchManager)getActivity()
                    .getSystemService(Context.SEARCH_SERVICE);

            ComponentName name = getActivity().getComponentName();
            SearchableInfo searchableInfo = searchManager.getSearchableInfo(name);

            searchView.setSearchableInfo(searchableInfo);
        }*/
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem toggleItem = menu.findItem(R.id.menu_item_toggle_polling);
        if(PollService.isServiceAlarmOn(getActivity())){
            toggleItem.setTitle(R.string.stop_polling);
        } else {
            toggleItem.setTitle(R.string.start_polling);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_search:
                getActivity().onSearchRequested();
                return true;
            case R.id.menu_item_clear:
                PreferenceManager.getDefaultSharedPreferences(getActivity())
                        .edit()
                        .putString(FlickrFetchr.PREF_SEARCH_QUERY,null)
                        .commit();
                updateItems();
                return true;
            case R.id.menu_item_toggle_polling:
                boolean shouldStartAlarm = !PollService.isServiceAlarmOn(getActivity());
                PollService.setServiceAlarm(getActivity(),shouldStartAlarm);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                    getActivity().invalidateOptionsMenu();
                }


                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        mThumbnailThread.quit();
        Log.i(TAG,"Background thread destroyed");
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mThumbnailThread.clearQueue();
    }

    public void updateItems(){
        new FetchItemsTask().execute();
    }



}
