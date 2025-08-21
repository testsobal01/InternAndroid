package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
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

        Button btn3 = findViewById(R.id.button3);
        btn3.setOnClickListener(this);

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
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null) {
                long[] pattern = {0, 100, 100, 100, 100, 100};
                vibrator.vibrate(pattern, -1);
            }
        }
    }

    private void clearAnswerValue() {
        TextView txtView = findViewById(R.id.answer);
        txtView.setText("値2");
    }

    private void setQuestionValue() {
        Random r = new Random();
        int questionValue = r.nextInt(11);

        TextView txtView = findViewById(R.id.question);
        txtView.setText(String.valueOf(questionValue));
    }

    private void setAnswerValue() {
        Random r = new Random();
        int answerValue = r.nextInt(11);

        TextView txtView = findViewById(R.id.answer);
        txtView.setText(String.valueOf(answerValue));
    }

    private void checkResult(boolean isHigh) {
        TextView txtViewQuestion = findViewById(R.id.question);
        TextView txtViewAnswer = findViewById(R.id.answer);

        int question = Integer.parseInt(txtViewQuestion.getText().toString());
        int answer = Integer.parseInt(txtViewAnswer.getText().toString());

        TextView txtResult = findViewById(R.id.text_result);

        String result;
        int score;

        if (isHigh) {
            if (question < answer) {
                result = "WIN";
                score = 2;
                animateWinText();
                vibrate(new long[]{0, 100, 100, 100, 100, 100});
            } else if (question > answer) {
                result = "LOSE";
                score = -1;
                animateLoseText();
                vibrate(new long[]{0, 500, 500, 500, 500});
            } else {
                result = "DRAW";
                score = 1;
                animateDrawText(); // DRAWアニメーションの呼び出し
                vibrate(new long[]{0, 1000, 1000});
            }
        } else { // Lowが押された場合
            if (question > answer) {
                result = "WIN";
                score = 2;
                animateWinText();
                vibrate(new long[]{0, 100, 100, 100, 100, 100});
            } else if (question < answer) {
                result = "LOSE";
                score = -1;
                animateLoseText();
                vibrate(new long[]{0, 500, 500, 500, 500});
            } else {
                result = "DRAW";
                score = 1;
                animateDrawText(); // DRAWアニメーションの呼び出し
                vibrate(new long[]{0, 1000, 1000});
            }
        }

        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText("結果：" + question + ":" + answer + "(" + result + ")");

        setNextQuestion();
        setScore(score);
    }

    private void vibrate(long[] pattern) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(pattern, -1);
        }
    }

    private void animateWinText() {
        TextView winText = findViewById(R.id.win_animation_text1);
        if (winText == null) return;

        winText.setVisibility(View.VISIBLE);

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        ObjectAnimator animator = ObjectAnimator.ofFloat(winText, "translationX", -1000f, screenWidth);
        animator.setDuration(3000);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.setRepeatMode(ObjectAnimator.RESTART);
        animator.start();

        new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {}
            public void onFinish() {
                animator.cancel();
                winText.setVisibility(View.GONE);
                winText.setTranslationX(-1000f);
            }
        }.start();
    }

    private void animateLoseText() {
        TextView loseText = findViewById(R.id.lose_animation_text);
        if (loseText == null) return;

        loseText.setVisibility(View.VISIBLE);

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        ObjectAnimator animator = ObjectAnimator.ofFloat(loseText, "translationX", -1000f, screenWidth);
        animator.setDuration(3000);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.setRepeatMode(ObjectAnimator.RESTART);
        animator.start();

        new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {}
            public void onFinish() {
                animator.cancel();
                loseText.setVisibility(View.GONE);
                loseText.setTranslationX(-1000f);
            }
        }.start();
    }

    // DRAW用のアニメーションメソッドを追加
    private void animateDrawText() {
        TextView drawText = findViewById(R.id.draw_animation_text);
        if (drawText == null) return;

        drawText.setVisibility(View.VISIBLE);

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        ObjectAnimator animator = ObjectAnimator.ofFloat(drawText, "translationX", -1000f, screenWidth);
        animator.setDuration(3000);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.setRepeatMode(ObjectAnimator.RESTART);
        animator.start();

        new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {}
            public void onFinish() {
                animator.cancel();
                drawText.setVisibility(View.GONE);
                drawText.setTranslationX(-1000f);
            }
        }.start();
    }

    private void setNextQuestion() {
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {}
            @Override
            public void onFinish() {
                setQuestionValue();
            }
        }.start();
    }

    private void setScore(int score) {
        TextView txtScore = findViewById(R.id.text_score);
        int newScore = Integer.parseInt(txtScore.getText().toString()) + score;
        txtScore.setText(String.valueOf(newScore));
    }

    private void clearScoreValue() {
        TextView txtScore = findViewById(R.id.text_score);
        txtScore.setText("0");
    }
}