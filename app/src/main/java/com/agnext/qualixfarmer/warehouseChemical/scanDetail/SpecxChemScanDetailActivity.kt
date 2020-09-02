package com.agnext.qualixfarmer.warehouseChemical.scanDetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.agnext.qualixfarmer.R
import com.agnext.qualixfarmer.base.BaseActivity
import com.agnext.qualixfarmer.network.Response.ResSpecxChemicalScans
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_scan_detail3.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import java.io.LineNumberReader

class SpecxChemScanDetailActivity : BaseActivity() {

    lateinit var testObject: ResSpecxChemicalScans.ChemicalScan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_detail3)
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
            val type = object : TypeToken<ResSpecxChemicalScans.ChemicalScan>() {}.type
            testObject = gson.fromJson(selectObject, type)
        }

        if (testObject != null) {
            tvCount.text = testObject.cropName
            tvReference.text = testObject.batchId
            tvCommodity.text = "${testObject.createdOn} , ${testObject.time}"
            scanDetailRV()
        }
    }

    /**Setting data in RV for scan detail*/
    private fun scanDetailRV()
    {
        rvScanInfo.layoutManager=LinearLayoutManager(this)
        rvScanInfo.adapter=SpecxChemDetailAdapter(this, testObject.analysesList!!)
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
