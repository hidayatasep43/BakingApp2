package com.hidayatasep.bakingapp.model;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;
import timber.log.Timber;

/**
 * Created by hidayatasep43 on 8/13/2017.
 */

public class Recipe extends RealmObject {

    @PrimaryKey
    public long id;
    public String mName;
    public RealmList<Ingredients> mListIngredients;
    public RealmList<Steps> mListSteps;

    public Recipe() {
        mListIngredients = new RealmList<>();
        mListSteps = new RealmList<>();
    }

    public Recipe(JSONObject jsonObject) {
        try {
            id = jsonObject.getLong("id");
            mName = jsonObject.getString("name");
        } catch (JSONException e) {
            Timber.e(e.toString());
        }
        mListIngredients = new RealmList<>();
        mListSteps = new RealmList<>();
    }


    public static boolean checkRecipeData(Realm realm){
        int count = realm.where(Recipe.class).findAllSorted("id").size();
        if (count == 0){
            return false;
        }
        return true;
    }

    public static RealmResults<Recipe> getAllRecipe(Realm realm){
        return realm.where(Recipe.class).findAll();
    }
}
