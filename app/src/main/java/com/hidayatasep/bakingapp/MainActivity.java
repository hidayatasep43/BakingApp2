package com.hidayatasep.bakingapp;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hidayatasep.bakingapp.model.Ingredients;
import com.hidayatasep.bakingapp.model.Recipe;
import com.hidayatasep.bakingapp.model.Steps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements RecipeAdapter.OnRecipeItemClickListener{

    @BindView(R.id.recyler_view) RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.tv_empty) TextView mTextViewEmpty;

    List<Recipe> mRecipeList;
    RealmResults<Recipe> mRecipes;
    RecipeAdapter mAdapter;

    boolean isDownload = false;

    RealmChangeListener<RealmResults<Recipe>> mRealmChangeListener = new RealmChangeListener<RealmResults<Recipe>>() {
        @Override
        public void onChange(RealmResults<Recipe> recipes) {
            mRecipeList.clear();
            for(Recipe recipe: recipes){
                mRecipeList.add(recipe);
                mAdapter.notifyDataSetChanged();
            }
            setUpContent();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //get all data
        mRecipes = Recipe.getAllRecipe(realm);
        mRecipes.addChangeListener(mRealmChangeListener);
        mRecipeList = new ArrayList<>();

        for(Recipe recipe: mRecipes){
            mRecipeList.add(recipe);
        }

        mAdapter = new RecipeAdapter(mRecipeList);
        mAdapter.setListener(this);
        int column = Util.calculateNumberOfColumn(this, 132);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this, column);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new CustomItemOffset(MainActivity.this, R.dimen.margin_item));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        setUpContent();

        //check if already download
        if(savedInstanceState != null){
            isDownload = savedInstanceState.getBoolean("download", false);
        }
        if(!isDownload){
            showProgress(R.string.loading);
            doGetRecipeData();
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doGetRecipeData();
            }
        });
    }

    private void doGetRecipeData() {
        if(Util.isNetworkConnected(this)){
            getRecipeData();
        }else{
            if(mProgressDialog.isShowing()) dismissProgress();
            if(mSwipeRefreshLayout.isRefreshing()) mSwipeRefreshLayout.setRefreshing(false);
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("download", isDownload);
    }

    private void setUpContent() {
        if(mRecipeList.isEmpty()){
            mRecyclerView.setVisibility(View.GONE);
            mTextViewEmpty.setVisibility(View.VISIBLE);
        }else{
            mRecyclerView.setVisibility(View.VISIBLE);
            mTextViewEmpty.setVisibility(View.GONE);
        }
    }

    public void getRecipeData(){
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(mProgressDialog.isShowing()) dismissProgress();
                        if(mSwipeRefreshLayout.isRefreshing()) mSwipeRefreshLayout.setRefreshing(false);
                        Timber.e(e.toString());
                        Toast.makeText(MainActivity.this, R.string.error_label, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                Timber.d("result = " + result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(mProgressDialog.isShowing()) dismissProgress();
                        if(mSwipeRefreshLayout.isRefreshing()) mSwipeRefreshLayout.setRefreshing(false);

                        if(result.isEmpty()){
                            Toast.makeText(MainActivity.this, R.string.error_label, Toast.LENGTH_SHORT).show();
                        }else{
                            //convert to realm
                            try {
                                JSONArray jsonArray = new JSONArray(result);
                                for(int i = 0; i < jsonArray.length(); i++){
                                    final JSONObject recipeObject = jsonArray.getJSONObject(i);
                                    final JSONArray ingredientObject = recipeObject.getJSONArray("ingredients");
                                    final JSONArray stepObject = recipeObject.getJSONArray("steps");

                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            Recipe recipe = new Recipe(recipeObject);
                                            realm.copyToRealmOrUpdate(recipe);

                                            for(int j = 0; j < ingredientObject.length(); j++){
                                                try {
                                                    Ingredients ingredients = new Ingredients(ingredientObject.getJSONObject(j));
                                                    long id = Ingredients.getPrimaryKey(realm);
                                                    ingredients.id = id;
                                                    ingredients.mRecipe = recipe;

                                                    realm.copyToRealmOrUpdate(ingredients);
                                                    recipe.mListIngredients.add(ingredients);
                                                } catch (JSONException e) {
                                                    Timber.e(e.toString());
                                                }
                                            }

                                            for(int j = 0; j < stepObject.length(); j++){
                                                try {
                                                    Steps steps= new Steps(stepObject.getJSONObject(j));
                                                    long id = Steps.getPrimaryKey(realm);
                                                    steps.id = id;
                                                    steps.mRecipe = recipe;

                                                    realm.copyToRealmOrUpdate(steps);
                                                    recipe.mListSteps.add(steps);
                                                } catch (JSONException e) {
                                                    Timber.e(e.toString());
                                                }
                                            }
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                Timber.e(e.toString());
                            }
                        }

                        isDownload = true;

                    }
                });
            }
        });
    }

    @Override
    public void onRecipeClicked(long id) {

    }
}
