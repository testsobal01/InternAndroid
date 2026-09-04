package com.example.makotomurase;



import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator; // ★インポートの重複を整理しました
import android.content.SharedPreferences;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SoundPool soundPool;
    private int soundId1; private int soundId2; private int soundId3; private int soundId4;
    private int soundId5; private int soundId6; private int soundId7; private int soundId8;
    private int soundId9;
    private String result = "";
    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = getSharedPreferences("AndroidPreference", MODE_PRIVATE);
        prefEditor = pref.edit();


        TextView textView=(TextView)findViewById(R.id.text_score);
        String savedScore = pref.getString("main_input", "0");
        textView.setText(savedScore);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
            view.setPadding(insets.left, insets.top, insets.right, 0);
            return windowInsets;
        });

        //region 各button
        Button btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(this);

        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(this);

        //endregion

        // 起動時に関数を呼び出す
        setQuestionValue();

        Audio();
    }


    //region 各button処理
    @Override
    public void onClick(View view) {
        vibration();
        int id = view.getId();
        if (id == R.id.button1) {
            setAnswerValue();
            checkResult(true);
            Audio2();
        } else if (id == R.id.button2) {
            setAnswerValue();
            checkResult(false);
            Audio2();
        } else if (id == R.id.button3) {
            setQuestionValue();
            clearAnswerValue();
            clearScoreValue();
        }
    }
  
      @Override

    protected void onPause(){
        super.onPause();

        Toast.makeText(this,"onPuse",Toast.LENGTH_SHORT).show();

        TextView textView=(TextView)findViewById(R.id.text_score);

        prefEditor.putString("main_input",textView.getText().toString());
        prefEditor.commit();
    }

    private void clearAnswerValue() { //
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText("値2");

        //背景色の定義と変更
        TextView txtViewQuestion = findViewById(R.id.question);
        TextView txtViewAnswer = findViewById(R.id.answer);
        txtViewAnswer.setBackgroundColor(Color.parseColor("#ffff00"));//黄色
        txtViewQuestion.setBackgroundColor(Color.parseColor("#ff00ff"));//ピンク

    } // answerリセットメソッド

    private void setQuestionValue() {
        Random r = new Random();
        int questionValue = r.nextInt(10 + 1);

        TextView txtView = findViewById(R.id.question);
        txtView.setText(Integer.toString(questionValue));
    } // questionリセットメソッド

    private void setAnswerValue() { //
        Random r = new Random();
        int answerValue = r.nextInt(10 + 1);

        TextView txtView = findViewById(R.id.answer);
        txtView.setText(Integer.toString(answerValue));
    } // answer乱数生成メソッド

    private void checkResult(boolean isHigh) { //
        TextView txtViewQuestion = findViewById(R.id.question);
        TextView txtViewAnswer = findViewById(R.id.answer);

        int question = Integer.parseInt(txtViewQuestion.getText().toString());
        int answer = Integer.parseInt(txtViewAnswer.getText().toString());

        TextView txtResult = (TextView) findViewById(R.id.text_result);


        // 結果を示す文字列を入れる変数を用意
        int score;
        boolean isWin = false; // ★【修正】isWin変数をここで作りました

        // Highが押された
        if (isHigh) {
            if (question < answer) {
                result = "WIN";
                score = 2;
                isWin = true; // 勝ち
                txtViewAnswer.setBackgroundColor(Color.parseColor("#ffff00"));//黄色
                txtViewQuestion.setBackgroundColor(Color.parseColor("#a611a6"));//暗いピンク
               // txtViewQuestion.setBackgroundColor(Color.parseColor("#e80ce8"));//暗いピンク

            } else if (question > answer) {
                result = "LOSE";
                score = -1;
                isWin = false; // 負け
                txtViewQuestion.setBackgroundColor(Color.parseColor("#ff00ff"));//ピンク
                txtViewAnswer.setBackgroundColor(Color.parseColor("#999923"));//暗い黄色
            } else {
                result = "DRAW";
                score = 1;
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                isWin = true; // 勝ち
                txtViewAnswer.setBackgroundColor(Color.parseColor("#ffff00"));//黄色
                txtViewQuestion.setBackgroundColor(Color.parseColor("#a611a6"));//暗いピンク
            } else if (question < answer) {
                result = "LOSE";
                score = -1;
                isWin = false; // 負け
                txtViewQuestion.setBackgroundColor(Color.parseColor("#ff00ff"));//ピンク
                txtViewAnswer.setBackgroundColor(Color.parseColor("#999923"));//暗い黄色
            } else {
                result = "DRAW";
                score = 1;
            }
        }

        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText("結果：" + question + ":" + answer + "(" + result + ")");

        // 続けて遊べるように値を更新
        setNextQuestion();
        // スコアを表示
        setScore(score);

        View bgQuestion = findViewById(R.id.question);
        View bgAnswer = findViewById(R.id.answer);

        // 勝敗に応じたアニメーションの処理
        if (result.equals("WIN")) {
            // 勝ったとき：左右の文字(question, answer)を大きくしながらアニメーション
            playResultAnimation(txtViewQuestion, true);
            playResultAnimation(txtViewAnswer, true);

            // 勝ったときだけ、背景全体（id: main）も一緒に大きく3Dアニメーションさせる！


            if (bgQuestion != null) {
                playResultAnimation(bgQuestion, true);
            }
            else if (bgAnswer != null) {
                playResultAnimation(bgAnswer, true);
            }

        } else if (result.equals("LOSE")) {
            // 負けたとき：左右の文字だけを小さくしながらアニメーション（背景は動かさない）
            playResultAnimation(txtViewQuestion, false);
            playResultAnimation(txtViewAnswer, false);


        }

    } // 勝敗表示メソッド

    //region リセットタイマーメソッド
    private void setNextQuestion() {
        // 第１引数がカウントダウン時間、第２引数は途中経過を受け取る間隔
        // 単位はミリ秒（1秒＝1000ミリ秒）
        new CountDownTimer(1500, 1000) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                setQuestionValue();
            }
        }.start();
    }
    //endregion

    private void setScore(int score) { //
        TextView txtScore = (TextView) findViewById(R.id.text_score);

        // エラー防止のための数値変換処理
        String scoreText = txtScore.getText().toString();
        int currentScore = 0;
        try {
            currentScore = Integer.parseInt(scoreText);
        } catch (NumberFormatException e) {
            currentScore = 0;
        }

        int newScore = currentScore + score;
        txtScore.setText(Integer.toString(newScore));
    } // スコア表示メソッド

    private void clearScoreValue() { //
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        txtScore.setText("0");
    }// スコアリセットメソッド

    private void vibration() { //
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(VibrationEffect.createOneShot(
                500, VibrationEffect.DEFAULT_AMPLITUDE));
    }// バイブレーションメソッド（項目4）

    protected  void Audio(){
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build();

        soundId1 = soundPool.load(this, R.raw.kurae, 1 );
        soundId2 = soundPool.load(this, R.raw.gu, 1 );
        soundId3 = soundPool.load(this, R.raw.saa, 1 );
        soundId4 = soundPool.load(this, R.raw.yaru, 1 );
        soundId5 = soundPool.load(this, R.raw.amai, 1 );
        soundId6 = soundPool.load(this, R.raw.kikanai, 1 );
        soundId7 = soundPool.load(this, R.raw.nidan, 1 );
        soundId8 = soundPool.load(this, R.raw.ukete, 1 );
        soundId9 = soundPool.load(this, R.raw.ku, 1 );

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool pool, int sampleId, int status) {
                if (status == 0) { // status == 0 は成功を意味します

                }
            }
        });
    }

    protected void Audio2(){
        Random ran = new Random();
        int ra = ran.nextInt(5)+1;
        if (result == "WIN"){
            if (ra > 2){
                soundPool.play(soundId1, 1.0f, 1.0f, 0, 0, 1.0f);
            }else if (ra < 2){
                soundPool.play(soundId7, 1.0f, 1.0f, 0, 0, 1.0f);
            }else {
                soundPool.play(soundId8, 1.0f, 1.0f, 0, 0, 1.0f);
            }
        }else if (result == "LOSE"){
            if (ra > 4){
                soundPool.play(soundId2, 1.0f, 1.0f, 0, 0, 1.0f);
            }else if(ra < 2){
                soundPool.play(soundId9, 1.0f, 1.0f, 0, 0, 1.0f);
            }else {
                soundPool.play(soundId4, 1.0f, 1.0f, 0, 0, 1.0f);
            }

        }else if (result == "DRAW"){
            soundPool.play(soundId6, 1.0f, 1.0f, 0, 0, 1.0f);
        }
    }

    private void playResultAnimation(View textView, boolean isWin) {
        float distance = 8000f;
        textView.setCameraDistance(distance);

        // 1. 前半：Y軸の3D回転アニメーションしながら拡大または縮小
        ObjectAnimator rotateY = ObjectAnimator.ofFloat(textView, "rotationY", 0f, 360f);
        ObjectAnimator scaleX1;
        ObjectAnimator scaleY1;

        if (isWin) {
            scaleX1 = ObjectAnimator.ofFloat(textView, "scaleX", 1.0f, 1.8f);
            scaleY1 = ObjectAnimator.ofFloat(textView, "scaleY", 1.0f, 1.8f);
        } else {
            scaleX1 = ObjectAnimator.ofFloat(textView, "scaleX", 1.0f, 0.5f);
            scaleY1 = ObjectAnimator.ofFloat(textView, "scaleY", 1.0f, 0.5f);
        }

        // 2. 後半：変化したサイズから1.0（元のサイズ）に戻すアニメーション
        ObjectAnimator scaleX2 = ObjectAnimator.ofFloat(textView, "scaleX", isWin ? 1.8f : 0.5f, 1.0f);
        ObjectAnimator scaleY2 = ObjectAnimator.ofFloat(textView, "scaleY", isWin ? 1.8f : 0.5f, 1.0f);

        AnimatorSet firstSet = new AnimatorSet();
        firstSet.playTogether(rotateY, scaleX1, scaleY1);
        firstSet.setDuration(600); // 0.6秒

        AnimatorSet secondSet = new AnimatorSet();
        secondSet.playTogether(scaleX2, scaleY2);
        secondSet.setDuration(400); // 0.4秒

        AnimatorSet totalSet = new AnimatorSet();
        totalSet.playSequentially(firstSet, secondSet);
        totalSet.start();
    }
}





