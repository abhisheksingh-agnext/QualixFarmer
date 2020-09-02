package com.agnext.qualixfarmer.tea.qualityAnalysis.paymentCard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.agnext.qualixfarmer.R
import com.agnext.qualixfarmer.tea.qualityAnalysis.*

import kotlinx.android.synthetic.main.card_payment_today.*





class TodayPaymentFragment : Fragment() {

    private lateinit var viewModel: QualityAnaViewModel

/**Instance TodayPaymentFragment*/
   open fun newInstanceTodayPaymentFragment(score:String ,count:String): TodayPaymentFragment {
        val f = TodayPaymentFragment()
        // Supply index input as an argument.
        val args = Bundle()
        args.putString("score", score)
        args.putString("count", count)
        f.arguments = args
        return f
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.card_payment_today, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments
        val score = args!!.getString("score","--" )
        val count = args!!.getString("count","--" )
        if(score=="0" || count=="0")
        {
            tvTotalEarnToday.text="0"
            tvAcceptanceToday.text="0"
        }
        else  if(score=="--" || count=="--"){
            tvTotalEarnToday.text="--"
            tvAcceptanceToday.text="--"
        }
        tvNoScansToday.text= count
        tvAvgScoreToday.text=score
    }}


