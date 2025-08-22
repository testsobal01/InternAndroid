package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
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

    int mysoundID;
    SoundPool soundPool;
    int hit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button botton1 = findViewById(R.id.button1);
        botton1.setOnClickListener(this);
        soundPool = null;

        AudioAttributes audioAttributes=new
                AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).setContentType(AudioAttributes.CONTENT_TYPE_SPEECH).build();

        soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(1).build();

        hit = getResources().getIdentifier("hit","raw",getPackageName());

        mysoundID=soundPool.load(getBaseContext(),hit,1);

        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(this);
        pref=getSharedPreferences("AndroidSeminor",MODE_PRIVATE);
        prefEditor=pref.edit();


        TextView txtsetting = (TextView) findViewById(R.id.text_setting);
        String title = getString(R.string.setting_score);
        txtsetting.setText(title);

        TextView txtView = (TextView) findViewById(R.id.answer);
        String title2 = getString(R.string.setting_score2);
        txtView.setText(title2);




        pref=getSharedPreferences("AndroidSeminor",MODE_PRIVATE);
        prefEditor=pref.edit();

        // 起動時に関数を呼び出す
        setQuestionValue();
        TextView inresultview=(TextView) findViewById(R.id.text_score);
        String readText=pref.getString("score_input","保存されていません");
        inresultview.setText(readText);
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

        if(view.getId() == R.id.button1) {
            Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vib.vibrate(1000);
        } else if (id == R.id.button2) {
            Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vib.vibrate(1000);
        }else if (id == R.id.button3) {
            Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vib.vibrate(2000);
        }
        if (id == R.id.button1) {
            soundPool.play(mysoundID,1f,1f,0,0,1);
        } else if (id == R.id.button2) {
            soundPool.play(mysoundID,1f,1f,0,0,1);
        } else if (id == R.id.button3) {
            soundPool.play(mysoundID, 1f, 1f, 0, 0, 1);
        }
    }

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        String title2 = getString(R.string.setting_score2);
        txtView.setText(title2);
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

//        // 結果を示す文字列を入れる変数を用意
        String result;
        int score;
        int winColor = Color.RED;
        int loseColor = Color.CYAN;
        int drawColor = Color.GREEN;

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = "WIN";
                score = 2;
                txtViewQuestion.setBackgroundColor(winColor);
                txtViewAnswer.setBackgroundColor(winColor);

            } else if (question > answer) {
                result = "LOSE";
                score = -1;
                txtViewQuestion.setBackgroundColor(loseColor);
                txtViewAnswer.setBackgroundColor(loseColor);

            } else {
                result = "DRAW";
                score = 1;
                txtViewQuestion.setBackgroundColor(drawColor);
                txtViewAnswer.setBackgroundColor(drawColor);
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                txtViewQuestion.setBackgroundColor(winColor);
                txtViewAnswer.setBackgroundColor(winColor);
            } else if (question < answer) {
                result = "LOSE";
                score = -1;
                txtViewQuestion.setBackgroundColor(loseColor);
                txtViewAnswer.setBackgroundColor(loseColor);
            } else {
                result = "DRAW";
                score = 1;
                txtViewQuestion.setBackgroundColor(drawColor);
                txtViewAnswer.setBackgroundColor(drawColor);
            }
        }

        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        String title3 = getString(R.string.setting_score3);

        txtResult.setText(title3 + question + ":" + answer + "(" + result + "))");
//        txtResult.setText("結果：" + question + ":" + answer + "(" + result + ")");

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
        prefEditor.putString("score_input",txtScore.getText().toString());
        prefEditor.commit();
    }

    private void clearScoreValue() {
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        txtScore.setText("0");
    }}

