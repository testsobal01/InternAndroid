package com.example.makotomurase;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.media.AudioAttributes;
import android.media.SoundPool;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //効果音
    public SoundPool soundPool;
    public int[] action = { 0,0,0,0 };

    public int max;

    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

    AnimatorSet set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 効果音
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(3)
                .build();
        action[0] = soundPool.load(this, R.raw.button01, 1);
        action[1] = soundPool.load(this, R.raw.button02, 1);
        action[2] = soundPool.load(this, R.raw.win01, 1);
        action[3] = soundPool.load(this, R.raw.lose01, 1);

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

        ImageButton option = (ImageButton) findViewById(R.id.option);
        option.setOnClickListener(this);

        max = 10;
        TextView maxvalue = findViewById(R.id.question);
        maxvalue.setText(max+"が設定されています");
        // 起動時に関数を呼び出す
        setQuestionValue();

        pref = getSharedPreferences("Score", MODE_PRIVATE);
        prefEditor = pref.edit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        TextView textview = (TextView) findViewById(R.id.text_score);

        prefEditor.putString("score_input", textview.getText().toString());
        prefEditor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView textview = (TextView) findViewById(R.id.text_score);

        String readText = pref.getString("score_input","0");
        textview.setText(readText);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        //効果音
        soundPool.play(action[1], 10f , 1f, 0, 0, 1f);

        if(set != null){
            set.cancel();
            set = null;
        }
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
        }else if (id == R.id.option){
            NumberPicker np = new NumberPicker(this);
            np.setMinValue(10);
            np.setMaxValue(50);

            new AlertDialog.Builder(this)
                    .setView(np)
                    .setTitle(R.string.numtitle)
                    .setPositiveButton(R.string.ok, (dialog,which)->{
                        TextView question = findViewById(R.id.question);
                        TextView maxvalue = findViewById(R.id.maxvalue);
                        question.setText(String.valueOf(np.getValue()));
                        maxvalue.setText(np.getValue()+"が設定されています");
                        max = np.getValue();
                    })
                    .setNegativeButton(R.string.canncel, (Dialog, which)->{})
                    .show();
        }
    }

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        String txt = getString(R.string.num2);
        txtView.setText(txt);
    }

    private void setQuestionValue() {
        Random r = new Random();
        // 0から10の範囲で乱数を生成（+1する必要がある）
        int questionValue = r.nextInt(max + 1);

        TextView txtView = findViewById(R.id.question);
        txtView.setText(Integer.toString(questionValue));
    }

    private void setAnswerValue() {
        Random r = new Random();
        int answerValue = r.nextInt(max + 1);

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
                result = getString(R.string.WIN);
                score = 2;
                //効果音
                soundPool.play(action[2], 10f , 1f, 0, 0, 1f);
                txtViewQuestion.setBackgroundColor(Color.parseColor("#FF8C00"));
                txtViewAnswer.setBackgroundColor(Color.parseColor("#FF4500"));
            } else if (question > answer) {
                result = getString(R.string.LOSE);
                score = -1;
                soundPool.play(action[3], 100f , 1f, 0, 0, 1f);
                txtViewQuestion.setBackgroundColor(Color.parseColor("#808080"));
                txtViewAnswer.setBackgroundColor(Color.parseColor("#A9A9A9"));
            } else {
                result = getString(R.string.DRAW);
                score = 1;
                txtViewQuestion.setBackgroundColor(Color.parseColor("#ff00ff"));
                txtViewAnswer.setBackgroundColor(Color.parseColor("#ffff00"));
            }
        } else {
            if (question > answer) {
                result = getString(R.string.WIN);
                score = 2;
                //効果音
                soundPool.play(action[2], 10f , 1f, 0, 0, 1f);
                txtViewQuestion.setBackgroundColor(Color.parseColor("#FF8C00"));
                txtViewAnswer.setBackgroundColor(Color.parseColor("#FF4500"));
            } else if (question < answer) {
                result = getString(R.string.LOSE);
                score = -1;
                soundPool.play(action[3], 100f , 1f, 0, 0, 1f);
                txtViewQuestion.setBackgroundColor(Color.parseColor("#808080"));
                txtViewAnswer.setBackgroundColor(Color.parseColor("#A9A9A9"));
            } else {
                result = getString(R.string.DRAW);
                score = 1;
                txtViewQuestion.setBackgroundColor(Color.parseColor("#ff00ff"));
                txtViewAnswer.setBackgroundColor(Color.parseColor("#ffff00"));
            }
        }


        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText("：" + question + ":" + answer + "(" + result + ")");

        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.result_animation);
        set.setTarget(txtViewAnswer);
        set.start();

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
                Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
               vibrator.vibrate(VibrationEffect.createOneShot(
               1000,VibrationEffect.DEFAULT_AMPLITUDE));
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

