package com.extra.a1103

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_registration_home.*
import kotlinx.android.synthetic.main.activity_registration_home.btn_back
import kotlinx.android.synthetic.main.activity_scan_contact.*

class scan_contact : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_contact)
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
            navigationBarColor = Color.TRANSPARENT
        }
        btn_back.setOnClickListener {
            finish()
        }
        btn_cancel.setOnClickListener {
            finish()
        }

    }
}