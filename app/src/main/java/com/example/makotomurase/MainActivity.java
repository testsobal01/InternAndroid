package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;



import android.content.res.Resources;
import android.graphics.Color;

import android.content.SharedPreferences;

import android.os.Bundle;
import android.os.CountDownTimer;

import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;

import android.os.Vibrator;

import android.view.View;
import android.view.animation.Animation;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences sharedPref;

    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

    private SoundPool soundPool;
    private int soundOne, soundTwo;
    private Button button1, button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1 = findViewById(R.id.button1);
        Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE) ;
        vib.vibrate(1000);

        btn1.setOnClickListener(this);

        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(this);

        // "AndroidSeminor" は、保存する先のファイル名のようなもの
        pref = getSharedPreferences("AndroidSeminor", MODE_PRIVATE);
        prefEditor = pref.edit();

        // 起動時に関数を呼び出す
        setQuestionValue();

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
                // ストリーム数に応じて
                .setMaxStreams(2)
                .build();

        // one.wav をロードしておく
        soundOne = soundPool.load(this, R.raw.hit, 1);

        // two.wav をロードしておく
        soundTwo = soundPool.load(this, R.raw.hit, 1);

        // load が終わったか確認する場合
        soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
            Log.d("debug","sampleId="+sampleId);
            Log.d("debug","status="+status);
        });

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);

        button1.setOnClickListener( v -> {
            // one.wav の再生
            // play(ロードしたID, 左音量, 右音量, 優先度, ループ,再生速度)
            soundPool.play(soundOne, 1.0f, 1.0f, 0, 0, 1);

            // ボタンの回転アニメーション
            RotateAnimation buttonRotation = new RotateAnimation(
                    0, 360, (float)(button1.getWidth()/2), (float)(button1.getHeight()/2));
            buttonRotation.setDuration(2000);
            button1.startAnimation(buttonRotation);
        });

        button2.setOnClickListener( v -> {
            // two.wav の再生
            soundPool.play(soundTwo, 1.0f, 1.0f, 1, 0, 1);

            // ボタンの回転アニメーション
            RotateAnimation buttonRotation = new RotateAnimation(
                    0, 360, (float)(button2.getWidth()/2), (float)(button2.getHeight()/2));
            buttonRotation.setDuration(2000);
            button2.startAnimation(buttonRotation);
        });
    }



    @Override
    public void onClick(View view) {
        Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE) ;
        vib.vibrate(1000);

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

        if(id == R.id.button1){

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
                result = "WIN";
                startRotationAnswer();
                score = 2;
                TextView txtView = findViewById(R.id.question);
                txtView.setBackgroundColor(getResources().getColor(R.color.red));
                TextView txtView1 = findViewById(R.id.answer);
                txtView1.setBackgroundColor(getResources().getColor(R.color.red));
            } else if (question > answer) {
                result = "LOSE";
                startRotationQuestion();
                score = -1;
                TextView txtView = findViewById(R.id.question);
                txtView.setBackgroundColor(getResources().getColor(R.color.blue));
                TextView txtView1 = findViewById(R.id.answer);
                txtView1.setBackgroundColor(getResources().getColor(R.color.blue));
            } else {
                result = "DRAW";
                score = 1;
                TextView txtView = findViewById(R.id.question);
                txtView.setBackgroundColor(getResources().getColor(R.color.green));
                TextView txtView1 = findViewById(R.id.answer);
                txtView1.setBackgroundColor(getResources().getColor(R.color.green));
            }
        } else {
            if (question > answer) {
                result = "WIN";
                startRotationAnswer();
                score = 2;
                TextView txtView = findViewById(R.id.question);
                txtView.setBackgroundColor(getResources().getColor(R.color.red));
                TextView txtView1 = findViewById(R.id.answer);
                txtView1.setBackgroundColor(getResources().getColor(R.color.red));
            } else if (question < answer) {
                result = "LOSE";
                startRotationQuestion();
                score = -1;
                TextView txtView = findViewById(R.id.question);
                txtView.setBackgroundColor(getResources().getColor(R.color.blue));
                TextView txtView1 = findViewById(R.id.answer);
                txtView1.setBackgroundColor(getResources().getColor(R.color.blue));
            } else {
                result = "DRAW";
                score = 1;
                TextView txtView = findViewById(R.id.question);
                txtView.setBackgroundColor(getResources().getColor(R.color.green));
                TextView txtView1 = findViewById(R.id.answer);
                txtView1.setBackgroundColor(getResources().getColor(R.color.green));
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

                TextView txtView = findViewById(R.id.question);
                txtView.setBackgroundColor(getResources().getColor(R.color.magenta));
                TextView txtView1 = findViewById(R.id.answer);
                txtView1.setBackgroundColor(getResources().getColor(R.color.yellow));
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


    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();

        TextView textView = (TextView)findViewById(R.id.text_score);
        // "main_input"というキー名（箱）に、文字列を保存
        prefEditor.putString("keep", textView.getText().toString());
        prefEditor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();

        // 画面上に文字列をセットするため、テキストビューを取得
        TextView textView = (TextView)findViewById(R.id.text_score);
        // 保存した値をキー名（main_input）を指定して取得。
        // 一度も保存されていない場合もありえるので、その時に変わりに表示する文字列も指定する
        String readText = pref.getString("keep", "保存されていません");
        textView.setText(readText);

    }
    public void startRotationQuestion(){

        TextView txtViewQuestion = findViewById(R.id.question);

        // RotateAnimation(float fromDegrees, float toDegrees, int pivotXType, float pivotXValue, int pivotYType,float pivotYValue)
        RotateAnimation rotate = new RotateAnimation(0.0f, 360.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        // animation時間 msec
        rotate.setDuration(3000);
        // 繰り返し回数
        rotate.setRepeatCount(0);
        // animationが終わったそのまま表示にする
        rotate.setFillAfter(true);

        //アニメーションの開始
        txtViewQuestion.startAnimation(rotate);

    }

    public void startRotationAnswer(){

        TextView txtViewAnswer = findViewById(R.id.answer);

        // RotateAnimation(float fromDegrees, float toDegrees, int pivotXType, float pivotXValue, int pivotYType,float pivotYValue)
        RotateAnimation rotate = new RotateAnimation(0.0f, 360.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        // animation時間 msec
        rotate.setDuration(3000);
        // 繰り返し回数
        rotate.setRepeatCount(0);
        // animationが終わったそのまま表示にする
        rotate.setFillAfter(true);

        //アニメーションの開始
        txtViewAnswer.startAnimation(rotate);
    }

}

