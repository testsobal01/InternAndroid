package com.example.makotomurase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
    private Random r;

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

        //プリファレンス
        pref = getSharedPreferences("InternAndroid", MODE_PRIVATE);
        prefEditor = pref.edit();

    }

    @Override

    public void onClick(View view) {
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
                setQuestionValue();
                clearAnswerValue();
                break;

        }

        switch (view.getId()) {
            case R.id.button1:
            case R.id.button2:
            case R.id.button3:
                Vibrator vib =(Vibrator) getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(1000);
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
        // 結果を示す文字列を入れる変数を用意
        String result;
        int score = 0;

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = "WIN";
                score = 2;
                ChangeColor1();
            } else if (question > answer) {
                result = "LOSE";
                score = -1;
                ChangeColor2();
            } else {
                result = "DRAW";
                score = 1;
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                ChangeColor1();
            } else if (question < answer) {
                result = "LOSE";
                score = -1;
                ChangeColor2();
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

    protected void onPause() {
        super.onPause();

        TextView textView = (TextView) findViewById(R.id.text_score);
        prefEditor.putString("temp_score", textView.getText().toString());
        prefEditor.commit();

    }

    protected void onResume() {
        super.onResume();

        TextView textView = (TextView) findViewById(R.id.text_score);

        //スコアが保存されていない場合
        String readText = pref.getString("temp_score", "0");
        textView.setText(readText);
    }

    //勝敗の色を変える
    public void ChangeColor1() {
        //  win の側を赤に　loseの方を　青に

        TextView textView= (TextView) findViewById(R.id.answer);
        textView.setBackgroundColor(Color.RED);

        TextView txtView= (TextView) findViewById(R.id.question);
        txtView.setBackgroundColor(Color.BLUE);
    }

    public void ChangeColor2(){
        //  win の側を赤に　loseの方を　青に

        TextView textView= (TextView) findViewById(R.id.question);
        textView.setBackgroundColor(Color.RED);

        TextView txtView= (TextView) findViewById(R.id.answer);
        txtView.setBackgroundColor(Color.BLUE);
    }

}