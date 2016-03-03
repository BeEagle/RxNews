package com.yuqirong.rxnews.rxmethod;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.yuqirong.greendao.ResultEntity;
import com.yuqirong.greendao.ResultEntityDao;
import com.yuqirong.rxnews.app.AppService;
import com.yuqirong.rxnews.app.Constant;
import com.yuqirong.rxnews.event.NewsEvent;
import com.yuqirong.rxnews.model.News;

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
 * Created by Administrator on 2016/2/24.
 */
public class RxNews {

    private static final String TAG = "RxNews";

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

    public static Subscription getNews(final String type, final String id, int startPage) {
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
                        return news1.ptime.compareTo(news2.ptime);
                    }
                })
                .doOnNext(new Action1<List<News>>() {

                    @Override
                    public void call(List<News> news) {
                        // TODO cache news
                        String json = AppService.getInstance().getGson().toJson(news);
                        cacheNews(json, id);
                    }
                })
                .subscribe(new Subscriber<List<News>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "error : " + e.getMessage());
                        NewsEvent event = new NewsEvent(Constant.Tag.REFRESH, null, id);
                        AppService.getInstance().getEventBus().post(event);
                    }

                    @Override
                    public void onNext(List<News> news) {
                        NewsEvent event;
                        if (news == null) {
                            event = new NewsEvent(Constant.Tag.REFRESH, null, id);
                            event.setResult(Constant.Result.FAIL);
                        } else {
                            event = new NewsEvent(Constant.Tag.REFRESH, news, id);
                            event.setResult(Constant.Result.SUCCESS);
                        }
                        AppService.getInstance().getEventBus().post(event);
                    }
                });
        return subscription;
    }

    // 缓存数据
    private static void cacheNews(String json, String id) {
        ResultEntityDao dao = AppService.getInstance().getResultEntityDao();
        // 删除旧数据
        dao.queryBuilder().where(ResultEntityDao.Properties.NId.eq(id)).buildDelete()
                .executeDeleteWithoutDetachingEntities();
        // 插入新数据
        ResultEntity resultEntity = new ResultEntity(null, json, id);
        dao.insert(resultEntity);
    }

    // 从缓存中读取数据
    private static List<News> getCacheNews(String id) {
        ResultEntityDao dao = AppService.getInstance().getResultEntityDao();
        // 查询
        ResultEntity entity = dao.queryBuilder().where(ResultEntityDao.Properties.NId.eq(id)).build().unique();

        List<News> news = AppService.getInstance().getGson().fromJson(entity.getJson(), new TypeToken<List<News>>() {
        }.getType());
        return news;
    }

}
