package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import android.media.AudioAttributes;
import android.media.SoundPool;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    AnimatorSet set;

    int mySoundID;          //サウンド管理ID
    int oto;                //サウンド
    SoundPool soundPool;    //サウンドプール
    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;
    int newScore;
    int colorQ;
    int colorA;

    boolean isVibrationEnabled = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(this);

        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        Button btn3 = findViewById(R.id.button3);
        btn3.setOnClickListener(this);


        set = (AnimatorSet) AnimatorInflater.loadAnimator(MainActivity.this,
                R.animator.blink_animation);

        set.setTarget(findViewById(R.id.answer));

        // サウンドプールをクリア
        soundPool = null;

        // 音を出すための手続き１　※音の出し方を設定している
        AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).setContentType(AudioAttributes.CONTENT_TYPE_SPEECH).build();

        // 音を出すための手続き２　※１の設定を利用してsoundPoolを設定
        soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(1).build();

        // 鳴らしたい音を設定（rawフォルダにあるsound1という音）
        oto = getResources().getIdentifier("sound1", "raw", getPackageName());

        //あらかじめ音をロードする必要がある　※直前にロードしても間に合わないので早めに
        mySoundID = soundPool.load(getBaseContext(), oto, 1);

        pref = getSharedPreferences("memorizeScore", MODE_PRIVATE);
        prefEditor = pref.edit();
        isVibrationEnabled = pref.getBoolean("vibrationEnabled", true);

        //色の取得
        colorQ = getResources().getColor(R.color.Question);
        colorA = getResources().getColor(R.color.Answer);

        // 起動時に関数を呼び出す
        setQuestionValue();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.volume_setting) {
            // 音量設定の処理をここに追加
            return true;
        } else if (id == R.id.vibration_setting) {
            isVibrationEnabled = !isVibrationEnabled;
            String status = isVibrationEnabled ? "ON" : "OFF";
            Toast.makeText(this, "Vibration " + status, Toast.LENGTH_SHORT).show();
            prefEditor.putBoolean("vibrationEnabled", isVibrationEnabled);
            prefEditor.apply(); // commit()の代わりにapply()を使用することを推奨
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button1) {
            setAnswerValue(true);
            checkResult(true);
        } else if (id == R.id.button2) {
            setAnswerValue(true);
            checkResult(false);
        } else if (id == R.id.button3) {
            setQuestionValue();
            clearAnswerValue();
            clearScoreValue();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();

        String textScore = Integer.toString(newScore);
        prefEditor.putString("memorizeScore", textScore);
        prefEditor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        TextView scoreText = (TextView) findViewById(R.id.text_score);

        int preScore = pref.getInt("memorizeScore", 0);
        scoreText.setText(String.valueOf(preScore));
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

        txtView.setBackgroundColor(colorQ);
        TextView txtViewAnswer = findViewById(R.id.answer);
        txtViewAnswer.setBackgroundColor(colorA);
    }

    private void setAnswerValue(boolean isButton) {
        Random r = new Random();
        int answerValue = r.nextInt(10 + 1);

        TextView txtView = findViewById(R.id.answer);
        if (isButton) {
            txtView.setText(Integer.toString(answerValue));
        } else {
                     txtView.setText("?");
        }

    }

    private void checkResult(boolean isHigh) {
        TextView txtViewQuestion = findViewById(R.id.question);
        TextView txtViewAnswer = findViewById(R.id.answer);

        int question = Integer.parseInt(txtViewQuestion.getText().toString());
        int answer = Integer.parseInt(txtViewAnswer.getText().toString());

        TextView txtResult = (TextView) findViewById(R.id.text_result);

        String result;
        int score;

        if (isHigh) {
            if (question < answer) {
                result = "WIN";
                score = 2;
                if (isVibrationEnabled) {
                    Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                    vib.vibrate(500);
                }
                // 音を鳴らす
                soundPool.play(mySoundID, 0.1f, 0.1f, 0, 0, 1);

                txtViewQuestion.setBackgroundColor(Color.LTGRAY);
                txtViewAnswer.setBackgroundColor(colorA);
            } else if (question > answer) {
                result = "LOSE";
                score = -1;

                txtViewAnswer.setBackgroundColor(Color.LTGRAY);
                txtViewQuestion.setBackgroundColor(colorQ);
            } else {
                result = "DRAW";
                score = 1;

                txtViewAnswer.setBackgroundColor(colorA);
                txtViewQuestion.setBackgroundColor(colorQ);
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                if (isVibrationEnabled) {
                    Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                    vib.vibrate(500);
                }
                // 音を鳴らす
                soundPool.play(mySoundID, 1f, 1f, 0, 0, 1);

                txtViewQuestion.setBackgroundColor(Color.LTGRAY);
                txtViewAnswer.setBackgroundColor(colorA);
            } else if (question < answer) {
                result = "LOSE";
                score = -1;

                txtViewAnswer.setBackgroundColor(Color.LTGRAY);
                txtViewQuestion.setBackgroundColor(colorQ);
            } else {
                result = "DRAW";
                score = 1;

                txtViewAnswer.setBackgroundColor(colorA);
                txtViewQuestion.setBackgroundColor(colorQ);
            }
        }

        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText("結果：" + question + ":" + answer + "(" + result + ")");
        setWinAnimation(result);
        // 続けて遊べるように値を更新

        setNextQuestion();
        countDownTimer();
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
        newScore = Integer.parseInt(txtScore.getText().toString()) + score;
        txtScore.setText(Integer.toString(newScore));
    }

    private void clearScoreValue() {
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        txtScore.setText("0");
    }

    private  void countDownTimer() {
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
               setAnswerValue(false);
            }
        }.start();
    }
  
    private void setWinAnimation(String result) {
        TextView txtViewAnswer = findViewById(R.id.answer);

        if(result == "WIN"){

            if(!set.isRunning()){
                set.start();
            }
        }
        else {
            if(set.isRunning()){
                set.end();
                txtViewAnswer.setAlpha(1.0f);
            }
        }
    }
}

