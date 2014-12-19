package com.almaghrib.mobile.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;
import com.android.volley.toolbox.ImageLoader.ImageCache;

public class LruBitmapCache extends LruCache<String, Bitmap> implements ImageCache {
    private final static int BYTES_PER_PIXEL = 4; // 4 bytes per pixel

    public LruBitmapCache(int maxSize) {
        super(maxSize);
    }

    public LruBitmapCache(Context context) {
        this(getCacheSize(context));
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        if (value != null) {
            return value.getRowBytes() * value.getHeight();
        }
        return 0;
    }

    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }

    /**
     * Returns a cache size equal to approximately three screens worth of images.
     * @param context
     * @return cache size
     */
    public static int getCacheSize(Context context) {
        final DisplayMetrics displayMetrics = context.getResources().
                getDisplayMetrics();
        final int screenWidth = displayMetrics.widthPixels;
        final int screenHeight = displayMetrics.heightPixels;

        final int screenBytes = screenWidth * screenHeight * BYTES_PER_PIXEL;

        return screenBytes * 3;
    }
}