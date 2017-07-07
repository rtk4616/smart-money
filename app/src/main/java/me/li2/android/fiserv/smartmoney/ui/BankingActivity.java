package me.li2.android.fiserv.smartmoney.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

import me.li2.android.fiserv.smartmoney.R;
import me.li2.android.fiserv.smartmoney.model.AccountItem;
import me.li2.android.fiserv.smartmoney.model.Accounts;
import me.li2.android.fiserv.smartmoney.model.ChatCause;
import me.li2.android.fiserv.smartmoney.service.SmartMoneyService;
import me.li2.android.fiserv.smartmoney.utils.ViewUtils;
import me.li2.android.fiserv.smartmoney.webservice.FiservService;
import me.li2.android.fiserv.smartmoney.webservice.ServiceGenerator;
import me.li2.android.fiserv.smartmoney.widget.AccountItemViewHolder;
import me.li2.android.fiserv.smartmoney.widget.NavigationViewManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static me.li2.android.fiserv.smartmoney.ui.ChatSessionFragment.ARG_KEY_CHAT_CAUSE;

/**
 * Created by weiyi on 01/07/2017.
 * https://github.com/li2
 */

public class BankingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "BankingActivity";
    private static final String ACTION_START_CHAT = "action_start_chat";

    private SmartMoneyService mService;
    private ArrayList<AccountItem> mAccountItems;
    private AccountItem mSelAccount;
    private NavigationViewManager mNavigationViewMgr;

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
        mNavigationViewMgr = new NavigationViewManager(navigationView);

        attachService();
        loadingAccounts();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // NOTE21: this method will refresh intent, otherwise getIntent will always return the very First.
        // and that's why intent extra is null.
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String action = getIntent().getAction();
        if (action == ACTION_START_CHAT) {
            ChatCause chatCause = getIntent().getParcelableExtra(ARG_KEY_CHAT_CAUSE);
            if (chatCause != null) {
                startChat(chatCause);
            }
        }
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

        if (id == R.id.nav_banking) {

        } else if (id == R.id.nav_connect) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_about) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //-------- Loading Accounts ---------------------------------------------------------

    private void loadingAccounts() {
        showLoadingDialog();
        FiservService service = ServiceGenerator.createService(FiservService.class);
        service.getAccounts().enqueue(new Callback<Accounts>() {
            @Override
            public void onResponse(Call<Accounts> call, Response<Accounts> response) {
                hideLoadingDialog();
                onAccountsGet(response.body().accounts);
            }

            @Override
            public void onFailure(Call<Accounts> call, Throwable t) {
                Log.e(TAG, "failed to get accounts");
                hideLoadingDialog();
                showLoadingFailedDialog();
            }
        });
    }

    private void onAccountsGet(ArrayList<AccountItem> accounts) {
        if (accounts == null || accounts.size() <= 0) {
            showLoadingFailedDialog();
            return;
        }

        Iterator<AccountItem> iterator = accounts.iterator();
        long position = 0;
        while (iterator.hasNext()) {
            AccountItem item = iterator.next();
            item.position = position++; // NOTE21 for transition with unique id between list-detail screen.
        }
        mAccountItems = accounts;
        mSelAccount = accounts.get(0);

        BankingOperationFragment fragment = BankingOperationFragment.newInstance(mSelAccount, accounts.size());
        fragment.setOnBankingOperationSelectListener(mOnBankingOperationSelectListener);
        addFragmentToStack(null, fragment);

        mNavigationViewMgr.updateAccountView(mSelAccount.name, mSelAccount.avatarUrl);
    }

    private Dialog mLoadingDialog;

    private void showLoadingDialog() {
        mLoadingDialog = ViewUtils.showLoadingDialog(new WeakReference<Activity>(this),
                getString(R.string.logging_in));
    }

    private void hideLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    private void showLoadingFailedDialog() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.logging_in_failed)
                .setPositiveButton(R.string.try_again, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadingAccounts();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .setCancelable(false)
                .show();
    }


    //-------- Service ------------------------------------------------------------------

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SmartMoneyService.SmartMoneyServiceBinder binder = (SmartMoneyService.SmartMoneyServiceBinder) service;
            mService = binder.getService();
            onServiceAttached(mService);
            mNavigationViewMgr.updateChatStatus(mService.isChatting);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService.isChatting = false;
            mService = null;
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
    }


    //-------- UI Part ------------------------------------------------------------------
    // NOTE21: use Fragment stack to manage multiple fragments in only one container,
    // it's much more easier than hide/show with more containers.
    // Shared elements between fragments refer to https://github.com/lgvalle/Material-Animations#shared-elements-between-fragments

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void addFragmentToStack(View sharedElement, Fragment newFragment) {
        Log.d(TAG, "add fragment to stack: " + newFragment.getClass().getSimpleName());

        Slide slideTransition = new Slide(Gravity.RIGHT);
        slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_medium));

        ChangeBounds changeBoundsTransition = new ChangeBounds();
        changeBoundsTransition.setDuration(getResources().getInteger(R.integer.anim_duration_medium));

        //newFragment.setEnterTransition(slideTransition);
        newFragment.setAllowEnterTransitionOverlap(false);
        newFragment.setAllowReturnTransitionOverlap(false);
        newFragment.setSharedElementEnterTransition(changeBoundsTransition);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, newFragment);
        if (!(newFragment instanceof BankingOperationFragment)) {
            ft.addToBackStack(null);
        }
        if (sharedElement != null && ViewCompat.getTransitionName(sharedElement) != null) {
            ft.addSharedElement(sharedElement, ViewCompat.getTransitionName(sharedElement)); // NOTE21-transition
        }
        ft.commit();
    }

    //-------- AccountListFragment ------------------------------------------------------

    private AccountItemViewHolder.OnAccountSelectListener mOnAccountSelectListener =
            new AccountItemViewHolder.OnAccountSelectListener() {
                @Override
                public void onAccountSelect(AccountItem accountItem, View sharedElement) {
                    // NOTE21-transition: pass sharedElement from fragment to activity by callback
                    Log.d(TAG, "Select account " + accountItem.name);
                    addFragmentToStack(sharedElement, TransactionListFragment.newInstance(accountItem));
                }
            };

    //-------- BankingOperationFragment -------------------------------------------------

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
                }

                @Override
                public void onTransferAccount(View sharedElement) {
                    AccountListFragment fragment = AccountListFragment.newInstance(mAccountItems);
                    fragment.setOnAccountSelectListener(mOnAccountSelectListener);
                    addFragmentToStack(sharedElement, fragment);
                }
            };

    //-------- Chat Session Fragment ----------------------------------------------------
    public static Intent newIntentToChat(Context packageContext, ChatCause chatCause) {
        Intent intent = new Intent(packageContext, BankingActivity.class);
        intent.setAction(ACTION_START_CHAT);
        intent.putExtra(ARG_KEY_CHAT_CAUSE, chatCause);
        return intent;
    }

    private void startChat(ChatCause chatCause) {
        ChatSessionFragment fragment = ChatSessionFragment.newInstance(chatCause);
        addFragmentToStack(null, fragment);

        if (mService != null) {
            mService.chatToName = chatCause.chatToName;
            mService.chatToAvatorUrl = chatCause.chatToAvatorUrl;
            mService.isChatting = true;
        }

        mNavigationViewMgr.updateChatView(chatCause.chatToName, chatCause.chatToAvatorUrl);
    }
}
