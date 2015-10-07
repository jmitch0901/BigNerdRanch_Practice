package com.jonathanmitchell.bignerdranchpractice.GeoQuiz;

/**
 * Created by jonathanmitchell on 1/1/15.
 */
public class TrueFalse {

    private int mQuestion;
    private boolean mTrueQuestion;
    private boolean mIsCheater;

    public TrueFalse(int question, boolean trueQuestion){
        mQuestion=question;
        mTrueQuestion=trueQuestion;
        mIsCheater=false;

    }

    public boolean questionWasCheated(){
        return mIsCheater;
    }

    public void setQuestionWasCheated(boolean cheated){
        mIsCheater=cheated;
    }


    public int getQuestion() {
        return mQuestion;
    }

    public void setQuestion(int mQuestion) {
        this.mQuestion = mQuestion;
    }

    public boolean isTrueQuestion() {
        return mTrueQuestion;
    }

    public void setTrueQuestion(boolean mTrueQuestion) {
        this.mTrueQuestion = mTrueQuestion;
    }
}
