package com.extra.a1103

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.core.view.drawToBitmap
import coil.load
import coil.transform.BlurTransformation
import com.extra.a1103.Adapters.registrationListAdapter
import com.extra.a1103.Dialog.mydateSelection
import com.extra.a1103.Dialogs.addContactDialog
import com.extra.a1103.Methods.sql
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.SingleDateSelector
import kotlinx.android.synthetic.main.activity_registration_home.*
import kotlinx.android.synthetic.main.activity_registration_home.dialogBackground
import kotlinx.android.synthetic.main.add_contact_dialog_layout.*
import kotlinx.android.synthetic.main.registration_listview_layout.view.*
import kotlinx.android.synthetic.main.vo_dialog.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log

class registration_home : AppCompatActivity() {
    @SuppressLint("RestrictedApi")
    var isCheck=Vector<Boolean>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_home)
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
            navigationBarColor = Color.TRANSPARENT
        }
        var constraintBuilder=
            CalendarConstraints.Builder().setValidator(DateValidatorPointBackward.before(Date().time)).setEnd(
                Date().time)
        var datePicker=
            MaterialDatePicker.Builder.customDatePicker(mydateSelection()).setTitleText(" ").setCalendarConstraints(constraintBuilder.build()).setTheme(R.style.datepicker).build()
        btn_keyword.setOnClickListener {
            datePicker.show(this.supportFragmentManager,"")
        }
        var calender=Calendar.getInstance()
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
            var output="${calender.get(Calendar.YEAR)}/${month}/${day}"
            btn_keyword.text=output
            btn_keyword.setTextColor(resources.getColor(R.color.green))
        }

        sql.addAllToListView(this,this,isCheck,list)
        btn_add.setOnClickListener {
            addContactDialog(this,this, isCheck).show()
            setBlurBackground()
        }
        btn_back.setOnClickListener{
            finish()
        }

        test.setOnClickListener {
            sql.clearSql(this)
        }

        btn_del.setOnClickListener {
            bottomNavigationView.visibility=View.GONE
            btn_scan.visibility=View.GONE
            bottomNavigationDeleteView.visibility=View.VISIBLE

        }

        btn_delCancel.setOnClickListener {
            bottomNavigationView.visibility=View.VISIBLE
            btn_scan.visibility=View.VISIBLE
            bottomNavigationDeleteView.visibility=View.GONE

        }

        btn_delConfirm.setOnClickListener {
            askDialog.visibility=View.VISIBLE
            txt_askTitle.text="確認要刪除\n以勾選項目?"
            btn_dialogDelConfirm.setOnClickListener {
                btn_keyword.text="請選擇日期"
                btn_keyword.setTextColor(resources.getColor(R.color.grey))
                txt_search.text="搜尋"
                txt_search.setTextColor(resources.getColor(R.color.green))
                bottomNavigationView.visibility=View.VISIBLE
                btn_scan.visibility=View.VISIBLE
                bottomNavigationDeleteView.visibility=View.GONE
                for(i in 0 until isCheck.count()){
                    if(isCheck[i]){
                        sql.delete(this,list.adapter.getItem(i).toString())
                    }
                }
                sql.addAllToListView(this,this,isCheck,list)
                for(i in 0 until isCheck.count()){
                    isCheck[i]=false
                }
                delSuccess.visibility=View.VISIBLE
                askDialog.visibility=View.GONE
                android.os.Handler().postDelayed({
                    delSuccess.visibility=View.GONE
                },2000)
            }
            btn_dialogCancelConfirm.setOnClickListener {
                askDialog.visibility=View.GONE
            }
        }

        txt_search.setOnClickListener{
            if(txt_search.text=="搜尋"){
                isCheck=Vector<Boolean>()
                sql.addResToListView(this,this,btn_keyword.text.toString(),isCheck,list)
                txt_search.text="取消"
                txt_search.setTextColor(resources.getColor(R.color.grey))
            }
            else if(txt_search.text=="取消"){
                btn_keyword.text="請選擇日期"
                btn_keyword.setTextColor(resources.getColor(R.color.grey))
                txt_search.text="搜尋"
                txt_search.setTextColor(resources.getColor(R.color.green))
                //取消
                sql.addAllToListView(this,this,isCheck,list)
            }
        }

        btn_scan.setOnClickListener {
            var intent=Intent(this,scan_contact::class.java)
            startActivity(intent)
        }

    }
    fun setBlurBackground() {
        val fullbitmap = window.decorView.drawToBitmap()
        //取得手機長、寬
        val phoneWidth = windowManager.defaultDisplay.width
        val phoneHeight = windowManager.defaultDisplay.height
        val bitmap = Bitmap.createBitmap(
            fullbitmap, 0, 0, phoneWidth, phoneHeight
        )
        dialogBackgroundImage.load(bitmap) {
            transformations(
                BlurTransformation(
                    context = this@registration_home,
                    radius = 25f,
                    sampling = 5f
                )
            )
        }
        dialogBackground.visibility=View.VISIBLE
    }

    override fun onRestart() {
        super.onRestart()
        sql.addAllToListView(this,this,isCheck,list)
    }
}

@SuppressLint("RestrictedApi")
class mydateSelection: SingleDateSelector() {
    override fun getSelectionDisplayString(context: Context): String {
        var simpleDateFormat= SimpleDateFormat("EEEE,MM月,DD")
        var calendar=Calendar.getInstance()
        if(selection!=null){
            calendar.timeInMillis= selection as Long
        }
        return simpleDateFormat.format(calendar.time)
    }

}
