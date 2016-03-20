package com.yuqirong.rxnews.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yuqirong.rxnews.R;
import com.yuqirong.rxnews.event.NewsEvent;
import com.yuqirong.rxnews.ui.fragment.NewsDetailFragment;

import butterknife.Bind;

/**
 * Created by yuqirong on 2016/3/9.
 */
public class NewsDetailActivity extends BaseActivity {

    @Bind(R.id.mCoordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;
    @Bind(R.id.mCollapsingToolbarLayout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @Bind(R.id.iv_album)
    ImageView iv_album;
    @Bind(R.id.mToolbar)
    Toolbar mToolbar;
    @Bind(R.id.mFrameLayout)
    FrameLayout mFrameLayout;

    NewsDetailFragment mNewsDetailFragment;

    @Override
    public int getContentViewId() {
        return R.layout.activity_news_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getBundleExtra("params");
        String postId = bundle.getString("postId");
        String id = bundle.getString("id");
        String imgsrc = bundle.getString("imgsrc");
        String title = bundle.getString("title");

        setSupportActionBar(mToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        mCollapsingToolbarLayout.setTitle(title);
        mCollapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.colorAccent));
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, android.R.color.white));

        Glide.with(this).load(imgsrc).centerCrop()
                .placeholder(R.drawable.thumbnail_default).crossFade().into(iv_album);

        mNewsDetailFragment = new NewsDetailFragment();
        mNewsDetailFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.mFrameLayout, mNewsDetailFragment, "NewsDetailFragment").commit();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }
    }

    public void onEventMainThread(NewsEvent newsEvent) {

    }

}
