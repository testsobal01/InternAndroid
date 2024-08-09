package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //sound effect
    SoundPool soundPool;
    int beep,collect,draw;

    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this,"onResume",Toast.LENGTH_SHORT).show();

        TextView textView = (TextView) findViewById(R.id.text_score);
        String readText = pref.getString("score_input","保存されていません");
        textView.setText(readText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        Button btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(this);

        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(this);

        pref = getSharedPreferences("AndroidSeminor",MODE_PRIVATE);
        prefEditor = pref.edit();

        // 起動時に関数を呼び出す
        setQuestionValue();

        AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();
        soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(3).build();
        beep = soundPool.load(this,R.raw.beep,1);
        collect = soundPool.load(this,R.raw.collect,1);
        draw = soundPool.load(this,R.raw.draw,1);

        soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
            Log.d("debug","sampleId="+sampleId);
            Log.d("debug","status="+status);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();

        TextView textView = (TextView) findViewById(R.id.text_score);
        prefEditor.putString("score_input", textView.getText().toString());
        prefEditor.commit();
    }

    @Override
    public void onClick(View view) {

        Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vib.vibrate(500);

        int id = view.getId();
        if (id == R.id.button1) {
            setAnswerValue();
            checkResult(true);
        } else if (id == R.id.button2) {
            setAnswerValue();
            checkResult(false);
        } else if (id == R.id.button3) {
            setQuestionValue();
            clearAnswerValue();
            clearScoreValue();
        }
    }


    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText("値2");
    }

    private void setQuestionValue() {
        Random r = new Random();
        // 0から10の範囲で乱数を生成（+1する 必要がある）
        int questionValue = r.nextInt(10 + 1);

        TextView txtView = findViewById(R.id.question);
        txtView.setText(Integer.toString(questionValue));
    }

    private void setAnswerValue() {
        Random r = new Random();
        int answerValue = r.nextInt(10 + 1);

        TextView txtView = findViewById(R.id.answer);
        txtView.setText(Integer.toString(answerValue));
    }

    private void checkResult(boolean isHigh) {
        TextView txtViewQuestion = findViewById(R.id.question);
        TextView txtViewAnswer = findViewById(R.id.answer);

        int question = Integer.parseInt(txtViewQuestion.getText().toString());
        int answer = Integer.parseInt(txtViewAnswer.getText().toString());

        TextView txtResult = (TextView) findViewById(R.id.text_result);

        TextView background1 = findViewById(R.id.question);
        TextView background2 = findViewById(R.id.answer);

        // 結果を示す文字列を入れる変数を用意
        String result;
        int score;

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = "WIN";
                score = 2;
                background1.setBackgroundColor(Color.parseColor("#3fff00ff"));
                background2.setBackgroundColor(Color.parseColor("#afffff00"));
            } else if (question > answer) {
                result = "LOSE";
                score = -1;
                background1.setBackgroundColor(Color.parseColor("#afff00ff"));
                background2.setBackgroundColor(Color.parseColor("#3fffff00"));
            } else {
                result = "DRAW";
                score = 1;
                background1.setBackgroundColor(Color.parseColor("#3fff00ff"));
                background2.setBackgroundColor(Color.parseColor("#3fffff00"));
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                background1.setBackgroundColor(Color.parseColor("#afff00ff"));
                background2.setBackgroundColor(Color.parseColor("#3fffff00"));
            } else if (question < answer) {
                result = "LOSE";
                score = -1;
                background1.setBackgroundColor(Color.parseColor("#3fff00ff"));
                background2.setBackgroundColor(Color.parseColor("#afffff00"));
            } else {
                result = "DRAW";
                score = 1;
                background1.setBackgroundColor(Color.parseColor("#3fff00ff"));
                background2.setBackgroundColor(Color.parseColor("#3fffff00"));
            }
        }
        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText("結果：" + question + ":" + answer + "(" + result + ")");
        soundEffect(result);

        // 続けて遊べるように値を更新
        setNextQuestion();
        // スコアを表示
        setScore(score);
    }

    private void setNextQuestion() {
        TextView background1 = findViewById(R.id.question);
        TextView background2 = findViewById(R.id.answer);
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
                background1.setBackgroundColor(Color.parseColor("#3fff00ff"));
                background2.setBackgroundColor(Color.parseColor("#3fffff00"));
            }
        }.start();
    }

    private void setScore(int score) {
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        int newScore = Integer.parseInt(txtScore.getText().toString()) + score;
        txtScore.setText(Integer.toString(newScore));
    }

    private void clearScoreValue() {
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        TextView background1 = findViewById(R.id.question);
        TextView background2 = findViewById(R.id.answer);

        txtScore.setText("0");
        background1.setBackgroundColor(Color.parseColor("#3fff00ff"));
        background2.setBackgroundColor(Color.parseColor("#3fffff00"));
    }
    private void soundEffect(String result){
        if(result.equals("WIN")){
            soundPool.play(collect,1f,1f,0,0,1f);
        } else if (result.equals("LOSE")) {
            soundPool.play(beep,1f,1f,0,0,1f);
        } else if (result.equals("DRAW")) {
            soundPool.play(draw,1f,1f,0,0,1f);
        }
    }
}

