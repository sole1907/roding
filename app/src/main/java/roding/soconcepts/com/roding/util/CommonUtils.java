package roding.soconcepts.com.roding.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by mac on 2/27/16.
 */
public class CommonUtils {

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int)((dp * displayMetrics.density) + 0.5);
    }

    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((px/displayMetrics.density)+0.5);
    }
}
