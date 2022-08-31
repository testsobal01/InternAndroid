package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
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

        pref = getSharedPreferences("TeamF", MODE_PRIVATE);
        prefEditor = pref.edit();

        TextView txtScore = (TextView) findViewById(R.id.text_score);
        int score = pref.getInt("game_score", 0);
        txtScore.setText(Integer.toString(score));

        // 起動時に関数を呼び出す
        setQuestionValue();

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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // スコアを保存
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        prefEditor.putInt("game_score", Integer.parseInt(txtScore.getText().toString()));
        prefEditor.commit();

    }

    private void clearAnswerValue() {
        String word = getString(R.string.atai);
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText(word);
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
        String word_win = getString(R.string.win);
        String word_lose = getString(R.string.lose);
        String word_draw = getString(R.string.draw);
        String word_result = getString(R.string.result);

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = word_win;
                score = 2;
                startAnimation(0);
            } else if (question > answer) {
                result = word_lose;
                score = -1;
                startAnimation(2);
                Vibrator vib= (Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(500);
            } else {
                result = word_draw;
                score = 1;
                Vibrator vib= (Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(200);
            }
        } else {
            if (question > answer) {
                result = word_win;
                score = 2;
                startAnimation(1);
            } else if (question < answer) {
                result = word_lose;
                score = -1;
                startAnimation(2);
                Vibrator vib= (Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(500);
            } else {
                result = word_draw;
                score = 1;
                Vibrator vib= (Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(200);
            }
        }


        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText(word_result + question + ":" + answer + "(" + result + ")");

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

    private void startAnimation(final int type) {
        final TextView txtViewQuestion = findViewById(R.id.question);
        final TextView txtViewAnswer = findViewById(R.id.answer);
        long time = 1000;

        // 縮小アニメーション
        final ScaleAnimation scaleSmallAnimation = new ScaleAnimation(
                2.0f, 1.0f, 2.0f,1.0f,
                Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleSmallAnimation.setDuration(time);
        scaleSmallAnimation.setRepeatCount(0);
        scaleSmallAnimation.setFillAfter(true);
        // 拡大アニメーション
        final ScaleAnimation scaleBigAnimation = new ScaleAnimation(
                1.0f, 2.0f, 1.0f,2.0f,
                Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleBigAnimation.setDuration(time);
        scaleBigAnimation.setRepeatCount(0);
        scaleBigAnimation.setFillAfter(true);
        scaleBigAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            // 拡大アニメーション終了後
            @Override
            public void onAnimationEnd(Animation animation) {
                switch (type) {
                    case 0:
                        txtViewAnswer.startAnimation(scaleSmallAnimation);
                        break;
                    case 1:
                        txtViewQuestion.startAnimation(scaleSmallAnimation);
                        break;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        // 回転アニメーション
        final RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 360.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(time / 2);
        rotateAnimation.setRepeatCount(0);
        rotateAnimation.setFillAfter(false);

        switch (type) {
            case 0:
                txtViewQuestion.setZ(0);
                txtViewAnswer.setZ(10);
                txtViewAnswer.startAnimation(scaleBigAnimation);
                break;
            case 1:
                txtViewQuestion.setZ(10);
                txtViewAnswer.setZ(0);
                txtViewQuestion.startAnimation(scaleBigAnimation);
                break;
            case 2:
                txtViewAnswer.setZ(100);
                txtViewAnswer.startAnimation(rotateAnimation);
                break;
        }
    }
}