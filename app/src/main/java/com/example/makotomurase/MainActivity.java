package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;
    SoundPool soundPool;
    int soundId;

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

        pref=getSharedPreferences("team_b",MODE_PRIVATE);
        prefEditor=pref.edit();

        // 起動時に関数を呼び出す
        setQuestionValue();

        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        soundId = soundPool.load(this, R.raw.sound, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Toast.makeText(this,"onPause",Toast.LENGTH_SHORT).show();
        TextView score=(TextView)findViewById(R.id.text_score);

        prefEditor.putString("score",score.getText().toString());
        prefEditor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        TextView score=(TextView)findViewById(R.id.text_score);
        String readText=pref.getString("score","0");
        score.setText(readText);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.button1:
                setAnswerValue();
                checkResult(true);

                Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib. vibrate(70);
                soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);

                break;
            case R.id.button2:
                setAnswerValue();
                checkResult(false);

                Vibrator vib2 = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib2. vibrate(70);

                soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
                break;
            case R.id.button3:
                setQuestionValue();
                clearAnswerValue();

                Vibrator vib3 = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib3.vibrate(70);
                break;

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
                result = "WIN";
                score = 2;
                TextView txtView = (TextView) findViewById(R.id.question);
                txtView.setBackgroundColor(Color.parseColor("#ff0000"));

            } else if (question > answer) {
                result = "LOSE";
                score = -1;
                TextView txtView = (TextView) findViewById(R.id.question);
                txtView.setBackgroundColor(Color.parseColor("#0000ff"));

            } else {
                result = "DRAW";
                score = 1;
                TextView txtView = (TextView) findViewById(R.id.question);
                txtView.setBackgroundColor(Color.parseColor("#7fff00"));
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                TextView txtView = (TextView) findViewById(R.id.answer);
                txtView.setBackgroundColor(Color.parseColor("#ff0000"));

            } else if (question < answer) {
                result = "LOSE";
                score = -1;
                TextView txtView = (TextView) findViewById(R.id.answer);
                txtView.setBackgroundColor(Color.parseColor("#0000ff"));

            } else {
                result = "DRAW";
                score = 1;
                TextView txtView = (TextView) findViewById(R.id.answer);
                txtView.setBackgroundColor(Color.parseColor("#7fff00"));
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
            }
        }.start();
    }

    private void setScore(int score) {
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        int newScore = Integer.parseInt(txtScore.getText().toString()) + score;
        txtScore.setText(Integer.toString(newScore));
    }



}