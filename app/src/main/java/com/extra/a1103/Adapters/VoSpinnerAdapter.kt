package com.extra.a1103.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ListView
import com.extra.a1103.R
import com.extra.a1103.databinding.ActivityMain2Binding.inflate
import kotlinx.android.synthetic.main.popup.view.*
import kotlinx.android.synthetic.main.spinner_bac_layout.view.*
import kotlinx.coroutines.NonDisposableHandle.parent

class voSpinnerAdapter(var context: Context) : BaseAdapter() {
    val voType = listOf("莫德納 - Moderna", "高端 - Medigen", "A Z - AstraZeneca", "B N T - BioNTech")
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
        var rootview = LayoutInflater.from(context).inflate(R.layout.spinner_bac_layout, p2, false)
        rootview.voName.text = voType.get(p0)
        return rootview
    }

    override fun getDropDownView(p0: Int, convertView: View?, p2: ViewGroup?): View {
        var rootview= LayoutInflater.from(context).inflate(R.layout.popup,p2,false)
        rootview.voTypeText.text=voType.get(p0)
        return rootview
    }

}