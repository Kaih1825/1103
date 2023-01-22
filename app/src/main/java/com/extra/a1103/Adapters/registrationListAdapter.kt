package com.extra.a1103.Adapters

import android.annotation.SuppressLint
import android.app.ActionBar.LayoutParams
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.extra.a1103.R
import com.extra.a1103.registration_home
import kotlinx.android.synthetic.main.registration_listview_layout.view.*
import java.net.Authenticator.RequestorType
import java.util.Vector

class registrationListAdapter(var context:Context,var activity: Activity,var isCheck:Vector<Boolean>):BaseAdapter(){

    val Int.toPx: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()
    override fun getCount(): Int {
        return isCheck.count()
    }

    override fun getItem(p0: Int): Any {
        return isCheck
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    @SuppressLint("ViewHolder", "ClickableViewAccessibility")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val v=LayoutInflater.from(context).inflate(R.layout.registration_listview_layout,null)
        v.listLayout.setOnTouchListener(object :View.OnTouchListener{
            var dx=0f
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                when(p1?.action){
                    MotionEvent.ACTION_DOWN ->run {
                        dx=p1!!.x
                    }
                    MotionEvent.ACTION_UP->{
                        dx=p1!!.x-dx
                        Log.e("dx", dx.toString() )
                        if(dx>50.toPx){
                            v.btn_ckb.visibility=View.VISIBLE
                            v.btn_del.visibility=View.GONE
                        }
                        if(dx<((-50).toPx)){
                            v.btn_ckb.visibility=View.GONE
                            v.btn_del.visibility=View.VISIBLE
                        }
                    }
                }
                return true
            }

        })
        if(isCheck[p0]){
            v.btn_ckb.background=activity.resources.getDrawable(R.drawable.a22)

        }
        else{
            v.btn_ckb.background=activity.resources.getDrawable(R.drawable.a23)
        }
        v.btn_ckb.setOnClickListener {
            if(!isCheck[p0]){
                v.btn_ckb.background=activity.resources.getDrawable(R.drawable.a22)

            }
            else{
                v.btn_ckb.background=activity.resources.getDrawable(R.drawable.a23)
            }
            isCheck[p0]=!isCheck[p0]
        }
        return v
    }

}

