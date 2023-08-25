package com.example.makotomurase;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;


import android.graphics.Color;

import android.content.SharedPreferences;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // プリファレンスクラスを定義(b-2)
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

        // プリファレンスのインスタンスを生成(b-2)
        pref = getSharedPreferences("TeamB", MODE_PRIVATE);
        prefEditor = pref.edit();

        // 保存されているスコアを読み込んで反映(b-2)
        int score = pref.getInt("score", 0);
        setScore(score);

        // 起動時に関数を呼び出す
        setQuestionValue();
    }

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

    // (b-6) 右側の背景色を変更する関数
    private void setAnswerColor(String result) {
        TextView txtView = findViewById(R.id.answer);

        if(result == "WIN"){ // 勝利
            txtView.setBackgroundColor(Color.RED);
        } else if (result == "LOSE") { // 敗北
            txtView.setBackgroundColor(Color.BLUE);
        } else if (result == "DRAW") { // 引き分け
            txtView.setBackgroundColor(Color.GREEN);
        } else if (result == "RESET") {
            txtView.setBackgroundColor(Color.YELLOW);
        } else { // 例外
            Toast.makeText(this, "不正なresult：" + result, Toast.LENGTH_LONG).show();
        }
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

        setAnswerColor(result);

        Locale locale = Locale.getDefault();
        String lang = locale.getLanguage();
        if (lang.equals("ja")) {
            // 日本語環境
            txtResult.setText("結果：" + question + ":" + answer + "(" + result + ")");
        } else {
            // その他の言語環境、通常英語が選択される
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
                setAnswerColor("RESET");
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
    protected void onPause() {
        super.onPause();

        // スコアを取得
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        int score = Integer.parseInt(txtScore.getText().toString());

        // スコアを保存
        prefEditor.putInt("score", score);
        prefEditor.commit();
    }
}

