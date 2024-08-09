package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
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
        int score;

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
        txtScore.setText("20");
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


