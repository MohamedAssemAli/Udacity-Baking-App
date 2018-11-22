package udacity.assem.com.udaceity_baking_app.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toolbar;

import butterknife.BindView;
import tests.assem.com.udaceity_baking_app.R;
import udacity.assem.com.udaceity_baking_app.Activities.MainActivity;
import udacity.assem.com.udaceity_baking_app.Models.IngredientModel;
import udacity.assem.com.udaceity_baking_app.Models.RecipeModel;

public class IngredientWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget);
            remoteViews.setOnClickPendingIntent(R.id.ingredient_widget_layout, pendingIntent);
            // getting recipe from sharedPref
            RecipeModel recipeModel = new WidgetHelper(context).getWidgetRecipe();
            StringBuilder stringBuilder = new StringBuilder();
            for (IngredientModel ingredientModel : recipeModel.getIngredients()) {
                stringBuilder.append(ingredientModel.getIngredient()).append("\n");
            }
            // Populate widget content
            remoteViews.setTextViewText(R.id.ingredient_widget_recipe_name, recipeModel.getName());
            remoteViews.setTextViewText(R.id.ingredient_widget_recipe_serving, "Serving : " + String.valueOf(recipeModel.getServings()) + " persons");
            remoteViews.setTextViewText(R.id.ingredient_widget_ingredient_items, stringBuilder.toString());
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }
}
