package com.almaghrib.mobile.facebook.data;

public class FacebookFeedItem {
	private int id;
	private String name, status, image, timeStamp, url;

	public FacebookFeedItem() {
	}

	public FacebookFeedItem(String name, String image, String status, String timeStamp, String url) {
		super();
		this.id = 1;
		this.name = name;
		this.image = image;
		this.status = status;
		this.timeStamp = timeStamp;
		this.url = url;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
