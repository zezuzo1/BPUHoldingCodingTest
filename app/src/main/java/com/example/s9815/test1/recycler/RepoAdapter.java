package com.example.s9815.test1.recycler;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import com.example.s9815.test1.InAppBrowserActivity;
import com.example.s9815.test1.R;
import com.example.s9815.test1.model.*;

public class RepoAdapter extends RecyclerView.Adapter<RepoRowHolder> {

    private List<RepoItem> feedItemList;
    private Context mContext;
    private final View.OnClickListener mOnClickListener = new MyOnClickListener();

    GlobalFunction service = GlobalFunction.sharedInstance;

    public RepoAdapter(Context context, List<RepoItem> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    @Override
    public RepoRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.repo_row, null);
        RepoRowHolder mh = new RepoRowHolder(v);
        v.setOnClickListener(mOnClickListener);
        v.setTag(mh);
        return mh;
    }

    public class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(final View view) {
            RepoRowHolder holder = (RepoRowHolder)view.getTag();
            int itemPosition = holder.getAdapterPosition();
            RepoItem feedItem = feedItemList.get(itemPosition);
            String target_url = feedItem.getTargetUrl();

            if( target_url == null || target_url.length() <= 0 )
            {
                Toast toast = Toast.makeText (view.getContext(), "Homepage field are empty", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }

            Intent intent = new Intent(mContext, InAppBrowserActivity.class);
            intent.putExtra(InAppBrowserActivity.KEY_URL,target_url);
            mContext.startActivity(intent);
        }
    }

    @Override
    public void onBindViewHolder(RepoRowHolder feedListRowHolder, int i) {
        RepoItem feedItem = feedItemList.get(i);
        service.asyncLoadImage(feedItem.getThumbnail(),feedListRowHolder.thumbnail);
        feedListRowHolder.title.setText(feedItem.getTitle());
        feedListRowHolder.subTitle.setText(feedItem.getSubTitle());
        feedListRowHolder.starCount.setText(feedItem.getStarCount());
    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }
}
