package com.agnext.qualixfarmer.fields.addFarmSeason

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.agnext.qualixfarmer.R
import com.agnext.qualixfarmer.base.BaseActivity
import com.agnext.qualixfarmer.utils.AlertUtil
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_add_season.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddSeasonActivity : BaseActivity(),View.OnClickListener, AdapterView.OnItemSelectedListener  {


    var cropArray:ArrayList<String> = ArrayList()
    var varietyArray:ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_season)
        initView()
    }

    /**UI methods*/
    fun initView() {
        //setting toolbar
        toolbar.title = "Add Season"
        setSupportActionBar(toolbar)
        //enabling back button
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        //Click register
        btnSaveSeason.setOnClickListener(this)
        tvStartDate.setOnClickListener(this)
        tvEndDate.setOnClickListener(this)
        readyData()
    }

    fun readyData()
    {
        cropArray.add("Wheat")
        cropArray.add("Rice")
        cropArray.add("Moong")
        cropArray.add("Tea")
        cropArray.add("Grapes")


        varietyArray.add("var 1")
        varietyArray.add("var 2")
        varietyArray.add("var 3")
        varietyArray.add("var 4")


        val adapterCrop =
            ArrayAdapter<String>(applicationContext, R.layout.spinner_layout, cropArray)
        val adapterVar =
            ArrayAdapter<String>(applicationContext, R.layout.spinner_layout, varietyArray)

        spinnerCrop.adapter = adapterCrop
        spinnerCropVariety.adapter=adapterVar

        spinnerCrop.onItemSelectedListener = this
        spinnerCrop.onItemSelectedListener = this

    }

    /**Override methods*/
    override fun onClick(view: View?) {
        when(view)
        {
            btnSaveSeason->{addSeason()}
            tvStartDate->{datePickerDialog(tvStartDate)}
            tvEndDate->{datePickerDialog(tvEndDate)}
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    /**Logical methods*/

    fun addSeason() {
        if (tvSeasonName.text!!.isNotEmpty() && tvSeasonCode.text!!.isNotEmpty() && tvStartDate.text!!.isNotEmpty() && tvEndDate.text!!.isNotEmpty()) {
           var intent = Intent()
            val gson = Gson()
            val type = object : TypeToken<SeasonData>() {}.type
            val json = gson.toJson(SeasonData(tvSeasonName.text.toString(),tvSeasonCode.text.toString(),spinnerCrop.selectedItem.toString(),spinnerCropVariety.selectedItem.toString() ,tvStartDate.text.toString(),tvEndDate.text.toString()), type)
            intent.putExtra("selectObject", json)
            setResult(122 , intent)
            finish()
        } else
        {
            AlertUtil.showToast(this,"Fill all details")
        }
    }
    private fun datePickerDialog(textView: TextInputEditText) {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year: Int, monthOfYear: Int, dayOfMonth: Int ->


                val fmt = SimpleDateFormat("dd/MM/yyyy")
                val month = monthOfYear + 1
                val date = fmt.parse("$dayOfMonth/$month/$year")

                val fmtOut = SimpleDateFormat("dd/MM/yyyy")
                textView.setText(fmtOut.format(date))

            }, year, month, day
        )

        var datePicker = dpDialog.datePicker
        var calendar = Calendar.getInstance()
        datePicker.maxDate = calendar.timeInMillis

        dpDialog.show()

    }

}

data class SeasonData(var season  :String, var  seasonCode:String,var Crop:String,var variety:String,var startDate :String,var endDate :String)
