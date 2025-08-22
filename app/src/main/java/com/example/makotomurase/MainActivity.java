package com.example.makotomurase;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.SharedPreferences;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Insets;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.graphics.Color;
import static java.lang.Math.ceil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.animation.ValueAnimator;
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

        //プリファレンスの生成
        pref = getSharedPreferences("team-g",MODE_PRIVATE);
        prefEditor = pref.edit();

        // 起動時に関数を呼び出す
        setQuestionValue();

        TextView txtSetting = findViewById(R.id.text_setting);
        maxRandomValue = pref.getInt(PREF_KEY_MAX, DEFAULT_MAX_VALUE);
        txtSetting.setText(maxRandomValue + " が設定されています。");


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
        String result;
        int score;

        String lastresultWIN = getResources().getString(R.string.lastresultWIN);
        String lastresultLOSE = getResources().getString(R.string.lastresultLOSE);
        String lastresultDRAW = getResources().getString(R.string.lastresultDRAW);
        View layout1 =findViewById(R.id.question);
        View layout2 = findViewById(R.id.answer);

        // Highが押された
        if (isHigh) {
            isInputEnabled = false;
            // result には結果のみを入れる
            if (question < answer) {
                result = lastresultWIN;
                score = 2;
                Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(400);

                layout1.setBackgroundColor(Color.CYAN);
                layout2.setBackgroundColor(Color.RED);
            } else if (question > answer) {
                result = lastresultLOSE;
                score = -1;
                Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(1000);
                layout1.setBackgroundColor(Color.RED);
                layout2.setBackgroundColor(Color.CYAN);
            } else {
                result = lastresultDRAW;
                score = 1;
                Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(500);
            }
        } else {
            isInputEnabled = false;
            if (question > answer) {
                result = lastresultWIN;
                layout1.setBackgroundColor(Color.RED);
                layout2.setBackgroundColor(Color.CYAN);
                score = 2;
                Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(400);
            } else if (question < answer) {
                result = lastresultLOSE;
                layout1.setBackgroundColor(Color.CYAN);
                layout2.setBackgroundColor(Color.RED);
                score = -1;
                Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(1000);
            } else {
                result = lastresultDRAW;
                layout1.setBackgroundColor(Color.GREEN);
                layout2.setBackgroundColor(Color.GREEN);
                score = 1;
                Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(500);
            }
        }

        if(score == 2 || score == -1) {
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
                    @Override public void onFinish() {
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

    private void score_Save(){
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        int Score = Integer.parseInt(txtScore.getText().toString());
        //プリファレンスの保存
        prefEditor.putInt("pref_score",Score);
        prefEditor.commit();
    }

    private void score_Load(){
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        //プリファレンスの読み込み
        int Pref_Score = pref.getInt("pref_score",0);
        txtScore.setText(Integer.toString(Pref_Score));
    }

    @Override
    protected void onResume() {
        super.onResume();
        score_Load();
    }

    @Override
    protected void onPause() {
        super.onPause();
        score_Save();
    }
    private void clearColorValue() {
        View layout1 =findViewById(R.id.question);
        View layout2 = findViewById(R.id.answer);
        layout1.setBackgroundColor(Color.MAGENTA);
        layout2.setBackgroundColor(Color.YELLOW);
    }

}
