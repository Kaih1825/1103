package com.extra.a1103.Widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.extra.a1103.R
import com.extra.a1103.RemoteViewsServices.showVoInfoRemoteViewsService
import com.extra.a1103.passport_home
import com.extra.a1103.registration_home

/**
 * Implementation of App Widget functionality.
 */
class ShowVoInfo2x2 : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget_showVoInfo_2x2(context, appWidgetManager, appWidgetId)
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
            if(intent.action.equals("com.appwidget.action.click.editVo")){
                var extra=intent.getIntExtra("editButton",-1)
                if(extra==0){
                    var goIntent=Intent(context,passport_home::class.java)
                    goIntent.flags= Intent.FLAG_ACTIVITY_NEW_TASK
                    goIntent.putExtra("editVoInfo","1")
                    startActivity(context!!,goIntent,null)
                }else if(extra==1){
                    var goIntent=Intent(context,passport_home::class.java)
                    goIntent.flags= Intent.FLAG_ACTIVITY_NEW_TASK
                    goIntent.putExtra("editVoInfo","2")
                    startActivity(context!!,goIntent,null)
                }else if(extra==2){
                    var goIntent=Intent(context,passport_home::class.java)
                    goIntent.flags= Intent.FLAG_ACTIVITY_NEW_TASK
                    goIntent.putExtra("editVoInfo","3")
                    startActivity(context!!,goIntent,null)
                }
            }
        }
    }
}

internal fun updateAppWidget_showVoInfo_2x2(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.show_vo_info2x2)
    val listIntent=Intent(context,showVoInfoRemoteViewsService::class.java)
    views.setRemoteAdapter(R.id.list,listIntent)
    var intent=Intent(context, ShowVoInfo2x2::class.java)
    intent.action = "com.appwidget.action.click.editVo"
    var pendingIntent= PendingIntent.getBroadcast(context,0,intent,0)
    views.setPendingIntentTemplate(R.id.list,pendingIntent)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}