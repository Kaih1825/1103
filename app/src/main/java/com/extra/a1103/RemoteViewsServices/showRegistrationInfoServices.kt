package com.extra.a1103.RemoteViewsServices

import android.content.Context
import android.content.Intent
import android.media.session.MediaSessionManager.RemoteUserInfo
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.extra.a1103.Methods.sql
import com.extra.a1103.R

class showRegistrationInfoServices:RemoteViewsService() {
    override fun onGetViewFactory(p0: Intent): RemoteViewsFactory {
        return showRegistrationInfoFactory(applicationContext)
    }
    class showRegistrationInfoFactory(
        private val context: Context
    ):RemoteViewsFactory{
        override fun onCreate() {}

        override fun onDataSetChanged() {}

        override fun onDestroy() {}

        override fun getCount(): Int {return 5}

        override fun getViewAt(i: Int): RemoteViews {
            var rv=RemoteViews(context.packageName,R.layout.show_reginfo_view)
            when (i) {
                0 -> {
                    rv.setImageViewResource(R.id.dot_1,R.drawable.cir_green)
                    rv.setImageViewResource(R.id.dot_2,R.drawable.cir)
                    rv.setImageViewResource(R.id.dot_3,R.drawable.cir)
                    rv.setImageViewResource(R.id.dot_4,R.drawable.cir)
                    rv.setImageViewResource(R.id.dot_5,R.drawable.cir)
                }
                1 -> {
                    rv.setImageViewResource(R.id.dot_1,R.drawable.cir)
                    rv.setImageViewResource(R.id.dot_2,R.drawable.cir_green)
                    rv.setImageViewResource(R.id.dot_3,R.drawable.cir)
                    rv.setImageViewResource(R.id.dot_4,R.drawable.cir)
                    rv.setImageViewResource(R.id.dot_5,R.drawable.cir)
                }
                2 -> {
                    rv.setImageViewResource(R.id.dot_1,R.drawable.cir)
                    rv.setImageViewResource(R.id.dot_2,R.drawable.cir)
                    rv.setImageViewResource(R.id.dot_3,R.drawable.cir_green)
                    rv.setImageViewResource(R.id.dot_4,R.drawable.cir)
                    rv.setImageViewResource(R.id.dot_5,R.drawable.cir)
                }
                3 -> {
                    rv.setImageViewResource(R.id.dot_1,R.drawable.cir)
                    rv.setImageViewResource(R.id.dot_2,R.drawable.cir)
                    rv.setImageViewResource(R.id.dot_3,R.drawable.cir)
                    rv.setImageViewResource(R.id.dot_4,R.drawable.cir_green)
                    rv.setImageViewResource(R.id.dot_5,R.drawable.cir)
                }
                4 -> {
                    rv.setImageViewResource(R.id.dot_1,R.drawable.cir)
                    rv.setImageViewResource(R.id.dot_2,R.drawable.cir)
                    rv.setImageViewResource(R.id.dot_3,R.drawable.cir)
                    rv.setImageViewResource(R.id.dot_4,R.drawable.cir)
                    rv.setImageViewResource(R.id.dot_5,R.drawable.cir_green)
                }
            }
            try{
                var cursor= sql.searchAllByCursor(context)!!
                cursor.moveToFirst()
                cursor.move(i)
                rv.setTextViewText(R.id.txt_date,cursor.getString(1))
                rv.setTextViewText(R.id.txt_time,cursor.getString(2))
                rv.setTextViewText(R.id.txt_voPlace,cursor.getString(3))
            }catch (ex:java.lang.Exception){
                rv.setTextViewText(R.id.txt_date,"無資料")
                rv.setTextViewText(R.id.txt_time,"")
                rv.setTextViewText(R.id.txt_voPlace,"")
            }

            var intent=Intent()
            rv.setOnClickFillInIntent(R.id.btn_add,intent)
            return rv
        }

        override fun getLoadingView(): RemoteViews? {return null}

        override fun getViewTypeCount(): Int {return 1}

        override fun getItemId(p0: Int): Long {return p0.toLong()}

        override fun hasStableIds(): Boolean {return true}

    }
}