package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SoundPlayer soundPlayer;
  
    // プリファレンスとプレファレンスの編集クラスの定義
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

        //　バイブレーション機能の追加
        Vibrator vib0 = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        vib0.vibrate(500);
      
        // プリファレンスの生成
        pref = getSharedPreferences("AndroidIntern", MODE_PRIVATE);
        prefEditor = pref.edit();

        //フッター画像の挿入（1つ目）
        ImageView imageView1 =findViewById(R.id.footer1);
        imageView1.setImageResource(R.drawable.footer02);

        //フッター画像の挿入（2つ目）
        ImageView imageView2 =findViewById(R.id.footer2);
        imageView2.setImageResource(R.drawable.footer02);

        //フッター画像の挿入（3つ目）
        ImageView imageView3 =findViewById(R.id.footer3);
        imageView3.setImageResource(R.drawable.footer02);

        //フッター画像の挿入（4つ目）
        ImageView imageView4 =findViewById(R.id.footer4);
        imageView4.setImageResource(R.drawable.footer02);

        //フッター画像の挿入（5つ目）
        ImageView imageView5 =findViewById(R.id.footer5);
        imageView5.setImageResource(R.drawable.footer02);


        // 起動時に関数を呼び出す
        setQuestionValue();

        soundPlayer = new SoundPlayer(this);

    }

    @Override
    protected void onPause() {
        super.onPause();

        // Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
        // プリファレンスの保存
        TextView scoreTextView = (TextView) findViewById(R.id.text_score);
        prefEditor.putString("score", scoreTextView.getText().toString());
        prefEditor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // プリファレンスの読み込み
        TextView scoreTextView = (TextView) findViewById(R.id.text_score);
        String readScoreText = pref.getString("score", "0");
        scoreTextView.setText(readScoreText);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.button1:
                //　バイブレーション機能の追加
                Vibrator vib1 = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib1.vibrate(150);

                setAnswerValue();
                checkResult(true);
                soundPlayer.playHitSound();
                break;

            case R.id.button2:
                //　バイブレーション機能の追加
                Vibrator vib2 = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib2.vibrate(150);

                setAnswerValue();
                checkResult(false);
                soundPlayer.playOverSound();
                break;

            case R.id.button3:
                //　バイブレーション機能の追加
                Vibrator vib3 = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib3.vibrate(150);

                setQuestionValue();
                clearAnswerValue();
                soundPlayer.playResetSound();
                break;
        }


    }

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText(getString(R.string.num2));
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

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = getString(R.string.WIN);
                score = 2;
            } else if (question > answer) {
                result = getString(R.string.LOSE);
                score = -1;
            } else {
                result = getString(R.string.DRAW);
                score = 1;
            }
        } else {
            if (question > answer) {
                result = getString(R.string.WIN);
                score = 2;
            } else if (question < answer) {
                result = getString(R.string.LOSE);
                score = -1;
            } else {
                result = getString(R.string.DRAW);
                score = 1;
            }
        }

        switch (score){
            case 2:
                txtViewAnswer.setBackgroundColor(Color.RED);
                txtViewQuestion.setBackgroundColor(Color.BLUE);
                break;
            case -1:
                txtViewAnswer.setText(txtViewAnswer.getText().toString());
                txtViewAnswer.setBackgroundColor(Color.BLUE);
                txtViewQuestion.setBackgroundColor(Color.RED);
                break;
            case 1:
                txtViewAnswer.setText(txtViewAnswer.getText().toString());
                txtViewAnswer.setBackgroundColor(Color.YELLOW);
                txtViewQuestion.setBackgroundColor(Color.YELLOW);
        }

        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText(getString(R.string.score) + question + ":" + answer + "(" + result + ")");

        // 続けて遊べるように値を更新
        setNextQuestion();

        // スコアを表示
        setScore(score);

        TextView txtScore = (TextView) findViewById(R.id.text_score);
        int scoreValue = Integer.parseInt(txtScore.getText().toString());
        if (scoreValue >= 10) {
            soundPlayer.playCelebSound();
        }

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

}