package me.li2.android.fiserv.smartmoney.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import me.li2.android.fiserv.smartmoney.model.AccountItem;
import me.li2.android.fiserv.smartmoney.model.Accounts;
import me.li2.android.fiserv.smartmoney.webservice.FiservService;
import me.li2.android.fiserv.smartmoney.webservice.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by weiyi on 03/07/2017.
 * https://github.com/li2
 */

public class SmartMoneyService extends Service {
    private static final String TAG = "SmartMoneyService";

    private ArrayList<AccountItem> mAccounts = new ArrayList<>();

    public interface OnAccountsGetListener {
        void onAccountsGet(ArrayList<AccountItem> accounts);
    }

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

    public void getAccounts(final OnAccountsGetListener l) {
        FiservService service = ServiceGenerator.createService(FiservService.class);
        service.getAccounts().enqueue(new Callback<Accounts>() {
            @Override
            public void onResponse(Call<Accounts> call, Response<Accounts> response) {
                mAccounts = response.body().accounts;
                if (l != null) {
                    l.onAccountsGet(mAccounts);
                }
            }

            @Override
            public void onFailure(Call<Accounts> call, Throwable t) {
                if (l != null) {
                    l.onAccountsGet(null);
                }
            }
        });
    }
}
