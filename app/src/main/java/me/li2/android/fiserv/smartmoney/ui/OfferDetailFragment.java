package me.li2.android.fiserv.smartmoney.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.li2.android.fiserv.smartmoney.R;

/**
 * Created by weiyi on 09/07/2017.
 * https://github.com/li2
 */

public class OfferDetailFragment extends Fragment {

    @BindView(R.id.offer_info_header_view)
    View mHeaderView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offer_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void showHeader(boolean show) {
        if (mHeaderView != null) {
            mHeaderView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
}
