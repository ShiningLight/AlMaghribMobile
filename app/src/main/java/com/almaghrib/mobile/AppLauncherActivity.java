package com.almaghrib.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

/**
 * Activity which decides which should be the first Activity to start, i.e. should we
 * fire up startup activity with the login or the home page activity first.
 */
public class AppLauncherActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final AlMaghribSharedPreferences prefs =
                AlMaghribSharedPreferences.getInstance(getApplicationContext());

        final Intent firstIntent;
        if (!prefs.getHasStartupScreenShown()) {
            // user has not seen start up screen
            firstIntent = new Intent(this, StartupScreenActivity.class);
        } else {
            firstIntent = new Intent(this, HomePageActivity.class);
        }
        startActivity(firstIntent);

        finish();
    }
}
