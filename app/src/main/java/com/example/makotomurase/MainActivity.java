package com.example.makotomurase;



import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.SharedPreferences;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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

        pref = getSharedPreferences("AndroidPreference", MODE_PRIVATE);
        prefEditor = pref.edit();


        TextView textView=(TextView)findViewById(R.id.text_score);
        String savedScore = pref.getString("main_input", "0");
        textView.setText(savedScore);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, windowInsets) -> {
            Insets insets = windowInsets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
                            | WindowInsetsCompat.Type.displayCutout());
            view.setPadding(insets.left, insets.top, insets.right, 0);
            return windowInsets;
        });

        //region 各button
        Button btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(this);

        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(this);

        //endregion

        // 起動時に関数を呼び出す
        setQuestionValue();
    }


    //region 各button処理
    @Override
    public void onClick(View view) {
        vibration();
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
    //endregion
  
      @Override

    protected void onPause(){
        super.onPause();

        Toast.makeText(this,"onPuse",Toast.LENGTH_SHORT).show();

        TextView textView=(TextView)findViewById(R.id.text_score);

        prefEditor.putString("main_input",textView.getText().toString());
        prefEditor.commit();
    }

    private void clearAnswerValue() { //
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText("値2");

        //背景色の定義と変更
        TextView txtViewQuestion = findViewById(R.id.question);
        TextView txtViewAnswer = findViewById(R.id.answer);
        txtViewAnswer.setBackgroundColor(Color.parseColor("#ffff00"));//黄色
        txtViewQuestion.setBackgroundColor(Color.parseColor("#ff00ff"));//ピンク

    } // answerリセットメソッド

    private void setQuestionValue() {
        Random r = new Random();
        // 0から10の範囲で乱数を生成（+1する必要がある）
        int questionValue = r.nextInt(10 + 1);

        TextView txtView = findViewById(R.id.question);
        txtView.setText(Integer.toString(questionValue));
    } // questionリセットメソッド

    private void setAnswerValue() { //
        Random r = new Random();
        int answerValue = r.nextInt(10 + 1);

        TextView txtView = findViewById(R.id.answer);
        txtView.setText(Integer.toString(answerValue));
    } // answer乱数生成メソッド

    private void checkResult(boolean isHigh) { //
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
                txtViewAnswer.setBackgroundColor(Color.parseColor("#ffff00"));//黄色
                txtViewQuestion.setBackgroundColor(Color.parseColor("#a611a6"));//暗いピンク
               // txtViewQuestion.setBackgroundColor(Color.parseColor("#e80ce8"));//暗いピンク

            } else if (question > answer) {
                result = "LOSE";
                score = -1;
                txtViewQuestion.setBackgroundColor(Color.parseColor("#ff00ff"));//ピンク
                txtViewAnswer.setBackgroundColor(Color.parseColor("#999923"));//暗い黄色
            } else {
                result = "DRAW";
                score = 1;
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                txtViewAnswer.setBackgroundColor(Color.parseColor("#ffff00"));//黄色
                txtViewQuestion.setBackgroundColor(Color.parseColor("#a611a6"));//暗いピンク
            } else if (question < answer) {
                result = "LOSE";
                score = -1;
                txtViewQuestion.setBackgroundColor(Color.parseColor("#ff00ff"));//ピンク
                txtViewAnswer.setBackgroundColor(Color.parseColor("#999923"));//暗い黄色
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
    } // 勝敗表示メソッド

    //region リセットタイマーメソッド
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
    //endregion

    private void setScore(int score) { //
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        int newScore = Integer.parseInt(txtScore.getText().toString()) + score;
        txtScore.setText(Integer.toString(newScore));
    } // スコア表示メソッド

    private void clearScoreValue() { //
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        txtScore.setText("0");
    }// スコアリセットメソッド

    private void vibration() { //
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(VibrationEffect.createOneShot(
                500, VibrationEffect.DEFAULT_AMPLITUDE));
    }// バイブレーションメソッド（項目4）
  
}

