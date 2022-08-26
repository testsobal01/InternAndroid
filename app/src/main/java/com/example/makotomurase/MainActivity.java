package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;


import android.animation.ValueAnimator;

import android.graphics.Color;


import android.content.Intent;

import android.content.SharedPreferences;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

    // 結果を示す文字列を入れる変数を用意
    String result;


    SoundPool soundPool;
    int sound_id = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }else {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    // USAGE_MEDIA
                    // USAGE_GAME
                    .setUsage(AudioAttributes.USAGE_GAME)
                    // CONTENT_TYPE_MUSIC
                    // CONTENT_TYPE_SPEECH, etc.
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();

            // ストリーム数に応じて
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    // ストリーム数に応じて
                    .setMaxStreams(1)
                    .build();
        }

        sound_id = soundPool.load(this, R.raw.aaa, 1);



        Button btn1 = (Button) findViewById(R.id.button1);
        btn1.setOnClickListener(this);

        Button btn2 = (Button) findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(this);

        pref = getSharedPreferences("MakotoMurase",MODE_PRIVATE);
        prefEditor = pref.edit();

        // 起動時に関数を呼び出す
        setQuestionValue();

    }

    @Override
    public void onClick(View view) {



        soundPool.play(sound_id,  1.0F, 1.0F, 0, 0, 1.0F);

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

        if(view.getId() == R.id.button1  || view.getId() == R.id.button2){
            if(result.equals("WIN")){
                Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(500);
            }
        }

    }

    @Override
    protected void onPause(){
        super.onPause();
        Toast.makeText(this, "Pause", Toast.LENGTH_SHORT).show();
        TextView textScore = (TextView) findViewById(R.id.text_score);
        prefEditor.putString("main_input",textScore.getText().toString());
        prefEditor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this,"Resume",Toast.LENGTH_SHORT).show();
        TextView textScore = (TextView) findViewById(R.id.text_score);
        String readText = pref.getString("main_input","");
        textScore.setText(readText);
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
        int score = 0;

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = getString(R.string.label_win);
                score = 2;
            } else if (question > answer) {
                result = getString(R.string.label_lose);
                score = -1;
            } else {
                result = getString(R.string.label_draw);;
                score = 1;
            }
        } else {
            if (question > answer) {
                result = getString(R.string.label_win);
                score = 2;
            } else if (question < answer) {
                result = getString(R.string.label_lose);
                score = -1;
            } else {
                result = getString(R.string.label_draw);
                score = 1;
            }
        }

        if(score==2){
            txtViewQuestion.setBackgroundColor(Color.BLUE);
            txtViewAnswer.setBackgroundColor(0xFFFF0000);
            txtResult.setBackgroundColor(0xFFFF66FF);
        }else if(score==1){
            txtViewQuestion.setBackgroundColor(Color.CYAN);
            txtViewAnswer.setBackgroundColor(Color.CYAN);
            txtResult.setBackgroundColor(0xFF999933);
        }else if(score==-1){
            txtViewQuestion.setBackgroundColor(Color.RED);
            txtViewAnswer.setBackgroundColor(Color.BLUE);
            txtResult.setBackgroundColor(0xFF99CCFF);
        }


        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText(getString(R.string.label_result)+"：" + question + ":" + answer + "(" + result + ")");

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

}