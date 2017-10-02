package com.luckycode.connectionshelper.common;

import android.app.Application;
import android.content.Context;

import com.luckycode.connectionshelper.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by marcelocuevas on 9/30/17.
 */

public class LuckyCodeApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Seravek-Medium.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

    public static LuckyCodeApp getApp(Context context) {
        return (LuckyCodeApp) context.getApplicationContext();
    }

}