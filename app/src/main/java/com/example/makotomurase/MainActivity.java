package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.style.BackgroundColorSpan;
import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // プリファレンス定義
    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

    private AnimatorSet set = null;

    int score = 0;

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

        //プリファレンスの呼び出しと生成
        pref = getSharedPreferences("AndroidTeamDevelop", MODE_PRIVATE);
        prefEditor = pref.edit();

        // 起動時に関数を呼び出す
        setQuestionValue();


    }

    @Override
    public void onClick(View view) {
        TextView txtViewQuestion = findViewById(R.id.question);
        TextView txtViewAnswer = findViewById(R.id.answer);
        Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vib.vibrate(1000);

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
            txtViewQuestion.setBackgroundColor(Color.parseColor("#FAFAFA"));
            txtViewAnswer.setBackgroundColor(Color.parseColor("#E0E0E0"));
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

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = "WIN";
                score = 2;
                TextView textView = findViewById(R.id.answer);
                set = (AnimatorSet) AnimatorInflater.loadAnimator(MainActivity.this,
                        R.animator.blink_animation);
                set.setTarget(textView);
                set.start();
                stopBlink();
            } else if (question > answer) {
                result = "LOSE";
                score = -1;
                TextView textView = findViewById(R.id.question);
                set = (AnimatorSet) AnimatorInflater.loadAnimator(MainActivity.this,
                        R.animator.blink_animation);
                set.setTarget(textView);
                set.start();
                stopBlink();
            } else {
                result = "DRAW";
                score = 1;
                TextView textView1 = findViewById(R.id.question);
                TextView textView2 = findViewById(R.id.answer);
                set = (AnimatorSet) AnimatorInflater.loadAnimator(MainActivity.this,
                        R.animator.blink_animation);
                set.setTarget(textView1);
                set.setTarget(textView2);
                set.start();
                stopBlink();
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                TextView textView = findViewById(R.id.question);
                set = (AnimatorSet) AnimatorInflater.loadAnimator(MainActivity.this,
                        R.animator.blink_animation);
                set.setTarget(textView);
                set.start();
                stopBlink();
            } else if (question < answer) {
                result = "LOSE";
                score = -1;
                TextView textView = findViewById(R.id.answer);
                set = (AnimatorSet) AnimatorInflater.loadAnimator(MainActivity.this,
                        R.animator.blink_animation);
                set.setTarget(textView);
                set.start();
                stopBlink();
            } else {
                result = "DRAW";
                score = 1;
                TextView textView1 = findViewById(R.id.question);
                TextView textView2 = findViewById(R.id.answer);
                set = (AnimatorSet) AnimatorInflater.loadAnimator(MainActivity.this,
                        R.animator.blink_animation);
                set.setTarget(textView1);
                set.setTarget(textView2);
                set.start();
                stopBlink();
            }
        }

        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText("結果：" + question + ":" + answer + "(" + result + ")");

        if(result=="WIN"){
            txtViewQuestion.setBackgroundColor(Color.parseColor("#4FC3F7"));
            txtViewAnswer.setBackgroundColor(Color.parseColor("#FF8A65"));
        }else if(result=="LOSE"){
            txtViewQuestion.setBackgroundColor(Color.parseColor("#FF8A65"));
            txtViewAnswer.setBackgroundColor(Color.parseColor("#4FC3F7"));
        }else if(result=="DRAW"){
            txtViewQuestion.setBackgroundColor(Color.parseColor("#BDBDBD"));
            txtViewAnswer.setBackgroundColor(Color.parseColor("#BDBDBD"));
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

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();

        TextView textView = (TextView)findViewById(R.id.text_score);
        int readText = pref.getInt("score", 0);

        textView.setText(String.valueOf(readText));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();

        //画面上に文字列を保存するため、テキストビューを取得
        TextView textView = (TextView) findViewById(R.id.text_score);

        prefEditor.putInt("score", Integer.parseInt(textView.getText.toString()));
        prefEditor.commit();
    }

    private void stopBlink() {

        new CountDownTimer(3000, 1000) {

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                if (set != null) {
                    if (set.isRunning()) {
                        set.pause();
                    }
                }
            }
        }.start();
    }

}




