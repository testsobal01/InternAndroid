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

import androidx.core.view.ViewCompat;

import java.util.Random;

import android.os.CountDownTimer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //プリファレンスでのスコア保存
    SharedPreferences pref;
    SharedPreferences. Editor prefEditer;
    //
    Boolean isWin = false;

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

        //保存するファイル的なもの
        pref = getSharedPreferences("AndroidSeminor", MODE_PRIVATE);
        prefEditer = pref.edit();



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //保存のためのスコアの取得
        TextView textView = (TextView) findViewById(R.id.text_score);

        //フォルダにスコアを保存
        prefEditer. putString("main_input",textView.getText().toString());
        prefEditer.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //画面上に文字列をセットするため、テキストビューを取得
        TextView textView = (TextView)findViewById(R.id.text_score);
        //保存した値をキー名を指定して取得
        //一度も保存されてない場合の文字列も追加する

        String readText = pref.getString("main_input","0");
        textView.setText(readText);
    }

    @Override

    public void onClick(View view) {
        TextView txtResult = (TextView) findViewById(R.id.text_score);
        int id = view.getId();
        switch (id) {
            case R.id.button1:
                setAnswerValue();
                checkResult(true);
                setNextQuestion();
                break;
            case R.id.button2:
                setAnswerValue();
                checkResult(false);
                setNextQuestion();
                break;
            case R.id.button3:

                setScore(0,true);
                setQuestionValue();
                clearAnswerValue();
                break;

        }

    }

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText("?");
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

    private void animationWin(){
        TextView txtViewAnswer = (TextView) findViewById(R.id.answer);
        // scaleを元に戻す
        txtViewAnswer.setScaleX(1f);
        txtViewAnswer.setScaleY(1f);

        // scaleアニメーション開始
        ViewCompat.animate(txtViewAnswer)
                .setDuration(1000)
                .scaleX(1.3f)
                .scaleY(1.3f)
                .start();
        // ↑↑ここまでを追加↑↑
    }

    private void animationLorD(){
        TextView txtViewAnswer = (TextView) findViewById(R.id.answer);
        // scaleを元に戻す
        txtViewAnswer.setScaleX(1.3f);
        txtViewAnswer.setScaleY(1.3f);

        // scaleアニメーション開始
        ViewCompat.animate(txtViewAnswer)
                .setDuration(1000)
                .scaleX(1f)
                .scaleY(1f)
                .start();
        // ↑↑ここまでを追加↑↑
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
                if (!isWin) {
                    animationWin();
                }
                isWin = true;
                result = "WIN";
                score = 2;
            } else if (question > answer) {
                animationLorD();
                isWin = false;
                result = "LOSE";
                score = -1;
            } else {
                animationLorD();
                isWin = false;
                result = "DRAW";
                score = 1;
            }
        } else {
            if (question > answer) {
                result = "WIN";
                if (!isWin) {
                    animationWin();
                }
                isWin = true;
                score = 2;
            } else if (question < answer) {
                animationLorD();
                isWin = false;
                result = "LOSE";
                score = -1;
            } else {
                animationLorD();
                isWin = false;
                result = "DRAW";
                score = 1;
            }
        }


        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText("結果：" + question + ":" + answer + "(" + result + ")");

        // スコアを表示
        setScore(score, false);

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

    private void setScore(int score, boolean isReset) {
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        int newScore = Integer.parseInt(txtScore.getText().toString()) + score;
        if(isReset == true){
            newScore = 0;
        }
        txtScore.setText(Integer.toString(newScore));
    }




}

