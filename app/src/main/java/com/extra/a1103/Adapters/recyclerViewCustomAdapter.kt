package com.extra.a1103.Adapter

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RemoteViews
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.core.view.drawToBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.BlurTransformation
import com.extra.a1103.Dialog.voTypeDialog
import com.extra.a1103.R
import com.extra.a1103.RemoteViewsFactories.showVoListRemoteViewsService
import com.extra.a1103.RemoteViewsServices.showVoInfoRemoteViewsService
import com.extra.a1103.Widgets.ShowVoInfo2x2
import com.extra.a1103.Widgets.ShowVoInfo5x2
import com.extra.a1103.passport_home
import kotlinx.android.synthetic.main.activity_passport_home.*
import kotlinx.android.synthetic.main.vo_dialog.*


class recyclerViewCustomAdapter(
    val numArray: Array<String>,
    val voNameArray: Array<String>,
    val hosNameArray: Array<String>,
    val dateArray: Array<String>,
    val layoutTypeArray: Array<Int>,
    val activity: Activity,
    val context:Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {  //靜態Object
        const val baseViewType = 1
        const val plusViewType = 2
    }

    val Int.toPx: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val num: TextView
        val voName: TextView
        val hosName: TextView
        val date: TextView
        val voClick: CardView
        val btnEdit:AppCompatButton

        init {
            num = view.findViewById(R.id.num)
            voName = view.findViewById(R.id.voName)
            hosName = view.findViewById(R.id.hosName)
            date = view.findViewById(R.id.date)
            voClick = view.findViewById(R.id.voClick)
            btnEdit=view.findViewById(R.id.btnEdit)
        }
    }

    class PlusViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val layoutClick:CardView
        init {
            layoutClick=view.findViewById(R.id.clickLayout)
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

                var sp=context.getSharedPreferences("getVoInfo",Context.MODE_PRIVATE)
                var name=activity.resources.getStringArray(R.array.voTypeArray)[sp.getInt("voTypeSelection_${position}",0)]
                voName.text=name.substring(0,name.indexOf("-"))
                hosName.text=activity.resources.getStringArray(R.array.voPlaceArray)[sp.getInt("voPlaceSelection_${position}",0)]
                date.text=sp.getString("voDate_${position}","2021.11.19")

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

                btnEdit.setOnClickListener {
                    voTypeDialog(context,activity,position).show()
                    setBlurBackground()
                    activity.dialogBackground.visibility = View.VISIBLE
                }
            }
        } else {
            val viewHolder = holder as PlusViewHolder
            viewHolder.apply {
                layoutClick.setOnClickListener {
                    var sp=context.getSharedPreferences("getVoInfo", Context.MODE_PRIVATE)
                    var spEdit=context.getSharedPreferences("getVoInfo", Context.MODE_PRIVATE).edit()
                    spEdit.putInt("num${position+1}IsOpen",1)
                    spEdit.apply()
                    val appWidgetManager= AppWidgetManager.getInstance(context)
                    var rv= RemoteViews(context.packageName, R.layout.show_vo_info5x2)
                    var listIntent= Intent(context, showVoListRemoteViewsService::class.java)
                    rv.setRemoteAdapter(R.id.list,listIntent)
                    var appWidgetIds = appWidgetManager.getAppWidgetIds(
                        ComponentName(context, ShowVoInfo5x2::class.java)
                    )
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list)
                    rv= RemoteViews(context.packageName, R.layout.show_vo_info2x2)
                    listIntent= Intent(context, showVoInfoRemoteViewsService::class.java)
                    rv.setRemoteAdapter(R.id.list,listIntent)
                    appWidgetIds = appWidgetManager.getAppWidgetIds(
                        ComponentName(context, ShowVoInfo2x2::class.java)
                    )
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list)
                    val isOpen1=sp.getInt("num1IsOpen",2)
                    val isOpen2=sp.getInt("num2IsOpen",2)
                    val isOpen3=sp.getInt("num3IsOpen",2)
                    activity.recyclerView.layoutManager = LinearLayoutManager(context)
                    activity.recyclerView.adapter = recyclerViewCustomAdapter(
                        arrayOf("1", "2","3"), arrayOf("默德納", "BNT","BNT"), arrayOf("新竹台大醫院\n新竹分院", "新竹台大醫院\n新竹分院","新竹台大醫院\n新竹分院"),
                        arrayOf("2021.11.20 13:20", "2021.11.20 13:20","2021.11.20 13:20"), arrayOf(isOpen1, isOpen2, isOpen3), activity,context
                    )
                    reLoad(sp,activity)
                }
            }
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = layoutTypeArray.size


    override fun getItemViewType(position: Int): Int {
        return layoutTypeArray[position]
    }
    fun setBlurBackground() {
        val fullbitmap = activity.window.decorView.drawToBitmap()
        //取得手機長、寬
        val phoneWidth = activity.windowManager.defaultDisplay.width
        val phoneHeight = activity.windowManager.defaultDisplay.height
        val bitmap = Bitmap.createBitmap(
            fullbitmap, 0, 0, phoneWidth, phoneHeight
        )
        activity.dialogBackground.load(bitmap) {
            transformations(
                BlurTransformation(
                    context = context,
                    radius = 25f,
                    sampling = 5f
                )
            )
        }
    }

    fun reLoad(sharedPreferences2: SharedPreferences, activity: Activity){
        val isOpen1=sharedPreferences2.getInt("num1IsOpen",2)
        val isOpen2=sharedPreferences2.getInt("num2IsOpen",2)
        val isOpen3=sharedPreferences2.getInt("num3IsOpen",2)
        if(isOpen1==1){
            activity.scroll1Img.setImageDrawable(activity.resources.getDrawable(R.drawable.a7))
            activity.scroll1Text1.text="已完成"
            activity.scroll1Text1.setTextColor(activity.resources.getColor(R.color.deepBlue))
            activity.scroll1Text2.text="第一劑疫苗"
            activity.scroll1Text2.setTextColor(activity.resources.getColor(R.color.deepBlue))
            activity.btn_vo1.setCardBackgroundColor(activity.resources.getColor(R.color.green))
            activity.btn_txt_vo1.text="已接種疫苗"
        }
        else{
            activity.scroll1Img.setImageDrawable(activity.resources.getDrawable(R.drawable.a8))
            activity.scroll1Text1.text="未完成"
            activity.scroll1Text1.setTextColor(Color.RED)
            activity.scroll1Text2.text="第一劑疫苗"
            activity.scroll1Text2.setTextColor(Color.RED)
            activity.btn_vo1.setCardBackgroundColor(Color.RED)
            activity.btn_txt_vo1.text="未接種疫苗"
        }
        if(isOpen2==1){
            activity.scroll2Img.setImageDrawable(activity.resources.getDrawable(R.drawable.a7))
            activity.scroll2Text1.text="已完成"
            activity.scroll2Text1.setTextColor(activity.resources.getColor(R.color.deepBlue))
            activity.scroll2Text2.text="第二劑疫苗"
            activity.scroll2Text2.setTextColor(activity.resources.getColor(R.color.deepBlue))
            activity.btn_vo2.setCardBackgroundColor(activity.resources.getColor(R.color.green))
            activity.btn_txt_vo2.text="已接種疫苗"
        }
        else{
            activity.scroll2Img.setImageDrawable(activity.resources.getDrawable(R.drawable.a8))
            activity.scroll2Text1.text="未完成"
            activity.scroll2Text1.setTextColor(Color.RED)
            activity.scroll2Text2.text="第二劑疫苗"
            activity.scroll2Text2.setTextColor(Color.RED)
            activity.btn_vo2.setCardBackgroundColor(Color.RED)
            activity.btn_txt_vo2.text="未接種疫苗"
        }
        if(isOpen3==1){
            activity.scroll3Img.setImageDrawable(activity.resources.getDrawable(R.drawable.a7))
            activity.scroll3Text1.text="已完成"
            activity.scroll3Text1.setTextColor(activity.resources.getColor(R.color.deepBlue))
            activity.scroll3Text2.text="第三劑疫苗"
            activity.scroll3Text2.setTextColor(activity.resources.getColor(R.color.deepBlue))
            activity.btn_vo3.setCardBackgroundColor(activity.resources.getColor(R.color.green))
            activity.btn_txt_vo3.text="已接種疫苗"
        }
        else{
            activity.scroll3Img.setImageDrawable(activity.resources.getDrawable(R.drawable.a8))
            activity.scroll3Text1.text="未完成"
            activity.scroll3Text1.setTextColor(Color.RED)
            activity.scroll3Text2.text="第三劑疫苗"
            activity.scroll3Text2.setTextColor(Color.RED)
            activity.btn_vo3.setCardBackgroundColor(Color.RED)
            activity.btn_txt_vo3.text="未接種疫苗"
        }
    }
}