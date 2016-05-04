package com.yuqirong.rxnews.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yuqirong.rxnews.R;
import com.yuqirong.rxnews.event.NewsEvent;
import com.yuqirong.rxnews.ui.fragment.NewsDetailFragment;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import cn.jpush.android.api.JPushInterface;

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
        Bundle bundle = getIntent().getExtras();
        String postid = null;
        String id = null;
        String imgsrc = null;
        String title = null;

        // 判断extras是否为null，不为null则是推送的新闻
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        if (extras != null) {
            try {
                JSONObject jsonObject = new JSONObject(extras);
                postid = jsonObject.getString("postid");
                id = jsonObject.getString("id");
                imgsrc = jsonObject.getString("imgsrc");
                title = jsonObject.getString("title");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            postid = bundle.getString("postid");
            id = bundle.getString("id");
            imgsrc = bundle.getString("imgsrc");
            title = bundle.getString("title");
        }

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

        // 如果是推送的新闻
        if (extras != null) {
            bundle.putString("postid",postid);
            bundle.putString("id",id);
        }
        mNewsDetailFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.mFrameLayout, mNewsDetailFragment, "NewsDetailFragment").commit();
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
