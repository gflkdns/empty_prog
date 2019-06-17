package com.demo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageHolder extends RecyclerView.ViewHolder {
    ImageView imageView;

    public ImageHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageview);
    }

    public void setData(String url) {
        Glide.with(itemView.getContext()).load(url).centerCrop().into(imageView);
    }
}
