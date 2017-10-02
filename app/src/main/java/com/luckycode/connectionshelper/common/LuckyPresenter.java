package com.luckycode.connectionshelper.common;

/**
 * Created by marcelocuevas on 9/30/17.
 */

public class LuckyPresenter<T> {

    private T mView;

    public LuckyPresenter(T mView) {
        this.mView = mView;
    }

    protected T getView() {
        return mView;
    }

    public void detachView() {
        mView = null;
    }
}