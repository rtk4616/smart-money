package me.li2.android.fiserv.smartmoney.model;

import android.app.Application;
import android.content.Context;

/**
 * Created by weiyi on 07/07/2017.
 * https://github.com/li2
 */

public class MainApplication extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

    // static way to get Context on Android.
    public static Context getAppContext() {
        return sContext;
    }
}
