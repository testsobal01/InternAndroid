package com.example.seminor.murase.makoto.murasemakoto;

<<<<<<< HEAD
import android.content.SharedPreferences;
=======
import android.content.Intent;
>>>>>>> add gamenseni
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

    //テキストビューを取得
    TextView textView1 ;
    TextView textView2 ;
    TextView textView3 ;

    String readText1 ;
    String readText2 ;
    String readText3 ;

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

        //保存先はAndroidSeminor
        pref = getSharedPreferences("AndroidSeminor", MODE_PRIVATE);
        prefEditor = pref.edit();

        textView1 = (TextView)findViewById(R.id.question);
        textView2 = (TextView)findViewById(R.id.answer);
        textView3 = (TextView)findViewById(R.id.text_score);



        readText1 = pref.getString("main_input", "保存されていません");
        textView1.setText(readText1);
        readText2 = pref.getString("score_input", "保存されていません");
        textView2.setText(readText2);
        readText3 = pref.getString("answer_input", "保存されていません");
        textView3.setText(readText3);



        // 起動時に関数を呼び出す
        setQuestionValue();
    }

    @Override
    protected void onPause() {
        super.onPause();


        textView1 = (TextView)findViewById(R.id.question);
        textView2 = (TextView)findViewById(R.id.answer);
        textView3 = (TextView)findViewById(R.id.text_score);

        //main_inputに保存
        prefEditor.putString("main_input",textView1.getText().toString());
        prefEditor.commit();

        prefEditor.putString("score_input",textView2.getText().toString());
        prefEditor.commit();

        prefEditor.putString("answer_input",textView3.getText().toString());
        prefEditor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();



    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.button1:
                setAnswerValue();
                checkResult(true);

                Vibrator vib1 = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib1.vibrate(3000);

                break;

            case R.id.button2:
                setAnswerValue();
                checkResult(false);

                Vibrator vib2 = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib2.vibrate(3000);
                break;

            case R.id.button3:
                setQuestionValue();
                clearAnswerValue();
                clearScoreValue();
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
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        int newScore = Integer.parseInt(txtScore.getText().toString()) + score;
        txtScore.setText(Integer.toString(newScore));
    }

    private void clearScoreValue() {
        TextView txtScore = (TextView)findViewById(R.id.text_score);
        txtScore.setText("0");
    }
}
