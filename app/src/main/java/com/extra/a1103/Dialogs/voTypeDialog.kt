package com.extra.a1103.Dialog

import android.app.ActionBar.LayoutParams
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.PopupWindow
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.extra.a1103.Adapter.recyclerViewCustomAdapter
import com.extra.a1103.R
import com.extra.a1103.passport_home
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import kotlinx.android.synthetic.main.activity_passport_home.*
import kotlinx.android.synthetic.main.list_view_layout.*
import kotlinx.android.synthetic.main.spinner_item.view.*
import kotlinx.android.synthetic.main.spinner_listview.*
import kotlinx.android.synthetic.main.spinner_listview.view.*
import kotlinx.android.synthetic.main.vo_dialog.*
import kotlinx.android.synthetic.main.vo_dialog.view.*
import spinnerAdapter
import java.util.*


class voTypeDialog(context: Context, var activity: Activity,var numOfVo:Int) : Dialog(context) {
    var popupWindow:PopupWindow?=null
    var isOpen=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.vo_dialog)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setDimAmount(0f)
        voTypeSpinner.adapter = spinnerAdapter(context,activity.resources.getStringArray(R.array.voTypeArray))
        voTypeSpinner.isClickable=false
        voTypeSpinner.setOnTouchListener(object :View.OnTouchListener{
            override fun onTouch(p0: View?, p1: MotionEvent?):Boolean {
                when (p1?.action) {
                    MotionEvent.ACTION_UP -> run {
                        Log.e("spinner", "close")
//                        voTypeSpinner.setSelection(1)
//                        voTypeSpinner.setBackgroundDrawable(activity.resources.getDrawable(R.drawable.spinner_bac_down))
                        var array=activity.resources.getStringArray(R.array.voTypeArray)
                        if(isOpen==1){
                            popupWindow!!.dismiss()
                        }
                        else{
                            showPopup(context,voTypeSpinner,array)
                        }
                    }
                }
                return true
            }
        })

        voCountSpinner.adapter=spinnerAdapter(context, activity.resources.getStringArray(R.array.voCountArray))
        voCountSpinner.isClickable=false
        voCountSpinner.setOnTouchListener(object :View.OnTouchListener{
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                if(p1?.action==MotionEvent.ACTION_UP){
                    showPopup(context,voCountSpinner, activity.resources.getStringArray(R.array.voCountArray))
                }
                return true
            }
        })

        voPlacetSpinner.adapter=spinnerAdapter(context, activity.resources.getStringArray(R.array.voPlaceArray))
        voPlacetSpinner.isClickable=false
        voPlacetSpinner.setOnTouchListener(object :View.OnTouchListener{
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                if(p1?.action==MotionEvent.ACTION_UP){
                    showPopup(context,voPlacetSpinner, activity.resources.getStringArray(R.array.voPlaceArray))
                }
                return true
            }
        })
        var datePicker=MaterialDatePicker.Builder.datePicker().setTheme(R.style.datepicker).build()

        btn_vodate.setOnClickListener {
            var activity2 = activity as passport_home
            datePicker.show(activity2.supportFragmentManager, "")
        }
        var calender=Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        datePicker.addOnPositiveButtonClickListener {
            calender.timeInMillis=datePicker.selection!!
            var month="";
            if(calender.get(Calendar.MONTH)+1<10){
                month="0${calender.get(Calendar.MONTH)+1}"
            }
            else{
                month=(calender.get(Calendar.MONTH)+1).toString()
            }
            var day=""
            if(calender.get(Calendar.DATE)<10){
                day="0${calender.get(Calendar.DATE)}"
            }
            else{
                day=calender.get(Calendar.DATE).toString()
            }
            var output="${calender.get(Calendar.YEAR)}.${month}.${day}"
            btn_vodate.text=output
        }
        var timePicker=MaterialTimePicker.Builder().setTheme(R.style.timePicker).build()

        btn_voTime.setOnClickListener {
            var activity2=activity as passport_home
            timePicker.show(activity2.supportFragmentManager,"")
        }

    }

    override fun show() {
        super.show()
    }

    override fun cancel() {
        super.cancel()
        activity.dialogBackground.visibility = View.INVISIBLE
    }

    fun showPopup(context: Context,spinner:Spinner,array: Array<String>){
        var view=LayoutInflater.from(context).inflate(R.layout.spinner_listview,null)
        popupWindow=PopupWindow(view,spinner.width-20.toPx,200.toPx)
        popupWindow!!.isOutsideTouchable=true
        popupWindow!!.setBackgroundDrawable(activity.getDrawable(R.drawable.popup_bac))
        view.listView.adapter=adapter(context,array)
        popupWindow!!.elevation=10f
        spinner.setBackgroundDrawable(activity.resources.getDrawable(R.drawable.spinner_bac_down))
        isOpen=1
        popupWindow!!.showAsDropDown(spinner,10.toPx,0)
        popupWindow!!.setOnDismissListener {
            spinner.setBackgroundDrawable(activity.resources.getDrawable(R.drawable.spinner_bac))
            isOpen=0
        }
        view.listView.setOnItemClickListener { adapterView, view, i, l -> run{
            spinner.setSelection(i)
            popupWindow!!.dismiss()
        } }
    }

    val Int.toPx: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()
}

class adapter(var context: Context,var array:Array<String>): BaseAdapter() {
    override fun getCount(): Int {
        return array.count()
    }

    override fun getItem(p0: Int): Any {
        return array[p0]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view=LayoutInflater.from(context).inflate(R.layout.spinner_item,p2,false)
        view.itemText.text=array[p0]
        return view
    }

}


