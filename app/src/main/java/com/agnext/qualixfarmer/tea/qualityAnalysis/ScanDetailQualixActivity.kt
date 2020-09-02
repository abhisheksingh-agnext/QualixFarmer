package com.agnext.qualixfarmer.tea.qualityAnalysis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.agnext.qualixfarmer.R
import com.agnext.qualixfarmer.network.Response.ResSpecxChemicalScans
import com.agnext.qualixfarmer.network.Response.ScanData
import com.agnext.qualixfarmer.utils.utils
import com.agnext.qualixfarmer.warehouseChemical.scanDetail.SpecxChemDetailAdapter
import com.github.mikephil.charting.utils.Utils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_scan_detail3.*
import kotlinx.android.synthetic.main.custom_toolbar.*

class ScanDetailQualixActivity : AppCompatActivity() {
    lateinit var testObject: ScanData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_detail_qualix)
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

    /**Set Data in screen*/
    private fun setData() {
        //getting data from intent
        val selectObject = intent.getStringExtra("selectObject")
        val gson = Gson()
        if (selectObject != null) {
            val type = object : TypeToken<ScanData>() {}.type
            testObject = gson.fromJson(selectObject, type)
        }

        if (testObject != null) {
            tvCount.text = testObject.commodityName
            tvReference.text = testObject.batchId
            tvCommodity.text =" ${utils.getTimeFromEpoch(testObject.dateDone!!.toLong())} "
            scanDetailRV()
        }
    }

    /**Setting data in RV for scan detail*/
    private fun scanDetailRV()
    {
        rvScanInfo.layoutManager= LinearLayoutManager(this)
        rvScanInfo.adapter= ScanDetailQualixAdapter(this, testObject.analysisResults!!)
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}