package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.style.BackgroundColorSpan;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static boolean first = false;

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


        ImageView imageView = findViewById(R.id.footer);
        imageView.setImageResource(R.drawable.footer);
      
        pref = getSharedPreferences("MakotoMurase",MODE_PRIVATE);
        prefEditor = pref.edit();
      
        if (!first) {
            first = true;
            Intent intent = new Intent(this, TopActivity.class);
            startActivity(intent);
        }

      
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
        Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vib.vibrate(2000);
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
                winAnimation(txtViewQuestion);
                winAnimation(txtViewAnswer);
            } else if (question > answer) {
                result = "LOSE";
                score = -1;
                loseAnimation(txtViewQuestion);
                loseAnimation(txtViewAnswer);
            } else {
                result = "DRAW";
                score = 1;
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                winAnimation(txtViewQuestion);
                winAnimation(txtViewAnswer);
            } else if (question < answer) {
                result = "LOSE";
                score = -1;
                loseAnimation(txtViewQuestion);
                loseAnimation(txtViewAnswer);
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

        if (result == "WIN"){
            TextView col = (TextView)findViewById(R.id.question);
            col.setBackgroundColor(Color.GREEN);

            TextView or = (TextView)findViewById(R.id.answer);
            or.setBackgroundColor(Color.GREEN);
        }

        if (result == "LOSE"){
            TextView ba = (TextView)findViewById(R.id.question);
            ba.setBackgroundColor(Color.RED);

            TextView ck = (TextView)findViewById(R.id.answer);
            ck.setBackgroundColor(Color.RED);
        }
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

                TextView gro = (TextView)findViewById(R.id.question);
                gro.setBackgroundColor(Color.parseColor("#ff00ff"));

                TextView und = (TextView)findViewById(R.id.answer);
                und.setBackgroundColor(Color.parseColor("#ffff00"));
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

    private void winAnimation(TextView view) {
        float scale = 1.0f;
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                scale, scale-0.1f, scale, scale-0.1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        scaleAnimation.setDuration(150);
        scaleAnimation.setRepeatCount(5);
        view.startAnimation(scaleAnimation);
    }

    private void loseAnimation(TextView view) {
        float scale = 1.0f;
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                        scale, scale-0.3f, scale, scale-0.3f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f
                );
        scaleAnimation.setDuration(1000);
        scaleAnimation.setRepeatCount(0);
        view.startAnimation(scaleAnimation);
    }

    @Override
    protected void onPause() {
        super.onPause();
        TextView textView = (TextView)findViewById(R.id.text_score);
        prefEditor.putString("KEY",textView.getText().toString());
        prefEditor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView textView = (TextView)findViewById(R.id.text_score);
        String readText = pref.getString("KEY","保存されていません");
        textView.setText(readText);
    }
}
