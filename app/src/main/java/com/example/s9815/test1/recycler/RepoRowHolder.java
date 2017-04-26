package com.example.s9815.test1.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.s9815.test1.R;

public class RepoRowHolder extends RecyclerView.ViewHolder {
    protected ImageView thumbnail;
    protected TextView title;
    protected TextView subTitle;
    protected TextView starCount;

    public RepoRowHolder(View view) {
        super(view);
        this.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        this.title = (TextView) view.findViewById(R.id.title);
        this.subTitle = (TextView) view.findViewById(R.id.subTitle);
        this.starCount = (TextView) view.findViewById(R.id.starcount);
    }
}