package com.agnext.qualixfarmer.tea.qualityAnalysis

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.agnext.qualixfarmer.R
import com.agnext.qualixfarmer.base.*
import com.agnext.qualixfarmer.commonUi.cropCalender.CropCalender
import com.agnext.qualixfarmer.commonUi.profile.ProfileActivity
import com.agnext.qualixfarmer.commonUi.setting.SettingActivity
import com.agnext.qualixfarmer.fields.farmList.FieldListActivity
import com.agnext.qualixfarmer.network.Response.ScanData
import com.agnext.qualixfarmer.tea.addDivision.AddDivisionActivity
import com.agnext.qualixfarmer.tea.addSection.AddSectionActivity
import com.agnext.qualixfarmer.tea.qualityAnalysis.paymentCard.MyPagerAdapter
import com.agnext.qualixfarmer.tea.scanDoneDetail.ScanDoneActivity
import com.agnext.qualixfarmer.tea.scanInputScreen.ScanInputActivity
import com.agnext.qualixfarmer.utils.AlertUtil
import com.agnext.qualixfarmer.utils.IntentUtil
import com.agnext.sensenextmyadmin.ui.auth.login.RefreshInteractor
import com.agnext.sensenextmyadmin.utils.extensions.ScreenState
import com.agnext.sensenextmyadmin.utils.extensions.postDelayed
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_tea_quality_analysis.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TeaQualityAnalysisActivity : BaseActivity(), View.OnClickListener,
    NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener,
    ItemOnClick {

    private lateinit var actionBar: ActionBar
    private lateinit var viewModel: QualityAnaViewModel
    private var doubleBackToExitPressedOnce = false
    lateinit var qualityAnalysisAdapter: QualityAnalysisAdapter
    lateinit var token: String
    lateinit var currentDay: String
    lateinit var weekDay: String
    lateinit var monthDay: String
    var filterData: MutableMap<String, String> = HashMap()
    lateinit var spCommodity: Spinner
    private lateinit var tvStartDate: TextView
    private lateinit var tvEndDate: TextView
    lateinit var tvApply: TextView
    lateinit var tvCancel: TextView
    lateinit var alertDialog: AlertDialog
    private var commodityPos = 0
    val commodities = ArrayList<String>()
    var farmerId: String = ""
    var farmerCode: String = ""

       override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tea_quality_analysis)
        token = "Bearer " + SessionClass(this).getUserToken()
        viewModel = ViewModelProvider(
            this,
            QAViewModelFactory(QualityAnaInterceptor(this), RefreshInteractor(this))
        )[QualityAnaViewModel::class.java]
        viewModel.qualityAnaState.observe(::getLifecycle, ::updateUI)
        filterData["farmer_id"] = SessionClass(this).getVMSId()
//        filterData["farmer_id"] = "134"

        initView()
        rvScanHistory.isFocusable = false
        getFirebaseToken(this)
    }

    /**Init View*/
    fun initView() {
        setSupportActionBar(toolbar)
        actionBar = supportActionBar!!
        actionBar.title = "Qualix Farmer"

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, 0, 0)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()


        navigation.setNavigationItemSelectedListener(this)
        spFilter.onItemSelectedListener = this

        llProfile.setOnClickListener(this)
        // viewAllPayment.setOnClickListener(this)
        ivQrImage.setOnClickListener(this)
        farmerId = SessionClass(this).getVMSId()
        farmerCode=SessionClass(this).getVMSFarmerCode()
        ivQrImage.setImageBitmap(AlertUtil.generateQR(farmerId, this))

        tvUserName.text = SessionClass(this).getVMSName()
        tvUserId.text = farmerId
        ivFilter.setOnClickListener(this)
        var calendar = Calendar.getInstance()
        val date = calendar.time
        val dateFormat = SimpleDateFormat("MM/dd/yyyy")

        currentDay = dateFormat.format(date)
        weekDay = getCalculatedDate(currentDay, "MM/dd/yyyy", -7)
        monthDay = getCalculatedDate(currentDay, "MM/dd/yyyy", -30)

