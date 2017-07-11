package me.li2.android.fiserv.smartmoney.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by weiyi on 10/07/2017.
 * https://github.com/li2
 */

public class Offers {
    @SerializedName("offers")
    public List<OfferItem> offers;

    public Offers(List<OfferItem> items) {
        offers = items;
    }
}
