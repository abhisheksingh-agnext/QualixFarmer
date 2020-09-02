package com.agnext.qualixfarmer.warehouseChemical.scanDetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agnext.qualixfarmer.R
import com.agnext.qualixfarmer.network.Response.ResSpecxChemicalScans
import kotlinx.android.synthetic.main.adap_physical_compo.view.*

class SpecxChemDetailAdapter
    (
val context: Context,
val scandList: ArrayList<ResSpecxChemicalScans.AnalysesList>
) :
RecyclerView.Adapter<SpecxChemDetailAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.adap_physical_compo, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return scandList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTitle.text = scandList[position].analysisName
        holder.tvValue.text = scandList[position].totalAmount
        holder.tvUnit.text = scandList[position].amountUnit
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle = view.tvTitle
        val tvValue = view.tvValue
        val tvUnit = view.tvUnit

    }
}