//        //Api for Scan list
//        if (hasConnection(this))
//            viewModel.getScansListVm(token, currentDay, weekDay)
//        else
//            AlertUtil.showToast(this, getString(R.string.internet_issue))

        //Api for Avg Data
        if (hasConnection(this))
            viewModel.getAvgScanVM(token)
        else
            AlertUtil.showToast(this, getString(R.string.internet_issue))
        progress.visibility = View.VISIBLE
        viewModel.getCommodity(SessionClass(this).getVMSToken())

    }

    private fun initPager() {
        pager.adapter = MyPagerAdapter(supportFragmentManager, viewModel)
        // pageIndicatorView.setViewPager(pager)
    }

    /**UI  State Observer*/
    private fun updateUI(screenState: ScreenState<QualityState>?) {
        when (screenState) {
            ScreenState.Loading -> progress.visibility = View.VISIBLE
            is ScreenState.Render -> processLoginState(screenState.renderState)
        }
    }

    private fun processLoginState(renderState: QualityState) {
        //   viewAllPayment.visibility = View.VISIBLE

        when (renderState) {
            QualityState.scansListSuccess -> {
                //setting the recycleView
                progress.visibility = View.GONE
                tvNoRecord.visibility = View.GONE
                //   viewAllPayment.visibility = View.VISIBLE
                rvScanHistory.visibility = View.VISIBLE

                rvScanHistory.layoutManager = LinearLayoutManager(this)
                qualityAnalysisAdapter =
                    QualityAnalysisAdapter(this, viewModel.scanQualixList.value!!, this)
                rvScanHistory.adapter = qualityAnalysisAdapter
            }
            QualityState.noScanListSuccess -> {
                progress.visibility = View.GONE
                tvNoRecord.visibility = View.VISIBLE
//                viewAllPayment.visibility = View.GONE
                rvScanHistory.visibility = View.GONE

            }
            QualityState.scansListFailure -> {
                progress.visibility = View.GONE
                AlertUtil.showToast(this, "Error to get data")
            }

            QualityState.tokenExpired -> {
                logout(this)
            }

            /*AvgScanData*/
            QualityState.avgScanDataSuccess -> {
                initPager()
            }

            QualityState.avgScanDataFaliure -> {
                pager.visibility = View.GONE
            }

            QualityState.monthFlcDataSuccess -> {
                //setting data in Graph
                //  graph()
            }
            QualityState.monthFlcDataFailure -> {
                //   AlertUtil.showToast(this, "Error to get data")
            }

            QualityState.commodityFailure -> {
                AlertUtil.showToast(this, viewModel.errorMsg)
            }

            QualityState.commoditySuccess -> {

            }
        }
    }

    /**Hide Navigation Item*/
//    private fun hideItem() {
//        val navMenu = navigation.menu
//        if (SessionClass(this).getBaseUrl() == Constant.BaseURlTea) {
//            navMenu.findItem(R.id.action_field).isVisible = true
//            navMenu.findItem(R.id.action_crop_calendar).isVisible = true
//        } else {
//            navMenu.findItem(R.id.action_field).isVisible = false
//            navMenu.findItem(R.id.action_crop_calendar).isVisible = false
//
//        }
//    }


    /**User define Callback*/
    override fun onItemClick(position: Int) {
//        val intent = Intent(this, ScanDetailActivity::class.java)
//        val gsonObj = Gson()
//        val type = object : TypeToken<ScanData>() {}.type
//        val json = gsonObj.toJson(viewModel.scanList.value!![position], type)
//        intent.putExtra("selectObject", json)
//        startActivity(intent)
        val intent = Intent(this, ScanDetailQualixActivity::class.java)
        val gsonObj = Gson()
        val type = object : TypeToken<ScanData>() {}.type
        val json = gsonObj.toJson(viewModel.scanQualixList.value!![position], type)
        intent.putExtra("selectObject", json)
        startActivity(intent)
    }

    /**System Callback Listener*/
    //1 Navigation Item Selected Listener
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_quality_ana -> {
                drawer_layout.closeDrawer(GravityCompat.START)
            }

            R.id.action_add_division -> {
                drawer_layout.closeDrawer(GravityCompat.START)
                IntentUtil.moveNextScreen(this, AddDivisionActivity::class.java)
            }
            R.id.action_add_section -> {
                drawer_layout.closeDrawer(GravityCompat.START)
                IntentUtil.moveNextScreen(this, AddSectionActivity::class.java)
            }

            R.id.action_field -> {
                drawer_layout.closeDrawer(GravityCompat.START)
                IntentUtil.moveNextScreen(this, FieldListActivity::class.java)
            }

            R.id.action_tea -> {
                drawer_layout.closeDrawer(GravityCompat.START)
                IntentUtil.moveNextScreen(this, ScanInputActivity::class.java)
            }

            R.id.action_scan_done -> {
                drawer_layout.closeDrawer(GravityCompat.START)
                IntentUtil.moveNextScreen(this, ScanDoneActivity::class.java)
            }
            R.id.action_crop_calendar -> {
                drawer_layout.closeDrawer(GravityCompat.START)
                IntentUtil.moveNextScreen(this, CropCalender::class.java)
            }
