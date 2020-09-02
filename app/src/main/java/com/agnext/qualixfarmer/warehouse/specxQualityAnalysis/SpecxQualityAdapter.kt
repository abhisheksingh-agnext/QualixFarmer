package com.agnext.qualixfarmer.warehouse.specxQualityAnalysis

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agnext.qualixfarmer.R
import com.agnext.qualixfarmer.network.Response.ResSpecxScans
import kotlinx.android.synthetic.main.item_payment_history.view.*
import java.text.SimpleDateFormat
import java.util.*


class SpecxQualityAdapter(
    val context: Context,
    val scandList: ArrayList<ResSpecxScans>,
    val mCallback: ItemOnClick
) :
    RecyclerView.Adapter<SpecxQualityAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_payment_history, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return scandList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTittle1.text = context.getString(R.string.reference_id)
        holder.tvSampleId.text = " ${scandList[position].reference_id}"

        holder.tvTittle2.text = context.getString(R.string.total_count)
        holder.tvFlc.text = " ${scandList[position].total_count}"

        holder.tvTittle3.text = context.getString(R.string.commodity_name)
        holder.tvDoneUser.text = " ${scandList[position].commodity_name}"

        val sdf = SimpleDateFormat("dd/MM/yyyy ,HH:mm:ss")
       var dateString=  sdf.format(Date(scandList[position].created_on!!.toLong()))
        holder.tvDate.text = dateString

        holder.container.setOnClickListener {
            mCallback.onItemClick(position)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTittle1 = view.tvTittle1
        val tvTittle2 = view.tvTittle2
        val tvTittle3 = view.tvTittle3


        val tvSampleId = view.tvSampleId
        val tvFlc = view.tvFlc
        val tvDoneUser = view.tvDoneUser
        val tvDate = view.tvDate
        val container = view.container
    }
}


interface ItemOnClick {
    fun onItemClick(position: Int)
}