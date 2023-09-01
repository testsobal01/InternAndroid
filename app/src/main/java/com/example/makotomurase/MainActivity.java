package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

    // Sound
    private SoundPlayer soundPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //効果音クラスのインスタンス
        soundPlayer = new SoundPlayer(this);

        Button btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(this);

        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(this);

        Button nextbt =  (Button) findViewById(R.id.next_button);
        nextbt.setOnClickListener(this);
        // 起動時に関数を呼び出す
        setQuestionValue();

        //プリファレンス設定
        pref = getSharedPreferences("save",MODE_PRIVATE);
        prefEditor = pref.edit();
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

            TextView textView3=findViewById(R.id.answer);
            textView3.setBackgroundColor(Color.parseColor("#ffff00"));
            textView3.setTextColor(Color.parseColor("#FF000000"));

        } else if (id == R.id.next_button) {
            Intent intent = new Intent(this, NextActivity.class);
            startActivity(intent);
        }
    }

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        String word = getString(R.string.answer);
        txtView.setText(word);

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
                soundPlayer.playMaruSound();
            } else if (question > answer) {
                result = "LOSE";
                score = -1;
                soundPlayer.playBatuSound();
            } else {
                result = "DRAW";
                score = 1;
                soundPlayer.playDrawSound();
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                soundPlayer.playMaruSound();
            } else if (question < answer) {
                result = "LOSE";
                score = -1;
                soundPlayer.playBatuSound();
            } else {
                result = "DRAW";
                score = 1;
                soundPlayer.playDrawSound();
            }

        }
        if (score==2){
            TextView textview1 = findViewById(R.id.answer);
            textview1.setBackgroundColor(Color.parseColor("#FF9800"));
            textview1.setTextColor(Color.parseColor("#FF000000"));

        } else if (score==-1) {
            TextView textView2 =findViewById(R.id.answer);
            textView2.setBackgroundColor(Color.parseColor("#0000FF"));
            textView2.setTextColor(Color.parseColor("#FFFFFFFF"));

        }else {
            TextView textView3=findViewById(R.id.answer);
            textView3.setBackgroundColor(Color.parseColor("#ffff00"));
            textView3.setTextColor(Color.parseColor("#FF000000"));
        }

        switch (score) {
            case 2:
                Vibrator vib2 = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vib2.vibrate(1000);
                break;
            case 1:
                Vibrator vib1 = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vib1.vibrate(500);
                break;
        }

        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText("：" + question + ":" + answer + "(" + result + ")");

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
    protected void onPause() {
        super.onPause();
        // 画面上の文字を取得するためテキストビューを取得
        TextView textView = (TextView)findViewById(R.id.text_score);
        //saveというキーに文字列を保存
        prefEditor.putString("save",textView.getText().toString());
        prefEditor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //textViewの取得
        TextView textView = (TextView)findViewById(R.id.text_score);
        //
        String readText = pref.getString("save","0");
        textView.setText(readText);
    }
}

