package swkoubou.peppermill;

/**
 * Created by uryoya on 16/09/05.
 */

public class HeatUpDetector {
    private Recorder meetingRecorder = null;
    private String sampleFilePath = "/data/data/swkoubou.peppermill/audiorecordtest.3gp";

    public void start() {
        meetingRecorder = new Recorder(sampleFilePath);
        meetingRecorder.start();
    }

    public void stop() {
        meetingRecorder.stop();
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
