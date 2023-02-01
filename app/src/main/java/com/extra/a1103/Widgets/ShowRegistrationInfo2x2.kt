package com.extra.a1103.Widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.content.ContextCompat
import com.extra.a1103.R
import com.extra.a1103.RemoteViewsServices.showRegistrationInfoRemoteViewsServices
import com.extra.a1103.RemoteViewsServices.showRegistrationInfoServices
import com.extra.a1103.registration_home

/**
 * Implementation of App Widget functionality.
 */
class ShowRegistrationInfo2x2 : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if(intent!=null){
            if(intent.action.equals("com.appwidget.action.click.addRe")) {
                var intent=Intent(context, registration_home::class.java)
                intent.flags= Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("action","startAddVoDialog")
                ContextCompat.startActivity(context!!, intent, null)
            }
        }
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.show_registration_info2x2)
    var listIntent=Intent(context, showRegistrationInfoServices::class.java)
    views.setRemoteAdapter(R.id.list,listIntent)
    var intent=Intent(context, ShowRegistrationInfo2x2::class.java)
    intent.action = "com.appwidget.action.click.addRe"
    var pendingIntent= PendingIntent.getBroadcast(context,0,intent,0)
    views.setPendingIntentTemplate(R.id.list,pendingIntent)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}