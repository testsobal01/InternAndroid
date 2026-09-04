package com.example.makotomurase;

import static androidx.core.view.ViewCompat.setBackgroundTintList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import androidx.appcompat.app.AlertDialog;
import android.widget.NumberPicker;

import android.content.res.ColorStateList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private jp.codeforfun.catchtheball.SoundPlayer soundPlayer;

    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;
    private Runnable runnable;
    private Handler handler = new Handler(Looper.getMainLooper());
    //今の乱数の上限値
    private int maxValue = 10;
    private final Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        soundPlayer = new jp.codeforfun.catchtheball.SoundPlayer(this);

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

        Button btn4 = findViewById(R.id.button4);
        btn4.setOnClickListener(this);

        // 起動時に関数を呼び出す
        setQuestionValue();

        //プリファレンスの生成 "ScoreStorage"は保存する先のファイル名
        pref = getSharedPreferences("ScoreStorage", MODE_PRIVATE);
        prefEditor = pref.edit();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button1) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VibrationEffect.createOneShot(
                    500, VibrationEffect.DEFAULT_AMPLITUDE));
        }else if (view.getId() == R.id.button2) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VibrationEffect.createOneShot(
                    500, VibrationEffect.DEFAULT_AMPLITUDE));
        }else if (view.getId() == R.id.button3) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VibrationEffect.createOneShot(
                    500, VibrationEffect.DEFAULT_AMPLITUDE));
        }
        int id = view.getId();
        if (id == R.id.button1) {
            setAnswerValue();
            checkResult(true);
           // soundPlayer.playDramSound();
        } else if (id == R.id.button2) {
            setAnswerValue();
            checkResult(false);
            //soundPlayer.playDramSound();
        } else if (id == R.id.button3) {
            //restart押す後左側の乱数3秒後更新しない
            cancelNextQuestion();

            setQuestionValue();
            clearAnswerValue();
            clearScoreValue();
            //soundPlayer.playButtonSound();
        }else if(id == R.id.button4){
            showSettingDialog();
        }
    }

    //プリファレンスの保存
    @Override
    protected void onPause(){
        super.onPause();
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();

        //scoreを保存するため、テキストビューを取得
        //textViewという名前は大丈夫ですか
        TextView textView = (TextView)findViewById(R.id.text_score);
        //"main_score"というキー名に、score(string)を保存
        prefEditor.putString("main_score", textView.getText().toString());
        prefEditor.commit();
    }

    //プリファレンスの読み込み
    @Override
    protected void onResume(){
        super.onResume();
        Log.d("AndroidTest", "onResume completed.");
        //画面上にscoreをセットするため、テキストビューを取得
        TextView textView = (TextView) findViewById(R.id.text_score);
        //一度も保存されていない場合もありますのて、その時に変わりに表示する文字列も指定する
        String readText = pref.getString("main_score", "0");
        textView.setText(readText);
    }


    private void showSettingDialog(){
        //create NumberPicker
        NumberPicker numberPicker = new NumberPicker(this);

        //setting上限は10~50
        numberPicker.setMinValue(10);
        numberPicker.setMaxValue(50);

        //今の設定値を表示する
        numberPicker.setValue(maxValue);

        //50に着いた時10に戻らない
        numberPicker.setWrapSelectorWheel(false);

        new AlertDialog.Builder(this)
                .setTitle(R.string.setting_dialog_title)
                .setView(numberPicker)

                //OK押すと設定値を保存する
                .setPositiveButton(R.string.btn_ok, (dialog, which)->{
                    maxValue = numberPicker.getValue();
                    updateMaxValueText();
                })
                //cancel
                .setNegativeButton(R.string.btn_cancel, null)
                .show();
    }

    private void updateMaxValueText(){
        TextView textMaxVlue = findViewById(R.id.text_max_value);
        textMaxVlue.setText(
                getString(R.string.max_value_message, maxValue)
        );
    }

    //3秒更新をキャンセルする
    private void cancelNextQuestion(){
        if(runnable != null){
            handler.removeCallbacks(runnable);
            runnable = null;
        }
    }

    private void TextAnimator(TextView txtView){
        // 現在の文字色を取得
        int baseColor = txtView.getCurrentTextColor();
        // 不透明な色（アルファ255）と、完全透明な色（アルファ0）を作る
        int opaqueColor = baseColor | 0xFF000000;      // 不透明
        int transparentColor = baseColor & 0x00FFFFFF; // 完全透明
        // ofInt(対象オブジェクト, プロパティ名, 開始色, 終了色)
        ObjectAnimator animator = ObjectAnimator.ofInt(
                txtView,
                "textColor",
                opaqueColor,
                transparentColor
        );
        // 色の補間（グラデーション変化）にArgbEvaluatorを設定
        animator.setEvaluator(new ArgbEvaluator());
        // 点滅の設定
        animator.setDuration(500); // 変化にかかる時間（ミリ秒）
        animator.setRepeatCount(ValueAnimator.INFINITE); // 無限に繰り返す
        animator.setRepeatMode(ValueAnimator.REVERSE); // 交互に反転再生
        // アニメーション開始
        animator.start();
        // 3秒後（3000ミリ秒）にアニメーションを止める ->button押す時止まる（目標）
        txtView.postDelayed(() -> {
            animator.cancel();
            // 止めたときに消えたままになるのを防ぐため、しっかりと見える状態（元の色）に戻す
            txtView.setTextColor(opaqueColor);
        }, 1000);
    }

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText("?");
    }

    private void setQuestionValue() {
        // 乱数を生成（+1する必要がある）
        int questionValue = random.nextInt(maxValue + 1);

        TextView txtView = findViewById(R.id.question);
        txtView.setText(Integer.toString(questionValue));
    }

    private void setAnswerValue() {
        int answerValue = random.nextInt(maxValue + 1);

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

                //背景色の設定をどこに適応させるかを書く
                txtViewQuestion.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.win_normal)));
                txtViewAnswer.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.win_normal)));
                TextAnimator(txtViewAnswer);
                soundPlayer.playYheeeSound();
            } else if (question > answer) {
                result = "LOSE";
                score = -1;

                txtViewQuestion.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.lose_normal)));
                txtViewAnswer.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.lose_normal)));
                TextAnimator(txtViewQuestion);
                soundPlayer.playShockSound();
            } else {
                result = "DRAW";
                score = 1;

                txtViewAnswer.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.draw_normal)));
                txtViewQuestion.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.draw_normal)));
                soundPlayer.playDoutenSound();
            }

        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;

                txtViewQuestion.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.win_normal)));
                txtViewAnswer.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.win_normal)));
                TextAnimator(txtViewAnswer);
                soundPlayer.playYheeeSound();
            } else if (question < answer) {
                result = "LOSE";
                score = -1;

                txtViewQuestion.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.lose_normal)));
                txtViewAnswer.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.lose_normal)));
                TextAnimator(txtViewQuestion);
                soundPlayer.playShockSound();
            } else {
                result = "DRAW";
                score = 1;
                soundPlayer.playDoutenSound();

                txtViewQuestion.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.draw_normal)));
                txtViewAnswer.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.draw_normal)));
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
}


    // ボタンのクリックイベント（リスナー）を設定



