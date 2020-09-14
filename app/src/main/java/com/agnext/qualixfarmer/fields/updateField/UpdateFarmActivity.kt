package com.agnext.qualixfarmer.fields.updateField

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.agnext.qualixfarmer.R
import com.agnext.qualixfarmer.base.BaseActivity
import com.agnext.qualixfarmer.base.Constant
import com.agnext.qualixfarmer.base.SessionClass
import com.agnext.qualixfarmer.fields.FieldPlotting
import com.agnext.qualixfarmer.network.Response.Coordinate
import com.agnext.qualixfarmer.network.Response.FarmRes
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
import kotlinx.android.synthetic.main.activity_update_field.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.layout_progress.*

class UpdateFarmActivity : BaseActivity(), OnMapReadyCallback, View.OnClickListener {
    private lateinit var viewModel: UpdateFarmViewModel
    var rectOptions = PolygonOptions()
    private var mMap: GoogleMap? = null
    var polyline: Polygon? = null
    var polylineNew: Polygon? = null
    var rectOptionsNew = PolygonOptions()


    var field_latitude = 0.0
    var field_longitude = 0.0

    //    var latlngList = ArrayList<LatLng>()
    private lateinit var testObject: FarmRes
    var points = ArrayList<LatLng>()
    var cropSpinnerArray = ArrayList<String>()
    var cropIdSpinnerArray = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_field)
        toolbar.title = "Update Farm"
        setSupportActionBar(toolbar)
        //enabling back button
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        btnUpdateFarm.setOnClickListener(this)
        btAddBoundary.setOnClickListener(this)
        //Map register
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapUpdateFieldDetail) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel = ViewModelProvider(
            this,
            UpdateFarmViewModelFactory(UpdateFramInteractor(this))
        )[UpdateFarmViewModel::class.java]
        viewModel.updateFarmState.observe(::getLifecycle, ::updateUI)
        val selectObject = intent.getStringExtra("jsonObject")
        val gson = Gson()
        if (selectObject != null) {
            val type = object : TypeToken<FarmRes>() {}.type
            testObject = gson.fromJson(selectObject, type)
            setData(testObject)
        }

        //Call to get all crop api
        viewModel.getCrop(qualixToken())
        //viewModel.getParticularFarmVM(userToken(this), farmId)
    }

    private fun setData(testObject: FarmRes) {
        tvFarmCode.text = testObject.plot_id
        tvFarmName.setText(testObject.plot_name)
        tvDistrict.setText(testObject.district)
        tvArea.setText(testObject.area)
        tvAddress.setText(testObject.address)
        if (testObject.crop_id != null)
            spinnerCrop.setSelection(cropIdSpinnerArray.indexOf(testObject.crop_id!!))
        var farmIndics = ArrayList<Coordinate>()
        if (testObject.coordinates != null && testObject.coordinates!!.size > 0) {
            cdFarmMap.visibility = View.VISIBLE
            for (i in 0 until testObject.coordinates!!.size) {
                points.add(
                    LatLng(
                        testObject.coordinates!![i].latitude!!.toDouble(),
                        testObject.coordinates!![i].longitude!!.toDouble()
                    )
                )
            }
        } else {
            cdFarmMap.visibility = View.GONE
        }
    }

    /**UI  State Observer*/
    private fun updateUI(screenState: ScreenState<UpdateFarmState>?) {
        when (screenState) {
            ScreenState.Loading -> progress.visibility = View.VISIBLE
            is ScreenState.Render -> processLoginState(screenState.renderState)
        }
    }

    private fun processLoginState(renderState: UpdateFarmState) {
        progress.visibility = View.GONE
        when (renderState) {
            UpdateFarmState.GetParticularFarmFailure -> {
                AlertUtil.showToast(this, "Error to get the farm detail")
            }
            UpdateFarmState.GetParticularFarmSuccess -> {

            }
            UpdateFarmState.GetCorpSuccess -> {
                if (viewModel.getCropList.value != null) {
                    for (i in 0 until viewModel.getCropList.value!!.size) {
                        cropSpinnerArray.add(viewModel.getCropList.value!![i].commodity_name.toString())
                        cropIdSpinnerArray.add(viewModel.getCropList.value!![i].commodity_id.toString())

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
            //Get Crop Variety
            UpdateFarmState.GetCropVarietyFailure -> {
                AlertUtil.showToast(this, getString(R.string.not_able_to_get_crops_variety))
            }
            UpdateFarmState.GetCropVarietySuccess -> {
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
            UpdateFarmState.GetCropFailure -> {
                AlertUtil.showToast(this, getString(R.string.not_able_to_get_crops))
            }

            UpdateFarmState.UpdateFarmSuccess -> {
                AlertUtil.showToast(this, "Farm Successfully updated")
                setResult(AppCompatActivity.RESULT_OK)
                finish()
            }
            UpdateFarmState.UpdateFarmFailure -> {
                AlertUtil.showToast(this, "Error to update farm")
            }

        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap

        mMap!!.mapType = GoogleMap.MAP_TYPE_SATELLITE
        mMap!!.uiSettings.isZoomControlsEnabled = true

        if (points.size > 0)
            drawField()
    }

    private fun drawField() {

        rectOptions.addAll(points)
        if (rectOptions != null) {
            polyline = mMap!!.addPolygon(rectOptions)
        }

        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(points[0].latitude, points[0].longitude))
            .zoom(15f)
            .build()
        mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun updateFarm() {
        if (tvFarmCode.text!!.isNotEmpty() && tvFarmName.text!!.isNotEmpty() && tvDistrict.text!!.isNotEmpty() && tvArea.text!!.isNotEmpty() && tvAddress.text!!.isNotEmpty()) {
            var data = HashMap<String, Any>()
            data["plot_name"] = tvFarmName.text.toString()
            data["district"] = tvDistrict.text.toString()
            data["area"] = tvArea.text.toString()
            data["address"] = tvAddress.text.toString()
            data["farmer_id"] = SessionClass(this).getVMSId()

            if (spinnerCrop != null && spinnerCrop.selectedItem != null && viewModel.getCropList.value != null) {
                data["crop_id"] =
                    viewModel.getCropList.value!![spinnerCrop.selectedItemPosition].commodity_id.toString()
            }

            if (spinnerCropVariety != null && spinnerCropVariety.selectedItem != null) {
                //   data["cropVerityId"] = viewModel.getCropVarietyList.value!!.cropVarietyData!![spinnerCropVariety.selectedItemPosition].cropVerityId.toString()
            }
            if (points != null) {
                data["coordinates"] = points
            }

            spinnerCrop.selectedItem
            viewModel.updateFarm(
                SessionClass(this).getVMSToken(),
                testObject.plot_id.toString(),
                data
            )

        } else {
            AlertUtil.showToast(this, getString(R.string.fill_all_fields))
        }
    }

    //Back
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onClick(v: View?) {
        when (v) {
            btnUpdateFarm -> {
                updateFarm()
            }
            btAddBoundary -> {
                val colorPrimary = utils.fetchPrimaryColor(this)
                FieldPlotting().showBoundaryOptionDialog(this, colorPrimary)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {

            Constant.REQUEST_BOUNDARY_GEO_PLOTING -> {
                if (data != null) {
                    points.clear()
                    points =
                        data.getParcelableArrayListExtra(Constants.KEY_POINTS)
                    Log.e("point", "$points")
                    addFieldOnMap(points)
                }
            }
            Constant.REQUEST_BOUNDARY_GEO_FENCING -> {
                if (data != null) {
                    points.clear()
                    points =
                        data.getParcelableArrayListExtra(Constants.KEY_POINTS)
                    addFieldOnMap(points)
                }
            }
        }
    }

    private fun addFieldOnMap(returnPoints: ArrayList<LatLng>) {

        btAddBoundary.visibility = View.GONE
        lnMap.visibility = View.VISIBLE

        polyline!!.remove()
        mMap!!.clear()

        rectOptionsNew.addAll(points)
        if (rectOptionsNew != null) {
            polylineNew = mMap!!.addPolygon(rectOptionsNew)
        }

        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(points[0].latitude, points[0].longitude))
            .zoom(15f)
            .build()
        mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

    }
}
