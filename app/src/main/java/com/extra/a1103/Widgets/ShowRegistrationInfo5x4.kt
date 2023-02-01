package com.extra.a1103.Widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.util.Log
import android.widget.RemoteViews
import androidx.core.content.ContextCompat.startActivity
import com.extra.a1103.R
import com.extra.a1103.RemoteViewsServices.showRegistrationInfoRemoteViewsServices
import com.extra.a1103.RemoteViewsServices.showVoInfoRemoteViewsService
import com.extra.a1103.registration_home

/**
 * Implementation of App Widget functionality.
 */
class ShowRegistrationInfo5x4 : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget_show_registration(context, appWidgetManager, appWidgetId)
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
                var intent=Intent(context,registration_home::class.java)
                intent.flags=FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("action","startAddVoDialog")
                startActivity(context!!,intent,null)
            }
            if(intent.action.equals("com.appwidget.action.click.delRe")){
                var intent2=Intent(context,registration_home::class.java)
                intent2.flags=FLAG_ACTIVITY_NEW_TASK
                intent2.putExtra("action","delRes")
                intent2.putExtra("id",intent.getStringExtra("delRegId"))
                intent2.putExtra("position",intent.getIntExtra("position",-1))
                startActivity(context!!,intent2,null)
            }
        }
    }
}

internal fun updateAppWidget_show_registration(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.show_registration_info5x4)
    var listIntent=Intent(context, showRegistrationInfoRemoteViewsServices::class.java)
    views.setRemoteAdapter(R.id.list,listIntent)
    var intent=Intent(context,ShowRegistrationInfo5x4::class.java)
    intent.action="com.appwidget.action.click.addRe"
    var pendingIntent=PendingIntent.getBroadcast(context,0,intent,0)
    views.setOnClickPendingIntent(R.id.btn_add,pendingIntent)

    var intent2=Intent(context,ShowRegistrationInfo5x4::class.java)
    intent2.action="com.appwidget.action.click.delRe"
    var pendingIntent2=PendingIntent.getBroadcast(context,0,intent2,0)
    views.setPendingIntentTemplate(R.id.list,pendingIntent2)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}