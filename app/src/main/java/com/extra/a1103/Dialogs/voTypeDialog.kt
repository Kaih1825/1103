package com.extra.a1103.Dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import android.widget.ListView
import android.widget.Spinner
import androidx.appcompat.resources.Compatibility.Api21Impl.inflate
import androidx.core.graphics.drawable.DrawableCompat.inflate
import com.extra.a1103.Adapter.voSpinnerAdapter
import com.extra.a1103.CustomSpinner
import com.extra.a1103.R
import kotlinx.android.synthetic.main.vo_dialog.*
import kotlinx.coroutines.NonDisposableHandle.parent


class voTypeDialog(context: Context, var activity: Activity) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.vo_dialog)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setDimAmount(0f)
        voTypeSpinner.adapter = voSpinnerAdapter(context)
        voTypeSpinner.setSpinnerEventsListener(object : CustomSpinner.OnSpinnerEventsListener {
            override fun onSpinnerOpened(spinner: Spinner) {
                // Do something when the spinner is opened
                Log.e("spinner", "open")
                voTypeSpinner.setBackgroundDrawable(activity.resources.getDrawable(R.drawable.spinner_bac_down))
            }

            override fun onSpinnerClosed(spinner: Spinner) {
                // Do something when the spinner is closed
                Log.e("spinner", "close")
                voTypeSpinner.setBackgroundDrawable(activity.resources.getDrawable(R.drawable.spinner_bac))
            }
        })
    }

    override fun show() {
        super.show()
    }
}
