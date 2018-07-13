package com.ivanmorett.parsetragram.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ivanmorett.parsetragram.GlideApp;
import com.ivanmorett.parsetragram.Models.Post;
import com.ivanmorett.parsetragram.constants.Images;

import java.util.List;

public class ImagesAdapter extends BaseAdapter {

    private final Context context;
    private final List<Post> mPosts;

    public ImagesAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.mPosts = posts;
    }

    @Override
    public int getCount() {
        return mPosts.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Post getItem(int position) {
        return mPosts.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(context);
        GlideApp.with(context)
                .load(mPosts.get(position).getImage().getUrl())
                .into(imageView);
        return imageView;
    }

}