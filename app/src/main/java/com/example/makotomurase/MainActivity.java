package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.SharedPreferences;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Insets;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //プリファレンスの生成
    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

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

        //プリファレンスの生成
        pref = getSharedPreferences("team-g",MODE_PRIVATE);
        prefEditor = pref.edit();

        // 起動時に関数を呼び出す
        setQuestionValue();
    }

    @SuppressLint({"NonConstantResourceId", "NewApi"})
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
            Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vib.vibrate(50);
            clearColorValue();
        }
    }

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        String PlayerNumber = getResources().getString(R.string.PlayerNum);
        txtView.setText(PlayerNumber);
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
        String PlayerNumber = getResources().getString(R.string.PlayerNum);

        int question = Integer.parseInt(txtViewQuestion.getText().toString());
        int answer = Integer.parseInt(txtViewAnswer.getText().toString());

        TextView txtResult = (TextView) findViewById(R.id.text_result);

        // 結果を示す文字列を入れる変数を用意
        String result;
        int score;

        String lastresultWIN = getResources().getString(R.string.lastresultWIN);
        String lastresultLOSE = getResources().getString(R.string.lastresultLOSE);
        String lastresultDRAW = getResources().getString(R.string.lastresultDRAW);
        View layout1 =findViewById(R.id.question);
        View layout2 = findViewById(R.id.answer);

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = lastresultWIN;
                score = 2;
                Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(400);

                layout1.setBackgroundColor(Color.CYAN);
                layout2.setBackgroundColor(Color.RED);
            } else if (question > answer) {
                result = lastresultLOSE;
                score = -1;
                Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(1000);
                layout1.setBackgroundColor(Color.RED);
                layout2.setBackgroundColor(Color.CYAN);
            } else {
                result = lastresultDRAW;
                score = 1;
                Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(500);
            }
        } else {
            if (question > answer) {
                result = lastresultWIN;
                layout1.setBackgroundColor(Color.RED);
                layout2.setBackgroundColor(Color.CYAN);
                score = 2;
                Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(400);
            } else if (question < answer) {
                result = lastresultLOSE;
                layout1.setBackgroundColor(Color.CYAN);
                layout2.setBackgroundColor(Color.RED);
                score = -1;
                Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(1000);
            } else {
                result = lastresultDRAW;
                layout1.setBackgroundColor(Color.GREEN);
                layout2.setBackgroundColor(Color.GREEN);
                score = 1;
                Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(500);
            }
        }

        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText(question + ":" + answer + "(" + result + ")");

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

    private void score_Save(){
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        int Score = Integer.parseInt(txtScore.getText().toString());
        //プリファレンスの保存
        prefEditor.putInt("pref_score",Score);
        prefEditor.commit();
    }

    private void score_Load(){
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        //プリファレンスの読み込み
        int Pref_Score = pref.getInt("pref_score",0);
        txtScore.setText(Integer.toString(Pref_Score));
    }

    @Override
    protected void onResume() {
        super.onResume();
        score_Load();
    }

    @Override
    protected void onPause() {
        super.onPause();
        score_Save();
    }
    private void clearColorValue() {
        View layout1 =findViewById(R.id.question);
        View layout2 = findViewById(R.id.answer);
        layout1.setBackgroundColor(Color.MAGENTA);
        layout2.setBackgroundColor(Color.YELLOW);
    }

}




