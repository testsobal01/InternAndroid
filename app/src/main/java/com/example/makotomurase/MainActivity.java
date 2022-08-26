package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
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

import static android.media.AudioManager.STREAM_MUSIC;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SoundPool sndPool;
    private int sndID;
    private int sndID2;
    private int sndID3;
    private int sndID4;
    private int sndID5;


    private int oto;
    private int oto2;
    private int oto3;
    private int oto4;
    private int oto5;



    public MainActivity() {
    }
    
    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*sndPool = new SoundPool(1,AudioManager.STREAM_MUSIC,0);
        sndID = sndPool.load(this.getApplicationContext(),R.raw.hit,1);*/



        Button btn1 = (Button) findViewById(R.id.button1);
        btn1.setOnClickListener(this);

        Button btn2 = (Button) findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(this);
        sndPool = null;
        AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).setContentType(AudioAttributes.CONTENT_TYPE_SPEECH).build();
        sndPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(1).build();
        oto = getResources().getIdentifier("hit", "raw", getPackageName());
        oto2 = getResources().getIdentifier("over", "raw", getPackageName());
        oto3 = getResources().getIdentifier("draw", "raw", getPackageName());
        oto4 = getResources().getIdentifier("restart", "raw", getPackageName());
        sndID = sndPool.load(getBaseContext(), oto, 1);
        sndID2 = sndPool.load(getBaseContext(), oto2, 1);
        sndID3 = sndPool.load(getBaseContext(), oto3, 1);
        sndID4 = sndPool.load(getBaseContext(), oto4, 1);



        ImageView imageView2 = findViewById(R.id.droid);
        imageView2.setImageResource(R.drawable.droid);


        pref  = getSharedPreferences("AndroidSeminor", MODE_PRIVATE);
        prefEditor = pref.edit();
        // 起動時に関数を呼び出す;


        setQuestionValue();


    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();

        TextView textView = (TextView)findViewById(R.id.text_score);
        prefEditor.putString("main_input",textView.getText().toString());
        prefEditor.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this,"onResume",Toast.LENGTH_SHORT).show();


        TextView textView = (TextView)findViewById(R.id.text_score);

       String readText = pref.getString("main_input","0");
        textView.setText(readText);
    }

   //@Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.button1:
                setAnswerValue();
                checkResult(true);
                break;
            case R.id.button2:
                setAnswerValue();
                checkResult(false);
                break;
            case R.id.button3:
                setQuestionValue();
                clearAnswerValue();
                break;


        }

    }

    private void clearAnswerValue() {
        sndPool.play(sndID4, 1f, 1f, 0, 0, 1);
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText(getResources().getString(R.string.action_settings6));
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
                sndPool.play(sndID, 1f, 1f, 0, 0, 1);
                result = "WIN";
                score = 2;
            } else if (question > answer) {
                Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(500);
                sndPool.play(sndID2, 1f, 1f, 0, 0, 1);
                result = "LOSE";
                if(question-answer==10){

                }
                score = -1;
            } else {
                sndPool.play(sndID3, 1f, 1f, 0, 0, 1);
                result = getResources().getString(R.string.action_settings8);
                score = 2;
            } else if (question > answer) {

                result = getResources().getString(R.string.action_settings9);

                Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(500);

                score = -1;
            } else {
                result = getResources().getString(R.string.action_settings10);
                score = 1;
            }
        } else {
            if (question > answer) {
                sndPool.play(sndID, 1f, 1f, 0, 0, 1);
                result = "WIN";
                score = 2;
            } else if (question < answer) {
                Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(500);
                sndPool.play(sndID2, 1f, 1f, 0, 0, 1);
                result = "LOSE";
                score = -1;
            } else {
                sndPool.play(sndID3, 1f, 1f, 0, 0, 1);
                result = getResources().getString(R.string.action_settings8);
                score = 2;
            } else if (question < answer) {

                result = getResources().getString(R.string.action_settings9);

                Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(500);

                score = -1;
            } else {
                result = getResources().getString(R.string.action_settings10);
                score = 1;
            }
        }


        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText(getResources().getString(R.string.action_settings7) + question + ":" + answer + "(" + result + ")");

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