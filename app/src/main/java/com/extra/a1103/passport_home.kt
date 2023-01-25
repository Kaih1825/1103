package com.extra.a1103

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog.show
import android.content.Context
import android.content.Intent
import android.content.res.Resources.getSystem
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.drawToBitmap
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.BlurTransformation
import com.extra.a1103.Adapter.recyclerViewCustomAdapter
import com.extra.a1103.Dialog.infoDialog
import com.extra.a1103.Dialog.voTypeDialog
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.activity_passport_home.*
import kotlinx.android.synthetic.main.activity_passport_home.view.*
import kotlinx.android.synthetic.main.dialog.*
import kotlinx.android.synthetic.main.dialog.close
import kotlinx.android.synthetic.main.dialog.dialogBtn_cancel
import kotlinx.android.synthetic.main.dialog.dialogBtn_confirm
import kotlinx.android.synthetic.main.list_view_layout.*
import kotlinx.android.synthetic.main.popup.view.*
import kotlinx.android.synthetic.main.spinner_bac_layout.view.*
import kotlinx.android.synthetic.main.vo_dialog.*
import java.util.*
import kotlin.math.abs


class passport_home : AppCompatActivity() {
    //120100912

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passport_home)
        //TODO 字數限制
        dialogBackground.visibility = View.INVISIBLE
        var sharedPreferences = this.getSharedPreferences("personalInfo", Context.MODE_PRIVATE)
        var sharedPreferencesEdit = sharedPreferences.edit()
//        data.visibility=View.INVISIBLE
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
            navigationBarColor = Color.TRANSPARENT
        }
        var getIntentExtra= intent.getStringExtra("action")
        if(getIntentExtra=="startInfoDialog"){

            dialogBackground.visibility = View.VISIBLE
            infoDialog(this, this).show()
            Handler().postDelayed({
                setBlurBackground()
            },1)
        }
        btn_back.setOnClickListener(View.OnClickListener {
            finish()
        })
        if (sharedPreferences.getString("chName", "null") == "null") {
            sharedPreferencesEdit.putString("chName", txt_chName.text.toString())
            sharedPreferencesEdit.putString("enName", txt_enName.text.toString())
            sharedPreferencesEdit.putString("birDate", txt_birDate.text.toString())
            sharedPreferencesEdit.apply()
        }
        var startX: Int = 0
        var startY: Int = 0
        var scrollDx = 0
        scrollView.setEnabledScroll(false)
        scrollView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        startX = event.rawX.toInt()
                        startY = event.rawY.toInt()
                    }
                    MotionEvent.ACTION_UP -> {
                        if (scrollDx == 0) {
                            scrollDx = scroll1.width
                        }
                        var endX: Int = event.rawX.toInt()
                        var endY: Int = event.rawY.toInt()
                        var spaceX = endX - startX
                        var spaceY = endY - startY
                        if (abs(spaceX) >= scrollDx / 2 && spaceX > 0) {
                            scrollView.smoothScrollTo(
                                (scrollView.scrollX - scrollDx - 50.toPx).toInt(),
                                0
                            )
                        } else if (abs(spaceX) >= scrollDx / 2 && spaceX < 0) {
                            scrollView.smoothScrollTo(
                                (scrollView.scrollX + scrollDx + 50.toPx).toInt(),
                                0
                            )
                        }
                    }
                }
                return true
            }
        })
        scrollView.setOnScrollChangeListener { view, x, y, ox, oy ->
            run {
                if (x <= 70.toPx + scroll1.width / 2) {
                    //Log.e("TAG", "1", )
                    scroll1Dot.setBackgroundColor(Color.parseColor("#1C54C6"))
                    scroll2Dot.setBackgroundColor(Color.parseColor("#B3B3B3"))
                    scroll3Dot.setBackgroundColor(Color.parseColor("#B3B3B3"))
                } else if (x <= 70.toPx + scroll1.width + 50.toPx + scroll2.width / 2) {
                    //Log.e("TAG", "2", )
                    scroll2Dot.setBackgroundColor(Color.parseColor("#1C54C6"))
                    scroll1Dot.setBackgroundColor(Color.parseColor("#B3B3B3"))
                    scroll3Dot.setBackgroundColor(Color.parseColor("#B3B3B3"))
                } else {
                    //Log.e("TAG", "3", )
                    scroll3Dot.setBackgroundColor(Color.parseColor("#1C54C6"))
                    scroll2Dot.setBackgroundColor(Color.parseColor("#B3B3B3"))
                    scroll1Dot.setBackgroundColor(Color.parseColor("#B3B3B3"))
                }
            }

        }
        txt_chName.setOnClickListener {
            sharedPreferencesEdit.clear()
            sharedPreferencesEdit.apply()

        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = recyclerViewCustomAdapter(
            arrayOf("1", "2"), arrayOf("默德納", "BNT"), arrayOf("新竹台大醫院\n新竹分院", "新竹台大醫院\n新竹分院"),
            arrayOf("2021.11.20 13:20", "2021.11.20 13:20"), arrayOf(1, 1, 2), this,this
        )
//        window.decorView
        img_personalInfo.setOnClickListener {
            setBlurBackground()
            dialogBackground.visibility = View.VISIBLE
            infoDialog(this, this).show()
        }
    }

    override fun onRestart() {
        super.onRestart()
        var getIntentExtra= intent.getStringExtra("action")
        if(getIntentExtra=="startInfoDialog"){

            dialogBackground.visibility = View.VISIBLE
            infoDialog(this, this).show()
            Handler().postDelayed({
                setBlurBackground()
            },1)
        }
    }


    val Int.toPx: Int get() = (this * getSystem().displayMetrics.density).toInt()
    fun setBlurBackground() {
        val fullbitmap = window.decorView.drawToBitmap()
        //取得手機長、寬
        val phoneWidth = windowManager.defaultDisplay.width
        val phoneHeight = windowManager.defaultDisplay.height
        val bitmap = Bitmap.createBitmap(
            fullbitmap, 0, 0, phoneWidth, phoneHeight
        )
        dialogBackground.load(bitmap) {
            transformations(
                BlurTransformation(
                    context = this@passport_home,
                    radius = 25f,
                    sampling = 5f
                )
            )
        }
    }


}





