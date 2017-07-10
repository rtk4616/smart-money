package me.li2.android.fiserv.smartmoney.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by weiyi on 10/07/2017.
 * https://github.com/li2
 */

public class Offers {
    @SerializedName("offers")
    public ArrayList<OfferItem> offers;
}
