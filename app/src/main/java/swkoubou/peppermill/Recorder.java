/*
 * Required permissions:
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 * <uses-permission android:name="android.permission.RECORD_AUDIO" />
 */
package swkoubou.peppermill;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;

/**
 * Created by uryoya on 16/09/05.
 */

public class Recorder {
    private static final String LOG_TAG = "Recorder";

    private MediaRecorder mRecorder = null;
    private String mFileName = null;

    public Recorder(String savePath) {
        mFileName = savePath;
        Log.d(LOG_TAG, mFileName);
    }

    public void start() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        try {
            mRecorder.start();
        } catch (IllegalStateException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    public void stop() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }
}