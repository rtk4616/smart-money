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

    private ViewGroup.LayoutParams mHeaderLayoutParams;
    private int mHeaderMaxHeight;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offer_detail, container, false);
        ButterKnife.bind(this, view);

        mHeaderLayoutParams = mHeaderView.getLayoutParams();
        mHeaderMaxHeight = (int)(getResources().getDimension(R.dimen.offer_info_header_height));

        return view;
    }

    public void setHeaderHeight(float percent) {
        if (mHeaderLayoutParams != null) {
            int height = (int)(mHeaderMaxHeight * percent);
            // NOTE21: if set height to 0, the view still has a height in constraint layout, the workaround is set to 1.
            if (height <= 0) {
                height = 1;
            }
            mHeaderLayoutParams.height = height;
            mHeaderView.setLayoutParams(mHeaderLayoutParams);
        }
    }
}
