package com.yuqirong.rxnews.ui.activity;

import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import com.yuqirong.greendao.ChannelEntity;
import com.yuqirong.greendao.ChannelEntityDao;
import com.yuqirong.rxnews.R;
import com.yuqirong.rxnews.app.AppService;
import com.yuqirong.rxnews.app.Constant;
import com.yuqirong.rxnews.event.VideoEvent;
import com.yuqirong.rxnews.ui.adapter.ChannelAdapter;
import com.yuqirong.rxnews.ui.adapter.FragmentAdapter;
import com.yuqirong.rxnews.ui.adapter.UnselectedAdapter;
import com.yuqirong.rxnews.ui.fragment.FragmentFactory;
import com.yuqirong.rxnews.ui.view.DragGridView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener, AdapterView.OnItemClickListener {

    @Bind(R.id.mAppBarLayout)
    AppBarLayout mAppBarLayout;
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
    @Bind(R.id.ib_arrow)
    ImageButton ib_arrow;

    private FragmentManager fm;
    private FragmentAdapter adapter;
    private List<ChannelEntity> selectList;
    private List<ChannelEntity> unselectList;
    private UnselectedAdapter unselectedAdapter;
    private ChannelAdapter selectedAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);
        fm = getSupportFragmentManager();
        adapter = new FragmentAdapter(fm);

        List<ChannelEntity> selectChannel = getSelectChannel();

        for (ChannelEntity entity : selectChannel) {
            adapter.addFragment(FragmentFactory.createNewsFragment(Constant.OTHER_TYPE, entity.getTId()), entity.getName());
        }
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(this);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    // 从数据库中得到选中的栏目
    private List<ChannelEntity> getSelectChannel() {
        ChannelEntityDao channelEntityDao = AppService.getInstance().getChannelEntityDao();
        if (channelEntityDao.count() == 0) {
            for (ChannelEntity c : Constant.CHANNEL_LIST) {
                channelEntityDao.insert(c);
            }
        }
        List<ChannelEntity> list = channelEntityDao.queryBuilder().where(ChannelEntityDao.Properties.IsSelect.eq(true)).list();
        return list;
    }

    // 从数据库中得到未选中的栏目
    private List<ChannelEntity> getUnselectChannel() {
        ChannelEntityDao channelEntityDao = AppService.getInstance().getChannelEntityDao();
        if (channelEntityDao.count() == 0) {
            for (ChannelEntity c : Constant.CHANNEL_LIST) {
                channelEntityDao.insert(c);
            }
        }
        List<ChannelEntity> list = channelEntityDao.queryBuilder().where(ChannelEntityDao.Properties.IsSelect.eq(false)).list();
        return list;
    }


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
        // Handle action bar layout_channel_item clicks here. The action bar will
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
        // Handle navigation view layout_channel_item clicks here.
        int id = item.getItemId();
        // 视频
        if (id == R.id.nav_video) {
            startActivity(VideoActivity.class);

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        item.setChecked(false);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onEventMainThread(VideoEvent videoEvent) {

    }

    @OnClick({R.id.fab, R.id.ib_arrow})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            case R.id.ib_arrow:
                ObjectAnimator.ofFloat(ib_arrow, "rotationX", ib_arrow.getRotationX(), ib_arrow.getRotationX() + 180f).setDuration(500).start();
                View channelView = LayoutInflater.from(this).inflate(R.layout.layout_channel, null);

                DragGridView dgv_my_channel = (DragGridView) channelView.findViewById(R.id.dgv_my_channel);
                GridView gv_more = (GridView) channelView.findViewById(R.id.gv_more);
                dgv_my_channel.setOnItemClickListener(this);
                gv_more.setOnItemClickListener(this);

                dgv_my_channel.setWindowHeight(mAppBarLayout.getBottom());
                selectList = getSelectChannel();

                selectedAdapter = new ChannelAdapter(this, selectList);
                dgv_my_channel.setAdapter(selectedAdapter);

                unselectList = getUnselectChannel();

                unselectedAdapter = new UnselectedAdapter(this, unselectList);
                gv_more.setAdapter(unselectedAdapter);


                PopupWindow popupWindow = new PopupWindow(channelView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setTouchable(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.screen_background_light, getTheme()));
                } else {
                    popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.screen_background_light));
                }
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        ObjectAnimator.ofFloat(ib_arrow, "rotationX", ib_arrow.getRotationX(), ib_arrow.getRotationX() + 180f).setDuration(500).start();
                    }
                });
                popupWindow.setAnimationStyle(R.style.window_anim_style);
                popupWindow.showAsDropDown(mTabLayout);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ChannelEntity entity;
        switch (parent.getId()) {
            case R.id.dgv_my_channel:
                // 数据库中更新数据
                entity = selectList.get(position);
                entity.setIsSelect(false);
                AppService.getInstance().getChannelEntityDao().update(entity);

                // 界面上的调整
                selectList.remove(position);
                selectedAdapter.notifyDataSetChanged();
                unselectList.add(entity);
                unselectedAdapter.notifyDataSetChanged();
                break;

            case R.id.gv_more:
                // 数据库中更新数据
                entity = unselectList.get(position);
                entity.setIsSelect(true);
                AppService.getInstance().getChannelEntityDao().update(entity);

                // 界面上的调整
                unselectList.remove(position);
                unselectedAdapter.notifyDataSetChanged();
                selectList.add(entity);
                selectedAdapter.notifyDataSetChanged();
                break;
        }
    }

//    // 给window设置透明度
//    private void setWindowAlpha(float alpha) {
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.alpha = alpha; //0.0-1.0
//        getWindow().setAttributes(lp);
//    }

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
