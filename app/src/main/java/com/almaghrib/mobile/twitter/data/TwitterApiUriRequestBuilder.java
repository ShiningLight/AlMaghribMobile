package com.almaghrib.mobile.twitter.data;

import android.util.Base64;
import android.util.Log;

import com.almaghrib.mobile.util.UriRequestBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TwitterApiUriRequestBuilder extends UriRequestBuilder {
    private static final String TAG = TwitterApiUriRequestBuilder.class.getSimpleName();

    private static final String BASE_REQUEST_URL = "https://api.twitter.com";

    private static final String TWITTER_API_VERSION = "/1.1";

    /* Endpoints */
    private static final String OAUTH2_REQUEST = "/oauth2/token";
    private static final String STATUSES_REQUEST = "/statuses";
    private static final String STATUSES_USER_TIMELINE_REQUEST = "/user_timeline.json";

    /* Parameters */
    private static final String SCREEN_NAME_PARAM = "screen_name";
    private static final String NUM_RESULTS_PARAM = "count";
    private static final String MAX_ID_PARAM = "max_id";

    /* Values */
    private static final String CONSUMER_KEY = "zD1MISbccs5YoRv1OJoTqa74y";
    private static final String CONSUMER_SECRET = "jrp5PQkiUuxMqk9oabmLe9jdVEdd7q3qsMjYYPpU3EJjHYnJy6";
    private static final String NUM_RESULTS_VALUE = "30";

    private static final String TOKEN_REQUEST_ENTITY = "grant_type=client_credentials";

    public TwitterApiUriRequestBuilder() {
    }

    public String buildTokenRequest() {
        return BASE_REQUEST_URL + OAUTH2_REQUEST;
    }

    public Map<String, String> buildTokenRequestHeaders() {
        try {
            // Step 1: Encode consumer key and secret
            // URL encode the consumer key and secret
            final String urlApiKey = URLEncoder.encode(CONSUMER_KEY, HTTP.UTF_8);
            final String urlApiSecret = URLEncoder.encode(CONSUMER_SECRET, HTTP.UTF_8);

            // Concatenate the encoded consumer key, a colon character, and the
            // encoded consumer secret
            final String combined = urlApiKey + ":" + urlApiSecret;

            // Base64 encode the string
            final String base64Encoded = Base64.encodeToString(combined.getBytes(), Base64.NO_WRAP);

            // Step 2: Obtain a bearer token
            final Map<String, String> headers = new HashMap<String, String>();
            headers.put("Authorization", "Basic " + base64Encoded);
            headers.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            return headers;

        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    public HttpEntity getTokenRequestEntity() {
        try {
            return new StringEntity(TOKEN_REQUEST_ENTITY);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    public String buildStatusUserTimelineRequest(String screenName, String maxId) {
        final List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(SCREEN_NAME_PARAM, screenName));
        params.add(new BasicNameValuePair(NUM_RESULTS_PARAM, NUM_RESULTS_VALUE));
        if (maxId != null) {
            // Subtract 1 from the lowest Tweet ID returned from the previous request and use this
            // for the value of max_id. It does not matter if this adjusted max_id is a valid
            // Tweet ID, or if it corresponds with a Tweet posted by a different user - the value
            // is just used to decide which Tweets to filter. When adjusted in this manner, it is
            // possible to page through a timeline without receiving redundant Tweets
            final long optimalMaxId = Long.valueOf(maxId) - 1;
            params.add(new BasicNameValuePair(MAX_ID_PARAM, String.valueOf(optimalMaxId)));
        }
        return buildTwitterApiRequest(STATUSES_REQUEST + STATUSES_USER_TIMELINE_REQUEST, params);
    }

    public Map<String, String> buildApiRequestHeaders(String accessToken) {
        // Step 3: Authenticate API requests with bearer token.

        // Construct a normal HTTPS request and include an Authorization
        // header with the value of Bearer <>
        final Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer " + accessToken);
        headers.put("Content-Type", "application/json");

        return headers;
    }

    private String buildTwitterApiRequest(String endpoint, List<NameValuePair> params) {
        return buildRequest(BASE_REQUEST_URL + TWITTER_API_VERSION + endpoint, params);
    }
}
