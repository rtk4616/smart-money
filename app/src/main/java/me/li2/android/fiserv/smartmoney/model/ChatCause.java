package me.li2.android.fiserv.smartmoney.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import me.li2.android.fiserv.smartmoney.R;

/**
 * Created by weiyi on 07/07/2017.
 * https://github.com/li2
 */

public class ChatCause implements Parcelable {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({REPORT_TYPE_FRAUDULENT_TRANSACTION, REPORT_TYPE_DUPLICATED_TRANSACTION})
    public @interface ReportType {}
    public static final int REPORT_TYPE_FRAUDULENT_TRANSACTION = 0;
    public static final int REPORT_TYPE_DUPLICATED_TRANSACTION = 1;

    public @ChatCause.ReportType int reportType;
    public String chatToName;
    public String chatToAvatorUrl;
    public String transactionType;
    public long transactionId;
    public String accountName;
    public long accountId;


    public ChatCause(int reportType, String chatToName, String chatToAvatorUrl,
                     String transactionType, long transactionId, String accountName, long accountId) {
        this.reportType = reportType;
        this.chatToName = chatToName;
        this.chatToAvatorUrl = chatToAvatorUrl;
        this.transactionType = transactionType;
        this.transactionId = transactionId;
        this.accountName = accountName;
        this.accountId = accountId;
    }

    @SuppressWarnings("ResourceType")
    private ChatCause(Parcel source) {
        reportType = source.readInt();
        chatToName = source.readString();
        chatToAvatorUrl = source.readString();
        transactionType = source.readString();
        transactionId = source.readLong();
        accountName = source.readString();
        accountId = source.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(reportType);
        dest.writeString(chatToName);
        dest.writeString(chatToAvatorUrl);
        dest.writeString(transactionType);
        dest.writeLong(transactionId);
        dest.writeString(accountName);
        dest.writeLong(accountId);
    }

    public static final Parcelable.Creator<ChatCause> CREATOR = new Creator<ChatCause>() {
        @Override
        public ChatCause createFromParcel(Parcel source) {
            return new ChatCause(source);
        }

        @Override
        public ChatCause[] newArray(int size) {
            return new ChatCause[size];
        }
    };

    @Override
    public String toString() {
        Context context = MainApplication.getAppContext();
        String formatter = context.getString(R.string.chat_cause_detail_formatter);
        int reportTypeStrId = (reportType == REPORT_TYPE_DUPLICATED_TRANSACTION)
                ? R.string.duplicate : R.string.fraudulent;

        return String.format(formatter,
                context.getString(reportTypeStrId),
                transactionType,
                transactionId,
                accountName,
                accountId);
    }
}
