package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

<<<<<<< HEAD
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
=======
import android.content.Intent;
import android.os.Bundle;
>>>>>>> 9029cdb (スタート画面を作りました)
import android.view.View;
import android.widget.Button;

<<<<<<< HEAD
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;
=======
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
>>>>>>> 9029cdb (スタート画面を作りました)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titlescreen);

        Button jun = findViewById(R.id.superbutton1);
        jun.setOnClickListener(this);

<<<<<<< HEAD
        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(this);

        pref = getSharedPreferences("MakotoMurase", MODE_PRIVATE);
        prefEditor = pref.edit();

        // 起動時に関数を呼び出す
        setQuestionValue();
=======
>>>>>>> 9029cdb (スタート画面を作りました)
    }

    //インテントの作成
    public void onClick(View view) {
        int id = view.getId();
<<<<<<< HEAD
        View background = findViewById(R.id.background);
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
            background.setBackgroundColor(Color.WHITE);
=======
        if (id == R.id.superbutton1) {
            Intent intent = new Intent(this, titleActivity.class);
            intent.putExtra("JUN", "MainActivityからの呼び出し");
            startActivity(intent);
>>>>>>> 9029cdb (スタート画面を作りました)

        }
        Vibrator vib=(Vibrator) getSystemService(VIBRATOR_SERVICE);
        vib.vibrate(1000);
    }
<<<<<<< HEAD

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
        View background = findViewById(R.id.background);

        // 結果を示す文字列を入れる変数を用意
        String result;
        int score;
        int wincolor = Color.GREEN;
        int losecolor = Color.CYAN;
        int drawcolor = Color.WHITE;

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = "WIN";
                score = 2;
                background.setBackgroundColor(wincolor);
            } else if (question > answer) {
                result = "LOSE";
                score = -1;
                background.setBackgroundColor(losecolor);
            } else {
                result = "DRAW";
                score = 1;
                background.setBackgroundColor(drawcolor);
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                background.setBackgroundColor(wincolor);
            } else if (question < answer) {
                result = "LOSE";
                score = -1;
                background.setBackgroundColor(losecolor);
            } else {
                result = "DRAW";
                score = 1;
                background.setBackgroundColor(drawcolor);
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

    @Override
    protected void onPause() {
        super.onPause();
        TextView textView = (TextView) findViewById(R.id.text_score);
        prefEditor.putString("pref_score", textView.getText().toString());
        prefEditor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView textView = (TextView) findViewById(R.id.text_score);
        String readText = pref.getString("pref_score", "0");
        textView.setText(readText);
    }
}

=======
}
>>>>>>> 9029cdb (スタート画面を作りました)
