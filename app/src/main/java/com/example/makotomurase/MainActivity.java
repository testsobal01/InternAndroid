package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.content.SharedPreferences;
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
    protected void onPause() {
        super.onPause();
        TextView textView = (TextView)findViewById(R.id.text_score);

        prefEditor.putString("main_input",textView.getText().toString());
        prefEditor.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();

        TextView textView = (TextView)findViewById(R.id.text_score);
        String readText = pref.getString("main_input","0");
        textView.setText(readText);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        //バイブレーションの追加
        Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        vib.vibrate(5000);

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

    }

    int score;

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        score = 0;
        txtScore.setText(Integer.toString(score));
        txtView.setText(getString(R.string.value2));
    }

    private void setQuestionValue() {
        Random r = new Random();
        // 0から10の範囲で乱数を生成（+1する必要がある）
        int questionValue = r.nextInt(10 + 1);
        //背景色のリセット
        TextView txtView_que= (TextView) findViewById(R.id.question);
        txtView_que.setBackgroundColor(Color.parseColor("#ff00ff"));
        txtView_que.setText(Integer.toString(questionValue));
        TextView txtView_ans = (TextView) findViewById(R.id.answer);
        txtView_ans.setBackgroundColor(Color.parseColor("#ffff00"));
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

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = getString(R.string.win);
                score = 2;
                //背景色の変更
                TextView txtView_que = (TextView) findViewById(R.id.question);
                txtView_que.setBackgroundColor(Color.parseColor("#FF7F50"));
                TextView txtView_ans = (TextView) findViewById(R.id.answer);
                txtView_ans.setBackgroundColor(Color.parseColor("#6A5ACD"));
                
            } else if (question > answer) {
                result = getString((R.string.lose));
                score = -1;
                //背景色の変更
                TextView txtView_que = (TextView) findViewById(R.id.question);
                txtView_que.setBackgroundColor(Color.parseColor("#6A5ACD"));
                TextView txtView_ans = (TextView) findViewById(R.id.answer);
                txtView_ans.setBackgroundColor(Color.parseColor("#FF7F50"));
            } else {
                result = getString(R.string.draw);
                score = 1;
                //背景色の変更
                TextView txtView_que = (TextView) findViewById(R.id.question);
                txtView_que.setBackgroundColor(Color.parseColor("#808080"));
                TextView txtView_ans = (TextView) findViewById(R.id.answer);
                txtView_ans.setBackgroundColor(Color.parseColor("#808080"));
            }
        } else {
            if (question > answer) {
                result = getString(R.string.win);
                score = 2;
                //背景色の変更
                TextView txtView_que = (TextView) findViewById(R.id.question);
                txtView_que.setBackgroundColor(Color.parseColor("#FF7F50"));
                TextView txtView_ans = (TextView) findViewById(R.id.answer);
                txtView_ans.setBackgroundColor(Color.parseColor("#6A5ACD"));
            } else if (question < answer) {
                result = getString(R.string.lose);
                score = -1;
                //背景色の変更
                TextView txtView_que = (TextView) findViewById(R.id.question);
                txtView_que.setBackgroundColor(Color.parseColor("#6A5ACD"));
                TextView txtView_ans = (TextView) findViewById(R.id.answer);
                txtView_ans.setBackgroundColor(Color.parseColor("#FF7F50"));
            } else {
                result = getString(R.string.draw);
                score = 1;
                //背景色の変更
                TextView txtView_que = (TextView) findViewById(R.id.question);
                txtView_que.setBackgroundColor(Color.parseColor("#808080"));
                TextView txtView_ans = (TextView) findViewById(R.id.answer);
                txtView_ans.setBackgroundColor(Color.parseColor("#808080"));


            }
        }


        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText(getString(R.string.result) + question + ":" + answer + "(" + result + ")");

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

}