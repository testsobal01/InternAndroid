package com.example.makotomurase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import android.media.AudioAttributes;
import android.media.SoundPool;


import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;
    public int numberpickervalue=10;
    private SoundPool soundPool;
    private int soundSound;




    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }
  
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        LayoutInflater inflater = getLayoutInflater();
        android.view.View dialogView = inflater.inflate(R.layout.dialog, null);
        NumberPicker numberPicker = dialogView.findViewById(R.id.number_picker);
        numberPicker.setMinValue(10);
        numberPicker.setMaxValue(50);
        numberPicker.setValue(10);
        new AlertDialog.Builder(this)
                .setTitle("最大値を設定してください")
                .setView(dialogView)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        TextView settt = (TextView) findViewById(R.id.settt);
                        settt.setText(numberPicker.getValue()+"が設定されています。");
                        numberpickervalue = numberPicker.getValue();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();

        return true;
    }

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

        pref = getSharedPreferences("AndroidSeminor",MODE_PRIVATE);
        prefEditor = pref.edit();
        // 起動時に関数を呼び出す
        setQuestionValue();

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                // USAGE_MEDIA
                // USAGE_GAME
                .setUsage(AudioAttributes.USAGE_GAME)
                // CONTENT_TYPE_MUSIC
                // CONTENT_TYPE_SPEECH, etc.
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build();

        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                // ストリーム数に応じて
                .setMaxStreams(2)
                .build();

        soundSound = soundPool.load(this, R.raw.sound, 1);

        soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
            Log.d("debug","sampleId="+sampleId);
            Log.d("debug","status="+status);
        });
    }

    @Override
    protected void onPause(){
        super.onPause();
        Toast.makeText(this,"onPause",Toast.LENGTH_SHORT).show();
        TextView textView = findViewById(R.id.text_score);
        prefEditor.putString("main_input",textView.getText().toString());
        prefEditor.commit();
    }
    @Override
    protected void onResume(){
        super.onResume();
        Toast.makeText(this,"onResume",Toast.LENGTH_SHORT).show();
        TextView textView = findViewById(R.id.text_score);
        String readText = pref.getString("main_input","0");
        textView.setText(readText);
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button1) {
            soundPool.play(soundSound, 1.0f, 1.0f, 0, 0, 1);
            setAnswerValue();
            checkResult(true);

        } else if (id == R.id.button2) {
            Toast.makeText(this, "sound", Toast.LENGTH_SHORT).show();
            soundPool.play(soundSound, 1.0f, 1.0f, 0, 0, 1);
            Toast.makeText(this, "sound2", Toast.LENGTH_SHORT).show();

            setAnswerValue();
            Toast.makeText(this, "sound3", Toast.LENGTH_SHORT).show();

            checkResult(false);
            Toast.makeText(this, "sound4", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.button3) {
            soundPool.play(soundSound, 1.0f, 1.0f, 0, 0, 1);
            setQuestionValue();
            clearAnswerValue();
            clearScoreValue();
        }
    }

    private void clearAnswerValue() {
        TextView txtView1 = (TextView) findViewById(R.id.answer);
        TextView txtView2 = (TextView) findViewById(R.id.question);
        txtView1.setText("値2");
        txtView1.setBackgroundColor(Color.rgb(255,255,0));
        txtView2.setBackgroundColor(Color.rgb(255,0,255));
    }

    private void setQuestionValue() {
        Random r = new Random();
        // 0から10の範囲で乱数を生成（+1する必要がある）
        int questionValue = r.nextInt(numberpickervalue + 1);

        TextView txtView = findViewById(R.id.question);
        txtView.setText(Integer.toString(questionValue));
    }

    private void setAnswerValue() {
        Random r = new Random();
        int answerValue = r.nextInt(numberpickervalue + 1);

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
                txtViewQuestion.setBackgroundColor(Color.rgb(0,0,255));
                txtViewAnswer.setBackgroundColor(Color.rgb(255,0,0));
            } else if (question > answer) {
                result = "LOSE";
                score = -1;
                vibration();
                txtViewQuestion.setBackgroundColor(Color.rgb(255,0,0));
                txtViewAnswer.setBackgroundColor(Color.rgb(0,0,255));
            } else {
                result = "DRAW";
                score = 1;
                txtViewQuestion.setBackgroundColor(Color.rgb(0,153,0));
                txtViewAnswer.setBackgroundColor(Color.rgb(0,153,0));
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                txtViewQuestion.setBackgroundColor(Color.rgb(255,0,0));
                txtViewAnswer.setBackgroundColor(Color.rgb(0,0,255));
            } else if (question < answer) {
                result = "LOSE";
                score = -1;
                txtViewQuestion.setBackgroundColor(Color.rgb(0,0,255));
                txtViewAnswer.setBackgroundColor(Color.rgb(255,0,0));
                vibration();
            } else {
                result = "DRAW";
                score = 1;
                txtViewQuestion.setBackgroundColor(Color.rgb(0,153,0));
                txtViewAnswer.setBackgroundColor(Color.rgb(0,153,0));
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


    private void clearScoreValue() {
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        txtScore.setText("0");
    }

    private void vibration(){

        Vibrator vib = null;
        vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);

        vib.vibrate(5000);

    }

}

