package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;


import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;

import android.graphics.Color;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;


    AnimatorSet set;

    // アニメーションフラグ　0は起動していない、1は起動中
    int flag = 0;


    SoundPool soundPool;
    int mp3a;

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

        pref = getSharedPreferences("AndroidSeminor", MODE_PRIVATE);
        prefEditor = pref.edit();

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
        // ③ 読込処理(CDを入れる)
        mp3a = soundPool.load(this, R.raw.a, 1);
        // 起動時に関数を呼び出す
        setQuestionValue();
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
        }
    }

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText("値2");
        //背景色を変更
        findViewById(R.id.question).setBackgroundColor(Color.rgb(255,0,255));
        findViewById(R.id.answer).setBackgroundColor(Color.rgb(255,255,0));
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
                result = "WIN";
                soundPool.play(mp3a,1f , 1f, 0, 0, 1f);
                score = 2;
                //背景色を変更
                findViewById(R.id.question).setBackgroundColor(Color.GREEN);
                findViewById(R.id.answer).setBackgroundColor(Color.GREEN);
            } else if (question > answer) {
                result = "LOSE";
                Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(100);
                score = -1;
                //背景色を変更
                findViewById(R.id.question).setBackgroundColor(Color.BLUE);
                findViewById(R.id.answer).setBackgroundColor(Color.BLUE);
            } else {
                result = "DRAW";
                score = 1;
                findViewById(R.id.question).setBackgroundColor(Color.WHITE);
                findViewById(R.id.answer).setBackgroundColor(Color.WHITE);
            }
        } else {
            if (question > answer) {
                result = "WIN";
                soundPool.play(mp3a,1f , 1f, 0, 0, 1f);
                score = 2;
                //背景色を変更
                findViewById(R.id.question).setBackgroundColor(Color.GREEN);
                findViewById(R.id.answer).setBackgroundColor(Color.GREEN);
            } else if (question < answer) {
                result = "LOSE";
                Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(100);
                score = -1;
                //背景色を変更
                findViewById(R.id.question).setBackgroundColor(Color.BLUE);
                findViewById(R.id.answer).setBackgroundColor(Color.BLUE);
            } else {
                result = "DRAW";
                score = 1;
                findViewById(R.id.question).setBackgroundColor(Color.WHITE);
                findViewById(R.id.answer).setBackgroundColor(Color.WHITE);
            }
        }

        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText("結果：" + question + ":" + answer + "(" + result + ")");

        // 続けて遊べるように値を更新
        setNextQuestion();
        // スコアを表示
        setScore(score);
        // アニメーションを開始
        onAnimation(result);
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
        TextView textView = (TextView)findViewById(R.id.text_score);
        prefEditor.putString("text_input",textView.getText().toString());
        prefEditor.commit();
    }

    protected void onResume(){
        super.onResume();
        TextView textView = (TextView) findViewById(R.id.text_score);
        String readText = pref.getString("text_input","0");
        textView.setText(readText);
    }

    Animation animation;

    protected void onAnimation(String result)
    {
        // テキストオブジェクトをレイアウトから取得
        TextView textView = findViewById(R.id.answer);

        if(result == "WIN")
        {
            // 勝ち
            animation = AnimationUtils.loadAnimation(this, R.anim.rotation_animation);
            textView.startAnimation(animation);
        }
        else if(result == "LOSE")
        {
            // 負け
            set = (AnimatorSet) AnimatorInflater.loadAnimator(MainActivity.this, R.animator.blink_animation);
            set.setTarget(textView);
            set.start();
        }
        else
        {
            // 引き分け
        }

    }

}

