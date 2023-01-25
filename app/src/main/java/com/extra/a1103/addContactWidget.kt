package com.extra.a1103

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.content.ContextCompat

/**
 * Implementation of App Widget functionality.
 */
class addContactWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget_addContact(context, appWidgetManager, appWidgetId)
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
            if(intent.action.equals("com.appwidget.action.click.addContact")){
                var intent=Intent(context,registration_home::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("action","startAddVoDialog")
                ContextCompat.startActivity(context!!, intent, null)
//                val manager = AppWidgetManager.getInstance(context)
//                val views = RemoteViews(context!!.packageName, R.layout.edit_info_widget)
//                manager.updateAppWidget(
//                    ComponentName(context!!, EditInfoWidget::class.java),
//                    views
//                )
            }
        }
    }
}

internal fun updateAppWidget_addContact(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.add_contact_widget)
    var intent= Intent(context, addContactWidget::class.java)
    intent.action="com.appwidget.action.click.addContact"
    var pendingIntent= PendingIntent.getBroadcast(context,0,intent, PendingIntent.FLAG_MUTABLE)
    views.setOnClickPendingIntent(R.id.clickLayout,pendingIntent)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}