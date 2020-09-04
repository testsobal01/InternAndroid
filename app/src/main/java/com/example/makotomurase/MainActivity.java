package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioAttributes;
import android.media.SoundPool;
import android.content.SharedPreferences;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SoundPool soundPool;
    private int soundOne, soundTwo;

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

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build();

        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(2)
                .build();

        soundOne = soundPool.load(this, R.raw.cursor7, 1);
        soundTwo = soundPool.load(this, R.raw.decision1, 1);

        pref=getSharedPreferences("AndoroidSeminor",MODE_PRIVATE);
        prefEditor=pref.edit();

        // 起動時に関数を呼び出す
        setQuestionValue();

    }
    protected void onResume(){
        super.onResume();

        TextView Score=(TextView)findViewById(R.id.text_score);
        String readText=pref.getString("score","0");
        Score.setText(readText);
    }

    protected void onPause(){
        super.onPause();

        TextView Score=(TextView)findViewById(R.id.text_score);
        prefEditor.putString("score",Score.getText().toString());
        prefEditor.commit();
    }

    @Override
    public void onClick(View view) {




        int id = view.getId();
        switch (id) {
            case R.id.button1:
                setAnswerValue();
                checkResult(true);
                soundPool.play(soundTwo, 1.0f, 1.0f, 0, 0, 1);
                break;
            case R.id.button2:
                setAnswerValue();
                checkResult(false);
                soundPool.play(soundTwo, 1.0f, 1.0f, 0, 0, 1);
                break;
            case R.id.button3:
                soundPool.play(soundOne, 1.0f, 1.0f, 0, 0, 1);
                setQuestionValue();
                clearAnswerValue();
                break;

        }

    }

    private void clearAnswerValue() {
        LinearLayout linearLayout=(LinearLayout) findViewById(R.id.main_field);
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText("値2");
        linearLayout.setBackgroundColor(Color.WHITE);
        setColorBlack();
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

        LinearLayout linearLayout=(LinearLayout) findViewById(R.id.main_field);


        // 結果を示す文字列を入れる変数を用意
        String result;
        int score = 0;

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = "WIN";
                score = 2;
                linearLayout.setBackgroundColor(Color.RED);
                setColorWhite();
            } else if (question > answer) {
                result = "LOSE";
                score = -1;
                Vibrator vib =(Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(500);
                linearLayout.setBackgroundColor(Color.BLUE);
                setColorWhite();
            } else {
                result = "DRAW";
                score = 1;
                linearLayout.setBackgroundColor(Color.LTGRAY);
                setColorWhite();
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                linearLayout.setBackgroundColor(Color.RED);
                setColorWhite();

            } else if (question < answer) {
                result = "LOSE";
                score = -1;
                Vibrator vib =(Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(500);
                linearLayout.setBackgroundColor(Color.BLUE);
                setColorWhite();
            } else {
                result = "DRAW";
                score = 1;
                linearLayout.setBackgroundColor(Color.LTGRAY);
                setColorWhite();
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
                LinearLayout linearLayout=(LinearLayout) findViewById(R.id.main_field);
                linearLayout.setBackgroundColor(Color.WHITE);
                setColorBlack();
            }
        }.start();
    }

    private void setScore(int score) {
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        int newScore = Integer.parseInt(txtScore.getText().toString()) + score;
        txtScore.setText(Integer.toString(newScore));
    }

    public void setColorWhite(){
        TextView text1=(TextView)findViewById(R.id.text1);
        TextView text_result=(TextView)findViewById(R.id.text_result);
        TextView text_score_01=(TextView)findViewById(R.id.text_score_01);
        TextView text_score=(TextView)findViewById(R.id.text_score);

        text1.setTextColor(Color.WHITE);
        text_result.setTextColor(Color.WHITE);
        text_score_01.setTextColor(Color.WHITE);
        text_score.setTextColor(Color.WHITE);
    }

    public void setColorBlack(){
        TextView text1=(TextView)findViewById(R.id.text1);
        TextView text_result=(TextView)findViewById(R.id.text_result);
        TextView text_score_01=(TextView)findViewById(R.id.text_score_01);
        TextView text_score=(TextView)findViewById(R.id.text_score);

        text1.setTextColor(Color.BLACK);
        text_result.setTextColor(Color.BLACK);
        text_score_01.setTextColor(Color.BLACK);
        text_score.setTextColor(Color.BLACK);
    }

}