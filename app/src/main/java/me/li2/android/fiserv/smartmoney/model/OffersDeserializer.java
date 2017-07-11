package me.li2.android.fiserv.smartmoney.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by weiyi on 10/07/2017.
 * https://github.com/li2
 */

public class OffersDeserializer implements JsonDeserializer<Offers> {
    @Override
    public Offers deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();

        Type type = new TypeToken<ArrayList<OfferItem>>() {}.getType();
        List<OfferItem> items = context.deserialize(jsonObject.get("offers"), type);

        return new Offers(items);
    }
}
