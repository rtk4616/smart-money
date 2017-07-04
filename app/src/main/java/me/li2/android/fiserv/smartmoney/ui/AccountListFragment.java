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
    private static final String ARG_KEY_ACCOUNT_LIST = "arg_key_account_list";

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;

    private AccountListAdapter mAdapter;
    private OnAccountSelectListener mOnAccountSelectListener;
    private ArrayList<AccountItem> mAccounts = new ArrayList<>();

    public AccountListFragment() {
    }

    public static AccountListFragment newInstance(ArrayList<AccountItem> accounts) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_KEY_ACCOUNT_LIST, accounts);

        AccountListFragment fragment = new AccountListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnAccountSelectListener(OnAccountSelectListener l) {
        mOnAccountSelectListener = l;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mAccounts = args.getParcelableArrayList(ARG_KEY_ACCOUNT_LIST);
        }
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_list, container, false);
        ButterKnife.bind(this, view);

        final RecyclerView recyclerView = mRecyclerView;
        mAdapter = new AccountListAdapter(getActivity());
        GridLayoutManager layoutManager = new GridLayoutManager(
                getActivity(), 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setScrollContainer(false);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    @SuppressWarnings("unused")
    private ArrayList<AccountItem> createItems() {
        ArrayList<AccountItem> items = new ArrayList<>();
        items.add(new AccountItem("http://i.imgur.com/9gbQ7YR.jpg", "Weiyi Li", 1234567890, 521.13f, 22.22f));
        items.add(new AccountItem("http://i.imgur.com/DNKnbG8.jpg", "New Zealand", 1234567896, 5.46f, 11.11f));
        return items;
    }

    public void update(ArrayList<AccountItem> accounts) {
        if (mAdapter != null && accounts != null) {
            mAccounts = accounts;
            mAdapter.notifyDataSetChanged();
        }
    }

    //-------- AccountListAdapter --------------------------------------------------

    private class AccountListAdapter extends RecyclerView.Adapter<AccountItemViewHolder> {
        private Context mContext;

        public AccountListAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getItemCount() {
            return (mAccounts != null) ? mAccounts.size() : 0;
        }

        @Override
        public AccountItemViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.list_account_item, parent, false);
            return new AccountItemViewHolder(itemView, mOnAccountSelectListener);
        }

        @Override
        public void onBindViewHolder(AccountItemViewHolder holder, int position) {
            holder.bindAccountItem(mAccounts.get(position), position);
        }
    }
}