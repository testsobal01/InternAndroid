package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;


import android.animation.ValueAnimator;


import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import android.content.SharedPreferences;
import android.graphics.Color;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    SoundPool soundPool;
    int mp3a;
    int mp3b;
    int mp3c;

    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1 = (Button) findViewById(R.id.button1);
        btn1.setOnClickListener(this);

        Button btn2 = (Button) findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(this);

        pref = getSharedPreferences("AndroidSeminor",MODE_PRIVATE);
        prefEditor = pref.edit();

        // 起動時に関数を呼び出す
        setQuestionValue();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        } else {
            AudioAttributes attr = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(attr)
                    .setMaxStreams(5)
                    .build();
        }

        // 読込処理(CDを入れる)
        mp3a = soundPool.load(this, R.raw.ok, 1);
        mp3b = soundPool.load(this, R.raw.error, 1);
        mp3c = soundPool.load(this, R.raw.draw, 1);
    }

    public void onA(){
        // 再生処理(再生ボタン)
        soundPool.play(mp3a,1f , 1f, 0, 0, 1f);
    }

    public void onB(){
        // 再生処理 (再生ボタン)
        soundPool.play(mp3b,1f , 1f, 0, 0, 1f);
    }

    public void onC(){
        // 再生処理 (再生ボタン)
        soundPool.play(mp3c,1f , 1f, 0, 0, 1f);
    }

    @Override
    protected void onPause(){
        super.onPause();

        TextView txtScore = (TextView) findViewById(R.id.text_score);
        prefEditor.putString("main_input",txtScore.getText().toString());
        prefEditor.commit();
    }

    @Override
    protected  void onResume(){
        super.onResume();
        TextView txtScore = (TextView) findViewById(R.id.text_score);

        String readText=pref.getString("main_input","0");
        txtScore.setText(readText);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.button1:
                setAnswerValue();
                checkResult(true);
                break;
            case R.id.button2:
                setAnswerValue();
                checkResult(false);
                break;
            case R.id.button3:
                setQuestionValue();
                clearAnswerValue();
                break;

        }

    }

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText("値2");
    }

    private void setQuestionValue() {
        Random r = new Random();
        // 0から10の範囲で乱数を生成（+1する必要がある）
        int questionValue = r.nextInt(10 + 1);
        TextView txtView = (TextView) findViewById(R.id.question);
        txtView.setText(Integer.toString(questionValue));
    }

    private void setAnswerValue() {
        Random r = new Random();
        int answerValue = r.nextInt(10 + 1);
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText(Integer.toString(answerValue));
    }

    private void checkResult(boolean isHigh) {
        TextView txtViewQuestion = (TextView) findViewById(R.id.question);
        TextView txtViewAnswer = (TextView) findViewById(R.id.answer);
        int question = Integer.parseInt(txtViewQuestion.getText().toString());
        int answer = Integer.parseInt(txtViewAnswer.getText().toString());
        TextView txtResult = (TextView) findViewById(R.id.text_result);
        // 結果を示す文字列を入れる変数を用意
        String result;
        int score = 0;

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = "WIN";
                onA();
                score = 2;
                txtViewQuestion.setBackgroundColor(Color.WHITE);
                txtViewAnswer.setBackgroundColor(Color.RED);
                setAnime();
            } else if (question > answer) {
                result = "LOSE";
                onB();
                score = -1;
                txtViewQuestion.setBackgroundColor(Color.RED);
                txtViewAnswer.setBackgroundColor(Color.WHITE);
                setAnimeAnswer();
            } else {
                result = "DRAW";
                onC();
                score = 1;
                txtViewQuestion.setBackgroundColor(Color.BLUE);
                txtViewAnswer.setBackgroundColor(Color.BLUE);
            }
        } else {
            if (question > answer) {
                result = "WIN";
                onA();
                score = 2;
                txtViewQuestion.setBackgroundColor(Color.RED);
                txtViewAnswer.setBackgroundColor(Color.WHITE);
                setAnime();
            } else if (question < answer) {
                result = "LOSE";
                onB();
                score = -1;
                txtViewQuestion.setBackgroundColor(Color.WHITE);
                txtViewAnswer.setBackgroundColor(Color.RED);
                setAnimeAnswer();
            } else {
                result = "DRAW";
                onC();
                score = 1;
                txtViewQuestion.setBackgroundColor(Color.BLUE);
                txtViewAnswer.setBackgroundColor(Color.BLUE);
            }
        }


        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText("結果：" + question + ":" + answer + "(" + result + ")");

        // 続けて遊べるように値を更新
        setNextQuestion();

        // スコアを表示
        setScore(score);

    }

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

    private void setScore(int score) {
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        int newScore = Integer.parseInt(txtScore.getText().toString()) + score;
        txtScore.setText(Integer.toString(newScore));
    }

    private void setAnime(){
        TextView txtViewQuestion = (TextView) findViewById(R.id.question);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        // animation時間 msec
        scaleAnimation.setDuration(2000);

        RotateAnimation rotate = new RotateAnimation(0.0f, 120.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.5f);
        // animation時間 msec
        rotate.setDuration(2000);

        AnimationSet animationSet = new AnimationSet( true );

        // animationSetにそれぞれ追加する
        animationSet.addAnimation( scaleAnimation );
        animationSet.addAnimation( rotate );

        txtViewQuestion.startAnimation(animationSet);
    }

    private void setAnimeAnswer(){
        TextView txtViewAnswer = (TextView) findViewById(R.id.answer);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        // animation時間 msec
        scaleAnimation.setDuration(2000);

        RotateAnimation rotate = new RotateAnimation(0.0f, 120.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.5f);
        // animation時間 msec
        rotate.setDuration(2000);

        AnimationSet animationSet = new AnimationSet( true );

        // animationSetにそれぞれ追加する
        animationSet.addAnimation( scaleAnimation );
        animationSet.addAnimation( rotate );

        txtViewAnswer.startAnimation(animationSet);
    }

}