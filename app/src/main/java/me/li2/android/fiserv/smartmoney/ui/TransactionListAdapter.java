/*
 *    Copyright (C) 2015 Haruki Hasegawa
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package me.li2.android.fiserv.smartmoney.ui;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionDefault;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionMoveToSwipedDirection;

import java.util.ArrayList;

import me.li2.android.fiserv.smartmoney.R;
import me.li2.android.fiserv.smartmoney.model.TransactionItem;
import me.li2.android.fiserv.smartmoney.utils.ViewUtils;
import me.li2.android.fiserv.smartmoney.widget.TransactionItemViewHolder;

/**
 * Created by weiyi on 03/07/2017.
 *
 * Copy the source code [Button under swipeable item] from:
 * android-advancedrecyclerview/example/src/main/java/com/h6ah4i/android/example/advrecyclerview/demo_s_button
 */

public class TransactionListAdapter
        extends RecyclerView.Adapter<TransactionItemViewHolder>
        implements SwipeableItemAdapter<TransactionItemViewHolder> {
    private static final String TAG = "MySwipeableItemAdapter";

    // NOTE: Make accessible with short name
    private interface Swipeable extends SwipeableItemConstants {
    }

    private ArrayList<TransactionItem> mTransactionItems;
    private TransactionItemViewHolder.TransactionEventListener mEventListener;

    public TransactionListAdapter(TransactionItemViewHolder.TransactionEventListener l) {
        // SwipeableItemAdapter requires stable ID, and also
        // have to implement the getItemId() method appropriately.
        setHasStableIds(true);
        mEventListener = l;
    }

    public void updateTransactionItems(ArrayList<TransactionItem> items) {
        mTransactionItems = items;
        notifyDataSetChanged();
    }

    public TransactionItem getItem(int position) {
        if (position < getItemCount()) {
            return mTransactionItems.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id;
    }

    @Override
    public int getItemCount() {
        return mTransactionItems != null ? mTransactionItems.size() : 0;
    }

    @Override
    public TransactionItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.list_transaction_item, parent, false);
        return new TransactionItemViewHolder(v, mEventListener);
    }

    @Override
    public void onBindViewHolder(TransactionItemViewHolder holder, int position) {
        holder.bindTransactionItem(mTransactionItems.get(position));
    }

    @Override
    public int onGetSwipeReactionType(TransactionItemViewHolder holder, int position, int x, int y) {
        if (ViewUtils.hitTest(holder.getSwipeableContainerView(), x, y)) {
            return Swipeable.REACTION_CAN_SWIPE_BOTH_H;
        } else {
            return Swipeable.REACTION_CAN_NOT_SWIPE_BOTH_H;
        }
    }

    @Override
    public void onSetSwipeBackground(TransactionItemViewHolder holder, int position, int type) {
        int bgRes = android.R.color.transparent;
        switch (type) {
            case Swipeable.DRAWABLE_SWIPE_NEUTRAL_BACKGROUND:
                break;
            case Swipeable.DRAWABLE_SWIPE_LEFT_BACKGROUND:
                break;
            case Swipeable.DRAWABLE_SWIPE_RIGHT_BACKGROUND:
                break;
        }

        holder.itemView.setBackgroundResource(bgRes);
    }

    @Override
    public SwipeResultAction onSwipeItem(TransactionItemViewHolder holder, int position, int result) {
        Log.d(TAG, "onSwipeItem(position = " + position + ", result = " + result + ")");

        switch (result) {
            // swipe left --- pin
            case Swipeable.RESULT_SWIPED_LEFT:
                return new SwipeLeftResultAction(this, position);
            // other --- do nothing
            case Swipeable.RESULT_SWIPED_RIGHT:
            case Swipeable.RESULT_CANCELED:
            default:
                if (position != RecyclerView.NO_POSITION) {
                    return new UnpinResultAction(this, position);
                } else {
                    return null;
                }
        }
    }

    private static class SwipeLeftResultAction extends SwipeResultActionMoveToSwipedDirection {
        private TransactionListAdapter mAdapter;
        private final int mPosition;
        private boolean mSetPinned;

        SwipeLeftResultAction(TransactionListAdapter adapter, int position) {
            mAdapter = adapter;
            mPosition = position;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();

            TransactionItem item = mAdapter.getItem(mPosition);

            if (item != null && !item.isPinned) {
                item.isPinned = true;
                mAdapter.notifyItemChanged(mPosition);
                mSetPinned = true;
            }
        }

        @Override
        protected void onSlideAnimationEnd() {
            super.onSlideAnimationEnd();
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            // clear the references
            mAdapter = null;
        }
    }

    private static class UnpinResultAction extends SwipeResultActionDefault {
        private TransactionListAdapter mAdapter;
        private final int mPosition;

        UnpinResultAction(TransactionListAdapter adapter, int position) {
            mAdapter = adapter;
            mPosition = position;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();

            TransactionItem item = mAdapter.getItem(mPosition);
            if (item.isPinned) {
                item.isPinned = false;
                mAdapter.notifyItemChanged(mPosition);
            }
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            // clear the references
            mAdapter = null;
        }
    }
}
