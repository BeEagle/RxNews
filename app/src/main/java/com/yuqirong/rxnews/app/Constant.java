package com.yuqirong.rxnews.app;

import com.yuqirong.greendao.ChannelEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/24.
 */
public class Constant {

    public static final String BASE_URL = "http://c.m.163.com";

    // 头条
    public static final String HEADLINE_TYPE = "headline";
    // 北京
    public static final String LOCAL_TYPE = "local";
    // 房产
    public static final String HOUSE_TYPE = "house";
    // 其他
    public static final String OTHER_TYPE = "list";

    // 头条
    public static final String HEADLINE_ID = "T1348647909107";
    // 足球
    public static final String FOOTBALL_ID = "T1399700447917";
    // 娱乐
    public static final String ENTERTAINMENT_ID = "T1348648517839";
    // 体育
    public static final String SPORTS_ID = "T1348649079062";
    // 财经
    public static final String FINANCE_ID = "T1348648756099";
    // 科技
    public static final String TECH_ID = "T1348649580692";
    // 电影
    public static final String MOVIE_ID = "T1348648650048";
    // 汽车
    public static final String CAR_ID = "T1348654060988";
    // 笑话
    public static final String JOKE_ID = "T1350383429665";
    // 游戏
    public static final String GAME_ID = "T1348654151579";
    // 时尚
    public static final String FASHION_ID = "T1348650593803";
    // 情感
    public static final String EMOTION_ID = "T1348650839000";
    // 精选
    public static final String CHOICE_ID = "T1370583240249";
    // 电台
    public static final String RADIO_ID = "T1379038288239";
    // nba
    public static final String NBA_ID = "T1348649145984";
    // 数码
    public static final String DIGITAL_ID = "T1348649776727";
    // 移动
    public static final String MOBILE_ID = "T1351233117091";
    // 彩票
    public static final String LOTTERY_ID = "T1356600029035";
    // 教育
    public static final String EDUCATION_ID = "T1348654225495";
    // 论坛
    public static final String FORUM_ID = "T1349837670307";
    // 旅游
    public static final String TOUR_ID = "T1348654204705";
    // 手机
    public static final String PHONE_ID = "T1348649654285";
    // 博客
    public static final String BLOG_ID = "T1349837698345";
    // 社会
    public static final String SOCIETY_ID = "T1348648037603";
    // 家居
    public static final String FURNISHING_ID = "T1348654105308";
    // 暴雪游戏
    public static final String BLIZZARD_ID = "T1397016069906";
    // 亲子
    public static final String PATERNITY_ID = "T1397116135282";
    // CBA
    public static final String CBA_ID = "T1348649475931";
    // 消息
    public static final String MSG_ID = "T1371543208049";
    // 军事
    public static final String MILITARY_ID = "T1348648141035";

    /**
     * 视频 http://c.3g.163.com/nc/video/list/V9LG4B3A0/n/10-10.html
     */
    // 热点视频
    public static final String VIDEO_HOT_ID = "V9LG4B3A0";
    // 娱乐视频
    public static final String VIDEO_ENTERTAINMENT_ID = "V9LG4CHOR";
    // 搞笑视频
    public static final String VIDEO_FUN_ID = "V9LG4E6VR";
    // 精品视频
    public static final String VIDEO_CHOICE_ID = "00850FRB";

    public enum Result {
        SUCCESS, FAIL
    }

    public enum Tag {
        REFRESH, INIT, LOAD_MORE
    }

    public static final String[] VIDEO_TITLE_ARRAYS = new String[]{"热点", "娱乐", "搞笑", "精品"};

    /**
     * 频道
     */
    public static final List<ChannelEntity> CHANNEL_LIST = new ArrayList<ChannelEntity>(30) {
        {
            add(new ChannelEntity("头条", "T1348647909107", true));
            add(new ChannelEntity("足球", "T1399700447917", false));
            add(new ChannelEntity("娱乐", "T1348648517839", false));
            add(new ChannelEntity("体育", "T1348649079062", false));
            add(new ChannelEntity("财经", "T1348648756099", false));
            add(new ChannelEntity("科技", "T1348649580692", false));
            add(new ChannelEntity("电影", "T1348648650048", false));
            add(new ChannelEntity("汽车", "T1348654060988", false));
            add(new ChannelEntity("笑话", "T1350383429665", false));
            add(new ChannelEntity("游戏", "T1348654151579", false));

            add(new ChannelEntity("情感", "T1348650839000", false));
            add(new ChannelEntity("精选", "T1370583240249", false));
            add(new ChannelEntity("电台", "T1379038288239", false));
            add(new ChannelEntity("NBA", "T1348649145984", false));
            add(new ChannelEntity("数码", "T1348649776727", false));
            add(new ChannelEntity("移动", "T1351233117091", false));
            add(new ChannelEntity("彩票", "T1356600029035", false));
            add(new ChannelEntity("教育", "T1348654225495", false));
            add(new ChannelEntity("论坛", "T1349837670307", false));
            add(new ChannelEntity("旅游", "T1348654204705", false));

            add(new ChannelEntity("手机", "T1348649654285", false));
            add(new ChannelEntity("博客", "T1349837698345", false));
            add(new ChannelEntity("社会", "T1348648037603", false));
            add(new ChannelEntity("游戏", "T1397016069906", false));
            add(new ChannelEntity("亲子", "T1397116135282", false));
            add(new ChannelEntity("CBA", "T1348649475931", false));
            add(new ChannelEntity("旅游", "T1348654204705", false));
            add(new ChannelEntity("军事", "T1348648141035", false));

        }
    };

}
