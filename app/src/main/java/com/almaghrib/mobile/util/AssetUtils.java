package com.almaghrib.mobile.util;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class AssetUtils {
    private static final String TAG = AssetUtils.class.getSimpleName();

    private AssetUtils() {}

    /**
     * Copies out the text from an asset file in the path given by
     * </code>assetFileLocation</code> to a String.
     * @param context
     * @param assetFileLocation
     * @return the string in the asset file or null if it could not be retrieved.
     */
    public static String readFileFromAssets(Context context, String assetFileLocation) {
        InputStream is = null;
        BufferedReader in = null;
        try {
            is = context.getAssets().open(assetFileLocation);
            in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            final StringBuilder sb = new StringBuilder();
            String str;
            while ((str=in.readLine()) != null) {
                sb.append(str);
            }
            return sb.toString();

        } catch (IOException e) {
            Log.w(TAG, e.getMessage(), e);

        } finally {
            closeQuietly(in);
            closeQuietly(is);
        }
        return null;
    }

    /**
     * Close the Closeable object, i.e. a source or destination of date that
     * can be close such as an InputStream. If closed then the resources this
     * object is holding is released.
     * @param closeable
     */
    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
