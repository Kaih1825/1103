package com.extra.a1103.RemoteViewsServices

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.extra.a1103.R

class showVoInfoRemoteViewsService:RemoteViewsService() {
    override fun onGetViewFactory(p0: Intent): RemoteViewsFactory {
        return showVoInfoRemoteViewsFactory(applicationContext, p0)
    }
    class showVoInfoRemoteViewsFactory(
        private var context:Context,
        var intent: Intent
    ):RemoteViewsFactory{
        private val appWidgetId:Int
        init {
            appWidgetId=intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID)
        }
        override fun onCreate() {}

        override fun onDataSetChanged() {}

        override fun onDestroy() {}

        override fun getCount(): Int {return 3}

        override fun getViewAt(i: Int): RemoteViews {
            var rv=RemoteViews(context.packageName, R.layout.show_voinfo_view)
            val sharedPreferences=context.getSharedPreferences("getVoInfo",Context.MODE_PRIVATE)
            rv.setTextViewText(R.id.txt_voNum,(i+1).toString())
            var name=context.resources.getStringArray(R.array.voTypeArray)[sharedPreferences.getInt("voTypeSelection_${i}",0)]
            rv.setTextViewText(R.id.txt_voType,name.substring(0,name.indexOf("-")))
            rv.setTextViewText(R.id.txt_voPlace,context.resources.getStringArray(R.array.voPlaceArray)[sharedPreferences.getInt("voPlaceSelection_${i}",0)])
            rv.setTextViewText(R.id.txt_voDate,sharedPreferences.getString("voDate_${i}","2021.11.19"))
            if(i==2){
                rv.setViewVisibility(R.id.txt_error, View.VISIBLE)
                rv.setViewVisibility(R.id.txt_voDate, View.GONE)
                rv.setViewVisibility(R.id.txt_voPlace, View.GONE)
                rv.setTextColor(R.id.txt_voNum, Color.RED)
                rv.setImageViewResource(R.id.img_voIsOk, R.drawable.a8)
                rv.setTextViewText(R.id.txt_voType,"未施打")
                rv.setTextColor(R.id.txt_voType, Color.RED)
            }
            else{
                rv.setViewVisibility(R.id.txt_error, View.GONE)
                rv.setViewVisibility(R.id.txt_voDate, View.VISIBLE)
                rv.setViewVisibility(R.id.txt_voPlace, View.VISIBLE)
                rv.setTextColor(R.id.txt_voNum,context.resources.getColor(R.color.deepBlue))
                rv.setImageViewResource(R.id.img_voIsOk, R.drawable.a7)
                rv.setTextColor(R.id.txt_voType,context.resources.getColor(R.color.deepBlue))
            }
            if(i==0){
                rv.setInt(R.id.dot_1,"setBackgroundColor",context.resources.getColor(R.color.deepBlue))
                rv.setInt(R.id.dot_2,"setBackgroundColor",context.resources.getColor(R.color.grey))
                rv.setInt(R.id.dot_3,"setBackgroundColor",context.resources.getColor(R.color.grey))
            }else if(i==1){
                rv.setInt(R.id.dot_1,"setBackgroundColor",context.resources.getColor(R.color.grey))
                rv.setInt(R.id.dot_2,"setBackgroundColor",context.resources.getColor(R.color.deepBlue))
                rv.setInt(R.id.dot_3,"setBackgroundColor",context.resources.getColor(R.color.grey))
            }else if(i==2){
                rv.setInt(R.id.dot_1,"setBackgroundColor",context.resources.getColor(R.color.grey))
                rv.setInt(R.id.dot_2,"setBackgroundColor",context.resources.getColor(R.color.grey))
                rv.setInt(R.id.dot_3,"setBackgroundColor",context.resources.getColor(R.color.deepBlue))
            }
            return rv
        }

        override fun getLoadingView(): RemoteViews? {
            return null
        }

        override fun getViewTypeCount(): Int {return 1}

        override fun getItemId(p0: Int): Long {return p0.toLong()}

        override fun hasStableIds(): Boolean {return true}

    }
}