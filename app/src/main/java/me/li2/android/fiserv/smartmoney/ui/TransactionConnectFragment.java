package me.li2.android.fiserv.smartmoney.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.li2.android.fiserv.smartmoney.R;
import me.li2.android.fiserv.smartmoney.model.AccountItem;
import me.li2.android.fiserv.smartmoney.model.TransactionItem;
import me.li2.android.fiserv.smartmoney.utils.ViewUtils;

/**
 * Created by weiyi on 04/07/2017.
 * https://github.com/li2
 */

public class TransactionConnectFragment extends Fragment {
    public static final String ARG_KEY_ACCOUNT = "arg_key_account";
    public static final String ARG_KEY_TRANSACTION = "arg_key_transaction";

    public interface ConnectEventListener {
        void call();
        void chat();
        void message();
    }

    public static TransactionConnectFragment newInstance(Bundle args) {
        TransactionConnectFragment fragment = new TransactionConnectFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.account_name_view)
    TextView mAccountNameView;

    @BindView(R.id.account_id_view)
    TextView mAccountIdView;

    @BindView(R.id.transaction_date_view)
    TextView mTransactionDateView;

    @BindView(R.id.transaction_type_view)
    TextView mTransactionTypeView;

    @BindView(R.id.transaction_amount_view)
    TextView mTransactionAmountView;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @OnClick(R.id.close_btn)
    void close() {
        if (getActivity() != null && !getActivity().isFinishing()) {
            getActivity().finish();
        }
    }

    private RecyclerViewExpandableItemManager mExpMgr;
    private AccountItem mAccountItem;
    private TransactionItem mTransactionItem;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mAccountItem = args.getParcelable(ARG_KEY_ACCOUNT);
            mTransactionItem = args.getParcelable(ARG_KEY_TRANSACTION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction_connect, container, false);
        ButterKnife.bind(this, view);

        RecyclerView recyclerView = mRecyclerView;

        // Setup expandable feature and RecyclerView
        RecyclerViewExpandableItemManager expMgr = new RecyclerViewExpandableItemManager(null);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(expMgr.createWrappedAdapter(new MyAdapter(getContext())));

