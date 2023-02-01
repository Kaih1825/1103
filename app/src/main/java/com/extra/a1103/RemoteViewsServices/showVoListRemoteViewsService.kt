package com.extra.a1103.RemoteViewsFactories

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.extra.a1103.R

class showVoListRemoteViewsService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return showVoListRemoteViewsFaactory(
            applicationContext, intent
        )
    }

    internal inner class showVoListRemoteViewsFaactory(
        private val context: Context,
        intent: Intent
    ) :
        RemoteViewsFactory {
        private val appWidgetId: Int

        init {
            appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }

        override fun onCreate() {}
        override fun onDataSetChanged() {}
        override fun onDestroy() {}
        override fun getCount(): Int {
            return 3
        }

        @SuppressLint("RemoteViewLayout")
        override fun getViewAt(i: Int): RemoteViews {
            val sharedPreferences=context.getSharedPreferences("getVoInfo",Context.MODE_PRIVATE)
            val rv=RemoteViews(context.packageName, R.layout.show_vo_listview_layout)
            var intent=Intent().putExtra("editButton5x2",i)
            rv.setOnClickFillInIntent(R.id.layoutClick,intent)
            rv.setTextViewText(R.id.txt_voNum,(i+1).toString())
            var name=resources.getStringArray(R.array.voTypeArray)[sharedPreferences.getInt("voTypeSelection_${i}",0)]
            rv.setTextViewText(R.id.txt_voType,name.substring(0,name.indexOf("-")))
            rv.setTextViewText(R.id.txt_voPlace,resources.getStringArray(R.array.voPlaceArray)[sharedPreferences.getInt("voPlaceSelection_${i}",0)])
            rv.setTextViewText(R.id.txt_voDate,sharedPreferences.getString("voDate2_${i}","2021/11/19")+" "+sharedPreferences.getString("voTime2_${i}","11:00"))
            if(sharedPreferences.getInt("num${i+1}IsOpen",2)==2){
                rv.setViewVisibility(R.id.txt_error,View.VISIBLE)
                rv.setViewVisibility(R.id.txt_voType,View.GONE)
                rv.setViewVisibility(R.id.txt_voDate,View.GONE)
                rv.setViewVisibility(R.id.txt_voPlace,View.GONE)
                rv.setTextColor(R.id.txt_voNum,Color.RED)
                rv.setImageViewResource(R.id.img_voIsOk, R.drawable.a8)
                rv.setTextViewText(R.id.txt_voIsOk,"未施打")
                rv.setTextViewText(R.id.txt_error,"請安排時間\n施打第${i+1}劑")
                rv.setTextColor(R.id.txt_voIsOk,Color.RED)
            }
            else{
                rv.setViewVisibility(R.id.txt_error,View.GONE)
                rv.setViewVisibility(R.id.txt_voType,View.VISIBLE)
                rv.setViewVisibility(R.id.txt_voDate,View.VISIBLE)
                rv.setViewVisibility(R.id.txt_voPlace,View.VISIBLE)
                rv.setTextColor(R.id.txt_voNum,resources.getColor(R.color.deepBlue))
                rv.setImageViewResource(R.id.img_voIsOk, R.drawable.a7)
                rv.setTextViewText(R.id.txt_voIsOk,"已施打")
                rv.setTextColor(R.id.txt_voIsOk,resources.getColor(R.color.deepBlue))
            }
            return rv
        }

        override fun getLoadingView(): RemoteViews? {
            return null
        }

        override fun getViewTypeCount(): Int {
            return 1
        }

        override fun getItemId(i: Int): Long {
            return i.toLong()
        }

        override fun hasStableIds(): Boolean {
            return true
        }
    }
}