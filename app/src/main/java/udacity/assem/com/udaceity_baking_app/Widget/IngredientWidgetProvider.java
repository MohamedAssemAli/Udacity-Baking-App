package udacity.assem.com.udaceity_baking_app.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import tests.assem.com.udaceity_baking_app.R;
import udacity.assem.com.udaceity_baking_app.Activities.MainActivity;

public class IngredientWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget);
            remoteViews.setOnClickPendingIntent(R.id.ingredient_widget_btn, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }

    }
}
