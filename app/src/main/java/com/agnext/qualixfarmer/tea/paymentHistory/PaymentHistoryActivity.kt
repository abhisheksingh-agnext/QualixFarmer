package com.agnext.qualixfarmer.tea.paymentHistory

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.agnext.qualixfarmer.R
import com.agnext.qualixfarmer.base.BaseActivity
import com.agnext.qualixfarmer.base.SessionClass
import com.agnext.qualixfarmer.network.ApiClient
import com.agnext.qualixfarmer.network.ApiInterface
import com.agnext.qualixfarmer.network.Response.ResUserScans
import com.agnext.qualixfarmer.network.Response.ScanDetail
import com.agnext.qualixfarmer.tea.qualityAnalysis.ItemOnClick
import com.agnext.qualixfarmer.tea.qualityAnalysis.QualityAnalysisAdapter
import com.agnext.qualixfarmer.utils.AlertUtil
import kotlinx.android.synthetic.main.activity_payment_history.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentHistoryActivity : BaseActivity() , ItemOnClick {
    override fun onItemClick(position: Int) {
    }

    var paymentHistoryList: ArrayList<ScanDetail> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_history)
        progress.visibility=View.VISIBLE
        initView()
    }


    /**InitView*/
    fun initView() {
        //setting toolbar
        toolbar.title = getString(R.string.payment_history)
        setSupportActionBar(toolbar)
        //enabling back button
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        //set Dummy Data
        val token=SessionClass(this).getUserToken()
        getScansListIn("Bearer $token","02/01/2020","02/25/2020")
    }


    fun getScansListIn(
        token: String,
        startDay: String,
        endDay: String
        ) {
        val apiService = ApiClient.getClient().create(ApiInterface::class.java)
        val call = apiService.getScans(token, "0", "100",
            "01/26/2020"
            , "02/25/2020")
        call.enqueue(object : Callback<ResUserScans> {
            override fun onResponse(
                call: Call<ResUserScans>,
                response: Response<ResUserScans>
            ) {

                if (response.isSuccessful) {
                    if (response.body()!!.data != null)
                    // listener.onScansListSuccess(response.body()!!.data as ArrayList<ScanDetail>)
                    {
                        paymentHistoryList= response.body()!!.data as ArrayList<ScanDetail>
                        rvScanHistory.layoutManager = LinearLayoutManager(this@PaymentHistoryActivity)
                     //   rvScanHistory.adapter = QualityAnalysisAdapter(this@PaymentHistoryActivity, paymentHistoryList,this@PaymentHistoryActivity)
                    } else
                    // listener.onNoScansListSuccess()
                        AlertUtil.showToast(this@PaymentHistoryActivity, "No Data ")

                } else {

                    if (response.code() == 401) {

                        //  listener.onTokenExpired(token, startDay, endDay)
                    } else {
                        // listener.onScansListFailure()
                    }

                }
                progress.visibility=View.GONE

            }

            override fun onFailure(call: Call<ResUserScans>, t: Throwable) {
                progress.visibility=View.GONE

                //  listener.onScansListFailure()
                AlertUtil.showToast(this@PaymentHistoryActivity, "Error to ")
            }
        })


    }

    /**System Override methods*/
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

data class PaymentHistoryObject(
    var doneBy: String,
    var FLC: String,
    var Amount: String,
    var PaymentStatus: String,
    var date: String,
    var time: String
)
