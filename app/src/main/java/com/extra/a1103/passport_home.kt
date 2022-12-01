package com.extra.a1103

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog.show
import android.content.Context
import android.content.res.Resources.getSystem
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.DatePicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.BlurTransformation
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.activity_passport_home.*
import kotlinx.android.synthetic.main.dialog.*


class passport_home : AppCompatActivity() {
    //120100911
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passport_home)
        dialogBackground.visibility = View.INVISIBLE
        var sharedPreferences=this.getSharedPreferences("personalInfo",Context.MODE_PRIVATE)
        var sharedPreferencesEdit=sharedPreferences.edit()
//        data.visibility=View.INVISIBLE
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
            navigationBarColor = Color.TRANSPARENT
        }
        btn_back.setOnClickListener(View.OnClickListener {
            finish()
        })
        if(sharedPreferences.getString("chName","null")=="null"){
            sharedPreferencesEdit.putString("chName",txt_chName.text.toString())
            sharedPreferencesEdit.putString("enName",txt_enName.text.toString())
            sharedPreferencesEdit.putString("birDate",txt_birDate.text.toString())
            sharedPreferencesEdit.apply()
        }
        scrollView.setOnScrollChangeListener { view, x, y, _, _ ->
            run {
                if (x <= 70.toPx+scroll1.width/2) {
                    //Log.e("TAG", "1", )
                    scroll1Dot.setBackgroundColor(Color.parseColor("#1C54C6"))
                    scroll2Dot.setBackgroundColor(Color.parseColor("#B3B3B3"))
                    scroll3Dot.setBackgroundColor(Color.parseColor("#B3B3B3"))
                } else if (x <= 70.toPx+scroll1.width+50.toPx+scroll2.width/2){
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
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CustomAdapter(
            arrayOf("1", "2"), arrayOf("默德納", "BNT"), arrayOf("新竹台大醫院\n新竹分院", "新竹台大醫院\n新竹分院"),
            arrayOf("2021.11.20 13:20", "2021.11.20 13:20"), arrayOf(1, 1, 2), this
        )
//        window.decorView
        Log.e("TAG", mainLayout.isLaidOut.toString())
        img_personalInfo.setOnClickListener {
            setBlurBackground()
            dialogBackground.visibility = View.VISIBLE
            infoDialog(this,this).show()
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

class infoDialog(context: Context,var activity: Activity):Dialog(context){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setDimAmount(0f)
        var sharedPreferences=context.getSharedPreferences("personalInfo",Context.MODE_PRIVATE)
        var sharedPreferencesEdit=sharedPreferences.edit()
        edt_cnName.hint=sharedPreferences.getString("cnName","王曉明")
        edt_enName.hint=sharedPreferences.getString("enName","Xiao Ming")
        close.setOnClickListener {
            activity.dialogBackground.visibility = View.INVISIBLE
            super.cancel()
        }
        picker_birdate.setOnClickListener{
            var dialog= MaterialDatePicker.Builder.datePicker()
                .setTitleText("")
                .setTheme(R.style.datepicker)
                .build()
            var activity2=activity as passport_home
            dialog.show(activity2.supportFragmentManager,"")
        }
        dialogBtn_close.setOnClickListener {
            activity.dialogBackground.visibility = View.INVISIBLE
            sharedPreferencesEdit.putString("cnName",edt_cnName.text.toString())
            sharedPreferencesEdit.apply()
            super.cancel()
        }
    }

    override fun show() {
        super.show()
    }

    override fun cancel() {
        activity.dialogBackground.visibility = View.INVISIBLE
        super.cancel()
    }
}

class CustomAdapter(
    val numArray: Array<String>,
    val voNameArray: Array<String>,
    val hosNameArray: Array<String>,
    val dateArray: Array<String>,
    val layoutTypeArray: Array<Int>,
    val activity: Activity
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {  //靜態Object
        const val baseViewType = 1
        const val plusViewType = 2
    }
    val Int.toPx: Int get() = (this * getSystem().displayMetrics.density).toInt()
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val num: TextView
        val voName: TextView
        val hosName: TextView
        val date: TextView
        val voClick: CardView

        init {
            num = view.findViewById(R.id.num)
            voName = view.findViewById(R.id.voName)
            hosName = view.findViewById(R.id.hosName)
            date = view.findViewById(R.id.date)
            voClick = view.findViewById(R.id.voClick)
        }
    }

    class PlusViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {

        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // Create a new view, which defines the UI of the list item
        if (viewType == baseViewType) {
            return ViewHolder(
                LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.list_view_layout, viewGroup, false)
            )
        } else {
            return PlusViewHolder(
                LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.list_view_layout2, viewGroup, false)
            )
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var layoutType = layoutTypeArray[position]
        if (layoutType == baseViewType) {
            val viewHolder = holder as ViewHolder
            //viewHolder.num.text=numArray[position]
            viewHolder.apply {
                num.text = numArray[position]
                voName.text = voNameArray[position]
                hosName.text = hosNameArray[position]
                date.text = dateArray[position]
                voClick.setOnClickListener {
                    if (position == 0) {
                        activity.scrollView.scrollTo(0, 0)
                    } else if (position == 1) {
                        activity.scrollView.scrollTo(70.toPx+activity.scroll1.width-20.toPx, 0)
                    } else if (position == 2) {
                        activity.scrollView.fullScroll(View.FOCUS_RIGHT)
                    }
                }
            }
        } else {
            val viewHolder = holder as PlusViewHolder
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = layoutTypeArray.size

    override fun getItemViewType(position: Int): Int {
        return layoutTypeArray[position]
    }
}
