package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.BreakIterator;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

    private int sound;
    private  SoundPool soundPool;
  
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

        AudioAttributes audioAttributes=new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_UNKNOWN)
                .build();

        soundPool=new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(1)
                .build();

        sound=soundPool.load(this,R.raw.button,1);

        // 起動時に関数を呼び出す
        setQuestionValue();

        pref = getSharedPreferences("score_input", MODE_PRIVATE);
        prefEditor = pref.edit();

    }


    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();

        TextView textView = (TextView) findViewById(R.id.text_score);
        String readText = pref.getString("score_input", "0");
        textView.setText(readText);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.button1:
                setAnswerValue();
                checkResult(true);
                Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(5000);
                soundPool.play(sound,1.0f,1.0f,0,0,1);
                break;

            case R.id.button2:
                setAnswerValue();
                checkResult(false);
                Vibrator vib2 = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vib2.vibrate(5000);
                soundPool.play(sound,1.0f,1.0f,0,0,1);
                break;

            case R.id.button3:
                setQuestionValue();
                clearAnswerValue();
                Vibrator vib3 = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vib3.vibrate(5000);
                soundPool.play(sound,1.0f,1.0f,0,0,1);
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
      
        View view1 =(View) findViewById(R.id.question);
        View view2 =(View) findViewById(R.id.answer);
      
        String resultText = getString(R.string.label_result);//結果テキストを変数に代入
        String winText = getString(R.string.label_win);
        String loseText = getString(R.string.label_lose);
        String drawText = getString(R.string.label_draw);
      

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = winText;
                score = 2;
                view1.setBackgroundColor(Color.parseColor("#0000ff"));
                view2.setBackgroundColor(Color.parseColor("#ff0000"));
            } else if (question > answer) {
                result = loseText;
                score = -1;
                view1.setBackgroundColor(Color.parseColor("#ff0000"));
                view2.setBackgroundColor(Color.parseColor("#0000ff"));
            } else {
                result = drawText;
                score = 1;
                view1.setBackgroundColor(Color.parseColor("#ff00ff"));
                view2.setBackgroundColor(Color.parseColor("#ffff00"));
            }
        } else {
            if (question > answer) {
                result = winText;
                score = 2;
                view1.setBackgroundColor(Color.parseColor("#0000ff"));
                view2.setBackgroundColor(Color.parseColor("#ff0000"));
            } else if (question < answer) {
                result = loseText;
                score = -1;
                view1.setBackgroundColor(Color.parseColor("#ff0000"));
                view2.setBackgroundColor(Color.parseColor("#0000ff"));
            } else {
                result = drawText;
                score = 1;
                view1.setBackgroundColor(Color.parseColor("#ff00ff"));
                view2.setBackgroundColor(Color.parseColor("#ffff00"));
            }
        }


        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText(resultText + question + ":" + answer + "(" + result + ")");

        // 続けて遊べるように値を更新
        setNextQuestion();

        // スコアを表示
        setScore(score);

    }

    private void setScore(int score) {
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        int newScore = Integer.parseInt(txtScore.getText().toString()) + score;
        txtScore.setText(Integer.toString(newScore));
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

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();

        TextView textView = (TextView) findViewById(R.id.text_score);

        prefEditor.putString("score_input", textView.getText().toString());
        prefEditor.commit();
    }
}


