package com.extra.a1103.Adapter

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.core.view.drawToBitmap
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.BlurTransformation
import com.extra.a1103.Dialog.voTypeDialog
import com.extra.a1103.R
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
                    voTypeDialog(context,activity,position,voName,hosName,date).show()
                    setBlurBackground()
                    activity.dialogBackground.visibility = View.VISIBLE
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
}