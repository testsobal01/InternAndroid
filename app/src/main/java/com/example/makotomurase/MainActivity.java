package com.example.makotomurase;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.annotation.SuppressLint;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
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
    int num_BGM;

    //AnimatorSetオブジェクトを宣言
    /**
     * アニメーションに利用するセットを宣言
     * blink：テキストの点滅、scale：テキストのサイズ変化
     */
    AnimatorSet blink;
    AnimatorSet scale;

    // 番号8 効果音
    //最大値設定用の変数
    TextView set;
    int max_num;

    // 効果音
    private static SoundPool soundPool;
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

        Button btn4 = (Button) findViewById(R.id.button_colorchange);
        btn4.setOnClickListener(this);

        Button btn5 = (Button) findViewById(R.id.button_option);
        btn5.setOnClickListener(this);

        Button btn6 = (Button) findViewById(R.id.button_howto);
        btn6.setOnClickListener(this);

        // 番号8 効果音
        SoundPlayer(this);
        pref = getSharedPreferences("MakotoMurase",MODE_PRIVATE);
        prefEditor = pref.edit();

        // 起動時に関数を呼び出す
        setQuestionValue();

        //番号11　BGMの追加
        //11-2 BGMの種類の追加
        Button buttonStart = findViewById(R.id.start);
        buttonStart.setOnClickListener( v ->  {
            // 音楽再生
            Random random = new Random();
            num_BGM = random.nextInt(5);
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
            Intent intent = new Intent(this, MiniGameActivity.class);
            startActivity(intent);});
        //  もし何かフッター触ったときにに入れたいのなら{}の中身をいじろう(番号9)


        //テキストビューを取得
        TextView player = (TextView) findViewById(R.id.answer);

        //プレイヤーのテキストビューにアニメーションを設定
        blink = (AnimatorSet) AnimatorInflater.loadAnimator(MainActivity.this, R.animator.blink_animation);
        blink.setTarget(player);
        scale = (AnimatorSet) AnimatorInflater.loadAnimator(MainActivity.this, R.animator.scale_animation);
        scale.setTarget(player);

        set = (TextView) findViewById(R.id.set_text);
        max_num = 10;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
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
            timer.cancel();
        } else if (id == R.id.button_option) {
            //最大値が設定できるダイアログを表示
            NumberPickerDialogFragment dialog = new NumberPickerDialogFragment();
            dialog.show(getSupportFragmentManager(), "sample");         
        }else if (id == R.id.button_colorchange){
            setRandomColor();
        }else if (id == R.id.button_howto){
            Intent intent_h = new Intent(this, HowToActivity.class);
            startActivity(intent_h);
        }
    }

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText("値2");
    }

    private void setQuestionValue() {
        Random r = new Random();
        // 0から10の範囲で乱数を生成（+1する必要がある）
        int questionValue = r.nextInt(max_num + 1);

        TextView txtView = findViewById(R.id.question);
        txtView.setText(Integer.toString(questionValue));
    }

    private void setAnswerValue() {
        Random r = new Random();
        int answerValue = r.nextInt(max_num + 1);

        TextView txtView = findViewById(R.id.answer);
        txtView.setText(Integer.toString(answerValue));
    }


    // 番号8 効果音
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

    @SuppressLint("NewApi")
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
            //タイマースタート
            timer.start();
            // result には結果のみを入れる
            if (question < answer) {
                result = "WIN";
                score = 2;
                correctSound();
                vibrator();

                //テキスト拡大
                scale.start();
            }  else if (question > answer) {
                result = "LOSE";
                score = -1;
                blipSound();

                //テキスト点滅
                blink.start();
            } else {
                result = "DRAW";
                score = 1;
            }
        } else {
            //タイマースタート
            timer.start();
            if (question > answer) {
                result = "WIN";
                score = 2;
                correctSound();
                vibrator();

                //テキスト拡大
                scale.start();
            } else if (question < answer) {
                result = "LOSE";
                score = -1;
                blipSound();

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
        //setNextQuestion();


        // スコアを表示
        setScore(score);
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
        //11-3 pause中のBGMのストップ
        audioStop();
    }
    @Override
    protected void onResume(){
        super.onResume();

        TextView textView = (TextView) findViewById(R.id.text_score);

        String readText = pref.getString("score","0");
        textView.setText(readText);
    }

    //ダイアログを出すクラス
    public static class NumberPickerDialogFragment extends androidx.fragment.app.DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.numberpicker, null, false);

            final MainActivity activity = (MainActivity) getActivity();
            final NumberPicker np1 = (NumberPicker) view.findViewById(R.id.numberPicker);
            np1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            np1.setMinValue(10);
            np1.setMaxValue(50);
            np1.setValue(activity.max_num);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("最大値を設定してください");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    int max = Integer.parseInt(String.valueOf(np1.getValue()));

                    MainActivity mainActivity = (MainActivity) getActivity();
                    assert mainActivity != null;
                    mainActivity.max_num = max;
                    mainActivity.set.setText(max + "が設定されています");
                }
            });
            builder.setNegativeButton("キャンセル", null);
            builder.setView(view);
            return builder.create();
        }
    }
    //番号13：値１・値2の色変更（ランダム）
    public void setRandomColor() {
        Random random = new Random();
        View text1 = findViewById(R.id.question);
        View text2 = findViewById(R.id.answer);

        for (int j = 0; j < 2; j++) {
            String colorCord = "#";
            int r = random.nextInt(156) + 100;
            int g = random.nextInt(156) + 100;
            int b = random.nextInt(156) + 100;

            int r1 = (r % 16);
            int r2 = (r / 16);
            int g1 = (g % 16);
            int g2 = (g / 16);
            int b1 = (b % 16);
            int b2 = b / 16;

            int[] colorDegits = {r1, r2, g1, g2, b1, b2};

            for (int i = 0; i < 6; i++) {
                if (colorDegits[i] == 10) {
                    colorCord = colorCord.concat("A");
                } else if (colorDegits[i] == 11) {
                    colorCord = colorCord.concat("B");
                } else if (colorDegits[i] == 12) {
                    colorCord = colorCord.concat("C");
                } else if (colorDegits[i] == 13) {
                    colorCord = colorCord.concat("D");
                } else if (colorDegits[i] == 14) {
                    colorCord = colorCord.concat("E");
                } else if (colorDegits[i] == 15) {
                    colorCord = colorCord.concat("F");
                } else {
                    colorCord = colorCord.concat(String.valueOf(colorDegits[i]));
                }
            }

            if (j == 0) {
                text1.setBackgroundColor(Color.parseColor(colorCord));
            } else {
                text2.setBackgroundColor(Color.parseColor(colorCord));
            }
        }
        Toast.makeText(this,"背景色が変わりました",Toast.LENGTH_SHORT).show();
    }


    //番号11　BGMの追加
    //11-2 BGMの種類の追加
    private boolean audioSetup(){
        mediaPlayer = new MediaPlayer();
        String filePath;
        if(num_BGM==0){
            filePath = "music.mp3";
        } else if (num_BGM==1) {
            filePath = "music2.mp3";
        } else if(num_BGM==2){
            filePath = "music3.mp3";
        }else if(num_BGM==3){
            filePath = "music4.mp3";
        }else {
            filePath = "music5.mp3";
        }

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

    // 番号19　タイマーリセット
    CountDownTimer timer = new CountDownTimer(3000, 1000) {
        public void onTick(long millisUntilFinished) {
            return;
        }

        public void onFinish() {
            setQuestionValue();
        }
    };

    // 番号4 バイブレーション機能
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void vibrator(){
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
    }
}


