package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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
            } else if (question > answer) {
                result = "LOSE";
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
                result = "LOSE";
                score = -1;
            } else {
                result = "DRAW";
                score = 1;
            }
        }

        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText("結果：" + question + ":" + answer + "(" + result + ")");
        b_7(result);


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

    private void b_7(String result) {//勝敗によって数字が動く機能
        TextView txtViewQuestion = findViewById(R.id.question);
        TextView txtViewAnswer = findViewById(R.id.answer);

        TranslateAnimation TranslateQs = new TranslateAnimation(
                Animation.ABSOLUTE, 0f,
                Animation.ABSOLUTE, 125f,
                Animation.ABSOLUTE, 0f,
                Animation.ABSOLUTE, 0f);
        TranslateQs.setDuration(500);
        TranslateQs.setRepeatCount(0);
        TranslateQs.setFillAfter(false);
        TranslateAnimation TranslateQf = new TranslateAnimation(
                Animation.ABSOLUTE, 125f,
                Animation.ABSOLUTE, 0f,
                Animation.ABSOLUTE, 0f,
                Animation.ABSOLUTE, 0f);
        TranslateQf.setDuration(1000);
        TranslateQf.setRepeatCount(0);
        TranslateQf.setFillAfter(false);
        TranslateAnimation TranslateQl = new TranslateAnimation(
                Animation.ABSOLUTE, 125f,
                Animation.ABSOLUTE, -1500f,
                Animation.ABSOLUTE, 0f,
                Animation.ABSOLUTE, 4000f);
        TranslateQl.setDuration(1000);
        TranslateQl.setRepeatCount(0);
        TranslateQl.setFillAfter(false);
        TranslateAnimation TranslateAs = new TranslateAnimation(
                Animation.ABSOLUTE, 0f,
                Animation.ABSOLUTE, -125f,
                Animation.ABSOLUTE, 0f,
                Animation.ABSOLUTE, 0f);
        TranslateAs.setDuration(500);
        TranslateAs.setRepeatCount(0);
        TranslateAs.setFillAfter(false);
        TranslateAnimation TranslateAf = new TranslateAnimation(
                Animation.ABSOLUTE, -125f,
                Animation.ABSOLUTE, 0f,
                Animation.ABSOLUTE, 0f,
                Animation.ABSOLUTE, 0f);
        TranslateAf.setDuration(1000);
        TranslateAf.setRepeatCount(0);
        TranslateAf.setFillAfter(false);
        TranslateAnimation TranslateAl = new TranslateAnimation(
                Animation.ABSOLUTE, -125f,
                Animation.ABSOLUTE, 1500f,
                Animation.ABSOLUTE, 0f,
                Animation.ABSOLUTE, 4000f);
        TranslateAl.setDuration(1000);
        TranslateAl.setRepeatCount(0);
        TranslateAl.setFillAfter(false);

        RotateAnimation RotateWq = new RotateAnimation(
                0.0f, -50f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        RotateWq.setDuration(1000);
        RotateWq.setRepeatCount(0);
        RotateWq.setFillAfter(false);
        RotateAnimation RotateWa = new RotateAnimation(
                0.0f, 50f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        RotateWa.setDuration(1000);
        RotateWa.setRepeatCount(0);
        RotateWa.setFillAfter(false);

        ScaleAnimation scaleS = new ScaleAnimation(
                1.0f, 0.7f, 1.0f, 0.7f,
                Animation.RELATIVE_TO_SELF, 0.7f,
                Animation.RELATIVE_TO_SELF, 0.7f);
        scaleS.setDuration(500);
        scaleS.setRepeatCount(0);
        scaleS.setFillAfter(false);
        ScaleAnimation scaleW = new ScaleAnimation(
                0.7f, 0.4f, 0.7f, 0.4f,
                Animation.RELATIVE_TO_SELF, 0.7f,
                Animation.RELATIVE_TO_SELF, 0.7f);
        scaleW.setDuration(1000);
        scaleW.setRepeatCount(0);
        scaleW.setFillAfter(false);
        ScaleAnimation scaleL = new ScaleAnimation(
                0.7f, 0.2f, 0.7f, 0.2f,
                Animation.RELATIVE_TO_SELF, 0.7f,
                Animation.RELATIVE_TO_SELF, 0.7f);
        scaleL.setDuration(1000);
        scaleL.setRepeatCount(0);
        scaleL.setFillAfter(false);
        ScaleAnimation scaleD = new ScaleAnimation(
                0.7f, 0.8f, 0.7f, 0.8f,
                Animation.RELATIVE_TO_SELF, 0.7f,
                Animation.RELATIVE_TO_SELF, 0.7f);
        scaleD.setDuration(1000);
        scaleD.setRepeatCount(0);
        scaleD.setFillAfter(false);

        AnimationSet startQ = new AnimationSet(true);
        startQ.addAnimation(TranslateQs);
        startQ.addAnimation(scaleS);
        AnimationSet startA = new AnimationSet(true);
        startA.addAnimation(TranslateAs);
        startA.addAnimation(scaleS);
        AnimationSet QW = new AnimationSet(true);
        QW.addAnimation(TranslateQf);
        QW.addAnimation(RotateWq);
        QW.addAnimation(scaleD);
        AnimationSet AW = new AnimationSet(true);
        AW.addAnimation(TranslateAf);
        AW.addAnimation(RotateWa);
        AW.addAnimation(scaleD);
        AnimationSet QL = new AnimationSet(true);
        QL.addAnimation(TranslateQl);
        QL.addAnimation(scaleL);
        AnimationSet AL = new AnimationSet(true);
        AL.addAnimation(TranslateAl);
        AL.addAnimation(scaleL);
        AnimationSet QD = new AnimationSet(true);
        QD.addAnimation(TranslateQf);
        QD.addAnimation(scaleD);
        AnimationSet AD = new AnimationSet(true);
        AD.addAnimation(TranslateAf);
        AD.addAnimation(scaleD);

        txtViewQuestion.startAnimation(startQ);
        txtViewAnswer.startAnimation(startA);
        new CountDownTimer(500, 1000) {
            @Override
            public void onTick(long l) {}
            @Override
            public void onFinish() {
                if (result == "WIN") {
                    txtViewQuestion.startAnimation(QW);
                    txtViewAnswer.startAnimation(AW);
                } else if (result == "LOSE") {
                    txtViewQuestion.startAnimation(QL);
                    txtViewAnswer.startAnimation(AL);
                } else if (result == "DRAW") {
                    txtViewQuestion.startAnimation(QD);
                    txtViewAnswer.startAnimation(AD);
                }
            }
        }.start();
    }
}

