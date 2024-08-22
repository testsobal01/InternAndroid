package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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

        // 起動時に関数を呼び出す
        setQuestionValue();

        //プリファレンスの生成
        pref = getSharedPreferences("AndroidSeminor", MODE_PRIVATE);
        prefEditor = pref.edit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
        TextView text_score = (TextView) findViewById(R.id.text_score);
        prefEditor.putString("score_input", text_score.getText().toString());
        prefEditor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();

        TextView text_score = (TextView) findViewById(R.id.text_score);

        String read_score = pref.getString("score_input", "0");
        text_score.setText(read_score);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button1) {
            Vibrator vid = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vid.vibrate(5000);
        }
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
                backgroundchangeWin();
            } else if (question > answer) {
                result = "LOSE";
                score = -1;
                backgroundchangeLose();
            } else {
                result = "DRAW";
                score = 1;
                backgroundchangeDraw();
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                backgroundchangeWin();
            } else if (question < answer) {
                result = "LOSE";
                score = -1;
                backgroundchangeLose();
            } else {
                result = "DRAW";
                score = 1;
                backgroundchangeDraw();
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

    private void backgroundchangeWin(){
        TextView txtquestion = (TextView) findViewById(R.id.question);
        TextView txtanswer = (TextView) findViewById(R.id.answer);
        txtquestion.setBackgroundColor(Color.rgb(255, 140, 0));
        txtanswer.setBackgroundColor(Color.rgb(255, 164, 0));
    }
    private void backgroundchangeLose(){
        TextView txtquestion = (TextView) findViewById(R.id.question);
        TextView txtanswer = (TextView) findViewById(R.id.answer);
        txtquestion.setBackgroundColor(Color.rgb(0, 133, 201));
        txtanswer.setBackgroundColor(Color.rgb(102, 153, 204));
    }
    private void backgroundchangeDraw(){
        TextView txtquestion = (TextView) findViewById(R.id.question);
        TextView txtanswer = (TextView) findViewById(R.id.answer);
        txtquestion.setBackgroundColor(Color.rgb(140, 140, 140));
        txtanswer.setBackgroundColor(Color.rgb(140, 140, 140));
    }
}

