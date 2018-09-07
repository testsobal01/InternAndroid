package com.example.seminor.murase.makoto.murasemakoto;


import android.content.SharedPreferences;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

    int newScore = 0;
    int question = 0;
    int answer = 0;
    String result;
    TextView txtViewQuestion, txtViewAnswer, txtResult, txtScore;

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

        pref = getSharedPreferences("AndroidSeminor",MODE_PRIVATE);
        prefEditor = pref.edit();

        // 起動時に関数を呼び出す
        setQuestionValue();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();

        prefEditor.putInt("main_input_score", newScore);
        prefEditor.putInt("main_input_question", question);
        prefEditor.putInt("main_input_answer", answer);
        prefEditor.putString("main_input_result", result);
        prefEditor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();

        int roadScore = pref.getInt("main_input_score", 0);
        int roadQuestion = pref.getInt("main_input_question",0);
        int roadAnswer = pref.getInt("main_input_answer",0);
        String roadResult = pref.getString("main_input_result", "未保存");

        txtScore = (TextView)findViewById(R.id.text_score);
        txtViewQuestion = (TextView)findViewById(R.id.question);
        txtViewAnswer = (TextView)findViewById(R.id.answer);
        txtResult = (TextView)findViewById(R.id.text_result);

        txtScore.setText(Integer.toString(roadScore));
        txtViewQuestion.setText(Integer.toString(roadQuestion));
        txtViewAnswer.setText(Integer.toString(roadAnswer));
        txtResult.setText(roadResult);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.button1:
                setAnswerValue();
                checkResult(true);

                Vibrator vib=(Vibrator)getSystemService(VIBRATOR_SERVICE);
                long[] pattern={1000, 1000, 1000, 1000, 1000, 5000};
                vib.vibrate(pattern,0);


                break;
            case R.id.button2:
                setAnswerValue();
                checkResult(false);
                ((Vibrator) getSystemService(VIBRATOR_SERVICE)).cancel();
                ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).cancel();
                break;
            case R.id.button3:
                setQuestionValue();
                clearAnswerValue();
                clearScoreValue();
                ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(1000);
                break;
        }
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
        txtViewQuestion = (TextView) findViewById(R.id.question);
        txtViewAnswer = (TextView) findViewById(R.id.answer);
        question = Integer.parseInt(txtViewQuestion.getText().toString());
        answer = Integer.parseInt(txtViewAnswer.getText().toString());
        txtResult = (TextView) findViewById(R.id.text_result);
        // 結果を示す文字列を入れる変数を用意
        int score = 0;

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = "WIN";
                score = 2;
            } else if (question > answer) {
                result = "LOOSE";
                score = -1;
            } else {
                result = "DRAW";
                score = 1;
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
            } else if (question < answer) {
                result = "LOOSE";
                score = -1;
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

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText("?");
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
        txtScore = (TextView) findViewById(R.id.text_score);
        newScore = Integer.parseInt(txtScore.getText().toString()) + score;
        txtScore.setText(Integer.toString(newScore));
    }

    private void clearScoreValue() {
        txtScore = (TextView)findViewById(R.id.text_score);
        txtScore.setText("0");
    }
}
