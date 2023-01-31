package com.extra.a1103.Dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.extra.a1103.Adapter.recyclerViewCustomAdapter
import com.extra.a1103.MainActivity2
import com.extra.a1103.R
import com.extra.a1103.RemoteViewsFactories.showVoListRemoteViewsService
import com.extra.a1103.RemoteViewsServices.showVoInfoRemoteViewsService
import com.extra.a1103.Widgets.ShowVoInfo2x2
import com.extra.a1103.Widgets.ShowVoInfo5x2
import com.extra.a1103.passport_home
import com.google.android.material.datepicker.*
import com.google.android.material.timepicker.*
import kotlinx.android.synthetic.main.activity_passport_home.*
import kotlinx.android.synthetic.main.list_view_layout.*
import kotlinx.android.synthetic.main.spinner_item.view.*
import kotlinx.android.synthetic.main.spinner_listview.*
import kotlinx.android.synthetic.main.spinner_listview.view.*
import kotlinx.android.synthetic.main.vo_dialog.*
import kotlinx.android.synthetic.main.vo_dialog.view.*
import spinnerAdapter
import java.text.SimpleDateFormat
import java.util.*


class voTypeDialog(context: Context, var activity: Activity,var numOfVo:Int) : Dialog(context) {
    var popupWindow:PopupWindow?=null
    var isOpen=0
    @SuppressLint("SetTextI18n", "RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.vo_dialog)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setDimAmount(0f)
        voTypeSpinner.adapter = spinnerAdapter(context,activity.resources.getStringArray(R.array.voTypeArray))
        voTypeSpinner.isClickable=false

        var sp=context.getSharedPreferences("getVoInfo",Context.MODE_PRIVATE)
        var spEdit=sp.edit()
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
        var constraintBuilder=CalendarConstraints.Builder().setValidator(DateValidatorPointBackward.before(Date().time)).setEnd(Date().time)
        var datePicker=MaterialDatePicker.Builder.customDatePicker(mydateSelection()).setTitleText(" ").setCalendarConstraints(constraintBuilder.build()).setTheme(R.style.datepicker).build()
        btn_vodate.setOnClickListener {
            var activity2 = activity as passport_home
            datePicker.show(activity2.supportFragmentManager, "")
        }
        var calender=Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        datePicker.addOnPositiveButtonClickListener {
            calender.timeInMillis=datePicker.selection!!
            var month=""
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
        var timePicker=MaterialTimePicker.Builder().setTitleText(" ").setTheme(R.style.timePicker).build()

        btn_voTime.setOnClickListener {
            var activity2=activity as passport_home
            timePicker.show(activity2.supportFragmentManager,"")
        }

        timePicker.addOnPositiveButtonClickListener {
            var hour="";var min="";var am_pm="am"
            if(timePicker.hour>12){
                hour="${timePicker.hour-12}"
                am_pm="pm"
            }
            else if(timePicker.hour==12){
                hour="12"
                am_pm="pm"
            }
            else if(timePicker.hour==0){
                hour="12"
                am_pm="am"
            }
            else{
                hour="${timePicker.hour}"
                am_pm="am"
            }

            min=timePicker.minute.toString()

            if(min.length==1){
                min= "0$min"
            }
            if(hour.length==1){
                hour="0${hour}"
            }
            btn_voTime.text="${hour}:${min} ${am_pm}"
        }

        voTypeSpinner.setSelection(sp.getInt("voTypeSelection_${numOfVo}",0))
        voCountSpinner.setSelection(sp.getInt("voCountSelection_${numOfVo}",0))
        voPlacetSpinner.setSelection(sp.getInt("voPlaceSelection_${numOfVo}",0))
        btn_vodate.text=sp.getString("voDate_${numOfVo}","2021.11.19")
        btn_voTime.text=sp.getString("voTime_${numOfVo}","11:00 AM")
        dialogBtn_confirm.setOnClickListener {
//            voName.text=voTypeSpinner.selectedItem.toString().substring(0,voTypeSpinner.selectedItem.toString().indexOf("-"))
//            hasName.text=voPlacetSpinner.selectedItem.toString()
//            date.text=btn_vodate.text.toString()
            spEdit.putInt("voTypeSelection_${numOfVo}",voTypeSpinner.selectedItemPosition)
            spEdit.putInt("voCountSelection_${numOfVo}",voCountSpinner.selectedItemPosition)
            spEdit.putInt("voPlaceSelection_${numOfVo}",voPlacetSpinner.selectedItemPosition)
            spEdit.putString("voDate_${numOfVo}",btn_vodate.text.toString())
            spEdit.putString("voTime_${numOfVo}",btn_voTime.text.toString())
            spEdit.apply()
            cancel()
            spEdit.putInt("num${numOfVo+1}IsOpen",1)
            spEdit.apply()
            val isOpen1=sp.getInt("num1IsOpen",2)
            val isOpen2=sp.getInt("num2IsOpen",2)
            val isOpen3=sp.getInt("num3IsOpen",2)
            val appWidgetManager=AppWidgetManager.getInstance(context)
            var rv=RemoteViews(context.packageName, R.layout.show_vo_info5x2)
            var listIntent= Intent(context, showVoListRemoteViewsService::class.java)
            rv.setRemoteAdapter(R.id.list,listIntent)
            var appWidgetIds = appWidgetManager.getAppWidgetIds(
                ComponentName(context, ShowVoInfo5x2::class.java)
            )
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list)
            rv=RemoteViews(context.packageName, R.layout.show_vo_info2x2)
            listIntent= Intent(context, showVoInfoRemoteViewsService::class.java)
            rv.setRemoteAdapter(R.id.list,listIntent)
            appWidgetIds = appWidgetManager.getAppWidgetIds(
                ComponentName(context, ShowVoInfo2x2::class.java)
            )
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list)

            activity.editSuccess.visibility= View.VISIBLE
            activity.dialogBackground.visibility = View.VISIBLE
            Handler().postDelayed({
                activity.editSuccess.visibility= View.INVISIBLE
                activity.dialogBackground.visibility = View.INVISIBLE
            },1000)

            activity.recyclerView.layoutManager = LinearLayoutManager(context)
            activity.recyclerView.adapter = recyclerViewCustomAdapter(
                arrayOf("1", "2","3"), arrayOf("默德納", "BNT","BNT"), arrayOf("新竹台大醫院\n新竹分院", "新竹台大醫院\n新竹分院","新竹台大醫院\n新竹分院"),
                arrayOf("2021.11.20 13:20", "2021.11.20 13:20","2021.11.20 13:20"), arrayOf(isOpen1,isOpen2,isOpen3), activity,context
            )
        }

        dialogBtn_cancel.setOnClickListener {
            cancel()
        }

        close.setOnClickListener {
            cancel()
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

@SuppressLint("RestrictedApi")
class mydateSelection: SingleDateSelector() {
    override fun getSelectionDisplayString(context: Context): String {
        var simpleDateFormat=SimpleDateFormat("EEEE,MM月,DD")
        var calendar=Calendar.getInstance()
        if(selection!=null){
            calendar.timeInMillis= selection as Long
        }
        return simpleDateFormat.format(calendar.time)
    }

}

