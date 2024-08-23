package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.media.SoundPool;
import android.media.AudioAttributes;

import java.sql.ResultSetMetaData;
import java.util.Random;
import android.view.animation.AnimationUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    int Sound1,Sound2,Sound3,Sound4;
    int oto1,oto2,oto4,oto3;

    SoundPool soundPool;
    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;
    int questionValueMax = 10;
    boolean isHighLowButtonClick = false;

    int wincount; //連勝数を示す変数を追加
    int streak = 0;  //= Integer.parseInt(txtWincount.getText().toString());


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
      
        setNextQuestion();

        //
        soundPool = null;
        AudioAttributes audioAttributes = new
                AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH).build();

        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes).setMaxStreams(1).build();

        oto1 = getResources().getIdentifier("sound1", "raw", getPackageName());
        Sound1=soundPool.load(getBaseContext(),oto1,1);

        oto2 = getResources().getIdentifier("sound2", "raw", getPackageName());
        Sound2=soundPool.load(getBaseContext(),oto2,1);

        oto3 = getResources().getIdentifier("sound3", "raw", getPackageName());
        Sound3=soundPool.load(getBaseContext(),oto3,1);

        oto4 = getResources().getIdentifier("sound4", "raw", getPackageName());
        Sound4=soundPool.load(getBaseContext(),oto4,1);
        //Sound = soundPool.load(getBaseContext(), R.raw.sound1, 1);
      
        // プリファレンスの生成
        setPreferences();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // スコアを再開
        resumeScore();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // スコアを保存
        saveScore();
    }

    @Override
    public void onClick(View view) {
        /*switch (view.getId()) {
            case R.id.button1:
                Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(5000);
                break;
        }*/

        //Button button = (Button) findViewById(R.id.button1);
        //button.setOnClickListener(this);




        int id = view.getId();
        if (id == R.id.button1) {
            isHighLowButtonClick = true;
            setAnswerValue();
            checkResult(true);
            colerChange(true);
        } else if (id == R.id.button2) {
            isHighLowButtonClick = true;
            setAnswerValue();
            checkResult(false);
            colerChange(false);
        } else if (id == R.id.button3) {

            soundPool.play(Sound4, 1f, 1f, 1, 0, 1f);

            setQuestionValue();
            clearAnswerValue();
            clearScoreValue();
            clearBackground();
            streak = 0;
        }

    }

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText("値2");
    }
    private void clearBackground(){
        LinearLayout back = findViewById(R.id.background);
        back.setBackgroundColor(Color.WHITE);
    }


    private void setQuestionValue() {
        Random r = new Random();
        // 0から10の範囲で乱数を生成（+1する必要がある）
        int questionValue = r.nextInt(questionValueMax + 1);

        TextView txtView = findViewById(R.id.question);
        txtView.setText(Integer.toString(questionValue));
    }

    private void setAnswerValue() {
        Random r = new Random();
        int answerValue = r.nextInt(questionValueMax + 1);

        TextView txtView = findViewById(R.id.answer);
        txtView.setText(Integer.toString(answerValue));
    }

    private void checkResult(boolean isHigh) {
        TextView txtViewQuestion = findViewById(R.id.question);
        TextView txtViewAnswer = findViewById(R.id.answer);

        int question = Integer.parseInt(txtViewQuestion.getText().toString());
        int answer = Integer.parseInt(txtViewAnswer.getText().toString());

        TextView txtResult = (TextView) findViewById(R.id.text_result);
        //TextView txtWincount = (TextView)findViewById(R.id.wincount);



        // 結果を示す文字列を入れる変数を用意
        String result;
        int score;

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                soundPool.play(Sound1, 1f, 1f, 1, 0, 1f);
                result = "WIN";
                score = 2;
                streak ++;

                findViewById(R.id.answer).startAnimation(AnimationUtils.loadAnimation(this, R.anim.animation));
                Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(1000);

            } else if (question > answer) {
                soundPool.play(Sound2, 1f, 1f, 1, 0, 1f);
                result = "LOSE";
                score = -1;
                streak = 0;
                findViewById(R.id.question).startAnimation(AnimationUtils.loadAnimation(this, R.anim.animation2));

            } else {
                soundPool.play(Sound3, 1f, 1f, 1, 0, 1f);
                result = "DRAW";
                score = 1;
                findViewById(R.id.question).startAnimation(AnimationUtils.loadAnimation(this, R.anim.animation3));
                findViewById(R.id.answer).startAnimation(AnimationUtils.loadAnimation(this, R.anim.animation4));

            }
        } else {
            if (question > answer) {
                soundPool.play(Sound1, 1f, 1f, 1, 0, 1f);
                result = "WIN";
                score = 2;
                streak ++;
                findViewById(R.id.question).startAnimation(AnimationUtils.loadAnimation(this, R.anim.animation));

            } else if (question < answer) {
                soundPool.play(Sound2, 1f, 1f, 1, 0, 1f);
                result = "LOSE";
                score = -1;
                findViewById(R.id.question).startAnimation(AnimationUtils.loadAnimation(this, R.anim.animation2));
                streak = 0;
            } else {
                soundPool.play(Sound3, 1f, 1f, 1, 0, 1f);
                result = "DRAW";
                score = 1;
                findViewById(R.id.question).startAnimation(AnimationUtils.loadAnimation(this, R.anim.animation3));
                findViewById(R.id.answer).startAnimation(AnimationUtils.loadAnimation(this, R.anim.animation4));

            }
        }

        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        //Toast.makeText(this, result, Toast.LENGTH_LONG).show();

        TextView txtViewToast = findViewById(R.id.toast);
        TextView txtViewToastBack = findViewById(R.id.toastBack);
        txtViewToastBack.setBackgroundColor(Color.parseColor("#B3e4edea"));
        txtViewToast.setText(result + "!");
      
        txtResult.setText("結果：" + question + ":" + answer + "(" + result + ")" + "   " + streak + "連勝中！");
        //txtWincount.setText("   " + wincount + "連勝中！");

        // 続けて遊べるように値を更新
        //setNextQuestion();

        //setWincount(streak);
        // スコアを表示
        setScore(score);
    }

    private void setNextQuestion() {
        // 第１引数がカウントダウン時間、第２引数は途中経過を受け取る間隔
        // 単位はミリ秒（1秒＝1000ミリ秒）
        new CountDownTimer(3000, 1) {
            boolean isFinishStop = false;
            @Override
            public void onTick(long l) {
                // 途中でHighかLowボタンが押されたとき
                if(isHighLowButtonClick == true){
                    isHighLowButtonClick = false;
                    // 次の値セット
                    setQuestionValue();
                    isFinishStop = true;
                    setNextQuestion();
                }
            }

            @Override
            public void onFinish() {
                if (isFinishStop == false) {
                    Random r = new Random();
                    questionValueMax = r.nextInt(9999 + 1);
                    // 3秒経過したら次の値をセット
                    setQuestionValue();
                    // 3秒経過したらCountDownTimer再起動
                    setNextQuestion();
                }
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


    //public void setWincount(int streak){
        //TextView txtWin = (TextView) findViewById(R.id.wincount);
        //int newWincount = Integer.parseInt(txtWin.getText().toString()) + 1;
        //txtWin.setText(Integer.toString(newWincount));
    //}
    //private void clearWincount() {
        //TextView txtWin = (TextView) findViewById(R.id.wincount);
        //txtWin.setText("0");
    //}

    private void colerChange(boolean isHigh) {

        TextView txtViewQuestion = findViewById(R.id.question);
        TextView txtViewAnswer = findViewById(R.id.answer);

        int question = Integer.parseInt(txtViewQuestion.getText().toString());
        int answer = Integer.parseInt(txtViewAnswer.getText().toString());


        LinearLayout back = findViewById(R.id.background);


        if (isHigh) {
            if (question < answer) {
                back.setBackgroundColor(Color.RED);
            } else if (question > answer) {
                back.setBackgroundColor(Color.BLUE);
            } else {
                back.setBackgroundColor(Color.WHITE);
            }
        } else {
            if (question > answer) {
                back.setBackgroundColor(Color.RED);
            } else if (question < answer) {
                back.setBackgroundColor(Color.BLUE);
            } else {
                back.setBackgroundColor(Color.WHITE);
            }
        }
    }
    private void setPreferences(){
        pref = getSharedPreferences("Score",MODE_PRIVATE);
        prefEditor = pref.edit();
    }

    private void resumeScore(){
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        txtScore.setText(pref.getString("main_score","0"));
    }

    private void saveScore(){
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        prefEditor.putString("main_score",txtScore.getText().toString());
        prefEditor.commit();
    }

}

