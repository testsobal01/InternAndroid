package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioAttributes;
import android.media.SoundPool;

import android.graphics.Color;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //プリファレンスの作成
    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

    //sound
    private SoundPlayer soundPlayer;

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

        soundPlayer = new SoundPlayer(this);

        // 起動時に関数を呼び出す
        setQuestionValue();

        //プリファレンスの保存先
        pref = getSharedPreferences("SaveScore", MODE_PRIVATE);
        prefEditor = pref.edit();

        //プリファレンスの読み込み
        TextView textView = (TextView) findViewById(R.id.text_score);
        String readText = pref.getString("SaveScore","保存されていません");
        textView.setText(readText);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button1) {
            setAnswerValue();
            checkResult(true);

            //バイブレーション機能
            Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
            vib.vibrate(1000);

        } else if (id == R.id.button2) {
            setAnswerValue();
            checkResult(false);

            //バイブレーション機能
            Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
            vib.vibrate(1000);

        } else if (id == R.id.button3) {
            setQuestionValue();
            clearAnswerValue();
            clearScoreValue();

            //バイブレーション機能
            Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
            vib.vibrate(1000);

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

                //勝った時の効果音
                soundPlayer.playWinSound();

            } else if (question > answer) {
                result = "LOSE";
                score = -1;

                //負けた時の効果音
                soundPlayer.playLoseSound();

            } else {
                result = "DRAW";
                score = 1;

                //ドローの効果音
                soundPlayer.playDrawSound();

            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;

                //勝った時の効果音
                soundPlayer.playWinSound();

            } else if (question < answer) {
                result = "LOSE";
                score = -1;

                //負けた時の効果音
                soundPlayer.playLoseSound();

            } else {
                result = "DRAW";
                score = 1;

                //ドローの効果音
                soundPlayer.playDrawSound();
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

        //プリファレンスの保存
        TextView textView = (TextView) findViewById(R.id.text_score);

        prefEditor.putString("SaveScore", textView.getText().toString());
        prefEditor.commit();
    }

    private void clearScoreValue() {
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        txtScore.setText("0");
    }
}

