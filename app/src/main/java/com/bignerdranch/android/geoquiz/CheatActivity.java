package com.bignerdranch.android.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class CheatActivity extends AppCompatActivity {
    private static final String EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown";
    private static final String EXTRA_CHEAT_NUMS="com.bignerdranch.android.geoquiz.cheat_nums";
    private static int Cheat_Nums=3;
    private boolean mAnswerIsTrue;
    private TextView mAnswerTextView;
    private Button mShowAnswerButton;
    private static final String FIRST_BUG="First Bug";

    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    private void setAnswerShowResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        data.putExtra(EXTRA_CHEAT_NUMS,Cheat_Nums);//作弊次数传回去
        setResult(RESULT_OK, data);//将CheatActivity结果传回到QuizActivity
    }

    public static boolean wasAnswerShown(Intent result)//父类中调用
    {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN,false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        Cheat_Nums=getIntent().getIntExtra(EXTRA_CHEAT_NUMS,3);//获取quizactivity传过来的数据
        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);

        mShowAnswerButton=(Button) findViewById(R.id.show_answer_button);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (mAnswerIsTrue) {
                    mAnswerTextView.setText(R.string.true_button);
                } else {
                    mAnswerTextView.setText(R.string.false_button);
                }
                Cheat_Nums--;
                setAnswerShowResult(true);
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){//Build.VERSION.SDK_INT常量代表了Android设备的版本号。
                    int cx = mShowAnswerButton.getWidth()/2;
                    int cy = mShowAnswerButton.getHeight()/2;
                    float radius = mShowAnswerButton.getWidth();
                    Animator anim = ViewAnimationUtils.createCircularReveal(mShowAnswerButton,cx,cy,radius,0);
                    //1>首先指定现实或隐藏的View 2>动画的中心位置 3>起始半径 4>结束半径
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation){
                            super.onAnimationEnd(animation);
                            mShowAnswerButton.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                } else{
                    mShowAnswerButton.setVisibility(View.INVISIBLE);
                }//if语句设置了一个动画效果
            }
        });
        if (savedInstanceState!=null){
            mAnswerIsTrue=savedInstanceState.getBoolean(FIRST_BUG,false);
            if (mAnswerIsTrue){
                setAnswerShowResult(mAnswerIsTrue);
            }
        }
    }

    protected void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(FIRST_BUG,mAnswerIsTrue);
    }

}