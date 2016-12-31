package roding.soconcepts.com.roding.util;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by mac on 7/13/16.
 */
public class RodingPreferences {

    public static void write(Context context, String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key, value).commit();
    }

    public static String read(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, null);
    }
}
