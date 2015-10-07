package com.jonathanmitchell.bignerdranchpractice;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.jonathanmitchell.bignerdranchpractice.CriminalIntent.*;
import com.jonathanmitchell.bignerdranchpractice.GeoQuiz.*;
import com.jonathanmitchell.bignerdranchpractice.HelloMoon.HelloMoon;
import com.jonathanmitchell.bignerdranchpractice.PhotoGallery.PhotoGalleryActivity;
import com.jonathanmitchell.bignerdranchpractice.RemoteControl.RemoteControlActivity;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private List<String> activities;
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);







        activities = new ArrayList<String>(0);
        listView = (ListView)findViewById(R.id.main_list_view);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,activities);

        listView.setAdapter(adapter);
        addActivityNames();

        listView.setOnItemClickListener(this);
    }


    private void addActivityNames(){
        activities.add("GeoQuiz");
        activities.add("Criminal Intent");
        activities.add("Hello Moon");
        activities.add("Remote Control Activity");
        activities.add("Photo Gallery");




        adapter.notifyDataSetChanged();
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String activityName = activities.get(position);

        Intent i = null;
        switch(activityName){
            case("GeoQuiz"):
                i=new Intent(this,GeoQuizActivity.class);
                startActivity(i);
                break;
            case("Criminal Intent"):
                i=new Intent(this,CrimeListActivity.class);
                startActivity(i);
                break;
            case("Hello Moon"):
                i=new Intent(this, HelloMoon.class);
                startActivity(i);
                break;
            case("Remote Control Activity"):
                i=new Intent(this, RemoteControlActivity.class);
                startActivity(i);
                break;
            case("Photo Gallery"):
                i = new Intent(this, PhotoGalleryActivity.class);
                startActivity(i);
                break;
            default:
                Toast.makeText(this,"Cannot Start Activity "+activityName,Toast.LENGTH_LONG).show();
                break;
        }

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
