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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import tests.assem.com.udaceity_baking_app.R;
import udacity.assem.com.udaceity_baking_app.Activities.DetailsActivity;
import udacity.assem.com.udaceity_baking_app.App.AppConfig;
import udacity.assem.com.udaceity_baking_app.Models.RecipeModel;
import udacity.assem.com.udaceity_baking_app.Models.StepModel;
import udacity.assem.com.udaceity_baking_app.Utils.Imageutility;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.RecipeHolder> {

    private ArrayList<StepModel> stepModelArrayList;
    private Context context;

    public StepAdapter(Context context, ArrayList<StepModel> stepModelArrayList) {
        this.context = context;
        this.stepModelArrayList = stepModelArrayList;
    }

    @NonNull
    @Override
    public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.item_step, parent, false);
        return new RecipeHolder(item);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecipeHolder holder, int position) {
        final StepModel stepModel = stepModelArrayList.get(position);
        holder.stepNumber.setText(String.valueOf(stepModel.getId() + 1));
        holder.stepShortDesc.setText(stepModel.getShortDescription());
        holder.steps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return stepModelArrayList.size();
    }

    class RecipeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_step_number)
        TextView stepNumber;
        @BindView(R.id.item_step_short_desc)
        TextView stepShortDesc;
        @BindView(R.id.item_step_steps)
        TextView steps;

        RecipeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void goToDetailsActivity(StepModel stepModel) {
//        Intent intent = new Intent(context, DetailsActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(AppConfig.INTENT_BUNDLE_KEY, stepModel);
//        intent.putExtras(bundle);
//        context.startActivity(intent);
    }
}
