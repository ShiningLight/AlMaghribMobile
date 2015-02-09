package com.almaghrib.mobile.facebook.data;

import com.almaghrib.mobile.util.UriRequestBuilder;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class FacebookApiUriRequestBuilder extends UriRequestBuilder {
    private static final String TAG = FacebookApiUriRequestBuilder.class.getSimpleName();

    private static final String BASE_REQUEST_URL = "https://graph.facebook.com";

    /* Endpoints */
    private static final String OAUTH_REQUEST = "/oauth/access_token";
    private static final String FEED_REQUEST = "/feed";

    /* Parameters */
    private static final String CLIENT_ID_PARAM = "client_id";
    private static final String CLIENT_SECRET_PARAM = "client_secret";
    private static final String GRANT_TYPE_PARAM = "grant_type";

    private static final String FIELDS_PARAM = "fields";
    private static final String ACCESS_TOKEN_PARAM = "access_token";

    /* Values */
    private static final String APP_ID = "301385320059899"; // client_id value
    private static final String APP_SECRET = "976a406c33a8587d2303c82fdf4bfed3"; // client_secret value
    private static final String GRANT_TYPE_VALUE = "client_credentials";

    private static final String FIELDS_VALUE = "created_time,id,message,story,status_type,picture,caption,link,from{name,id},attachments{media}";

    public FacebookApiUriRequestBuilder() {
    }

    public String buildAccessTokenRequest() {
        final List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(CLIENT_ID_PARAM, APP_ID));
        params.add(new BasicNameValuePair(CLIENT_SECRET_PARAM, APP_SECRET));
        params.add(new BasicNameValuePair(GRANT_TYPE_PARAM, GRANT_TYPE_VALUE));

        return buildFacebookRequest(OAUTH_REQUEST, params);
    }

    public String buildFeedRequest(String user, String accessToken) {
        final List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(FIELDS_PARAM, FIELDS_VALUE));
        params.add(new BasicNameValuePair(ACCESS_TOKEN_PARAM, getFormattedAccessToken(accessToken)));

        return buildFacebookRequest("/" + user + FEED_REQUEST, params);
    }

    /**
     * Get the formatted access token value without the parameter name.
     * access_token contains something like "access_token=XXXXXXXXXX|YYYYYY" ,
     * so we need to replace the pipe and remove the param name.
     * @param accessToken unformatted access token along with the parameter name
     * @return formatted access token value
     */
    private String getFormattedAccessToken(String accessToken) {
        accessToken = accessToken.split(ACCESS_TOKEN_PARAM + "=")[1]; // Get actual token
        accessToken = accessToken.replace("|", "%7C");
        return accessToken;
    }

    private String buildFacebookRequest(String endpoint, List<NameValuePair> params) {
        return buildRequest(BASE_REQUEST_URL + endpoint, params);
    }
}
