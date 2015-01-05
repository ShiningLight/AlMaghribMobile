package com.almaghrib.mobile.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtils {
    private static final String TAG = DateUtils.class.getSimpleName();

    private DateUtils() {
    }

    public static String getFormattedDate(SimpleDateFormat inputDateFormat,
                                          SimpleDateFormat outputDateFormat,
                                          String dateString) {
        Date d = null;
        try {
            d = inputDateFormat.parse(dateString);
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        final String formattedTime = (d != null) ? outputDateFormat.format(d) : "";

        return formattedTime;
    }

}
