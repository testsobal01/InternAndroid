package com.example.makotomurase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.media.AudioAttributes;
import android.media.SoundPool;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.content.ClipData;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;

import android.content.res.Configuration;

import android.view.Menu;
import android.view.MenuItem;



import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    int soundWin;
    int soundLose;
    int soundDraw;
    SoundPool soundPool;
    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;
    Menu menu;

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

        // 起動時に関数を呼び出す
        setQuestionValue();

        setSoundEffects();

        pref=getSharedPreferences("main_input",MODE_PRIVATE);
        prefEditor=pref.edit();
        String str=pref.getString("main_input","保存されてません");
        if (!str.equals("保存されてません")) {
            TextView txtScore = (TextView) findViewById(R.id.text_score);

            txtScore.setText(str);
        }

        Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        vib.vibrate(50);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.language_menu,menu);
        this.menu = menu;
        return true;
    }
    

    private void setLocate(String language){
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config,null);
        TextView textResult = (TextView)findViewById(R.id.result);
        TextView textScore = (TextView)findViewById(R.id.score);
        TextView textExplain = (TextView)findViewById(R.id.explain);
        Button button1 = (Button)findViewById(R.id.button1);
        Button button2 = (Button)findViewById(R.id.button2);
        Button button3 = (Button)findViewById(R.id.button3);
        MenuItem ja_menu = menu.findItem(R.id.ja_setting);
        MenuItem en_menu = menu.findItem(R.id.en_setting);
        textResult.setText(getString(R.string.result));
        textScore.setText(getString(R.string.label_score));
        textExplain.setText(getString(R.string.explain));
        button1.setText(getString(R.string.btn_high));
        button2.setText(getString(R.string.btn_low));
        button3.setText(getString(R.string.btn_restart));
        ja_menu.setTitle(getString(R.string.ja_setting));
        en_menu.setTitle(getString(R.string.en_setting));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.ja_setting:
                setLocate("ja");
                return true;
            case R.id.en_setting:
                setLocate("en");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.button1:
                setAnswerValue();
                checkResult(true);
                Vibrator vib1 = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib1.vibrate(100);
                break;
            case R.id.button2:
                setAnswerValue();
                checkResult(false);
                Vibrator vib2 = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib2.vibrate(100);
                break;
            case R.id.button3:
                setQuestionValue();
                clearAnswerValue();

                Vibrator vib3 = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib3.vibrate(100);

                TextView txt1=(TextView)findViewById(R.id.question);
                TextView txt2=(TextView)findViewById(R.id.answer);
                txt1.setBackgroundColor(Color.MAGENTA);
                txt2.setBackgroundColor(Color.YELLOW);
                txt1.setTextColor(Color.BLACK);
                txt2.setTextColor(Color.BLACK);

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
                TextView txt1=(TextView)findViewById(R.id.question);
                TextView txt2=(TextView)findViewById(R.id.answer);
                txt1.setBackgroundColor(Color.BLUE);
                txt2.setBackgroundColor(Color.YELLOW);
                txt1.setTextColor(Color.BLACK);
                txt2.setTextColor(Color.GREEN);
                result = "WIN";
                score = 2;

            } else if (question > answer) {
                TextView txt1=(TextView)findViewById(R.id.question);
                TextView txt2=(TextView)findViewById(R.id.answer);
                txt1.setBackgroundColor(Color.MAGENTA);
                txt2.setBackgroundColor(Color.BLUE);
                txt2.setTextColor(Color.BLACK);
                txt1.setTextColor(Color.GREEN);
                result = "LOSE";
                score = -1;
            } else {
                TextView txt1=(TextView)findViewById(R.id.question);
                TextView txt2=(TextView)findViewById(R.id.answer);
                txt2.setTextColor(Color.BLACK);
                txt1.setTextColor(Color.BLACK);
                result = "DRAW";
                score = 1;
            }
        } else {
            if (question > answer) {
                TextView txt1=(TextView)findViewById(R.id.question);
                TextView txt2=(TextView)findViewById(R.id.answer);
                txt1.setBackgroundColor(Color.MAGENTA);
                txt2.setBackgroundColor(Color.BLUE);
                txt2.setTextColor(Color.BLACK);
                txt1.setTextColor(Color.GREEN);
                result = "WIN";
                score = 2;
            } else if (question < answer) {
                TextView txt1=(TextView)findViewById(R.id.question);
                TextView txt2=(TextView)findViewById(R.id.answer);
                txt1.setBackgroundColor(Color.BLUE);
                txt2.setBackgroundColor(Color.YELLOW);
                txt2.setTextColor(Color.GREEN);
                txt1.setTextColor(Color.BLACK);
                result = "LOSE";
                score = -1;
            } else {
                TextView txt1=(TextView)findViewById(R.id.question);
                TextView txt2=(TextView)findViewById(R.id.answer);
                txt2.setTextColor(Color.BLACK);
                txt1.setTextColor(Color.BLACK);
                result = "DRAW";
                score = 1;
            }
        }


        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        ringSoundEffects(result);
        txtResult.setText(question + ":" + answer + "(" + result + ")");

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

    private void setSoundEffects(){
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(1)
                .build();

        soundWin = soundPool.load(getApplicationContext(),R.raw.correct,1);
        soundLose = soundPool.load(getApplicationContext(),R.raw.incorrect,1);
        soundDraw = soundPool.load(getApplicationContext(),R.raw.draw,1);
    }

    private void ringSoundEffects(String result){
        if(result.equals("WIN")){
            soundPool.play(soundWin,0.5f,0.5f,1,0,1);
        }else if(result.equals("LOSE")){
            soundPool.play(soundLose,0.5f,0.5f,1,0,1);
        }else if(result.equals("DRAW")){
            soundPool.play(soundDraw,0.5f,0.5f,1,0,1);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this,"onPause",Toast.LENGTH_SHORT).show();

        TextView textView=(TextView)findViewById(R.id.text_score);
        prefEditor.putString("main_input",textView.getText().toString());
        prefEditor.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this,"onResume",Toast.LENGTH_SHORT).show();
        TextView textView=(TextView)findViewById(R.id.text_score);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this,"onDestroy",Toast.LENGTH_SHORT).show();
    }
}