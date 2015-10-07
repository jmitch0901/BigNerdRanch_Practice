package com.jonathanmitchell.bignerdranchpractice.PhotoGallery;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.jonathanmitchell.bignerdranchpractice.PhotoGallery.Networking.FlickrFetchr;
import com.jonathanmitchell.bignerdranchpractice.PhotoGallery.Networking.GalleryItem;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jonathanmitchell on 1/9/15.
 */
public class ThumbnailDownloader<Token> extends HandlerThread {

    private static final String TAG = "TumbnailDownloader";

    private static final int MESSAGE_DOWNLOADED = 0;

    private LruCache<String, Bitmap> cache; //Map a bitmap BY it's URL

    private static final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    private static final int memAllocated = maxMemory/8;



    Handler mHandler;
    /*
        The data stored is the URL and you can reference these URL's given a
        token (As in index for an array. Token for this insatnce is an imageView)
     */
    Map<Token, String> requestMap
            = Collections.synchronizedMap(new HashMap<Token, String>());


    Handler mResponseHandler;

    Listener<Token> mListener;

    public interface Listener<Token>{
       public void onThumbnailDownloaded(Token token, Bitmap thumbNail);
    }

    public void setListener(Listener<Token> listener){
        mListener = listener;
    }

    public ThumbnailDownloader(Handler handleResponse){
        super(TAG);

        mResponseHandler = handleResponse;

        cache = new LruCache<String, Bitmap>(memAllocated){

            @TargetApi(12)
            @Override
            protected int sizeOf(String key, Bitmap value) {

                /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1)
                    return value.getByteCount()/1024;*/

                //return super.sizeOf(key,value);

                //Might crash with lower api than 12
                return value.getByteCount()/1024;
            }


        };

    }

    public void queueThumbnail(Token token, String url){

       // Log.i(TAG,"Got a URL: "+url);
         requestMap.put(token, url);
         mHandler.obtainMessage(MESSAGE_DOWNLOADED, token).sendToTarget();

    }

    @Override
    protected void onLooperPrepared() {

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == MESSAGE_DOWNLOADED) {

                    Token token = (Token) msg.obj;
                    Log.i(TAG, "Got a request for url: " + requestMap.get(token));

                    handleRequest(token);
                }
            }
        };

    }

    private void handleRequest(final Token token){

        try{

            final String url = requestMap.get(token);

            if(url == null)
                return;

            if(cache.get(url) != null){

                mResponseHandler.post(new Runnable() { //Posting a message back to the main thread
                    @Override
                    public void run() {

                        requestMap.remove(token);
                        mListener.onThumbnailDownloaded(token,cache.get(url));

                    }
                });


                return;
            }

            byte[] bitmapBytes = new FlickrFetchr().getUrlBytes(url);//Downloading done here


            final Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapBytes,0,bitmapBytes.length);
            Log.i(TAG,"Bitmap Created");

            cache.put(url,bitmap);


            mResponseHandler.post(new Runnable() { //Posting a message back to the main thread
                @Override
                public void run() {
                    if(requestMap.get(token)!=url){
                        return;
                    }

                    requestMap.remove(token);
                    mListener.onThumbnailDownloaded(token,bitmap);

                }
            });

        } catch (IOException e) {
            Log.e(TAG, "Error downloading image: ",e);
        }
    }

    public void clearQueue(){
        mHandler.removeMessages(MESSAGE_DOWNLOADED);
        requestMap.clear();
    }























}
