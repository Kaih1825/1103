package com.extra.a1103.Dialogs

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.extra.a1103.Methods.sql
import com.extra.a1103.R
import com.extra.a1103.registration_home
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CalendarConstraints.DateValidator
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.SingleDateSelector
import com.google.android.material.timepicker.MaterialTimePicker
import kotlinx.android.synthetic.main.activity_registration_home.*
import kotlinx.android.synthetic.main.add_contact_dialog_layout.*
import kotlinx.android.synthetic.main.spinner_listview.*
import kotlinx.android.synthetic.main.vo_dialog.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Vector

class addContactDialog(context: Context,var activity: Activity,var isCheck:Vector<Boolean>): Dialog(context) {
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_contact_dialog_layout)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setDimAmount(0f)
        edt_placeNum.addTextChangedListener(object : TextWatcher{
            var del=false
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                del = p3==0
            }

            override fun afterTextChanged(p0: Editable?) {
                if((edt_placeNum.text.length==4||edt_placeNum.text.length==9||edt_placeNum.text.length==14)&&!del){
                    edt_placeNum.setText(edt_placeNum.text.toString()+" ")
                    edt_placeNum.setSelection(edt_placeNum.text.length)
                }
            }

        })
        var calendarConstraints= CalendarConstraints.Builder().setValidator(DateValidatorPointBackward.before(Date().time)).build()
        var datePicker=MaterialDatePicker.Builder.customDatePicker(singleDateSelector()).setCalendarConstraints(calendarConstraints).setTitleText(" ").setTheme(R.style.datepicker).build()
        btn_date.setOnClickListener {
            var activity2=activity as registration_home
            datePicker.show(activity2.supportFragmentManager,"")
        }
        datePicker.addOnPositiveButtonClickListener {
            var calendar=Calendar.getInstance()
            calendar.timeInMillis= datePicker.selection!!
            var month=""
            month = if(calendar.get(java.util.Calendar.MONTH)+1<10){
                "0${calendar.get(java.util.Calendar.MONTH)+1}"
            } else{
                "${calendar.get(java.util.Calendar.MONTH)+1}"
            }
            var date=if(calendar.get(java.util.Calendar.DATE)<10){
                "0${calendar.get(java.util.Calendar.DATE)}"
            }
            else{
                "${calendar.get(java.util.Calendar.DATE)}"
            }
            var res="${calendar.get(java.util.Calendar.YEAR)}/${month}/${date}"
            btn_date.text=res
        }

        var timePicker=MaterialTimePicker.Builder().setTitleText(" ").setTheme(R.style.timePicker).build()
        btn_time.setOnClickListener {
            var activity2=activity as registration_home
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
            btn_time.text="${hour}:${min} ${am_pm}"
        }
        btn_addItem.setOnClickListener {
            sql.insert(context,btn_date.text.toString(),btn_time.text.toString(),edt_placeNum.text.toString())
            sql.addAllToListView(context,activity,isCheck,activity.list)
            Handler().postDelayed({
                activity.dialogBackground.visibility= View.GONE
                activity.addSuccess.visibility=View.GONE
            },2000)
            cancel()
            activity.dialogBackground.visibility= View.VISIBLE
            activity.addSuccess.visibility=View.VISIBLE
        }

        btn_cancel.setOnClickListener {
            cancel()
        }
    }
    override fun show() {
        super.show()
    }

    override fun cancel() {
        super.cancel()
        activity.dialogBackground.visibility= View.GONE
    }
}

@SuppressLint("RestrictedApi")
class singleDateSelector():SingleDateSelector(){
    override fun getSelectionDisplayString(context: Context): String {
        var format=SimpleDateFormat("EEEE,MM月,DD")
        var calendar=Calendar.getInstance()
        if(selection!=null){
            calendar.timeInMillis=selection as Long
        }
        return format.format(calendar.time)
    }
}