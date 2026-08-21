package com.example.makotomurase;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.viewmodel.CreationExtras;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.LimitExceededException;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Random;
import android.media.ToneGenerator;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.media.AudioManager;
import android.media.AudioAttributes;
import android.media.SoundPool;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SoundPool soundPool;

    private int soundpinpon2, soundbubbu1,soundloop200103;

    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

    public static final int LIMIT_INIT = 10;

    int limit = LIMIT_INIT;

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


        Intent intent2 = getIntent();
        Bundle extra=intent2.getExtras();




        Button btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(this);

        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(this);

        Button btnSetting = findViewById(R.id.settingBtn);
        btnSetting.setOnClickListener(this);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .build();

        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                // ストリーム数に応じて
                .setMaxStreams(4)
                .build();

        soundpinpon2 = soundPool.load(this, R.raw.pinpon2, 1);
        soundbubbu1 = soundPool.load(this, R.raw.bubbu1, 1);
        soundloop200103= soundPool.load(this, R.raw.loop200103, 1);




        TextView lblLimit = findViewById(R.id.label_limit);
        lblLimit.setText(getResources().getString(R.string.label_limit, limit));

      /*  TextView scoreLabel = findViewById(R.id.text_score);
        int score = getIntent().getIntExtra("SCORE", 0);
        scoreLabel.setText(score + "");

        SharedPreferences sharedPreferences = getSharedPreferences("GAME_DATA", MODE_PRIVATE);
        int highScore = sharedPreferences.getInt("SCORE", 0);*/

        pref = getSharedPreferences("AndroidSEminar",MODE_PRIVATE);
        prefEditor = pref.edit();


        // 起動時に関数を呼び出す
        setQuestionValue(limit);
    }
    @Override
    protected void onPause(){
            super.onPause();
            Toast.makeText(this,"onPause",Toast.LENGTH_SHORT).show();

            TextView textView = (TextView) findViewById(R.id.text_score);


            prefEditor.putInt("main_input", Integer.parseInt(textView.getText().toString()));
            prefEditor.commit();

         /*   TextView view3 = findViewById(R.id.text_score);
            SharedPreferences preferences = getSharedPreferences("AndroidSEminar",MODE_PRIVATE);
            pref.edit().putInt("score",score).apply();*/

    }
    @Override
    protected void onResume(){
            super.onResume();
            Log.d("AndroidTest","onResume completed");
            TextView textView = (TextView) findViewById(R.id.text_score);
            String readText = String.valueOf(pref.getInt("main_input",0));
            textView.setText(readText);
    }



    @Override
    public void onClick(View view) {
      /*  ToneGenerator toneGen = new ToneGenerator(AudioManager.STREAM_SYSTEM, 100);
        toneGen.startTone(ToneGenerator.TONE_DTMF_1, 150);*/

        int id = view.getId();
        if (id == R.id.button1) {
            setAnswerValue(limit);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                checkResult(true);
                diableAllBtns();
            }
        } else if (id == R.id.button2) {
            setAnswerValue(limit);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                checkResult(false);
                diableAllBtns();
            }
        } else if (id == R.id.button3) {
            setQuestionValue(limit);
            clearAnswerValue();
            clearScoreValue();
            resetBgColor();
        } else if (id == R.id.settingBtn) {
            //numberPicker宣言
            final NumberPicker numPicker = new NumberPicker(getApplicationContext());
            numPicker.setValue(limit);
            numPicker.setMinValue(10);
            numPicker.setMaxValue(50);

            //AlertDialog準備
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            //タイトル設定
            builder.setTitle(getResources().getString(R.string.dialogTitle))
                    //numberPicker配置
                    .setView(numPicker)
                    //Yesボタン
                    .setPositiveButton(
                            getResources().getString(R.string.dialogYesBtn),
                            (dialog, which) -> {
                                //上限値を更新
                                limit = numPicker.getValue();
                                TextView lblLimit = findViewById(R.id.label_limit);
                                lblLimit.setText(getResources().getString(R.string.label_limit, limit));
                            }
                    )
                    //Noボタン
                    .setNegativeButton(
                            getResources().getString(R.string.dialogNoBtn),
                            null
                    ).show();
        }



    }

    private void clearAnswerValue() {
        TextView ansView = (TextView) findViewById(R.id.answer);
        ansView.setText(getResources().getString(R.string.label_answer));
    }

    private void resetBgColor() {
        TextView ansView = (TextView) findViewById(R.id.answer);
        TextView qstView = (TextView) findViewById(R.id.question);

        ansView.setBackgroundColor(getResources().getColor(R.color.bgAnswerDefault));
        qstView.setBackgroundColor(getResources().getColor(R.color.bgQuestionDefault));
    }

    private  void setWinnerBgColor(String result) {
        TextView ansView = (TextView) findViewById(R.id.answer);
        TextView qstView = (TextView) findViewById(R.id.question);

        if (result.equals("WIN")){
            ansView.setBackgroundColor(getResources().getColor(R.color.bgAnswerWin));
        } else if (result.equals("LOSE")){
            qstView.setBackgroundColor(getResources().getColor(R.color.bgQuestionWin));
        }
    }

    private void blinkText(String result) {
        TextView view;
        TextView ansView = (TextView) findViewById(R.id.answer);
        TextView qstView = (TextView) findViewById(R.id.question);

        if (result.equals("WIN")) {
            view = ansView;
        } else if (result.equals("LOSE")) {
            view = qstView;
        } else {
            return;
        }

        ValueAnimator colorAnimator = ValueAnimator.ofArgb(getResources().getColor(R.color.fontColor), Color.TRANSPARENT);
        colorAnimator.setDuration(200);
        colorAnimator.setRepeatCount(3);
        colorAnimator.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimator.addUpdateListener(animator -> view.setTextColor((int) animator.getAnimatedValue()));
        colorAnimator.start();
    }

    private void diableAllBtns(){
        Button btn1 = findViewById(R.id.button1);
        Button btn2 = findViewById(R.id.button2);
        Button btn3 = (Button) findViewById(R.id.button3);
        Button btnSetting = findViewById(R.id.settingBtn);

        btn1.setEnabled(false);
        btn2.setEnabled(false);
        btn3.setEnabled(false);
        btnSetting.setEnabled(false);
    }

    private void enableAllBtns(){
        Button btn1 = findViewById(R.id.button1);
        Button btn2 = findViewById(R.id.button2);
        Button btn3 = (Button) findViewById(R.id.button3);
        Button btnSetting = findViewById(R.id.settingBtn);

        btn1.setEnabled(true);
        btn2.setEnabled(true);
        btn3.setEnabled(true);
        btnSetting.setEnabled(true);
    }

    private void setQuestionValue(int num) {
        Random r = new Random();
        // 0から10の範囲で乱数を生成（+1する必要がある）
        int questionValue = r.nextInt(num + 1);

        TextView txtView = findViewById(R.id.question);
        txtView.setText(Integer.toString(questionValue));
    }

    private void setAnswerValue(int num) {
        Random r = new Random();
        int answerValue = r.nextInt(num + 1);

        TextView txtView = findViewById(R.id.answer);
        txtView.setText(Integer.toString(answerValue));
    }

    private void checkResult(boolean isHigh) {
        TextView txtViewQuestion = findViewById(R.id.question);
        TextView txtViewAnswer = findViewById(R.id.answer);

        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(
                            (VibrationEffect.createOneShot(
                            500, VibrationEffect.DEFAULT_AMPLITUDE)));
                }
                soundPool.play(soundpinpon2, 1.0f, 1.0f, 0, 0, 1);
            } else if (question > answer) {
                result = "LOSE";
                score = -1;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createWaveform(new long[]{0, 100, 50, 100}, -1));
                }
                soundPool.play(soundpinpon2, 1.0f, 1.0f, 0, 0, 1);
            } else {
                result = "DRAW";
                score = 1;
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(
                            500, VibrationEffect.DEFAULT_AMPLITUDE));
                }
            } else if (question < answer) {
                result = "LOSE";
                score = -1;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createWaveform(new long[]{0, 100, 50, 100}, -1));
                }
                soundPool.play(soundpinpon2, 1.0f, 1.0f, 0, 0, 1);
            } else {
                result = "DRAW";
                score = 1;
            }
        }

        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText(getResources().getString(R.string.label_result, question, answer, result));

        // 勝ったほうの背景色を変更
        setWinnerBgColor(result);
        blinkText(result);

        // 続けて遊べるように値を更新
        setNextQuestion();
        // スコアを表示
        setScore(score);


    }

    private long[] longArrayOf(int i, int i1, int i2) {
        return new long[0];
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
                setQuestionValue(limit);
                resetBgColor();

                //全ボタンを有効化
                enableAllBtns();
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

