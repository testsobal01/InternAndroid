package com.example.makotomurase;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationAttributes;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SoundPlayer sp;

    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

  private long pattern1[] = {0,100,80,100,80,300,100,0,100,0,100,300,80,100,80,300,80,300,80,300};
    private long pattern2[] = {0,300,80,300,80,100,80,300,80,100,100,0,100,0,100,300,80,100};
    private long pattern3[] = {0,100,100,100,100,100,100,0,100,0,100,300,80,300,80,300,100,0,100,0,100,100,80,100,80,100};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Bundle extra = intent.getExtras();

        Button btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(this);

        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(this);

        // 起動時に関数を呼び出す
        setQuestionValue();

        sp = new SoundPlayer(this);

        pref = getSharedPreferences("MakotoMurase",MODE_PRIVATE);
        prefEditor = pref.edit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //scoreの保存
        TextView textView = (TextView) findViewById(R.id.text_score);//保存するscoreを取得
        prefEditor.putString("score_input",textView.getText().toString());
        prefEditor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView textView = (TextView) findViewById(R.id.text_score);
        String readText = pref.getString("score_input","0");
        textView.setText(readText);
    }

    @Override
    public void onClick(View view) {

        sp.playHitSound();
        int id = view.getId();

        Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);

        if (id == R.id.button1) {
            vib.cancel();
            setAnswerValue();
            checkResult(true);
            vib.vibrate(pattern1,-1);
        } else if (id == R.id.button2) {
            vib.cancel();
            setAnswerValue();
            checkResult(false);
            vib.vibrate(pattern2,-1);
        } else if (id == R.id.button3) {
            vib.cancel();
            setQuestionValue();
            clearAnswerValue();
            clearScoreValue();
            change_back_color(1);
            animation(1);
            vib.vibrate(pattern3,-1);
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
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText("結果：" + question + ":" + answer + "(" + result + ")");

        //勝敗で背景色変更
        change_back_color(score);

        //勝敗でアニメーション
        animation(score);

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

    public void change_back_color(int result){
        LinearLayout filled = findViewById(R.id.main);
        switch (result){
            case 2:
                filled.setBackgroundColor(Color.parseColor("#FFd700"));
                break;

            case -1:
                filled.setBackgroundColor(Color.CYAN);
                break;

            case 1:
                filled.setBackgroundColor(Color.WHITE);
                break;
        }
    }

    public void change_back_color_txt(int result){
        TextView answer = (TextView) findViewById(R.id.answer);
        TextView question = (TextView) findViewById(R.id.question);
        switch (result){
            case 2:
                answer.setBackgroundColor(Color.parseColor("#ffff00"));
                question.setBackgroundColor(Color.parseColor("#FFd700"));
                break;

            case -1:
                answer.setBackgroundColor(Color.CYAN);
                question.setBackgroundColor(Color.parseColor("#ff00ff"));
                break;

            case 1:
                answer.setBackgroundColor(Color.parseColor("#ffff00"));
                question.setBackgroundColor(Color.parseColor("#ff00ff"));
                break;
        }
    }

    public void animation(int result) {
        TextView answer = (TextView) findViewById(R.id.answer);
        TextView question = (TextView) findViewById(R.id.question);
        switch (result) {
            case 2:
                change_back_color_txt(result);
                question.animate().alpha(0.5f).setDuration(500);
                answer.animate().alpha(1f).setDuration(500);
                break;

            case -1:
                change_back_color_txt(result);
                answer.animate().alpha(0.5f).setDuration(500);
                question.animate().alpha(1f).setDuration(500);
                break;

            case 1:
                change_back_color_txt(result);
                answer.animate().alpha(1f).setDuration(500);
                question.animate().alpha(1f).setDuration(500);
                break;
        }
    }
}