        // NOTE: need to disable change animations to ripple effect work properly
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        expMgr.attachRecyclerView(recyclerView);
        mExpMgr = expMgr;

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateHeaderView(mAccountItem, mTransactionItem);
    }

    private void updateHeaderView(final AccountItem accountItem, final TransactionItem transactionItem) {
        mAccountNameView.setText(accountItem.name);
        mAccountIdView.setText("" + accountItem.id);
        mTransactionDateView.setText(ViewUtils.dateToString(transactionItem.date));
        mTransactionTypeView.setText(transactionItem.type);
        mTransactionAmountView.setText(ViewUtils.moneyAmountFormat(getContext(), transactionItem.amount));
    }

    private ConnectEventListener mEventListener = new ConnectEventListener() {
        @Override
        public void call() {
            Toast.makeText(getContext(), "on Call click", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void chat() {
            Toast.makeText(getContext(), "on Chat click", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void message() {
            Toast.makeText(getContext(), "on Message click", Toast.LENGTH_SHORT).show();
        }
    };


    private class ConnectWayItem {
        public long id;
        public int avatorImageResId;
        public String title;
        public String detail;
        public int detailIconResId;
        public int actionImageResId;
        public List<ReportWayItem> children = new ArrayList<>();

        public ConnectWayItem(int id, int avatorImageResId, String title, String detail, int detailIconResId,
                              int actionImageResId, List<ReportWayItem> children) {
            this.id = id;
            this.avatorImageResId = avatorImageResId;
            this.title = title;
            this.detail = detail;
            this.detailIconResId = detailIconResId;
            this.actionImageResId = actionImageResId;
            if (children != null) {
                this.children = children;
            }
        }
    }

    private class ReportWayItem {
        public long id;
        public String reportType;

        public ReportWayItem(int id, String reportType) {
            this.id = id;
            this.reportType = reportType;
        }
    }

    public class ConnectWayViewHolder extends AbstractExpandableItemViewHolder {

        @BindView(R.id.connect_way_avator)
        ImageView mAvatorView;
        @BindView(R.id.connect_way_title_view)
        TextView mTitleView;
        @BindView(R.id.connect_way_detail_view)
        TextView mDetailView;
        @BindView(R.id.connect_way_detail_icon)
        ImageView mDetailIcon;
        @BindView(R.id.connect_action_btn)
        Button mActionBtn;

        @OnClick(R.id.connect_action_btn)
        public void onActionClick() {
            if (mEventListener != null) {
                int position = getAdapterPosition();
                if (position == 0) {
                    mEventListener.call();
                } else if (position == 1) {
                    mEventListener.chat();
                } else if (position == 2) {
                    mEventListener.message();
                }
            }
        }

        private Context mContext;

        public ConnectWayViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
        }

        public void bindConnectWayItem(ConnectWayItem item) {
            Picasso.with(mContext)
                    .load(item.avatorImageResId)
                    .into(mAvatorView);
            mTitleView.setText(item.title);
            mDetailView.setText(item.detail);
            mDetailIcon.setImageResource(item.detailIconResId);
            mActionBtn.setBackground(ContextCompat.getDrawable(mContext, item.actionImageResId));
        }
    }

    public class ReportWayViewHolder extends AbstractExpandableItemViewHolder {
        @BindView(R.id.report_textView)
        TextView mTextView;

        @OnClick(R.id.report_item_view)
        public void onReportClick() {
            Toast.makeText(getContext(), "On " + mTextView.getText() + " click", Toast.LENGTH_SHORT).show();
        }

        public ReportWayViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindReportWayItem(ReportWayItem item) {
            mTextView.setText(item.reportType);
        }
    }

    private class MyAdapter extends AbstractExpandableItemAdapter<ConnectWayViewHolder, ReportWayViewHolder> {
        List<ConnectWayItem> mItems = new ArrayList<>();

        public MyAdapter(Context context) {
            setHasStableIds(true); // this is required for expandable feature.

            List<ReportWayItem> children = new ArrayList<>();
            children.add(new ReportWayItem(0, context.getString(R.string.report_fraudulent_transaction)));
            children.add(new ReportWayItem(1, context.getString(R.string.report_duplicate_transaction)));

            ConnectWayItem group1 = new ConnectWayItem(0, R.drawable.i_connect_avator2,
                    getString(R.string.connect_way_call_title),
                    "2 min",
                    R.drawable.i_clock,
                    R.drawable.i_connect_call,
                    null);

            ConnectWayItem group2 = new ConnectWayItem(1, R.drawable.i_connect_avator3,
                    getString(R.string.connect_way_chat_title),
                    getString(R.string.connect_way_chat_detail),
                    R.drawable.i_clock,
                    R.drawable.i_connect_chat,
                    children);

            ConnectWayItem group3 = new ConnectWayItem(2, R.drawable.i_connect_avator4,
                    getString(R.string.connect_way_message_title),
                    getString(R.string.connect_way_message_detail),
                    R.drawable.i_pencil,
                    R.drawable.i_connect_message,
                    children);

            mItems.add(group1);
            mItems.add(group2);
            mItems.add(group3);
        }

        @Override
        public int getGroupCount() {
            return mItems.size();
        }

        @Override
        public int getChildCount(int groupPosition) {
            return mItems.get(groupPosition).children.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            // This method need to return unique value within all group items.
            return mItems.get(groupPosition).id;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            // This method need to return unique value within the group.
            return mItems.get(groupPosition).children.get(childPosition).id;
        }

        @Override
        public ConnectWayViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_connect, parent, false);
            return new ConnectWayViewHolder(v);
        }

        @Override
        public ReportWayViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_report, parent, false);
            return new ReportWayViewHolder(v);
        }

        @Override
        public void onBindGroupViewHolder(ConnectWayViewHolder holder, int groupPosition, int viewType) {
            ConnectWayItem group = mItems.get(groupPosition);
            holder.bindConnectWayItem(group);
        }

        @Override
        public void onBindChildViewHolder(ReportWayViewHolder holder, int groupPosition, int childPosition, int viewType) {
            ReportWayItem child = mItems.get(groupPosition).children.get(childPosition);
            holder.bindReportWayItem(child);
        }

        @Override
        public boolean onCheckCanExpandOrCollapseGroup(ConnectWayViewHolder holder, int groupPosition, int x, int y, boolean expand) {
            return true;
        }

        @Override
        public boolean onHookGroupExpand(int groupPosition, boolean fromUser) {
            // NOTE21: collapse all other groups when one item expand.
            mExpMgr.collapseAll();

            // NOTE21: Visibility of expanding last view in the expandable recyclerview
            if (groupPosition == getGroupCount() - 1) {
                // not work with https://github.com/h6ah4i/android-advancedrecyclerview/issues/60
                //mExpMgr.scrollToGroup(groupPosition, 64);
                mRecyclerView.smoothScrollToPosition(groupPosition + getChildCount(groupPosition));
            }
            return true;
        }
    }
}
