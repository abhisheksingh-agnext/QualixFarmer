package com.agnext.qualixfarmer.fields.addField

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.agnext.qualixfarmer.R
import com.agnext.qualixfarmer.base.BaseActivity
import com.agnext.qualixfarmer.base.Constant
import com.agnext.qualixfarmer.base.Constant.Companion.REQUEST_BOUNDARY_GEO_FENCING
import com.agnext.qualixfarmer.base.Constant.Companion.REQUEST_BOUNDARY_GEO_PLOTING
import com.agnext.qualixfarmer.base.Constant.Companion.REQUEST_SEASON
import com.agnext.qualixfarmer.base.SessionClass
import com.agnext.qualixfarmer.fields.FieldPlotting
import com.agnext.qualixfarmer.fields.addFarmSeason.SeasonData
import com.agnext.qualixfarmer.network.Response.ResCrops
import com.agnext.qualixfarmer.utils.AlertUtil
import com.agnext.qualixfarmer.utils.utils
import com.agnext.sensenextmyadmin.utils.extensions.ScreenState
import com.gisnext.lib.util.Constants
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_add_farm.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.layout_progress.*


class AddFarmActivity : BaseActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener,
    OnMapReadyCallback {
    private var mMap: GoogleMap? = null
    var field_latitude = 0.0
    var field_longitude = 0.0
    var rectOptions = PolygonOptions()
    var polyline: Polygon? = null

    var seasonArray: ArrayList<SeasonData> = ArrayList()
    var seasonSpinnerArray: ArrayList<String> = ArrayList()

     var  points= ArrayList<LatLng>()
    private lateinit var viewModel: AddFarmViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_farm)
        initView()
    }

    fun initView() {
        //setting toolbar
        toolbar.title = "Add Farm"
        setSupportActionBar(toolbar)
        //enabling back button
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        //Click register
        btnAddFarm.setOnClickListener(this)
        btAddBoundary.setOnClickListener(this)

        spinnerCrop.onItemSelectedListener = this
        spinnerCropVariety.onItemSelectedListener=this

        getDataReady()

        //get map ready
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFieldDetail) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //getting VM instance
        viewModel = ViewModelProvider(
            this,
            AddFarmViewModelFactory(AddFarmInteractor(this))
        )[AddFarmViewModel::class.java]
        viewModel.addFarmState.observe(::getLifecycle, ::updateUI)

        //Call to get all crop api
        viewModel.getCrop(qualixToken())
    }

    /**UI  State Observer*/
    private fun updateUI(screenState: ScreenState<AddFarmState>?) {
        when (screenState) {
            ScreenState.Loading -> progress.visibility = View.VISIBLE
            is ScreenState.Render -> processLoginState(screenState.renderState)
        }
    }

    private fun processLoginState(renderState: AddFarmState) {
        progress.visibility = View.GONE
        when (renderState) {

            //Get Corp Success
            AddFarmState.GetCorpSuccess -> {
                var cropSpinnerArray = ArrayList<String>()
                if (viewModel.getCropList.value != null) {
                    for (i in 0 until viewModel.getCropList.value!!.size) {
                        cropSpinnerArray.add(viewModel.getCropList.value!![i].commodity_name.toString())
                    }
                    val adapter = ArrayAdapter<String>(
                        applicationContext,
                        R.layout.spinner_layout,
                        cropSpinnerArray
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerCrop.adapter = adapter

                }

            }
            AddFarmState.GetCropFailure -> {
                AlertUtil.showToast(this, getString(R.string.not_able_to_get_crops))
            }

            //Get Crop Variety
            AddFarmState.GetCropVarietyFailure -> {
                AlertUtil.showToast(this, getString(R.string.not_able_to_get_crops_variety))
            }
            AddFarmState.GetCropVarietySuccess -> {
                var cropVarietySpinnerArray = ArrayList<String>()
                if (viewModel.getCropVarietyList.value != null) {
                    for (i in 0 until viewModel.getCropVarietyList.value!!.size) {
                        cropVarietySpinnerArray.add(viewModel.getCropVarietyList.value!![i].commodity__name.toString())
                    }
                    val adapter = ArrayAdapter<String>(
                        applicationContext,
                        R.layout.spinner_layout,
                        cropVarietySpinnerArray
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerCropVariety.adapter = adapter

                }
            }

            //Add farm
            AddFarmState.AddFarmSuccess->{
                AlertUtil.showToast(this,"Farm Successfully added")
                setResult(AppCompatActivity.RESULT_OK)
                finish()
            }
            AddFarmState.AddFarmFailure->{AlertUtil.showToast(this,"Error to add farm")}

            //Expire Token
            AddFarmState.ExpireToken -> {
                logout(this)
            }
        }
    }


    private fun getDataReady() {
        seasonArray.add(SeasonData("kharif", "1", "Rice", "var 1", "01/05/2020", "01/10/2020"))
        seasonArray.add(SeasonData("Rabi", "2", "Wheat", "var 2", "01/10/2020", "01/05/2020"))
        for (i in 0 until seasonArray.size) {
            seasonSpinnerArray.add(seasonArray[i].season)
        }
    }


    /**Logical Method*/
    //Save data and Callback
    private fun addFarm() {
        if (tvFarmCode.text!!.isNotEmpty() && tvFarmName.text!!.isNotEmpty() && tvDistrict.text!!.isNotEmpty() && tvArea.text!!.isNotEmpty() && tvAddress.text!!.isNotEmpty()) {
            var data = HashMap<String, Any>()
            data["plot_name"] = tvFarmName.text.toString()
            data["district"] = tvDistrict.text.toString()
            data["area"] = tvArea.text.toString()
            data["address"] = tvAddress.text.toString()
            data["farmer_id"]=SessionClass(this).getVMSId()

            if (spinnerCrop != null && spinnerCrop.selectedItem != null && viewModel.getCropList.value!=null)  {
                data["crop_id"] = viewModel.getCropList.value!![spinnerCrop.selectedItemPosition].commodity_id.toString()
            }

            if (spinnerCropVariety != null && spinnerCropVariety.selectedItem != null) {
             //   data["cropVerityId"] = viewModel.getCropVarietyList.value!!.cropVarietyData!![spinnerCropVariety.selectedItemPosition].cropVerityId.toString()
            }
            if(points!=null)
            {
                data["coordinates"]=points
            }

            spinnerCrop.selectedItem
            viewModel.addFarm(qualixToken(), data)

        } else {
            AlertUtil.showToast(this, getString(R.string.fill_all_fields))
        }
    }

    // Draw field in map
    private fun addFieldOnMap(points: ArrayList<LatLng>) {

        btAddBoundary.visibility = View.GONE
        lnMap.visibility = View.VISIBLE

        for (i in 0 until points.size) {

            field_latitude = points[i].latitude
            field_longitude = points[i].longitude
            rectOptions.add(LatLng(field_latitude, field_longitude))
        }

        if (rectOptions != null) {
            polyline = mMap!!.addPolygon(rectOptions)
        }

        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(field_latitude, field_longitude))
            .zoom(15f)
            .build()
        mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

    }

    /**Override method*/
    //Onclick
    override fun onClick(view: View?) {
        when (view) {

            btnAddFarm -> {
                addFarm()
            }

            btAddBoundary -> {
                val colorPrimary = utils.fetchPrimaryColor(this)
                FieldPlotting().showBoundaryOptionDialog(this, colorPrimary)
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, p3: Long) {
        when (parent) {
            spinnerCrop -> {
                val crops = viewModel.getCropList.value!! as ArrayList<ResCrops>
                viewModel.getCropVariety(qualixToken(), crops[pos].commodity_id.toString())
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_SEASON -> {
                if (data != null) {
                    val selectObject = data.getStringExtra("selectObject")
                    val gson = Gson()
                    if (selectObject != null) {
                        val type = object : TypeToken<SeasonData>() {}.type
                        var data: SeasonData = gson.fromJson(selectObject, type)
                        seasonSpinnerArray.clear()
                        seasonArray.clear()
                        seasonArray.add(data)
                        getDataReady()
                    }
                }
            }
            REQUEST_BOUNDARY_GEO_PLOTING -> {
                if (data != null) {
                 points =
                    data!!.getParcelableArrayListExtra(Constants.KEY_POINTS)
                Log.e("point", "$points")
                addFieldOnMap(points)}
            }
            REQUEST_BOUNDARY_GEO_FENCING -> {
                if(data!=null){
               points =
                    data!!.getParcelableArrayListExtra(Constants.KEY_POINTS)
                addFieldOnMap(points)

            }}
        }
    }

    //On Map Ready
    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap

        mMap!!.mapType = GoogleMap.MAP_TYPE_SATELLITE
        mMap!!.uiSettings.isZoomControlsEnabled = true

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
