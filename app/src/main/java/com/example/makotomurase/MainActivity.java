package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // データ保存するやつ
    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

    SoundPool soundPool;
    int sound_a;

    SoundPool soundPool;
    int sound_a;
    int sound_b;

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

        sound_a = soundPool.load(this, R.raw.airhorn, 1);
        sound_b = soundPool.load(this, R.raw.erro, 1);

        // 起動時に関数を呼び出す
        setQuestionValue();

        // "SaveFile"は保存先
        pref = getSharedPreferences("SeveFile", MODE_PRIVATE);
        prefEditor = pref.edit();

    }

    @Override
    protected void onPause() {
        super.onPause();

        // 画面上のスコアを取得して保存
        TextView textView = (TextView) findViewById(R.id.text_score);
        prefEditor.putString("score", textView.getText().toString());
        prefEditor.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();

        // 保存データを読み込んで表示
        TextView textView = (TextView) findViewById(R.id.text_score);
        String readText = pref.getString("score", "0");
        textView.setText(readText);
    }

    @Override
    public void onClick(View view) {
        Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        int id = view.getId();
        switch (id) {
            case R.id.button1:
                setAnswerValue();
                checkResult(true);
                vib.vibrate(100);
                break;
            case R.id.button2:
                setAnswerValue();
                checkResult(false);
                vib.vibrate(100);
                break;
            case R.id.button3:
                setQuestionValue();
                clearAnswerValue();
                vib.vibrate(300);
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
        LinearLayout backcolor = (LinearLayout) findViewById(R.id.back_color);
        // 結果を示す文字列を入れる変数を用意
        String result;
        int score = 0;

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = "WIN";
                score = 2;
                soundPool.play(sound_a, 1f, 1f, 0, 0,1f);
                backcolor.setBackgroundColor(getResources().getColor(R.color.red));
            } else if (question > answer) {
                result = "LOSE";
                score = -1;
                backcolor.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                soundPool.play(sound_b, 1f, 1f, 0, 0,1f);
            } else {
                result = "DRAW";
                score = 1;
                backcolor.setBackgroundColor(getResources().getColor(R.color.green));
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                soundPool.play(sound_a, 1f, 1f, 0, 0,1f);
                backcolor.setBackgroundColor(getResources().getColor(R.color.red));
            } else if (question < answer) {
                result = "LOSE";
                score = -1;
                backcolor.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                soundPool.play(sound_b, 1f, 1f, 0, 0,1f);
            } else {
                result = "DRAW";
                score = 1;
                backcolor.setBackgroundColor(getResources().getColor(R.color.green));
            }
        }

        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText(question + ":" + answer + "(" + result + ")");

        // 勝敗に合わせてアニメーション
        TextView value1 = (TextView) findViewById(R.id.question);
        TextView value2 = (TextView) findViewById(R.id.answer);
        textAnimation(value1, value2, result);

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

    public void textAnimation(TextView v1, TextView v2, String result) {
        int textDefaultSize = 60;
        Animation crushAnimation = AnimationUtils.loadAnimation(this, R.anim.popup);

        if(result == "WIN"){
            v1.startAnimation(crushAnimation);
        }else if(result == "LOSE"){
            v2.startAnimation(crushAnimation);
        }
        v1.setTextSize(textDefaultSize);
        v2.setTextSize(textDefaultSize);
    }

}