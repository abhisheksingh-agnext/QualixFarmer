package com.agnext.qualixfarmer.tea.qualityAnalysis.paymentCard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.agnext.qualixfarmer.R
import kotlinx.android.synthetic.main.card_payment_yearly.*

class YearlyPaymentFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.card_payment_yearly, container, false)
    }

    open fun newInstanceYearlyPaymentFragment(score:String ,count:String): YearlyPaymentFragment {
        val f = YearlyPaymentFragment()
        // Supply index input as an argument.
        val args = Bundle()
        args.putString("score", score)
        args.putString("count", count)
        f.arguments = args
        return f
    }
//    tvAvgEarnyear   tvAvgScanYear

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments
        val score = args!!.getString("score","--" )
        val count = args!!.getString("count","--" )
        if(score=="0" || count=="0")
        {
            tvAvgEarnYear.text="0"
            tvAvgAcceptYear.text="0"
        }
        else  if(score=="--" || count=="--"){
            tvAvgEarnYear.text="--"
            tvAvgAcceptYear.text="--"
        }
        tvAvgScoreYear.text=score
        tvAvgScanYear.text=count
    }}
