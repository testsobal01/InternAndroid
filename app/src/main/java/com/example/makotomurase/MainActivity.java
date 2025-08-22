package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.SharedPreferences;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Insets;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //プリファレンスの生成
    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

    private static final long START_TIME = 10000;
    private TextView mTextViewCountDown;
    private Button mButtonStartPause;
    private Button getmButtonReset;

    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;

    private long mTimeLeftInMillis = START_TIME;





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


        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        getmButtonReset = findViewById(R.id.button3);
        mButtonStartPause = findViewById(R.id.button4);

        //プリファレンスの生成
        pref = getSharedPreferences("team-g",MODE_PRIVATE);
        prefEditor = pref.edit();

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mTimerRunning) {
                    startTimer();
                }
            }
        });

        // 起動時に関数を呼び出す
        setQuestionValue();
    }

    private void startTimer(){
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                mTimerRunning = false;
                mTimeLeftInMillis = 0;
                updateCountDownText();
                mButtonStartPause.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "タイムアップ！", Toast.LENGTH_SHORT).show();
            }
        }.start();

        mTimerRunning = true;
    }


    private void resetTimer(){
        mTimeLeftInMillis = START_TIME;
        updateCountDownText();
        mTimerRunning = false;
        mButtonStartPause.setVisibility(View.VISIBLE);
    }

    private void updateCountDownText() {
        int minutes = (int)(mTimeLeftInMillis/1000)/60;
        int seconds = (int)(mTimeLeftInMillis/1000)%60;

        // 0秒以下になったら強制的に 00:00 表示
        if (mTimeLeftInMillis <= 0) {
            minutes = 0;
            seconds = 0;
        }

        String timerLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mTextViewCountDown.setText(timerLeftFormatted);
    }



    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (mTimerRunning) {
            startTimer();
        }

        // タイマーが動いているときだけ結果判定を実行
        if (mTimerRunning) {
            if (id == R.id.button1) {
                setAnswerValue();
                checkResult(true);
            } else if (id == R.id.button2) {
                setAnswerValue();
                checkResult(false);
            }
        }

        if (id == R.id.button3) {
            setQuestionValue();
            clearAnswerValue();
            clearScoreValue();
            resetTimer();
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
        double probability = 0.05;
        //5%の確率で777が出る。
        if (r.nextDouble() < probability) {
            answerValue = 777;
        }
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
            if(answer == 777){
                result = "Lucky Win";
                score = 2;
            }else if (question < answer) {
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
            if(answer == 777){
                result = "Lucky Win";
                score = 2;
            } else if (question > answer) {
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
}

