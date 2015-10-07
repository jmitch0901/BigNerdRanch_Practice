package com.jonathanmitchell.bignerdranchpractice.CriminalIntent.IO_JSON;

import android.content.Context;

import com.jonathanmitchell.bignerdranchpractice.CriminalIntent.Crime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by jonathanmitchell on 1/6/15.
 */
public class CriminalIntentJSONSerializer {

    private Context mContext;
    private String mFileName;

    public CriminalIntentJSONSerializer(Context c, String f){
        mContext = c;
        mFileName = f;
    }

    public ArrayList<Crime> loadCrimes() throws IOException, JSONException{

        ArrayList<Crime> crimes = new ArrayList<Crime>(0);
        BufferedReader reader = null;
        try{
            //Open and read the file into a StringBuilder
            InputStream in = mContext.openFileInput(mFileName);
            reader = new BufferedReader(new InputStreamReader(in));

            StringBuilder jsonString = new StringBuilder();
            String line = null;

            while((line=reader.readLine()) != null){
                //Line breaks are omitted and irrelevant
                jsonString.append(line);
            }

            //Parse the JSON using JSONTokener
            JSONArray jArray = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();

            //Build the array of crimes from JSONObjects
            for(int i = 0 ; i < jArray.length(); i++){
                crimes.add(new Crime(jArray.getJSONObject(i)));
            }
        }catch(FileNotFoundException e){
            //Do Nothing
        } finally {
            if(reader != null){
                reader.close();
            }
        }







        return crimes;
    }

    public void saveCrimes(ArrayList<Crime> crimes) throws JSONException, IOException{

        JSONArray jArray = new JSONArray();

        //Build JSON Array
        for(Crime c : crimes){
            jArray.put(c.toJSON());
        }

        //Write the file to disk
        Writer writer = null;
        try{
            OutputStream os = mContext.openFileOutput(mFileName,Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(os);
            writer.write(jArray.toString());
        } finally {
            if (writer != null){
                writer.flush();
                writer.close();
            }
        }
    }

}
