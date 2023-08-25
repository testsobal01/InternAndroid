package com.example.makotomurase;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView textRange, textScore, textScoreOperator, textScoreAdd, textQuestionNumber, textAnswerNumber;
    private int questionNumber, answerNumber, score = 0, range = 10;
    private final Handler handler = new Handler();
    private final Runnable handlerRunnable = () -> handler.post(this::setQuestionValue);

    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button1) {

            animateRandomNumberTextView(textAnswerNumber, () -> {
                setAnswerValue();
                checkResult(true);
            });

        } else if (id == R.id.button2) {
            animateRandomNumberTextView(textAnswerNumber, () -> {
                setAnswerValue();
                checkResult(false);
            });
        } else if (id == R.id.button3) {
            animateRandomNumberTextView(textQuestionNumber, this::setQuestionValue);
            clearAnswerValue();
            clearScoreValue();
        }
    }


    private void init() {
        linkViewIds();
        readIntent();
        registerViewListeners();
    }

    private void readIntent() {
        range = getIntent().getIntExtra(Constrants.KEY_INTENT_RANGE, 10);
        textRange.setText(String.valueOf(range));
    }

    private void linkViewIds() {
        textRange = findViewById(R.id.text_range);
        textScoreOperator = findViewById(R.id.text_score_operator);
        textScoreAdd = findViewById(R.id.text_score_add);
        textScore = findViewById(R.id.text_score);
        textQuestionNumber = findViewById(R.id.question);
        textAnswerNumber = findViewById(R.id.answer);
    }

    private void registerViewListeners() {
        Button btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(this);

        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(this);
    }

    public void animateRandomNumberTextView(final TextView view, Runnable callback) {
        Random random = new Random();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100);
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(v -> view.setText(String.valueOf(random.nextInt(10))));
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                callback.run();
            }
        });
        valueAnimator.start();
    }

    /**
     * StartActivityからはここを呼んでね！
     */
    public void startNewGame(StartActivity _safeChecker) {

        setQuestionValue();
    }

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText("値2");
    }

    private void setQuestionValue() {
        Random r = new Random();
        // 0から10の範囲で乱数を生成（+1する必要がある）
        questionNumber = r.nextInt(10 + 1);

        TextView txtView = findViewById(R.id.question);
        txtView.setText(String.format(Locale.getDefault(), "%d", questionNumber));
        zoomTextViewAnimation(textQuestionNumber, true);
    }

    private void setNumberAnimation(int from, int to, TextView view, Runnable callback) {
        final ValueAnimator anim = ValueAnimator.ofInt(from, to);
        anim.setDuration(Constrants.DURATION_ANIM_NUMBER);
        anim.addUpdateListener(animation -> {
            view.setText(String.valueOf((Integer) animation.getAnimatedValue()));
        });
        anim.start();
    }

    private void zoomTextViewAnimation(TextView view, boolean showNextText) {
        String backup = view.getText().toString();
        if (showNextText) {
            view.setText(
                    String.format("%s\n%s\n%s",
                            getString(R.string.next_first),
                            backup,
                            getString(R.string.next_second)
                    )
            );
        }
        view.setTextSize(100);
        final ValueAnimator anim = ValueAnimator.ofInt(100, 60);
        anim.setDuration(300);
        anim.addUpdateListener(animation -> view.setTextSize((Integer) animation.getAnimatedValue()));
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (showNextText) {
                    handler.postDelayed(() -> view.setText(backup), Constrants.DELAY_NEXT_TEXT_UPDATE);
                }
            }
        });
        anim.start();
    }

    private void setAnswerValue() {
        Random r = new Random();
        answerNumber = r.nextInt(range + 1);

        TextView txtView = findViewById(R.id.answer);
        txtView.setText(String.format(Locale.getDefault(), "%d", answerNumber));
        zoomTextViewAnimation(textAnswerNumber, false);
    }

    private void checkResult(boolean isHigh) {
        TextView txtResult = (TextView) findViewById(R.id.text_result);

        // 結果を示す文字列を入れる変数を用意
        String result;
        int _score;

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (questionNumber < answerNumber) {
                result = "WIN";
                _score = Constrants.SCORE_WIN;
            } else if (questionNumber > answerNumber) {
                result = "LOSE";
                _score = Constrants.SCORE_LOSE;
            } else {
                result = "DRAW";
                _score = Constrants.SCORE_DRAW;
            }
        } else {
            if (questionNumber > answerNumber) {
                result = "WIN";
                _score = Constrants.SCORE_WIN;
            } else if (questionNumber < answerNumber) {
                result = "LOSE";
                _score = Constrants.SCORE_LOSE;
            } else {
                result = "DRAW";
                _score = Constrants.SCORE_DRAW;
            }
        }

        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText("結果：" + questionNumber + ":" + answerNumber + "(" + result + ")");

        // 続けて遊べるように値を更新
        setNextQuestion();
        // スコアを表示
        handler.postDelayed(() -> updateScore(_score), Constrants.DELAY_NEXT_TEXT_UPDATE);
    }

    @SuppressLint("ResourceType")
    private void updateScore(int _score) {
        score += _score;
        setNumberAnimation(score - _score, score, textScore, null);
        @IdRes int operator = R.string.plus;
        if (_score < 0) {
            operator = R.string.minus;
        }
        textScoreOperator.setText(getString(operator));
        setNumberAnimation(Math.abs(_score), 0, textScoreAdd, () -> {
            handler.postDelayed(() -> {
                textScoreOperator.setText(null);
                textScoreAdd.setText(null);
            }, Constrants.DELAY_NEXT_TEXT_UPDATE);
        });
    }

    private void setNextQuestion() {
        handler.removeCallbacks(handlerRunnable);
        handler.postDelayed(handlerRunnable, Constrants.DELAY_AFTER_GAME);
    }

    private void clearScoreValue() {
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        txtScore.setText("0");
    }
}

