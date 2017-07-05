package me.li2.android.fiserv.smartmoney.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.li2.android.fiserv.smartmoney.R;

/**
 * Created by weiyi on 04/07/2017.
 * https://github.com/li2
 */

public class TransactionConnectFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @OnClick(R.id.close_btn)
    public void close() {
        if (getActivity() != null && !getActivity().isFinishing()) {
            getActivity().finish();
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
        recyclerView.setAdapter(expMgr.createWrappedAdapter(new MyAdapter()));

        // NOTE: need to disable change animations to ripple effect work properly
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        expMgr.attachRecyclerView(recyclerView);


        return view;
    }

    static abstract class MyBaseItem {
        public final long id;
        public final String text;

        public MyBaseItem(long id, String text) {
            this.id = id;
            this.text = text;
        }
    }

    static class MyGroupItem extends MyBaseItem {
        public final List<MyChildItem> children;

        public MyGroupItem(long id, String text) {
            super(id, text);
            children = new ArrayList<>();
        }
    }

    static class MyChildItem extends MyBaseItem {
        public MyChildItem(long id, String text) {
            super(id, text);
        }
    }

    static abstract class MyBaseViewHolder extends AbstractExpandableItemViewHolder {
        TextView textView;

        public MyBaseViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }

    static class MyGroupViewHolder extends MyBaseViewHolder {
        public MyGroupViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class MyChildViewHolder extends MyBaseViewHolder {
        public MyChildViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class MyAdapter extends AbstractExpandableItemAdapter<MyGroupViewHolder, MyChildViewHolder> {
        List<MyGroupItem> mItems;

        public MyAdapter() {
            setHasStableIds(true); // this is required for expandable feature.

            mItems = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                MyGroupItem group = new MyGroupItem(i, "GROUP " + i);
                for (int j = 0; j < 5; j++) {
                    group.children.add(new MyChildItem(j, "child " + j));
                }
                mItems.add(group);
            }
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
        public MyGroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_connect, parent, false);
            return new MyGroupViewHolder(v);
        }

        @Override
        public MyChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new MyChildViewHolder(v);
        }

        @Override
        public void onBindGroupViewHolder(MyGroupViewHolder holder, int groupPosition, int viewType) {
            MyGroupItem group = mItems.get(groupPosition);
//            holder.textView.setText(group.text);
        }

        @Override
        public void onBindChildViewHolder(MyChildViewHolder holder, int groupPosition, int childPosition, int viewType) {
            MyChildItem child = mItems.get(groupPosition).children.get(childPosition);
            holder.textView.setText(child.text);
        }

        @Override
        public boolean onCheckCanExpandOrCollapseGroup(MyGroupViewHolder holder, int groupPosition, int x, int y, boolean expand) {
            return true;
        }
    }
}
