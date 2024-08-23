package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
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
                changeBackgroundWin();
                startAnimation1_Win(findViewById(R.id.question));
                startAnimation2(findViewById(R.id.answer));
            } else if (question > answer) {
                result = "LOSE";
                score = -1;
                changeBackGroundLose();
                startAnimation1_Lose(findViewById(R.id.answer));
                startAnimation2(findViewById(R.id.question));
            } else {
                result = "DRAW";
                score = 1;
                changeBackGroundDraw();
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                changeBackgroundWin();
                startAnimation1_Win(findViewById(R.id.question));
                startAnimation2(findViewById(R.id.answer));
            } else if (question < answer) {
                result = "LOSE";
                score = -1;
                changeBackGroundLose();
                startAnimation1_Lose(findViewById(R.id.answer));
                startAnimation2(findViewById(R.id.question));
            } else {
                result = "DRAW";
                score = 1;
                changeBackGroundDraw();
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
                animationReset(findViewById(R.id.answer));
                animationReset(findViewById(R.id.question));
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

    private void changeBackgroundWin(){
        TextView qtxtScore = (TextView) findViewById(R.id.question);
        qtxtScore.setBackgroundColor(Color.RED);
        TextView atxtScore = (TextView) findViewById(R.id.answer);
        atxtScore.setBackgroundColor(Color.BLUE);
    }

    private void changeBackGroundLose(){
        TextView qtxtScore = (TextView) findViewById(R.id.question);
        qtxtScore.setBackgroundColor(Color.BLUE);
        TextView atxtScore = (TextView) findViewById(R.id.answer);
        atxtScore.setBackgroundColor(Color.RED);
    }

    private void changeBackGroundDraw(){
        TextView qtxtScore = (TextView) findViewById(R.id.question);
        qtxtScore.setBackgroundColor(Color.WHITE);
        TextView atxtScore = (TextView) findViewById(R.id.answer);
        atxtScore.setBackgroundColor(Color.WHITE);
    }

    private void startAnimation1_Lose(View view) {
        ObjectAnimator transY, transX, scaleX, alpha;
        List<Animator> animatorList = new ArrayList<>();

        // 移動距離(translationX)は、開始位置から左右に移動距離を100, 70, 50, 40...と徐々に減らしていく
        // 前のアニメーションと繋がるように終了位置(第4引数)の値を開始位置(第3引数)として設定する

        animatorList.add(ObjectAnimator.ofFloat(view, "translationX", 0, -1000));
        animatorList.add(ObjectAnimator.ofFloat(view, "translationX", -1000, 700));
        animatorList.add(ObjectAnimator.ofFloat(view, "translationX", 700, -500));
        animatorList.add(ObjectAnimator.ofFloat(view, "translationX", -500, 400));
        animatorList.add(ObjectAnimator.ofFloat(view, "translationX", 400, -300));
        animatorList.add(ObjectAnimator.ofFloat(view, "translationX", -300, 0f));


        AnimatorSet set = new AnimatorSet();
        set.playTogether(animatorList);// 順番にアニメーションを実施
        set.start();
    }

    private void startAnimation1_Win(View view) {
        ObjectAnimator transY, transX, scaleX, alpha;
        List<Animator> animatorList = new ArrayList<>();

        // 移動距離(translationX)は、開始位置から左右に移動距離を100, 70, 50, 40...と徐々に減らしていく
        // 前のアニメーションと繋がるように終了位置(第4引数)の値を開始位置(第3引数)として設定する

        animatorList.add(ObjectAnimator.ofFloat(view, "translationX", 0, 1000));
        animatorList.add(ObjectAnimator.ofFloat(view, "translationX", 1000, -700));
        animatorList.add(ObjectAnimator.ofFloat(view, "translationX", -700, 500));
        animatorList.add(ObjectAnimator.ofFloat(view, "translationX", 500, -400));
        animatorList.add(ObjectAnimator.ofFloat(view, "translationX", -400, 300));
        animatorList.add(ObjectAnimator.ofFloat(view, "translationX", 300, 0));


        AnimatorSet set = new AnimatorSet();
        set.playTogether(animatorList);// 順番にアニメーションを実施
        set.start();
    }

    private void startAnimation2(View view) {
        ObjectAnimator transY, scaleX, alpha;
        List<Animator> animatorList = new ArrayList<>();

        // アニメーション時間もランダムに5～10秒に演出
        int durationTY = (3) * 1000;
        int durationTX = (5) * 1000;
        int durationSX = (5) * 1000;

        // 上昇"translationY"
        transY = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y.getName(), 0.0f, -1000.0f);
        transY.setDuration(durationTY);
        animatorList.add(transY);


        // 幅の伸び縮み"scaleX"
        scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X.getName(), 1.0f, 0.1f, 1.0f, 0.1f, 1.0f, 0.1f, 1.0f);
        scaleX.setDuration(durationSX);
        animatorList.add(scaleX);

        // 透明化"alpha"
        alpha = ObjectAnimator.ofFloat(view, View.ALPHA.getName(), 1.0f, 0.0f);
        alpha.setDuration(Math.max(Math.max(durationTY, durationTX), durationSX) / 2);
        animatorList.add(alpha);

        // アニメーションの複合化、再生
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animatorList);
        set.start();
    }

    public void animationReset(View view){
        ObjectAnimator transY, alpha0;
        List<Animator> animatorList0 = new ArrayList<>();

        int durationTY = (2) * 1000;

        transY = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y.getName(), 0.0f, 0.0f);
        transY.setDuration(durationTY);
        animatorList0.a

        alpha0 = ObjectAnimator.ofFloat(view, View.ALPHA.getName(), 0.0f, 1.0f);
        animatorList0.add(alpha0);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(animatorList0);
        set.start();
    }
}

