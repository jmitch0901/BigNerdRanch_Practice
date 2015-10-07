package com.jonathanmitchell.bignerdranchpractice.GeoQuiz;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jonathanmitchell.bignerdranchpractice.R;

public class CheatActivity extends ActionBarActivity {

    public static final String EXTRA_ANSWER_IS_TRUE = "com.jonathanmitchell.bignerdranchpractice.GeoQuiz.answer_is_true";
    public static final String EXTRA_ANSWER_SHOWN = "com.jonathanmitchell.bignerdranchpractice.GeoQuiz.answer_shown";

    private static final String KEY_CHEATED="cheated";
    private static final String KEY_ANSWER="answer";
    public static final String KEY_CHEATED_ALREADY="cheated already";


    private boolean mAnswerIsTrue;

    private TextView mAnswerTextView;
    private Button mShowAnswer;

    private boolean mICheated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mAnswerTextView=(TextView)findViewById(R.id.tv_cheat);

        if(savedInstanceState!=null){
            mICheated=savedInstanceState.getBoolean(KEY_CHEATED);
            mAnswerTextView.setText(savedInstanceState.getString(KEY_ANSWER));
        }

        setAnswerShownResult(mICheated);

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE,false);



        mShowAnswer=(Button)findViewById(R.id.showAnswerButton);

        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(mAnswerIsTrue){
                    mAnswerTextView.setText(R.string.true_string);
                }
                else
                    mAnswerTextView.setText(R.string.false_string);



                setAnswerShownResult(true);
            }
        });
    }

    private void setAnswerShownResult(boolean answerIsShown){
        mICheated=answerIsShown;
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN,answerIsShown);
        setResult(RESULT_OK,data);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(KEY_CHEATED, mICheated);
        outState.putString(KEY_ANSWER, mAnswerTextView.getText().toString());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cheat, menu);
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
