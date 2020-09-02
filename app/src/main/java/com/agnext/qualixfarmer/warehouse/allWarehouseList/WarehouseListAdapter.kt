package com.agnext.qualixfarmer.warehouse.allWarehouseList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.agnext.qualixfarmer.R
import kotlinx.android.synthetic.main.item_warehouse_list.view.*

class WarehouseListAdapter(
    val context: Context,
    val warehouseList: ArrayList<WarehouseData>,
    val mCallback: WarehouseDetailCallback
)  : RecyclerView.Adapter<WarehouseListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv1 : TextView= view.tv1
        val tv2: TextView = view.tv2
        val tv3: TextView = view.tv3

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WarehouseListAdapter.ViewHolder {
        return WarehouseListAdapter.ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_warehouse_list, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return warehouseList.size!!
    }

    override fun onBindViewHolder(holder: WarehouseListAdapter.ViewHolder, position: Int) {
        holder.tv1.text = "Warehouse : ${warehouseList[position].wareName}"
        holder.tv2.text = "Warehouse quantity : ${warehouseList[position].wareQuantity}"
        holder.tv3.text = "Address : ${warehouseList[position].area}"

        holder.tv3.setOnClickListener{
            mCallback.onClickItem(position)
        }

    }
}

interface WarehouseDetailCallback {
    fun onClickItem(position: Int)
}