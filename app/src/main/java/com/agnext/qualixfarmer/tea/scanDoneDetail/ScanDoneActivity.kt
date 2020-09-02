package com.agnext.qualixfarmer.tea.scanDoneDetail

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.agnext.qualixfarmer.R
import com.agnext.qualixfarmer.base.BaseActivity
import com.agnext.qualixfarmer.base.SessionClass
import com.agnext.qualixfarmer.commonUi.login.LoginActivity
import com.agnext.qualixfarmer.utils.IntentUtil
import com.agnext.sensenextmyadmin.utils.extensions.ScreenState
import kotlinx.android.synthetic.main.activity_scan_done.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.layout_progress.*


class ScanDoneActivity : BaseActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {
    private lateinit var viewModel: ScanAreaViewModel
    var sectionId: String? = null
    lateinit var token: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_done)
        initView()
    }

    /**InitView*/
    fun initView() {
        //setting toolbar
        toolbar.title = getString(R.string.scan_done_detail)
        setSupportActionBar(toolbar)
        //enabling back button
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        // SetOnClickListener
        confirmUpload.setOnClickListener(this)

        spSection.onItemSelectedListener = this
        spDivision.onItemSelectedListener = this

        //VM
        viewModel = ViewModelProvider(
            this,
            ScanAreaViewModelFactory(ScanAreaInteractor())
        )[ScanAreaViewModel::class.java]
        viewModel.scanAreaState.observe(::getLifecycle, ::updateUI)

        token = "Bearer " + SessionClass(this).getUserToken()
        // Api hit to get Data
        viewModel.getGardenData(token, SessionClass(this).getUserId())
    }

    /**UI  State Observer*/
    private fun updateUI(screenState: ScreenState<ScanAreaState>?) {
        when (screenState) {
            ScreenState.Loading -> progress.visibility = View.VISIBLE
            is ScreenState.Render -> processLoginState(screenState.renderState)
        }
    }

    private fun processLoginState(renderState: ScanAreaState) {
        progress.visibility = View.GONE
        when (renderState) {
            ScanAreaState.UserDataFailure -> {
                showToast(this, "Error to get the Division Data")
            }
            ScanAreaState.UserDataSuccess -> {
                var divisionArray = ArrayList<String>()
                for (i in 0 until viewModel.divisionList.value!!.size) {
                    divisionArray.add(viewModel.divisionList.value!![i].divisionName.toString())
                }
                val adapter =
                    ArrayAdapter(this, android.R.layout.simple_spinner_item, divisionArray)

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spDivision.adapter = adapter

            }
            ScanAreaState.UploadScanDataSuccess -> {
                showToast(this, "Scan Data saved")
                etWeight.setText("")
                etArea.setText("")
            }

            ScanAreaState.APITokenExpire -> {
                showToast(this,getString(R.string.token_expire))
                IntentUtil.moveNextScreenWithNoStack(this, LoginActivity::class.java)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            confirmUpload -> {
                if (etWeight.text.isNullOrEmpty() && etArea.text.isNullOrEmpty() && sectionId.isNullOrEmpty()) {
                    showToast(this, getString(R.string.add_all_fields))
                } else {
                    var requestData = HashMap<String, Any>()
                    requestData["sectionId"] = sectionId!!.toIntOrNull()!!
                    requestData["weight"] = etWeight.text.toString()
                    requestData["coveredArea"] = etArea.text.toString()

                    viewModel.saveScanData(token, requestData)
                }
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        when (parent) {
            spDivision -> {
                if (spDivision.selectedItem.toString() != "No Division")
                    addSectionData(position)
            }
            spSection -> {
                if (spSection.selectedItem.toString() != "No Section")
                    sectionId =
                        viewModel.divisionList.value!![spDivision.selectedItemPosition].sectionList[position].sectionId.toString()
            }
        }

    }

    private fun addSectionData(position: Int) {
        var sectionArray = ArrayList<String>()
        if(!viewModel.divisionList.value.isNullOrEmpty())
        {
        for (i in 0 until viewModel.divisionList.value!![position].sectionList.size) {
            sectionArray.add(viewModel.divisionList.value!![position].sectionList[i].name.toString())
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sectionArray)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spSection.adapter = adapter
        }
    }

    /**System Override methods*/
    //1 OnBack Press
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
