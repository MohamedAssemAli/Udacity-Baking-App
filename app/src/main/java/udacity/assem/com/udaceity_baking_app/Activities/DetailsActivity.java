package udacity.assem.com.udaceity_baking_app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import tests.assem.com.udaceity_baking_app.R;
import udacity.assem.com.udaceity_baking_app.App.AppConfig;
import udacity.assem.com.udaceity_baking_app.Fragments.IngredientsFragment;
import udacity.assem.com.udaceity_baking_app.Models.RecipeModel;

public class DetailsActivity extends AppCompatActivity {

//    public static Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent bundleIntent = getIntent();
        RecipeModel recipeModel = (RecipeModel) bundleIntent.getSerializableExtra(AppConfig.INTENT_BUNDLE_KEY);
        if (recipeModel != null) {
            manageFragments(recipeModel, new IngredientsFragment());
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

    public void manageFragments(RecipeModel recipeModel, Fragment tempFragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
//        if (fragment == null)
//            fragment = tempFragment;
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConfig.INTENT_BUNDLE_KEY, recipeModel);
        tempFragment.setArguments(bundle);
        ft.replace(R.id.start_content, tempFragment);
        ft.setCustomAnimations(R.anim.fade_out, R.anim.fade_in);
        ft.commit();
    }
}
