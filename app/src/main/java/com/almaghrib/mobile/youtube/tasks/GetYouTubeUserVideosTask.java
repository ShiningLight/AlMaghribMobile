package com.almaghrib.mobile.youtube.tasks;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
 
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.almaghrib.mobile.util.StreamUtils;
 
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * This is the task that will ask YouTube for a list of videos for a specified user</br>
 * This class implements Runnable meaning it will be ran on its own Thread</br>
 * Because it runs on it's own thread we need to pass in an object that is notified when it has finished
 *
 */
public class GetYouTubeUserVideosTask implements Runnable {
    private static final String TAG = GetYouTubeUserVideosTask.class.getSimpleName();
    
    // A reference to retrieve the data when this task finishes
    public static final String LIBRARY = "Library";
    
    private static final SimpleDateFormat RETRIEVED_DATE_FORMAT =
    		new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private static final SimpleDateFormat OUTPUT_DATE_FORMAT =
    		new SimpleDateFormat("dd-MM-yyyy");
    
    // A handler that will be notified when the task is finished
    private final Handler replyTo;
    // The user we are querying on YouTube for videos
    private final String channelId;
 
    /**
     * Don't forget to call run(); to start this task
     * @param replyTo - the handler you want to receive the response when this task has finished
     * @param channelId - the username of who on YouTube you are browsing
     */
    public GetYouTubeUserVideosTask(Handler replyTo, String channelId) {
        this.replyTo = replyTo;
        this.channelId = channelId;
    }
     
    @Override
    public void run() {
    	// Get a httpclient to talk to the internet
    	HttpClient client = new DefaultHttpClient();
        try {
            // Perform a GET request to YouTube for a JSON list of all the videos by a specific user
            HttpUriRequest request = new HttpGet(
            		YouTubeConstants.BASE_REQUEST_URL + YouTubeConstants.SEARCH_REQUEST +
            		"?key=AIzaSyCq5TVzGp1J6_nPCLwaiHfs6C4gSSbHzuM&channelId=" + channelId + "&part=snippet,id&order=date&maxResults=20");
            
            // Get the response that YouTube sends back
            HttpResponse response = client.execute(request);
            
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            
	            // Convert this response into a readable string
	            String jsonString = StreamUtils.convertToString(response.getEntity().getContent());

                Log.d(TAG, jsonString);

	            // Create a JSON object that we can use from the String
	            JSONObject json = new JSONObject(jsonString);
	             
	            // For further information about the syntax of this request and JSON-C
	            // see the documentation on YouTube http://code.google.com/apis/youtube/2.0/developers_guide_jsonc.html
	             
	            // Get are search result items
	            JSONArray jsonArray = (JSONArray) json.get("items");
	             
	            // Create a list to store are videos in
	            List<YouTubeVideo> videos = new ArrayList<YouTubeVideo>();
	            // Loop round our JSON list of videos creating Video objects to use within our app
	            for (int i = 0; i < jsonArray.length(); i++) {
	                JSONObject jsonObject = jsonArray.getJSONObject(i);
	                
	                final String videoId = jsonObject.getJSONObject("id").getString("videoId");
	                
	                final JSONObject snippetJsonObj = jsonObject.getJSONObject("snippet");
	                final String title = snippetJsonObj.getString("title");
	                final String publishedAt = snippetJsonObj.getString("publishedAt");
	                Date d = null;
					try {
						d = RETRIEVED_DATE_FORMAT.parse(publishedAt);
					} catch (ParseException e) {
						Log.e(TAG, e.getMessage(), e);
					}
	                final String formattedTime = (d != null) ? OUTPUT_DATE_FORMAT.format(d) : "";
	                
	                // A url to the thumbnail image of the video
	                // We will use this later to get an image using a Custom ImageView
	                // Found here http://blog.blundell-apps.com/imageview-with-loading-spinner/
	                final String thumbUrl = snippetJsonObj.getJSONObject("thumbnails")
	                		.getJSONObject("default").getString("url");
	                 
	                // Create the video object and add it to our list
	                videos.add(new YouTubeVideo(
	                		title, YouTubeConstants.WATCH_BASE_URL + videoId, thumbUrl, formattedTime));
	            }
	            // Create a library to hold our videos
	            final YouTubeVideoLibrary lib = new YouTubeVideoLibrary(channelId, videos);
	            // Pack the Library into the bundle to send back to the Activity
	            Bundle data = new Bundle();
	            data.putSerializable(LIBRARY, lib);
	             
	            // Send the Bundle of data (our Library) back to the handler (our Activity)
	            Message msg = Message.obtain();
	            msg.setData(data);
	            replyTo.sendMessage(msg);
            }
             
        // We don't do any error catching, just nothing will happen if this task falls over
        // an idea would be to reply to the handler with a different message so your Activity can act accordingly
        } catch (ClientProtocolException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
        	client.getConnectionManager().shutdown();
        }
    }
}