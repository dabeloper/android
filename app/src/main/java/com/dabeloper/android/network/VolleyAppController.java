package com.dabeloper.android.network;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NoCache;
import com.android.volley.toolbox.Volley;

/**
 * Created by David Antonio Bolaños AKA DABELOPER on 04/09/2017.
 */

public class VolleyAppController {

    public static final String TAG = VolleyAppController.class.getSimpleName();

    private Context mContext;
    private RequestQueue mRequestQueue, mRequestQueueNoCache;
    private ImageLoader mImageLoader;

    private static VolleyAppController mInstance;

    public static synchronized VolleyAppController getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyAppController(context);
        }
        return mInstance;
    }//END getInstance

    public VolleyAppController( @NonNull Context context ){
        this.mContext = context;
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(this.mContext.getApplicationContext());
        }
        if (mRequestQueueNoCache == null) {
            mRequestQueueNoCache = new RequestQueue(new NoCache(), new BasicNetwork(new HurlStack()));
        }
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue, new VolleyBitmapCache());
        }
    }//END constructor

    public RequestQueue getRequestQueue() {
        return this.mRequestQueue;
    }

    public RequestQueue getRequestQueueNoCache() {
        return this.mRequestQueueNoCache;
    }

    public ImageLoader getImageLoader() {
        return this.mImageLoader;
    }

    //Add Request to the Queue
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    //Add Request to the Queue without a TAG(ID)
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG+Math.random());
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueueNoCache(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueueNoCache().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}
