package com.agnext.qualixfarmer.warehouse.addWarehouse

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.agnext.qualixfarmer.R
import com.agnext.qualixfarmer.base.BaseActivity
import com.agnext.qualixfarmer.utils.utils
import kotlinx.android.synthetic.main.activity_add_warehouse.*
import kotlinx.android.synthetic.main.custom_toolbar.*

class AddWarehouseActivity : BaseActivity(), View.OnClickListener {


    var warehouseIdArray: ArrayList<String> = ArrayList()
    var cropArray: ArrayList<String> = ArrayList()
    var cropVarietyArray: ArrayList<String> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_warehouse)
        initView()
    }

    /**InitView*/
    fun initView() {
        //setting toolbar
        toolbar.title = "Add Warehouse "
        setSupportActionBar(toolbar)
        //enabling back button
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        //Click register
        tvDeliverDate.setOnClickListener(this)
        tvRequestDate.setOnClickListener(this)

        //Spinner Data
        getDataReady()
    }


    private fun getDataReady() {
        for (i in 0 until 5) {
            warehouseIdArray.add("$i")
            cropArray.add("Crop $i")
            cropVarietyArray.add("Variety $i")
        }
        setDataSpinner()
    }

    private fun setDataSpinner() {
        val adapterWarehouseId =
            ArrayAdapter<String>(applicationContext, R.layout.spinner_layout, warehouseIdArray)
        val adapterCrop =
            ArrayAdapter<String>(applicationContext, R.layout.spinner_layout, cropArray)
        val adapterCropVariety =
            ArrayAdapter<String>(applicationContext, R.layout.spinner_layout, cropVarietyArray)

        spWareId.adapter = adapterWarehouseId
        spCropId.adapter = adapterCrop
        spCropVariety.adapter = adapterCropVariety
    }


    /**System Override methods*/

    override fun onClick(view: View?) {
        when (view) {
            tvDeliverDate->{
                utils.datePickerDialog(tvDeliverDate,this)
            }
            tvRequestDate->{
                utils.datePickerDialog(tvRequestDate,this)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
