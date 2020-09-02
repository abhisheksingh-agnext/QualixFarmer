package com.agnext.qualixfarmer.tea.scanHistory

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.agnext.qualixfarmer.R
import com.agnext.qualixfarmer.base.BaseActivity
import com.agnext.qualixfarmer.base.SessionClass
import com.agnext.qualixfarmer.base.getCalculatedDate
import com.agnext.qualixfarmer.commonUi.login.LoginActivity
import com.agnext.qualixfarmer.network.ApiClient
import com.agnext.qualixfarmer.network.ApiInterface
import com.agnext.qualixfarmer.network.Response.ResUserScans
import com.agnext.qualixfarmer.network.Response.ScanDetail
import com.agnext.qualixfarmer.tea.scanDetail.ScanDetailActivity
import com.agnext.qualixfarmer.utils.AlertUtil
import com.agnext.qualixfarmer.utils.IntentUtil
import com.borax12.materialdaterangepicker.date.DatePickerDialog
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IFillFormatter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_scan_history.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.layout_progress.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ScanHistoryActivity : BaseActivity(),
    DatePickerDialog.OnDateSetListener, ItemClickListener {

    var scanHistoryList: ArrayList<ScanDetail> = ArrayList()
    lateinit var chart: CombinedChart
    lateinit var currentDay: String
    lateinit var weekDay: String
    lateinit var monthDay: String
    lateinit var yearDay: String

    val WEEK_DATA = "WEEK_DATA"
    val MONTH_DATA = "MONTH_DATA"
    val YEAR_DATA = "YEAR_DATA"

    private var timeList = java.util.ArrayList<String>()
    private var countList = java.util.ArrayList<Float>()
    private var avgScoreList = java.util.ArrayList<Float>()
    lateinit var scanListAdapter: AdapterScanHistory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_history)
        initView()
    }

    /**InitView*/
    fun initView() {
        //setting toolbar
        toolbar.title = getString(R.string.scan_history)
        setSupportActionBar(toolbar)
        //enabling back button
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        /*Move the focus to top*/
        rvScanHistory.isFocusable = false
        //set Dummy Data
        rvScanHistory.layoutManager = LinearLayoutManager(this@ScanHistoryActivity)
        scanListAdapter = AdapterScanHistory(this@ScanHistoryActivity, scanHistoryList, this)
        rvScanHistory.adapter = scanListAdapter

        progress.visibility = View.VISIBLE

        var calendar = Calendar.getInstance()
        val date = calendar.time
        val dateFormat = SimpleDateFormat("MM/dd/yyyy")

        currentDay = dateFormat.format(date)
        weekDay = getCalculatedDate(currentDay!!, "MM/dd/yyyy", -7)
        monthDay = getCalculatedDate(currentDay!!, "MM/dd/yyyy", -30)
        apiHitUserScans(currentDay, weekDay)
        graph()

    }


    private fun apiHitUserScans(startDay: String, endDay: String) {
        try {
            val apiService = ApiClient.getClient().create(ApiInterface::class.java)
            val call = apiService.getScans("Bearer " + SessionClass(this).getUserToken(), "0", "100",endDay,startDay)
            call.enqueue(object : Callback<ResUserScans> {
                override fun onResponse(
                    call: Call<ResUserScans>,
                    response: Response<ResUserScans>
                ) {

                    if (response.isSuccessful) {
                        var loginResponse = response.body()
                        scanHistoryList.clear()
                        scanHistoryList.addAll(loginResponse!!.data!!)
                        scanListAdapter.notifyDataSetChanged()
                        progress.visibility = View.GONE

                    } else {
//                        listener.onError()
                        AlertUtil.showToast(this@ScanHistoryActivity, response.message())
                        if (response.code() == 401) {
                            AlertUtil.showToast(this@ScanHistoryActivity, "Server session expired")
                            IntentUtil.moveNextScreenWithNoStack(
                                this@ScanHistoryActivity,
                                LoginActivity::class.java
                            )
                        }
                    }

                }

                override fun onFailure(call: Call<ResUserScans>, t: Throwable) {
                    Log.e("errror", "errror")
//                    listener.onError()
                }
            })

        } catch (e: Exception) {
            Log.e("error", e.toString())
            e.printStackTrace()
        }
    }

    /**----------------------------Option menu*/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.tea_sorting_items, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_custom -> {
                openDateRangePicker()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**Graph Functionality*/
    //1 Graph
    private fun graph() {

        chart = findViewById(R.id.chart)
        chart!!.setViewPortOffsets(40f, 10f, 10f, 20f)
        chart!!.description.isEnabled = false
        chart!!.setTouchEnabled(true)
        chart!!.isDragEnabled = true
        chart!!.setScaleEnabled(true)
        chart!!.setPinchZoom(true)
        chart!!.setDrawGridBackground(false)
        chart!!.maxHighlightDistance = 200f

        chart!!.animateXY(500, 500)
        chart!!.invalidate()

        /*Call to draw graph*/
        typeFilterData(WEEK_DATA)
    }

    //2 Graph
    private fun typeFilterData(requestTime: String) {
        var dateFrom = currentDay
        var dateTo = currentDay

        when (requestTime) {
            WEEK_DATA -> dateFrom = weekDay
            MONTH_DATA -> dateFrom = monthDay
            YEAR_DATA -> dateFrom = yearDay
        }
        // getFilterData(dateFrom, dateTo)
        getFilterData()
    }

    //3 Graph
    private fun getFilterData() {
        val rand = Random()
        /*Dummy dat
        * Replace it the Api Data*/
        for (i in 0 until 10) {
            // Generate random integers in range 0 to 999
            val randInt1 = rand.nextInt(100)
            val randInt2 = rand.nextInt(100)
            val randInt3 = rand.nextInt(100)
            timeList!!.add(randInt1.toString())
            countList!!.add(randInt2.toFloat())
            avgScoreList!!.add(randInt3.toFloat())
        }
        generateCombineGraph(countList, avgScoreList)
    }

    //4 Graph
    private fun generateCombineGraph(
        avgCountList: java.util.ArrayList<Float>,
        avgScoreList: java.util.ArrayList<Float>
    ) {
        var combineData = CombinedData()

        combineData.setData(generateLineData(avgScoreList))
        combineData.setData(generateBarData(avgCountList))
        chart!!.data = combineData
    }


    //5 Graph
    private fun generateBarData(avgScoreList: java.util.ArrayList<Float>): BarData {

        var entries2 = ArrayList<BarEntry>()

        for (index in 0 until avgScoreList.size) {
            entries2.add(BarEntry(index.toFloat(), avgScoreList[index]))
        }

        val set2 = BarDataSet(entries2, getString(R.string.flc_count))
        set2.stackLabels = arrayOf(getString(R.string.flc_count))
        set2.setColors(Color.rgb(61, 165, 255))
        set2.valueTextColor = Color.rgb(61, 165, 255)
        set2.valueTextSize = 10f
        set2.axisDependency = YAxis.AxisDependency.LEFT

        val barWidth = 0.45f // x2 dataset
        val d = BarData(set2)
        d.barWidth = barWidth

        return d
    }

    //6 Graph
    private fun generateLineData(avgCountList: java.util.ArrayList<Float>): LineData {
        val values =
            java.util.ArrayList<Entry>()
        for (index in 0 until avgCountList.size) {
            values.add(Entry(index.toFloat(), avgCountList[index]))
        }
        val set1: LineDataSet

        // create a dataset and give it a type
        set1 = LineDataSet(values, getString(R.string.no_of_scans))
        set1.mode = LineDataSet.Mode.CUBIC_BEZIER
        set1.cubicIntensity = 0.2f
        set1.setDrawCircles(false)
        set1.lineWidth = 1.8f
        set1.circleRadius = 4f
        set1.setCircleColor(Color.WHITE)
        set1.color = resources.getColor(R.color.colorPrimary1)
        set1.fillAlpha = 100
        set1.setDrawHorizontalHighlightIndicator(false)
        set1.fillFormatter =
            IFillFormatter { dataSet, dataProvider -> chart!!.axisLeft.axisMinimum }
        // create a data object with the data sets
        val data = LineData(set1)
        data.setValueTextSize(9f)
        data.setDrawValues(false)

        return data
    }


    /**System Override methods*/

    override fun onItemClick(pos: Int) {
        val intent = Intent(this, ScanDetailActivity::class.java)
        val gsonObj = Gson()
        val type = object : TypeToken<ScanDetail>() {}.type
        val json = gsonObj.toJson(scanHistoryList[pos], type)
        intent.putExtra("selectObject", json)
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun openDateRangePicker() {


        var now = Calendar.getInstance();
        var dpd = DatePickerDialog.newInstance(
            this,
            now.get(Calendar.YEAR),
            now.get(Calendar.MONTH),
            now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(fragmentManager, "Datepickerdialog");

    }

    override fun onDateSet(
        view: DatePickerDialog?,
        year: Int,
        monthOfYear: Int,
        dayOfMonth: Int,
        yearEnd: Int,
        monthOfYearEnd: Int,
        dayOfMonthEnd: Int
    ) {

    }


}

data class ScanHistoryObject(
    var doneBy: String,
    var FLC: String,
    var quantity: String,
    var date: String,
    var time: String
)

