package udacity.assem.com.udaceity_baking_app.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import tests.assem.com.udaceity_baking_app.R;
import udacity.assem.com.udaceity_baking_app.Activities.DetailsActivity;
import udacity.assem.com.udaceity_baking_app.App.AppConfig;
import udacity.assem.com.udaceity_baking_app.Models.RecipeModel;
import udacity.assem.com.udaceity_baking_app.Utils.Imageutility;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder> {

    private ArrayList<RecipeModel> recipeModelArrayList;
    private Context context;

    public RecipeAdapter(Context context, ArrayList<RecipeModel> recipeModelArrayList) {
        this.context = context;
        this.recipeModelArrayList = recipeModelArrayList;
    }

    @NonNull
    @Override
    public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false);
        return new RecipeHolder(item);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecipeHolder holder, int position) {
        final RecipeModel recipeModel = recipeModelArrayList.get(position);
        Imageutility.fitImage(context, holder.recipeImg, recipeModel.getImage(), R.drawable.placeholder, R.drawable.placeholder);
        holder.recipeName.setText(recipeModel.getName());
        holder.recipeServing.setText("Serving : " + String.valueOf(recipeModel.getServings()) + " persons");
        holder.recipeIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToDetailsActivity(recipeModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeModelArrayList.size();
    }

    class RecipeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_recipe_image)
        ImageView recipeImg;
        @BindView(R.id.item_recipe_name)
        TextView recipeName;
        @BindView(R.id.item_recipe_serving)
        TextView recipeServing;
        @BindView(R.id.item_recipe_ingredients)
        TextView recipeIngredients;

        RecipeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void goToDetailsActivity(RecipeModel recipeModel) {
        Intent intent = new Intent(context, DetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConfig.INTENT_BUNDLE_KEY, recipeModel);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
