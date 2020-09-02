package com.agnext.qualixfarmer.fields.farmList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.agnext.qualixfarmer.R
import com.agnext.qualixfarmer.network.Response.ResAllFarms
import com.daimajia.swipe.SwipeLayout
import kotlinx.android.synthetic.main.item_farm.view.*

class FieldListAdapter(
    val context:Context, val
    fieldList: ArrayList<ResAllFarms>,
    val mCallback: FarmListCallback
) : RecyclerView.Adapter<FieldListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val item: LinearLayout = view.item
        val tvFarmName : TextView= view.tvName
        val tvCrop : TextView= view.tvStatus
        val tvCropType: TextView = view.tvCreated
        val lnEdit:LinearLayout=view.lnEdit
        val lnDelete:LinearLayout=view.lnDelete
        val swipe:SwipeLayout=view.swipe
        val ivIndicator:View=view.viIndicator


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FieldListAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_farm, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return fieldList.size!!
    }

    override fun onBindViewHolder(holder: FieldListAdapter.ViewHolder, position: Int) {
        holder.tvFarmName.text = " : ${fieldList[position].farmId}, ${fieldList[position].address}"
        holder.tvCrop.text = " : ${fieldList[position].cropName}"
        holder.tvCropType.text = " : ${fieldList[position].cropVerityName}"

        holder.item.setOnClickListener{
            mCallback.onClickItem(position)
        }
        holder.lnEdit.setOnClickListener{
            holder.swipe.close()
            mCallback.editFarmCallback(position)
        }
        holder.lnDelete.setOnClickListener{
            holder.swipe.close()
            mCallback.deleteFarmCallback(position)
        }
        holder.swipe.addSwipeListener(object : SwipeLayout.SwipeListener {
            override fun onClose(layout: SwipeLayout) {
                holder.ivIndicator.visibility=View.VISIBLE
            }

            override fun onUpdate(
                layout: SwipeLayout,
                leftOffset: Int,
                topOffset: Int
            ) { //you are swiping.
            }

            override fun onStartOpen(layout: SwipeLayout) {

            }
            override fun onOpen(layout: SwipeLayout) { //when the BottomView totally show.
                holder.ivIndicator.visibility=View.INVISIBLE

            }

            override fun onStartClose(layout: SwipeLayout) {}
            override fun onHandRelease(
                layout: SwipeLayout,
                xvel: Float,
                yvel: Float
            ) { //when user's hand released.
            }
        })
    }
}