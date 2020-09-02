package com.agnext.qualixfarmer.warehouseChemical.QualityAnalysis

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.agnext.qualixfarmer.R
import com.agnext.qualixfarmer.base.*
import com.agnext.qualixfarmer.commonUi.profile.ProfileActivity
import com.agnext.qualixfarmer.commonUi.setting.SettingActivity
import com.agnext.qualixfarmer.network.Response.ResSpecxChemicalScans
import com.agnext.qualixfarmer.utils.AlertUtil
import com.agnext.qualixfarmer.utils.IntentUtil
import com.agnext.qualixfarmer.warehouse.specxQualityAnalysis.*
import com.agnext.qualixfarmer.warehouseChemical.scanDetail.SpecxChemScanDetailActivity
import com.agnext.sensenextmyadmin.utils.extensions.ScreenState
import com.agnext.sensenextmyadmin.utils.extensions.postDelayed
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_quality_analysis_chemical.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import java.text.SimpleDateFormat
import java.util.*

class QASpecxChemicalActivity : BaseActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    AdapterView.OnItemSelectedListener,
    View.OnClickListener, ItemOnClick {

    private lateinit var actionBar: ActionBar
    private var doubleBackToExitPressedOnce = false

    lateinit var currentDay: String
    lateinit var token: String

    var currentDate: Long?=null
    var weekDay: Long? = null
    var monthDay: Long? = null

    var fromCurrentDate: Long?=null
    var fromWeekDay: Long? = null
    var fromMonthDay: Long? = null

    private lateinit var viewModel: SpecxQualityAnaViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quality_analysis_chemical)

        initView()

    }

    /**Init View*/
    fun initView() {
        setSupportActionBar(toolbar)
        actionBar = supportActionBar!!
        actionBar.title = getString(R.string.qualix_ware)
        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, 0, 0)
        toggle.isDrawerIndicatorEnabled = true
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        //Dates for today, week, month
        var calendar = Calendar.getInstance()
        val date = calendar.time
        val dateFormat = SimpleDateFormat("MMM dd yyyy HH:mm:ss.SSS zzz")

        currentDay = dateFormat.format(date)
        weekDay =getCalculatedEpochDate(currentDay!!, "MMM dd yyyy HH:mm:ss.SSS zzz", -7)
        monthDay = getCalculatedEpochDate(currentDay!!, "MMM dd yyyy HH:mm:ss.SSS zzz", -30)
        currentDate=getCalculatedEpochDate(currentDay!!, "MMM dd yyyy HH:mm:ss.SSS zzz", 0)

        fromCurrentDate=getCalculatedEpochDate(currentDay!!, "MMM dd yyyy ", 0)
        fromWeekDay=getCalculatedEpochDate(currentDay!!, "MMM dd yyyy ", -7)
        fromMonthDay = getCalculatedEpochDate(currentDay!!, "MMM dd yyyy ", -30)

        //Register listener
        navigation.setNavigationItemSelectedListener(this)
        spFilter.onItemSelectedListener = this
        ivQrImage.setOnClickListener(this)
        llProfile.setOnClickListener(this)
        viewAllPayment.setOnClickListener(this)


        //Initiate the VM
        viewModel = ViewModelProvider(
            this,
            SpecxQualityAnaViewModelFactory(SpecxQualityInteractor(this))
        )[SpecxQualityAnaViewModel::class.java]
        viewModel!!.specxQualityState.observe(::getLifecycle, ::updateUI)


        //Basic data setting
        tvUserName.text = SessionClass(this).getUserName()
        tvTi.text="Vendor ID"
        tvUserId.text = "${SessionClass(this).getUserId()}"

        token = "Bearer ${SessionClass(this).getUserToken()}"

        //Api call
        viewModel.getSpecxChemicalScans(token)
    }


    /**UI  State Observer*/
    private fun updateUI(screenState: ScreenState<SpecxQualityState>?) {
        when (screenState) {
            ScreenState.Loading -> progress.visibility = View.VISIBLE
            is ScreenState.Render -> processLoginState(screenState.renderState)
        }
    }

    private fun processLoginState(renderState: SpecxQualityState) {
        progress.visibility = View.GONE
        when (renderState) {
            SpecxQualityState.QASpecxChemicalListFailure->{
                AlertUtil.showToast(this,"Specx  Scan data failure")

                tvNoRecord.visibility=View.VISIBLE
                rvSpecxChemicalScan.visibility=View.GONE
            }
            SpecxQualityState. QASpecxChemicalListSuccess->{
//                rvSpecxChemicalScan

                tvNoRecord.visibility=View.GONE
                rvSpecxChemicalScan.visibility=View.VISIBLE

                //setting list in recycle view
                rvSpecxChemicalScan.layoutManager =
                    LinearLayoutManager(this@QASpecxChemicalActivity)
                rvSpecxChemicalScan.adapter =
                    QASpecxChemAdapter(
                        this@QASpecxChemicalActivity,
                        viewModel.specxChemAllScans.value!!,
                        this@QASpecxChemicalActivity
                    )
            }
            SpecxQualityState.ExpireToken->{
                logout(this)}

        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_quality_ana -> {
                drawer_layout.closeDrawer(GravityCompat.START)
            }

            R.id.action_crop_calendar -> {
                drawer_layout.closeDrawer(GravityCompat.START)
                //     IntentUtil.moveNextScreen(this, CropCalender::class.java)
            }
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
        return true    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
        when (pos) {
//            0 -> viewModel.getQualityScanList(fromCurrentDate.toString(), currentDate.toString(),token)
//            1 -> viewModel.getQualityScanList(fromWeekDay.toString(), currentDate.toString(),token)
//            2 -> viewModel.getQualityScanList(fromMonthDay.toString(), currentDate.toString(),token)

        }
    }

    override fun onClick(view: View?) {
        when (view) {
        ivQrImage -> AlertUtil.imageDialog(this, AlertUtil.generateQR("123456", this))
        llProfile -> IntentUtil.moveScreenIntent(this, ProfileActivity::class.java, false)
//        viewAllPayment -> IntentUtil.moveScreenIntent(
//        this,
//        SpecxAllScansActivity::class.java,
//        false
//        )

        }}

    override fun onItemClick(position: Int) {

        var intent = Intent(this, SpecxChemScanDetailActivity::class.java)
        val gson = Gson()
        val type = object : TypeToken<ResSpecxChemicalScans.ChemicalScan>() {}.type
        val json = gson.toJson(viewModel.specxChemAllScans.value!![position], type)
        intent.putExtra("selectObject", json)
        startActivity(intent)
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

}
