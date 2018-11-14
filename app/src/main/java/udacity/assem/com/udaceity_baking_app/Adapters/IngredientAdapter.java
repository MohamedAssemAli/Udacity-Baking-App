package udacity.assem.com.udaceity_baking_app.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import tests.assem.com.udaceity_baking_app.R;
import udacity.assem.com.udaceity_baking_app.Models.IngredientModel;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientHolder> {

    private ArrayList<IngredientModel> ingredientModelArrayList;
    private Context context;

    public IngredientAdapter(Context context, ArrayList<IngredientModel> ingredientModelArrayList) {
        this.context = context;
        this.ingredientModelArrayList = ingredientModelArrayList;
    }

    @NonNull
    @Override
    public IngredientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.item_ingredient, parent, false);
        return new IngredientHolder(item);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull IngredientHolder holder, int position) {
        final IngredientModel ingredientModel = ingredientModelArrayList.get(position);
        holder.ingredientName.setText(ingredientModel.getIngredient());
        holder.ingredientQuantity.setText(String.valueOf(ingredientModel.getQuantity()));
        holder.ingredientMeasure.setText(ingredientModel.getMeasure());
    }

    @Override
    public int getItemCount() {
        return ingredientModelArrayList.size();
    }

    class IngredientHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_ingredient_name)
        TextView ingredientName;
        @BindView(R.id.item_ingredient_quantity)
        TextView ingredientQuantity;
        @BindView(R.id.item_ingredient_measure)
        TextView ingredientMeasure;

        IngredientHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
