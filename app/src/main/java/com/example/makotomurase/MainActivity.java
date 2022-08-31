package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
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

        pref = getSharedPreferences("TeamF", MODE_PRIVATE);
        prefEditor = pref.edit();

        TextView txtScore = (TextView) findViewById(R.id.text_score);
        int score = pref.getInt("game_score", 0);
        txtScore.setText(Integer.toString(score));

        // 起動時に関数を呼び出す
        setQuestionValue();

    }

    @Override
    public void onClick(View view) {
        TextView txtViewQuestion = (TextView) findViewById(R.id.question);
        TextView txtViewAnswer = (TextView) findViewById(R.id.answer);
        int id = view.getId();
        switch (id) {
            case R.id.button1:
                setAnswerValue();
                checkResult(true);
                break;
            case R.id.button2:
                setAnswerValue();
                checkResult(false);
                break;
            case R.id.button3:
                // 背景色(初期値)
                txtViewAnswer.setBackgroundColor(Color.rgb(255,255,0));
                // 背景色(初期値)
                txtViewQuestion.setBackgroundColor(Color.rgb(255,0,255));
                setQuestionValue();
                clearAnswerValue();
                break;

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // スコアを保存
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        prefEditor.putInt("game_score", Integer.parseInt(txtScore.getText().toString()));
        prefEditor.commit();

    }

    private void clearAnswerValue() {
        String word = getString(R.string.atai);
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText(word);
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
        String word_win = getString(R.string.win);
        String word_lose = getString(R.string.lose);
        String word_draw = getString(R.string.draw);
        String word_result = getString(R.string.result);

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = word_win;
                score = 2;
                // 背景色(勝ち,赤)
                txtViewAnswer.setBackgroundColor(Color.RED);
                // 背景色(負け,灰色)
                txtViewQuestion.setBackgroundColor(Color.GRAY);
            } else if (question > answer) {
                result = word_lose;
                score = -1;
                // 背景色(勝ち,赤)
                txtViewQuestion.setBackgroundColor(Color.RED);
                // 背景色(負け,灰色)
                txtViewAnswer.setBackgroundColor(Color.GRAY);
                Vibrator vib= (Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(500);
            } else {
                result = word_draw;
                score = 1;
                // 背景色(引き分け,緑)
                txtViewQuestion.setBackgroundColor(Color.GREEN);
                // 背景色(引き分け,緑)
                txtViewAnswer.setBackgroundColor(Color.GREEN);
                Vibrator vib= (Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(200);
            }
        } else {
            if (question > answer) {
                result = word_win;
                score = 2;
                // 背景色(勝ち,赤)
                txtViewAnswer.setBackgroundColor(Color.RED);
                // 背景色(負け,灰色)
                txtViewQuestion.setBackgroundColor(Color.GRAY);
            } else if (question < answer) {
                result = word_lose;
                score = -1;
                // 背景色(勝ち,赤)
                txtViewQuestion.setBackgroundColor(Color.RED);
                // 背景色(負け,灰色)
                txtViewAnswer.setBackgroundColor(Color.GRAY);
                Vibrator vib= (Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(500);
            } else {
                result = word_draw;
                score = 1;
                // 背景色(引き分け,緑)
                txtViewQuestion.setBackgroundColor(Color.GREEN);
                // 背景色(引き分け,緑)
                txtViewAnswer.setBackgroundColor(Color.GREEN);
                Vibrator vib= (Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(200);
            }
        }


        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText(word_result + question + ":" + answer + "(" + result + ")");

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

}