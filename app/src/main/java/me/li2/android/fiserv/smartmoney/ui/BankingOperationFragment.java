package me.li2.android.fiserv.smartmoney.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.li2.android.fiserv.smartmoney.R;
import me.li2.android.fiserv.smartmoney.model.AccountItem;
import me.li2.android.fiserv.smartmoney.widget.AccountItemViewHolder;
import me.li2.android.fiserv.smartmoney.widget.SimpleDividerItemDecoration;

/**
 * Created by weiyi on 01/07/2017.
 * https://github.com/li2
 */

public class BankingOperationFragment extends Fragment {
    private static final String TAG = "BankingOperation";
    private static final String ARG_KEY_ACCOUNT_ITEM = "ArgKeyAccountItem";
    private static final String ARG_KEY_ACCOUNT_NUMBER = "ArgKeyAccountNumber";
    private static final String ARG_KEY_RECYCLER_VIEW_HEIGHT = "arg_key_recycler_view_height";
    private static final int LAYOUT_COLUMNS_NUMBER = 3;
    private static final int LAYOUT_ROWS_NUMBER = 3;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({BANKING_OPERATION_PAY_MERCHANT, BANKING_OPERATION_PAY_BILL, BANKING_OPERATION_PAY_COMPANY,
            BANKING_OPERATION_PAY_PERSON, BANKING_OPERATION_REQUEST_MONEY, BANKING_OPERATION_DEPOSIT_CHECK,
            BANKING_OPERATION_INSIGHTS, BANKING_OPERATION_OFFERS, BANKING_OPERATION_WALLET})

    public @interface BankingOperation {
    }

    public static final int BANKING_OPERATION_PAY_MERCHANT = 0;
    public static final int BANKING_OPERATION_PAY_BILL = 1;
    public static final int BANKING_OPERATION_PAY_COMPANY = 2;
    public static final int BANKING_OPERATION_PAY_PERSON = 3;
    public static final int BANKING_OPERATION_REQUEST_MONEY = 4;
    public static final int BANKING_OPERATION_DEPOSIT_CHECK = 5;
    public static final int BANKING_OPERATION_INSIGHTS = 6;
    public static final int BANKING_OPERATION_OFFERS = 7;
    public static final int BANKING_OPERATION_WALLET = 8;

