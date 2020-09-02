package com.agnext.qualixfarmer.tea.qualityAnalysis.paymentCard

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.agnext.qualixfarmer.tea.qualityAnalysis.QualityAnaViewModel

class MyPagerAdapter(
    fm: FragmentManager?,
    val viewModel: QualityAnaViewModel
) :
    FragmentPagerAdapter(fm!!) {
    override fun getItem(pos: Int): Fragment {
        return if (pos == 0)
            TodayPaymentFragment().newInstanceTodayPaymentFragment(
                viewModel.avgScanData.value!!.averageDay.toString(),
                viewModel.avgScanData.value!!.scanCountDay.toString()
            )
        else if (pos == 1) WeeklyPaymentFragment().newInstanceWeeklyPaymentFragment(
            viewModel.avgScanData.value!!.averageWeek.toString(),
            viewModel.avgScanData.value!!.scanCountWeek.toString()

        )
        else if (pos == 2) MonthlyPaymentFragment().newInstanceWeeklyPaymentFragment(
            viewModel.avgScanData.value!!.averageMonth.toString(),
            viewModel.avgScanData.value!!.scanCountMonth.toString()

        )
        else if (pos == 3) YearlyPaymentFragment().newInstanceYearlyPaymentFragment(
            viewModel.avgScanData.value!!.averageYear.toString(),
            viewModel.avgScanData.value!!.scanCountYear.toString()
        )
        else YearlyPaymentFragment()
    }

    override fun getCount(): Int {
        return 4
    }
}