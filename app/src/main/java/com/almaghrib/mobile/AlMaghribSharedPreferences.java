package com.almaghrib.mobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Map;
import java.util.Set;

public class AlMaghribSharedPreferences {
    private static AlMaghribSharedPreferences instance;

    private final SharedPreferences mSharedPreferences;

    /**
     * Singleton access.
     */
    public static synchronized AlMaghribSharedPreferences getInstance(final Context context) {
        if (instance == null) {
            instance = new AlMaghribSharedPreferences(context);
        }
        return instance;
    }

    private AlMaghribSharedPreferences(final Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getTwitterAccessToken() {
        return mSharedPreferences.getString(
                AlMaghribSharedPreferenceKeys.TWITTER_OAUTH_ACCESS_TOKEN, "");
    }

    public void setTwitterAccessToken(String accessToken) {
        mSharedPreferences.edit().putString(
                AlMaghribSharedPreferenceKeys.TWITTER_OAUTH_ACCESS_TOKEN, accessToken).commit();
    }

    public String getTwitterAccessTokenSecret() {
        return mSharedPreferences.getString(
                AlMaghribSharedPreferenceKeys.TWITTER_OAUTH_ACCESS_TOKEN_SECRET, "");
    }

    public void setTwitterAccessTokenSecret(String accessTokenSecret) {
        mSharedPreferences.edit().putString(
                AlMaghribSharedPreferenceKeys.TWITTER_OAUTH_ACCESS_TOKEN_SECRET, accessTokenSecret).commit();
    }
}
