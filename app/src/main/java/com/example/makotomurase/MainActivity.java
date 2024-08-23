package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Vibrator;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    int mp3;
    int mp32;
    int mp33;
    SoundPool soundPool;

    String result_text;
    String win_text;
    String lose_text;
    String draw_text;

    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent=getIntent();
        Bundle extra =intent.getExtras();
        String intentStiring= extra.getString("KEY");



        Button btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(this);

        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(this);

        AudioAttributes attr = new AudioAttributes.Builder().
                setUsage(AudioAttributes.USAGE_MEDIA).
                setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();

        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attr)
                .setMaxStreams(5)
                .build();

        mp3 = soundPool.load(this, R.raw.takai, 1);
        mp32 = soundPool.load(this,R.raw.sound_reset,1);
        mp33=soundPool.load(this,R.raw.hikui,1);

        result_text = getString(R.string.label_result);
        win_text= getString(R.string.label_win);
        lose_text=getString(R.string.label_lose);
        draw_text=getString(R.string.label_draw);


        // 起動時に関数を呼び出す
        setQuestionValue();

        pref = getSharedPreferences("AndroidSeminor",MODE_PRIVATE);
        prefEditor = pref.edit();
    }

    @Override
    protected void onPause(){
        super.onPause();
        Toast.makeText(this,"onPause",Toast.LENGTH_SHORT).show();

        TextView textView = (TextView)findViewById(R.id.text_score);
        prefEditor.putString("main_input",textView.getText().toString());
        prefEditor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();

        TextView textView = (TextView) findViewById(R.id.text_score);
        String readText = pref.getString("main_input", "0");
        textView.setText(readText);
    }

    @Override
    public void onClick(View view) {


        int id = view.getId();
        if (id == R.id.button1) {
            setAnswerValue();
            checkResult(true);
            soundPool.play(mp3, 1f, 1f, 0, 0, 1f);
        } else if (id == R.id.button2) {
            setAnswerValue();
            checkResult(false);
            soundPool.play(mp33, 1f, 1f, 0, 0, 1f);
        } else if (id == R.id.button3) {
            setQuestionValue();
            clearAnswerValue();
            clearScoreValue();
            Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vib.vibrate(300);
            soundPool.play(mp32, 1f, 1f, 0, 0, 1f);
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

        // 結果を示す文字列を入れる変数を用意
        String result;
        int score;

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = win_text;
                score = 2;
                changeBackgroundWin();
            } else if (question > answer) {
                result = lose_text;
                score = -1;
                changeBackGroundLose();
            } else {
                result = draw_text;
                score = 1;
                changeBackGroundDraw();
            }
        } else {
            if (question > answer) {
                result = win_text;
                score = 2;
                changeBackgroundWin();
            } else if (question < answer) {
                result = lose_text;
                score = -1;
                changeBackGroundLose();
            } else {
                result = draw_text;
                score = 1;
                changeBackGroundDraw();
            }
        }

        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText(result_text + question + ":" + answer + "(" + result + ")");

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

    private void clearScoreValue() {
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        txtScore.setText("0");
    }

    private void changeBackgroundWin(){
        TextView qtxtScore = (TextView) findViewById(R.id.question);
        qtxtScore.setBackgroundColor(Color.RED);
        TextView atxtScore = (TextView) findViewById(R.id.answer);
        atxtScore.setBackgroundColor(Color.BLUE);
    }

    private void changeBackGroundLose(){
        TextView qtxtScore = (TextView) findViewById(R.id.question);
        qtxtScore.setBackgroundColor(Color.BLUE);
        TextView atxtScore = (TextView) findViewById(R.id.answer);
        atxtScore.setBackgroundColor(Color.RED);
    }

    private void changeBackGroundDraw(){
        TextView qtxtScore = (TextView) findViewById(R.id.question);
        qtxtScore.setBackgroundColor(Color.WHITE);
        TextView atxtScore = (TextView) findViewById(R.id.answer);
        atxtScore.setBackgroundColor(Color.WHITE);
    }
}

