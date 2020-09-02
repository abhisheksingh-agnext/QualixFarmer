package com.agnext.qualixfarmer.tea.qualityAnalysis

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agnext.qualixfarmer.R
import com.agnext.qualixfarmer.network.Response.ScanData
import com.agnext.qualixfarmer.network.Response.ScanDetail
import com.agnext.qualixfarmer.utils.utils.Companion.getTimeFromEpoch
import kotlinx.android.synthetic.main.item_payment_history.view.*
import kotlinx.android.synthetic.main.item_scan.view.tvDoneUser
import kotlin.collections.ArrayList


class QualityAnalysisAdapter(val context: Context, val scandList: ArrayList<ScanData>, val mCallback:ItemOnClick) :
    RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_payment_history, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return scandList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvSampleId.text = " ${scandList[position].scanId}"
        if(scandList[position].qualityScore!=null)
        holder.tvFlc.text=" ${scandList[position].qualityScore}"
        else{
            holder.tvTittle2.text="Device name"
            holder.tvFlc.text=" ${scandList[position].deviceName}"}
        holder.tv5.text=" ${scandList[position].deviceSerialNo}"
        holder.tvDoneUser.text = " ${scandList[position].commodityName}"
        holder.tvDate.text = " ${getTimeFromEpoch(scandList[position].dateDone!!.toLong())} "
        holder.container.setOnClickListener{
            mCallback.onItemClick(position)
        }
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvSampleId = view.tvSampleId
    val tvFlc=view.tvFlc
    val tvDoneUser = view.tvDoneUser
    val tvDate = view.tvDate
    val container=view.container
    val tvTittle2=view.tvTittle2
    val tv5=view.tv5
}

interface ItemOnClick
{
    fun onItemClick(position:Int)
}
