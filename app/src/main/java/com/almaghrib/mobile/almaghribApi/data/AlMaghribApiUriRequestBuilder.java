package com.almaghrib.mobile.almaghribApi.data;

import android.util.Log;

import com.almaghrib.mobile.util.UriRequestBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlMaghribApiUriRequestBuilder extends UriRequestBuilder {
    private static final String TAG = "AlMaghribApiUriRequestBuilder";

    public static final String BASE_REQUEST_URL = "http://api.almaghrib.us/v1";

    /* Endpoints */
    public static final String SIGN_IN_REQUEST = "/signin";

    /* Parameters */
    private static final String EMAIL_PARAM = "email";
    private static final String PASSWORD_PARAM = "password";
    private static final String CONTENT_TYPE_PARAM = "Content-Type";
    private static final String ORIGIN_PARAM = "Origin";

    /* Values */
    private static final String CONTENT_TYPE_VALUE = "application/json";
    private static final String ORIGIN_VALUE = "http://almaghrib.org";

    public AlMaghribApiUriRequestBuilder() {
    }

    public String buildLoginRequest() {
        return buildAlMaghribRequest(SIGN_IN_REQUEST, null);
    }

    public Map<String, String> getLoginParamsMap(String username, String password) {
        final HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(EMAIL_PARAM, username);
        headers.put(PASSWORD_PARAM, password);
        return headers;
    }

    public Map<String, String> getLoginHeaders() {
        final HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CONTENT_TYPE_PARAM, CONTENT_TYPE_VALUE);
        headers.put(ORIGIN_PARAM, ORIGIN_VALUE);
        return headers;
    }

    public HttpEntity getLoginHTTPEntity(String username, String password) {
        final JSONObject json = new JSONObject();
        try {
            json.put(EMAIL_PARAM, username);
            json.put(PASSWORD_PARAM, password);
            StringEntity se = null;
            try {
                se = new StringEntity(json.toString());
                se.setContentEncoding("UTF-8");
                se.setContentType(CONTENT_TYPE_VALUE);
                return se;
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    private String buildAlMaghribRequest(String endpoint, List<NameValuePair> params) {
        return buildRequest(BASE_REQUEST_URL + endpoint, params);
    }
}
