package com.example.imgsurch;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.koushikdutta.ion.Ion;

import java.util.List;

public class IonImageUrlAdapter extends ImageUrlAdapter {

    public IonImageUrlAdapter(Context context, List<String> urls) {
        super(context, urls);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String url = getItem(position);

        ImageView view = (ImageView) convertView;
        if (view == null) {
            view = new ImageView(getContext());
            view.setMinimumWidth(160);
            view.setMinimumHeight(160);
        }

        Ion.with(view)
                .placeholder(R.drawable.contact_picture_placeholder)
                .error(android.R.drawable.stat_notify_error)
                .load(url);

        return view;
    }
}
