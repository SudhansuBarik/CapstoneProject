package io.github.sudhansubarik.moviescentral.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import io.github.sudhansubarik.moviescentral.R;
import io.github.sudhansubarik.moviescentral.activities.MainActivity;
import io.github.sudhansubarik.moviescentral.utils.Constants;

/**
 * Implementation of App Widget functionality.
 */
public class MoviesAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.movies_app_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.movies_app_widget);

        Intent widgetPressIntent = new Intent(context, MainActivity.class);
        PendingIntent buttonPressPendingIntent = PendingIntent.getActivity(context, 0, widgetPressIntent, 0);
        views.setOnClickPendingIntent(R.id.widget_relativeLayout, buttonPressPendingIntent);

        SharedPreferences sharedPref = context.getSharedPreferences(Constants.SHARED_PREF_MOVIE, Context.MODE_PRIVATE);

        String movieString = sharedPref.getString(Constants.SHARED_PREF_KEY, "Not Found!");

        views.setTextViewText(R.id.appwidget_text, movieString);

        AppWidgetManager.getInstance(context).updateAppWidget(
                new ComponentName(context, MoviesAppWidget.class), views);
    }
}
