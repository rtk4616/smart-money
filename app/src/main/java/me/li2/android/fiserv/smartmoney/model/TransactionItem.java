package me.li2.android.fiserv.smartmoney.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by weiyi on 04/07/2017.
 * https://github.com/li2
 */

public class TransactionItem {
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
}
