package swkoubou.peppermill;

import android.content.Context;
import com.aldebaran.qi.sdk.object.actuation.Animation;

public class Animations {
    static public Animation HeatUp(Context context) {
        return Animation.fromResources(context, R.raw.heatup);
    }
}