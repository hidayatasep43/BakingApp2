package com.hidayatasep.bakingapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hidayatasep.bakingapp.model.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hidayatasep43 on 8/19/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder>{

    List<Recipe> mRecipeList;
    static OnRecipeItemClickListener mListener;

    public RecipeAdapter(List<Recipe> recipeList) {
        mRecipeList = recipeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recipe recipe = mRecipeList.get(position);
        holder.mTextViewRecipe.setText(recipe.mName);
    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }

    public void setListener(OnRecipeItemClickListener mListener) {
        RecipeAdapter.mListener = mListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        @BindView(R.id.tv_recipe) TextView mTextViewRecipe;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if(mListener != null){
                mListener.onRecipeClicked(mRecipeList.get(position).id);
            }
        }
    }

    public interface OnRecipeItemClickListener{
        public void  onRecipeClicked(long id);
    }

}
