package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;
    Button btn1;
    Button btn2;
    CountDownTimer counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = findViewById(R.id.button_high);
        btn1.setOnClickListener(this);

        btn2 = findViewById(R.id.button_low);
        btn2.setOnClickListener(this);

        Button btn3 = findViewById(R.id.button_restart);
        btn3.setOnClickListener(this);

        pref = getSharedPreferences("AndroidSeminar", MODE_PRIVATE);

        counter = new CountDownTimer(0, 0) {

            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
            }
        }.start();

        // 起動時に関数を呼び出す
        setQuestionValue();

    }

    @Override
    protected void onResume() {
        super.onResume();

        TextView txtScore = findViewById(R.id.text_score);
        String read_txt = pref.getString("main_import", "0");
        txtScore.setText(read_txt);
    }

    @Override
    protected void onPause() {
        super.onPause();

        TextView txtScore = findViewById(R.id.text_score);
        prefEditor = pref.edit();
        prefEditor.putString("main_import", txtScore.getText().toString());
        prefEditor.apply();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        buttonEnabled(false);
        if (id == R.id.button_high) {
            setAnswerValue();
            vibration();
            checkResult(true);
        } else if (id == R.id.button_low) {
            setAnswerValue();
            checkResult(false);
        } else if (id == R.id.button_restart) {
            counter.cancel();
            setQuestionValue();
            clearAnswerValue();
        }
    }

    private void clearAnswerValue() {
        TextView txtView = findViewById(R.id.answer);
        txtView.setText(getString(R.string.text_hatena));
        TextView txtScore = findViewById(R.id.text_score);

        prefEditor = pref.edit();
        prefEditor.putString("main_import", "0");
        prefEditor.apply();
        String read_txt = pref.getString("main_import", "0");
        txtScore.setText(read_txt);
    }

    private void setQuestionValue() {
        Random r = new Random();
        // 0から10の範囲で乱数を生成（+1する必要がある）
        int questionValue = r.nextInt(10 + 1);
        TextView txtView = findViewById(R.id.question);
        txtView.setText(String.valueOf(questionValue));
        buttonEnabled(true);
    }

    private void setAnswerValue() {
        Random r = new Random();
        int answerValue = r.nextInt(10 + 1);
        TextView txtView = findViewById(R.id.answer);
        txtView.setText(String.valueOf(answerValue));
    }

    private void checkResult(boolean isHigh) {
        TextView txtViewQuestion = findViewById(R.id.question);
        TextView txtViewAnswer = findViewById(R.id.answer);
        int question = Integer.parseInt(txtViewQuestion.getText().toString());
        int answer = Integer.parseInt(txtViewAnswer.getText().toString());
        TextView txtResult = findViewById(R.id.text_result);
        // 結果を示す文字列を入れる変数を用意
        String result;
        int score;

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = "WIN";
                score = 2;
                alphaAnimationTest(txtViewAnswer);
            } else if (question > answer) {
                result = "LOSE";
                score = -1;
                alphaAnimationTest(txtViewQuestion);
            } else {
                result = "DRAW";
                score = 1;
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                alphaAnimationTest(txtViewAnswer);
            } else if (question < answer) {
                result = "LOSE";
                score = -1;
                alphaAnimationTest(txtViewQuestion);
            } else {
                result = "DRAW";
                score = 1;
            }
        }

        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText(getString(R.string.result_text, question, answer, result));
        if (result.equals("WIN")) {
            txtViewAnswer.setBackgroundColor(Color.BLUE);
            txtViewQuestion.setBackgroundColor(Color.RED);
        } else if (result.equals("LOSE")) {
            txtViewAnswer.setBackgroundColor(Color.RED);
            txtViewQuestion.setBackgroundColor(Color.BLUE);
        } else {
            txtViewAnswer.setBackgroundColor(Color.YELLOW);
            txtViewQuestion.setBackgroundColor(Color.YELLOW);
        }

        // 続けて遊べるように値を更新
        setNextQuestion();

        // スコアを表示
        setScore(score);
    }

    private void setNextQuestion() {
        // 第１引数がカウントダウン時間、第２引数は途中経過を受け取る間隔
        // 単位はミリ秒（1秒＝1000ミリ秒）
        counter = new CountDownTimer(3000, 1000) {

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
        TextView txtScore = findViewById(R.id.text_score);
        int newScore = Integer.parseInt(txtScore.getText().toString()) + score;
        txtScore.setText(String.valueOf(newScore));
    }

    public void vibration() {
        Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vib.vibrate(500);
    }

    private void buttonEnabled(boolean status) {
        btn1.setEnabled(status);
        btn2.setEnabled(status);
    }

    // 透過アニメーションの例
    void alphaAnimationTest(View v) {
        AlphaAnimation alpha = new AlphaAnimation(
                0.0f,  // 開始時の透明度（0は完全に透過）
                1.0f); // 終了時の透明度（1は全く透過しない）

        // 3秒かけてアニメーションする
        alpha.setDuration(3000);

        // アニメーション終了時の表示状態を維持する
        alpha.setFillEnabled(true);
        alpha.setFillAfter(true);

        // アニメーションを開始
        v.startAnimation(alpha);
    }

}

