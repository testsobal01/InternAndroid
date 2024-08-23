package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.graphics.Color;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Animation win_animation;
    Animation lose_animation;
    Animation draw_animation;
    TextView animeText1;
    TextView animeText2;
    Animation judgeWin;
    Animation judgeLose;
    Animation judgeDraw;
    TextView judgeText;

    int mp3a;
    int mp3b;
    int mp3c;
    SoundPool soundPool;
    private MediaPlayer mediaPlayer;


          public void play_mp3d(){
              mediaPlayer = MediaPlayer.create(this,R.raw.bgm2);
              mediaPlayer.setLooping(true); //    ループ設定
              mediaPlayer.setVolume( 1f, 1f);
              mediaPlayer.start();
          }
    public void play_mp3a() {
        soundPool.play(mp3a, 1f, 0, 0, 0, 1f);
    }

    ;

    public void play_mp3b() {
        soundPool.play(mp3b, 1f, 0, 0, 0, 1f);
    }

    ;

    public void play_mp3c() {
        soundPool.play(mp3c, 1f, 0, 0, 0, 1f);
    }

    ;
    String text_kekka;
    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

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

        win_animation = AnimationUtils.loadAnimation(this,R.anim.win_animation);
        lose_animation = AnimationUtils.loadAnimation(this,R.anim.lose_animation);
        draw_animation = AnimationUtils.loadAnimation(this,R.anim.draw_animation);
        animeText1=(TextView) findViewById(R.id.answer);
        animeText2=(TextView) findViewById(R.id.question);

        Typeface numFont=Typeface.createFromAsset(getAssets(),"BAUHS93.TTF");
        animeText1.setTypeface(numFont);
        animeText2.setTypeface(numFont);
        // 起動時に関数を呼び出す
        setQuestionValue();

        judgeText=(TextView)findViewById(R.id.judge);
        judgeWin=AnimationUtils.loadAnimation(this,R.anim.judge_win);
        judgeLose=AnimationUtils.loadAnimation(this,R.anim.judge_lose);
        judgeDraw=AnimationUtils.loadAnimation(this,R.anim.judge_draw);
        Typeface judgeFont=Typeface.createFromAsset(getAssets(),"BAUHS93.TTF");
        judgeText.setTypeface(judgeFont);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);

        } else {
            AudioAttributes attr = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(attr)
                    .setMaxStreams(2)
                    .build();
        }
        mp3a = soundPool.load(this, R.raw.button1, 1);
        mp3b = soundPool.load(this, R.raw.button2, 1);
        mp3c = soundPool.load(this, R.raw.button3, 1);



        text_kekka = getString(R.string.kekka);

        ImageView imageView = (ImageView) findViewById(R.id.image_view);

        AssetManager assetManager = getResources().getAssets();
        try {
            InputStream inputStream = assetManager.open("baby-623417_1280.jpg");
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            imageView.setImageBitmap(bitmap);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //プリファレンスの生成
        pref = getSharedPreferences("AndroidSeminor", MODE_PRIVATE);
        prefEditor = pref.edit();

        play_mp3d();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
        TextView text_score = (TextView) findViewById(R.id.text_score);
        prefEditor.putString("score_input", text_score.getText().toString());
        prefEditor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();

        TextView text_score = (TextView) findViewById(R.id.text_score);

        String read_score = pref.getString("score_input", "0");
        text_score.setText(read_score);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button1) {
            Vibrator vid = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vid.vibrate(2000);
        }
        int id = view.getId();
        if (id == R.id.button1) {
            setButton(false);
            setAnswerValue();
            checkResult(true);
            play_mp3a();
        } else if (id == R.id.button2) {
            setButton(false);
            setAnswerValue();
            checkResult(false);
            play_mp3b();
        } else if (id == R.id.button3) {
            setQuestionValue();
            clearAnswerValue();
            clearScoreValue();
            play_mp3c();
        }




    }

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText("?");
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

        // 結果を示す文字列を入れる変数を用意
        String result;
        int score;

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = "WIN";
                score = 2;
                animeText1.startAnimation(win_animation);
                animeText2.startAnimation(lose_animation);
                judgeText.startAnimation(judgeWin);
                backgroundchangeWin();
            } else if (question > answer) {
                result = "LOSE";
                score = -1;
                animeText1.startAnimation(lose_animation);
                animeText2.startAnimation(win_animation);
                judgeText.startAnimation(judgeLose);
                backgroundchangeLose();
            } else {
                result = "DRAW";
                score = 1;
                animeText1.startAnimation(draw_animation);
                animeText2.startAnimation(draw_animation);
                judgeText.startAnimation(judgeDraw);
                backgroundchangeDraw();
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                animeText1.startAnimation(win_animation);
                animeText2.startAnimation(lose_animation);
                judgeText.startAnimation(judgeWin);
                backgroundchangeWin();
            } else if (question < answer) {
                result = "LOSE";
                score = -1;
                animeText1.startAnimation(lose_animation);
                animeText2.startAnimation(win_animation);
                judgeText.startAnimation(judgeLose);
                backgroundchangeLose();
            } else {
                result = "DRAW";
                score = 1;
                animeText1.startAnimation(draw_animation);
                animeText2.startAnimation(draw_animation);
                judgeText.startAnimation(judgeDraw);
                backgroundchangeDraw();
            }

        }
        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        txtResult.setText(text_kekka+ question + ":" + answer + "(" + result + ")");
        judgeText.setText(result);

        // 続けて遊べるように値を更新
        setNextQuestion();
        // スコアを表示
        setScore(score);

        TextView textView = findViewById(R.id.text_score);
        String kekka = textView.getText().toString(); // "1"
        int suji = Integer.parseInt(kekka); // 1

        if (suji > 9) {
            System.out.println("WIN");
            txtViewQuestion.setText("YOUR");
            txtViewAnswer.setText("WIN");
        } else if (suji < -4) {
            System.out.println("LOSE");
            txtViewQuestion.setText("YOUR");
            txtViewAnswer.setText("LOSE");
        }
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
                setButton(true);
                judgeText.setText("");
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



    private void backgroundchangeWin(){
        LinearLayout txtquestion = (LinearLayout) findViewById(R.id.questionBackground);
        LinearLayout txtanswer = (LinearLayout) findViewById(R.id.answerBackground);
        txtquestion.setBackgroundColor(Color.rgb(255, 140, 0));
        txtanswer.setBackgroundColor(Color.rgb(255, 164, 0));
    }

    private void backgroundchangeLose() {
        LinearLayout txtquestion = (LinearLayout) findViewById(R.id.questionBackground);
        LinearLayout txtanswer = (LinearLayout) findViewById(R.id.answerBackground);
        txtquestion.setBackgroundColor(Color.rgb(0, 133, 201));
        txtanswer.setBackgroundColor(Color.rgb(102, 153, 204));
    }

    private void backgroundchangeDraw() {
        LinearLayout txtquestion = (LinearLayout) findViewById(R.id.questionBackground);
        LinearLayout txtanswer = (LinearLayout) findViewById(R.id.answerBackground);
        txtquestion.setBackgroundColor(Color.rgb(140, 140, 140));
        txtanswer.setBackgroundColor(Color.rgb(140, 140, 140));
    }

    private void setButton(boolean b){
        Button buttonHigh=findViewById(R.id.button1);
        Button buttonLow=findViewById(R.id.button2);

        buttonHigh.setEnabled(b);
        buttonLow.setEnabled(b);
    }
}
