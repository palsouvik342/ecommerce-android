package os.com.krishirasayan.classes;

import static android.content.Context.MODE_PRIVATE;

import static os.com.krishirasayan.consts.Config.SF_APPDATA_FILENAME;

import android.content.Context;
import android.content.SharedPreferences;

public class AppData {

    public static final String KEY_FCM_TOKEN = "fcm_token";

    public static boolean hasFcmToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SF_APPDATA_FILENAME, Context.MODE_PRIVATE);
        if(sharedPreferences.contains(KEY_FCM_TOKEN)) {
            return true;
        }
        else {
            return false;
        }
    }

    public static String getFcmToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SF_APPDATA_FILENAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_FCM_TOKEN, "");
    }

    public static void setFcmToken(Context context, String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SF_APPDATA_FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_FCM_TOKEN, token);
        editor.apply();
    }

    public static void clear(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SF_APPDATA_FILENAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}