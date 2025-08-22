package com.example.makotomurase;

import androidx.appcompat.app.AlertDialog;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.SharedPreferences;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Insets;
import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.graphics.Color;

import static java.lang.Math.ceil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.animation.ValueAnimator;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Locale;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //プリファレンスの生成
    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

    //　アニメーションする時間管理用の変数を追加
    private static final long COUNT_DOWN_MILLISECOND = 3000;
    private static final long INTERVAL_MILLISECOND = 1000;

    private boolean isInputEnabled = true;
    private CountDownTimer questionTimer;

    private static final String PREF_KEY_MAX = "pref_max_value";
    private static final int DEFAULT_MAX_VALUE = 10;
    private int maxRandomValue = DEFAULT_MAX_VALUE; // 現在の上限値
    private MediaPlayer mediaPlayer;

    public class SoundPlayer {

        private MediaPlayer mediaPlayer;
        private Context context;

        public SoundPlayer(Context context) {
            this.context = context;
        }

        public void playSound(int soundResourceId) {
            // MediaPlayerを解放
            releaseMediaPlayer();

            // MediaPlayerを初期化
            mediaPlayer = MediaPlayer.create(context, soundResourceId);

            // 再生
            mediaPlayer.start();

            // 再生終了時の処理を設定（オプション）
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // 再生が終了したらMediaPlayerを解放
                    releaseMediaPlayer();
                }
            });
        }

        public void releaseMediaPlayer() {
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }
    }

    private static final long START_TIME = 10000;
    private TextView mTextViewCountDown;
    private Button mButtonStartPause;
    private Button getmButtonReset;

    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;

    private long mTimeLeftInMillis = START_TIME;


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


        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        getmButtonReset = findViewById(R.id.button3);
        mButtonStartPause = findViewById(R.id.button4);

        //プリファレンスの生成
        pref = getSharedPreferences("team-g", MODE_PRIVATE);
        prefEditor = pref.edit();

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mTimerRunning) {
                    startTimer();
                }
            }
        });

        // 起動時に関数を呼び出す
        setQuestionValue();

        TextView txtSetting = findViewById(R.id.text_setting);
        maxRandomValue = pref.getInt(PREF_KEY_MAX, DEFAULT_MAX_VALUE);
        txtSetting.setText(maxRandomValue + " が設定されています。");

        mediaPlayer = MediaPlayer.create(this, R.raw.famipop3);
        mediaPlayer.setLooping(true);  // ループ再生設定



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                mTimeLeftInMillis = 0;
                updateCountDownText();
                mButtonStartPause.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "タイムアップ！", Toast.LENGTH_SHORT).show();
            }
        }.start();

        mTimerRunning = true;
    }


    private void resetTimer() {
        mTimeLeftInMillis = START_TIME;
        updateCountDownText();
        mTimerRunning = false;
        mButtonStartPause.setVisibility(View.VISIBLE);
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        // 0秒以下になったら強制的に 00:00 表示
        if (mTimeLeftInMillis <= 0) {
            minutes = 0;
            seconds = 0;
        }

        String timerLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mTextViewCountDown.setText(timerLeftFormatted);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            showSettingDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSettingDialog() {
        final NumberPicker numberPicker = new NumberPicker(this);
        numberPicker.setMinValue(10);
        numberPicker.setMaxValue(100);
        numberPicker.setValue(maxRandomValue);

        new AlertDialog.Builder(this)
                .setTitle("ランダム値の上限を設定")
                .setView(numberPicker)
                .setPositiveButton("OK", (dialog, which) -> {
                    maxRandomValue = numberPicker.getValue();

                    // 表示更新
                    TextView txtSetting = findViewById(R.id.text_setting);
                    txtSetting.setText(maxRandomValue + " が設定されています。");

                    // プリファレンスに保存
                    prefEditor.putInt(PREF_KEY_MAX, maxRandomValue);
                    prefEditor.apply();

                    // 新しい問題を即時に反映（任意）
                    setQuestionValue();
                })
                .setNegativeButton("キャンセル", null)
                .show();
    }


    @SuppressLint({"NonConstantResourceId", "NewApi"})
    @Override
    public void onClick(View view) {

        if (!isInputEnabled) {
            return;
        }
        int id = view.getId();

        if (id == R.id.button1) {
            setAnswerValue();
            checkResult(true);
        } else if (id == R.id.button2) {
            setAnswerValue();
            checkResult(false);
        } else if (id == R.id.button3) {
            if (questionTimer != null) {
                questionTimer.cancel();
            }
            setQuestionValue();
            clearAnswerValue();
            clearScoreValue();
            Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vib.vibrate(50);
            clearColorValue();
            SoundPlayer soundPlayer = new SoundPlayer(this);
            soundPlayer.playSound(R.raw.horyu);
        }
        if (mTimerRunning) {
            startTimer();
        }


        // タイマーが動いているときだけ結果判定を実行
        if (mTimerRunning) {
            if (id == R.id.button1) {
                setAnswerValue();
                checkResult(true);
            } else if (id == R.id.button2) {
                setAnswerValue();
                checkResult(false);
            }
        }

        if (id == R.id.button3) {
            setQuestionValue();
            clearAnswerValue();
            clearScoreValue();
            resetTimer();
        }
    }

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        String PlayerNumber = getResources().getString(R.string.PlayerNum);
        txtView.setText(PlayerNumber);
    }

    private void setQuestionValue() {
        Random r = new Random();
        int questionValue = r.nextInt(maxRandomValue + 1); // ←変更
        TextView txtView = findViewById(R.id.question);
        txtView.setText(Integer.toString(questionValue));
    }

    private void setAnswerValue() {
        Random r = new Random();
        int answerValue = r.nextInt(maxRandomValue + 1); // ←変更
        double probability = 0.05;
        //5%の確率で777が出る。
        if (r.nextDouble() < probability) {
            answerValue = 777;
        }
        TextView txtView = findViewById(R.id.answer);
        txtView.setText(Integer.toString(answerValue));
    }

    private void checkResult(boolean isHigh) {
        TextView txtViewQuestion = findViewById(R.id.question);
        TextView txtViewAnswer = findViewById(R.id.answer);
        String PlayerNumber = getResources().getString(R.string.PlayerNum);

        int question = Integer.parseInt(txtViewQuestion.getText().toString());
        int answer = Integer.parseInt(txtViewAnswer.getText().toString());

        TextView txtResult = (TextView) findViewById(R.id.text_result);

        TextView animation_Label_TextView;

        // 結果を示す文字列を入れる変数を用意
        String result = "";
        int score = 0;

        String lastresultWIN = getResources().getString(R.string.lastresultWIN);
        String lastresultLOSE = getResources().getString(R.string.lastresultLOSE);
        String lastresultDRAW = getResources().getString(R.string.lastresultDRAW);
        View layout1 = findViewById(R.id.question);
        View layout2 = findViewById(R.id.answer);

        // Highが押された
        if (isHigh) {
            isInputEnabled = false;
            // result には結果のみを入れる
            if (question < answer) {
                result = lastresultWIN;
                if (answer == 777) {
                    result = "Lucky Win";
                    score = 2;
                } else if (question < answer) {
                    result = "WIN";
                    score = 2;
                    Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    vib.vibrate(400);

                    layout1.setBackgroundColor(Color.CYAN);
                    layout2.setBackgroundColor(Color.RED);

                    SoundPlayer soundPlayer = new SoundPlayer(this);

                    soundPlayer.playSound(R.raw.atari);

                } else if (question > answer) {
                    result = lastresultLOSE;
                    score = -1;
                    Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    vib.vibrate(1000);
                    layout1.setBackgroundColor(Color.RED);
                    layout2.setBackgroundColor(Color.CYAN);

                    SoundPlayer soundPlayer = new SoundPlayer(this);

                    soundPlayer.playSound(R.raw.dededon);

                } else {
                    result = lastresultDRAW;
                    score = 1;
                    Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    vib.vibrate(500);

                    SoundPlayer soundPlayer = new SoundPlayer(this);

                    soundPlayer.playSound(R.raw.kezoku);
                }
            }
        } else {
            isInputEnabled = false;
            if (question > answer) {
                result = lastresultWIN;
                layout1.setBackgroundColor(Color.RED);
                layout2.setBackgroundColor(Color.CYAN);
                if (answer == 777) {
                    result = "Lucky Win";
                    score = 2;
                } else if (question > answer) {
                    result = "WIN";
                    score = 2;
                    Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    vib.vibrate(400);

                    SoundPlayer soundPlayer = new SoundPlayer(this);

                    soundPlayer.playSound(R.raw.maxkezoku);

                } else if (question < answer) {
                    result = lastresultLOSE;
                    layout1.setBackgroundColor(Color.CYAN);
                    layout2.setBackgroundColor(Color.RED);
                    score = -1;
                    Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    vib.vibrate(1000);

                    SoundPlayer soundPlayer = new SoundPlayer(this);

                    soundPlayer.playSound(R.raw.dededon);

                } else {
                    result = lastresultDRAW;
                    layout1.setBackgroundColor(Color.GREEN);
                    layout2.setBackgroundColor(Color.GREEN);
                    score = 1;
                    Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    vib.vibrate(500);

                    SoundPlayer soundPlayer = new SoundPlayer(this);

                    soundPlayer.playSound(R.raw.kezoku);

                }
            }
        }

        if (score == 2 || score == -1) {
            if (question > answer) {
                animation_Label_TextView = findViewById(R.id.question);
            } else {
                animation_Label_TextView = findViewById(R.id.answer);
            }
            CountDownTimer countDownTimer = new CountDownTimer(COUNT_DOWN_MILLISECOND, INTERVAL_MILLISECOND) {
                @Override
                public void onTick(long millisUntilFinished) {
                    // scaleを元に戻す
                    animation_Label_TextView.setScaleX(1f);
                    animation_Label_TextView.setScaleY(1f);

                    // scaleアニメーション開始
                    ViewCompat.animate(animation_Label_TextView)
                            .setDuration(1000)
                            .scaleX(2f)
                            .scaleY(2f)
                            .start();
                }

                @Override
                public void onFinish() {
                    long COUNT_DOWN_MILLISECOND = 5000;
                    // scaleを元に戻す
                    animation_Label_TextView.setScaleX(1f);
                    animation_Label_TextView.setScaleY(1f);
                }
            }.start();
        }

        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText(question + ":" + answer + "(" + result + ")");

        // 続けて遊べるように値を更新
        setNextQuestion();
        // スコアを表示
        setScore(score);
    }

    private void setNextQuestion() {
        // 第１引数がカウントダウン時間、第２引数は途中経過を受け取る間隔
        // 単位はミリ秒（1秒＝1000ミリ秒）
        questionTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {
                // 途中経過を受け取った時に何かしたい場合
                // 今回は特に何もしない
            }

            @Override
            public void onFinish() {
                // 3秒経過したら次の値をセット
                setQuestionValue();
                isInputEnabled = true;
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

    private void score_Save() {
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        int Score = Integer.parseInt(txtScore.getText().toString());
        //プリファレンスの保存
        prefEditor.putInt("pref_score", Score);
        prefEditor.commit();
    }

    private void score_Load() {
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        //プリファレンスの読み込み
        int Pref_Score = pref.getInt("pref_score", 0);
        txtScore.setText(Integer.toString(Pref_Score));
    }

    @Override
    protected void onResume() {
        super.onResume();
        score_Load();
        if(mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        score_Save();

        if(mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void clearColorValue() {
        View layout1 = findViewById(R.id.question);
        View layout2 = findViewById(R.id.answer);
        layout1.setBackgroundColor(Color.MAGENTA);
        layout2.setBackgroundColor(Color.YELLOW);
    }

}
