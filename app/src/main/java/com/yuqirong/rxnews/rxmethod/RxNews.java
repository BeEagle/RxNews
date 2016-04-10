package com.yuqirong.rxnews.rxmethod;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.yuqirong.greendao.ResultEntity;
import com.yuqirong.greendao.ResultEntityDao;
import com.yuqirong.rxnews.app.AppService;
import com.yuqirong.rxnews.app.Constant;
import com.yuqirong.rxnews.event.NewsDetailEvent;
import com.yuqirong.rxnews.event.NewsEvent;
import com.yuqirong.rxnews.event.VideoEvent;
import com.yuqirong.rxnews.module.news.model.bean.News;
import com.yuqirong.rxnews.module.news.model.bean.NewsDetail;
import com.yuqirong.rxnews.module.video.model.bean.Video;

import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by yuqirong on 2016/2/24.
 */
public class RxNews {

    private static final String TAG = "RxNews";

    /**
     * 得到news列表的缓存
     *
     * @param id
     * @return
     */
    public static Subscription initNews(final String id) {
        Subscription subscription = Observable.create(new Observable.OnSubscribe<List<News>>() {

            @Override
            public void call(Subscriber<? super List<News>> subscriber) {
                List<News> news = getCacheNews(id);
                subscriber.onNext(news);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).subscribe(new Subscriber<List<News>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "error : " + e.getMessage());
                NewsEvent event = new NewsEvent(Constant.Tag.INIT, null, id);
                event.setResult(Constant.Result.FAIL);
                AppService.getInstance().getEventBus().post(event);
            }

            @Override
            public void onNext(List<News> news) {
                NewsEvent event;
                if (news == null) {
                    event = new NewsEvent(Constant.Tag.INIT, null, id);
                    event.setResult(Constant.Result.FAIL);
                } else {
                    event = new NewsEvent(Constant.Tag.INIT, news, id);
                    event.setResult(Constant.Result.SUCCESS);
                }
                AppService.getInstance().getEventBus().post(event);
            }
        });
        return subscription;
    }

    /**
     * 得到实时的新闻列表
     *
     * @param type
     * @param id
     * @param startPage
     * @return
     */
    public static Subscription getNews(final String type, final String id, final int startPage) {
        Subscription subscription = AppService.getInstance().getRxNewsAPI().getNews(type, id, startPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Map<String, List<News>>, Observable<News>>() {
                    @Override
                    public Observable<News> call(Map<String, List<News>> stringListMap) {
                        return Observable.from(stringListMap.get(id));
                    }
                })
                .toSortedList(new Func2<News, News, Integer>() {
                    // 按时间先后排序
                    @Override
                    public Integer call(News news1, News news2) {
                        return -news1.ptime.compareTo(news2.ptime);
                    }
                })
                .doOnNext(new Action1<List<News>>() {

                    @Override
                    public void call(List<News> news) {
                        // cache news
                        String json = AppService.getInstance().getGson().toJson(news);
                        cacheJSON(json, id);
                    }
                })
                .subscribe(new Subscriber<List<News>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "getNews onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "error : " + e.getMessage());
                        NewsEvent event = new NewsEvent(startPage == 0 ? Constant.Tag.REFRESH : Constant.Tag.LOAD_MORE, null, id);
                        AppService.getInstance().getEventBus().post(event);
                    }

                    @Override
                    public void onNext(List<News> news) {
                        NewsEvent event;
                        if (news == null) {

                            event = new NewsEvent(startPage == 0 ? Constant.Tag.REFRESH : Constant.Tag.LOAD_MORE, null, id);
                            event.setResult(Constant.Result.FAIL);
                        } else {
                            event = new NewsEvent(startPage == 0 ? Constant.Tag.REFRESH : Constant.Tag.LOAD_MORE, news, id);
                            event.setResult(Constant.Result.SUCCESS);
                        }
                        AppService.getInstance().getEventBus().post(event);
                    }
                });
        return subscription;
    }

    /**
     * 得到新闻的详情
     *
     * @param id
     * @param postId
     * @return
     */
    public static Subscription getNewsDetail(final String id, final String postId) {
        Subscription subscription = AppService.getInstance().getRxNewsAPI().getNewsDetail(postId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Map<String, NewsDetail>, NewsDetail>() {

                    @Override
                    public NewsDetail call(Map<String, NewsDetail> stringNewsDetailMap) {
                        return stringNewsDetailMap.get(postId);
                    }
                })
                .subscribe(new Subscriber<NewsDetail>() {

                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "getNewsDetail onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "error : " + e.getMessage());
                        NewsDetailEvent event = new NewsDetailEvent(id, null);
                        AppService.getInstance().getEventBus().post(event);
                    }

                    @Override
                    public void onNext(NewsDetail newsDetail) {
                        NewsDetailEvent event;
                        if (newsDetail == null) {
                            event = new NewsDetailEvent(id, null);
                            event.setResult(Constant.Result.FAIL);
                        } else {
                            event = new NewsDetailEvent(id, newsDetail);
                            event.setResult(Constant.Result.SUCCESS);
                        }
                        AppService.getInstance().getEventBus().post(event);
                    }
                });
        return subscription;
    }

    /**
     * 缓存新闻
     *
     * @param json
     * @param id
     */
    // 缓存数据
    private static void cacheJSON(String json, String id) {
        ResultEntityDao dao = AppService.getInstance().getResultEntityDao();
        // 删除旧数据
        dao.queryBuilder().where(ResultEntityDao.Properties.NId.eq(id)).buildDelete()
                .executeDeleteWithoutDetachingEntities();
        // 插入新数据
        ResultEntity resultEntity = new ResultEntity(null, json, id);
        dao.insert(resultEntity);
    }

    /**
     * 从缓存中读取数据
     *
     * @param id
     * @return
     */
    private static List<News> getCacheNews(String id) {
        ResultEntityDao dao = AppService.getInstance().getResultEntityDao();
        // 查询
        ResultEntity entity = dao.queryBuilder().where(ResultEntityDao.Properties.NId.eq(id)).build().unique();

        List<News> news = AppService.getInstance().getGson().fromJson(entity.getJson(), new TypeToken<List<News>>() {
        }.getType());
        return news;
    }


    public static Subscription initVideos(final String id) {
        Subscription subscription = Observable.create(new Observable.OnSubscribe<List<Video>>() {

            @Override
            public void call(Subscriber<? super List<Video>> subscriber) {
                List<Video> cacheVideos = getCacheVideos(id);
                subscriber.onNext(cacheVideos);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Video>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "error : " + e.getMessage());
                        VideoEvent event = new VideoEvent(id, null, Constant.Tag.INIT);
                        event.setResult(Constant.Result.FAIL);
                        AppService.getInstance().getEventBus().post(event);
                    }

                    @Override
                    public void onNext(List<Video> videos) {
                        VideoEvent event;
                        if (videos == null) {
                            event = new VideoEvent(id, null, Constant.Tag.INIT);
                            event.setResult(Constant.Result.FAIL);
                        } else {
                            event = new VideoEvent(id, videos, Constant.Tag.INIT);
                            event.setResult(Constant.Result.SUCCESS);
                        }
                        AppService.getInstance().getEventBus().post(event);
                    }
                });
        return subscription;
    }

    public static Subscription getVideo(final String id, final int startPage) {
        Subscription subscription = AppService.getInstance().getRxNewsAPI().getVideo(id, startPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Map<String, List<Video>>, Observable<Video>>() {
                    @Override
                    public Observable<Video> call(Map<String, List<Video>> stringListMap) {
                        return Observable.from(stringListMap.get(id));
                    }
                }).toSortedList(new Func2<Video, Video, Integer>() {
                    @Override
                    public Integer call(Video video, Video video2) {
                        return -video.ptime.compareTo(video2.ptime);
                    }
                }).doOnNext(new Action1<List<Video>>() {
                    @Override
                    public void call(List<Video> videos) {
                        // cache video
                        String json = AppService.getInstance().getGson().toJson(videos);
                        cacheJSON(json, id);
                    }
                }).subscribe(new Subscriber<List<Video>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "getVideo onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "error : " + e.getMessage());
                        VideoEvent event = new VideoEvent(id, null, startPage == 0 ? Constant.Tag.REFRESH : Constant.Tag.LOAD_MORE);
                        AppService.getInstance().getEventBus().post(event);
                    }

                    @Override
                    public void onNext(List<Video> videos) {
                        VideoEvent event;
                        if (videos == null) {
                            event = new VideoEvent(id, null, startPage == 0 ? Constant.Tag.REFRESH : Constant.Tag.LOAD_MORE);
                            event.setResult(Constant.Result.FAIL);
                        } else {
                            event = new VideoEvent(id, videos, startPage == 0 ? Constant.Tag.REFRESH : Constant.Tag.LOAD_MORE);
                            event.setResult(Constant.Result.SUCCESS);
                        }
                        AppService.getInstance().getEventBus().post(event);
                    }
                });

        return subscription;
    }

    /**
     * 从缓存中读取数据
     *
     * @param id
     * @return
     */
    private static List<Video> getCacheVideos(String id) {
        ResultEntityDao resultEntityDao = AppService.getInstance().getResultEntityDao();
        ResultEntity entity = resultEntityDao.queryBuilder().where(ResultEntityDao.Properties.NId.eq(id)).unique();
        List<Video> videos = AppService.getInstance().getGson().fromJson(entity.getJson(), new TypeToken<List<News>>() {
        }.getType());
        return videos;
    }

}
