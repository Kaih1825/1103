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
import android.widget.ArrayAdapter
import android.widget.PopupWindow
import android.widget.Spinner
import com.extra.a1103.R
import kotlinx.android.synthetic.main.list_view_layout.*
import kotlinx.android.synthetic.main.spinner_listview.view.*
import kotlinx.android.synthetic.main.vo_dialog.*
import spinnerAdapter


class voTypeDialog(context: Context, var activity: Activity) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.vo_dialog)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setDimAmount(0f)
        voTypeSpinner.adapter = spinnerAdapter(context)
        voTypeSpinner.isClickable=false
//        voTypeSpinner.setSpinnerEventsListener(object : CustomSpinner.OnSpinnerEventsListener {
//            override fun onSpinnerOpened(spinner: Spinner) {
//                // Do something when the spinner is opened
//                Log.e("spinner", "open")
//                voTypeSpinner.setBackgroundDrawable(activity.resources.getDrawable(R.drawable.spinner_bac_down))
//            }
//
//            override fun onSpinnerClosed(spinner: Spinner) {
//                // Do something when the spinner is closed
//                Log.e("spinner", "close")
//                voTypeSpinner.setBackgroundDrawable(activity.resources.getDrawable(R.drawable.spinner_bac))
//            }
//        })
        voTypeSpinner.setOnTouchListener(object :View.OnTouchListener{
            override fun onTouch(p0: View?, p1: MotionEvent?):Boolean {
                when (p1?.action) {
                    MotionEvent.ACTION_UP -> run {
                        Log.e("spinner", "close")
//                        voTypeSpinner.setSelection(1)
//                        voTypeSpinner.setBackgroundDrawable(activity.resources.getDrawable(R.drawable.spinner_bac_down))
                        var adapter=ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,activity.resources.getStringArray(R.array.voTypeArray))
                        showPopup(context,voTypeSpinner,adapter)
                    }
                }
                return true
            }
        })
    }

    override fun show() {
        super.show()
    }

    fun showPopup(context: Context,spinner:Spinner,adapter: ArrayAdapter<String>){
        var view=LayoutInflater.from(context).inflate(R.layout.spinner_listview,null)
        var popupWindow=PopupWindow(view,spinner.width-10.toPx,150.toPx)
        popupWindow.isOutsideTouchable=true
        popupWindow.setBackgroundDrawable(activity.getDrawable(R.drawable.popup_bac))
        view.listView.adapter=adapter
        popupWindow.elevation=10f
        popupWindow.showAsDropDown(spinner)
    }

    val Int.toPx: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()
}
