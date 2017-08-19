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

public class Steps extends RealmObject{

    @PrimaryKey
    public long id;

    public long mIdSteps;
    public String mShortDescription;
    public String mDescription;
    public String mVideoUrl;
    public String mThumbnailUrl;
    public Recipe mRecipe;

    public Steps() {
    }

    public Steps(long id, long idSteps, String shortDescription, String description, String videoUrl, String thumbnailUrl) {
        this.id = id;
        mIdSteps = idSteps;
        mShortDescription = shortDescription;
        mDescription = description;
        mVideoUrl = videoUrl;
        mThumbnailUrl = thumbnailUrl;
    }

    public Steps(JSONObject object) {
        try {
            mIdSteps = object.getLong("id");
            mShortDescription = object.getString("shortDescription");
            mDescription = object.getString("description");
            mVideoUrl = object.getString("videoURL");
            mThumbnailUrl = object.getString("thumbnailURL");
        } catch (JSONException e) {
            Timber.e(e.toString());
        }
    }

    public static long getPrimaryKey(Realm realm){
        Number number = realm.where(Steps.class).max("id");
        long id = 1;
        if(number != null){
            id = (number.longValue() + 1);
        }
        return id;
    }
}
