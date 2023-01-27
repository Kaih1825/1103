package com.extra.a1103

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.drawToBitmap
import coil.load
import coil.transform.BlurTransformation
import com.extra.a1103.Dialogs.addContactDialog
import com.extra.a1103.Methods.sql
import com.google.zxing.BinaryBitmap
import com.google.zxing.LuminanceSource
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import kotlinx.android.synthetic.main.activity_passport_home.*
import kotlinx.android.synthetic.main.activity_registration_home.*
import kotlinx.android.synthetic.main.activity_registration_home.btn_back
import kotlinx.android.synthetic.main.activity_registration_home.dialogBackground
import kotlinx.android.synthetic.main.activity_scan_contact.*
import kotlinx.android.synthetic.main.activity_scan_contact.btn_add
import kotlinx.android.synthetic.main.activity_scan_contact.btn_cancel
import kotlinx.android.synthetic.main.activity_scan_contact.txt_placeNum
import kotlinx.android.synthetic.main.add_contact_dialog_layout.*
import org.json.JSONObject
import java.util.Date
import kotlin.math.log


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

        addPicture.setOnClickListener {
            if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),0)
            }
            val intent= Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent,1)
        }

        btn_add.setOnClickListener {
            var placeNum=""
            if(txt_placeNum.text.isEmpty()){
                placeNum="0000 0000 0000 0000"
            }
            else{
                placeNum=txt_placeNum.text.toString()
            }
            var calendar=Calendar.getInstance()
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
            var nowDate="${calendar.get(java.util.Calendar.YEAR)}/${month}/${date}"

            /////////

            var hour= calendar.get(Calendar.HOUR).toString()
            var min=calendar.get(Calendar.MINUTE).toString()

            if(calendar.get(Calendar.HOUR)>12){
                hour=(calendar.get(Calendar.HOUR)-12).toString()
            }
            if(min.length==1){
                min= "0$min"
            }
            if(hour.length==1){
                hour="0${hour}"
            }
            if(hour=="00"){
                hour="12"
            }
            var am_pm=if(calendar.get(Calendar.AM_PM)==0){"AM"}else{"PM"}
            var nowTime="${hour}:${min} $am_pm"
            sql.insert(this,nowDate,nowTime,placeNum)
            setBlurBackground()
            Handler().postDelayed({
                addSuccess2.visibility=View.GONE
                dialogBackground2.visibility=View.GONE
                finish()
            },2000)
            addSuccess2.visibility=View.VISIBLE
            dialogBackground2.visibility=View.VISIBLE

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1&&resultCode== RESULT_OK){
            val data=data?.data
            img_qrCode.setImageURI(data)
            getRes(img_qrCode.drawable.toBitmap())
            var res=getRes(img_qrCode.drawable.toBitmap())
            if(res!="ERROR"){
                var placeCode=parse(res)
                try{
                    placeCode.toLong()
                    if(placeCode.length==16){
                        var outputString=placeCode.substring(0,4)+" "+placeCode.substring(4,8)+" "+placeCode.substring(8,12)+" "+placeCode.substring(12,16)
                        txt_placeNum.text=outputString
                    }
                }catch (ex:java.lang.Exception){
                    Log.e("ERROR", ex.toString(), )
                }
            }
            else{
                txt_placeNum.text=""
            }

        }
    }

    fun getRes(bitmap: Bitmap):String{
        var infoArray=IntArray(bitmap.height*bitmap.width)
        bitmap.getPixels(infoArray,0,bitmap.width,0,0,bitmap.width,bitmap.height)
        var source=RGBLuminanceSource(bitmap.width,bitmap.height,infoArray)
        var binaryBitmap=BinaryBitmap(HybridBinarizer(source))
        var reader=MultiFormatReader()
        try{
            return reader.decode(binaryBitmap).text
        }catch (ex:java.lang.Exception){
            return "ERROR"
        }
    }

    fun parse(jsonText: String):String{
        var jsonObject=JSONObject(jsonText)
        var placeCode=jsonObject.getString("placecode")
        return placeCode
    }

    fun setBlurBackground() {
        val fullbitmap = window.decorView.drawToBitmap()
        //取得手機長、寬
        val phoneWidth = windowManager.defaultDisplay.width
        val phoneHeight = windowManager.defaultDisplay.height
        val bitmap = Bitmap.createBitmap(
            fullbitmap, 0, 0, phoneWidth, phoneHeight
        )
        dialogBackground2.load(bitmap) {
            transformations(
                BlurTransformation(
                    context = this@scan_contact,
                    radius = 25f,
                    sampling = 5f
                )
            )
        }
    }
}