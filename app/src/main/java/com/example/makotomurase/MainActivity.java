package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.graphics.Color;
import android.content.SharedPreferences;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.res.Configuration;
import java.util.Random;
import java.util.Locale;
import android.media.MediaPlayer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MediaPlayer mediaPlayer;

    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, windowInsets) -> {
            Insets insets = windowInsets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
                            | WindowInsetsCompat.Type.displayCutout());
            view.setPadding(insets.left, insets.top, insets.right, 0);
            return windowInsets;
        });

        Button btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(this);

        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(this);

        final View layout=findViewById(R.id.answer);
        layout.setBackgroundColor(Color.YELLOW);

        final View layout1=findViewById(R.id.question);
        layout1.setBackgroundColor(Color.RED);



        // 起動時に関数を呼び出す
        setQuestionValue();

        pref = getSharedPreferences("GameScore", MODE_PRIVATE);
        prefEditor = pref.edit();
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
            Vibrator vibrator;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                VibratorManager vibratorManager = (VibratorManager) getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
                vibrator = vibratorManager.getDefaultVibrator();
            } else {
                vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            }

            if (vibrator != null && vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // 第1引数: ミリ秒, 第2引数: 強度（0〜255、DEFAULT_AMPLITUDEは標準）
                    vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(100);
                }
            }
            setQuestionValue();
            clearAnswerValue();
            clearScoreValue();

            final View layout=findViewById(R.id.answer);
            final View layout1=findViewById(R.id.question);
            layout.setBackgroundColor(Color.YELLOW);
            layout1.setBackgroundColor(Color.RED);

            MediaPlayer mp = MediaPlayer.create(this, R.raw.restart);
            mp.start();

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
        final View layout=findViewById(R.id.answer);
        final View layout1=findViewById(R.id.question);

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = "WIN";
                score = 2;

                layout.setBackgroundColor(Color.YELLOW);
                layout1.setBackgroundColor(Color.CYAN);

                MediaPlayer mp = MediaPlayer.create(this, R.raw.win);
                mp.start();

                mp.setOnCompletionListener(player -> {
                    player.release();
                });

            } else if (question > answer) {
                result = "LOSE";
                score = -1;

                layout.setBackgroundColor(Color.CYAN);
                layout1.setBackgroundColor(Color.YELLOW);

                MediaPlayer mp = MediaPlayer.create(this, R.raw.lose);
                mp.start();

                mp.setOnCompletionListener(player -> {
                    player.release();
                });
            } else {
                result = "DRAW";
                score = 1;

                layout.setBackgroundColor(Color.GREEN);
                layout1.setBackgroundColor(Color.GREEN);

                MediaPlayer mp = MediaPlayer.create(this, R.raw.draw);
                mp.start();

                mp.setOnCompletionListener(player -> {
                    player.release();
                });
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;

                layout.setBackgroundColor(Color.YELLOW);
                layout1.setBackgroundColor(Color.CYAN);
                
                MediaPlayer mp = MediaPlayer.create(this, R.raw.win);
                mp.start();

            } else if (question < answer) {
                result = "LOSE";
                score = -1;

                layout.setBackgroundColor(Color.CYAN);
                layout1.setBackgroundColor(Color.YELLOW);
                
                MediaPlayer mp = MediaPlayer.create(this, R.raw.lose);
                mp.start();

            } else {
                result = "DRAW";
                score = 1;

                layout.setBackgroundColor(Color.GREEN);
                layout1.setBackgroundColor(Color.GREEN);

                MediaPlayer mp = MediaPlayer.create(this, R.raw.draw);
                mp.start();
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

                final View layout=findViewById(R.id.answer);
                layout.setBackgroundColor(Color.YELLOW);
                final View layout1=findViewById(R.id.question);
                layout1.setBackgroundColor(Color.RED);



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

    @Override
    protected void onPause() {
        super.onPause();

        TextView txtScore = (TextView) findViewById(R.id.text_score);
        prefEditor.putString("main_input", txtScore.getText().toString());
        prefEditor.commit();
    }

    @Override
    protected void onResume(){
        super.onResume();
        // Log.d("test", "onResume completed.");

        TextView textView = (TextView)findViewById(R.id.text_score);
        String readText = pref.getString("main_input", "保存されていません。");
        textView.setText(readText);
    }

    private void playSound(int soundResId) {
        // Stop and release any currently playing sound
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }

        // Create and start new sound
        mediaPlayer = MediaPlayer.create(this, soundResId);
        mediaPlayer.start();

        // Release after completion
        mediaPlayer.setOnCompletionListener(mp -> {
            mp.release();
            mediaPlayer = null;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    private void playSound1(int soundResId) {
        // Stop and release previous sound if it exists
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }

        // Initialize and start new sound
        mediaPlayer = MediaPlayer.create(this, soundResId);
        mediaPlayer.start();

        // Release the MediaPlayer automatically when completed
        mediaPlayer.setOnCompletionListener(mp -> {
            mp.release();
            mediaPlayer = null;
        });
    }

}