    @BindView(R.id.account_number_view) TextView mAccountNumberView;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.account_info_view) View mAccountInfoView;
    @OnClick(R.id.account_transfer_btn)
    public void transferAccount() {
        if (mOnBankingOperationSelectListener != null) {
            mOnBankingOperationSelectListener.onTransferAccount(mAvatorView);
        }
    }

    private AccountItemViewHolder mAccountItemViewHolder;
    private ImageView mAvatorView;
    private AccountItem mAccountItem;
    private int mAccountNumber;
    private BankingOperationAdapter mAdapter;
    private OnBankingOperationSelectListener mOnBankingOperationSelectListener;
    private int mRecyclerViewHeight;

    public BankingOperationFragment() {
    }

    public static BankingOperationFragment newInstance(AccountItem accountItem, int accountNumber) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_KEY_ACCOUNT_ITEM, accountItem);
        args.putInt(ARG_KEY_ACCOUNT_NUMBER, accountNumber);

        BankingOperationFragment fragment = new BankingOperationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public interface OnBankingOperationSelectListener {
        void onBankingOperationSelect(@BankingOperation int operation);
        void onRecyclerViewSetup();
        void onTransferAccount(View sharedElement);
    }

    public void setOnBankingOperationSelectListener(OnBankingOperationSelectListener l) {
        mOnBankingOperationSelectListener = l;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        Bundle args = getArguments();
        if (args != null) {
            mAccountItem = args.getParcelable(ARG_KEY_ACCOUNT_ITEM);
            mAccountNumber = args.getInt(ARG_KEY_ACCOUNT_NUMBER, 0);
        }

        if (savedInstanceState != null) {
            mRecyclerViewHeight = savedInstanceState.getInt(ARG_KEY_RECYCLER_VIEW_HEIGHT);
        } else {
            mRecyclerViewHeight = PreferenceManager.getDefaultSharedPreferences(getContext())
                    .getInt(ARG_KEY_RECYCLER_VIEW_HEIGHT, 0);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_KEY_RECYCLER_VIEW_HEIGHT, mRecyclerViewHeight);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_banking_operation, container, false);
        ButterKnife.bind(this, view);

        //ViewGroup parent = (ViewGroup) getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        //View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.widget_account_item_view, parent, false);
        mAccountItemViewHolder = new AccountItemViewHolder(mAccountInfoView, null, null);
        mAvatorView = (ImageView) view.findViewById(R.id.account_avator_view);

        final RecyclerView recyclerView = mRecyclerView;
        GridLayoutManager layoutManager = new GridLayoutManager(
                getActivity(), LAYOUT_COLUMNS_NUMBER, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setScrollContainer(false);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        // NOTE21: optimize recyclerview setup
        if (mRecyclerViewHeight > 0) {
            setupAdapter(recyclerView, mRecyclerViewHeight);
        } else {
            recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    mRecyclerViewHeight = recyclerView.getMeasuredHeight();
                    Log.d(TAG, "RecyclerView height is ready " + mRecyclerViewHeight);
                    setupAdapter(recyclerView, mRecyclerViewHeight);
                }
            });
        }
        
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateAccountView(mAccountNumber, mAccountItem);
    }

    @Override
    public void onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .edit()
                .putInt(ARG_KEY_RECYCLER_VIEW_HEIGHT, mRecyclerViewHeight)
                .apply();
        super.onDestroy();
    }

    public void updateAccountView(int accountNumber, AccountItem accountItem) {
        mAccountNumberView.setText("" + accountNumber);
        if (mAccountItemViewHolder != null) {
            mAccountItemViewHolder.bindAccountItem(accountItem, 0);
        }
    }

    //-------- BankingOperationItem -----------------------------------------------------

    public static class BankingOperationItem {
        public @BankingOperation int operation;
        public int iconId;
        public int titleId;
        public Drawable background;

        public BankingOperationItem(@BankingOperation int operation, int iconId, int titleId) {
            this.operation = operation;
            this.iconId = iconId;
            this.titleId = titleId;
        }
    }

    private ArrayList<BankingOperationItem> createOperationItems() {
        ArrayList<BankingOperationItem> items  = new ArrayList<>();
        items.add(new BankingOperationItem(BANKING_OPERATION_PAY_MERCHANT, R.drawable.i_banking_pay_merchant, R.string.banking_operation_pay_merchant));
        items.add(new BankingOperationItem(BANKING_OPERATION_PAY_BILL, R.drawable.i_banking_pay_bill, R.string.banking_operation_pay_bill));
        items.add(new BankingOperationItem(BANKING_OPERATION_PAY_COMPANY, R.drawable.i_banking_pay_company, R.string.banking_operation_pay_company));
        items.add(new BankingOperationItem(BANKING_OPERATION_PAY_PERSON, R.drawable.i_banking_pay_person, R.string.banking_operation_pay_person));
        items.add(new BankingOperationItem(BANKING_OPERATION_REQUEST_MONEY, R.drawable.i_banking_request_money, R.string.banking_operation_request_money));
        items.add(new BankingOperationItem(BANKING_OPERATION_DEPOSIT_CHECK, R.drawable.i_banking_deposit_check, R.string.banking_operation_deposit_check));
        items.add(new BankingOperationItem(BANKING_OPERATION_INSIGHTS, -1, R.string.banking_operation_insights));
        items.add(new BankingOperationItem(BANKING_OPERATION_OFFERS, -1, R.string.banking_operation_offers));
        items.add(new BankingOperationItem(BANKING_OPERATION_WALLET, -1, R.string.banking_operation_wallet));
        return items;
    }

    public void updateOperationItem(@BankingOperation int operation, Drawable background) {
        if (mAdapter != null) {
            mAdapter.updateItem(operation, background);
        }
    }

    //-------- BankingOperationAdapter --------------------------------------------------

    private void setupAdapter(RecyclerView recyclerView, int height) {
        mAdapter = new BankingOperationAdapter(getActivity(), createOperationItems(), height);
        recyclerView.setAdapter(mAdapter);
        if (mOnBankingOperationSelectListener != null) {
            mOnBankingOperationSelectListener.onRecyclerViewSetup();
        }
    }

    private class BankingOperationAdapter extends RecyclerView.Adapter<BankingOperationViewHolder> {
        private Context mContext;
        private List<BankingOperationItem> mItems;
        private int mContainerHeight;

        public BankingOperationAdapter(Context context, List<BankingOperationItem> items, int containerHeight) {
            mContext = context;
            mItems = items;
            mContainerHeight = containerHeight;
        }

        @Override
        public int getItemCount() {
            return (mItems != null) ? mItems.size() : 0;
        }

        @Override
        public BankingOperationViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.list_banking_operation_item, parent, false);
            // NOTE21: to fix issue that cannot set item with fixed height, because parent.getMeasuredHeight() always return 0.
            if (mContainerHeight != 0) {
                int itemWidth = ViewGroup.LayoutParams.MATCH_PARENT;
                int itemHeight = mContainerHeight / LAYOUT_ROWS_NUMBER;
                itemView.setLayoutParams(new GridLayoutManager.LayoutParams(itemWidth, itemHeight));
            }
            return new BankingOperationViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(BankingOperationViewHolder holder, int position) {
            holder.bindBankingOperationItem(mItems.get(position), position);
        }

        public void updateItem(@BankingOperation int operation, Drawable background) {
            for (int i = 0; i < getItemCount(); i++) {
                BankingOperationItem item = mItems.get(i);
                if (item.operation == operation) {
                    item.background = background;
                    notifyItemChanged(i);
                    break;
                }
            }
        }
    }

    //-------- BankingOperationViewHolder -----------------------------------------------

    public class BankingOperationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Context mContext;
        private BankingOperationItem mItem;
        @BindView(R.id.itemIcon) ImageView mIconView;
        @BindView(R.id.itemTitle) TextView mTitleView;
        private View mItemView;

        public BankingOperationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            mContext = itemView.getContext();
            mItemView = itemView;
        }

        public void bindBankingOperationItem(BankingOperationItem item, int position) {
            if (item != null) {
                mItem = item;

                if (item.iconId > 0) {
                    mIconView.setImageResource(item.iconId);
                }

                mTitleView.setText(item.titleId);

                if (item.operation == BANKING_OPERATION_INSIGHTS
                        || item.operation == BANKING_OPERATION_OFFERS
                        || item.operation == BANKING_OPERATION_WALLET) {
                    mTitleView.setTextColor(ContextCompat.getColor(mContext, android.R.color.white));
                }

                if (item.background != null) {
                    mItemView.setBackground(item.background);
                } else {
                    if (item.operation == BANKING_OPERATION_INSIGHTS) {
                        mItemView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.i_banking_insights));
                    } else if (item.operation == BANKING_OPERATION_OFFERS) {
                        mItemView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.i_banking_offers));
                    } else if (item.operation == BANKING_OPERATION_WALLET) {
                        mItemView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.i_banking_wallet));
                    }
                }
            }
        }

        @Override
        public void onClick(View v) {
            if (mOnBankingOperationSelectListener != null) {
                mOnBankingOperationSelectListener.onBankingOperationSelect(mItem.operation);
            }
        }
    }
}