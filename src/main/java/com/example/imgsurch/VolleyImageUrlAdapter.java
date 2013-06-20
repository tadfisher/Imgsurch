package com.example.imgsurch;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VolleyImageUrlAdapter extends ImageUrlAdapter {
    private ImageLoader.ImageCache mBitmapCache;
    private ImageLoader mImageLoader;
    private RequestQueue mRequestQueue;

    public VolleyImageUrlAdapter(Context context, List<String> urls) {
        super(context, urls);

        mRequestQueue = Volley.newRequestQueue(context);
        mBitmapCache = new BitmapCache(50);
        mImageLoader = new ImageLoader(mRequestQueue, mBitmapCache);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String url = getItem(position);

        NetworkImageView view = (NetworkImageView) convertView;
        if (view == null) {
            view = new NetworkImageView(getContext());
            view.setMinimumHeight(160);
            view.setMinimumWidth(160);
            view.setDefaultImageResId(R.drawable.contact_picture_placeholder);
            view.setErrorImageResId(android.R.drawable.stat_notify_error);
            view.setLayoutParams(new GridView.LayoutParams(160, 160));
        }

        view.setImageUrl(url, mImageLoader);

        return view;
    }

    static class BitmapCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {
        public BitmapCache(int maxSize) {
            super(maxSize);
        }

        @Override
        public Bitmap getBitmap(String url) {
            return get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            put(url, bitmap);
        }
    }
}
