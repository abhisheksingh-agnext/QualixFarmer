package com.agnext.qualixfarmer.tea.qualityAnalysis.paymentCard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.agnext.qualixfarmer.R
import kotlinx.android.synthetic.main.card_payment_weekly.*

class WeeklyPaymentFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.card_payment_weekly, container, false)
    }

    open fun newInstanceWeeklyPaymentFragment(score:String ,count:String): WeeklyPaymentFragment {
        val f = WeeklyPaymentFragment()
        // Supply index input as an argument.
        val args = Bundle()
        args.putString("score", score)
        args.putString("count", count)
        f.arguments = args
        return f
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments
        val score = args!!.getString("score","--" )
        val count = args!!.getString("count","--" )
        if(score=="0" || count=="0")
        {
            tvAvgEarnWeek.text="0"
            tvAvgAcceptWeek.text="0"
        }
        else  if(score=="--" || count=="--"){
            tvAvgEarnWeek.text="--"
            tvAvgAcceptWeek.text="--"
        }
        tvAvgScoreWeek.text=score
        tvAvgScanWeek.text=count
    }}

