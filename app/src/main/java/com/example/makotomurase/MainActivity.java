package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator; // ★インポートの重複を整理しました
import android.os.Bundle;
import android.os.CountDownTimer;
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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
            view.setPadding(insets.left, insets.top, insets.right, 0);
            return windowInsets;
        });

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

        String result;
        int score;
        boolean isWin = false; // ★【修正】isWin変数をここで作りました

        // Highが押された
        if (isHigh) {
            if (question < answer) {
                result = "WIN";
                score = 2;
                isWin = true; // 勝ち
            } else if (question > answer) {
                result = "LOSE";
                score = -1;
                isWin = false; // 負け
            } else {
                result = "DRAW";
                score = 1;
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                isWin = true; // 勝ち
            } else if (question < answer) {
                result = "LOSE";
                score = -1;
                isWin = false; // 負け
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

        // 続けて遊べるように値を更新
        setNextQuestion();
        // スコアを表示
        setScore(score);

        View bgQuestion = findViewById(R.id.question);
        View bgAnswer = findViewById(R.id.answer);

        // 勝敗に応じたアニメーションの処理
        if (result.equals("WIN")) {
            // 勝ったとき：左右の文字(question, answer)を大きくしながらアニメーション
            playResultAnimation(txtViewQuestion, true);
            playResultAnimation(txtViewAnswer, true);

            // 勝ったときだけ、背景全体（id: main）も一緒に大きく3Dアニメーションさせる！


            if (bgQuestion != null) {
                playResultAnimation(bgQuestion, true);
            }
            else if (bgAnswer != null) {
                playResultAnimation(bgAnswer, true);
            }

        } else if (result.equals("LOSE")) {
            // 負けたとき：左右の文字だけを小さくしながらアニメーション（背景は動かさない）
            playResultAnimation(txtViewQuestion, false);
            playResultAnimation(txtViewAnswer, false);


        }

    }

    private void setNextQuestion() {
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                setQuestionValue();
            }
        }.start();
    }

    private void setScore(int score) {
        TextView txtScore = (TextView) findViewById(R.id.text_score);

        // エラー防止のための数値変換処理
        String scoreText = txtScore.getText().toString();
        int currentScore = 0;
        try {
            currentScore = Integer.parseInt(scoreText);
        } catch (NumberFormatException e) {
            currentScore = 0;
        }

        int newScore = currentScore + score;
        txtScore.setText(Integer.toString(newScore));
    }

    private void clearScoreValue() {
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        txtScore.setText("0");
    }

    private void playResultAnimation(View textView, boolean isWin) {
        float distance = 8000f;
        textView.setCameraDistance(distance);

        // 1. 前半：Y軸の3D回転アニメーションしながら拡大または縮小
        ObjectAnimator rotateY = ObjectAnimator.ofFloat(textView, "rotationY", 0f, 360f);
        ObjectAnimator scaleX1;
        ObjectAnimator scaleY1;

        if (isWin) {
            scaleX1 = ObjectAnimator.ofFloat(textView, "scaleX", 1.0f, 1.8f);
            scaleY1 = ObjectAnimator.ofFloat(textView, "scaleY", 1.0f, 1.8f);
        } else {
            scaleX1 = ObjectAnimator.ofFloat(textView, "scaleX", 1.0f, 0.5f);
            scaleY1 = ObjectAnimator.ofFloat(textView, "scaleY", 1.0f, 0.5f);
        }

        // 2. 後半：変化したサイズから1.0（元のサイズ）に戻すアニメーション
        ObjectAnimator scaleX2 = ObjectAnimator.ofFloat(textView, "scaleX", isWin ? 1.8f : 0.5f, 1.0f);
        ObjectAnimator scaleY2 = ObjectAnimator.ofFloat(textView, "scaleY", isWin ? 1.8f : 0.5f, 1.0f);

        AnimatorSet firstSet = new AnimatorSet();
        firstSet.playTogether(rotateY, scaleX1, scaleY1);
        firstSet.setDuration(600); // 0.6秒

        AnimatorSet secondSet = new AnimatorSet();
        secondSet.playTogether(scaleX2, scaleY2);
        secondSet.setDuration(400); // 0.4秒

        AnimatorSet totalSet = new AnimatorSet();
        totalSet.playSequentially(firstSet, secondSet);
        totalSet.start();
    }
}





