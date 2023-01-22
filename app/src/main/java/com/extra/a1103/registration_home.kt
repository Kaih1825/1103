package com.extra.a1103

import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import androidx.core.view.drawToBitmap
import androidx.core.widget.addTextChangedListener
import coil.load
import coil.transform.BlurTransformation
import com.extra.a1103.Adapters.registrationListAdapter
import com.extra.a1103.Dialogs.addContactDialog
import kotlinx.android.synthetic.main.activity_registration_home.*
import kotlinx.android.synthetic.main.activity_registration_home.dialogBackground
import java.util.Vector

class registration_home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_home)
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
            navigationBarColor = Color.TRANSPARENT
        }
        var ischeck= Vector<Boolean>()
        if(true){
            ischeck.add(false)
            ischeck.add(false)
            ischeck.add(false)
            ischeck.add(false)
            ischeck.add(false)
            ischeck.add(false)
            ischeck.add(false)
            ischeck.add(false)
            ischeck.add(false)
            ischeck.add(false)
            ischeck.add(false)
            ischeck.add(false)
            ischeck.add(false)
            ischeck.add(false)
            ischeck.add(false)
        }
        list.adapter=registrationListAdapter(this,this,ischeck)
        edt_keyword.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                TODO("Not yet implemented")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(edt_keyword.text.isNotEmpty()){
                    txt_search.text="取消"
                    txt_search.setTextColor(resources.getColor(R.color.grey))
                }
                else{
                    txt_search.text="搜尋"
                    txt_search.setTextColor(resources.getColor(R.color.green))
                }
            }

            override fun afterTextChanged(p0: Editable?) {
//                TODO("Not yet implemented")
            }

        })
        btn_add.setOnClickListener {
            addContactDialog(this,this).show()
            setBlurBackground()
        }
        btn_back.setOnClickListener{
            finish()
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
}
