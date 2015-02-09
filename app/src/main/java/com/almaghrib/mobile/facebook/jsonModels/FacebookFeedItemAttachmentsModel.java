package com.almaghrib.mobile.facebook.jsonModels;

import com.almaghrib.mobile.facebook.data.FacebookFeedItem;

import java.util.ArrayList;

public class FacebookFeedItemAttachmentsModel {
    ArrayList<FacebookFeedItemAttachmentDataModel> data;

    public FacebookFeedItemAttachmentsModel(
            ArrayList<FacebookFeedItemAttachmentDataModel> data) {
        this.data = data;
    }

    public ArrayList<FacebookFeedItemAttachmentDataModel> getData() {
        return data;
    }

    public void setData(ArrayList<FacebookFeedItemAttachmentDataModel> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "FacebookFeedItemAttachmentsModel{" +
                "data=" + ((data != null) ? data.toString() : null) +
                '}';
    }

    public static class FacebookFeedItemAttachmentDataModel {
        FacebookFeedItemAttachmentDataMediaModel media;

        public FacebookFeedItemAttachmentDataModel(
                FacebookFeedItemAttachmentDataMediaModel media) {
            this.media = media;
        }

        public FacebookFeedItemAttachmentDataMediaModel getMedia() {
            return media;
        }

        public void setMedia(FacebookFeedItemAttachmentDataMediaModel media) {
            this.media = media;
        }

        @Override
        public String toString() {
            return "FacebookFeedItemAttachmentDataModel{" +
                    "media=" + ((media != null) ? media.toString() : null) +
                    '}';
        }

        public static class FacebookFeedItemAttachmentDataMediaModel {
            FacebookFeedItemAttachmentDataMediaImageModel image;

            public FacebookFeedItemAttachmentDataMediaModel(
                    FacebookFeedItemAttachmentDataMediaImageModel image) {
                this.image = image;
            }

            public FacebookFeedItemAttachmentDataMediaImageModel getImage() {
                return image;
            }

            public void setImage(FacebookFeedItemAttachmentDataMediaImageModel image) {
                this.image = image;
            }

            @Override
            public String toString() {
                return "FacebookFeedItemAttachmentDataMediaModel{" +
                        "image=" + ((image != null) ? image.toString() : null) +
                        '}';
            }

            public class FacebookFeedItemAttachmentDataMediaImageModel {
                String height;
                String width;
                String src;

                public FacebookFeedItemAttachmentDataMediaImageModel(String height) {
                    this.height = height;
                }

                public String getHeight() {
                    return height;
                }

                public String getWidth() {
                    return width;
                }

                public String getSrc() {
                    return src;
                }

                public void setHeight(String height) {
                    this.height = height;
                }

                public void setWidth(String width) {
                    this.width = width;
                }

                public void setSrc(String src) {
                    this.src = src;
                }

                @Override
                public String toString() {
                    return "FacebookFeedItemAttachmentDataMediaImageModel{" +
                            "height='" + height + '\'' +
                            ", width='" + width + '\'' +
                            ", src='" + src + '\'' +
                            '}';
                }
            }

        }

    }

}
