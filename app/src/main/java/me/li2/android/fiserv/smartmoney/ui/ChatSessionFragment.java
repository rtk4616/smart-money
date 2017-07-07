package me.li2.android.fiserv.smartmoney.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.li2.android.fiserv.smartmoney.R;
import me.li2.android.fiserv.smartmoney.model.ChatCause;

/**
 * Created by weiyi on 07/07/2017.
 * https://github.com/li2
 */

public class ChatSessionFragment extends Fragment {
    private static final String TAG = "ChatSessionFragment";
    public static final String ARG_KEY_CHAT_CAUSE = "arg_chat_cause";

    public static ChatSessionFragment newInstance(ChatCause chatCause) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_KEY_CHAT_CAUSE, chatCause);

        ChatSessionFragment fragment = new ChatSessionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.textView)
    TextView mTextView;

    private ChatCause mChatCause;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mChatCause = args.getParcelable(ARG_KEY_CHAT_CAUSE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_session, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTextView.setText(mChatCause.toString());
    }
}
