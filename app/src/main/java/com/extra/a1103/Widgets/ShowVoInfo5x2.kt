package com.extra.a1103.Widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import androidx.core.content.ContextCompat.startActivity
import com.extra.a1103.R
import com.extra.a1103.RemoteViewsFactories.showVoListRemoteViewsService
import com.extra.a1103.passport_home

/**
 * Implementation of App Widget functionality.
 */
class ShowVoInfo5x2 : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget_showVoInfo(context, appWidgetManager, appWidgetId)
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
            if(intent.action.equals("com.appwidget.action.click.editInfo")){
                val intent=Intent(context,passport_home::class.java)
                intent.flags=FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("action","startInfoDialog")
                startActivity(context!!,intent,null)
            }
            if(intent.action.equals("com.appwidget.action.click.editVo5x2")){
                Log.e("Status", "onReceive", )
                var extra=intent.getIntExtra("editButton5x2",-1)
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

internal fun updateAppWidget_showVoInfo(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.show_vo_info5x2)
    val editIntent=Intent(context,ShowVoInfo5x2::class.java)
    editIntent.action="com.appwidget.action.click.editInfo"
    val pendingIntent= PendingIntent.getBroadcast(context,0,editIntent,0)
    views.setOnClickPendingIntent(R.id.btn_edit,pendingIntent)
    views.setOnClickPendingIntent(R.id.btn_edit2,pendingIntent)
    val sharedPreferences=context.getSharedPreferences("personalInfo",Context.MODE_PRIVATE)
    views.setTextViewText(R.id.txt_name,sharedPreferences.getString("cnName", "王曉明"))
    val listIntent=Intent(context,showVoListRemoteViewsService::class.java)
    views.setRemoteAdapter(R.id.list,listIntent)

    var intent=Intent(context, ShowVoInfo5x2::class.java)
    intent.action = "com.appwidget.action.click.editVo5x2"
    var pendingIntent2= PendingIntent.getBroadcast(context,0,intent,0)
    views.setPendingIntentTemplate(R.id.list,pendingIntent2)



    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,R.id.list)
}