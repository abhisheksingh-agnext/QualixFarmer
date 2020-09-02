package com.agnext.qualixfarmer.tea.qualityAnalysis

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agnext.qualixfarmer.R
import com.agnext.qualixfarmer.network.Response.AnalysisResults
import kotlinx.android.synthetic.main.adap_physical_compo.view.*

class ScanDetailQualixAdapter
    (
    val context: Context,
    val scandList: ArrayList<AnalysisResults>
) :

    RecyclerView.Adapter<ScanDetailQualixAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle = view.tvTitle
        val tvValue = view.tvValue
        val tvUnit = view.tvUnit

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.adap_physical_compo, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return scandList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTitle.text = scandList[position].analysisType
        holder.tvValue.text = scandList[position].analysisResult
        holder.tvUnit.text = scandList[position].analysisUnit
    }


}