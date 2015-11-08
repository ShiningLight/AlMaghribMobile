package com.almaghrib.mobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.almaghrib.mobile.almaghribApi.jsonModels.AlMaghribLoginItemUserModel;

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

    /** AlMaghrib Account info **/

    public void setAlMaghribToken(String token) {
        putString(AlMaghribSharedPreferenceKeys.ALMAGHRIB_TOKEN, token);
    }

    public String getAlMaghribToken() {
        return getString(AlMaghribSharedPreferenceKeys.ALMAGHRIB_TOKEN, "");
    }

    public void saveAlMaghribUserInfo(AlMaghribLoginItemUserModel user) {
        putString(AlMaghribSharedPreferenceKeys.ALMAGHRIB_ACCOUNT_EMAIL, user.getEmail());
        putString(AlMaghribSharedPreferenceKeys.ALMAGHRIB_ACCOUNT_FULL_NAME, user.getFullName());
        putString(AlMaghribSharedPreferenceKeys.ALMAGHRIB_ACCOUNT_PREF_NAME, user.getPrefName());
        putString(AlMaghribSharedPreferenceKeys.ALMAGHRIB_ACCOUNT_COUNTRY, user.getCountry());
        putString(AlMaghribSharedPreferenceKeys.ALMAGHRIB_ACCOUNT_GENDER, user.getGender());
    }

    public void clearAlMaghribUserInfo() {
        removeString(AlMaghribSharedPreferenceKeys.ALMAGHRIB_ACCOUNT_EMAIL);
        removeString(AlMaghribSharedPreferenceKeys.ALMAGHRIB_ACCOUNT_FULL_NAME);
        removeString(AlMaghribSharedPreferenceKeys.ALMAGHRIB_ACCOUNT_PREF_NAME);
        removeString(AlMaghribSharedPreferenceKeys.ALMAGHRIB_ACCOUNT_COUNTRY);
        removeString(AlMaghribSharedPreferenceKeys.ALMAGHRIB_ACCOUNT_GENDER);
    }

    public void setUserCity(String city) {
        putString(AlMaghribSharedPreferenceKeys.USER_CITY, city);
    }

    public String getUserCity(String city) {
        return getString(AlMaghribSharedPreferenceKeys.USER_CITY, "");
    }

    /** Twitter token **/

    public String getTwitterAccessToken() {
        return getString(AlMaghribSharedPreferenceKeys.TWITTER_OAUTH_ACCESS_TOKEN, "");
    }

    public void setTwitterAccessToken(String accessToken) {
        putString(AlMaghribSharedPreferenceKeys.TWITTER_OAUTH_ACCESS_TOKEN, accessToken);
    }

    public String getTwitterAccessTokenSecret() {
        return getString(AlMaghribSharedPreferenceKeys.TWITTER_OAUTH_ACCESS_TOKEN_SECRET, "");
    }

    public void setTwitterAccessTokenSecret(String accessTokenSecret) {
        putString(AlMaghribSharedPreferenceKeys.TWITTER_OAUTH_ACCESS_TOKEN_SECRET, accessTokenSecret);
    }

    /** Util methods **/

    private void putString(String key, String value) {
        mSharedPreferences.edit().putString(key, value).commit();
    }

    private String getString(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    private void removeString(String key) {
        mSharedPreferences.edit().remove(key).commit();
    }

}
