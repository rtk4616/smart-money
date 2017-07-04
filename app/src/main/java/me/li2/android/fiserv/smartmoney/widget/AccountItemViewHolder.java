package me.li2.android.fiserv.smartmoney.widget;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import me.li2.android.fiserv.smartmoney.R;
import me.li2.android.fiserv.smartmoney.model.AccountItem;
import me.li2.android.fiserv.smartmoney.utils.ViewUtils;

/**
 * Created by weiyi on 02/07/2017.
 * https://github.com/li2
 */

public class AccountItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final String TAG = "AccountItemViewHolder";

    public interface OnAccountSelectListener {
        void onAccountSelect(AccountItem accountItem, View sharedElement);
    }

    // NOTE21-transition: because ViewHolder cannot call startPostponedEnterTransition(),
    // so define this callback to notify fragment to call startPostponedEnterTransition() after image loaded
    // so that the Fragment actually loads transition animation.
    public interface OnSharedElementLoadListener {
        void onSharedElementLoad();
    }

    private Context mContext;
    private AccountItem mItem;
    private OnAccountSelectListener mOnAccountSelectListener;
    private OnSharedElementLoadListener mOnSharedElementLoadListener;

    @BindView(R.id.account_avator_view) ImageView mAvatorView;
    @BindView(R.id.account_name_view) TextView mNameView;
    @BindView(R.id.account_id_view) TextView mIdView;
    @BindView(R.id.account_balance_view) TextView mBalanceView;

    public AccountItemViewHolder(View itemView, OnAccountSelectListener l, OnSharedElementLoadListener l2) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
        mContext = itemView.getContext();
        mOnAccountSelectListener = l;
        mOnSharedElementLoadListener = l2;
    }

    public void bindAccountItem(AccountItem item, int position) {
        if (item == null) {
            return;
        }
        mItem = item;
        mNameView.setText(item.name);
        mIdView.setText("" + item.id);
        mBalanceView.setText(String.format(
                mContext.getString(R.string.account_balance_format), item.balance));

        /*
         NOTE21-transition: make sure the android:transitionName between shared elements must be the same,
         and also has to be unique in the view hierarchy,
         As for RecyclerView to Fragment,
         If we set the transitionName in XML then all ImageView in the gallery would have the same one,
         which means when we came back to the gallery the framework would have no idea where to move the image to.
         Since the item position is unique, so it's best to use position here to set transitionName.

         http://mikescamell.com/shared-element-transitions-part-4-recyclerview/
         https://stackoverflow.com/a/31072112/2722270
         https://github.com/lgvalle/Material-Animations#shared-elements-between-fragments
         */
        String transitionName = ViewUtils.transitionName(mContext, position);
        ViewCompat.setTransitionName(mAvatorView, transitionName);

        // TODO Transformations cannot apply on placeholder and error images, and make sure ImageView width == height
        Picasso.with(mContext)
                .load(item.avatarUrl)
                .transform(new CropCircleTransformation())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(mAvatorView, new Callback() {
                    @Override
                    public void onSuccess() {
                        notifyOnSharedElementLoad();
                    }
                    @Override
                    public void onError() {
                        notifyOnSharedElementLoad();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (mOnAccountSelectListener != null) {
            mOnAccountSelectListener.onAccountSelect(mItem, mAvatorView);
        }
    }

    private void notifyOnSharedElementLoad() {
        if (mOnSharedElementLoadListener != null) {
            mOnSharedElementLoadListener.onSharedElementLoad();
        }
    }
}
