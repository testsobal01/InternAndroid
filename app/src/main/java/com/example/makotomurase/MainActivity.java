package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;
import android.content.Context;
import android.os.Vibrator;
import android.graphics.Color;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Locale locale = Locale.getDefault();
    String language = locale.getLanguage();

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

        if (locale.equals(Locale.JAPAN)||language.equals(Locale.JAPANESE)){
            TextView txtView = (TextView) findViewById(R.id.answer);
            txtView.setText("値2");
        }else{
            TextView txtView = (TextView) findViewById(R.id.answer);
            txtView.setText("Value2");
        }

        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setBackgroundColor(Color.WHITE);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button1) {
            setAnswerValue();
            checkResult(true);
            ((Vibrator)getSystemService(Context.VIBRATOR_SERVICE)).vibrate(100);
        } else if (id == R.id.button2) {
            setAnswerValue();
            checkResult(false);
            ((Vibrator)getSystemService(Context.VIBRATOR_SERVICE)).vibrate(100);
        } else if (id == R.id.button3) {
            setQuestionValue();
            clearAnswerValue();
            clearScoreValue();
            ((Vibrator)getSystemService(Context.VIBRATOR_SERVICE)).vibrate(100);
        }
    }

    private void clearAnswerValue() {
        if (locale.equals(Locale.JAPAN)||language.equals(Locale.JAPANESE)){
            TextView txtView = (TextView) findViewById(R.id.answer);
            txtView.setText("値2");
        }else{
            TextView txtView = (TextView) findViewById(R.id.answer);
            txtView.setText("Value2");
        }
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setBackgroundColor(Color.WHITE);
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
                score = 2;
                TextView txtView = (TextView) findViewById(R.id.answer);
                txtView.setBackgroundColor(Color.parseColor("#00bfff"));
                if (locale.equals(Locale.JAPAN)||language.equals(Locale.JAPANESE)) {
                    result = "勝ち";
                }else{
                    result = "WIN";
                }
            } else if (question > answer) {
                score = -1;
                TextView txtView = (TextView) findViewById(R.id.answer);
                txtView.setBackgroundColor(Color.parseColor("#ff6347"));
                if (locale.equals(Locale.JAPAN)||language.equals(Locale.JAPANESE)) {
                    result = "負け";
                }else{
                    result = "LOSE";
                }
            } else {
                score = 1;
                TextView txtView = (TextView) findViewById(R.id.answer);
                txtView.setBackgroundColor(Color.parseColor("#ffd700"));
                if (locale.equals(Locale.JAPAN)||language.equals(Locale.JAPANESE)) {
                    result = "引き分け";
                }else{
                    result = "DRAW";
                }
            }
        } else {
            if (question > answer) {
                score = 2;
                TextView txtView = (TextView) findViewById(R.id.answer);
                txtView.setBackgroundColor(Color.parseColor("#00bfff"));
                if (locale.equals(Locale.JAPAN)||language.equals(Locale.JAPANESE)) {
                    result = "勝ち";
                }else{
                    result = "WIN";
                }
            } else if (question < answer) {
                score = -1;
                TextView txtView = (TextView) findViewById(R.id.answer);
                txtView.setBackgroundColor(Color.parseColor("#ff6347"));
                if (locale.equals(Locale.JAPAN)||language.equals(Locale.JAPANESE)) {
                    result = "負け";
                }else{
                    result = "LOSE";
                }

            } else {
                score = 1;
                TextView txtView = (TextView) findViewById(R.id.answer);
                txtView.setBackgroundColor(Color.parseColor("#ffd700"));
                if (locale.equals(Locale.JAPAN)||language.equals(Locale.JAPANESE)) {
                    result = "引き分け";
                }else{
                    result = "DRAW";
                }

            }
        }

        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        if (locale.equals(Locale.JAPAN)||language.equals(Locale.JAPANESE)) {
            txtResult.setText("結果：" + question + ":" + answer + "(" + result + ")");
        }else {
            txtResult.setText("Result：" + question + ":" + answer + "(" + result + ")");
        }

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
}

