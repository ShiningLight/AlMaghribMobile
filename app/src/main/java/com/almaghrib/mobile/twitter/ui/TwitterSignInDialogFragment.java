package com.almaghrib.mobile.twitter.ui;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.almaghrib.mobile.R;
import com.almaghrib.mobile.twitter.data.TwitterApiUriRequestBuilder;

import org.apache.http.protocol.HTTP;
import org.scribe.exceptions.OAuthConnectionException;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * DialogFragment which allows the user to sign in to Twitter.
 */
public class TwitterSignInDialogFragment extends DialogFragment {
    private static final String TAG = TwitterSignInDialogFragment.class.getSimpleName();

    interface AuthorizationCompleteCallback {

        /**
         * Notify listeners that authorization is complete so
         * access token request can be made.
         * @param requestToken
         * @param url
         */
        void onAuthorizationComplete(Token requestToken, String url);
    }

    private OAuthService mService;
    private AuthorizationCompleteCallback mListener;

    private GetRequestTokenTask mTokenTask;

    public static void showTwitterSignInDialog(FragmentManager fm,
                                               AuthorizationCompleteCallback listener,
                                               OAuthService service) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        final FragmentTransaction ft = fm.beginTransaction();
        final Fragment prev = fm.findFragmentByTag(TAG);
        if (prev != null) {
            ft.remove(prev);
        }
        // Create and show the dialog.
        final TwitterSignInDialogFragment dialogFragment =
                TwitterSignInDialogFragment.newInstance(service, listener);
        dialogFragment.show(ft, TAG);
    }

    private static TwitterSignInDialogFragment newInstance(OAuthService service,
                                                           AuthorizationCompleteCallback frag) {
        final TwitterSignInDialogFragment fragment = new TwitterSignInDialogFragment();
        fragment.setOAuthService(service);
        fragment.setAuthorizationCallbackListener(frag);
        return fragment;
    }

    public TwitterSignInDialogFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.sign_in_twitter_dialog, container, false);

        final WebView webView = (WebView) v.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);

        getDialog().setTitle(R.string.twitter_sign_in_dialog_title);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mTokenTask == null) {
            mTokenTask = new GetRequestTokenTask(mService, view);
            mTokenTask.execute();
        }
    }

    @Override
    public void onDestroy() {
        mTokenTask.cancel(true);
        mTokenTask = null;
        super.onDestroy();
    }

    private void setOAuthService(OAuthService service) {
        mService = service;
    }

    private void setAuthorizationCallbackListener(AuthorizationCompleteCallback listener) {
        mListener = listener;
    }

    private class GetRequestTokenTask extends AsyncTask<Void, Void, Token> {
        private OAuthService service;
        private View view;

        public GetRequestTokenTask(OAuthService service, View view) {
            this.service = service;
            this.view = view;
        }
        @Override
        protected Token doInBackground(Void... param) {
            try {
                final Token requestToken = service.getRequestToken();
                Log.d(TAG, "requestToken: " + requestToken.toString());
                return requestToken;
            } catch (OAuthConnectionException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return null;
        }
        @Override
        protected void onPostExecute(final Token requestToken) {
            if (requestToken != null && view != null) {
                final String authUrl = service.getAuthorizationUrl(requestToken);
                Log.d(TAG, "url: " + authUrl);
                final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
                final WebView loginWebView = (WebView) view.findViewById(R.id.webView);
                loginWebView.setVisibility(View.VISIBLE);
                //attach WebViewClient to intercept the callback url
                loginWebView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        progressBar.setVisibility(View.GONE);
                    }
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        //check for our custom callback protocol, otherwise use default behavior
                        Log.d(TAG, "url: " + url);
                        if (url.startsWith(TwitterApiUriRequestBuilder.OAUTH_CALLBACK_VALUE)) {
                            //authorization complete hide webview for now.
                            loginWebView.setVisibility(View.GONE);

                            mListener.onAuthorizationComplete(requestToken, url);
                            dismiss();
                            return true;
                        }
                        // else if url has redirect_after_login then load that page
                        final String redirectUrl = Uri.parse(url).getQueryParameter("redirect_after_login");
                        if (!redirectUrl.isEmpty()) {
                            try {
                                url = URLDecoder.decode(redirectUrl, HTTP.UTF_8);
                                Log.d(TAG, "redirect url: " + url);
                                loginWebView.loadUrl(url);
                                return true;
                            } catch (UnsupportedEncodingException e) {
                                Log.d(TAG, e.getMessage(), e);
                            }
                        }
                        return super.shouldOverrideUrlLoading(view, url);
                    }
                });
                //send user to authorization page
                loginWebView.loadUrl(authUrl);
            } else {
                dismiss();
            }
        }

    }

}
