package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.ContextThemeWrapper;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;
    AnimatorSet set_win;

    //音の準備
    private SoundPool soundPool;
    private int soundWin, soundLose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(this);
        btn1.setBackgroundColor(getResources().getColor(R.color.green));

        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(this);
        btn2.setBackgroundColor(getResources().getColor(R.color.green));

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(this);
        btn3.setBackgroundColor(getResources().getColor(R.color.green));

        //プリファレンスの生成
        pref = getSharedPreferences("HIGH OR LOW",MODE_PRIVATE);
        prefEditor = pref.edit();
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        int readscore = pref.getInt("HIGH OR LOW",0);
        txtScore.setText(Integer.toString(readscore));

        // 起動時に関数を呼び出す
        setQuestionValue();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //スコアの保存
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        prefEditor.putInt("HIGH OR LOW", Integer.parseInt(txtScore.getText().toString()));
        prefEditor.commit();
    }

    @Override
    public void onClick(View view) {
        if(set_win != null)set_win.cancel();
        int id = view.getId();
        if (id == R.id.button1) {
            setAnswerValue();
            checkResult(true);
        } else if (id == R.id.button2) {
            setAnswerValue();
            checkResult(false);
        } else if (id == R.id.button3) {
            vib(150);//バイブレーションの追加
            setQuestionValue();
            clearAnswerValue();
            clearScoreValue();
        }
    }

    //アンサーをリセット
    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText("?");
    }

    //クエスチョンの生成
    private void setQuestionValue() {
        Random r = new Random();
        // 0から10の範囲で乱数を生成（+1する必要がある）
        int questionValue = r.nextInt(10 + 1);

        TextView txtView = findViewById(R.id.question);
        txtView.setText(Integer.toString(questionValue));
    }

    //アンサーの生成
    private void setAnswerValue() {
        Random r = new Random();
        int answerValue = r.nextInt(10 + 1);

        TextView txtView = findViewById(R.id.answer);
        txtView.setText(Integer.toString(answerValue));
    }

    //結果の生成
    private void checkResult(boolean isHigh) {
        TextView txtViewQuestion = findViewById(R.id.question);
        TextView txtViewAnswer = findViewById(R.id.answer);


        int question = Integer.parseInt(txtViewQuestion.getText().toString());
        int answer = Integer.parseInt(txtViewAnswer.getText().toString());

        TextView txtResult = (TextView) findViewById(R.id.text_result);

        //音の準備
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                // USAGE_MEDIA
                // USAGE_GAME
                .setUsage(AudioAttributes.USAGE_GAME)
                // CONTENT_TYPE_MUSIC
                // CONTENT_TYPE_SPEECH, etc.
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(2).build();
        soundWin = soundPool.load(this,R.raw.win,1);
        soundLose = soundPool.load(this,R.raw.lose,1);
        soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
            Log.d("debug","sampleId="+sampleId);
            Log.d("debug","status="+status);
        });

        // 結果を示す文字列を入れる変数を用意
        String result;
        int score;

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = "WIN";
                score = 2;

                vib(50);//バイブレーションを追加
                soundPool.play(soundWin, 1.0f, 1.0f, 0, 0, 1);
                setAnimation(txtViewAnswer);

                backgroundColor(score);//背景の追加

            } else if (question > answer) {
                result = "LOSE";
                soundPool.play(soundLose, 1.0f, 1.0f, 0, 0, 1);
                score = -1;
                backgroundColor(score);//背景の追加
            } else {
                result = "DRAW";
                score = 1;
                backgroundColor(score);//背景の追加
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                vib(50);//バイブレーションを追加
                soundPool.play(soundWin, 1.0f, 1.0f, 0, 0, 1);
                setAnimation(txtViewAnswer);
            } else if (question < answer) {
                result = "LOSE";
                score = -1;
                soundPool.play(soundLose, 1.0f, 1.0f, 0, 0, 1);
            } else {
                result = "DRAW";
                score = 1;
                backgroundColor(score);//背景の追加
            }
        }

        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText("" + question + ":" + answer + "(" + result + ")");

        // 続けて遊べるように値を更新
        setNextQuestion();
        // スコアを表示
        setScore(score);
        if(set_win != null){
            set_win.start();
            set_win.addListener(new AnimatorListenerAdapter() {
                private boolean canseled;
                @Override
                public void onAnimationCancel(Animator animation) {
                    super.onAnimationCancel(animation);
                    canseled = true;
                    set_win.end();
                    set_win=null;
                    txtViewAnswer.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if(!canseled)set_win.start();
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    canseled = false;
                }
            });
        }
    }

    //三秒後にクエスチョンを再生成
    private void setNextQuestion() {
        // 第１引数がカウントダウン時間、第２引数は途中経過を受け取る間隔
        // 単位はミリ秒（1秒＝1000ミリ秒）
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {
                // 途中経過を受け取った時に何かしたい場合
                // 今回は特に何もしない
            }

            @Override
            public void onFinish() {
                // 3秒経過したら次の値をセット
                setQuestionValue();
            }
        }.start();
    }

    //スコアの表示
    private void setScore(int score) {
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        int newScore = Integer.parseInt(txtScore.getText().toString()) + score;
        txtScore.setText(Integer.toString(newScore));
    }

    //スコアのリセット
    private void clearScoreValue() {
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        txtScore.setText("0");
    }


    private void vib(int t){
        Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        vib.vibrate(t);
        //checkResultのWIN時とbutton3クリック時にバイブレーション
    }//追加したバイブレーション機能

    private void setAnimation(TextView win){
        set_win = (AnimatorSet) AnimatorInflater.loadAnimator(this,R.animator.win_animation);
        set_win.setTarget(win);
    }


    private void backgroundColor(int i) {
        TextView txtViewQuestion = findViewById(R.id.question);
        TextView txtViewAnswer = findViewById(R.id.answer);
        if (i == 2) {
            txtViewQuestion.setBackground(new ColorDrawable(getResources().getColor(R.color.Win_Left_color)));//背景色変更
            txtViewAnswer.setBackground(new ColorDrawable(getResources().getColor(R.color.nomal_Right_color)));//背景色変更
        }else if(i ==1){
            txtViewAnswer.setBackground(new ColorDrawable(getResources().getColor(R.color.nomal_Right_color)));//背景色変更
            txtViewQuestion.setBackground(new ColorDrawable(getResources().getColor(R.color.nomal_Left_color)));//背景色変更
        }else if(i == -1){
            txtViewAnswer.setBackground(new ColorDrawable(getResources().getColor(R.color.Lose_Right_color)));//背景色変更
            txtViewQuestion.setBackground(new ColorDrawable(getResources().getColor(R.color.nomal_Left_color)));//背景色変更


        }


    }//背景変更機能の追加
}
