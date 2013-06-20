package com.example.imgsurch;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.lang.Override;import java.lang.String;import java.util.List;

abstract class ImageUrlAdapter extends BaseAdapter {
    private final Context mContext;
    private final List<String> mUrls;

    public ImageUrlAdapter(Context context, List<String> urls) {
        mContext = context;

        mUrls = urls;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);

    @Override
    public int getCount() {
        return mUrls.size();
    }

    @Override
    public String getItem(int position) {
        return mUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    protected Context getContext() {
        return mContext;
    }
}
