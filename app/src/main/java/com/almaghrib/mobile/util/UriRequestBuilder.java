package com.almaghrib.mobile.util;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.protocol.HTTP;

import java.util.List;

public class UriRequestBuilder {
    private static final String TAG = UriRequestBuilder.class.getSimpleName();

    public UriRequestBuilder() {
    }

    protected String buildRequest(String url, List<NameValuePair> params) {
        return addAllParametersToUrl(url, params);
    }

    private static String addAllParametersToUrl(String url, List<NameValuePair> params) {
        if (url == null) {
            return null;
        }
        if (params != null) {
            final String encodedParams = URLEncodedUtils.format(params, HTTP.UTF_8);
            url += "?" + encodedParams;
        }
        Log.d(TAG, "Url: " + url);
        return url;
    }
}
