package com.agnext.qualixfarmer.tea.scanHistory

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agnext.qualixfarmer.R
import com.agnext.qualixfarmer.network.Response.ScanDetail
import kotlinx.android.synthetic.main.item_scan.view.*
import kotlinx.android.synthetic.main.item_scan_history.view.*

class AdapterScanHistory (val context: Context, val scandList: ArrayList<ScanDetail> ,val mCallback: ItemClickListener) :
    RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_scan, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return scandList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvDate.text = " ${scandList[position].dateDone} "
        holder.tvTime.text = " ${scandList[position].timeCreatedOn} "
        holder.tvConsignment.text = " Ag0${position}"
        holder.tvDoneUser.text = " ${scandList[position].id}"
        holder.tvFlc.text = " ${scandList[position].totalRecords.toString()}"

        holder.cardLayout.setOnClickListener{
          mCallback.onItemClick(position)
        }
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvDate = view.tvDate
    val tvTime = view.tvTime
    val tvConsignment = view.tvConsignment
    val tvDoneUser = view.tvDoneUser
    val tvFlc = view.tvFlc
    val cardLayout=view.cardLayout

}

interface ItemClickListener
{
    fun onItemClick(pos:Int)
}
