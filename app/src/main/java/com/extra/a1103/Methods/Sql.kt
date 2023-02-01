package com.extra.a1103.Methods

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.database.Cursor
import java.util.Vector
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.openDatabase
import android.database.sqlite.SQLiteDatabase.openOrCreateDatabase
import android.widget.ListView
import android.widget.RemoteViews
import com.extra.a1103.Adapters.registrationListAdapter
import com.extra.a1103.R
import com.extra.a1103.RemoteViewsServices.showRegistrationInfoRemoteViewsServices
import com.extra.a1103.RemoteViewsServices.showRegistrationInfoServices
import com.extra.a1103.Widgets.ShowRegistrationInfo2x2
import com.extra.a1103.Widgets.ShowRegistrationInfo5x4

class sql {
    companion object {
        var db: SQLiteDatabase? =null
        fun init(context: Context){
            try{
                db= context.openOrCreateDatabase("db.db", MODE_PRIVATE,null)
                db!!.execSQL("CREATE TABLE sql(id INTEGER PRIMARY KEY,date TEXT,time TEXT,placeNum TEXT)")
            }catch (ex:java.lang.Exception){}
        }

        fun insert(context: Context,date:String,time:String,placeNum:String){
            init(context)
            db!!.execSQL("INSERT INTO sql(date,time,placeNum) VALUES('${date}','${time}','${placeNum}')")
            updateWidget(context)
        }

        fun delete(context: Context,id:String){
            init(context)
            db!!.execSQL("DELETE FROM sql WHERE id=${id}")
            updateWidget(context)
        }

        fun clearSql(context: Context){
            init(context)
            db!!.execSQL("DROP TABLE sql")
            init(context)
            updateWidget(context)
        }

        fun count(context: Context): Int {
            init(context)
            var cursor=db!!.rawQuery("SELECT COUNT(*) FROM sql",null)
            cursor.moveToNext()
            return cursor.getInt(0)
        }

        fun searchAll(context: Context):Vector<Vector<String>>{
            init(context)
            var cursor=db!!.rawQuery("SELECT * FROM sql ORDER BY date DESC",null)
            cursor.moveToFirst()
            var root=Vector<Vector<String>>()
            var id=Vector<String>()
            var date=Vector<String>()
            var time=Vector<String>()
            var placeNum=Vector<String>()
            for(i in 0 until cursor.count){
                id.add(cursor.getInt(0).toString())
                date.add(cursor.getString(1))
                time.add(cursor.getString(2))
                placeNum.add(cursor.getString(3))
                cursor.moveToNext()
            }
            root.add(id)
            root.add(date)
            root.add(time)
            root.add(placeNum)
            return root
        }

        fun addAllToListView(context: Context,activity: Activity,isCheck:Vector<Boolean>,listView:ListView){
            var root= searchAll(context)
            var id=root[0]
            var date=root[1]
            var time=root[2]
            var placeNum=root[3]
            for(i in 0 until date.count()){
                isCheck.add(false)
            }
            listView.adapter=registrationListAdapter(context,activity,isCheck,id,date, time, placeNum)
        }

        fun searchAllByCursor(context: Context): Cursor? {
            init(context)
            var cursor=db!!.rawQuery("SELECT * FROM sql ORDER BY date DESC",null)
            return cursor
        }

        fun searchByDate(context: Context,date: String):Vector<Vector<String>>{
            init(context)
            var cursor=db!!.rawQuery("SELECT * FROM sql WHERE date='${date}' ORDER BY date DESC",null)
            cursor.moveToFirst()
            var root=Vector<Vector<String>>()
            var id=Vector<String>()
            var date=Vector<String>()
            var time=Vector<String>()
            var placeNum=Vector<String>()
            for(i in 0 until cursor.count){
                id.add(cursor.getInt(0).toString())
                date.add(cursor.getString(1))
                time.add(cursor.getString(2))
                placeNum.add(cursor.getString(3))
                cursor.moveToNext()
            }
            root.add(id)
            root.add(date)
            root.add(time)
            root.add(placeNum)
            return root
        }

        fun addResToListView(context: Context,activity: Activity,date: String,isCheck:Vector<Boolean>,listView:ListView){
            var root= searchByDate(context,date)
            var id=root[0]
            var date=root[1]
            var time=root[2]
            var placeNum=root[3]
            isCheck.clear()
            for(i in 0 until date.count()){
                isCheck.add(false)
            }
            listView.adapter=registrationListAdapter(context,activity,isCheck,id,date, time, placeNum)
        }

        fun updateWidget(context: Context){
            var appWidgetManager= AppWidgetManager.getInstance(context)
            var rv=RemoteViews(context.packageName, R.layout.show_registration_info5x4)
            var listIntent=Intent(context,showRegistrationInfoRemoteViewsServices::class.java)
            rv.setRemoteAdapter(R.id.list,listIntent)
            var appWidgetId=appWidgetManager.getAppWidgetIds(ComponentName(context,ShowRegistrationInfo5x4::class.java))
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,R.id.list)
            appWidgetManager= AppWidgetManager.getInstance(context)
            rv=RemoteViews(context.packageName, R.layout.show_registration_info2x2)
            listIntent=Intent(context,showRegistrationInfoServices::class.java)
            rv.setRemoteAdapter(R.id.list,listIntent)
            appWidgetId=appWidgetManager.getAppWidgetIds(ComponentName(context,
                ShowRegistrationInfo2x2::class.java))
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,R.id.list)
        }
    }
}