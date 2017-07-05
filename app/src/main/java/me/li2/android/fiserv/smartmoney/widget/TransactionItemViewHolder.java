package me.li2.android.fiserv.smartmoney.widget;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.annotation.SwipeableItemResults;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractSwipeableItemViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.li2.android.fiserv.smartmoney.R;
import me.li2.android.fiserv.smartmoney.model.TransactionItem;
import me.li2.android.fiserv.smartmoney.utils.ViewUtils;

/**
 * Created by weiyi on 04/07/2017.
 * https://github.com/li2
 */

public class TransactionItemViewHolder extends AbstractSwipeableItemViewHolder {
    private static final String TAG = "TransactionItemView";

    public interface TransactionEventListener {
        void connect(final TransactionItem item);
        void details(final TransactionItem item);
    }

    // NOTE: Make accessible with short name
    private interface Swipeable extends SwipeableItemConstants {
    }

    private Context mContext;
    private TransactionItem mTransactionItem;
    private TransactionEventListener mEventListener;

    @BindView(R.id.transaction_item_container_view)
    ViewGroup mForegroundContainer;
    @BindView(R.id.transaction_date_view)
    TextView mDateView;
    @BindView(R.id.transaction_type_view)
    TextView mTypeView;
    @BindView(R.id.transaction_source_view)
    TextView mSourceView;
    @BindView(R.id.transaction_amount_view)
    TextView mAmountView;
    @BindView(R.id.transaction_connect_btn)
    Button mConnectButton;
    @BindView(R.id.transaction_detail_btn)
    Button mDetailsButton;
    @BindView(R.id.transaction_operation_panel_view)
    View mOperationPanelView;

    @OnClick(R.id.transaction_item_container_view)
    public void onItemClick() {
        // do nothing
    }

    @OnClick(R.id.transaction_connect_btn)
    public void onConnectBtnClick() {
        if (mEventListener != null) {
            mEventListener.connect(mTransactionItem);
        }
    }

    @OnClick(R.id.transaction_detail_btn)
    public void onDetailsBtnClick() {
        if (mEventListener != null) {
            mEventListener.details(mTransactionItem);
        }
    }

    public TransactionItemViewHolder(View itemView, TransactionEventListener l) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();
        mEventListener = l;
    }

    public void bindTransactionItem(TransactionItem item) {
        mTransactionItem = item;

        mDateView.setText(ViewUtils.dateToString(item.date));
        mTypeView.setText(item.type);
        mSourceView.setText(item.where);
        mAmountView.setText(ViewUtils.moneyAmountFormat(mContext, item.amount));

        // set background resource (target view ID: container)
        final int swipeState = getSwipeStateFlags();

        if ((swipeState & Swipeable.STATE_FLAG_IS_UPDATED) != 0) {
            int bgResId = 0;

            if ((swipeState & Swipeable.STATE_FLAG_IS_ACTIVE) != 0) {
                bgResId = R.drawable.bg_item_swiping_active_state;
            } else if ((swipeState & Swipeable.STATE_FLAG_SWIPING) != 0) {
                bgResId = R.drawable.bg_item_swiping_state;
            } else {
                bgResId = R.drawable.bg_item_normal_state;
            }

            mForegroundContainer.setBackgroundResource(bgResId);
        }

        // set swiping properties
        setMaxLeftSwipeAmount(-0.5f);
        setMaxRightSwipeAmount(0);
        setSwipeItemHorizontalSlideAmount(item.isPinned ? -0.5f : 0);
    }

    @Override
    public View getSwipeableContainerView() {
        return mForegroundContainer;
    }

    @Override
    public void onSlideAmountUpdated(float horizontalAmount, float verticalAmount, boolean isSwiping) {
        super.onSlideAmountUpdated(horizontalAmount, verticalAmount, isSwiping);
        @SwipeableItemResults int result = getSwipeResult();

        Log.d(TAG, "H amount " + horizontalAmount + ", " + isSwiping + ", result " + result);

        // 往左滑动，负数增大 -0.004 到 -0.99， 到 0。透明度增大
        // 往右滑动，负数减小，到0
        // 因此可以用它的绝对值当做 alpha

        if (horizontalAmount <= 0) {
            float alpha = Math.abs(horizontalAmount);
            mOperationPanelView.setAlpha(alpha);
        }

        if (!isSwiping) {
            if (result == Swipeable.RESULT_SWIPED_LEFT) {
                mOperationPanelView.setAlpha(1);
            } else if (result == Swipeable.RESULT_SWIPED_RIGHT || result == Swipeable.RESULT_CANCELED) {
                mOperationPanelView.setAlpha(0);
            }
        }
    }
}
