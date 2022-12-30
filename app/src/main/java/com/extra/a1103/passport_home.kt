package com.extra.a1103

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog.show
import android.content.Context
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
                        Log.e("TAG", spaceX.toString())
                        if (abs(spaceX) >= scrollDx / 2 && spaceX > 0) {
                            Log.e("scrollDx", scrollDx.toString())
                            Log.e("MOVE", "-1")
                            Log.e("MoveX", (scrollView.scrollX).toInt().toString())
                            scrollView.smoothScrollTo(
                                (scrollView.scrollX - scrollDx - 50.toPx).toInt(),
                                0
                            )
                        } else if (abs(spaceX) >= scrollDx / 2 && spaceX < 0) {
                            Log.e("scrollDx", scrollDx.toString())
                            Log.e("MOVE", "-1")
                            scrollView.smoothScrollTo(
                                (scrollView.scrollX + scrollDx + 50.toPx).toInt(),
                                0
                            )
                            Log.e("MoveX", (scrollView.scrollX).toInt().toString())
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
            voTypeDialog(this,this).show()
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
            infoDialog(this, this).show()
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
class spinnerAdapter(var context: Context): BaseAdapter() {
    val voType= listOf("莫德納 - Moderna","高端 - Medigen","A Z - AstraZeneca","B N T - BioNTech")
    override fun getCount(): Int {
        return voType.count()
    }

    override fun getItem(p0: Int): Any {
        return voType.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var rootview=LayoutInflater.from(context).inflate(R.layout.spinner_bac_layout,p2,false)
        rootview.voName.text=voType.get(p0)
        return rootview
    }

    override fun getDropDownView(p0: Int, convertView: View?, p2: ViewGroup?): View {
        var rootview=LayoutInflater.from(context).inflate(R.layout.popup,p2,false)
        rootview.voTypeText.text=voType.get(p0)
        return rootview
    }

}

class voTypeDialog(context: Context,var activity: Activity):Dialog(context){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.vo_dialog)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setDimAmount(0f)
        voTypeSpinner.adapter=spinnerAdapter(context)
    }

    override fun show() {
        super.show()
    }
}
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
        edt_cnName.addTextChangedListener(object : TextWatcher{
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
        edt_enName.addTextChangedListener(object : TextWatcher{
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
                activity.editSuccess.visibility=View.INVISIBLE
                activity.dialogBackground.visibility=View.INVISIBLE
            },1000
        )
        super.cancel()
        activity.editSuccess.visibility=View.VISIBLE

    }
    override fun cancel() {
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
                        activity.scrollView.smoothScrollTo(0, 0)
                    } else if (position == 1) {
                        activity.scrollView.smoothScrollTo(
                            70.toPx + activity.scroll1.width - 20.toPx,
                            0
                        )
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
