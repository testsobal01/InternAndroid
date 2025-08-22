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

    private Button bonusButton;
    private boolean isBonusActive = false; // ボーナス発動中フラグ

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

        bonusButton = findViewById(R.id.bonus_button);
        bonusButton.setVisibility(View.GONE);
        bonusButton.setOnClickListener(v -> onBonusButtonClicked());

        setQuestionValue();
    }

    @Override
    public void onClick(View view) {
        if (isBonusActive) {
            Toast.makeText(this, "ボーナス中です！まずはボーナスボタンを押してください。", Toast.LENGTH_SHORT).show();
            return; // ボーナス中は他のボタン無効化
        }

        int id = view.getId();
        if (id == R.id.button1) {
            setAnswerValue();
            checkResultWithChance(true);
        } else if (id == R.id.button2) {
            setAnswerValue();
            checkResultWithChance(false);
        } else if (id == R.id.button3) {
            setQuestionValue();
            clearAnswerValue();
            clearScoreValue();
            vibrate(new long[]{0, 100, 100, 100, 100, 100});
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

    private void checkResultWithChance(boolean isHigh) {
        Random rand = new Random();

        // 5%でボーナス発生
        if (rand.nextInt(100) < 35) {
            activateBonus();
            return;
        }

        // 10%でチャンス演出
        if (rand.nextInt(10) == 0) {
            showChanceAnimation(() -> continueCheckResult(isHigh));
        } else {
            continueCheckResult(isHigh);
        }
    }

    private void activateBonus() {
        isBonusActive = true;
        bonusButton.setVisibility(View.VISIBLE);
        Toast.makeText(this, "ボーナスチャンス！ボーナスボタンを押してね！", Toast.LENGTH_LONG).show();
    }

    private void onBonusButtonClicked() {
        if (!isBonusActive) return;

        Random rand = new Random();
        int chance = rand.nextInt(100); // 0〜99

        TextView txtScore = findViewById(R.id.text_score);

        if (chance < 35) { // 20%の確率でスコア0リセット
            txtScore.setText("0");
            Toast.makeText(this, "ボーナス大外れ…スコアが0に！", Toast.LENGTH_LONG).show();
            vibrate(new long[]{0, 500, 500, 1000});
            shakeViewWithVibration(findViewById(android.R.id.content));
        } else {
            int bonusPoints = rand.nextInt(16) + 5; // 5〜20点
            int currentScore = Integer.parseInt(txtScore.getText().toString());
            txtScore.setText(String.valueOf(currentScore + bonusPoints));
            Toast.makeText(this, "ボーナスゲット！+" + bonusPoints + "点！", Toast.LENGTH_LONG).show();
            vibrate(new long[]{0, 100, 100, 100});
            shakeViewWithVibration(findViewById(android.R.id.content));
        }

        // ボーナス終了処理
        isBonusActive = false;
        bonusButton.setVisibility(View.GONE);
    }

    private void continueCheckResult(boolean isHigh) {
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
                shakeViewWithVibration(findViewById(android.R.id.content));
            } else if (question > answer) {
                result = "LOSE";
                score = -1;
                animateLoseText();
                shakeViewWithVibration(findViewById(android.R.id.content));
            } else {
                result = "DRAW";
                score = 1;
                animateDrawText();
                shakeViewWithVibration(findViewById(android.R.id.content));
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                animateWinText();
                shakeViewWithVibration(findViewById(android.R.id.content));
            } else if (question < answer) {
                result = "LOSE";
                score = -1;
                animateLoseText();
                shakeViewWithVibration(findViewById(android.R.id.content));
            } else {
                result = "DRAW";
                score = 1;
                animateDrawText();
                shakeViewWithVibration(findViewById(android.R.id.content));
            }
        }

        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText("結果：" + question + ":" + answer + "(" + result + ")");

        setNextQuestion();
        setScore(score);
    }

    private void shakeViewWithVibration(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX",
                0f, 25f, -25f, 25f, -25f, 15f, -15f, 6f, -6f, 0f);
        animator.setDuration(500);
        animator.start();

        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(new long[]{0, 50, 50, 50, 50}, -1);
        }
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

    private void showChanceAnimation(Runnable onAnimationEnd) {
        TextView chanceText = findViewById(R.id.chance_text1);
        chanceText.setVisibility(View.VISIBLE);
        chanceText.setText("激アツ！CHANCE!!");
        chanceText.setAlpha(0f);

        chanceText.animate()
                .alpha(1f)
                .setDuration(500)
                .withEndAction(() -> {
                    chanceText.animate()
                            .alpha(0f)
                            .setDuration(500)
                            .withEndAction(() -> {
                                chanceText.setVisibility(View.GONE);
                                onAnimationEnd.run();
                            });
                });
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
