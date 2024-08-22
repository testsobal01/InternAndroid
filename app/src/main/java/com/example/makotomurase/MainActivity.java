package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;
  
    // 機能8 準備（コンポを部屋に置く）
    SoundPool soundPool;    // 効果音を鳴らす本体（コンポ）
    int mp3a;          // 効果音

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

        // CD入れる
        mp3a = soundPool.load(this, R.raw.buttonpushefect1, 1);
        // 起動時に関数を呼び出す
        setQuestionValue();

        pref = getSharedPreferences("AndroidSeminor", MODE_PRIVATE);
        prefEditor = pref.edit();
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.button1) {
            soundPool.play(mp3a,1f , 1f, 0, 0, 1f);
            setAnswerValue();
            checkResult(true);
        } else if (id == R.id.button2) {
            soundPool.play(mp3a,1f , 1f, 0, 0, 1f);
            setAnswerValue();
            checkResult(false);
        } else if (id == R.id.button3) {

            //以下2行バイブレーション機能
            Vibrator vib= (Vibrator)getSystemService(VIBRATOR_SERVICE);
            vib.vibrate(1000);

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
        int score;
        String result;


        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = "WIN";
                //勝った時の背景色を変える　High:左を青、右を赤
                TextView Color_High_Left = findViewById(R.id.question);
                Color_High_Left.setBackgroundColor(Color.CYAN);
                TextView Color_High_Right = findViewById(R.id.answer);
                Color_High_Right.setBackgroundColor(Color.argb(255,255,69,0));
                score = 2;
            } else if (question > answer) {
                result = "LOSE";
                //負けた時の背景色を変える　Lose:左を赤、右を青
                TextView Color_HIGH_Left = findViewById(R.id.question);
                Color_HIGH_Left.setBackgroundColor(Color.argb(255,255,69,0));
                TextView Color_HIGH_Right = findViewById(R.id.answer);
                Color_HIGH_Right.setBackgroundColor(Color.CYAN);
                score = -1;
            } else {
                result = "DRAW";
                TextView Color_HIGH_Left = findViewById(R.id.question);
                Color_HIGH_Left.setBackgroundColor(Color.MAGENTA);
                TextView Color_HIGH_Right = findViewById(R.id.answer);
                Color_HIGH_Right.setBackgroundColor(Color.MAGENTA);
                //引き分けた時の背景色を変える　Draw:紫
                score = 1;
            }
        } else {
            if (question > answer) {
                result = "WIN";
                //勝った時の背景色を変える　LOW:左を黄色、右を緑
                TextView Color_Low_Left = findViewById(R.id.question);
                Color_Low_Left.setBackgroundColor(Color.argb(255,255,165,0));
                TextView Color_Low_Right = findViewById(R.id.answer);
                Color_Low_Right.setBackgroundColor(Color.GREEN);
                score = 2;
            } else if (question < answer) {
                result = "LOSE";
                //負けた時の背景色を変える　LOW:左を緑、右を黄色
                TextView Color_Low_Left = findViewById(R.id.question);
                Color_Low_Left.setBackgroundColor(Color.GREEN);
                TextView Color_Low_Right = findViewById(R.id.answer);
                Color_Low_Right.setBackgroundColor(Color.argb(255,255,165,0));
                score = -1;
            } else {
                result = "DRAW";
                //引き分けた時の背景色を変える　LOW:グレー
                TextView Color_Low_Left = findViewById(R.id.question);
                Color_Low_Left.setBackgroundColor(Color.argb(255,169,169,169));
                TextView Color_Low_Right = findViewById(R.id.answer);
                Color_Low_Right.setBackgroundColor(Color.argb(255,169,169,169));
                score = 1;
            }
        }

        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText("結果：" + question + ":" + answer + "(" + result + ")");

        //以下アニメーション機能
        TextView spaceshipImage = (TextView) findViewById(R.id.question);
        TextView spaceshipImage2 = (TextView) findViewById(R.id.answer);
        if (result == "WIN"){
            Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.animation1);
            spaceshipImage.startAnimation(hyperspaceJumpAnimation);
            Animation hyperspaceJumpAnimation2 = AnimationUtils.loadAnimation(this, R.anim.animation2);
            spaceshipImage2.startAnimation(hyperspaceJumpAnimation2);
        } else if (result == "LOSE"){
            Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.animation2);
            spaceshipImage.startAnimation(hyperspaceJumpAnimation);
            Animation hyperspaceJumpAnimation2 = AnimationUtils.loadAnimation(this, R.anim.animation1);
            spaceshipImage2.startAnimation(hyperspaceJumpAnimation2);
        } else {

        }
        //ここまでアニメーション機能

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

    @Override
    protected  void onPause(){
        super.onPause();

       TextView textView = (TextView) findViewById(R.id.text_score);

       prefEditor.putInt("totalScoreLabel", Integer.parseInt(textView.getText().toString()));
       prefEditor.commit();


    }

    protected void onResume(){
        super.onResume();

        TextView textView = (TextView) findViewById(R.id.text_score);

        boolean b = false;
        int readText = pref.getInt("totalScoreLabel",Integer.parseInt(textView.getText().toString()));

        textView.setText(Integer.toString(readText));
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


}

