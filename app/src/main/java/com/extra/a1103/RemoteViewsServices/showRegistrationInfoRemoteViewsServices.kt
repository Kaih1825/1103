package com.extra.a1103.RemoteViewsServices

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.extra.a1103.Methods.sql
import com.extra.a1103.R

class showRegistrationInfoRemoteViewsServices: RemoteViewsService() {
    override fun onGetViewFactory(p0: Intent): RemoteViewsFactory {
        return showRegistrationInfoRemoteViewsFactory(applicationContext)
    }

    class showRegistrationInfoRemoteViewsFactory(
        private var context: Context,
    ):RemoteViewsFactory{
        override fun onCreate() {}

        override fun onDataSetChanged() {}

        override fun onDestroy() {}

        override fun getCount(): Int {return sql.count(context)}

        override fun getViewAt(p0: Int): RemoteViews {
            var rv=RemoteViews(context.packageName, R.layout.show_registration_list_layout)
            var cursor=sql.searchAllByCursor(context)!!
            cursor.moveToFirst()
            cursor.move(p0)
            rv.setTextViewText(R.id.txt_date,cursor.getString(1))
            rv.setTextViewText(R.id.txt_time,cursor.getString(2))
            rv.setTextViewText(R.id.txt_voPlace,cursor.getString(3))
            var intent=Intent()
            intent.putExtra("delRegId",cursor.getInt(0).toString())
            intent.putExtra("position",p0)
            rv.setOnClickFillInIntent(R.id.btn_del,intent)
            return rv
        }

        override fun getLoadingView(): RemoteViews? {return null}

        override fun getViewTypeCount(): Int {return 1}

        override fun getItemId(p0: Int): Long {return p0.toLong()}

        override fun hasStableIds(): Boolean {return true}

    }

}