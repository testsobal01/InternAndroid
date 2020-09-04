package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.animation.Animator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity<zoom> extends AppCompatActivity implements View.OnClickListener {

    private TextView tv;


    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;
    private SoundPool soundPool;
    private int winSoundId;//勝利時の効果音id
    private int loseSoundId;//敗北時の効果音id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1 = (Button) findViewById(R.id.button1);
        btn1.setOnClickListener(this);

        Button btn2 = (Button) findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(this);

        tv = findViewById(R.id.answer);


        // 起動時に関数を呼び出す
        setQuestionValue();

        pref = getSharedPreferences("AndroidSeminor", MODE_PRIVATE);
        prefEditor = pref.edit();

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(2)
                .build();
        winSoundId = soundPool.load(this, R.raw.winsound1, 1);
        loseSoundId = soundPool.load(this, R.raw.losesound1, 1);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.button1:
                myVivrator(250);
                setAnswerValue();
                checkResult(true);
                break;
            case R.id.button2:
                myVivrator(250);
                setAnswerValue();
                checkResult(false);
                break;
            case R.id.button3:
                myVivrator(250);
                setQuestionValue();
                clearAnswerValue();
                break;

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        //onPause時にtext_scoreを読み込んでmain_score_inputにString型で保存
        TextView textView = (TextView) findViewById(R.id.text_score);
        prefEditor.putString("main_score_input", textView.getText().toString());
        prefEditor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //onResume時にmain_score_inputからscoreを読んで,text_scoreにセット
        TextView textView = (TextView) findViewById(R.id.text_score);
        String readText = pref.getString("main_score_input", "0");
        textView.setText(readText);
    }

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText("値2");
    }

    private void setQuestionValue() {
        Random r = new Random();
        // 0から10の範囲で乱数を生成（+1する必要がある）
        int questionValue = r.nextInt(10 + 1);
        TextView txtView = (TextView) findViewById(R.id.question);
        txtView.setText(Integer.toString(questionValue));
    }

    private void setAnswerValue() {
        Random r = new Random();
        int answerValue = r.nextInt(10 + 1);
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText(Integer.toString(answerValue));
    }

    private void checkResult(boolean isHigh) {
        TextView txtViewQuestion = (TextView) findViewById(R.id.question);
        TextView txtViewAnswer = (TextView) findViewById(R.id.answer);
        int question = Integer.parseInt(txtViewQuestion.getText().toString());
        int answer = Integer.parseInt(txtViewAnswer.getText().toString());
        TextView txtResult = (TextView) findViewById(R.id.text_result);
        // 結果を示す文字列を入れる変数を用意
        String result;
        int score = 0;

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = "WIN";
                sound(result);
                score = 2;
                startScale();
                //startZoomXml();
            } else if (question > answer) {
                result = "LOSE";
                sound(result);
                score = -1;
                startRotation();
            } else {
                result = "DRAW";
                score = 1;
            }
        } else {
            if (question > answer) {
                result = "WIN";
                sound(result);
                startScale();
                score = 2;
                //startZoomXml();
            } else if (question < answer) {
                result = "LOSE";
                sound(result);
                score = -1;
                startRotation();
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

    public void myVivrator(int time) {
        Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vib.vibrate(time);
    }

    public void sound(String result) {
        if (result.equals("WIN")) {
            soundPool.play(winSoundId, 100, 1f, 0, 0, 1f);
        } else if (result.equals("LOSE")) {
            soundPool.play(loseSoundId, 100, 1f, 0, 0, 1f);
        }
    }

    /*private void startZoomXml() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoom_animation);
        tv.startAnimation(animation);
    }*/

    private void startScale() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                1.0f, 4.0f, 1.0f,4.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(500);
        scaleAnimation.setRepeatCount(5);
        scaleAnimation.setRepeatMode(Animation.REVERSE);
        tv.startAnimation(scaleAnimation);
    }

    private void startRotation() {

        // RotateAnimation(float fromDegrees, float toDegrees, int pivotXType, float pivotXValue, int pivotYType,float pivotYValue)
        RotateAnimation rotate = new RotateAnimation(0.0f, 180.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        rotate.setDuration(3);
        rotate.setRepeatCount(0);
        rotate.setFillAfter(true);

        tv.startAnimation(rotate);





    }
}
