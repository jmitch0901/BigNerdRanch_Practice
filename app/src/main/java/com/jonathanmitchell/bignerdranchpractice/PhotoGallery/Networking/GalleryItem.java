package com.jonathanmitchell.bignerdranchpractice.PhotoGallery.Networking;

/**
 * Created by jonathanmitchell on 1/9/15.
 */
public class GalleryItem {

    private String mCaption;
    private String mId;
    private String mUrl;

    public GalleryItem(){
        mCaption="";
        mId="";
        mUrl="";
    }

    public GalleryItem(String caption, String id, String url){
        mCaption=caption;
        mId=id;
        mUrl=url;
    }

    public String toString(){
        return mCaption;
    }

    public String getCaption() {
        return mCaption;
    }

    public void setCaption(String mCaption) {
        this.mCaption = mCaption;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String debugToString(){
        return "Caption: "+mCaption+", ID: "+mId+", URL: "+mUrl+"\n";
    }
}
