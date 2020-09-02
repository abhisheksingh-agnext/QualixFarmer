package com.agnext.qualixfarmer.tea.paymentHistory

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agnext.qualixfarmer.R
import kotlinx.android.synthetic.main.item_payment_history.view.*

class AdapterPaymentHistory  (val context: Context, val scanHistoryList: ArrayList<PaymentHistoryObject>) :
    RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_payment_history, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return scanHistoryList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
     /*   holder.tvDate.text = " : ${scanHistoryList[position].date}"
        holder.tvTime.text = " : ${scanHistoryList[position].time}"
        holder.tvFLC.text = " : ${scanHistoryList[position].FLC}"
        holder.tvDoneBy.text = "  : ${scanHistoryList[position].doneBy}"
        holder.tvAmount.text=" : ${scanHistoryList[position].Amount}"
        holder.tvPaymentStatus.text="  : ${scanHistoryList[position].PaymentStatus}"*/

    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
   /* val tvDate = view.tv1
    val tvTime = view.tv2
    val tvFLC = view.tv3
    val tvDoneBy = view.tv4
    val tvAmount = view.tv5
    val tvPaymentStatus = view.tv6
*/

}
