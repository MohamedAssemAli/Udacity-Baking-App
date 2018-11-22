package udacity.assem.com.udaceity_baking_app.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import tests.assem.com.udaceity_baking_app.R;
import udacity.assem.com.udaceity_baking_app.App.AppConfig;
import udacity.assem.com.udaceity_baking_app.Fragments.StepsFragment;
import udacity.assem.com.udaceity_baking_app.Models.StepModel;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.RecipeHolder> {

    private ArrayList<StepModel> stepModelArrayList;
    private Context context;
    private boolean isTwoPane;

    public StepAdapter(Context context, ArrayList<StepModel> stepModelArrayList, boolean isTwoPane) {
        this.context = context;
        this.stepModelArrayList = stepModelArrayList;
        this.isTwoPane = isTwoPane;
    }

    @NonNull
    @Override
    public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.item_step, parent, false);
        return new RecipeHolder(item);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecipeHolder holder, @SuppressLint("RecyclerView") final int position) {
        final StepModel stepModel = stepModelArrayList.get(position);
        holder.stepNumber.setText(String.valueOf(stepModel.getId() + 1));
        holder.stepShortDesc.setText(stepModel.getShortDescription());
        holder.steps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushFragment(stepModelArrayList, position);
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

    private void pushFragment(ArrayList<StepModel> stepModelArrayList, int index) {
        FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConfig.INTENT_BUNDLE_KEY, stepModelArrayList);
        bundle.putBoolean(AppConfig.INTENT_TWO_PANE_FLAG, isTwoPane);
        bundle.putInt(AppConfig.INTENT_STEP_INDEX, index);
        StepsFragment fragment = new StepsFragment();
        fragment.setArguments(bundle);
        if (isTwoPane) {
            ft.replace(R.id.details_activity_detail_fragment, fragment);
        } else {
            ft.replace(R.id.start_content, fragment);
            ft.addToBackStack(null);
        }
        ft.setCustomAnimations(R.anim.fade_out, R.anim.fade_in);
        ft.commit();
    }
}
