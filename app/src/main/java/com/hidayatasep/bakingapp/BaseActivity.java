package com.hidayatasep.bakingapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.realm.Realm;

/**
 * Created by hidayatasep43 on 8/13/2017.
 */

public class BaseActivity extends AppCompatActivity{

    protected Realm realm;
    protected ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm = Realm.getDefaultInstance();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    public Realm getRealm() {
        return realm;
    }

    protected void showProgress(int stringResId) {
        showProgress(getString(stringResId));
    }

    protected void showProgress(String message) {
        mProgressDialog.setMessage(message);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
    }

    protected void showProgressDeterminate(int stringResId) {
        showProgressDeterminate(getString(stringResId));
    }

    protected void showProgressDeterminate(String message) {
        mProgressDialog.setMessage(message);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMax(100);
        mProgressDialog.show();
    }

    protected void setProgressDialog(int progress){
        mProgressDialog.setProgress(progress);
    }

    protected void dismissProgress() {
        mProgressDialog.dismiss();
    }

    protected boolean isProgressShowing() {
        return mProgressDialog.isShowing();
    }

}
