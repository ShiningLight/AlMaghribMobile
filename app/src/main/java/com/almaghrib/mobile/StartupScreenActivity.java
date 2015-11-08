package com.almaghrib.mobile;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Spinner;

import com.almaghrib.mobile.almaghribApi.data.AlMaghribApiUriRequestBuilder;
import com.almaghrib.mobile.almaghribApi.jsonModels.AlMaghribLoginModelContainer;
import com.almaghrib.mobile.util.GsonRequest;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

public class StartupScreenActivity extends FragmentActivity {

    private static final String TAG = "StartupScreenActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup_screen);
    }

    public void goButtonAction(View view) throws Exception {
        final EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        final EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);

        final String username = usernameEditText.getText().toString();
        final String password = passwordEditText.getText().toString();

        if (username.isEmpty() && password.isEmpty()) {
            // User wants to proceed without logging in
            startMainScreen();
        } else {
            if (username.isEmpty()) {
                showEditTextError(usernameEditText, "Username is empty");
            } else if (password.isEmpty()) {
                showEditTextError(passwordEditText, "Password is empty");
            } else {
                loginToAlMaghribAccount(username, password);
            }
        }
    }

    private void showEditTextError(EditText editText, String errorMsg) {
        final Animation shake = AnimationUtils.loadAnimation(editText.getContext(), R.anim.shake);
        editText.startAnimation(shake);
        editText.setError(errorMsg);
    }

    private void loginToAlMaghribAccount(String username, String password) throws Exception {
        // show dialog and make request
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle(getString(R.string.logging_in_dialog_title));
        pd.show();

        final AlMaghribApiUriRequestBuilder uriBuilder = new AlMaghribApiUriRequestBuilder();
        final String url = uriBuilder.buildLoginRequest();

        final GsonRequest<AlMaghribLoginModelContainer> request =
                new GsonRequest<AlMaghribLoginModelContainer>(
                        Request.Method.POST, url,
                        AlMaghribLoginModelContainer.class,
                        uriBuilder.getLoginParamsMap(username, password),
                        uriBuilder.getLoginHeaders(),
                        uriBuilder.getLoginHTTPEntity(username, password),
                        createLoginRequestSuccessListener(pd),
                        createLoginRequestErrorListener(pd)) {
                };

        request.setTag(TAG);
        RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private Response.Listener<AlMaghribLoginModelContainer> createLoginRequestSuccessListener(
            final ProgressDialog pd) {
        return new Response.Listener<AlMaghribLoginModelContainer>() {
            @Override
            public void onResponse(AlMaghribLoginModelContainer response) {
                try {
                    Log.d(TAG, response.toString());
                    saveAlMaghribTokenAndUserInfo(response);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
                pd.cancel();
                startMainScreen();
            };
        };
    }

    private void saveAlMaghribTokenAndUserInfo(AlMaghribLoginModelContainer response) {
        final AlMaghribSharedPreferences prefs =
                AlMaghribSharedPreferences.getInstance(getApplicationContext());
        if (!TextUtils.isEmpty(response.getToken())) {
            prefs.setAlMaghribToken(response.getToken());
        }
        if (response.getUser() != null) {
            prefs.saveAlMaghribUserInfo(response.getUser());
        }
    }

    private Response.ErrorListener createLoginRequestErrorListener(final ProgressDialog pd) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage(), error);
                pd.cancel();
                Snackbar.make(StartupScreenActivity.this.findViewById(R.id.containerLayout),
                        R.string.log_in_failed_snackbar_text, Snackbar.LENGTH_SHORT)
                        .show();
            }
        };
    }

    private void startMainScreen() {
        // save user city
        final Spinner citySpinner = (Spinner) findViewById(R.id.citySpinner);
        AlMaghribSharedPreferences.getInstance(getApplicationContext())
                .setUserCity(citySpinner.getSelectedItem().toString());
        //go to home screen

    }

    @Override
    public void onDestroy() {
        RequestQueueSingleton.getInstance(getApplicationContext())
                .cancelPendingRequests(TAG);
        super.onDestroy();
    }
}
