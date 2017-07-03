package me.li2.android.fiserv.smartmoney.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;

import me.li2.android.fiserv.smartmoney.R;
import me.li2.android.fiserv.smartmoney.model.AccountItem;
import me.li2.android.fiserv.smartmoney.service.SmartMoneyService;
import me.li2.android.fiserv.smartmoney.widget.AccountItemViewHolder;

/**
 * Created by weiyi on 01/07/2017.
 * https://github.com/li2
 */

public class BankingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "BankingActivity";
    private SmartMoneyService mSmartMoneyService;
    private AccountListFragment mAccountListFragment;
    private BankingOperationFragment mBankingOperationFragment;
    private KProgressHUD mLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_banking);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        showLoadingView();
        attachService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detachService();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_banking, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_g) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //-------- Service ------------------------------------------------------------------

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SmartMoneyService.SmartMoneyServiceBinder binder = (SmartMoneyService.SmartMoneyServiceBinder) service;
            mSmartMoneyService = binder.getService();
            onServiceAttached(mSmartMoneyService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mSmartMoneyService = null;
        }
    };

    private void attachService() {
        Intent intent = new Intent(this, SmartMoneyService.class);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    private void detachService() {
        unbindService(mServiceConnection);
    }

    private void onServiceAttached(SmartMoneyService service) {
        getAccounts();
    }

    private void getAccounts() {
        if (mSmartMoneyService == null) {
            return;
        }
        mSmartMoneyService.getAccounts(new SmartMoneyService.OnAccountsGetListener() {
            @Override
            public void onAccountsGet(ArrayList<AccountItem> accounts) {
                if (accounts != null && accounts.size() > 0) {
                    hideLoadingView();
                    setupBankingOperationFragment(accounts.get(0), accounts.size());
                } else {
                    Log.e(TAG, "failed to get accounts");
                }
            }
        });
    }


    //-------- UI Part ------------------------------------------------------------------

    private void showLoadingView () {
        mLoadingView = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.logging_in))
                .show();
    }

    private void hideLoadingView() {
        if (mLoadingView != null) {
            mLoadingView.dismiss();
        }
    }

    private void showFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left, -1)
                    .show(fragment)
                    .commit();
        }
    }

    private void hideFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(-1, android.R.anim.slide_out_right)
                    .hide(fragment)
                    .commit();
        }
    }

    //-------- AccountListFragment ------------------------------------------------------

    private void setupAccountListFragment(ArrayList<AccountItem> accounts) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment  = fm.findFragmentById(R.id.fragment_account_list_container);

        if (fragment == null) {
            fragment = AccountListFragment.newInstance(accounts);
            fm.beginTransaction().add(R.id.fragment_account_list_container, fragment).commit();
            fm.executePendingTransactions();
        }
        mAccountListFragment = (AccountListFragment) fragment;
        mAccountListFragment.setOnAccountSelectListener(mOnAccountSelectListener);
    }

    private AccountItemViewHolder.OnAccountSelectListener mOnAccountSelectListener =
            new AccountItemViewHolder.OnAccountSelectListener() {
                @Override
                public void onAccountSelect(AccountItem accountItem) {
                    Log.d(TAG, "Select account " + accountItem.name);
                    // TODO show Transaction list
                }
            };

    //-------- BankingOperationFragment -------------------------------------------------

    private void setupBankingOperationFragment(AccountItem accountItem, int accountNumber) {
        // NOTE21: add fragment programmatically, instead of inflated in the layout.
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment  = fm.findFragmentById(R.id.fragment_banking_operation_container);

        if (fragment == null) {
            fragment = BankingOperationFragment.newInstance(accountItem, accountNumber);
            fm.beginTransaction().add(R.id.fragment_banking_operation_container, fragment).commit();
            fm.executePendingTransactions();
        }
        mBankingOperationFragment = (BankingOperationFragment) fragment;
        mBankingOperationFragment.setOnBankingOperationSelectListener(mOnBankingOperationSelectListener);
    }

    private BankingOperationFragment.OnBankingOperationSelectListener mOnBankingOperationSelectListener =
            new BankingOperationFragment.OnBankingOperationSelectListener() {
                @Override
                public void onBankingOperationSelect(@BankingOperationFragment.BankingOperation int operation) {
                    Toast.makeText(getApplicationContext(), "banking operation " + operation, Toast.LENGTH_SHORT).show();
                    switch (operation) {
                        case BankingOperationFragment.BANKING_OPERATION_PAY_MERCHANT:
                            break;

                        case BankingOperationFragment.BANKING_OPERATION_PAY_BILL:
                            break;

                        case BankingOperationFragment.BANKING_OPERATION_PAY_COMPANY:
                            break;

                        case BankingOperationFragment.BANKING_OPERATION_PAY_PERSON:
                            break;

                        case BankingOperationFragment.BANKING_OPERATION_REQUEST_MONEY:
                            break;

                        case BankingOperationFragment.BANKING_OPERATION_DEPOSIT_CHECK:
                            break;

                        case BankingOperationFragment.BANKING_OPERATION_INSIGHTS:
                            break;

                        case BankingOperationFragment.BANKING_OPERATION_OFFERS:
                            break;

                        case BankingOperationFragment.BANKING_OPERATION_WALLET:
                            break;
                    }
                }

                @Override
                public void onRecyclerViewSetup() {
                    updateBankingOperationBkg();
                }

                @Override
                public void onTransferAccount() {
                    hideFragment(mBankingOperationFragment);
                    if (mAccountListFragment == null) {
                        setupAccountListFragment(mSmartMoneyService.mAccounts);
                    } else {
                        showFragment(mAccountListFragment);
                    }
                }
            };

    private void updateBankingOperationBkg() {
        mBankingOperationFragment.updateItem(
                BankingOperationFragment.BANKING_OPERATION_INSIGHTS,
                ContextCompat.getDrawable(this, R.drawable.i_banking_insights));
        mBankingOperationFragment.updateItem(
                BankingOperationFragment.BANKING_OPERATION_OFFERS,
                ContextCompat.getDrawable(this, R.drawable.i_banking_offers));
        mBankingOperationFragment.updateItem(
                BankingOperationFragment.BANKING_OPERATION_WALLET,
                ContextCompat.getDrawable(this, R.drawable.i_banking_wallet));
    }
}
