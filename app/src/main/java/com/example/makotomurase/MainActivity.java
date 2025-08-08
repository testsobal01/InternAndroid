package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.content.SharedPreferences;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Vibrator;
import java.util.Random;
import android.os.Handler;
import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    int mp3b;
    int mp3c;
    int mp3d;
    int mp3e;
    int mp3f;
    int mp3g;
    SoundPool soundPool2;
    SoundPool soundPool3;
    SoundPool soundPool4;
    SoundPool soundPool5;
    SoundPool soundPool6;
    SoundPool soundPool7;
    private ImageView imageView;
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

        AudioAttributes attr = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        soundPool2 = new SoundPool.Builder()
                .setAudioAttributes(attr)
                .setMaxStreams(2)
                .build();
        soundPool3 = new SoundPool.Builder()
                .setAudioAttributes(attr)
                .setMaxStreams(2)
                .build();
        soundPool4 = new SoundPool.Builder()
                .setAudioAttributes(attr)
                .setMaxStreams(2)
                .build();
        soundPool5 = new SoundPool.Builder()
                .setAudioAttributes(attr)
                .setMaxStreams(2)
                .build();
        soundPool6 = new SoundPool.Builder()
                .setAudioAttributes(attr)
                .setMaxStreams(2)
                .build();
        soundPool7 = new SoundPool.Builder()
                .setAudioAttributes(attr)
                .setMaxStreams(2)
                .build();

        mp3b = soundPool2.load(this, R.raw.high, 1);
        mp3c = soundPool3.load(this, R.raw.row, 1);
        mp3d = soundPool4.load(this, R.raw.tyantyan3, 1);
        mp3e = soundPool5.load(this, R.raw.win, 1);
        mp3f = soundPool6.load(this, R.raw.lose1, 1);
        mp3g = soundPool7.load(this, R.raw.drow, 1);

        pref = getSharedPreferences("AndroidSeminor", MODE_PRIVATE);
        prefEditor = pref.edit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();

        TextView textView = (TextView) findViewById(R.id.text_score);
        String readText = pref.getString("main_input", "保存されていません");
        textView.setText(readText);
    }

    }
    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();

        TextView textView = (TextView) findViewById(R.id.text_score);
        prefEditor.putString("main_input", textView.getText().toString());
        prefEditor.commit();
    }



    @Override
    public void onClick(View view) {
//        soundPool=new SoundPool(2, AudioManager.STREAM_MUSIC,0);



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
            soundPool4.play(mp3d,1f , 1f, 0, 0, 1f);

        }
    }

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText(getString(R.string.answer));
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
        TextView txtResult = findViewById(R.id.text_result);
        View rootLayout = findViewById(R.id.root_layout);

        int question = Integer.parseInt(txtViewQuestion.getText().toString());
        int answer = Integer.parseInt(txtViewAnswer.getText().toString());

        // 結果を示す文字列を入れる変数を用意
        String result;
        int score;

        TextView winnerView = null;
        TextView loserView = null;

        // Highが押された
        if (isHigh) {
            soundPool2.play(mp3b,1f , 1f, 0, 0, 1f);

            // result には結果のみを入れる
            if (question < answer) {
                result =getString(R.string.WIN);
                score = 2;
                soundPool5.play(mp3e,1f , 1f, 0, 0, 1f);
                winnerView = txtViewAnswer;
                loserView = txtViewQuestion;
                rootLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.win_background));
            } else if (question > answer) {
                result =getString(R.string.LOSE);
                score = -1;
                soundPool6.play(mp3f,1f , 1f, 0, 0, 1f);
                winnerView = txtViewQuestion;
                loserView = txtViewAnswer;
                rootLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.lose_background));
                Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(400);
            } else {
                result =getString(R.string.DRAW);
                score = 1;
                soundPool7.play(mp3g,1f , 1f, 0, 0, 1f);
                rootLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.default_background));
            }
        } else {
            soundPool3.play(mp3c,1f , 1f, 0, 0, 1f);

            if (question > answer) {
                result =getString(R.string.WIN);
                score = 2;
                soundPool5.play(mp3e,2f , 2f, 0, 0, 1f);
                winnerView = txtViewQuestion;
                loserView = txtViewAnswer;
                rootLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.win_background));
            } else if (question < answer) {
                result =getString(R.string.LOSE);
                score = -1;
                soundPool6.play(mp3f,1f , 1f, 0, 0, 1f);
                rootLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.lose_background));
                winnerView = txtViewAnswer;
                loserView = txtViewQuestion
                Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(400);
            } else {
                result =getString(R.string.DRAW);
                score = 1;
                soundPool7.play(mp3g,1f , 1f, 0, 0, 1f);
                rootLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.default_background));
            }
        }

        new Handler().postDelayed(() -> {
            rootLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.default_background));
        }, 5000);
        if (!result.equals(getString(R.string.DRAW))) {
            animateWinner(winnerView);
            animateLoser(loserView);
        }
      
        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText(getString(R.string.result)+ question + ":" + answer + "(" + result + ")");

        // 続けて遊べるように値を更新
        setNextQuestion();
        // スコアを表示
        setScore(score);


    }

    private void animateWinner(TextView view) {
            ScaleAnimation scaleUp = new ScaleAnimation(
                    1.0f, 1.5f, 1.0f, 1.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
            );
            scaleUp.setDuration(300);
            scaleUp.setRepeatCount(1);
            scaleUp.setRepeatMode(Animation.REVERSE);
            view.startAnimation(scaleUp);
        }

        private void animateLoser(TextView view) {
            ScaleAnimation scaleDown = new ScaleAnimation(
                    1.0f, 0.5f, 1.0f, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
            );
            scaleDown.setDuration(300);
            scaleDown.setRepeatCount(1);
            scaleDown.setRepeatMode(Animation.REVERSE);
            view.startAnimation(scaleDown);
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
}

