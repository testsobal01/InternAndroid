package com.example.makotomurase;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import javax.xml.transform.Result;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.animation.ValueAnimator;
import android.util.TypedValue;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
  


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;
  
    private Vibrator vib;
  
    private SoundPool soundPool;
    private int soundOne, soundTwo;
    private MediaPlayer mediaPlayer;
    private Button button1, button2, button3;

    private int maxnum = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Bundle extra = intent.getExtras();
        String intentString = extra.getString("START");

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

        TextView textValue = (TextView) findViewById(R.id.SetTo);
        textValue.setText("It is set to 10");


        // AudioAttributes 設定
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION) // 効果音向け
                .build();

        // SoundPool 初期化
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(2)
                .build();

        // 効果音をロード
        soundOne = soundPool.load(this, R.raw.hit, 1);
        soundTwo = soundPool.load(this, R.raw.kira, 1);

        setVolumeControlStream((AudioManager.STREAM_MUSIC));
        mediaPlayer=MediaPlayer.create(this,R.raw.game_bgm);
        if (mediaPlayer !=null) {
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }

        pref = getSharedPreferences("SaveValue", MODE_PRIVATE);
        prefEditor = pref.edit();

        // 起動時に関数を呼び出す
        setQuestionValue();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button1) {
            soundPool.play(soundOne, 1.0f, 1.0f, 0, 0, 1.0f);
            Vibrator vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VibrationEffect.createOneShot(
                    150, VibrationEffect.DEFAULT_AMPLITUDE));
            setAnswerValue();
            checkResult(true);

        } else if (id == R.id.button2) {
            soundPool.play(soundTwo, 1.0f, 1.0f, 0, 0, 1.0f);
            Vibrator vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VibrationEffect.createOneShot(
                    150, VibrationEffect.DEFAULT_AMPLITUDE));

            setAnswerValue();
            checkResult(false);
        } else if (id == R.id.button3) {
            Vibrator vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VibrationEffect.createOneShot(
                    150, VibrationEffect.DEFAULT_AMPLITUDE));

            setQuestionValue();
            clearAnswerValue();
            clearScoreValue();
        } else if (id == R.id.button4) {
            MyNumberPicker();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        int savevalue = 0;
        TextView SaveScore = (TextView) findViewById(R.id.text_score);
        String savescore = (String) SaveScore.getText();
        try {
            savevalue = Integer.parseInt(savescore);
        } catch (NumberFormatException e) {
            System.err.println("数値に変換できません： " + e.getMessage());
        }

        prefEditor.putInt("Save", savevalue);
        prefEditor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        int readvalue = pref.getInt("Save", 0);
        TextView ReadScore = (TextView) findViewById(R.id.text_score);
        ReadScore.setText(Integer.toString(readvalue));
    }

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText("値2");

        android.view.View mainLayout = findViewById(R.id.main);
        if (mainLayout != null) {
            mainLayout.setBackgroundColor(android.graphics.Color.parseColor("#FFFFFF"));
        }
    }

    private void animateTextSize(final TextView textView, float fromSize, float toSize) {
        ValueAnimator animator = ValueAnimator.ofFloat(fromSize, toSize);
        animator.setDuration(300); // 0.3秒で変化
        animator.addUpdateListener(animation -> {
            float animatedValue = (float) animation.getAnimatedValue();
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, animatedValue);
        });
        animator.start();
    }
    private void animateTextColor(final TextView textView, int fromColor, int toColor) {
        ValueAnimator colorAnim = ValueAnimator.ofObject(new ArgbEvaluator(), fromColor, toColor);
        colorAnim.setDuration(500); // 0.5秒で色変化
        colorAnim.addUpdateListener(animator ->
                textView.setTextColor((int) animator.getAnimatedValue())
        );
        colorAnim.start();
    }


    private void setQuestionValue() {
        Random r = new Random();
        // 0から10の範囲で乱数を生成（+1する必要がある）
        int questionValue = r.nextInt(maxnum + 1);

        TextView txtView = findViewById(R.id.question);
        txtView.setText(Integer.toString(questionValue));
        // 20sp → 48sp にアニメーション
        animateTextSize(txtView, 20, 48);
        // 黒 → 赤 に変化
        animateTextColor(txtView, Color.BLACK, Color.BLACK);
    }

    private void setAnswerValue() {
        Random r = new Random();
        int answerValue = r.nextInt(maxnum + 1);

        TextView txtView = findViewById(R.id.answer);
        txtView.setText(Integer.toString(answerValue));
        animateTextSize(txtView, 20, 48);

        animateTextColor(txtView, Color.BLACK, Color.BLACK);
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
                result = getString(R.string.win);
                score = 2;
                animateTextSize(txtResult, 20, 30);
            } else if (question > answer) {
                result = getString(R.string.lose);
                score = -1;
                animateTextSize(txtResult, 20, 20);
            } else {
                result = getString(R.string.draw);
                score = 1;
                animateTextSize(txtResult, 20, 25);
            }
        } else {
            if (question > answer) {
                result = getString(R.string.win);
                score = 2;
                animateTextSize(txtResult, 20, 30);
            } else if (question < answer) {
                result = getString(R.string.lose);
                score = -1;
                animateTextSize(txtResult, 20, 20);
            } else {
                result = getString(R.string.draw);
                score = 1;
                animateTextSize(txtResult, 20, 25);
            }
        }

        Toast.makeText(this, result, Toast.LENGTH_LONG).show();


        android.view.View mainLayout = findViewById(R.id.main);
        if (mainLayout != null) {
            if (result.equals(getString(R.string.win))) {
                mainLayout.setBackgroundColor(android.graphics.Color.parseColor("#ff7fff"));  // 勝ち：赤
            } else if (result.equals(getString(R.string.lose))) {
                mainLayout.setBackgroundColor(android.graphics.Color.parseColor("#7fbfff")); // 負け：青
            } else if (result.equals(getString(R.string.draw))) {
                mainLayout.setBackgroundColor(android.graphics.Color.parseColor("#7fff7f")); // 引き分け：緑
            }
        }
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

    private int MyNumberPicker() {
        NumberPicker numberPicker = new NumberPicker(this);

        numberPicker.setMaxValue(50);
        numberPicker.setMinValue(10);


        new AlertDialog.Builder(MainActivity.this)
                .setTitle("最大値を設定してください")
                .setView(numberPicker)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                         maxnum = numberPicker.getValue();
                        TextView NewtextValue = (TextView) findViewById(R.id.SetTo);
                        String numax = "It is set to " + Integer.toString(maxnum);
                        NewtextValue.setText(numax);
                    }
                })
                .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();

        return maxnum;
    }
}


