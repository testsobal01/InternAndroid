package com.example.makotomurase;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.annotation.SuppressLint;
import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Context;
import android.media.SoundPool;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;
    private MediaPlayer mediaPlayer;

    //AnimatorSetオブジェクトを宣言
    /**
     * アニメーションに利用するセットを宣言
     * blink：テキストの点滅、scale：テキストのサイズ変化
     */
    AnimatorSet blink;
    AnimatorSet scale;

    // 効果音
    private static SoundPool soundPool;
//    private SoundPlayer soundPlayer;
    private static int correct_answer1 = 1;
    private static int blip01 = 1;

    @SuppressLint("MissingSuperCall")
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

        View layout = findViewById(R.id.layout);
        layout.setBackgroundColor(0xFFFFFF);

        Button btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(this);

        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(this);


        SoundPlayer(this);
        //soundPlayer = new SoundPlayer(this);
        //SoundPool soundPlayer = SoundPlayer(this);
        pref = getSharedPreferences("MakotoMurase",MODE_PRIVATE);
        prefEditor = pref.edit();

        // 起動時に関数を呼び出す
        setQuestionValue();

        //番号11　BGMの追加
        Button buttonStart = findViewById(R.id.start);
        buttonStart.setOnClickListener( v ->  {
            // 音楽再生
            audioPlay();
        });

        Button buttonStop = findViewById(R.id.stop);
        buttonStop.setOnClickListener( v -> {
            if (mediaPlayer != null) {
                audioStop();
            }
        });

        ImageView fooder=findViewById(R.id.footer);
        fooder.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(),"\uD83E\uDEF5ω・´)<貴様ッ！なぜわかった！",Toast.LENGTH_SHORT).show();
        });
        //  もし何かフッター触ったときにに入れたいのなら{}の中身をいじろう(番号9)


        //テキストビューを取得
        TextView player = (TextView) findViewById(R.id.answer);

        //プレイヤーのテキストビューにアニメーションを設定
        blink = (AnimatorSet) AnimatorInflater.loadAnimator(MainActivity.this, R.animator.blink_animation);
        blink.setTarget(player);
        scale = (AnimatorSet) AnimatorInflater.loadAnimator(MainActivity.this, R.animator.scale_animation);
        scale.setTarget(player);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button1) {
            setAnswerValue();
            checkResult(true);

            // 番号4 バイブレーション機能
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VibrationEffect.createOneShot(3000, VibrationEffect.DEFAULT_AMPLITUDE));

        } else if (id == R.id.button2) {
            setAnswerValue();
            checkResult(false);
        } else if (id == R.id.button3) {
            setQuestionValue();
            clearAnswerValue();
            clearScoreValue();
        }



    }

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText("値2");
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


    public void SoundPlayer(Context context) {

        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);

        correct_answer1 = soundPool.load(context, R.raw.correct_answer1, 0);
        blip01 = soundPool.load(context, R.raw.blip01, 0);
    }

    public void correctSound() {
        soundPool.play(correct_answer1, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void blipSound() {
        soundPool.play(blip01, 1.0f, 1.0f, 1, 0, 1.0f);
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
                correctSound();

                //テキスト拡大
                scale.start();
            }  else if (question > answer) {
                result = "LOSE";
                score = -1;

                //テキスト点滅
                blink.start();
            } else {
                result = "DRAW";
                score = 1;
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                correctSound();

                //テキスト拡大
                scale.start();
            } else if (question < answer) {
                result = "LOSE";
                score = -1;

                //テキスト点滅
                blink.start();
            } else {
                result = "DRAW";
                score = 1;
            }
        }
        changeBackgroundColor(result);

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

    public void changeBackgroundColor(String result) {
        View layout = findViewById(R.id.layout);

        if (Objects.equals(result, "WIN")) {
            layout.setBackgroundColor(0xFFFF0000);
        } else if (Objects.equals(result, "LOSE")) {
            layout.setBackgroundColor(0xFFAFDFE4);
        } else if (Objects.equals(result, "DRAW")) {
            layout.setBackgroundColor(0xFF00FF00);
        }
    }

    //番号２　プリファレンスにスコアを保存
    @Override
    protected void onPause(){
        super.onPause();
        Toast.makeText(this,"onPause",Toast.LENGTH_SHORT).show();

        TextView textView = (TextView) findViewById(R.id.text_score);

        prefEditor.putString("score",textView.getText().toString());
        prefEditor.commit();
    }
    @Override
    protected void onResume(){
        super.onResume();

        TextView textView = (TextView) findViewById(R.id.text_score);

        String readText = pref.getString("score","0");
        textView.setText(readText);
    }

    //番号11　BGMの追加
    private boolean audioSetup(){
        mediaPlayer = new MediaPlayer();

        String filePath = "music.mp3";

        try(AssetFileDescriptor afdescripter = getAssets().openFd(filePath))
        {
            mediaPlayer.setDataSource(
                    afdescripter.getFileDescriptor(),
                    afdescripter.getStartOffset(),
                    afdescripter.getLength());
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
    private void audioPlay() {

        if (mediaPlayer == null) {
            if (audioSetup()){
                Toast.makeText(getApplication(), "Rread audio file", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplication(), "Error: read audio file", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        else{
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
        }

        mediaPlayer.start();

        mediaPlayer.setOnCompletionListener( mp -> {
            Log.d("debug","end of audio");
            audioStop();
        });

    }
    private void audioStop() {
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();

        mediaPlayer = null;
    }
}

