package udacity.assem.com.udaceity_baking_app.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import udacity.assem.com.udaceity_baking_app.Fragments.StepsFragment;
import udacity.assem.com.udaceity_baking_app.Models.RecipeModel;
import udacity.assem.com.udaceity_baking_app.Models.StepModel;
import udacity.assem.com.udaceity_baking_app.Utils.Imageutility;

import static udacity.assem.com.udaceity_baking_app.Activities.DetailsActivity.fragment;

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
                pushFragment(stepModel);
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

    private void pushFragment(StepModel stepModel) {
        FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConfig.INTENT_BUNDLE_KEY, stepModel);
        fragment = new StepsFragment();
        fragment.setArguments(bundle);
        ft.replace(R.id.start_content, fragment);
        ft.setCustomAnimations(R.anim.fade_out, R.anim.fade_in);
        ft.commit();
    }
}
