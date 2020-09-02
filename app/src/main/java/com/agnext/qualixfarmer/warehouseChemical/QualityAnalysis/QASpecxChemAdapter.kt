package com.agnext.qualixfarmer.warehouseChemical.QualityAnalysis

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agnext.qualixfarmer.R
import com.agnext.qualixfarmer.network.Response.ResSpecxChemicalScans
import com.agnext.qualixfarmer.warehouse.specxQualityAnalysis.ItemOnClick
import kotlinx.android.synthetic.main.item_payment_history.view.*

class QASpecxChemAdapter(
    val context: Context,
    val scanList: ArrayList<ResSpecxChemicalScans.ChemicalScan>,
    val mCallback: ItemOnClick
) :
    RecyclerView.Adapter<QASpecxChemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_payment_history, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return scanList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvTittle1.text = context.getString(R.string.batch_Id)
        holder.tvSampleId.text  =" : ${scanList[position].batchId}"

        holder.tvTittle2.text=context.getString(R.string.crop_name)
        holder.tvFlc.text=" : ${scanList[position].cropName}"

        holder.tvTittle3.visibility=View.GONE
        holder.tvDoneUser.visibility=View.GONE

        holder.tvDate.text="${scanList[position].createdOn} , ${scanList[position].time} "

        holder.container.setOnClickListener {
            mCallback.onItemClick(position)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTittle1 = view.tvTittle1
        val tvSampleId = view.tvSampleId

        val tvTittle2 = view.tvTittle2
        val tvFlc = view.tvFlc

        val tvTittle3 = view.tvTittle3
        val tvDoneUser = view.tvDoneUser

        val tvDate = view.tvDate
        val container = view.container
    }
}
