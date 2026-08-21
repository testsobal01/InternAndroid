package com.example.makotomurase;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import android.media.AudioAttributes;
import android.media.SoundPool;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SoundPool soundPool;
    private MediaPlayer mediaPlayer;

    private int soundpinpon2, soundbubbu1,soundloop;

    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

    public static final int LIMIT_INIT = 10;

    public static final int MO_INIT = 3;

    int limit = LIMIT_INIT;
    double win=0.0;
    double lose=0.0;
    double draw=0.0;
    double winningRate = 0.0;

    int mo= MO_INIT;

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



        TextView textView= findViewById(R.id.ma);
        textView.setText(String.valueOf(mo));




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
        soundloop= soundPool.load(this, R.raw.loop, 1);




        TextView lblLimit = findViewById(R.id.label_limit);
        lblLimit.setText(getResources().getString(R.string.label_limit, limit));


        pref = getSharedPreferences("AndroidSEminar",MODE_PRIVATE);
        prefEditor = pref.edit();
       // soundPool.play(soundloop, 1.0f, 1.0f, 0, 0, 1);

        audioPlay();


        // 起動時に関数を呼び出す
        setQuestionValue(limit);

        setScore(mo);
    }
    private boolean audioSetup(){
        boolean fileCheck = false;

        // rawにファイルがある場合
        mediaPlayer = MediaPlayer.create(this, R.raw.yume);
        // 音量調整を端末のボタンに任せる
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        fileCheck = true;

        return fileCheck;
    }
    private void audioPlay() {
       // audioPlay();
        if (mediaPlayer == null) {
            // audio ファイルを読出し
            if (audioSetup()){
                //
            }
            else{
                Toast.makeText(getApplication(), "Error: read audio file", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        else{
            // 繰り返し再生する場合
            mediaPlayer.stop();
            mediaPlayer.reset();
            // リソースの解放
            mediaPlayer.release();
        }

        // 再生する
        mediaPlayer.start();

        // 終了を検知するリスナー


    }

    @Override
    protected void onPause(){
            super.onPause();

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

        TextView moTxt = findViewById(R.id.ma);
        int id = view.getId();

        if (id == R.id.button1) {
            setAnswerValue(limit);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                checkResult(true);
                disableAllBtn();
            }
        } else if (id == R.id.button2) {
            setAnswerValue(limit);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                checkResult(false);
                disableAllBtn();
            }
        } else if (id == R.id.button3) {
            mo= MO_INIT;
            moTxt.setText(String.valueOf(mo));
            setQuestionValue(limit);
            clearAnswerValue();
            clearScoreValue();
            resetBgColor();
            win = 0;
            lose = 0;
            draw=0;
            winningRate=0;
            String winRateStr = String.format("%.1f", winningRate);
            TextView txtWinrate = (TextView) findViewById(R.id.text_winrate);
            txtWinrate.setText(" 勝率:"+ winRateStr + "%");

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

    private boolean IsGameOver (int life) {
        if (life <= 0){
            return true;
        }
        return false;
    }

    private void transit2GameOver() {
        Intent intent=new Intent(this,gameover.class);
        startActivity(intent);
        finish();

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

    private void disableAllBtn(){
        Button btn1 = findViewById(R.id.button1);
        Button btn2 = findViewById(R.id.button2);
        Button btn3 = (Button) findViewById(R.id.button3);
        Button btnSetting = findViewById(R.id.settingBtn);

        btn1.setEnabled(false);
        btn2.setEnabled(false);
        btn3.setEnabled(false);
        btnSetting.setEnabled(false);
    }

    private void enableAllBtn(){
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
        TextView textView= findViewById(R.id.ma);


        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        int question = Integer.parseInt(txtViewQuestion.getText().toString());
        int answer = Integer.parseInt(txtViewAnswer.getText().toString());

        TextView txtResult = (TextView) findViewById(R.id.text_result);
        TextView txtWinrate = (TextView) findViewById(R.id.text_winrate);


        // 結果を示す文字列を入れる変数を用意
        String result;
        int score;
        String winrate;

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = "WIN";
                score = 2;
                win += 1;
                winningRate = (win/(win+lose+draw))*100;
                String winRateStr = String.format("%.1f", winningRate);
                txtWinrate.setText(" 勝率:"+ winRateStr + "%");
                vibrator.vibrate(VibrationEffect.createOneShot(
                        500, VibrationEffect.DEFAULT_AMPLITUDE));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(
                            (VibrationEffect.createOneShot(
                            500, VibrationEffect.DEFAULT_AMPLITUDE)));
                }
                soundPool.play(soundpinpon2, 1.0f, 1.0f, 0, 0, 1);


            } else if (question > answer) {
                result = "LOSE";
                score = -1;
                lose += 1;
                winningRate = (win/(win+lose+draw))*100;
                String winRateStr = String.format("%.1f", winningRate);
                txtWinrate.setText(" 勝率:"+ winRateStr + "%");
                vibrator.vibrate(VibrationEffect.createOneShot(
                        1000, VibrationEffect.DEFAULT_AMPLITUDE));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createWaveform(new long[]{0, 100, 50, 100}, -1));
                }
                soundPool.play(soundbubbu1, 1.0f, 1.0f, 0, 0, 1);
            } else {
                result = "DRAW";
                score = 1;
                draw += 1;
                winningRate = (win/(win+lose+draw))*100;
                String winRateStr = String.format("%.1f", winningRate);
                txtWinrate.setText(" 勝率:"+ winRateStr + "%");
            }

        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                win += 1;
                winningRate = (win/(win+lose+draw))*100;
                String winRateStr = String.format("%.1f", winningRate);
                txtWinrate.setText(" 勝率:"+ winRateStr + "%");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(
                            500, VibrationEffect.DEFAULT_AMPLITUDE));
                }
                soundPool.play(soundpinpon2, 1.0f, 1.0f, 0, 0, 1);
            } else if (question < answer) {
                result = "LOSE";
                score = -1;
                lose += 1;
                winningRate = (win/(win+lose+draw))*100;
                String winRateStr = String.format("%.1f", winningRate);
                txtWinrate.setText(" 勝率:"+ winRateStr + "%");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createWaveform(new long[]{0, 100, 50, 100}, -1));
                }
                soundPool.play(soundbubbu1, 1.0f, 1.0f, 0, 0, 1);
            } else {
                result = "DRAW";
                score = 1;
                draw += 1;
                winningRate = (win/(win+lose+draw))*100;
                String winRateStr = String.format("%.1f", winningRate);
                txtWinrate.setText(" 勝率:"+ winRateStr + "%");
            }
        }

        //持ち点変更
        mo += score;
        textView.setText(String.valueOf(mo));

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

    private void setNextQuestion() {
        // 第１引数がカウントダウン時間、第２引数は途中経過を受け取る間隔
        // 単位はミリ秒（1秒＝1000ミリ秒）
        new CountDownTimer(2000, 1000) {
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
                TextView ansView = (TextView) findViewById(R.id.answer);
                ansView.setText(getResources().getString(R.string.label_answer));

                //全ボタンを有効化
                enableAllBtn();
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