//            R.id.action_scan_history -> {
//                drawer_layout.closeDrawer(GravityCompat.START)
//                IntentUtil.moveNextScreen(this, ScanHistoryActivity::class.java)
//            }

            R.id.action_profile -> {
                drawer_layout.closeDrawer(GravityCompat.START)
                IntentUtil.moveNextScreen(this, ProfileActivity::class.java)
            }
            R.id.action_setting -> {
                drawer_layout.closeDrawer(GravityCompat.START)
                IntentUtil.moveNextScreen(this, SettingActivity::class.java)
            }
            R.id.action_logout -> {
                drawer_layout.closeDrawer(GravityCompat.START)
                showLogoutDialog(getString(R.string.logout), getString(R.string.logout_text), this)
            }
        }
        return true
    }


    //2 onClick Listener
    override fun onClick(view: View?) {
        when (view) {
            ivFilter -> {
                filterDialog()
            }

            ivQrImage -> {
                AlertUtil.imageDialog(this, AlertUtil.generateQR(farmerId, this))
            }
            llProfile -> {
                IntentUtil.moveScreenIntent(this, ProfileActivity::class.java, false)
            }
            tvApply -> {
                alertDialog.dismiss()
                progress.visibility = View.VISIBLE
                viewModel.getScanHistory(filterData)
            }
            tvCancel -> {
                alertDialog.dismiss()
                filterData.clear()
                viewModel.getScanHistory(filterData)
            }
        }
    }

    //3 Back Pressed
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish()
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        AlertUtil.showToast(this, getString(R.string.double_click_to_exit))
        postDelayed(2000) { doubleBackToExitPressedOnce = false }

    }

    //4 Spinner Callback
    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onItemSelected(spinner: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
        when (spinner) {

            spFilter -> {
                when (pos) {
                    0 -> viewModel.getScanHistory(filterData)
                    1 -> {

                        filterData["date_from"] = timeToEpoch(monthDay).toString()
                        filterData["date_to"] = timeToEpoch(currentDay).toString()

                        viewModel.getScanHistory(filterData)
                    }

                    2 -> {
                        filterData["date_from"] = timeToEpoch(weekDay).toString()
                        filterData["date_to"] = timeToEpoch(currentDay).toString()
                        viewModel.getScanHistory(filterData)
                    }


                }
            }
            spCommodity -> {
                if (pos > 0) {
                    filterData["commodity_id"] =
                        viewModel.commodityList[pos - 1].commodity_id.toString()
                    commodityPos = pos
                } else if (pos == 0) {
                    filterData.remove("commodity_id")
                }
            }
        }
    }

    private fun filterDialog() {
        //Api hit for filter data
        //  allFilterApis(91)
        //Create Filter Dialog
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.layout_filter, null)
        dialogBuilder.setView(dialogView)
        tvStartDate = dialogView.findViewById(R.id.tvStartDate)
        tvEndDate = dialogView.findViewById(R.id.tvEndDate)
        spCommodity = dialogView.findViewById(R.id.spCommodity)
        tvApply = dialogView.findViewById(R.id.tvApply)
        tvCancel = dialogView.findViewById(R.id.tvCancel)

        spCommodity.onItemSelectedListener = this

        tvStartDate.setOnClickListener(this)
        tvEndDate.setOnClickListener(this)
        tvApply.setOnClickListener(this)
        tvCancel.setOnClickListener(this)

        commodities.clear()
        commodities.add("Select commodity")
        for (i in 0 until viewModel.commodityList.size) {
            commodities.add(viewModel.commodityList[i].commodity_name.toString())
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, commodities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCommodity.adapter = adapter

        alertDialog = dialogBuilder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()

        val handler = Handler()
        handler.postDelayed({
            setFilterData()
        }, 1000)
    }

    fun setFilterData() {
        if (commodityPos != 0 && commodities.size >= commodityPos)
            spCommodity.setSelection(commodityPos)
    }

    fun timeToEpoch(date: String): Long {
        val date = SimpleDateFormat("MM/dd/yyyy").parse(date)
        return date.time
    }

}
