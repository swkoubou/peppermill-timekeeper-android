package swkoubou.peppermill;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;


/**
 * Created by Yuta.Watanabe on 2016/07/23.
 */
public class Convert3gpToWAV {
    private Context context = null;
    private FFmpeg ffmpeg = null;
    private String TAG = this.getClass().toString();

    public Convert3gpToWAV(Context context){
        this.context = context;
        ffmpeg = FFmpeg.getInstance(context);

    }
    public void convert(String input_file_path, String outputfile_path, final Runnable callback){

        Handler handler = new Handler();
        String str = "-i " + input_file_path + " " + outputfile_path;
        final String[] cmd = str.split(" ");

        handler.post(new Runnable() {
            @Override
            public void run() {
                load(new Runnable() {
                    @Override
                    public void run() {
                        execute(cmd, new Runnable() {
                            @Override
                            public void run() {
                                callback.run();
                            }
                        });

                    }
                });
            }
        });
    }

    private void load(final Runnable callback){
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {

                @Override
                public void onStart() {
                    Log.d(TAG, "load onstart");
                }

                @Override
                public void onFailure() {
                    Log.d(TAG, "load onFailure");
                }

                @Override
                public void onSuccess() {
                    Log.d(TAG, "load onSuccess");
                }

                @Override
                public void onFinish() {
                    Log.d(TAG, "load onFinish");
                    callback.run();
                }
            });
        } catch (FFmpegNotSupportedException e) {
            e.printStackTrace();
        }
    }

    private void execute(String[] cmd, final Runnable callback){
        try {
            ffmpeg.execute(cmd, new ExecuteBinaryResponseHandler() {

                @Override
                public void onStart() {
                    Log.d(TAG, "execute onStart");
                }

                @Override
                public void onProgress(String message) {
                    Log.d(TAG, message);
                }

                @Override
                public void onFailure(String message) {
                    Log.d(TAG, message);
                }

                @Override
                public void onSuccess(String message) {
                    Log.d(TAG, message);
                }

                @Override
                public void onFinish() {
                    Log.d(TAG, "execute onFinish");
                    callback.run();
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
        }
    }
}