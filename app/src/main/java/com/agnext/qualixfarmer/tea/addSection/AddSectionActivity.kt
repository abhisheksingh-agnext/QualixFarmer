package com.agnext.qualixfarmer.tea.addSection

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.agnext.qualixfarmer.R
import com.agnext.qualixfarmer.base.BaseActivity
import com.agnext.qualixfarmer.base.Constant.Companion.REQUEST_BOUNDARY_GEO_FENCING
import com.agnext.qualixfarmer.base.Constant.Companion.REQUEST_BOUNDARY_GEO_PLOTING
import com.agnext.qualixfarmer.base.SessionClass
import com.agnext.qualixfarmer.commonUi.login.LoginActivity
import com.agnext.qualixfarmer.fields.FieldPlotting
import com.agnext.qualixfarmer.network.ApiClient
import com.agnext.qualixfarmer.network.ApiInterface
import com.agnext.qualixfarmer.tea.addDivision.ResAddDivision
import com.agnext.qualixfarmer.tea.addDivision.ResGarden
import com.agnext.qualixfarmer.utils.IntentUtil
import com.agnext.qualixfarmer.utils.utils
import com.gisnext.lib.util.Constants
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import kotlinx.android.synthetic.main.activity_add_section.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddSectionActivity : BaseActivity(), AdapterView.OnItemSelectedListener, OnMapReadyCallback,
    View.OnClickListener {
    var token: String? = null
    val apiService = ApiClient.getClient().create(ApiInterface::class.java)
    private var mMap: GoogleMap? = null
    var points = ArrayList<LatLng>()
    var field_latitude = 0.0
    var field_longitude = 0.0
    var field_latitude_cam = 0.0
    var field_longitude_cam = 0.0
    var rectOptions = PolygonOptions()
    var polyline: Polygon? = null
    var divIdArray = ArrayList<String>()

    var gardenIdArray = ArrayList<String>()
    var gardenNameArray = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_section)
        initView()
    }

    fun initView() {
        //setting toolbar
        toolbar.title = " Add Section"
        setSupportActionBar(toolbar)
        //enabling back button
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        // SetOnClickListener
        addSectionBoundary.setOnClickListener(this)
        addSection.setOnClickListener(this)
        ivAddDivision.setOnClickListener(this)

        spGarden.onItemSelectedListener = this
        spDivision.onItemSelectedListener = this

        //get map ready
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFieldDetail) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //VM
//        viewModel = ViewModelProvider(
//            this,
//            ScanAreaViewModelFactory(ScanAreaInteractor())
//        )[ScanAreaViewModel::class.java]
//        viewModel.scanAreaState.observe(::getLifecycle, ::updateUI)

        token = "Bearer " + SessionClass(this).getUserToken()
        // Api hit to get Data
