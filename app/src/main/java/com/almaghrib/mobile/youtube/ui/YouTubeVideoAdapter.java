package com.almaghrib.mobile.youtube.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.almaghrib.mobile.R;
import com.almaghrib.mobile.RequestQueueSingleton;
import com.almaghrib.mobile.youtube.tasks.YouTubeVideo;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

/**
 * This adapter is used to show our Video objects in a ListView
 * It hasn't got many memory optimisations, if your list is getting bigger or more complex
 * you may want to look at better using your view resources: http://developer.android.com/resources/samples/ApiDemos/src/com/example/android/apis/view/List14.html
 * @author paul.blundell
 */
public class YouTubeVideoAdapter extends BaseAdapter {
    private Context mContext;
    private ImageLoader mImageLoader;

    // The list of videos to display
    List<YouTubeVideo> videos;
    // An inflator to use when creating rows
    private LayoutInflater mInflater;
     
    /**
     * @param context this is the context that the list will be shown in - used to create new list rows
     * @param videos this is a list of videos to display
     */
    public YouTubeVideoAdapter(Context context, List<YouTubeVideo> videos) {
        mContext = context;
        // Get the ImageLoader through your singleton class.
        mImageLoader = RequestQueueSingleton.getInstance(context).getImageLoader();
        this.videos = videos;
        this.mInflater = LayoutInflater.from(context);
    }
 
    @Override
    public int getCount() {
        return videos.size();
    }
 
    @Override
    public Object getItem(int position) {
        return videos.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	YouTubeViewHolder holder;
    	
        // If convertView wasn't null it means we have already set it to our list_item_user_video so no need to do it again
        if(convertView == null){
            // This is the layout we are using for each row in our list
            // anything you declare in this layout can then be referenced below
            convertView = mInflater.inflate(R.layout.list_item_user_video, null);
            
            holder = new YouTubeViewHolder();
            // We are using a custom imageview so that we can load images using urls
            // For further explanation see: http://blog.blundell-apps.com/imageview-with-loading-spinner/
            holder.thumb = (NetworkImageView) convertView.findViewById(R.id.videoThumbImageView);
            holder.thumb.setTag(YouTubeFragment.TAG); // set tag so we can cancel unnecessary requests
            holder.title = (TextView) convertView.findViewById(R.id.videoTitleTextView);
            holder.publishedAt = (TextView) convertView.findViewById(R.id.videoPublishedAtTextView);
        
            // store the holder with the view
            convertView.setTag(holder);
        } else {
        	holder = (YouTubeViewHolder) convertView.getTag();
        }
        // Get a single video from our list
        final YouTubeVideo video = videos.get(position);

        holder.thumb.setDefaultImageResId(R.drawable.youtube_default_image);
        // Set the URL of the image that should be loaded into this view, and
        // specify the ImageLoader that will be used to make the request.
        holder.thumb.setImageUrl(video.getThumbUrl(), mImageLoader);
        // Set the image for the list item
        //holder.thumb.setImageDrawable(video.getThumbUrl());

        // Set the title for the list item
        holder.title.setText(video.getTitle());
        holder.publishedAt.setText(video.getpublishedDate());
        
        attachOpenVideoListener(convertView, video);
        
        return convertView;
    }

	private void attachOpenVideoListener(View convertView, final YouTubeVideo video) {
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Context context = v.getContext();
				if (context != null) {
					context.startActivity(new Intent(Intent.ACTION_VIEW,
							Uri.parse(video.getUrl())));
				}
			}
		});
	}
    
}