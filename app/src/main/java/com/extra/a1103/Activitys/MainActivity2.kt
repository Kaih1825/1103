package com.extra.a1103

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_main2.*

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        go_bl.setOnClickListener(View.OnClickListener {
            var i=Intent(this,passport_home::class.java)
            startActivity(i)
        })
        go_gr.setOnClickListener(View.OnClickListener {
            var i=Intent(this,registration_home::class.java)
            startActivity(i)
        })
    }

}