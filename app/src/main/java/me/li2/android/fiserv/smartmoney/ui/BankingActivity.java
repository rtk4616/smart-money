package me.li2.android.fiserv.smartmoney.ui;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import me.li2.android.fiserv.smartmoney.R;
import me.li2.android.fiserv.smartmoney.model.AccountItem;

/**
 * Created by weiyi on 01/07/2017.
 * https://github.com/li2
 */

public class BankingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private BankingOperationFragment mBankingOperationFragment;

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

        mBankingOperationFragment = (BankingOperationFragment)
                getSupportFragmentManager().findFragmentById(R.id.banking_operation_fragment);
        mBankingOperationFragment.setOnBankingOperationSelectListener(mOnBankingOperationSelectListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Test
        updateBankingOperationBkg();
        AccountItem item = new AccountItem("http://i.imgur.com/9gbQ7YR.jpg", "Weiyi Li", 1234567890, 521.13f);
        mBankingOperationFragment.updateAccountItemView(item);
    }

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
            };
}
