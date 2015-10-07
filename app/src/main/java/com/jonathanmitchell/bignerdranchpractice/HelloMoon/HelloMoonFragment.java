package com.jonathanmitchell.bignerdranchpractice.HelloMoon;

import com.jonathanmitchell.bignerdranchpractice.*;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by jonathanmitchell on 1/4/15.
 */
public class HelloMoonFragment extends Fragment {

    private AudioPlayer mPlayer = new AudioPlayer();
    private Button mPlayButton;
    private Button mStopButton;

    private static final String KEY_BUTTON_NAME = "button name";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_hello_moon,container,false);



        mPlayButton=(Button)v.findViewById(R.id.hellomoon_playButton);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.play(getActivity());
                changeButtonText(mPlayer.getPlayState());
            }
        });

        mStopButton=(Button)v.findViewById(R.id.hellomoon_stopButton);
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.stop();
                changeButtonText(mPlayer.getPlayState());
            }
        });

        if(savedInstanceState!=null){
            changeButtonText(savedInstanceState.getBoolean(KEY_BUTTON_NAME));
        }

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {


        outState.putBoolean(KEY_BUTTON_NAME,mPlayer.getPlayState());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayer.stop();
    }

    public void changeButtonText(boolean isPlaying){
        if(isPlaying){
            mPlayButton.setText(R.string.hellomoon_pause);
        }
        else
            mPlayButton.setText(R.string.hellomoon_play);
    }

    private class AudioPlayer {

        private MediaPlayer mPlayer;
        private boolean mPlaying = false;

        public void stop(){
            if(mPlayer!=null){
                mPlayer.release();
                mPlayer=null;
                mPlaying=false;
            }
        }

        public void play(Context c){

            if(mPlayer!=null && mPlaying){
                mPlayer.pause();
                mPlaying=false;
                return;
            }
            else if (mPlayer!=null && !mPlaying){
                mPlayer.start();
                mPlaying=true;
                return;
            }

            stop();


            mPlayer = MediaPlayer.create(c, R.raw.one_small_step);
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stop();
                    mPlaying=false;
                    changeButtonText(mPlaying);
                }
            });

            mPlaying=true;
            mPlayer.start();
        }

        public boolean getPlayState(){
            return mPlaying;
        }



    }



}
