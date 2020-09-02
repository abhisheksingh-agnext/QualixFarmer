package com.agnext.qualixfarmer.warehouse.specxAllScans

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.agnext.qualixfarmer.R
import com.agnext.qualixfarmer.base.BaseActivity
import com.agnext.qualixfarmer.base.SessionClass
import com.agnext.qualixfarmer.network.ApiClient
import com.agnext.qualixfarmer.network.ApiInterface
import com.agnext.qualixfarmer.network.Response.ResSpecxScans
import com.agnext.qualixfarmer.warehouse.specxQualityAnalysis.ItemOnClick
import com.agnext.qualixfarmer.warehouse.specxQualityAnalysis.SpecxQualityAdapter
import kotlinx.android.synthetic.main.activity_specx_quality_analysis.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SpecxAllScansActivity : BaseActivity(),
    ItemOnClick {
    override fun onItemClick(position: Int) {
    }

    var scanArrayList = ArrayList<ResSpecxScans>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_specx_all_scans)
        initView()
    }
    fun initView()
    {
        //setting toolbar
        toolbar.title="Scans"
        setSupportActionBar(toolbar)
        //enabling back button
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        specxScanList("","")

    }


    /**Api hit ScanList*/
    private fun specxScanList(fromDate: String, toDate: String) {
        progress.visibility = View.VISIBLE
        val apiService = ApiClient.getClient().create(ApiInterface::class.java)
        val call = apiService.getSpecxScans(
            "Bearer ${SessionClass(this).getUserToken()}",
            "1583132399","1583132399"
//            fromDate,
//            toDate
        )
        call.enqueue(object : Callback<ArrayList<ResSpecxScans>> {
            override fun onFailure(call: Call<ArrayList<ResSpecxScans>>, t: Throwable) {
                Log.e("error", "error")
                progress.visibility = View.GONE

            }

            override fun onResponse(
                call: Call<ArrayList<ResSpecxScans>>,
                response: Response<ArrayList<ResSpecxScans>>
            ) {

                if (response.isSuccessful) {
                    if (response.body()!!.size > 1) {
//                        tvNoRecord.visibility= View.GONE
                      //  rvSpecxScanHistory.visibility= View.VISIBLE
                        scanArrayList = response.body()!!
                        Log.e("data", "data")
                        rvSpecxScanHistory.layoutManager =
                            LinearLayoutManager(this@SpecxAllScansActivity)
                        rvSpecxScanHistory.adapter =
                            SpecxQualityAdapter(
                                this@SpecxAllScansActivity,
                                scanArrayList,
                                this@SpecxAllScansActivity
                            )


                    } else {
                     //   tvNoRecord.visibility= View.VISIBLE
                      //  rvSpecxScanHistory.visibility= View.GONE

                        if (response.code() == 401) {
                        }
                    }

                }
                progress.visibility = View.GONE
            }
        })

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
