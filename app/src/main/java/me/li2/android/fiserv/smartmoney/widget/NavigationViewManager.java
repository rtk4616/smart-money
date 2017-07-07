package me.li2.android.fiserv.smartmoney.widget;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.li2.android.fiserv.smartmoney.BuildConfig;
import me.li2.android.fiserv.smartmoney.R;

/**
 * Created by weiyi on 07/07/2017.
 * https://github.com/li2
 */

public class NavigationViewManager {

    private TextView mAppVersionView;
    private Button mLogoutBtn;

    @BindView(R.id.nav_user_name_view)
    TextView mUserNameView;
    @BindView(R.id.nav_user_avator)
    ImageView mUserAvatorView;
    @BindView(R.id.nav_chatting_with_view)
    TextView mChatNameView;
    @BindView(R.id.nav_chatting_with_avator)
    ImageView mChatAvatorView;
    @BindView(R.id.nav_chatting_dot_view)
    TextView mChattingDotView;

    private Context mContext;

    public NavigationViewManager(NavigationView navigationView) {
        mContext = navigationView.getContext();

        // NOTE21 cannot use @BindView to bind view in headerView and navigationView at the same time,

        mAppVersionView = (TextView) navigationView.findViewById(R.id.app_version_view);
        mLogoutBtn = (Button) navigationView.findViewById(R.id.log_out_btn);
        mLogoutBtn.setOnClickListener(mOnLogoutClick);

        View headerView = navigationView.getHeaderView(0);
        ButterKnife.bind(this, headerView);

        setAppVersion();
    }

    public void updateAccountView(String username, String avatorUrl) {
        mUserNameView.setText(username);

        Picasso.with(mContext)
                .load(avatorUrl)
                .into(mUserAvatorView);
    }

    public void updateChatStatus(boolean isChatting) {
        int visibility = isChatting ? View.VISIBLE : View.GONE;
        mChatNameView.setVisibility(visibility);
        mChatAvatorView.setVisibility(visibility);
        mChattingDotView.setVisibility(visibility);
    }

    public void updateChatView(String chatName, String chatAvatorUrl) {
        updateChatStatus(true);
        mChatNameView.setText(String.format(mContext.getString(R.string.is_chatting_with_sb), chatName));
        Picasso.with(mContext)
                .load(chatAvatorUrl)
                .placeholder(R.drawable.i_connect_avator3)
                .error(R.drawable.i_connect_avator3)
                .into(mChatAvatorView);
    }

    private void setAppVersion() {
        mAppVersionView.setText(String.format(
                mContext.getString(R.string.app_version), BuildConfig.VERSION_NAME));
    }

    private View.OnClickListener mOnLogoutClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(mContext, "Log Out", Toast.LENGTH_SHORT).show();
        }
    };
}
