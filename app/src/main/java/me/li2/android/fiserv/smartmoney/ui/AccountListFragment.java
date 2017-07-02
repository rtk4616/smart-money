package me.li2.android.fiserv.smartmoney.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.li2.android.fiserv.smartmoney.R;
import me.li2.android.fiserv.smartmoney.model.AccountItem;
import me.li2.android.fiserv.smartmoney.widget.AccountItemViewHolder;
import me.li2.android.fiserv.smartmoney.widget.AccountItemViewHolder.OnAccountSelectListener;

/**
 * Created by weiyi on 02/07/2017.
 * https://github.com/li2
 */

public class AccountListFragment extends Fragment {
    private static final String TAG = "BankingOperation";

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    private AccountListAdapter mAdapter;
    private OnAccountSelectListener mOnAccountSelectListener;

    public AccountListFragment() {
    }

    public void setOnAccountSelectListener(OnAccountSelectListener l) {
        mOnAccountSelectListener = l;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_list, container, false);
        ButterKnife.bind(this, view);

        final RecyclerView recyclerView = mRecyclerView;
        mAdapter = new AccountListAdapter(getActivity(), createItems());
        GridLayoutManager layoutManager = new GridLayoutManager(
                getActivity(), 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setScrollContainer(false);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    private ArrayList<AccountItem> createItems() {
        ArrayList<AccountItem> items = new ArrayList<>();
        items.add(new AccountItem("http://i.imgur.com/9gbQ7YR.jpg", "Weiyi Li", 1234567890, 521.13f));
        items.add(new AccountItem("http://i.imgur.com/0E2tgV7.jpg", "Checking Account", 1234567891, 1.29f));
        items.add(new AccountItem("http://i.imgur.com/P5JLfjk.jpg", "Saving Account", 1234567892, 45.34f));
        items.add(new AccountItem("http://i.imgur.com/nz67a4F.jpg", "Genius Credit Card", 1234567893, 52.49f));
        items.add(new AccountItem("http://i.imgur.com/dFH34N5.jpg", "Student Loan", 1234567894, 35.98f));
        items.add(new AccountItem("http://i.imgur.com/FI49ftb.jpg", "Credit Card", 1234567895, 0.13f));
        items.add(new AccountItem("http://i.imgur.com/DNKnbG8.jpg", "New Zealand", 1234567896, 5.46f));
        return items;
    }

    //-------- AccountListAdapter --------------------------------------------------

    private class AccountListAdapter extends RecyclerView.Adapter<AccountItemViewHolder> {
        private Context mContext;
        private List<AccountItem> mItems;

        public AccountListAdapter(Context context, List<AccountItem> items) {
            mContext = context;
            mItems = items;
        }

        @Override
        public int getItemCount() {
            return (mItems != null) ? mItems.size() : 0;
        }

        @Override
        public AccountItemViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.list_account_item, parent, false);
            return new AccountItemViewHolder(itemView, mOnAccountSelectListener);
        }

        @Override
        public void onBindViewHolder(AccountItemViewHolder holder, int position) {
            holder.bindAccountItem(mItems.get(position), position);
        }
    }
}