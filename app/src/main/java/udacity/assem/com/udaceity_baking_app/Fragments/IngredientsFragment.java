package udacity.assem.com.udaceity_baking_app.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tests.assem.com.udaceity_baking_app.R;
import udacity.assem.com.udaceity_baking_app.Adapters.IngredientAdapter;
import udacity.assem.com.udaceity_baking_app.Adapters.StepAdapter;
import udacity.assem.com.udaceity_baking_app.App.AppConfig;
import udacity.assem.com.udaceity_baking_app.Models.IngredientModel;
import udacity.assem.com.udaceity_baking_app.Models.RecipeModel;
import udacity.assem.com.udaceity_baking_app.Models.StepModel;
import udacity.assem.com.udaceity_baking_app.Utils.BuildViews;
import udacity.assem.com.udaceity_baking_app.Utils.Imageutility;
import udacity.assem.com.udaceity_baking_app.Widget.WidgetHelper;

public class IngredientsFragment extends Fragment {

    // Vars
    private final String TAG = IngredientsFragment.class.getSimpleName();
    private boolean isTwoPane = false;
    private RecipeModel recipeModel;
    // Views
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ingredient_fragment_recipe_image)
    ImageView recipeImg;
    @BindView(R.id.ingredient_fragment_recipe_name)
    TextView recipeName;
    @BindView(R.id.ingredient_fragment_recipe_serving)
    TextView recipeServing;
    @BindView(R.id.ingredient_fragment_ingredient_recycler)
    RecyclerView ingredientRecyclerView;
    @BindView(R.id.ingredient_fragment_steps_recycler)
    RecyclerView stepsRecyclerView;

    // OnClicks
    @OnClick(R.id.ingredient_fragment_add_to_widget)
    void addToWidget() {
        new WidgetHelper(requireContext()).setWidgetRecipe(recipeModel);
        Toast.makeText(requireContext(), getString(R.string.added_to_widget), Toast.LENGTH_SHORT).show();
    }

    public IngredientsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ingredients, container, false);
        ButterKnife.bind(this, view);
        if (getArguments() != null) {
            recipeModel = (RecipeModel) getArguments().getSerializable(AppConfig.INTENT_BUNDLE_KEY);
            isTwoPane = getArguments().getBoolean(AppConfig.INTENT_TWO_PANE_FLAG);
            init();
            populateUi();
        } else {
            closeOnError();
        }
        return view;
    }

    private void init() {
        // Pass ingredients
        ArrayList<IngredientModel> ingredientModelArrayList = (ArrayList<IngredientModel>) recipeModel.getIngredients();
        IngredientAdapter ingredientAdapter = new IngredientAdapter(requireContext(), ingredientModelArrayList);
        ingredientRecyclerView.setAdapter(ingredientAdapter);
        ingredientRecyclerView.setNestedScrollingEnabled(false);
        // Pass steps
        ArrayList<StepModel> stepModelArrayList = (ArrayList<StepModel>) recipeModel.getSteps();
        StepAdapter stepAdapter = new StepAdapter(requireContext(), stepModelArrayList, isTwoPane);
        stepsRecyclerView.setAdapter(stepAdapter);
        stepsRecyclerView.setNestedScrollingEnabled(false);
    }

    @SuppressLint("SetTextI18n")
    private void populateUi() {
        Imageutility.fitImage(requireContext(), recipeImg, recipeModel.getImage(), R.drawable.placeholder, R.drawable.placeholder);
        recipeName.setText(recipeModel.getName());
        recipeServing.setText("Serving : " + String.valueOf(recipeModel.getServings()) + " persons");
        new BuildViews().setupGridRecView(requireContext(), ingredientRecyclerView, 2);
        new BuildViews().setupLinearVerticalRecView(requireContext(), stepsRecyclerView);
    }

    private void closeOnError() {
        Objects.requireNonNull(getActivity()).finish();
        Toast.makeText(requireContext(), R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }
}
