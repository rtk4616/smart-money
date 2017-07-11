package me.li2.android.fiserv.smartmoney.model;

import android.text.TextUtils;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import me.li2.android.fiserv.smartmoney.R;

/**
 * Created by weiyi on 10/07/2017.
 * https://github.com/li2
 *
 * Fake data: http://li2.me/assets/file/fiserv_test_data_offer_list.json
 */

public class OfferItemDeserializer implements JsonDeserializer<OfferItem> {
    @Override
    public OfferItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();

        int selectedIconResId = R.drawable.i_map_marker_shopping_zone_green;
        int unselectedIconResId = R.drawable.i_map_marker_shopping_zone_gray;
        LatLng latLng = new LatLng(-36.8485, 174.7633); // default auckland

        String type = jsonObject.get("type").getAsString();
        if (!TextUtils.isEmpty(type)) {
            if (type.equals("book")) {
                selectedIconResId = R.drawable.i_map_marker_book_green;
                unselectedIconResId = R.drawable.i_map_marker_book_gray;
            } else if (type.equals("coffee")) {
                selectedIconResId = R.drawable.i_map_marker_coffee_green;
                unselectedIconResId = R.drawable.i_map_marker_coffee_gray;
            }
        }

        JsonObject coordJsonObject = jsonObject.get("coord").getAsJsonObject();
        if (coordJsonObject != null) {
            double lon = coordJsonObject.get("lon").getAsDouble();
            double lat = coordJsonObject.get("lat").getAsDouble();
            latLng = new LatLng(lon, lat);
        }

        return new OfferItem(
                type,
                jsonObject.get("name").getAsString(),
                jsonObject.get("street").getAsString(),
                jsonObject.get("expire").getAsInt(),
                jsonObject.get("distance").getAsFloat(),
                jsonObject.get("saved").getAsFloat(),
                latLng,
                selectedIconResId,
                unselectedIconResId);
    }
}
