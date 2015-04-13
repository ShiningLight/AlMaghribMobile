package com.almaghrib.mobile.youtube.data;

import com.almaghrib.mobile.util.UriRequestBuilder;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class YouTubeApiUriRequestBuilder extends UriRequestBuilder {

    public static final String BASE_REQUEST_URL = "https://www.googleapis.com/youtube/v3";

    public static final String WATCH_BASE_URL = "https://www.youtube.com/watch?v=";

    /* Endpoints */
    public static final String SEARCH_REQUEST = "/search";

    /* Parameters */
    private static final String KEY_PARAM = "key";
    private static final String CHANNEL_ID_PARAM = "channelId";
    private static final String PART_PARAM = "part";
    private static final String ORDER_PARAM = "order";
    private static final String MAX_RESULTS_PARAM = "maxResults";
    private static final String TYPE_PARAM = "type";
    private static final String PAGE_TOKEN_PARAM = "pageToken";

    /* Values */
    private static final String KEY_VALUE = "AIzaSyCq5TVzGp1J6_nPCLwaiHfs6C4gSSbHzuM";
    private static final String PART_VALUE = "snippet,id";
    private static final String ORDER_VALUE = "date";
    private static final String DEFAULT_MAX_RESULTS_VALUE = "15";
    private static final String TYPE_VALUE = "video";

    public YouTubeApiUriRequestBuilder() {
    }

    public String buildSearchRequest(String channelId, String pageToken) {
        return buildSearchRequest(channelId, pageToken, DEFAULT_MAX_RESULTS_VALUE);
    }

    public String buildSearchRequest(String channelId, String pageToken, String maxResults) {
        final List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(KEY_PARAM, KEY_VALUE));
        params.add(new BasicNameValuePair(CHANNEL_ID_PARAM, channelId));
        params.add(new BasicNameValuePair(PART_PARAM, PART_VALUE));
        params.add(new BasicNameValuePair(ORDER_PARAM, ORDER_VALUE));
        params.add(new BasicNameValuePair(MAX_RESULTS_PARAM, maxResults));
        params.add(new BasicNameValuePair(TYPE_PARAM, TYPE_VALUE));
        if (pageToken != null) {
            params.add(new BasicNameValuePair(PAGE_TOKEN_PARAM, pageToken));
        }
        return buildYouTubeRequest(SEARCH_REQUEST, params);
    }

    private String buildYouTubeRequest(String endpoint, List<NameValuePair> params) {
        return buildRequest(BASE_REQUEST_URL + endpoint, params);
    }
}
