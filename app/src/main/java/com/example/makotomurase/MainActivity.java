package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.SoundPool;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SoundPool sndPool;
    private int snd1, snd2, snd3, snd4;
    private boolean isCountdown = false;
    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

    private int total=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(this);
        btn1.setBackground(getResources().getDrawable(R.drawable.my_button, null));


        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(this);

        sndPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        snd1 = sndPool.load(this, R.raw.win, 1);
        snd2 = sndPool.load(this, R.raw.lose, 1);
        snd3 = sndPool.load(this, R.raw.restart, 1);
        snd4 = sndPool.load(this, R.raw.draw, 1);

        // 起動時に関数を呼び出す
        setQuestionValue();

        pref = getSharedPreferences("AndroidSeminor", MODE_PRIVATE);
        prefEditor = pref.edit();
    }

    @Override
    protected void onPause() {
        super.onPause();

        //プリファレンスにスコア保存
        TextView textView = (TextView)findViewById(R.id.text_score);
        prefEditor.putString("score_input", textView.getText().toString());
        prefEditor.commit();

        //プリファレンスにハイスコア保存
        TextView textView2 = (TextView)findViewById(R.id.text_high_score);
        prefEditor.putString("high_score_input", textView2.getText().toString());
        prefEditor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //プリファレンスからスコア読み込み
        TextView textView = (TextView) findViewById(R.id.text_score);
        String readText = pref.getString("score_input", "0");
        textView.setText(readText);

        //プリファレンスからハイスコア読み込み
        TextView textView2 = (TextView) findViewById(R.id.text_high_score);
        String readText2 = pref.getString("high_score_input", "0");
        textView2.setText(readText2);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button1 && !isCountdown) {
            setAnswerValue();
            checkResult(true);
        } else if (id == R.id.button2 && !isCountdown) {
            setAnswerValue();
            checkResult(false);
        } else if (id == R.id.button3) {
            sndPool.play(snd3, 1.0f, 1.0f, 0, 0, 1);
            total=0;
            WinScore();
            setQuestionValue();
            clearAnswerValue();
            clearScoreValue();
            RestartVibrate();
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
                total++;
                WinScore();
                sndPool.play(snd1, 1.0f, 1.0f, 0, 0, 1);
            } else if (question > answer) {
                result = "LOSE";
                score = -1;
                total=0;
                WinScore();
                sndPool.play(snd2, 1.0f, 1.0f, 0, 0, 1);
                LoseVibrate();
            } else {
                result = "DRAW";
                score = 1;
                sndPool.play(snd4, 1.0f, 1.0f, 0, 0, 1);
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                total++;
                WinScore();
                sndPool.play(snd1, 1.0f, 1.0f, 0, 0, 1);
            } else if (question < answer) {
                result = "LOSE";
                score = -1;
                total=0;
                WinScore();
                sndPool.play(snd2, 1.0f, 1.0f, 0, 0, 1);
                LoseVibrate();
            } else {
                result = "DRAW";
                score = 1;
                sndPool.play(snd4, 1.0f, 1.0f, 0, 0, 1);
            }

        }

        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        String Result = getString(R.string.Result);
        txtResult.setText(Result +":" + question + ":" + answer + "(" + result + ")");

        // 続けて遊べるように値を更新
        setNextQuestion();
        // スコアを表示
        setScore(score);
    }

    private void setNextQuestion() {
        // 第１引数がカウントダウン時間、第２引数は途中経過を受け取る間隔
        // 単位はミリ秒（1秒＝1000ミリ秒）
        new CountDownTimer(3000, 500) {
        isCountdown = true;

            @Override
            public void onTick(long l) {
                //カウントダウン
                TextView txtTime = (TextView) findViewById(R.id.text_countdown);
                txtTime.setText(Long.toString(l / 1000 + 1));
            }

            @Override
            public void onFinish() {
                // 3秒経過したら次の値をセット
                setQuestionValue();
                //カウントダウン非表示
                TextView txtTime = (TextView) findViewById(R.id.text_countdown);
                txtTime.setText("");
                isCountdown = false;
            }
        }.start();
    }

    private void setScore(int score) {
        //スコア計算
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        int newScore = Integer.parseInt(txtScore.getText().toString()) + score;
        txtScore.setText(Integer.toString(newScore));

        //ハイスコア計算
        TextView txtHighScore = (TextView) findViewById(R.id.text_high_score);
        int newHighScore = Math.max(Integer.parseInt(txtHighScore.getText().toString()), newScore);
        txtHighScore.setText(Integer.toString(newHighScore));
    }

    private void clearScoreValue() {
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        txtScore.setText("0");
    }
  
    private void LoseVibrate() {
        Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vib.vibrate(300);
    }

    private void RestartVibrate() {
        Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vib.vibrate(100);
    }

    private void WinScore() {
        TextView MaxWin = (TextView) findViewById(R.id.Max_Win);
        MaxWin.setText("連勝数：" +total+"  ");
    }
}
