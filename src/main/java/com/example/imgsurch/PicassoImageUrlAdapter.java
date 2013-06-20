package com.example.imgsurch;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.lang.Override;import java.lang.String;import java.util.List;

public class PicassoImageUrlAdapter extends ImageUrlAdapter {
    public PicassoImageUrlAdapter(Context context, List<String> urls) {
        super(context, urls);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String url = getItem(position);

        ImageView view = (ImageView) convertView;
        if (view == null) {
            view = new ImageView(getContext());
            view.setMinimumHeight(160);
            view.setMinimumWidth(160);
        }

        Picasso.with(getContext())
                .load(url)
                .placeholder(R.drawable.contact_picture_placeholder)
                .error(android.R.drawable.stat_notify_error)
                .into(view);

        return view;
    }
}
