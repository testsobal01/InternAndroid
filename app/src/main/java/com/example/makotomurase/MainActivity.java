package com.example.makotomurase;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView ans;
    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;
    ImageView imageView;



    private SoundPool soundPool;


    private static Context context;
    private static int koukaon;

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

        ans = (TextView) findViewById(R.id.answer);
        pref = getSharedPreferences("InternAndroid",MODE_PRIVATE);
        prefEditor = pref.edit();

        // 起動時に関数を呼び出す
        setQuestionValue();


        TextView textView = (TextView)findViewById(R.id.text_score);
        ImageView imageView2 = findViewById(R.id.imageview);
        imageView2.setImageResource(R.drawable.base);


        String readText = pref.getString("text_input","保存されていません");
        textView.setText(readText);

        AudioAttributes attr = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attr)
                .setMaxStreams(1)
                .build();
        koukaon = soundPool.load(this,R.raw.koukaon,1);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        TextView textView;
        switch (id) {
            case R.id.button1:
                setAnswerValue();
                checkResult(true);
                textView = (TextView)findViewById(R.id.text_score);

                prefEditor.putString("text_input",textView.getText().toString());
                prefEditor.commit();
                Sound();
                break;
            case R.id.button2:
                setAnswerValue();
                checkResult(false);
                textView = (TextView)findViewById(R.id.text_score);

                prefEditor.putString("text_input",textView.getText().toString());
                prefEditor.commit();
                Sound();
                break;
            case R.id.button3:
                setQuestionValue();
                clearAnswerValue();
                textView = (TextView)findViewById(R.id.text_score);

                prefEditor.putString("text_input",textView.getText().toString());
                prefEditor.commit();
                Sound();
                break;

        }
        Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vib.vibrate(250);
    }

    @Override

    protected void onPause() {
        super.onPause();
        Toast.makeText(this,"onPause",Toast.LENGTH_SHORT).show();

    }

    @Override

    protected void onResume() {
        super.onResume();
        Toast.makeText(this,"onResume",Toast.LENGTH_SHORT).show();


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
                score = 2;
                txtViewQuestion.setBackgroundColor(Color.RED);
                txtViewAnswer.setBackgroundColor(Color.RED);
                startRotation();
            } else if (question > answer) {
                result = "LOSE";
                score = -1;
                blinkText(ans, 100, 200);
                txtViewQuestion.setBackgroundColor(Color.BLUE);
                txtViewAnswer.setBackgroundColor(Color.BLUE);
            } else {
                result = "DRAW";
                score = 1;
                blinkText(ans, 100, 200);
                txtViewQuestion.setBackgroundColor(Color.GREEN);
                txtViewAnswer.setBackgroundColor(Color.GREEN);
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                txtViewQuestion.setBackgroundColor(Color.RED);
                txtViewAnswer.setBackgroundColor(Color.RED);
                startRotation();
            } else if (question < answer) {
                result = "LOSE";
                score = -1;
                blinkText(ans, 100, 200);
                txtViewQuestion.setBackgroundColor(Color.BLUE);
                txtViewAnswer.setBackgroundColor(Color.BLUE);
            } else {
                result = "DRAW";
                score = 1;
                blinkText(ans, 100, 200);
                txtViewQuestion.setBackgroundColor(Color.GREEN);
                txtViewAnswer.setBackgroundColor(Color.GREEN);
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
        int oldscore =0;
        if(!txtScore.getText().toString().equals("保存されていません")){
            oldscore = Integer.parseInt(txtScore.getText().toString());
        }
        int newScore = oldscore + score;
        txtScore.setText(Integer.toString(newScore));

        if(newScore > 10){
            imageView = findViewById(R.id.image_view);
            imageView.setImageResource(R.drawable.img_congratulations);
            imageView.setVisibility(View.VISIBLE);
        }
    }

    private void Sound(){
        soundPool.play(koukaon, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    private void startRotation() {

        // RotateAnimation(float fromDegrees, float toDegrees, int pivotXType, float pivotXValue, int pivotYType,float pivotYValue)
        RotateAnimation rotate = new RotateAnimation(0.0f, 360.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        // animation時間 msec
        rotate.setDuration(1000);
        // 繰り返し回数
        rotate.setRepeatCount(1);
        // animationが終わったそのまま表示にする
        rotate.setFillAfter(true);

        //アニメーションの開始
        ans.startAnimation(rotate);

    }

    private void blinkText(TextView ans, long duration, long offset){
        Animation anm = new AlphaAnimation(0.0f, 1.0f);
        anm.setDuration(duration);
        anm.setStartOffset(offset);
        anm.setRepeatMode(Animation.REVERSE);
        anm.setRepeatCount(Animation.INFINITE);
        ans.startAnimation(anm);
    }


}