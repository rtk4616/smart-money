package me.li2.android.fiserv.smartmoney.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import me.li2.android.fiserv.smartmoney.R;

/**
 * Created by weiyi on 01/07/2017.
 * https://github.com/li2
 */

public class BankingOperationFragment extends Fragment {
    private static final String TAG = "BankingOperationFragment";

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

    private RecyclerView mRecyclerView;
    private BankingOperationAdapter mAdapter;
    private List<BankingOperationItem> mBankingOperationItems;
    private OnBankingOperationSelectListener mOnBankingOperationSelectListener;

    public BankingOperationFragment() {
    }

    public interface OnBankingOperationSelectListener {
        void onBankingOperationSelect(@BankingOperation int operation);
    }

    public void setData(List<BankingOperationItem> items) {
        mBankingOperationItems = items;
    }

    public void setOnBankingOperationSelectListener(OnBankingOperationSelectListener l) {
        mOnBankingOperationSelectListener = l;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mAdapter = new BankingOperationAdapter(getActivity(), createItems());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_banking_operation, container, false);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setScrollContainer(false);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.addItemDecoration(new SimpleListDividerDecorator(
                ContextCompat.getDrawable(getContext(), android.R.drawable.divider_horizontal_dim_dark), true));
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    private ArrayList<BankingOperationItem> createItems() {
        ArrayList<BankingOperationItem> items  = new ArrayList<>();
        items.add(new BankingOperationItem(BANKING_OPERATION_PAY_COMPANY, R.drawable.i_banking_pay_company, R.string.banking_operation_pay_company));
        items.add(new BankingOperationItem(BANKING_OPERATION_PAY_BILL, R.drawable.i_banking_pay_bill, R.string.banking_operation_pay_bill));
        items.add(new BankingOperationItem(BANKING_OPERATION_PAY_MERCHANT, R.drawable.i_banking_pay_merchant, R.string.banking_operation_pay_merchant));
        items.add(new BankingOperationItem(BANKING_OPERATION_DEPOSIT_CHECK, R.drawable.i_banking_deposit_check, R.string.banking_operation_deposit_check));
        items.add(new BankingOperationItem(BANKING_OPERATION_REQUEST_MONEY, R.drawable.i_banking_request_money, R.string.banking_operation_request_money));
        items.add(new BankingOperationItem(BANKING_OPERATION_PAY_PERSON, R.drawable.i_banking_pay_person, R.string.banking_operation_pay_person));
        items.add(new BankingOperationItem(BANKING_OPERATION_WALLET, -1, R.string.banking_operation_wallet));
        items.add(new BankingOperationItem(BANKING_OPERATION_OFFERS, -1, R.string.banking_operation_offers));
        items.add(new BankingOperationItem(BANKING_OPERATION_INSIGHTS, -1, R.string.banking_operation_insights));
        return items;
    }

    public void updateItem(@BankingOperation int operation, Drawable background) {
        if (mAdapter != null) {
            mAdapter.updateItem(operation, background);
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

    //-------- BankingOperationAdapter --------------------------------------------------

    private class BankingOperationAdapter extends RecyclerView.Adapter<BankingOperationViewHolder> {
        private Context mContext;
        private List<BankingOperationItem> mItems;

        public BankingOperationAdapter(Context context, List<BankingOperationItem> items) {
            mContext = context;
            mItems = items;
        }

        @Override
        public int getItemCount() {
            return (mItems != null) ? mItems.size() : 0;
        }

        @Override
        public BankingOperationViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.list_banking_operation_item, parent, false);
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

    private class BankingOperationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private BankingOperationItem mItem;
        private View mItemView;
        private ImageView mIconView;
        private TextView mTitleView;

        public BankingOperationViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mIconView = (ImageView) itemView.findViewById(R.id.itemIcon);
            mTitleView = (TextView) itemView.findViewById(R.id.itemTitle);
            itemView.setOnClickListener(this);
        }

        public void bindBankingOperationItem(BankingOperationItem item, int position) {
            if (item != null) {
                mItem = item;
                if (item.background != null) {
                    mItemView.setBackground(item.background);
                }

                if (item.iconId > 0) {
                    mIconView.setImageResource(item.iconId);
                }

                mTitleView.setText(item.titleId);

                if (item.operation == BANKING_OPERATION_INSIGHTS
                        || item.operation == BANKING_OPERATION_OFFERS
                        || item.operation == BANKING_OPERATION_WALLET) {
                    mTitleView.setTextColor(ContextCompat.getColor(mItemView.getContext(), android.R.color.white));
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