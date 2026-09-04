package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.app.ActivityManager;
import android.graphics.Color;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.BreakIterator;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SoundPool soundpool;
    private int soundId;

    AnimatorSet set;
    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

    private int maxValue = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Bundle extra = intent.getExtras();
        String intentString = extra.getString("Top");



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

        // 設定ボタン
        Button btnSetting = findViewById(R.id.button_Setting);
        btnSetting.setOnClickListener(this);

        // 追加
        pref = getSharedPreferences("Save", MODE_PRIVATE);
        prefEditor = pref.edit();

        TextView textView= findViewById(R.id.answer);
        //AnimatorInflaterで、AnimatorSetオブジェクトを取得
        //前もって作成したR.animator.blink_animationをインフレート
        set = (AnimatorSet) AnimatorInflater.loadAnimator(MainActivity.this,
                R.animator.blink_animation);
        //アニメーション対称のオブジェクトを設定
        set.setTarget(textView);

        // 起動時に関数を呼び出す
        setQuestionValue();

        //効果音
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        soundpool = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build();

        soundId = soundpool.load(this, R.raw.click, 1);

        //再生

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button1) {
            setAnswerValue();
            checkResult(true);
            setSoundpool();
        } else if (id == R.id.button2) {
            setAnswerValue();
            checkResult(false);
            setSoundpool();
        } else if (id == R.id.button3) {
            setQuestionValue();
            clearAnswerValue();
            clearScoreValue();

            TextView textViewQ = (TextView) findViewById(R.id.question);
            textViewQ.setBackgroundColor(Color.parseColor("#ff00ff"));

            TextView textViewA = (TextView) findViewById(R.id.answer);
            textViewA.setBackgroundColor(Color.parseColor("#ffff00"));

            setSoundpool();
        } else if (id == R.id.button_Setting) {
        // 設定ボタンの処理をこちらに移動しました
        showSettingDialog();
        }



    }

private void showSettingDialog() {
    NumberPicker picker = new NumberPicker(this);
    picker.setMinValue(10);
    picker.setMaxValue(50);
    picker.setValue(maxValue);

    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("最大値を設定してください");
    builder.setView(picker);
    builder.setPositiveButton("OK", (dialog, which) -> {
        maxValue = picker.getValue(); // getMaxValue() から getValue() に修正

        TextView txtSetting = findViewById(R.id.text_Setting);
        if (txtSetting != null) {
            txtSetting.setText("最大値 : " + maxValue);
        }

        // 新しい最大値で問題を再設定
        setQuestionValue();
    });
    builder.setNegativeButton("キャンセル", null);
    builder.show();
}

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText("値2");
    }

    private void setQuestionValue() {
        Random r = new Random();
        // 0から10の範囲で乱数を生成（+1する必要がある）
        int questionValue = r.nextInt(maxValue + 1);

        TextView txtView = findViewById(R.id.question);
        txtView.setText(Integer.toString(questionValue));
    }

    private void setAnswerValue() {
        Random r = new Random();
        int answerValue = r.nextInt(maxValue + 1);

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

                TextView txtView = findViewById(R.id.answer);
                txtView.setBackgroundColor(Color.parseColor("#ff0014"));
                if (set != null && !set.isRunning()) {
                    set.start();
                }
                TextView TxtView = findViewById(R.id.question);
                TxtView.setBackgroundColor(Color.parseColor("#96000a"));

            } else if (question > answer) {
                result = "LOSE";
                score = -1;
                TextView txtView = findViewById(R.id.answer);
                txtView.setBackgroundColor(Color.parseColor("#96000a"));
                TextView TxtView = findViewById(R.id.question);
                TxtView.setBackgroundColor(Color.parseColor("#ff0014"));
            } else {
                result = "DRAW";
                score = 1;
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                TextView txtView = findViewById(R.id.answer);
                txtView.setBackgroundColor(Color.parseColor("#005dff"));
                if (set != null && !set.isRunning()) {
                    set.start();
                }
                TextView TxtView = findViewById(R.id.question);
                TxtView.setBackgroundColor(Color.parseColor("#002560"));

            } else if (question < answer) {
                result = "LOSE";
                score = -1;
                TextView txtView = findViewById(R.id.answer);
                txtView.setBackgroundColor(Color.parseColor("#002560"));
                TextView TxtView = findViewById(R.id.question);
                TxtView.setBackgroundColor(Color.parseColor("#005dff"));
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

        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }

    private void setSoundpool(){
        soundpool.play(soundId, 1.0f, 1.0f, 0,0, 1.0f);
    }

    @Override
    public void onPause(){
        super.onPause();
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();

        TextView textView = (TextView)findViewById(R.id.text_score);
        prefEditor.putString("score_input", textView.getText().toString());
        prefEditor.commit();
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("AndroidTest","onResume completed");
        TextView textView = (TextView)findViewById(R.id.text_score);
        String readText = pref.getString("score_input", "0");
        textView.setText(readText);
    }
}