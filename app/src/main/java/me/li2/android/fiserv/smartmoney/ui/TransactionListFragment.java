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

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.SwipeDismissItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.li2.android.fiserv.smartmoney.R;
import me.li2.android.fiserv.smartmoney.model.AccountItem;
import me.li2.android.fiserv.smartmoney.model.TransactionItem;
import me.li2.android.fiserv.smartmoney.model.Transactions;
import me.li2.android.fiserv.smartmoney.utils.ViewUtils;
import me.li2.android.fiserv.smartmoney.webservice.FiservService;
import me.li2.android.fiserv.smartmoney.webservice.ServiceGenerator;
import me.li2.android.fiserv.smartmoney.widget.TransactionItemViewHolder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by weiyi on 03/07/2017.
 *
 * Copy the source code [Button under swipeable item] from:
 * android-advancedrecyclerview/example/src/main/java/com/h6ah4i/android/example/advrecyclerview/demo_s_button
 */

public class TransactionListFragment extends Fragment {
    private static final String TAG = "TransactionListFragment";
    private static final String ARG_KEY_ACCOUNT_ITEM = "ArgKeyAccountItem";

    private AccountItem mAccountItem;
    private RecyclerView.LayoutManager mLayoutManager;
    private TransactionListAdapter mTransactionListAdapter;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter mWrappedAdapter;
    private RecyclerViewSwipeManager mRecyclerViewSwipeManager;
    private RecyclerViewTouchActionGuardManager mRecyclerViewTouchActionGuardManager;

    // view widget
    @BindView(R.id.account_avator_view) ImageView mAccountAvatorView;
    @BindView(R.id.account_id_view) TextView mAccountIdView;
    @BindView(R.id.account_balance_view) TextView mAccountBalanceView;
    @BindView(R.id.account_name_view) TextView mAccountNameView;
    @BindView(R.id.account_available_credit_view) TextView mAccountAvailableCreditView;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;

    public TransactionListFragment() {
        super();
    }

