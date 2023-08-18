package com.example.makotomurase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

        Button btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(this);

        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(this);

        // 起動時に関数を呼び出す
        setQuestionValue();
        Sound();
        pref = getSharedPreferences("AndroidSeminor", MODE_PRIVATE);
        prefEditor = pref.edit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //テキストビューの取得
        TextView textView = (TextView) findViewById(R. id.text_score);
        //text_result(キー名)に文字列を保存
        prefEditor.putString("text_score", textView.getText().toString());
        prefEditor.commit();
    }

    //連打したかどうかを判別する変数
    int pushButtonCheck=0;
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

            resetBackColor();

            //4-バイブレーション
            Vibrator vib=(Vibrator)getSystemService((VIBRATOR_SERVICE));
            vib.vibrate(300);
            Sound();
            setQuestionValue();
            clearAnswerValue();
            clearScoreValue();
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
                winSound();
                winChangeBackColor();
            } else if (question > answer) {
                result = "LOSE";
                score = -1;
                lowSound();
                loseChangeBackColor();
            } else {
                result = "DRAW";
                score = 1;
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                winSound();
                winChangeBackColor();
            } else if (question < answer) {
                result = "LOSE";
                score = -1;
                lowSound();
                loseChangeBackColor();
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
    }

    private void setNextQuestion() {
        // 第１引数がカウントダウン時間、第２引数は途中経過を受け取る間隔
        // 単位はミリ秒（1秒＝1000ミリ秒）
        CountDownTimer cdt=new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {
            pushButtonCheck++;
            }

            @Override
            public void onFinish() {
                // 3秒経過したら次の値をセット
                setQuestionValue();
                Sound();
                pushButtonCheck=0;
            }
        }.start();
        if(pushButtonCheck!=0){
            cdt.cancel();
        }
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
    //6-勝った時の背景色変更
    public void winChangeBackColor(){
        TextView textBackColor_a=(TextView) findViewById(R.id.answer);
        TextView textBackColor_q=(TextView) findViewById(R.id.question);
        textBackColor_a.setBackgroundColor(getResources().getColor(R.color.rainbow));
        textBackColor_q.setBackgroundColor(Color.YELLOW);
    }
    //6-負けた時の背景色変更
    public void loseChangeBackColor(){
        TextView textBackColor_a=(TextView) findViewById(R.id.answer);
        TextView textBackColor_q=(TextView) findViewById(R.id.question);
        textBackColor_a.setBackgroundColor(getResources().getColor(R.color.lightblue));
        textBackColor_q.setBackgroundColor(Color.YELLOW);
    }

    //6-背景色のリセット
    public void resetBackColor(){
        TextView textBackColor_a=(TextView) findViewById(R.id.answer);
        TextView textBackColor_q=(TextView) findViewById(R.id.question);
        textBackColor_a.setBackgroundColor(Color.YELLOW);
        textBackColor_q.setBackgroundColor(Color.MAGENTA);
    }
    //8-効果音の追加
    public void Sound(){
        SoundPool sndPl=new SoundPool(1, AudioManager.STREAM_MUSIC,0);
        int sndId=sndPl.load(this.getApplicationContext(),R.raw.quiz_start,0);
        sndPl.play(sndId,1.0F,1.0F,0,0,1.0F);
    }
    public void winSound(){
        SoundPool sndPl=new SoundPool(1, AudioManager.STREAM_MUSIC,0);
        int sndId=sndPl.load(this.getApplicationContext(),R.raw.quiz_win2,0);
        sndPl.play(sndId,1.0F,1.0F,0,0,1.0F);
    }
    public void lowSound(){
        SoundPool sndPl=new SoundPool(1, AudioManager.STREAM_MUSIC,0);
        int sndId=sndPl.load(this.getApplicationContext(),R.raw.quiz_lose1,0);
        sndPl.play(sndId,1.0F,1.0F,0,0,1.0F);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        //テキストビューの取得
        TextView textView = (TextView) findViewById(R. id.text_score);
        //保存した値をキー名を指定して取得(保存されていない場合の文字列も指定)
        String readText = pref.getString("text_score","保存されていません");
        textView.setText(readText);

    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu){
        getMenuInflater().inflate(R.menu.main_option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();

        return super.onOptionsItemSelected(item);
    }


}

