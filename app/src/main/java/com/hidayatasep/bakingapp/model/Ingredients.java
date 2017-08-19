package com.hidayatasep.bakingapp.model;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import timber.log.Timber;

/**
 * Created by hidayatasep43 on 8/13/2017.
 */

public class Ingredients extends RealmObject{

    @PrimaryKey
    public long id;

    public int mQuantity;
    public String mMeasure;
    public String mIngredient;
    public Recipe mRecipe;

    public Ingredients() {
    }

    public Ingredients(long id, int quantity, String measure, String ingredient) {
        this.id = id;
        mQuantity = quantity;
        mMeasure = measure;
        mIngredient = ingredient;
    }

    public Ingredients(JSONObject object){
        try {
            mQuantity = object.getInt("quantity");
            mMeasure = object.getString("measure");
            mIngredient = object.getString("ingredient");
        } catch (JSONException e) {
            Timber.e(e.toString());
        }
    }

    public static long getPrimaryKey(Realm realm){
        Number number = realm.where(Ingredients.class).max("id");
        long id = 1;
        if(number != null){
            id = (number.longValue() + 1);
        }
        return id;
    }
}
