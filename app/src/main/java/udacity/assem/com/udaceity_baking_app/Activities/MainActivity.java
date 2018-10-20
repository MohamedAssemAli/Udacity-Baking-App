package udacity.assem.com.udaceity_baking_app.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tests.assem.com.udaceity_baking_app.R;
import udacity.assem.com.udaceity_baking_app.Adapters.RecipeAdapter;
import udacity.assem.com.udaceity_baking_app.Models.RecipeModel;
import udacity.assem.com.udaceity_baking_app.Networking.ApiClient;
import udacity.assem.com.udaceity_baking_app.Networking.ApiInterface;
import udacity.assem.com.udaceity_baking_app.Utils.BuildViews;

public class MainActivity extends AppCompatActivity {

    // Vars
    private final String TAG = MainActivity.class.getSimpleName();
    ArrayList<RecipeModel> recipeModelArrayList;
    RecipeAdapter recipeAdapter;
    // Views
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.main_fragment_recipe_recycler)
    RecyclerView recipeRecyclerView;
    @BindView(R.id.progress_bar)
    ContentLoadingProgressBar progressBar;
    @BindView(R.id.progress_layout)
    RelativeLayout progressLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        // toolbar
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        toolbarTitle.setText(getString(R.string.app_name));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void init() {
        toggleLayout(false);
        recipeModelArrayList = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(this, recipeModelArrayList);
        recipeRecyclerView.setAdapter(recipeAdapter);
        new BuildViews().setupLinearVerticalRecView(this, recipeRecyclerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        parseRecipe();
    }

    private void parseRecipe() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<RecipeModel>> recipeModelCall = apiService.RecipeModel();
        recipeModelCall.enqueue(new Callback<List<RecipeModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<RecipeModel>> call, @NonNull Response<List<RecipeModel>> response) {
                recipeModelArrayList.clear();
                recipeModelArrayList.addAll(Objects.requireNonNull(response.body()));
                recipeAdapter.notifyDataSetChanged();
                toggleLayout(true);
            }

            @Override
            public void onFailure(@NonNull Call<List<RecipeModel>> call, @NonNull Throwable t) {

            }
        });
    }

    private void toggleLayout(boolean flag) {
        if (flag) {
            progressLayout.setVisibility(View.GONE);
            progressBar.hide();
            recipeRecyclerView.setVisibility(View.VISIBLE);
        } else {
            progressLayout.setVisibility(View.VISIBLE);
            progressBar.show();
            recipeRecyclerView.setVisibility(View.GONE);
        }
    }
}
