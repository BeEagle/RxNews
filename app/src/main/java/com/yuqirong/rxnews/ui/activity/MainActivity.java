package com.yuqirong.rxnews.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.yuqirong.rxnews.R;
import com.yuqirong.rxnews.app.Constant;
import com.yuqirong.rxnews.event.VideoEvent;
import com.yuqirong.rxnews.ui.adapter.FragmentAdapter;
import com.yuqirong.rxnews.ui.fragment.FragmentFactory;
import com.yuqirong.rxnews.ui.view.VerticalDrawerLayout;

import butterknife.Bind;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.fab)
    FloatingActionButton mFAButton;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.nav_view)
    NavigationView mNavigationView;
    @Bind(R.id.mTabLayout)
    TabLayout mTabLayout;
    @Bind(R.id.mViewPager)
    ViewPager mViewPager;
    @Bind(R.id.mVerticalDrawerLayout)
    VerticalDrawerLayout mVerticalDrawerLayout;
    @Bind(R.id.ib_arrow)
    ImageButton ib_arrow;

    private FragmentManager fm;
    private FragmentAdapter adapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        mFAButton.setOnClickListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);
        fm = getSupportFragmentManager();
        adapter = new FragmentAdapter(fm);
        adapter.addFragment(FragmentFactory.createNewsFragment(Constant.OTHER_TYPE, Constant.FINANCE_ID), Constant.TITLE_ARRAYS[0]);
        adapter.addFragment(FragmentFactory.createNewsFragment(Constant.OTHER_TYPE, Constant.FOOTBALL_ID), Constant.TITLE_ARRAYS[1]);
        adapter.addFragment(FragmentFactory.createNewsFragment(Constant.OTHER_TYPE, Constant.ENTERTAINMENT_ID), Constant.TITLE_ARRAYS[2]);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(this);
        mTabLayout.setupWithViewPager(mViewPager);
        ib_arrow.setOnClickListener(this);
        mVerticalDrawerLayout.setDrawerListener(mSimpleDrawerListener);
    }

    private VerticalDrawerLayout.SimpleDrawerListener mSimpleDrawerListener = new VerticalDrawerLayout.SimpleDrawerListener() {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            ib_arrow.setRotation(slideOffset * 180);
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            // Handle the camera action
            startActivity(VideoActivity.class);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void onEventMainThread(VideoEvent videoEvent) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            case R.id.ib_arrow:
                if (mVerticalDrawerLayout.isDrawerOpen()) {
                    mVerticalDrawerLayout.closeDrawer();
                    mFAButton.show();
                } else {
                    mVerticalDrawerLayout.openDrawerView();
                    mFAButton.hide();
                }
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (adapter.getItem(position) == null) {
            //TODO INIT
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.clear();
    }

}
