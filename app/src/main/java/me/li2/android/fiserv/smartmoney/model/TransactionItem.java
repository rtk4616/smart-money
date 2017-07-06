package me.li2.android.fiserv.smartmoney.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by weiyi on 04/07/2017.
 * https://github.com/li2
 */

public class TransactionItem implements Parcelable {
    /**
     * date : 2016-04-29
     * type : Charge
     * where : ShangHai Zi Ran Bie Gu - Buffet
     * amount : -521.13
     */
    @SerializedName("date")
    public Date date;
    @SerializedName("type")
    public String type;
    @SerializedName("where")
    public String where;
    @SerializedName("amount")
    public double amount;

    // for swipeable
    public long id;
    public boolean isPinned;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(date);
        dest.writeString(type);
        dest.writeString(where);
        dest.writeDouble(amount);
        dest.writeLong(id);
        dest.writeValue(isPinned);
    }

    public static final Parcelable.Creator<TransactionItem> CREATOR = new Parcelable.Creator<TransactionItem>() {
        @Override
        public TransactionItem createFromParcel(Parcel source) {
            return new TransactionItem(source);
        }

        @Override
        public TransactionItem[] newArray(int size) {
            return new TransactionItem[size];
        }
    };

    private TransactionItem(Parcel source) {
        date = (Date) source.readValue(null);
        type = source.readString();
        where = source.readString();
        amount = source.readDouble();
        id = source.readLong();
        isPinned = (boolean) source.readValue(null);
    }
}
