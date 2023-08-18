package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;


import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;

import android.content.Context;
import android.content.SharedPreferences;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    AnimatorSet set;

    SharedPreferences pref;
    SharedPreferences.Editor editor;


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

        // 起動時に関数を呼び出す
        setQuestionValue();

        TextView textView = findViewById(R.id.text_score);
        pref = getSharedPreferences("score", MODE_PRIVATE);
        editor = pref.edit();
        if (pref.getString("score", "0") == "0") {
            editor.putString("score", textView.getText().toString());
        }
        editor.commit();

    }

    @Override
    public void onClick(View view) {


        int id = view.getId();
        if (id == R.id.button1) {
            setAnswerValue();
            checkResult(true);

            Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
            vib.vibrate(100);
        }

        else if (id == R.id.button2) {
            setAnswerValue();
            checkResult(false);

            Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
            vib.vibrate(50);
        }

        else if (id == R.id.button3) {
            Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
            vib.vibrate(50);
            setQuestionValue();
            clearAnswerValue();
            clearScoreValue();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
        TextView textView = findViewById(R.id.text_score);
        editor.putString("score", textView.getText().toString());
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Toast.makeText(this,"onResume",Toast.LENGTH_SHORT).show();
        TextView txtScore = findViewById(R.id.text_score);
        txtScore.setText(pref.getString("score", ""));
    }

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText(String.format("%s", getString(R.string.label_atai)));
    }

    private void setQuestionValue() {
        Random r = new Random();
        // 0から10の範囲で乱数を生成（+1する必要がある）
        int questionValue = r.nextInt(10 + 1);

        TextView txtView = findViewById(R.id.question);
        txtView.setText(Integer.toString(questionValue));

        set = (AnimatorSet) AnimatorInflater.loadAnimator(MainActivity.this, R.animator.blink_animation);
        set.setTarget(txtView);
        // onStart();
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


        if (score == 2) {
            set = (AnimatorSet) AnimatorInflater.loadAnimator(MainActivity.this, R.animator.blink_animation);
            set.setTarget(txtViewAnswer);
            onStart();
        }
        else if (score == -1) {
            set = (AnimatorSet) AnimatorInflater.loadAnimator(MainActivity.this, R.animator.blink_animation);
            set.setTarget(txtViewQuestion);
            onStart();
        }


        setBackGroundColor(result);


        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText(String.format("%s" + question + ":" + answer + "(" + result + ")", getString(R.string.label_result)));

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


    @Override
    protected void onStart() {
        super.onStart();
        set.start();
    }
  
    private void setBackGroundColor(String result){
        TextView txtViewQuestion = findViewById(R.id.question);
        TextView txtViewAnswer = findViewById(R.id.answer);

        if(result.equals("WIN")){
            txtViewQuestion.setBackgroundColor(Color.parseColor("#FF0000"));
            txtViewAnswer.setBackgroundColor(Color.parseColor("#FF7F00"));
        }
        else if(result.equals("LOSE")){
            txtViewQuestion.setBackgroundColor(Color.parseColor("#007FFF"));
            txtViewAnswer.setBackgroundColor(Color.parseColor("#00FFFF"));
        }
        else if(result.equals("DRAW")){
            txtViewQuestion.setBackgroundColor(Color.parseColor("#FF7F00"));
            txtViewAnswer.setBackgroundColor(Color.parseColor("#FFFF00"));
        }

    }
}


