package com.luckycode.connectionshelper.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by marcelocuevas on 9/30/17.
 */

public abstract class LuckyFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(layout(), container, false);
        ButterKnife.bind(this, v);
        init();
        return v;
    }

    protected abstract int layout();


    protected abstract void init();


}