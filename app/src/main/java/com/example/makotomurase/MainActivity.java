package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.text.Layout;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import android.media.AudioManager;
import android.media.SoundPool;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;
    private SoundPool m_soundPool;
    private int m_soundID;

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

        m_soundPool = new SoundPool(1,AudioManager.STREAM_MUSIC,0);
        m_soundID = m_soundPool.load(this.getApplicationContext(),R.raw.sound1,1);

        // 起動時に関数を呼び出す
        setQuestionValue();

        pref = getSharedPreferences("AndroidSeminar",MODE_PRIVATE);
        prefEditor = pref.edit();

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.button1:
                Vibrator vib1= (Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib1.vibrate(250);
                setAnswerValue();
                checkResult(true);
                break;
            case R.id.button2:
                Vibrator vib2= (Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib2.vibrate(250);
                setAnswerValue();
                checkResult(false);
                break;
            case R.id.button3:
                Vibrator vib3= (Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib3.vibrate(500);
                setQuestionValue();
                clearAnswerValue();
                break;

        }

        Animation animation= AnimationUtils.loadAnimation(this,R.anim.anim);

        findViewById(R.id.question).startAnimation(animation);
        findViewById(R.id.answer).startAnimation(animation);
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


        //question.setTextColor(getResources().getColor(R.color.colorRED));
        //answer.setTextColor(getResources().getColor(R.color.colorRED));
        //question.setTextColor(getResources().getColor(R.color.colorBULE));
        //answer.setTextColor(getResources().getColor(R.color.colorBULE));


        // 結果を示す文字列を入れる変数を用意
        String result;
        int score = 0;

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = "WIN";
                score = 2;
                txtViewQuestion.setBackgroundColor(getResources().getColor(R.color.colorRED));
                txtViewAnswer.setBackgroundColor(getResources().getColor(R.color.colorRED));
            } else if (question > answer) {
                result = "LOSE";
                m_soundPool.play(m_soundID,1.0F,1.0F,0,0,1.0F);
                score = -1;
                txtViewQuestion.setBackgroundColor(getResources().getColor(R.color.colorBULE));
                txtViewAnswer.setBackgroundColor(getResources().getColor(R.color.colorBULE));

            } else {
                result = "DRAW";
                score = 1;
                txtViewQuestion.setBackgroundColor(getResources().getColor(R.color.colorWHITE));
                txtViewAnswer.setBackgroundColor(getResources().getColor(R.color.colorWHITE));
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                txtViewQuestion.setBackgroundColor(getResources().getColor(R.color.colorRED));
                txtViewAnswer.setBackgroundColor(getResources().getColor(R.color.colorRED));
            } else if (question < answer) {
                result = "LOSE";
                m_soundPool.play(m_soundID,1.0F,1.0F,0,0,1.0F);
                score = -1;
                txtViewQuestion.setBackgroundColor(getResources().getColor(R.color.colorBULE));
                txtViewAnswer.setBackgroundColor(getResources().getColor(R.color.colorBULE));
            } else {
                result = "DRAW";
                score = 1;
                txtViewQuestion.setBackgroundColor(getResources().getColor(R.color.colorWHITE));
                txtViewAnswer.setBackgroundColor(getResources().getColor(R.color.colorWHITE));
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
            }
        }.start();
    }

    private void setScore(int score) {

        TextView txtScore = (TextView) findViewById(R.id.text_score);
        int newScore = Integer.parseInt(txtScore.getText().toString()) + score;
        txtScore.setText(Integer.toString(newScore));
    }
    @Override
    protected void onPause(){
        super.onPause();
        TextView textView = (TextView)findViewById(R.id.text_score);
        prefEditor.putString("main_input",textView.getText().toString());
        prefEditor.commit();
    }

    @Override
    protected void onResume(){
        super.onResume();

        TextView textView =(TextView)findViewById(R.id.text_score);
        String readText = pref.getString("main_input","0");
        textView.setText(readText);
        
    }


}