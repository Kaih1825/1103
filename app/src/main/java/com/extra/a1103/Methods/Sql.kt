package com.extra.a1103.Methods

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import java.util.Vector
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.openDatabase
import android.database.sqlite.SQLiteDatabase.openOrCreateDatabase
import android.widget.ListView
import com.extra.a1103.Adapters.registrationListAdapter

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
        }

        fun delete(context: Context,id:String){
            init(context)
            db!!.execSQL("DELETE FROM sql WHERE id=${id}")
        }

        fun clearSql(context: Context){
            init(context)
            db!!.execSQL("DROP TABLE sql")
            init(context)
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
    }
}