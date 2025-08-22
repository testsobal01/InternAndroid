package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class BettingActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_betting);

        Button btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(this);

        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(this);

        Button btn4 = (Button) findViewById(R.id.button4);
        btn4.setOnClickListener(this);

        pref = getSharedPreferences("InternAndroid", MODE_PRIVATE);
        prefEditor = pref.edit();

        // 起動時に関数を呼び出す
        setQuestionValue();

    }


    @Override
    protected void onPause() {
        super.onPause();

        TextView textView = (TextView) findViewById(R.id.text_score);

        prefEditor.putString("main_input",textView.getText().toString());
        prefEditor.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();

        TextView textView = (TextView) findViewById(R.id.text_score);

        String readText = pref.getString("main_input","保存されていません");
        textView.setText(readText);

    }

    //初期の持ち点：100点
    int points=100;
    //bet金額
    int bet=0;

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.button4)
        {
            bet ++;
            TextView txtbet = (TextView) findViewById(R.id.text_bet);
            txtbet.setText(Integer.toString(bet));
            points--;
            TextView txtscore = (TextView) findViewById(R.id.text_score);
            txtscore.setText(Integer.toString(points));
        }
        else if (id == R.id.button1 && bet != 0)
        {
            setAnswerValue();
            checkResult(true);
            clearBetValue();
        }
        else if (id == R.id.button2 && bet != 0)
        {
            setAnswerValue();
            checkResult(false);
            clearBetValue();
        }
        else if (id == R.id.button3)
        {

            //バイブレーション追加
            Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vib.vibrate(500);

            setQuestionValue();
            clearAnswerValue();
            clearScoreValue();
            clearBetValue();
            setBackgroundColor(R.color.default_color);
        }
        if(bet==0)
        {
            Toast.makeText(this, "NO BET", Toast.LENGTH_LONG).show();
        }
    }

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        String value2 = getString(R.string.value2_default);
        txtView.setText(value2);
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
        String result = null;

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = "WIN";
                points += bet*2;
                textAnimation(R.anim.scale_up_down);
                setBackgroundColor(R.color.win_color);
            } else if (question > answer) {
                result = "LOSE";
                textAnimation(R.anim.scale_down_up);
                setBackgroundColor(R.color.lose_color);
            } else {
                result = "DRAW";
                points += bet;
                setBackgroundColor(R.color.default_color);
            }
        } else {
            if (question > answer) {
                result = "WIN";
                points += bet*2;
                textAnimation(R.anim.scale_up_down);
                setBackgroundColor(R.color.win_color);
            } else if (question < answer) {
                result = "LOSE";
                textAnimation(R.anim.scale_down_up);
                setBackgroundColor(R.color.lose_color);
            } else {
                result = "DRAW";
                points += bet;
                setBackgroundColor(R.color.default_color);
            }
        }



        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        String result_default = getString(R.string.result);
        txtResult.setText(result_default + "：" + question + ":" + answer + "(" + result + ")");

        // 続けて遊べるように値を更新
        setNextQuestion();
        // スコアを表示
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        txtScore.setText(Integer.toString(points));

    }

    private void textAnimation(int anim_id){
        TextView animText = findViewById(R.id.answer);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), anim_id);
        animText.startAnimation(animation);
    }

    private void setBackgroundColor(int color_id){
        TextView myTextView = findViewById(R.id.answer);
        myTextView.setBackgroundColor(ContextCompat.getColor(this, color_id));
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

    private void clearScoreValue() {
        points=100;
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        txtScore.setText("100");
    }

    private void clearBetValue(){
        TextView txtBet = (TextView) findViewById(R.id.text_bet);
        txtBet.setText("0");
        bet=0;
    }


}

