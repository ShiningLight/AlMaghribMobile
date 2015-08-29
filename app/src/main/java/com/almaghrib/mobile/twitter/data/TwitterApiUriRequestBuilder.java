package com.almaghrib.mobile.twitter.data;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

public class TwitterApiUriRequestBuilder {
    private static final String TAG = TwitterApiUriRequestBuilder.class.getSimpleName();

    private static final String BASE_REQUEST_URL = "https://api.twitter.com";

    private static final String TWITTER_API_VERSION = "/1.1";

    /* Endpoints */
    // Information endpoints
    private static final String STATUSES_REQUEST = "/statuses";
    private static final String STATUSES_USER_TIMELINE_REQUEST = "/user_timeline.json";

    /* Parameters */
    // Information retrieval params
    private static final String SCREEN_NAME_PARAM = "screen_name";
    private static final String NUM_RESULTS_PARAM = "count";
    private static final String MAX_ID_PARAM = "max_id";

    /* Values */
    // OAuth values
    public static final String OAUTH_CALLBACK_VALUE = "http://www.almaghrib.org";
    // Information retrieval values
    public static final String CONSUMER_KEY = "qpJ9UeiRd7P5y9UGP7OfsarEz";
    public static final String CONSUMER_SECRET = "Nbm5vqLZN5M4TH9tXPgE7zXJ66PmADJsnGEL4dXI9ivwvJxa1J";

    public static final String TWITTER_KEY = "3XCYlJeAdYxkX87BLkcks3XzL";
    public static final String TWITTER_SECRET = "xUo1WvOKfhMxXU13uYFJFY3CtLGDCv96oKr7T5zgWj5TmhCXxJ";

    private static final String NUM_RESULTS_VALUE = "20";

    public static OAuthService buildOAuthService() {
        return new ServiceBuilder()
                .provider(TwitterApi.class)
                .apiKey(CONSUMER_KEY)
                .apiSecret(CONSUMER_SECRET)
                .callback(OAUTH_CALLBACK_VALUE)
                .build();
    }

    public static OAuthRequest buildStatusUserTimelineRequest(String screenName, String maxId) {
        final OAuthRequest request = new OAuthRequest(Verb.GET,
                buildTwitterApiRequestUrl(STATUSES_REQUEST + STATUSES_USER_TIMELINE_REQUEST));

        request.addQuerystringParameter(SCREEN_NAME_PARAM, screenName);
        request.addQuerystringParameter(NUM_RESULTS_PARAM, NUM_RESULTS_VALUE);
        if (maxId != null) {
            // Subtract 1 from the lowest Tweet ID returned from the previous request and use this
            // for the value of max_id. It does not matter if this adjusted max_id is a valid
            // Tweet ID, or if it corresponds with a Tweet posted by a different user - the value
            // is just used to decide which Tweets to filter. When adjusted in this manner, it is
            // possible to page through a timeline without receiving redundant Tweets
            final long optimalMaxId = Long.valueOf(maxId) - 1;
            request.addQuerystringParameter(MAX_ID_PARAM, String.valueOf(optimalMaxId));
        }
        return request;
    }

    private static String buildTwitterApiRequestUrl(String endpoint) {
        return BASE_REQUEST_URL + TWITTER_API_VERSION + endpoint;
    }

}
