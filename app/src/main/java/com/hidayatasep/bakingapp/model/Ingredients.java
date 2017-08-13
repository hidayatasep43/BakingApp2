package com.hidayatasep.bakingapp.model;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by hidayatasep43 on 8/13/2017.
 */

public class Ingredients extends RealmObject{

    @PrimaryKey
    private long id;

    private int mQuantity;
    private String mMeasure;
    private String mIngredient;

    public Ingredients() {
    }

    public Ingredients(long id, int quantity, String measure, String ingredient) {
        this.id = id;
        mQuantity = quantity;
        mMeasure = measure;
        mIngredient = ingredient;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int quantity) {
        mQuantity = quantity;
    }

    public String getMeasure() {
        return mMeasure;
    }

    public void setMeasure(String measure) {
        mMeasure = measure;
    }

    public String getIngredient() {
        return mIngredient;
    }

    public void setIngredient(String ingredient) {
        mIngredient = ingredient;
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
