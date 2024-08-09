package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

    private int doubtStartFlag = 0;
    private int doubtResultFlag = 0;
    private int winCount = 0;

    private int nowScore = 0;

    private int highButtonFlag = 0;

    private int lowButtonFlag = 0;

    private int leftNumber = 0;





    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Bundle extra = intent.getExtras();

        Button btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(this);

        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(this);

        Button btn4 = (Button) findViewById(R.id.button4);
        btn4.setOnClickListener(this);

        b_2();

        setQuestionValue();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Vibrator vib=(Vibrator)getSystemService(VIBRATOR_SERVICE);

        setNowScore();

        if (id == R.id.button1) {

            setAnswerValue();

            this.highButtonFlag++;

            checkResult(true);

        } else if (id == R.id.button2) {

            setAnswerValue();

            this.lowButtonFlag++;

            checkResult(false);

        } else if (id == R.id.button3) {
            setQuestionValue();
            clearAnswerValue();
            clearScoreValue();
            vib.vibrate(100);

        }else {
            doubtJudge();
        }
    }

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText("値2");
    }

    private void setQuestionValue() {
        setNowScore();
        Random r = new Random();

        if (this.winCount == 2 && this.nowScore >= 20 && this.doubtStartFlag == 1) {
            int questionValue1 = r.nextInt(7) + 2;
            TextView txtView = findViewById(R.id.question);
            txtView.setText(Integer.toString(questionValue1));

        }else{
            int questionValue2 = r.nextInt(10 + 1);
            TextView txtView = findViewById(R.id.question);
            txtView.setText(Integer.toString(questionValue2));
        }


    }

    private void setAnswerValue() {
        setStartDoubtFlag();
        setNowScore();
        Random r = new Random();

        if (this.winCount == 2 && this.nowScore >= 20 && this.doubtStartFlag == 1){
            Toast.makeText(this, "ダウト発動", Toast.LENGTH_SHORT).show();
            getLeftNumber();
            if(this.highButtonFlag == 1){
                int answerValue = r.nextInt(this.leftNumber - 1);
                TextView txtView = findViewById(R.id.answer);
                txtView.setText(Integer.toString(answerValue));

            }else if(this.lowButtonFlag == 1){
                int answerValue = this.leftNumber + 1;
                TextView txtView = findViewById(R.id.answer);
                txtView.setText(Integer.toString(answerValue));

            }else{
                int answerValue = r.nextInt(this.leftNumber);
                TextView txtView = findViewById(R.id.answer);
                txtView.setText(Integer.toString(answerValue));
            }

            this.winCount = 0;
            this.doubtStartFlag = 0;
            this.highButtonFlag = 0;
            this.lowButtonFlag = 0;

        }else{
            int answerValue = r.nextInt(10 + 1);

            TextView txtView = findViewById(R.id.answer);
            txtView.setText(Integer.toString(answerValue));
        }
    }

    private void checkResult(boolean isHigh) {
        Vibrator vib=(Vibrator)getSystemService(VIBRATOR_SERVICE);

        TextView txtViewQuestion = findViewById(R.id.question);
        TextView txtViewAnswer = findViewById(R.id.answer);

        int question = Integer.parseInt(txtViewQuestion.getText().toString());
        int answer = Integer.parseInt(txtViewAnswer.getText().toString());

        TextView txtResult = (TextView) findViewById(R.id.text_result);

        // 結果を示す文字列を入れる変数を用意
        String result;
        int score ;







        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = "WIN";
                score = 2;
                vib.vibrate(100);
                this.winCount++;
            } else if (question > answer) {
                result = "LOSE";
                score = -1;
                vib.vibrate(1000);
            } else {
                result = "DRAW";
                score = 1;
                vib.vibrate(100);
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                vib.vibrate(100);
                this.winCount++;
            } else if (question < answer) {
                result = "LOSE";
                score = -1;

                vib.vibrate(1000);}

            else {
                result = "DRAW";
                score = 1;
                vib.vibrate(100);
            }
        }

        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText("結果：" + question + ":" + answer + "(" + result + ")");
        b_7(result);


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

        if(newScore <0){
            Intent intent = new Intent(this, LoseActivity.class);
            startActivity(intent);
        }

        if(newScore >=30){
            Intent intent = new Intent(this, WinActivity.class);
            startActivity(intent);
        }
    }

    private void clearScoreValue() {
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        txtScore.setText("20");
    }

    private void b_7(String result) {//勝敗によって数字が動く機能
        TextView txtViewQuestion = findViewById(R.id.question);
        TextView txtViewAnswer = findViewById(R.id.answer);

        TranslateAnimation TranslateQs = new TranslateAnimation(
                Animation.ABSOLUTE, 0f,
                Animation.ABSOLUTE, 125f,
                Animation.ABSOLUTE, 0f,
                Animation.ABSOLUTE, 0f);
        TranslateQs.setDuration(500);
        TranslateQs.setRepeatCount(0);
        TranslateQs.setFillAfter(false);
        TranslateAnimation TranslateQf = new TranslateAnimation(
                Animation.ABSOLUTE, 125f,
                Animation.ABSOLUTE, 0f,
                Animation.ABSOLUTE, 0f,
                Animation.ABSOLUTE, 0f);
        TranslateQf.setDuration(1000);
        TranslateQf.setRepeatCount(0);
        TranslateQf.setFillAfter(false);
        TranslateAnimation TranslateQl = new TranslateAnimation(
                Animation.ABSOLUTE, 125f,
                Animation.ABSOLUTE, -1500f,
                Animation.ABSOLUTE, 0f,
                Animation.ABSOLUTE, 4000f);
        TranslateQl.setDuration(1000);
        TranslateQl.setRepeatCount(0);
        TranslateQl.setFillAfter(false);
        TranslateAnimation TranslateAs = new TranslateAnimation(
                Animation.ABSOLUTE, 0f,
                Animation.ABSOLUTE, -125f,
                Animation.ABSOLUTE, 0f,
                Animation.ABSOLUTE, 0f);
        TranslateAs.setDuration(500);
        TranslateAs.setRepeatCount(0);
        TranslateAs.setFillAfter(false);
        TranslateAnimation TranslateAf = new TranslateAnimation(
                Animation.ABSOLUTE, -125f,
                Animation.ABSOLUTE, 0f,
                Animation.ABSOLUTE, 0f,
                Animation.ABSOLUTE, 0f);
        TranslateAf.setDuration(1000);
        TranslateAf.setRepeatCount(0);
        TranslateAf.setFillAfter(false);
        TranslateAnimation TranslateAl = new TranslateAnimation(
                Animation.ABSOLUTE, -125f,
                Animation.ABSOLUTE, 1500f,
                Animation.ABSOLUTE, 0f,
                Animation.ABSOLUTE, 4000f);
        TranslateAl.setDuration(1000);
        TranslateAl.setRepeatCount(0);
        TranslateAl.setFillAfter(false);

        RotateAnimation RotateWq = new RotateAnimation(
                0.0f, -50f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        RotateWq.setDuration(1000);
        RotateWq.setRepeatCount(0);
        RotateWq.setFillAfter(false);
        RotateAnimation RotateWa = new RotateAnimation(
                0.0f, 50f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        RotateWa.setDuration(1000);
        RotateWa.setRepeatCount(0);
        RotateWa.setFillAfter(false);

        ScaleAnimation scaleS = new ScaleAnimation(
                1.0f, 0.7f, 1.0f, 0.7f,
                Animation.RELATIVE_TO_SELF, 0.7f,
                Animation.RELATIVE_TO_SELF, 0.7f);
        scaleS.setDuration(500);
        scaleS.setRepeatCount(0);
        scaleS.setFillAfter(false);
        ScaleAnimation scaleW = new ScaleAnimation(
                0.7f, 0.4f, 0.7f, 0.4f,
                Animation.RELATIVE_TO_SELF, 0.7f,
                Animation.RELATIVE_TO_SELF, 0.7f);
        scaleW.setDuration(1000);
        scaleW.setRepeatCount(0);
        scaleW.setFillAfter(false);
        ScaleAnimation scaleL = new ScaleAnimation(
                0.7f, 0.2f, 0.7f, 0.2f,
                Animation.RELATIVE_TO_SELF, 0.7f,
                Animation.RELATIVE_TO_SELF, 0.7f);
        scaleL.setDuration(1000);
        scaleL.setRepeatCount(0);
        scaleL.setFillAfter(false);
        ScaleAnimation scaleD = new ScaleAnimation(
                0.7f, 0.8f, 0.7f, 0.8f,
                Animation.RELATIVE_TO_SELF, 0.7f,
                Animation.RELATIVE_TO_SELF, 0.7f);
        scaleD.setDuration(1000);
        scaleD.setRepeatCount(0);
        scaleD.setFillAfter(false);

        AnimationSet startQ = new AnimationSet(true);
        startQ.addAnimation(TranslateQs);
        startQ.addAnimation(scaleS);
        AnimationSet startA = new AnimationSet(true);
        startA.addAnimation(TranslateAs);
        startA.addAnimation(scaleS);
        AnimationSet QW = new AnimationSet(true);
        QW.addAnimation(TranslateQf);
        QW.addAnimation(RotateWq);
        QW.addAnimation(scaleD);
        AnimationSet AW = new AnimationSet(true);
        AW.addAnimation(TranslateAf);
        AW.addAnimation(RotateWa);
        AW.addAnimation(scaleD);
        AnimationSet QL = new AnimationSet(true);
        QL.addAnimation(TranslateQl);
        QL.addAnimation(scaleL);
        AnimationSet AL = new AnimationSet(true);
        AL.addAnimation(TranslateAl);
        AL.addAnimation(scaleL);
        AnimationSet QD = new AnimationSet(true);
        QD.addAnimation(TranslateQf);
        QD.addAnimation(scaleD);
        AnimationSet AD = new AnimationSet(true);
        AD.addAnimation(TranslateAf);
        AD.addAnimation(scaleD);

        txtViewQuestion.startAnimation(startQ);
        txtViewAnswer.startAnimation(startA);
        new CountDownTimer(500, 1000) {
            @Override
            public void onTick(long l) {}
            @Override
            public void onFinish() {
                if (result == "WIN") {
                    txtViewQuestion.startAnimation(QW);
                    txtViewAnswer.startAnimation(AW);
                } else if (result == "LOSE") {
                    txtViewQuestion.startAnimation(QL);
                    txtViewAnswer.startAnimation(AL);
                } else if (result == "DRAW") {
                    txtViewQuestion.startAnimation(QD);
                    txtViewAnswer.startAnimation(AD);
                }
            }
        }.start();
    }

    public void b_2(){
        pref = getSharedPreferences("AndroidSeminor", MODE_PRIVATE);
        prefEditor = pref.edit();
    }

    //b_2
    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();

        //画面上のテキストを保存
        TextView keepScoreText = (TextView)findViewById(R.id.text_score);
        prefEditor.putString("score_keep", keepScoreText.getText().toString());
        prefEditor.commit();
    }

    @Override
    protected void onResume(){
        super.onResume();
        Toast.makeText(this, "oR", Toast.LENGTH_SHORT).show();

        TextView keepScoreText = (TextView)findViewById(R.id.text_score);
        //ここができない
        String keepScoreTextString = pref.getString("score_keep", "保存なし");
        keepScoreText.setText(keepScoreTextString);
    }

    //左の数字を取得
    public void getLeftNumber() {
        TextView txtViewQuestion = findViewById(R.id.question);
        this.leftNumber = Integer.parseInt(txtViewQuestion.getText().toString());
    }


    public void setNowScore(){
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        this.nowScore= Integer.parseInt(txtScore.getText().toString());
    }

    public void setStartDoubtFlag(){
        Random r3 = new Random();
        int doubtRandom = r3.nextInt(100);

        if(doubtRandom > 70){
            this.doubtStartFlag = 1;
        }
    }
    public void doubtJudge(){
        if(this.doubtResultFlag == 1){
            //ダウト成功で7点+
            setScore(7);
            //ダウト成功ポップ

        }else{
            //ダウト失敗ポップ

        }
        //ダウト結果のフラグ初期化
        this.doubtResultFlag = 0;
    }
}


