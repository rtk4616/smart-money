package me.li2.android.fiserv.smartmoney.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * name:     Checking AccountItem
 * id:       1000234268
 * balance:  $752.80
 *
 * Created by weiyi on 01/07/2017.
 * https://github.com/li2
 */
public class AccountItem implements Parcelable{
    public long position; // NOTE21 for transition with unique id.

    @SerializedName("avatar_url")
    public String avatarUrl;
    @SerializedName("name")
    public String name;
    @SerializedName("id")
    public long id;
    @SerializedName("balance")
    public double balance;
    @SerializedName("available_credit")
    public double availableCredit;

    public AccountItem(String avatarUrl, String name, long id, double balance, double availableCredit) {
        this.avatarUrl = avatarUrl;
        this.name = name;
        this.id = id;
        this.balance = balance;
        this.availableCredit = availableCredit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(position);
        // NOTE21: use writeValue() to write String to parcel will cause problem, that the object cannot retrieve from intent extras!
        dest.writeString(avatarUrl);
        dest.writeString(name);
        dest.writeLong(id);
        dest.writeDouble(balance);
        dest.writeDouble(availableCredit);
    }

    public static final Parcelable.Creator<AccountItem> CREATOR = new Parcelable.Creator<AccountItem>() {
        @Override
        public AccountItem createFromParcel(Parcel source) {
            return new AccountItem(source);
        }

        @Override
        public AccountItem[] newArray(int size) {
            return new AccountItem[size];
        }
    };

    private AccountItem(Parcel source) {
        position = source.readLong();
        avatarUrl = source.readString();
        name = source.readString();
        id = source.readLong();
        balance = source.readDouble();
        availableCredit = source.readDouble();
    }
}
