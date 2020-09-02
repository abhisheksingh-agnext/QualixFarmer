package com.agnext.qualixfarmer.tea.qualityAnalysis.paymentCard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.agnext.qualixfarmer.R
import kotlinx.android.synthetic.main.card_payment_monthly.*

class MonthlyPaymentFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.card_payment_monthly, container, false)
    }

    open fun newInstanceWeeklyPaymentFragment(score:String ,count:String): MonthlyPaymentFragment {
        val f = MonthlyPaymentFragment()
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
            tvAvgEarnMonth.text="0"
            tvAvgAcceptMonth.text="0"
        }
        else  if(score=="--" || count=="--"){
            tvAvgEarnMonth.text="--"
            tvAvgAcceptMonth.text="--"
        }
        tvAvgScoreMonth.text=score
        tvAvgScanMonth.text=count
    }}
