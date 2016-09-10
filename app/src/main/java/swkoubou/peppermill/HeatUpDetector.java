package swkoubou.peppermill;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by uryoya on 16/09/05.
 */

public class HeatUpDetector {
    private Recorder meetingRecorder = null;
    private String sampleFilePath = "/data/data/swkoubou.peppermill/audiorecordtest.3gp";
    private String wavFilePath = "/data/data/swkoubou.peppermill/audiorecordtest.wav";
    private String serverUrl = "http://10.0.2.2:5000/analyze";
    private static final String LOG_TAG = "HeatUpDetector";
    Context context = null;

    HeatUpDetector(Context c) {
        context = c;
    }

    public void start() {
        meetingRecorder = new Recorder(sampleFilePath);
        meetingRecorder.start();
    }

    public void stop() {
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
                try {
                    JSONObject heatupResult = new JSONObject(response_string);
                    Log.d(LOG_TAG, heatupResult.getString("is_burn"));
                    if (heatupResult.getBoolean("is_burn")) {
                        Log.d(LOG_TAG, "ture");
                    } else {
                        Log.d(LOG_TAG, "false");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        // Recorder.onStop()
        // audio_file_3gp = Recorder.getFile()
        // audio_file_wav = ConvertWAV.convert(audio_file_3gp)
        // HeatUpPost.uploadFile(audio_file_wav)
        // if (HeatUpPost.isHeatUp()) {
        //     Animations.HeatUp()
        // }
        // pass
    }
}
