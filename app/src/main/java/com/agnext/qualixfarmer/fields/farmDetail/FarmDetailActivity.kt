package com.agnext.qualixfarmer.fields.farmDetail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.agnext.qualixfarmer.R
import com.agnext.qualixfarmer.base.BaseActivity
import com.agnext.qualixfarmer.base.Constant.Companion.REQUEST_BOUNDARY_GEO_FENCING
import com.agnext.qualixfarmer.base.Constant.Companion.REQUEST_BOUNDARY_GEO_PLOTING
import com.agnext.qualixfarmer.fields.FieldPlotting
import com.agnext.qualixfarmer.fields.farmList.FieldData
import com.agnext.qualixfarmer.network.Response.ResAllFarms
import com.agnext.qualixfarmer.utils.AlertUtil
import com.agnext.qualixfarmer.utils.utils
import com.agnext.sensenextmyadmin.utils.extensions.ScreenState
import com.gisnext.lib.util.Constants.KEY_POINTS
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
import kotlinx.android.synthetic.main.activity_farm_detail.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.layout_progress.*

class FarmDetailActivity : BaseActivity(), View.OnClickListener, OnMapReadyCallback {

    var rectOptions = PolygonOptions()
    private var mMap: GoogleMap? = null
    var polyline: Polygon? = null
    var field_latitude = 0.0
    var field_longitude = 0.0

    private lateinit var viewModel: FarmDetailViewModel
    private lateinit var testObject: ResAllFarms
    var latlngList = ArrayList<LatLng>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_farm_detail)

        viewModel = ViewModelProvider(
            this,
            FarmDetailViewModelFactory(FarmDetailInteractor(this))
        )[FarmDetailViewModel::class.java]
        viewModel!!.farmState.observe(::getLifecycle, ::updateUI)

        initView()
    }

    /**Initialize View*/
    fun initView() {
        //setting toolbar
        toolbar.title = "Farm Detail"
        setSupportActionBar(toolbar)
        //enabling back button
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)


        val selectObject = intent.getStringExtra("jsonObject")
        val gson = Gson()
        if (selectObject != null) {
            val type = object : TypeToken<ResAllFarms>() {}.type
            testObject = gson.fromJson(selectObject, type)
            // setData(testObject)
        }

        //get farm Detail
        viewModel.getFarmDetail()

        // click register
        btAddBoundries.setOnClickListener(this)

        //Map register
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFieldDetail) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**Set Data in View*/
    private fun setData(testObject: ResAllFarms) {
        //1
        tvFarmName.text = testObject.farmId
        tvFarmerCode.text = "Field Id : ${testObject.farmerId}"
        tvFarmerName.text = testObject.farmerName
        //2
        tvCrop.text = testObject.cropName
        tvCropVariety.text = testObject.cropVerityName

        //3
        tvAddress.text = testObject.address
        tvDistrict.text = testObject.district
        tvArea.text = testObject.area

        if(testObject.farmIndics!=null)
            if(testObject.farmIndics!!.size>0)
        {
            btAddBoundries.visibility=View.GONE
            cdFarmMap.visibility=View.VISIBLE
            if(testObject.farmIndics!![0].latitude!=null)
            {
            for(i in 0 until testObject.farmIndics!!.size)
            {
                latlngList.add(LatLng(testObject.farmIndics!![i].latitude!!.toDouble(), testObject.farmIndics!![i].longitude!!.toDouble()))
            }
//            drawField()
            }

        }
        else
        {
            btAddBoundries.visibility=View.GONE
            cdFarmMap.visibility=View.GONE
        }


    }


    /**UI  State Observer*/
    private fun updateUI(screenState: ScreenState<FarmDetailState>?) {
        when (screenState) {
            ScreenState.Loading -> progress.visibility = View.VISIBLE
            is ScreenState.Render -> processLoginState(screenState.renderState)
        }
    }

    private fun processLoginState(renderState: FarmDetailState) {
        progress.visibility = View.GONE
        when (renderState) {
            FarmDetailState.farmDetailFailure -> {
                AlertUtil.showToast(this, "error to get farm detail")
            }
            FarmDetailState.farmDetailSuccess -> {
                setData(testObject)
            }

        }
    }

    /**Override methods*/

    //On Click
    override fun onClick(view: View?) {
        when (view) {
            btAddBoundries -> {
                val colorPrimary = utils.fetchPrimaryColor(this)
                FieldPlotting().showBoundaryOptionDialog(this, colorPrimary)
            }
        }
    }

    //On Map Ready
    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap

        mMap!!.mapType = GoogleMap.MAP_TYPE_SATELLITE
        mMap!!.uiSettings.isZoomControlsEnabled = true

        if(latlngList.size>0)
        drawField()
    }

    private fun drawField()
    {
        for (i in 0 until latlngList.size) {

            field_latitude = latlngList[i].latitude!!.toDouble()
            field_longitude = latlngList[i].longitude!!.toDouble()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null)
            when (requestCode) {
                REQUEST_BOUNDARY_GEO_PLOTING -> {
                    val points: ArrayList<LatLng> = data.getParcelableArrayListExtra(KEY_POINTS)
                    Log.e("points", "${points.toString()}")
                }
                REQUEST_BOUNDARY_GEO_FENCING -> {
                    val points: ArrayList<LatLng> = data.getParcelableArrayListExtra(KEY_POINTS)
                    Log.e("points", "${points.toString()}")

                }
            }
    }

    //Back
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}


