package swkoubou.peppermill;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.aldebaran.qi.sdk.object.interaction.Say;

public class PepperTimer extends AppCompatActivity {
    public int i=0;
    private Button startButton, stopButton;
    private TextView timerText;
    public int count = 0;
    public long pause =0;
    public long millis;
    public Say say;
    public  HeatUpDetector heatupdetector = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final long Timer=from_min_to_msec(1);   //msec(カウントしたい分数)
        setTitle("PepperTimer");               //アプリのタイトル
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);
        startButton = (Button)findViewById(R.id.start_button);
        stopButton = (Button)findViewById(R.id.stop_button);
        timerText = (TextView)findViewById(R.id.timer);
        timerText.setText("05:00");
        // CountDownTimer(long millisInFuture, long countDownInterval)
        final CountDown[] countDown = {new CountDown(Timer)};

        say= new Say(this);

        startButton.setOnClickListener(new View.OnClickListener() {     //スタートボタンの処理
            @Override
            public void onClick(View v) {           //スタートボタンを押したとき
                count++;
                bunki(count);
            }
            private void bunki(int count){
                if(count%2==1) {// 開始
                   // heatupdetector.start();
                    bunki2(count);
                    say.run("スタート");
                    startButton.setText("pause");
                }else if(count%2==0){               //一時停止
                    heatupdetector.stop();
                    pause=millis;
                    say.run("停止");
                    countDown[0].cancel();
                    startButton.setText("start");
                }
            }
            private void bunki2(int count){
                if(count > 1) {
                    countDown[0] = new CountDown(pause);
                    countDown[0].start();
                }else{countDown[0].start();}
            }
        });
        //タイマーリセット
        stopButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                heatupdetector.stop();
                count=0;
                millis=0;
                say.run("リセットするお");
                // 中止
                countDown[0].cancel();
                countDown[0] = new CountDown(Timer);
                startButton.setText("start");
                timerText.setText("05:00");
                startButton.setVisibility(View.VISIBLE);

            }
        });

    }
    public  long  from_min_to_msec(long min){
        return min*60*1000;
    }

    class CountDown extends CountDownTimer {                    //タイマーのクラス
        public long countMillis = -1;
       // HeatUpDetector heatupdetector = null;
        //時計関係のやつ　
        public CountDown(long millisInFuture) {

            super(millisInFuture, 100);
            heatupdetector = new HeatUpDetector(PepperTimer.this);
            heatupdetector.start();
        }

        @Override
        public void onTick(long millisUntilFinished) {          //残り時間の表示
            // 残り時間を分、秒、ミリ秒に分割

            i++;
            long mm = millisUntilFinished / 1000 / 60;
            long ss = millisUntilFinished / 1000 % 60;
            //long ms = millisUntilFinished - ss * 1000 - mm * 1000 * 60;
            millis=millisUntilFinished;
            timerText.setText(String.format("%1$02d:%2$02d", mm, ss));
            if(i==4) {
                heatupdetector.stop();
                //  heatupdetector.start();
            }
            if(i==5){
                heatupdetector.start();
                i=0;
            }

            //timerText.setText(String.format("%1$02d:%2$02d.%3$03d", mm, ss, ms));
        }
        @Override
        public void onFinish() {                                //残り時間０の時
            heatupdetector.stop();
            startButton.setVisibility(View.INVISIBLE);
            timerText.setText(String.format("終わりだよ！"));
            say.run("おわりました");
        }
        public final Long getCount()
        {
            return countMillis;
        }
    }
}