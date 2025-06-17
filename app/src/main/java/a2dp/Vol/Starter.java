package a2dp.Vol;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

/**
 * @author Jim Roal This broadcast receiver just listens to the BOOT_COMPLETE
 * intent and starts the service if the preference is set to do so
 */
public class Starter extends BroadcastReceiver {
    SharedPreferences preferences;

    public static final String PREFS_NAME = "a2dp.Vol_preferences";
    //private MyApplication application;

    @Override
    public void onReceive(Context context, Intent arg1) {

        preferences = context.getSharedPreferences(PREFS_NAME, 0);

        if (preferences.getBoolean("bootstart", false)) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                context.startService(new Intent(context, service.class));
            } else {
                context.startForegroundService(new Intent(context, service.class));
            }
        }
    }
}
