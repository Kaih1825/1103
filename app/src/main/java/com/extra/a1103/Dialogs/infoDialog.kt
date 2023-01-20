package com.extra.a1103.Dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.extra.a1103.R
import com.extra.a1103.passport_home
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.activity_passport_home.*
import kotlinx.android.synthetic.main.dialog.*
import java.util.*

class infoDialog(context: Context, var activity: Activity) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setDimAmount(0f)
        var sharedPreferences = context.getSharedPreferences("personalInfo", Context.MODE_PRIVATE)
        var sharedPreferencesEdit = sharedPreferences.edit()
        edt_cnName.hint = sharedPreferences.getString("cnName", "王曉明")
        edt_enName.hint = sharedPreferences.getString("enName", "Xiao Ming")
        btn_birdate.text = sharedPreferences.getString(
            "birYear",
            "1996"
        ) + "/" + sharedPreferences.getString("birMonth", "05") + "/" + sharedPreferences.getString(
            "birDay",
            "18"
        )
        var text=""
        edt_cnName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(originalText: CharSequence?, start: Int, count: Int, after: Int) {
                if(edt_cnName.text.length==15){
                    text=originalText.toString()
                }
            }

            override fun onTextChanged(onText: CharSequence?, start: Int, before: Int, count: Int) {
                if(edt_cnName.text.length>15){
                    edt_cnName.error="上限為15字元"
                    edt_cnName.setText(text)
                    edt_cnName.setSelection(edt_cnName.text.length)
                }
            }

            override fun afterTextChanged(afterText: Editable?) {

            }
        })
        var text2=""
        edt_enName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(originalText: CharSequence?, start: Int, count: Int, after: Int) {
                if(edt_enName.text.length==30){
                    text2=originalText.toString()
                }
            }

            override fun onTextChanged(onText: CharSequence?, start: Int, before: Int, count: Int) {
                if(edt_enName.text.length>30){
                    edt_enName.error="上限為30字元"
                    edt_enName.setText(text2)
                    edt_enName.setSelection(edt_enName.text.length)
                }
            }

            override fun afterTextChanged(afterText: Editable?) {

            }
        })
        edt_id.hint = sharedPreferences.getString("id", "J1236456789")
        edt_cardId1.hint = sharedPreferences.getString("cardId1", "0000")
        edt_cardId2.hint = sharedPreferences.getString("cardId2", "1234")
        edt_cardId3.hint = sharedPreferences.getString("cardId3", "5678")
        edt_cardId4.hint = sharedPreferences.getString("cardId4", "4321")
        genderSwitch.isChecked = sharedPreferences.getBoolean("gender", false)
        if (genderSwitch.isChecked) {
            txtWomen.setTextColor(activity.resources.getColor(R.color.lightBlue))
            txtMen.setTextColor(activity.resources.getColor(R.color.lightBlueGrey))
        } else {
            txtMen.setTextColor(activity.resources.getColor(R.color.lightBlue))
            txtWomen.setTextColor(activity.resources.getColor(R.color.lightBlueGrey))
        }
        close.setOnClickListener {
            activity.dialogBackground.visibility = View.INVISIBLE
            super.cancel()
        }

        var dialog = MaterialDatePicker.Builder.datePicker()
            .setTitleText("")
            .setTheme(R.style.datepicker)
            .build()
        var calender = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        var year: String = "1996"
        var month: String = "05"
        var day: String = "18"
        var btDayHaveChange = false
        btn_birdate.setOnClickListener {
            var activity2 = activity as passport_home
            dialog.show(activity2.supportFragmentManager, "")
        }
        dialog.addOnPositiveButtonClickListener {
            calender.timeInMillis = dialog.selection!!
            year = "${calender.get(Calendar.YEAR)}"
            month = "${calender.get(Calendar.MONTH) + 1}"
            day = "${calender.get(Calendar.DATE)}"
            btn_birdate.text =
                "${calender.get(Calendar.YEAR)}/${calender.get(Calendar.MONTH) + 1}/${
                    calender.get(Calendar.DATE)
                }"
            btDayHaveChange = true
        }
        edt_cardId2.isEnabled = false
        edt_cardId3.isEnabled = false
        edt_cardId4.isEnabled = false
        edt_cardId1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                edt_cardId2.isEnabled = edt_cardId1.text.isNotEmpty()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
        edt_cardId2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                edt_cardId3.isEnabled = edt_cardId2.text.isNotEmpty()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        edt_cardId3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                edt_cardId4.isEnabled = edt_cardId3.text.isNotEmpty()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
        genderSwitch.setOnClickListener {
            if (genderSwitch.isChecked) {
                txtWomen.setTextColor(activity.resources.getColor(R.color.lightBlue))
                txtMen.setTextColor(activity.resources.getColor(R.color.lightBlueGrey))
            } else {
                txtMen.setTextColor(activity.resources.getColor(R.color.lightBlue))
                txtWomen.setTextColor(activity.resources.getColor(R.color.lightBlueGrey))
            }

        }
        dialogBtn_cancel.setOnClickListener{
            activity.dialogBackground.visibility = View.INVISIBLE
            super.cancel()
        }
        dialogBtn_confirm.setOnClickListener {
            var regex=Regex("^[A-Z][0-9]{9}$")
            if(!edt_id.text.isEmpty() && !regex.matches(edt_id.text)){
                edt_id.setError("格式錯誤")
                return@setOnClickListener
            }
            if (!edt_cnName.text.isEmpty()) {
                sharedPreferencesEdit.putString("cnName", edt_cnName.text.toString())

            }
            if (!edt_enName.text.isEmpty()) {
                sharedPreferencesEdit.putString("enName", edt_enName.text.toString())

            }
            if (btDayHaveChange) {
                sharedPreferencesEdit.putString("birYear", year)
                sharedPreferencesEdit.putString("birMonth", month)
                sharedPreferencesEdit.putString("birDay", day)

            }
            if (!edt_id.text.isEmpty()) {
                sharedPreferencesEdit.putString("id", edt_id.text.toString())
            }
            if (edt_cardId1.text.isNotEmpty()) {
                sharedPreferencesEdit.putString("cardId1", edt_cardId1.text.toString())
            }
            if (edt_cardId2.text.isNotEmpty()) {
                sharedPreferencesEdit.putString("cardId2", edt_cardId2.text.toString())
            }
            if (edt_cardId3.text.isNotEmpty()) {
                sharedPreferencesEdit.putString("cardId3", edt_cardId3.text.toString())
            }
            if (edt_cardId4.text.isNotEmpty()) {
                sharedPreferencesEdit.putString("cardId4", edt_cardId4.text.toString())
            }
            sharedPreferencesEdit.apply()
            activity.txt_chName.text = sharedPreferences.getString("cnName", "王曉明")
            activity.txt_enName.text = sharedPreferences.getString("enName", "Xiao Ming")
            activity.txt_birDate.text = sharedPreferences.getString("birDate", "1996/05/18")
            activity.txt_birDate.text = "${
                sharedPreferences.getString(
                    "birYear",
                    "1996"
                )
            }.${
                sharedPreferences.getString(
                    "birMonth",
                    "05"
                )
            }.${sharedPreferences.getString("birDay", "18")}"
            sharedPreferencesEdit.putBoolean("gender", genderSwitch.isChecked)
            sharedPreferencesEdit.apply()
            saveAndCancel()
        }
    }

    override fun show() {
        super.show()
    }

    fun saveAndCancel(){

        Handler().postDelayed(
            {
                activity.editSuccess.visibility= View.INVISIBLE
                activity.dialogBackground.visibility= View.INVISIBLE
            },1000
        )
        super.cancel()
        activity.editSuccess.visibility= View.VISIBLE

    }
    override fun cancel() {
        super.cancel()
        activity.dialogBackground.visibility= View.INVISIBLE
    }
}