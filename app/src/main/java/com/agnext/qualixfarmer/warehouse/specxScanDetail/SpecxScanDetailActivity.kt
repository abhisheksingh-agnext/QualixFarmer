package com.agnext.qualixfarmer.warehouse.specxScanDetail

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.agnext.qualixfarmer.R
import com.agnext.qualixfarmer.base.BaseActivity
import com.agnext.qualixfarmer.base.SessionClass
import com.agnext.qualixfarmer.network.ApiClient
import com.agnext.qualixfarmer.network.ApiInterface
import com.agnext.qualixfarmer.network.Response.ResSpecxScanDetail
import com.agnext.qualixfarmer.network.Response.ResSpecxScans
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_specx_scan_detail.*
import kotlinx.android.synthetic.main.activity_specx_scan_detail.progress
import kotlinx.android.synthetic.main.custom_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SpecxScanDetailActivity : BaseActivity() {
    lateinit var testObject: ResSpecxScans
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_specx_scan_detail)
        progress.visibility= View.VISIBLE
        initViews()
    }

    /**Init View*/
    private fun initViews() {
        toolbar.title = getString(R.string.scan_detail)
        setSupportActionBar(toolbar)
        //enabling back button
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        setData()
    }

    fun setData() {
        //getting data from intent
        val selectObject = intent.getStringExtra("selectObject")
        val gson = Gson()
        if (selectObject != null) {
            val type = object : TypeToken<ResSpecxScans>() {}.type
            testObject = gson.fromJson(selectObject, type)
        }

        if (testObject != null) {
            tvCount.text = testObject.total_count
            tvReference.text = testObject.reference_id
            tvCommodity.text = testObject.commodity_name
           // tvDate.text = getDateFromEpoch(testObject.created_on!!)
            ScanResult(testObject.id!!)
        }
    }


    fun ScanResult(id: String) {
        val apiService = ApiClient.getClient().create(ApiInterface::class.java)
        val call = apiService.getSpecxScanResult("Bearer ${SessionClass(this).getUserToken()}", id)
        call.enqueue(object : Callback<ArrayList<ResSpecxScanDetail>> {
            override fun onFailure(call: Call<ArrayList<ResSpecxScanDetail>>, t: Throwable) {
                progress.visibility=View.GONE

            }

            override fun onResponse(
                call: Call<ArrayList<ResSpecxScanDetail>>,
                response: Response<ArrayList<ResSpecxScanDetail>>
            ) {

                if (response.isSuccessful) {
                    if (response.body()!!.size > 1) {
                        rvScanInfo.layoutManager=LinearLayoutManager(this@SpecxScanDetailActivity)
                        rvScanInfo.adapter=SpecxScanDetailAdapter(this@SpecxScanDetailActivity,response.body()!!)
                    } else {
                        if (response.code() == 401) {
                        }
                    }

                }
                progress.visibility= View.GONE
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}

