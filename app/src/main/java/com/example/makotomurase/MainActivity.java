package com.example.makotomurase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;


    @Override
    protected void onResume() {
        super.onResume();
        TextView textView = (TextView) findViewById(R.id.text_score);

        String readText = pref.getString("main_input", "保存されていません");
        textView.setText(readText);
    }

    @Override
    protected void onPause() {
        super.onPause();
        TextView textView = (TextView) findViewById(R.id.text_score);

        prefEditor.putString("main_input", textView.getText().toString());
        prefEditor.commit();
    }

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

        // 起動時に関数を呼び出す
        setQuestionValue();

        pref = getSharedPreferences("AndroidSeminor", MODE_PRIVATE);
        prefEditor = pref.edit();

    }



    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.button1:
                Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(300);
                setAnswerValue();
                checkResult(true);
                break;
            case R.id.button2:
                Vibrator vib2 = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vib2.vibrate(300);
                setAnswerValue();
                checkResult(false);
                break;
            case R.id.button3:
                Vibrator vib3 = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vib3.vibrate(300);
                setQuestionValue();
                clearAnswerValue();
                break;

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
        LinearLayout linearlayout = (LinearLayout) findViewById(R.id.linearlayout);
        // 結果を示す文字列を入れる変数を用意
        String result;
        int score = 0;

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = "WIN";
                linearlayout.setBackgroundColor(Color.rgb(234, 145, 152));
                score = 2;
                RotateAnimation rotate = new RotateAnimation(0, 360, txtViewAnswer.getWidth()/2, txtViewAnswer.getHeight()/2); // imgの中心を軸に、0度から360度にかけて回転
                rotate.setDuration(3000); // 3000msかけてアニメーションする
                txtViewAnswer.startAnimation(rotate); // アニメーション適用
            } else if (question > answer) {
                result = "LOSE";
                linearlayout.setBackgroundColor(Color.rgb(143, 168, 232));
                score = -1;
            } else {
                result = "DRAW";
                linearlayout.setBackgroundColor(Color.rgb(142, 229, 152));
                score = 1;
            }
        } else {
            if (question > answer) {
                result = "WIN";
                linearlayout.setBackgroundColor(Color.rgb(234, 145, 152));
                score = 2;

                RotateAnimation rotate = new RotateAnimation(0, 360, txtViewQuestion.getWidth()/2, txtViewQuestion.getHeight()/2); // imgの中心を軸に、0度から360度にかけて回転
                rotate.setDuration(3000); // 3000msかけてアニメーションする
                txtViewQuestion.startAnimation(rotate); // アニメーション適用
            } else if (question < answer) {
                result = "LOSE";
                linearlayout.setBackgroundColor(Color.rgb(143, 168, 232));
                score = -1;
            } else {
                result = "DRAW";
                linearlayout.setBackgroundColor(Color.rgb(142, 229, 152));
                score = 1;
            }
        }


        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        //String message3 = getString(R.string.message3);
        String s = getResources().getString(R.string.message3);
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText(s+ question + ":" + answer + "(" + result + ")");

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
        TextView textView = (TextView) findViewById(R.id.text_score);
        String text = textView.getText().toString();

        if (text.equals("保存されていません")) {
            textView.setText(Integer.toString(score));
        } else {
            int newScore = Integer.parseInt(textView.getText().toString()) + score;
            textView.setText(Integer.toString(newScore));
        }
    }

}

