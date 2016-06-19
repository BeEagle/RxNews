package com.yuqirong.greendao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.yuqirong.greendao.ResultEntity;
import com.yuqirong.greendao.ChannelEntity;

import com.yuqirong.greendao.ResultEntityDao;
import com.yuqirong.greendao.ChannelEntityDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig resultEntityDaoConfig;
    private final DaoConfig channelEntityDaoConfig;

    private final ResultEntityDao resultEntityDao;
    private final ChannelEntityDao channelEntityDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        resultEntityDaoConfig = daoConfigMap.get(ResultEntityDao.class).clone();
        resultEntityDaoConfig.initIdentityScope(type);

        channelEntityDaoConfig = daoConfigMap.get(ChannelEntityDao.class).clone();
        channelEntityDaoConfig.initIdentityScope(type);

        resultEntityDao = new ResultEntityDao(resultEntityDaoConfig, this);
        channelEntityDao = new ChannelEntityDao(channelEntityDaoConfig, this);

        registerDao(ResultEntity.class, resultEntityDao);
        registerDao(ChannelEntity.class, channelEntityDao);
    }
    
    public void clear() {
        resultEntityDaoConfig.getIdentityScope().clear();
        channelEntityDaoConfig.getIdentityScope().clear();
    }

    public ResultEntityDao getResultEntityDao() {
        return resultEntityDao;
    }

    public ChannelEntityDao getChannelEntityDao() {
        return channelEntityDao;
    }

}
