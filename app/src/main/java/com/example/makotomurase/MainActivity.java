package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;
    int newScore = 0;
    int score = 0;
    int num;

    @Override
    protected void onPause() {
        super.onPause();

        prefEditor.putInt("main_input",newScore);
        prefEditor.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        score = pref.getInt("main_input",num);
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        txtScore.setText(Integer.toString(score));

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

        pref = getSharedPreferences("AndroidSeminor",MODE_PRIVATE);
        prefEditor = pref.edit();

        // 起動時に関数を呼び出す
        setQuestionValue();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_manu,menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.button1:
                Vibrator vib=(Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(250);
                setAnswerValue();
                checkResult(true);
                break;
            case R.id.button2:
                Vibrator vib2=(Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib2.vibrate(250);
                setAnswerValue();
                checkResult(false);
                break;

            case R.id.button3:
                Vibrator vib3=(Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib3.vibrate(100);
                setQuestionValue();
                clearAnswerValue();
                score = 0;
                TextView txtScore = (TextView) findViewById(R.id.text_score);
                txtScore.setText(Integer.toString(score));
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

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = "WIN";
                score = 2;
                TextView view = (TextView) findViewById(R.id.question);
                //View view;
                int colorFrom = ((ColorDrawable)view.getBackground()).getColor();
                int colorTo = Color.RED;
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                colorAnimation.setDuration(5000);
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        TextView view = (TextView) findViewById(R.id.question);
                        //view.setBackgroundColor((int) animator.getAnimatedValue());
                        view.setTextColor((int)animator.getAnimatedValue());
                    }

                });
                colorAnimation.start();

            } else if (question > answer) {
                result = "LOSE";
                score = -1;
                TextView view = (TextView) findViewById(R.id.question);
                //View view;
                int colorFrom = ((ColorDrawable)view.getBackground()).getColor();
                int colorTo = Color.BLUE;
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                colorAnimation.setDuration(5000);
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        TextView view = (TextView) findViewById(R.id.question);
                        //view.setBackgroundColor((int) animator.getAnimatedValue());
                        view.setTextColor((int)animator.getAnimatedValue());
                    }

                });
                colorAnimation.start();

            } else {
                result = "DRAW";
                score = 1;
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                TextView view = (TextView) findViewById(R.id.question);
                //View view;
                int colorFrom = ((ColorDrawable)view.getBackground()).getColor();
                int colorTo = Color.RED;
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                colorAnimation.setDuration(5000);
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        TextView view = (TextView) findViewById(R.id.question);
                        //view.setBackgroundColor((int) animator.getAnimatedValue());
                        view.setTextColor((int)animator.getAnimatedValue());

                    }

                });
                colorAnimation.start();

            } else if (question < answer) {
                result = "LOSE";
                score = -1;
                TextView view = (TextView) findViewById(R.id.question);
                //View view;
                int colorFrom = ((ColorDrawable)view.getBackground()).getColor();
                int colorTo = Color.BLUE;
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                colorAnimation.setDuration(5000);
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        TextView view = (TextView) findViewById(R.id.question);
                        //view.setBackgroundColor((int) animator.getAnimatedValue());
                        view.setTextColor((int)animator.getAnimatedValue());
                    }

                });
                colorAnimation.start();
            } else {
                result = "DRAW";
                score = 1;
                TextView view = (TextView) findViewById(R.id.question);
                //View view;
                int colorFrom = ((ColorDrawable)view.getBackground()).getColor();
                int colorTo = Color.GRAY;
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                colorAnimation.setDuration(5000);
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        TextView view = (TextView) findViewById(R.id.question);
                        //view.setBackgroundColor((int) animator.getAnimatedValue());
                        view.setTextColor((int)animator.getAnimatedValue());
                    }

                });
                colorAnimation.start();
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
        newScore = Integer.parseInt(txtScore.getText().toString()) + score;
        txtScore.setText(Integer.toString(newScore));
    }

}