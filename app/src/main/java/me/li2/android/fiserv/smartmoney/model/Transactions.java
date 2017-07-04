package me.li2.android.fiserv.smartmoney.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by weiyi on 04/07/2017.
 * https://github.com/li2
 */

public class Transactions {
    /**
     * user_name : Weiyi Li
     * account : {"avatar_url":"http://i.imgur.com/nz67a4F.jpg","name":"Genius Credit Card","id":"1234567893","balance":"5769.29","available_credit":"4230.71"}
     * transactions : [{"date":"2016-04-29","type":"Charge","where":"ShangHai Zi Ran Bie Gu - Buffet","amount":"-521.13"},
     *                 {"date":"2016-07-13","type":"Charge","where":"Hui Hang Gu Dao - Hiking","amount":"-666.66"},
     *                 {"date":"2016-08-01","type":"Charge","where":"Hai Di Lao - Birthday party","amount":"-222.22"},
     *                 {"date":"2017-03-04","type":"Charge","where":"Flying to New Zealand","amount":"-999.99"},
     *                 {"date":"2017-06-23","type":"Charge","where":"Apply Working Holiday Visa","amount":"-111.11"}]
     */
    @SerializedName("user_name")
    public String userName;
    @SerializedName("account")
    public AccountItem account;
    @SerializedName("transactions")
    public ArrayList<TransactionItem> transactionItems;
}
