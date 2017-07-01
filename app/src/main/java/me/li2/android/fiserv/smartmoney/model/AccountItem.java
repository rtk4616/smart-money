package me.li2.android.fiserv.smartmoney.model;

/**
 * name:     Checking AccountItem
 * id:       1000234268
 * balance:  $752.80
 *
 * Created by weiyi on 01/07/2017.
 * https://github.com/li2
 */
public class AccountItem {
    public String avatarUrl;
    public String name;
    public long id;
    public double balance;

    public AccountItem(String avatarUrl, String name, long id, double balance) {
        this.avatarUrl = avatarUrl;
        this.name = name;
        this.id = id;
        this.balance = balance;
    }
}
