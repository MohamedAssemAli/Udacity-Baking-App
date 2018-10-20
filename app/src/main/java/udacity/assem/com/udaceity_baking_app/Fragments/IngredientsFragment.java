package udacity.assem.com.udaceity_baking_app.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tests.assem.com.udaceity_baking_app.R;
import udacity.assem.com.udaceity_baking_app.Adapters.IngredientAdapter;
import udacity.assem.com.udaceity_baking_app.Adapters.RecipeAdapter;
import udacity.assem.com.udaceity_baking_app.App.AppConfig;
import udacity.assem.com.udaceity_baking_app.Models.IngredientModel;
import udacity.assem.com.udaceity_baking_app.Models.RecipeModel;
import udacity.assem.com.udaceity_baking_app.Networking.ApiClient;
import udacity.assem.com.udaceity_baking_app.Networking.ApiInterface;
import udacity.assem.com.udaceity_baking_app.Utils.BuildViews;
import udacity.assem.com.udaceity_baking_app.Utils.Imageutility;

public class IngredientsFragment extends Fragment {

    // Vars
    private final String TAG = IngredientsFragment.class.getSimpleName();
    ArrayList<IngredientModel> ingredientModelArrayList;
    IngredientAdapter ingredientAdapter;
    RecipeModel recipeModel;
    // Views
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.ingredient_fragment_recipe_image)
    ImageView recipeImg;
    @BindView(R.id.ingredient_fragment_recipe_name)
    TextView recipeName;
    @BindView(R.id.ingredient_fragment_recipe_serving)
    TextView recipeServing;
    @BindView(R.id.ingredient_fragment_ingredient_recycler)
    RecyclerView ingredientRecyclerView;

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
            init();
            populateUi();
        } else {
            closeOnError();
        }
        return view;
    }

    private void init() {
        ingredientModelArrayList = (ArrayList<IngredientModel>) recipeModel.getIngredients();
        ingredientAdapter = new IngredientAdapter(requireContext(), ingredientModelArrayList);
        ingredientRecyclerView.setAdapter(ingredientAdapter);
        ingredientRecyclerView.setNestedScrollingEnabled(false);
    }

    @SuppressLint("SetTextI18n")
    private void populateUi() {
        toolbarTitle.setText(recipeModel.getName());
        Imageutility.fitImage(requireContext(), recipeImg, recipeModel.getImage(), R.drawable.placeholder, R.drawable.placeholder);
        recipeName.setText(recipeModel.getName());
        recipeServing.setText("Serving : " + String.valueOf(recipeModel.getServings()) + " persons");
        new BuildViews().setupLinearVerticalRecView(requireContext(), ingredientRecyclerView);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void closeOnError() {
        Objects.requireNonNull(getActivity()).finish();
        Toast.makeText(requireContext(), R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }
}
