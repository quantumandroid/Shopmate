package com.volley.config;

import android.content.Context;
import android.graphics.Bitmap;

import android.text.TextUtils;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class VolleyClient {

    public static final String TAG = VolleyClient.class.getSimpleName();

    public static VolleyClient mInstance;

    private Context context;

    private RequestQueue mRequestQueue;

    private ImageLoader mImageLoader;

    public VolleyClient(Context context) {
        this.context = context;

        mRequestQueue = getRequestQueue();
    }

    public static synchronized VolleyClient getInstance(Context context) {
        // If Instance is null then initialize new Instance
        if (mInstance == null) {
            mInstance = new VolleyClient(context);
        }
        // Return MySingleton new Instance
        return mInstance;
    }

    public RequestQueue getRequestQueue() {

        if (mRequestQueue == null) {

            mRequestQueue = Volley.newRequestQueue(context);

        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {

        /*
         * Set a tag on this request. Can be used to cancel all requests with this tag by RequestQueue.
         * cancelAll(Object).
         * */

        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        /*
         * Set a tag on this request. Can be used to cancel all requests with this tag by RequestQueue.
         * cancelAll(Object).
         * */
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public ImageLoader getImageLoader() {

        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(mRequestQueue,
                    new ImageLoader.ImageCache() {
                        private final LruCache<String, Bitmap>
                                cache = new LruCache<String, Bitmap>(20);

                        @Override
                        public Bitmap getBitmap(String url) {
                            return cache.get(url);
                        }

                        @Override
                        public void putBitmap(String url, Bitmap bitmap) {
                            cache.put(url, bitmap);
                        }
                    });
        }


        return mImageLoader;
    }

}