    public static TransactionListFragment newInstance(AccountItem accountItem) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_KEY_ACCOUNT_ITEM, accountItem);

        TransactionListFragment fragment = new TransactionListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // NOTE21-transition: have to tell the Fragment to wait to load until we tell it
        postponeEnterTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // NOTE21-transition: to tell it what type of Transition we want.
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }

        Bundle args = getArguments();
        if (args != null) {
            mAccountItem = args.getParcelable(ARG_KEY_ACCOUNT_ITEM);
        }
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction_list, container, false);
        ButterKnife.bind(this, view);
        if (mAccountItem != null) {
            // NOTE21-transition: set transitionName on imageView when it's inflated
            String transitionName = ViewUtils.transitionName(getContext(), mAccountItem.position);
            ViewCompat.setTransitionName(mAccountAvatorView, transitionName);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //noinspection ConstantConditions
        mLayoutManager = new LinearLayoutManager(getContext());

        // touch guard manager  (this class is required to suppress scrolling while swipe-dismiss animation is running)
        mRecyclerViewTouchActionGuardManager = new RecyclerViewTouchActionGuardManager();
        mRecyclerViewTouchActionGuardManager.setInterceptVerticalScrollingWhileAnimationRunning(true);
        mRecyclerViewTouchActionGuardManager.setEnabled(true);

        // swipe manager
        mRecyclerViewSwipeManager = new RecyclerViewSwipeManager();

        //adapter
        final TransactionListAdapter myItemAdapter = new TransactionListAdapter(mEventListener);

        mTransactionListAdapter = myItemAdapter;
        mAdapter = myItemAdapter;
        mWrappedAdapter = mRecyclerViewSwipeManager.createWrappedAdapter(myItemAdapter);      // wrap for swiping

        final GeneralItemAnimator animator = new SwipeDismissItemAnimator();

        // Change animations are enabled by default since support-v7-recyclerview v22.
        // Disable the change animation in order to make turning back animation of swiped item works properly.
        animator.setSupportsChangeAnimations(false);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mWrappedAdapter);  // requires *wrapped* adapter
        mRecyclerView.setItemAnimator(animator);

        // additional decorations
        //noinspection StatementWithEmptyBody
        if (ViewUtils.supportsViewElevation()) {
            // Lollipop or later has native drop shadow feature. ItemShadowDecorator is not required.
        } else {
            mRecyclerView.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable) ContextCompat.getDrawable(getContext(), R.drawable.line_divider)));
        }
        mRecyclerView.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(getContext(), R.drawable.line_divider), true));

        // NOTE:
        // The initialization order is very important! This order determines the priority of touch event handling.
        //
        // priority: TouchActionGuard > Swipe > DragAndDrop
        mRecyclerViewTouchActionGuardManager.attachRecyclerView(mRecyclerView);
        mRecyclerViewSwipeManager.attachRecyclerView(mRecyclerView);

        updateView();
    }

    @Override
    public void onDestroyView() {
        if (mRecyclerViewSwipeManager != null) {
            mRecyclerViewSwipeManager.release();
            mRecyclerViewSwipeManager = null;
        }

        if (mRecyclerViewTouchActionGuardManager != null) {
            mRecyclerViewTouchActionGuardManager.release();
            mRecyclerViewTouchActionGuardManager = null;
        }

        if (mRecyclerView != null) {
            mRecyclerView.setItemAnimator(null);
            mRecyclerView.setAdapter(null);
            mRecyclerView = null;
        }

        if (mWrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(mWrappedAdapter);
            mWrappedAdapter = null;
        }
        mAdapter = null;
        mTransactionListAdapter = null;
        mLayoutManager = null;

        super.onDestroyView();
    }

    private TransactionItemViewHolder.TransactionEventListener mEventListener = new TransactionItemViewHolder.TransactionEventListener() {
        @Override
        public void connect(final TransactionItem transactionItem, final View sharedElement) {
            Intent intent = TransactionConnectActivity.newIntent(getContext(), mAccountItem, transactionItem);

            // Start an activity with a shared element
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions transitionOptions = ActivityOptions.makeSceneTransitionAnimation(
                        getActivity(), sharedElement, ViewCompat.getTransitionName(sharedElement));
                startActivity(intent, transitionOptions.toBundle());
            } else {
                startActivity(intent);
            }
        }

        @Override
        public void details(final TransactionItem item, final View sharedElement) {

        }
    };

    private void updateView() {
        updateAccountItemView(mAccountItem);
        loadTransactions();
    }

    private void updateAccountItemView(AccountItem accountItem) {
        Picasso.with(getContext())
                .load(mAccountItem.avatarUrl)
                .into(mAccountAvatorView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        // NOTE21-transition: make sure we call startPostponedEnterTransition() in the image load callback
                        // so that the Fragment actually loads.
                        startPostponedEnterTransition();
                    }

                    @Override
                    public void onError() {
                        startPostponedEnterTransition();
                    }
                });

        mAccountIdView.setText("" + accountItem.id);
        mAccountBalanceView.setText(ViewUtils.moneyAmountFormat(getContext(), accountItem.balance));
        mAccountAvailableCreditView.setText(ViewUtils.moneyAmountFormat(getContext(), accountItem.availableCredit));
    }

    private Dialog mLoadingDialog;

    private void showLoadingDialog() {
        mLoadingDialog = ViewUtils.showLoadingDialog(new WeakReference<Activity>(getActivity()),
                getString(R.string.loading_transactions));
    }

    private void hideLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    private void loadTransactions() {
        showLoadingDialog();
        FiservService service = ServiceGenerator.createService(FiservService.class);
        service.getTransactions().enqueue(new Callback<Transactions>() {
            @Override
            public void onResponse(Call<Transactions> call, Response<Transactions> response) {
                ArrayList<TransactionItem> items = response.body().transactionItems;
                Iterator<TransactionItem> iterator = items.iterator();
                long id = 0;
                while (iterator.hasNext()) {
                    TransactionItem item = iterator.next();
                    item.id = id++; // NOTE21 swipeable feature needs unique id.
                }
                mTransactionListAdapter.updateTransactionItems(items);
                hideLoadingDialog();
            }

            @Override
            public void onFailure(Call<Transactions> call, Throwable t) {
                hideLoadingDialog();
                showLoadingFailedDialog();
            }
        });
    }

    private void showLoadingFailedDialog() {
        new AlertDialog.Builder(getContext())
                .setMessage(R.string.loading_transactions_failed)
                .setPositiveButton(R.string.try_again, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadTransactions();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .setCancelable(false)
                .show();
    }
}
