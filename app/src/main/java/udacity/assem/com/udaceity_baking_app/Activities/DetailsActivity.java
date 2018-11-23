package udacity.assem.com.udaceity_baking_app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.Objects;

import tests.assem.com.udaceity_baking_app.R;
import udacity.assem.com.udaceity_baking_app.App.AppConfig;
import udacity.assem.com.udaceity_baking_app.Fragments.IngredientsFragment;
import udacity.assem.com.udaceity_baking_app.Models.RecipeModel;

public class DetailsActivity extends AppCompatActivity {

    private boolean isTwoPane = false;
    private boolean upFlag = false;
    private RecipeModel recipeModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent bundleIntent = getIntent();
        recipeModel = (RecipeModel) bundleIntent.getSerializableExtra(AppConfig.INTENT_BUNDLE_KEY);
        if (recipeModel != null) {
            // toolbar
            Objects.requireNonNull(getSupportActionBar()).setTitle(recipeModel.getName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            if (findViewById(R.id.details_activity_detail_fragment) != null)
                isTwoPane = true;
            if (savedInstanceState == null) {
                manageFragments(recipeModel);
                upFlag = true;
            } else {
                upFlag = false;
            }
        } else {
            closeOnError();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    public void manageFragments(RecipeModel recipeModel) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConfig.INTENT_BUNDLE_KEY, recipeModel);
        bundle.putSerializable(AppConfig.INTENT_TWO_PANE_FLAG, isTwoPane);
        IngredientsFragment ingredientsFragment = new IngredientsFragment();
        ingredientsFragment.setArguments(bundle);
        if (isTwoPane)
            ft.replace(R.id.details_activity_master_fragment, ingredientsFragment);
        else
            ft.replace(R.id.start_content, ingredientsFragment);
        ft.setCustomAnimations(R.anim.fade_out, R.anim.fade_in);
        ft.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        handleBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        handleBackPressed();
    }

    private void handleBackPressed() {
        if (isTwoPane || findViewById(R.id.steps_fragment_video_player) == null)
            finish();
        else {
            manageFragments(recipeModel);
        }
    }
}
