package com.yuqirong.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrator on 2016/2/26.
 */
public class DBHelper {

    private DaoMaster mDaoMaster;
    private static DBHelper mDBHelper;
    private DaoSession mDaoSession;
    private ResultEntityDao mResultEntityDao;
    private ChannelEntityDao mChannelEntityDao;

    private DBHelper(Context context) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context,"rxnews.db",null);
        SQLiteDatabase writableDB = devOpenHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(writableDB);
        mDaoSession = mDaoMaster.newSession();
        mResultEntityDao = mDaoSession.getResultEntityDao();
        mChannelEntityDao = mDaoSession.getChannelEntityDao();
    }

    public synchronized static DBHelper getInstance(Context mContext) {
        if (mDBHelper == null) {
            mDBHelper = new DBHelper(mContext);
        }
        return mDBHelper;
    }

    public DaoSession getmDaoSession() {
        return mDaoSession;
    }

    public ResultEntityDao getResultEntityDao() {
        return mResultEntityDao;
    }

    public ChannelEntityDao getChannelEntityDao(){
        return mChannelEntityDao;
    }
}
