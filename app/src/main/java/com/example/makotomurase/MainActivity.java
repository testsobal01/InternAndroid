package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Handler;
import android.text.InputType;
import android.widget.EditText;
import java.sql.Time;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.content.Context;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.app.Dialog;
import android.view.Window;


import java.util.Random;

import android.graphics.Color;

import android.view.animation.Animation;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

    private SoundPool soundPool;
    private int soundOne, soundTwo,soundThree;
    private Button button1,button2,button3;
    private TextView timerTextView;
    private Button restartButton;
    private Handler handler;
    private long startTime;
    private long timeLimit = 60000;


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

        FloatingActionButton fabBtn = findViewById(R.id.time_setting);
        fabBtn.setOnClickListener(v -> showEditDialog());

        pref = getSharedPreferences("InternAndroid", MODE_PRIVATE);
        prefEditor = pref.edit();

        // 起動時に関数を呼び出す
        setQuestionValue();

        timerTextView = findViewById(R.id.text_time);
        startTime = System.currentTimeMillis();
        handler = new Handler();
        handler.post(updateTimer);
        restartTimer();
    }

    private void ShowGameOverPopup(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customLayout = inflater.inflate(R.layout.time, null);
        TextView messageTextView = customLayout.findViewById(R.id.timeup);
        messageTextView.setText(R.string.timeup);

       TextView timeUpView = findViewById(R.id.text_time_up);
       timeUpView.setVisibility(View.VISIBLE);

        TextView timeUpView2 = findViewById(R.id.text_time);
        timeUpView2.setVisibility(View.GONE);

    }

    private Runnable updateTimer = new Runnable() {
        @Override
        public void run() {
            long elapsedTime = System.currentTimeMillis() - startTime;
            long remainingTime = timeLimit - elapsedTime;

            if(remainingTime < 0){
                remainingTime = 0;
            }

            int remainingSeconds = (int) (remainingTime / 1000);
            timerTextView.setText("残り時間：" + remainingSeconds);

            if(remainingTime <= 0){
                handler.removeCallbacks(this);
                ShowGameOverPopup();
            }else{
                handler.postDelayed(this,1000);
            }
        }
    };

    @Override
    public void onClick(View view) {

        //効果音
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                // ストリーム数に応じて
                .setMaxStreams(2)
                .build();

        soundOne = soundPool.load(this, R.raw.dodon,1);
//        soundTwo = soundPool.load(this,R.raw.don,1);
//        soundThree = soundPool.load(this,R.raw.kaka,1);
        soundPool.setOnLoadCompleteListener((soundPool, sampleId,status) ->{
            System.out.println("こんにちは");
            soundPool.play(soundOne, 10.0f, 10.0f, 0,0,1);
        });
      
        int id = view.getId();
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
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null) {
                long[] pattern = {0, 100, 100, 100, 100, 100};
                vibrator.vibrate(pattern, -1);

            }
            restartTimer();
        }

    }

    private void restartTimer(){
        handler.removeCallbacks(updateTimer);
        startTime = System.currentTimeMillis();
        resetTimerDisplaying();
        handler.post(updateTimer);
    }

    private void resetTimerDisplaying() {
        timerTextView.setText("残り時間：" + (timeLimit / 1000));

        TextView timeUpView = findViewById(R.id.text_time_up);
        timeUpView.setVisibility(View.GONE);

        TextView timeUpView2 = findViewById(R.id.text_time);
        timeUpView2.setVisibility(View.VISIBLE);
    }

    private void clearAnswerValue() {
        String value2text=getString(R.string.value2text);
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText(value2text);
    }

    private void setQuestionValue() {
        Random r = new Random();
        // 0から10の範囲で乱数を生成（+1する必要がある）
        int questionValue = r.nextInt(10 + 1);

        TextView txtView = findViewById(R.id.question);
        txtView.setText(Integer.toString(questionValue));
    }

    private void setAnswerValue() {
        Random r = new Random();
        int answerValue = r.nextInt(10 + 1);

        TextView txtView = findViewById(R.id.answer);
        txtView.setText(Integer.toString(answerValue));
    }

    private void checkResult(boolean isHigh) {
        TextView txtViewQuestion = findViewById(R.id.question);
        TextView txtViewAnswer = findViewById(R.id.answer);

        int question = Integer.parseInt(txtViewQuestion.getText().toString());
        int answer = Integer.parseInt(txtViewAnswer.getText().toString());

        TextView txtResult = (TextView) findViewById(R.id.text_result);

        TextView textView1 =findViewById(R.id.text_result);
        String title1=getString(R.string.text_result);
        textView1.setText("結果");
        // 結果を示す文字列を入れる変数を用意
        String result;
        int score;

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = "WIN";
                score = 2;
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator != null) {
                    long[] pattern = {0, 100, 100, 100, 100, 100};
                    vibrator.vibrate(pattern, -1);

                }
            } else if (question > answer) {
                result = "LOSE";
                score = -1;
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator != null) {
                    long[] pattern = {0, 500, 500, 500, 500};
                    vibrator.vibrate(pattern, -1);
                }
            } else {
                result = "DRAW";
                score = 1;
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator != null) {
                    long[] pattern = {0, 1000, 1000,};
                    vibrator.vibrate(pattern, -1);

                }
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator != null) {
                    long[] pattern = {0, 100, 100, 100, 100, 100};
                    vibrator.vibrate(pattern, -1);

                }
            } else if (question < answer) {
                result = "LOSE";
                score = -1;
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator != null) {
                    long[] pattern = {0, 500, 500, 500, 500};
                    vibrator.vibrate(pattern, -1);
                }
            } else {
                result = "DRAW";
                score = 1;
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator != null) {
                    long[] pattern = {0, 1000, 1000,};
                    vibrator.vibrate(pattern, -1);

                }
            }
        }
       String result1=getString(R.string.text_result);

        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText(result1+"" + question + ":" + answer + "(" + result + ")");

        // 背景色をランダムに変更する
        changeBackgroundColor();

        //ポップアップ広告
        showPopupAd();
        // 文字を動かす
        alphaAnimation(txtViewAnswer);
        translateAnimation(txtViewQuestion);

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

    private void changeBackgroundColor(){
        LinearLayout txtViewQuestion = findViewById(R.id.question_background);
        LinearLayout txtViewAnswer = findViewById(R.id.answer_background);

        txtViewQuestion.setBackgroundColor(Color.parseColor(randomColorCode()));
        txtViewAnswer.setBackgroundColor(Color.parseColor(randomColorCode()));

    }

    private String randomColorCode(){
        Random r = new Random();
        String colorCode = "#";

        for(int i = 0; i < 8; i++){
            colorCode = colorCode + Integer.toHexString(r.nextInt(16)).toUpperCase();
        }

        return  colorCode;
    }

    private void showPopupAd() {
        Dialog dialog = new Dialog(this);
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_ad_layout);
                Button closeBtn = dialog.findViewById(R.id.close_button);
                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

                ImageView popupImage = dialog.findViewById(R.id.popup_image);
                popupImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = "https://www.sobal.co.jp/";
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                    }
                });
            }
        }.start();
    }

    private void alphaAnimation(TextView txtView){
        AlphaAnimation anm = new AlphaAnimation(0.0f, 1.0f);
        anm.setDuration(50);
        anm.setStartOffset(50);
        anm.setRepeatMode(Animation.REVERSE);
        anm.setRepeatCount(3);
        txtView.startAnimation(anm);
    }

    private void translateAnimation(TextView txtView){

        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -0.01f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -0.0f
        );

        // animation時間 msec
        translateAnimation.setDuration(100);
        // 繰り返し回数
        translateAnimation.setRepeatCount(3);
        // animationが終わったそのまま表示にする
        translateAnimation.setFillAfter(true);
        //アニメーションの開始
        txtView.startAnimation(translateAnimation);
    }

    private void showEditDialog() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText timeLimitEdit = new EditText(this);
        timeLimitEdit.setHint("制限時間(秒)");
        timeLimitEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
        timeLimitEdit.setText(String.valueOf(timeLimit / 1000));
        layout.addView(timeLimitEdit);

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("制限時間(秒)")
                .setView(layout)
                .setPositiveButton("OK", (dialog, which) -> {

                    String timeStr = timeLimitEdit.getText().toString();
                    if (!timeStr.isEmpty()) {
                        long sec = Long.parseLong(timeStr);
                        timeLimit = sec * 1000;
                    }
                    restartTimer();
                })
                .setNegativeButton("キャンセル", null)
                .show();
    }


    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "OnPause", Toast.LENGTH_SHORT).show();

        //テキストビューを取得
        TextView textView = (TextView)findViewById(R.id.text_score);
        //文字列を保存
        prefEditor.putString("main_input",textView.getText().toString());
        prefEditor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "OnResume", Toast.LENGTH_SHORT).show();

        //テキストビューを取得
        TextView textView = (TextView)findViewById(R.id.text_score);
        //保存されていないとき用に表示する文字列を指定
        String readText = pref.getString("main_input", "保存されていません");
        textView.setText(readText);
    }



}
