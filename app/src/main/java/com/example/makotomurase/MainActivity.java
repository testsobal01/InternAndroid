package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;


import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
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

import org.w3c.dom.Text;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    SoundPool soundPool;
    int mp3lose;
    int mp3win;

    int mp3restart;

    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(this);

        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(this);
      
        Intent intent = getIntent();
        Bundle extra = intent.getExtras();

        pref = getSharedPreferences("AndroidSeminar", MODE_PRIVATE);
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

        mp3win = soundPool.load(this, R.raw.win, 1);
        mp3lose = soundPool.load(this, R.raw.lose, 1);
        mp3restart = soundPool.load(this, R.raw.restart, 1);
    }

    protected void onResume(){
        super.onResume();
        Toast.makeText(this,"onResume",Toast.LENGTH_SHORT).show();

        TextView textView = (TextView)findViewById(R.id.text_score);
        String readText = pref.getString("main_input","保存されていません");
        textView.setText(readText);
    }
    @Override
    public void onClick(View view) {
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

            Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vib.vibrate(400);

        }
    }

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        String st_value2 =getString(R.string.value2);
        txtView.setText(st_value2);
        int colorId = getResources().getColor(R.color.yellow);
        txtView.setBackgroundColor(colorId);
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

        // 表示テキストを用意
        String st_win =getString(R.string.win);
        String st_lose =getString(R.string.lose);
        String st_draw =getString(R.string.draw);
        String st_result =getString(R.string.result);

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = st_win;
                int colorId = getResources().getColor(R.color.red);
                txtViewAnswer.setBackgroundColor(colorId);
                score = 2;
                soundPool.play(mp3win,1f , 1f, 0, 0, 1f);

            } else if (question > answer) {
                result = st_lose;
                int colorId = getResources().getColor(R.color.blue);
                txtViewAnswer.setBackgroundColor(colorId);
                score = -1;
                soundPool.play(mp3lose,1f , 1f, 0, 0, 1f);

            } else {
                result = st_draw;
                int colorId = getResources().getColor(R.color.white);
                txtViewAnswer.setBackgroundColor(colorId);
                score = 1;
                soundPool.play(mp3restart,1f , 1f, 0, 0, 1f);
            }
        } else {
            if (question > answer) {
                result = st_win;
                int colorId = getResources().getColor(R.color.red);
                txtViewAnswer.setBackgroundColor(colorId);
                score = 2;
                soundPool.play(mp3win,1f , 1f, 0, 0, 1f);
            } else if (question < answer) {
                result = st_lose;
                int colorId = getResources().getColor(R.color.blue);
                txtViewAnswer.setBackgroundColor(colorId);
                score = -1;
                soundPool.play(mp3lose,1f , 1f, 0, 0, 1f);               
            } else {
                result = st_draw;
                int colorId = getResources().getColor(R.color.white);
                txtViewAnswer.setBackgroundColor(colorId);
                score = 1;
                soundPool.play(mp3restart,1f , 1f, 0, 0, 1f);

            }
        }

        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText(st_result + question + ":" + answer + "(" + result + ")");

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

    protected void onPause(){
        super.onPause();
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();

        TextView textView = (TextView)findViewById(R.id.text_score);
        prefEditor.putString("main_input", textView.getText().toString());
        prefEditor.commit();
    }
}

