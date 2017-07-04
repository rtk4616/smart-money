package me.li2.android.fiserv.smartmoney.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by weiyi on 03/07/2017.
 * https://github.com/li2
 */

public class SmartMoneyService extends Service {
    private static final String TAG = "SmartMoneyService";
    
    public class SmartMoneyServiceBinder extends Binder {
        public SmartMoneyService getService() {
            return SmartMoneyService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new SmartMoneyServiceBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
