package com.hidayatasep.bakingapp.model;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by hidayatasep43 on 8/13/2017.
 */

public class Steps extends RealmObject{

    @PrimaryKey
    private long id;

    private long mIdSteps;
    private String mShortDescription;
    private String mDescription;
    private String mVideoUrl;
    private String mThumbnailUrl;

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

    public long getIdSteps() {
        return mIdSteps;
    }

    public void setIdSteps(long idSteps) {
        mIdSteps = idSteps;
    }

    public String getShortDescription() {
        return mShortDescription;
    }

    public void setShortDescription(String shortDescription) {
        mShortDescription = shortDescription;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        mVideoUrl = videoUrl;
    }

    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        mThumbnailUrl = thumbnailUrl;
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
