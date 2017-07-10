package me.li2.android.fiserv.smartmoney.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by weiyi on 10/07/2017.
 * https://github.com/li2
 */

public class OfferItem {
    /**
     * type : mall
     * name : Michael Lors
     * street : 430 W 51st St,
     * expire : 1
     * distance : 0.1
     * saved : 20.1
     * coord : {"lon":"-36.8485","lat":"174.7633"}
     */

    @SerializedName("type")
    public String type;
    @SerializedName("name")
    public String name;
    @SerializedName("street")
    public String street;
    @SerializedName("expire")
    public int expire;
    @SerializedName("distance")
    public float distance;
    @SerializedName("saved")
    public float saved;
    @SerializedName("coord")
    public CoordBean coord;

    public static class CoordBean {
        /**
         * lon : -36.8485
         * lat : 174.7633
         */
        @SerializedName("lon")
        public double lon;
        @SerializedName("lat")
        public double lat;
    }
}
