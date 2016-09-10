package swkoubou.peppermill;

import android.content.Context;
import android.util.Log;

import com.aldebaran.qi.sdk.object.actuation.Animate;
import com.aldebaran.qi.sdk.object.actuation.Animation;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by uryoya on 16/09/05.
 */

public class HeatUpDetector {
    private static Recorder meetingRecorder = null;
    private static String sampleFilePath = "/data/data/swkoubou.peppermill/audiorecordtest.3gp";
    private static String wavFilePath = "/data/data/swkoubou.peppermill/audiorecordtest.wav";
    private static String serverUrl = "http://10.0.2.2:5000/analyze";
    private static final String LOG_TAG = "HeatUpDetector";
    static Context context = null;

    HeatUpDetector(Context c) {
        context = c;
    }

    public static void start() {
        meetingRecorder = new Recorder(sampleFilePath);
        meetingRecorder.start();
    }

    public static void stop() {
        meetingRecorder.stop();
        Convert3gpToWAV convert3gptowav = new Convert3gpToWAV(context);
        convert3gptowav.convert(sampleFilePath, wavFilePath, new Runnable() {
            @Override
            public void run() {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                //do post
                HeatUpPost heatUpPost = new HeatUpPost(serverUrl);
                String response_string = heatUpPost.uploadFile(context, wavFilePath);
                Log.d(LOG_TAG, response_string);
                try {
                    JSONObject heatupResult = new JSONObject(response_string);
                    if (heatupResult.getBoolean("is_burst")) {
                        Log.d(LOG_TAG, "burning");
                        Animation heatUpAnimation = Animations.HeatUp(context);
                        Animate animate = new Animate(context);
                        animate.run(heatUpAnimation);
                    } else {
                        Log.d(LOG_TAG, "nothing");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
