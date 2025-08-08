package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * score用のpreference
     */
    SharedPreferences pref;
    SharedPreferences.Editor preEditer;

    /**
     * SEを鳴らす用
     */
    private AudioPlayer audioPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Bundle extra = intent.getExtras();
        String intentString = extra.getString("KEY");

        audioPlayer= new AudioPlayer(this);// audioPlayerのインスタンスを作成


        Button btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(this);

        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(this);

        //preferenceのインスタンスを生成
        pref = getSharedPreferences("AndroidSeminor",MODE_PRIVATE);
        preEditer = pref.edit();

        // 起動時に関数を呼び出す
        setQuestionValue();
    }


    @Override
    protected void onPause() {
        super.onPause();

        // スコアの保存
        TextView textScore = (TextView) findViewById(R.id.text_score);
        int nowScore = Integer.parseInt(textScore.getText().toString()); // 現時点のスコアを入手
        preEditer.putInt("score",nowScore);// スコアを保存

        // 最高スコアの保存
        TextView textMaxScore = (TextView) findViewById(R.id.max_score);
        int maxScore = Integer.parseInt(textMaxScore.getText().toString()); // 現時点の最高スコアを入手
        preEditer.putInt("max_score",maxScore);// 最高スコアを保存

        preEditer.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // もともとのスコアを呼び出す
        TextView textScore = (TextView) findViewById(R.id.text_score);
        textScore.setText(String.valueOf(pref.getInt("score",0)));

        // もともとの最高スコアを呼び出す
        TextView textMaxScore = (TextView) findViewById(R.id.max_score);
        textMaxScore.setText(String.valueOf(pref.getInt("max_score",0)));
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

            TextView txtViewAnswer = findViewById(R.id.answer);
            txtViewAnswer.setBackgroundColor(Color.rgb(255,255,0));

            audioPlayer.playPushButtonSE();// SEを鳴らす
        }
    }

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText(getResources().getString(R.string.value));
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
                result = getResources().getString(R.string.w_result);
                score = 2;
                txtViewAnswer.setBackgroundColor(Color.rgb(255,69,0));
            } else if (question > answer) {
                result = getResources().getString(R.string.l_result);
                score = -1;
                txtViewAnswer.setBackgroundColor(Color.rgb(65,105,225));
            } else {
                result = getResources().getString(R.string.d_result);
                score = 1;
                txtViewAnswer.setBackgroundColor(Color.rgb(220,220,220));
            }
        } else {
            if (question > answer) {
                result = getResources().getString(R.string.w_result);
                score = 2;
                txtViewAnswer.setBackgroundColor(Color.rgb(255,69,0));
            } else if (question < answer) {
                result = getResources().getString(R.string.l_result);
                score = -1;
                txtViewAnswer.setBackgroundColor(Color.rgb(65,105,225));
            } else {
                result = getResources().getString(R.string.d_result);
                score = 1;
                txtViewAnswer.setBackgroundColor(Color.rgb(220,220,220));
            }
        }

        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        audioPlayer.playResultSE(result);// SEを鳴らす
        txtResult.setText(getResources().getString(R.string.result)+ question + ":" + answer + "(" + result + ")");
        valueAnimation(result);

        // 続けて遊べるように値を更新
        setNextQuestion();
        // スコアを表示
        setScore(score);
    }

    /**
     * 結果に応じて数値をアニメーションする
     * @param result 結果
     */
    private void valueAnimation (String result){
        /** アニメーションの対象となるtextview */
        TextView target;
        /** animationを定義したxmlファイルの読み込み */
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.value_animater);

        if(result.equals("WIN")){
            target = (TextView) findViewById(R.id.answer);
        }else if(result.equals("LOSE")){
            target = (TextView) findViewById(R.id.question);
        }else{
            return;
        }

        //TODO: 一通り終わったらこだわる。背景色も回転してしまうので時間があれば修正
        target.startAnimation(animation); //アニメーションを実行

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

        /**
         * 最高スコアの更新
         */
        TextView txtMaxScore = (TextView) findViewById(R.id.max_score);
        int maxScore = Integer.parseInt(txtMaxScore.getText().toString());
        if(maxScore<newScore) maxScore = newScore;
        txtMaxScore.setText(Integer.toString(maxScore));
    }

    private void clearScoreValue() {
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        txtScore.setText("0");
    }
}

