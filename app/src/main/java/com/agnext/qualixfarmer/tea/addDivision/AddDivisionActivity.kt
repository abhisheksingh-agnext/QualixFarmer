package com.agnext.qualixfarmer.tea.addDivision

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.agnext.qualixfarmer.R
import com.agnext.qualixfarmer.base.BaseActivity
import com.agnext.qualixfarmer.base.SessionClass
import com.agnext.qualixfarmer.commonUi.login.LoginActivity
import com.agnext.qualixfarmer.network.ApiClient
import com.agnext.qualixfarmer.network.ApiInterface
import com.agnext.qualixfarmer.utils.IntentUtil
import kotlinx.android.synthetic.main.activity_add_division.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddDivisionActivity : BaseActivity(), View.OnClickListener,
    AdapterView.OnItemSelectedListener {
    var token: String? = null
    var gardenId: String? = null

    val apiService = ApiClient.getClient().create(ApiInterface::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_division)
        initView()
    }

    /**InitView*/
    fun initView() {
        //setting toolbar
        toolbar.title = " Add Division"
        setSupportActionBar(toolbar)
        //enabling back button
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        //  SetOnClickListener
        addDivision.setOnClickListener(this)

        spGarden.onItemSelectedListener = this
//        spDivision.onItemSelectedListener = this

        //VM
//        viewModel = ViewModelProvider(
//            this,
//            ScanAreaViewModelFactory(ScanAreaInteractor())
//        )[ScanAreaViewModel::class.java]
//        viewModel.scanAreaState.observe(::getLifecycle, ::updateUI)

        token = userToken(this)
        // Api hit to get Data
//        viewModel.getGardenData(token, SessionClass(this).getUserId())

        getGarden()
    }

    fun getGarden() {
        val call = apiService.getGarden(token)
        call.enqueue(object : retrofit2.Callback<ResGarden> {
            override fun onFailure(call: Call<ResGarden>, t: Throwable) {
                showToast(this@AddDivisionActivity, "Error to get garden")
            }

            override fun onResponse(call: Call<ResGarden>, response: Response<ResGarden>) {
                when {
                    response.isSuccessful -> {
                        var gardenArray = ArrayList<String>()
                        if(response.body()!!.data!=null)
                        {
                        for (i in 0 until response.body()!!.data!!.size) {
                            gardenArray.add(response.body()!!.data!![i].gardenId.toString())
                        }
                        val adapter = ArrayAdapter(
                            this@AddDivisionActivity,
                            android.R.layout.simple_spinner_item,
                            gardenArray
                        )

                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spGarden.adapter = adapter
                        }
                    }
                    response.code() == 401 -> {
                        showToast(this@AddDivisionActivity, "Token expire")
                        IntentUtil.moveNextScreenWithNoStack(
                            this@AddDivisionActivity,
                            LoginActivity::class.java
                        )
                    }
                    else -> {
                        showToast(this@AddDivisionActivity, "Error to get data")
                    }
                }
            }
        })
    }

    /**System Override methods*/
    //1 OnBack Press
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onClick(v: View?) {
        when (v) {
            addDivision -> {
                if (etDivisionName.text.isNullOrEmpty() || gardenId.isNullOrEmpty()) {
                    showToast(this, "Add all parameter")
                } else {
                    addDivision()
                }
            }
        }
    }


    fun addDivision() {
        var gardenId:String= (spGarden.selectedItem.toString())
        var dataRequest=HashMap<String,Any>()
        dataRequest["divisionName"]=etDivisionName.text.toString()
        dataRequest["gardenId"]=gardenId
        val call = apiService.addDivision(token!!,dataRequest)
        call.enqueue(object:Callback<ResAddDivision>{
            override fun onFailure(call: Call<ResAddDivision>, t: Throwable) {
                showToast(this@AddDivisionActivity,"Error to add division")
            }

            override fun onResponse(
                call: Call<ResAddDivision>,
                response: Response<ResAddDivision>
            ) {
                when
                {
                    response.isSuccessful->{
                        onBackPressed()
                        showToast(this@AddDivisionActivity, "Successfully division is added")
                    }
                    response.code() == 401 -> {
                        showToast(this@AddDivisionActivity, "Token expire")
                        IntentUtil.moveNextScreenWithNoStack(
                            this@AddDivisionActivity,
                            LoginActivity::class.java
                        )
                    }
                    else -> {
                        showToast(this@AddDivisionActivity, "Error to add division")
                    }
                }
            }
        })

//        showToast(this, "Division added successfully")


    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent) {
            spGarden -> {
                gardenId=spGarden.selectedItem.toString()
            }
        }
    }
}
