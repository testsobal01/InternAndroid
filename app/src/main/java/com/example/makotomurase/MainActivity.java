package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import android.graphics.Color;
import android.media.SoundPool;
import android.media.AudioManager;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SoundPool m_soundPool;
    private int button,lose1,lose2,restart,start,win1,win2;

    private Animation animation;
  
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

        animation = AnimationUtils.loadAnimation(this, R.anim.animation_set);

        // 起動時に関数を呼び出す
        setQuestionValue();

        m_soundPool = new SoundPool(1,AudioManager.STREAM_MUSIC,0);
        button = m_soundPool.load(this.getApplicationContext(), R.raw.button, 1);
        lose2 = m_soundPool.load(this.getApplicationContext(), R.raw.  lose2, 1);
        start = m_soundPool.load(this.getApplicationContext(), R.raw.start, 1);
        win1 = m_soundPool.load(this.getApplicationContext(), R.raw.win1, 1);

        pref=getSharedPreferences("AndroidSeminor",MODE_PRIVATE);
        prefEditor=pref.edit();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.button1:
                Vibrator vib=(Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(50);
                setAnswerValue();
                checkResult(true);
                m_soundPool.play(button, 1.0f, 1.0f, 1, 0, 1.0f);
                break;
            case R.id.button2:
                Vibrator vib2=(Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib2.vibrate(50);
                setAnswerValue();
                checkResult(false);
                m_soundPool.play(button, 1.0f, 1.0f, 1, 0, 1.0f);
                break;
            case R.id.button3:
                Vibrator vib3=(Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib3.vibrate(50);
                setQuestionValue();
                clearAnswerValue();
                m_soundPool.play(start, 1.0f, 1.0f, 1, 0, 1.0f);
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
        // 結果を示す文字列を入れる変数を用意
        String result;
        int score = 0;

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = "WIN";
                score = 2;
            } else if (question > answer) {
                result = "LOSE";
                score = -1;
            } else {
                result = "DRAW";
                score = 1;
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
            } else if (question < answer) {
                result = "LOSE";
                score = -1;
            } else {
                result = "DRAW";
                score = 1;
            }
        }


        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast ans = Toast.makeText(this, result, Toast.LENGTH_LONG);
        View back = ans.getView();
        txtResult.setText("結果：" + question + ":" + answer + "(" + result + ")");
        if(result == "WIN") {
            txtResult.setTextColor(Color.RED);
            back.setBackgroundColor(Color.RED);
            m_soundPool.play(win1, 1.0f, 1.0f, 2, 0, 1.0f);
            txtViewAnswer.startAnimation(animation);
        }else if(result == "LOSE"){
            txtResult.setTextColor(Color.BLUE);
            back.setBackgroundColor(Color.BLUE);
            m_soundPool.play(lose2, 1.0f, 1.0f, 2, 0, 1.0f);
            txtViewQuestion.startAnimation(animation);
        }else{
            txtResult.setTextColor(Color.BLACK);
            back.setBackgroundColor(Color.BLACK);
            m_soundPool.play(restart, 1.0f, 1.0f, 1, 0, 1.0f);
        }
        ans.show();

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
    protected void onPause() {
        super.onPause();

        TextView textView=(TextView)findViewById(R.id.text_score);

        prefEditor.putInt("main_input", Integer.parseInt(textView.getText().toString()));
        prefEditor.commit();


    }

    @Override
    protected void onResume() {
        super.onResume();

        TextView textView=(TextView)findViewById(R.id.text_score);

        int readText=pref.getInt("main_input", 0);
        textView.setText(Integer.toString(readText));



    }


}