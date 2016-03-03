package com.yuqirong.rxnews.event;

import com.yuqirong.rxnews.app.Constant;

/**
 * Created by Administrator on 2016/2/24.
 */
public class BaseEvent {

    protected Constant.Result result;

    public Constant.Result getResult() {
        return result;
    }

    public void setResult(Constant.Result result) {
        this.result = result;
    }
}
