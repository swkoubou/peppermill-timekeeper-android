package com.example.a1621098.layoutpractice;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aldebaran.qi.sdk.object.ObjectProxy;
import com.aldebaran.qi.sdk.object.interaction.Say;
import android.app.Activity;



public class Layout extends AppCompatActivity {


    private Button startButton, stopButton,waitButton;
    private TextView timerText;
    public int timeca= 300000;
    public int timcash = 0;
    public int count = 0;
    public long pause;
    //public long millis;
    //final long counts = timerText.getLineCount();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("PepperTimer");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);

        startButton = (Button)findViewById(R.id.start_button);
        stopButton = (Button)findViewById(R.id.stop_button);



        timerText = (TextView)findViewById(R.id.timer);
        timerText.setText("5:00.000");


        // インスタンス生成
        // CountDownTimer(long millisInFuture, long countDownInterval)
        // 3分= 3x60x1000 = 180000 msec
        final CountDown[] countDown = {new CountDown(300000, 100)};


        final Say say = new Say(this);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                count++;
                if(count%2==1) {
                    say.run("スタート");
                    // 開始
                   // if(count > 1){ countDown[0] = new CountDown(pause, 100);countDown[0].start();}else{countDown[0].start();}
                    countDown[0].start();
                    Button startButton = (Button) v;
                    startButton.setText("pose");

                }else if(count%2==0){
                    //一時停止
                    pause = countDown[0].getCount();
                    countDown[0] = new CountDown(pause, 100);
                    say.run("停止");
                    countDown[0].cancel();
                    Button startButton = (Button) v;
                    startButton.setText("start");

                }

            }
        });

        stopButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                count=0;
                say.run("リセットするお");
                // 中止
                countDown[0].cancel();
                timerText.setText("5:00.000");

            }
        });
    }

    class CountDown extends CountDownTimer {
        public long countMillis = -1;
        public CountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            pause = millisInFuture;
        }

        @Override
        public void onFinish() {
            // 完了
            timerText.setText("0:00.000");
            timerText.setText(String.format("終わりだよ！"));

        }

        // インターバルで呼ばれる
        @Override
        public void onTick(long millisUntilFinished) {
            // 残り時間を分、秒、ミリ秒に分割
            long mm = millisUntilFinished / 1000 / 60;
            long ss = millisUntilFinished / 1000 % 60;
            long ms = millisUntilFinished - ss * 1000 - mm * 1000 * 60;

            timerText.setText(String.format("%1$02d:%2$02d.%3$03d", mm, ss, ms));
        }
        public final Long getCount()
        {
            return countMillis;
        }
    }

}
