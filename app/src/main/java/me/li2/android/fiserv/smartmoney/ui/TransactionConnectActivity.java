package me.li2.android.fiserv.smartmoney.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import me.li2.android.fiserv.smartmoney.R;
import me.li2.android.fiserv.smartmoney.model.AccountItem;
import me.li2.android.fiserv.smartmoney.model.TransactionItem;

/**
 * Created by weiyi on 04/07/2017.
 * https://github.com/li2
 */

public class TransactionConnectActivity extends AppCompatActivity {
    private static final String ARG_BUNDLE_KEY = "arg_bundle_key";

    public static Intent newIntent(Context packageContext, AccountItem account, TransactionItem transaction) {
        Bundle args = new Bundle();
        args.putParcelable(TransactionConnectFragment.ARG_KEY_ACCOUNT, account);
        args.putParcelable(TransactionConnectFragment.ARG_KEY_TRANSACTION, transaction);

        Intent intent = new Intent(packageContext, TransactionConnectActivity.class);
        intent.putExtra(ARG_BUNDLE_KEY, args);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_connect);

        Intent intent = getIntent();
        Bundle args = null;
        if (intent != null) {
            args = intent.getBundleExtra(ARG_BUNDLE_KEY);
        }

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = TransactionConnectFragment.newInstance(args);
        }
        fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
    }
}
