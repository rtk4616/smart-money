package me.li2.android.fiserv.smartmoney.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

/**
 * Created by weiyi on 10/07/2017.
 * https://github.com/li2
 */

public class OfferItem implements Parcelable {
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

    public LatLng latLng;
    public int selectedIconResId;
    public int unselectedIconResId;


    public OfferItem(String type, String name, String street, int expire, float distance,
                     float saved, LatLng latLng, int selectedIconResId, int unselectedIconResId) {
        this.type = type;
        this.name = name;
        this.street = street;
        this.expire = expire;
        this.distance = distance;
        this.saved = saved;
        this.latLng = latLng;
        this.selectedIconResId = selectedIconResId;
        this.unselectedIconResId = unselectedIconResId;
    }

    private OfferItem(Parcel source) {
        type = source.readString();
        name = source.readString();
        street = source.readString();
        expire = source.readInt();
        distance = source.readFloat();
        saved = source.readFloat();
        latLng = source.readParcelable(LatLng.class.getClassLoader());
        selectedIconResId = source.readInt();
        unselectedIconResId = source.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(name);
        dest.writeString(street);
        dest.writeInt(expire);
        dest.writeFloat(distance);
        dest.writeFloat(saved);
        dest.writeParcelable(latLng, flags);
        dest.writeInt(selectedIconResId);
        dest.writeInt(unselectedIconResId);
    }

    public static final Parcelable.Creator<OfferItem> CREATOR = new Creator<OfferItem>() {
        @Override
        public OfferItem createFromParcel(Parcel source) {
            return new OfferItem(source);
        }

        @Override
        public OfferItem[] newArray(int size) {
            return new OfferItem[size];
        }
    };
}