//        viewModel.getGardenData(token, SessionClass(this).getUserId())

        getGarden()
    }


    /**API METHODS*/
    //1 Get Garden
    fun getGarden() {
        val call = apiService.getGarden(token)
        call.enqueue(object : retrofit2.Callback<ResGarden> {
            override fun onFailure(call: Call<ResGarden>, t: Throwable) {
                //       showToast(this@AddSectionActivity, "Error to get garden")
            }

            override fun onResponse(call: Call<ResGarden>, response: Response<ResGarden>) {
                when {
                    response.isSuccessful -> {

                        for (i in 0 until response.body()!!.data!!.size) {
                            gardenNameArray.add(response.body()!!.data!![i].gardenName.toString())
                            gardenIdArray.add(response.body()!!.data!![i].gardenId.toString())
                        }
                        val adapter = ArrayAdapter(
                            this@AddSectionActivity,
                            android.R.layout.simple_spinner_item,
                            gardenNameArray
                        )

                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spGarden.adapter = adapter
                    }
                    response.code() == 401 -> {
                        showToast(this@AddSectionActivity, "Token expire")
                        IntentUtil.moveNextScreenWithNoStack(
                            this@AddSectionActivity,
                            LoginActivity::class.java
                        )
                    }
                    else -> {
                        //   showToast(this@AddSectionActivity, "Error to get data")
                    }
                }
            }
        })
    }

    //Get Division
    fun getDivision(gardenId: String) {
        val call = apiService.getDivision(token, gardenId)
        call.enqueue(object : Callback<ResDivision> {
            override fun onFailure(call: Call<ResDivision>, t: Throwable) {
                //  showToast(this@AddSectionActivity, "Error to get data")
            }

            override fun onResponse(call: Call<ResDivision>, response: Response<ResDivision>) {
                when {
                    response.isSuccessful -> {
                        var gardenArray = ArrayList<String>()
                        if (!response.body()!!.data.isNullOrEmpty()) {
                            for (i in 0 until response.body()!!.data!!.size) {
                                gardenArray.add(response.body()!!.data!![i].divisionName.toString())
                                divIdArray.add(response.body()!!.data!![i].divisionId.toString())
                            }

                        } else {
                            gardenArray.add("No Division")
                            divIdArray.clear()
                        }
                        val adapter = ArrayAdapter(
                            this@AddSectionActivity,
                            android.R.layout.simple_spinner_item,
                            gardenArray
                        )

                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spDivision.adapter = adapter
                    }
                    response.code() == 401 -> {
                        showToast(this@AddSectionActivity, "Token expire")
                        IntentUtil.moveNextScreenWithNoStack(
                            this@AddSectionActivity,
                            LoginActivity::class.java
                        )
                    }
                    else -> {
                        //    showToast(this@AddSectionActivity, "Error to get data")
                    }
                }
            }
        })
    }

    //Add Division
    fun addDivision(divisionName: String, gardenID: String) {
        var gardenId: String = (spGarden.selectedItem.toString())
        var dataRequest = HashMap<String, Any>()
        dataRequest["divisionName"] = divisionName
        dataRequest["gardenId"] = gardenID
        val call = apiService.addDivision(token!!, dataRequest)
        call.enqueue(object : Callback<ResAddDivision> {
            override fun onFailure(call: Call<ResAddDivision>, t: Throwable) {
                showToast(this@AddSectionActivity, "Error to add division")
            }

            override fun onResponse(
                call: Call<ResAddDivision>,
                response: Response<ResAddDivision>
            ) {
                when {
                    response.isSuccessful -> {
                        onBackPressed()
                        showToast(this@AddSectionActivity, "Successfully division is added")
                    }
                    response.code() == 401 -> {
                        showToast(this@AddSectionActivity, "Token expire")
                        IntentUtil.moveNextScreenWithNoStack(
                            this@AddSectionActivity,
                            LoginActivity::class.java
                        )
                    }
                    else -> {
                        showToast(this@AddSectionActivity, "Error to add division")
                    }
                }
            }
        })


    }


    //Add Section
    fun addSection() {
        var dataRequest = HashMap<String, Any>()
        dataRequest["divisionId"] = divIdArray[spDivision.selectedItemPosition]
        var pointArray = ArrayList<SectionCoordinates>()

        if (points != null) {
            for (i in 0 until points.size) {
                var objecct = SectionCoordinates(
                    points[i].latitude.toString(),
                    points[i].longitude.toString()
                )
                pointArray.add(objecct)
            }

        }
        var sectionData =
            SectionData(etSectionName.text.toString(), etArea.text.toString(), pointArray)
        var sectionArray = ArrayList<SectionData>()
        sectionArray.add(sectionData)
        dataRequest["sectionVO"] = sectionArray
        val call = apiService.addSection(token!!, dataRequest)
        call.enqueue(object : Callback<ResAddSection> {
            override fun onFailure(call: Call<ResAddSection>, t: Throwable) {
                // showToast(this@AddSectionActivity, "Error to add section")
            }

            override fun onResponse(call: Call<ResAddSection>, response: Response<ResAddSection>) {
                when {
                    response.isSuccessful -> {
                        if (response.body()!!.resAddSectionData!!.size > 0) {
                            onBackPressed()
                            showToast(this@AddSectionActivity, "Section added successfully")
                        }
                    }
                    response.code() == 401 -> {
                        showToast(this@AddSectionActivity, "Token expire")
                        IntentUtil.moveNextScreenWithNoStack(
                            this@AddSectionActivity,
                            LoginActivity::class.java
                        )
                    }
                    response.code() == 400 -> {
                        showToast(this@AddSectionActivity, "Section name already exist")
                    }
                    else -> {
                        //showToast(this@AddSectionActivity, "Error to get data")
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

    //2 Spinner Listener
    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent) {
            spGarden -> {
                if (gardenIdArray.size > 0)
                    getDivision(gardenIdArray[position].toString())
            }
            spDivision -> {

            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap

        mMap!!.mapType = GoogleMap.MAP_TYPE_SATELLITE
        mMap!!.uiSettings.isZoomControlsEnabled = true
    }

    override fun onClick(v: View?) {
        when (v) {
            ivAddDivision -> {
                showDialogAddDivision()
            }
            addSectionBoundary -> {
                val colorPrimary = utils.fetchPrimaryColor(this)
                FieldPlotting().showBoundaryOptionDialog(this, colorPrimary)
            }
            addSection -> {
                if (etSectionName.text.isNotEmpty() || etArea.text.isNotEmpty())
                    if (spGarden.selectedItem != "No Garden" && spDivision.selectedItem != "No Section") {
                        addSection()
                    } else
                        showToast(this, "Add all parameters")
                else
                    showToast(this, "Add all parameters")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {

            REQUEST_BOUNDARY_GEO_PLOTING -> {
                points =
                    data!!.getParcelableArrayListExtra(Constants.KEY_POINTS)
                Log.e("point", "$points")
                addFieldOnMap(points)
            }
            REQUEST_BOUNDARY_GEO_FENCING -> {
                points =
                    data!!.getParcelableArrayListExtra(Constants.KEY_POINTS)
                addFieldOnMap(points)

            }
        }
    }

    // Draw field in map
    private fun addFieldOnMap(points: ArrayList<LatLng>) {

        addSectionBoundary.visibility = View.GONE
        lnMap.visibility = View.VISIBLE

        for (i in 0 until points.size) {

            field_latitude = points[i].latitude
            field_longitude = points[i].longitude
            rectOptions.add(LatLng(field_latitude, field_longitude))
            if (i == 1) {
                field_latitude_cam = points[i].latitude
                field_longitude_cam = points[i].longitude
            }
        }

        if (rectOptions != null) {
            polyline = mMap!!.addPolygon(rectOptions)
        }

        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(field_latitude_cam, field_longitude_cam))
            .zoom(18f)
            .build()
        mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

    }

    fun showDialogAddDivision() {
        var inflater: LayoutInflater = this@AddSectionActivity.layoutInflater
        var dialogView: View = inflater.inflate(R.layout.dialog_flc, null)

        var spGardenDialog = dialogView.findViewById<Spinner>(R.id.spGardenDialog)
        var etDivisionName = dialogView.findViewById<EditText>(R.id.etDivisionName)
        var addDivDialog = dialogView.findViewById<Button>(R.id.addDivision)
        var cancel = dialogView.findViewById<Button>(R.id.cancel)


        val dialog = AlertDialog.Builder(this)
            .setTitle("Add Division")
            .setView(dialogView)
            .setCancelable(false)
            .show()

        // UI functionality
        val adapter = ArrayAdapter(
            this@AddSectionActivity,
            android.R.layout.simple_spinner_item,
            gardenNameArray
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spGardenDialog.adapter = adapter

        // Click Listener
        cancel.setOnClickListener {
            dialog.hide()
        }
        addDivDialog.setOnClickListener {
            if (etDivisionName.text.toString() != null)

                addDivision(
                    gardenIdArray[spGardenDialog.selectedItemPosition],
                    etDivisionName.text.toString()
                )
            else
                showToast(this, "Add all parameter")
        }
    }

}

data class SectionData(
    val name: String,
    val totalArea: String,
    val sectionIndicsVO: ArrayList<SectionCoordinates>
)

data class SectionCoordinates(val latitude: String, val longitude: String)
