package com.luckycode.connectionshelper.common;

/**
 * Created by marcelocuevas on 9/30/17.
 */

public abstract class LuckyInteractor<T extends LuckyPresenter> {

    private T mPresenter;

    public LuckyInteractor(T mPresenter) {
        this.mPresenter = mPresenter;
    }

    public LuckyInteractor() {

    }

    public T getmPresenter() {
        return mPresenter;
    }
}