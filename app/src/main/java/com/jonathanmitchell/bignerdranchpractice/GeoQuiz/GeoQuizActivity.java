package com.jonathanmitchell.bignerdranchpractice.GeoQuiz;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jonathanmitchell.bignerdranchpractice.R;

import java.util.ArrayList;

public class GeoQuizActivity extends ActionBarActivity {



    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button cheatButton;
    private TextView mQuestionTextView;
    private Button mPreviousButton;

    private TrueFalse[] mQuestionBank = new TrueFalse[]{
            new TrueFalse(R.string.question_oceans,true),
            new TrueFalse(R.string.question_mideast,false),
            new TrueFalse(R.string.question_africa,false),
            new TrueFalse(R.string.question_americas,true),
            new TrueFalse(R.string.question_asia,true)
    };

    private int mCurrentIndex = 0;

    private boolean mIsCheater;

    private static final String KEY_INDEX="index";
    private static final String KEY_IS_CHEATER="cheater";

    private TextView mApiTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_quiz);

        if(savedInstanceState!=null){
            mCurrentIndex=savedInstanceState.getInt(KEY_INDEX,0);
           // mIsCheater=savedInstanceState.getBoolean(KEY_IS_CHEATER);

            for(int i = 0 ; i < mQuestionBank.length; i++){
                mQuestionBank[i].setQuestionWasCheated(savedInstanceState.getBoolean(KEY_IS_CHEATER+i));
            }
        }

        mApiTextView = (TextView)findViewById(R.id.tv_api_level);
        mApiTextView.setText("API level "+Build.VERSION.SDK_INT);


        mQuestionTextView=(TextView)findViewById(R.id.tv_question);


        mTrueButton = (Button)findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);

            }
        });

        mFalseButton = (Button)findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);

            }
        });

        mNextButton = (Button)findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (1 + mCurrentIndex) % mQuestionBank.length;
                mIsCheater=false;
                updateQuestion();
            }
        });

        mPreviousButton = (Button)findViewById(R.id.prev_button);
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCurrentIndex-=1;

                if(mCurrentIndex<0){
                    mCurrentIndex=mQuestionBank.length-1;
                }
                mCurrentIndex=(mCurrentIndex) % mQuestionBank.length;
                updateQuestion();
            }
        });



        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNextButton.performClick();
            }
        });

        cheatButton = (Button)findViewById(R.id.button_cheat);
        cheatButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(GeoQuizActivity.this,CheatActivity.class);
                i.putExtra(CheatActivity.EXTRA_ANSWER_IS_TRUE,mQuestionBank[mCurrentIndex].isTrueQuestion());
                i.putExtra(CheatActivity.KEY_CHEATED_ALREADY,mQuestionBank[mCurrentIndex].questionWasCheated());
                startActivityForResult(i, 0);
            }
        });






        updateQuestion();
    }

    private void updateQuestion(){
        int question = mQuestionBank[mCurrentIndex].getQuestion();
        mQuestionTextView.setText(question);
        mIsCheater=mQuestionBank[mCurrentIndex].questionWasCheated();
    }

    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue=mQuestionBank[mCurrentIndex].isTrueQuestion();

        int messageResId = 0;

        if(mIsCheater){
            messageResId=R.string.judgement_toast;
        }
        else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }

            Toast.makeText(this,messageResId,Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_INDEX,mCurrentIndex);

        outState.putBoolean(KEY_IS_CHEATER,mQuestionBank[mCurrentIndex].questionWasCheated());

        for(int i = 0; i < mQuestionBank.length; i++){
            outState.putBoolean(KEY_IS_CHEATER+i,mQuestionBank[i].questionWasCheated());
        }





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data==null)
            return;

        mIsCheater = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN,false);
        if(mIsCheater){
            mQuestionBank[mCurrentIndex].setQuestionWasCheated(true);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_geo_quiz, menu);
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
