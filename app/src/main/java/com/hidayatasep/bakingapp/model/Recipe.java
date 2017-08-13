package com.hidayatasep.bakingapp.model;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

/**
 * Created by hidayatasep43 on 8/13/2017.
 */

public class Recipe extends RealmObject {

    @PrimaryKey
    private long id;

    private String mName;
    private RealmList<Ingredients> mListIngredients;
    private RealmList<Steps> mListSteps;

    public Recipe() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public RealmList<Ingredients> getListIngredients() {
        return mListIngredients;
    }

    public void setListIngredients(RealmList<Ingredients> listIngredients) {
        mListIngredients = listIngredients;
    }

    public RealmList<Steps> getListSteps() {
        return mListSteps;
    }

    public void setListSteps(RealmList<Steps> listSteps) {
        mListSteps = listSteps;
    }

    public static RealmResults<Recipe> getAllRecipe(Realm realm){
        return realm.where(Recipe.class).findAllSorted("id");
    }

    public static boolean checkRecipeData(Realm realm){
        int count = realm.where(Recipe.class).findAllSorted("id").size();
        if (count == 0){
            return false;
        }
        return true;
    }
}
