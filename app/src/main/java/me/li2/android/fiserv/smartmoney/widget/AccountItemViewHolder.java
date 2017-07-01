package me.li2.android.fiserv.smartmoney.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import me.li2.android.fiserv.smartmoney.R;
import me.li2.android.fiserv.smartmoney.model.AccountItem;

/**
 * Created by weiyi on 02/07/2017.
 * https://github.com/li2
 */

public class AccountItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public interface OnAccountSelectListener {
        void onAccountSelect(AccountItem accountItem);
    }

    public void setOnAccountSelectListener(OnAccountSelectListener l) {
        mOnAccountSelectListener = l;
    }

    private Context mContext;
    private AccountItem mItem;
    private OnAccountSelectListener mOnAccountSelectListener;

    @BindView(R.id.account_avator_view) ImageView mAvatorView;
    @BindView(R.id.account_name_view) TextView mNameView;
    @BindView(R.id.account_id_view) TextView mIdView;
    @BindView(R.id.account_balance_view) TextView mBalanceView;

    public AccountItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
        mContext = itemView.getContext();
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

        Picasso.with(mContext)
                .load(item.avatarUrl)
                .transform(new CropCircleTransformation())
                .placeholder(R.drawable.i_banking_insights) // TODO Transformations cannot apply on placeholder and error images, and make sure ImageView width == height
                .error(R.drawable.i_banking_insights)
                .into(mAvatorView);
    }

    @Override
    public void onClick(View v) {
        if (mOnAccountSelectListener != null) {
            mOnAccountSelectListener.onAccountSelect(mItem);
        }
    }
}
