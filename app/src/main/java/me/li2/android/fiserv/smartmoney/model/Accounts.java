package me.li2.android.fiserv.smartmoney.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by weiyi on 03/07/2017.
 * https://github.com/li2
 */

public class Accounts {
    /**
     * user_name : Weiyi Li
     * accounts : [{"avatar_url":"http://i.imgur.com/9gbQ7YR.jpg","name":"Weiyi Li","id":"1234567890","balance":"521.13"},{"avatar_url":"http://i.imgur.com/0E2tgV7.jpg","name":"Checking Account","id":"1234567891","balance":"521.13"},{"avatar_url":"http://i.imgur.com/P5JLfjk.jpg","name":"Saving Account","id":"1234567892","balance":"21.43"},{"avatar_url":"http://i.imgur.com/nz67a4F.jpg","name":"Genius Credit Card","id":"1234567893","balance":"5769.29"},{"avatar_url":"http://i.imgur.com/dFH34N5.jpg","name":"Student Loan","id":"1234567894","balance":"1.11"},{"avatar_url":"http://i.imgur.com/FI49ftb.jpg","name":"Credit Card","id":"1234567895","balance":"0.0"},{"avatar_url":"http://i.imgur.com/DNKnbG8.jpg","name":"New Zealand","id":"1234567896","balance":"6666.66"}]
     */
    @SerializedName("user_name")
    public String userName;
    @SerializedName("accounts")
    public List<AccountItem> accounts;
}
