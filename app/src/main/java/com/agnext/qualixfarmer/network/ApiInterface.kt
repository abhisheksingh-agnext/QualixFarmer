package com.agnext.qualixfarmer.network

import com.agnext.qualixfarmer.network.Response.*
import com.agnext.qualixfarmer.tea.addDivision.ResAddDivision
import com.agnext.qualixfarmer.tea.addDivision.ResGarden
import com.agnext.qualixfarmer.tea.addSection.ResAddSection
import com.agnext.qualixfarmer.tea.addSection.ResDivision
import com.agnext.qualixfarmer.tea.scanDoneDetail.ResScanDetail
import com.agnext.qualixfarmer.tea.scanDoneDetail.ResponseGetUserDetail
import com.agnext.sensenextmyadmin.ui.auth.login.LoginResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @POST("/api/auth/login")
    fun login(@Body options: HashMap<String, String>): Call<LoginResponse>


    /**=================================TEA*/
    @GET("/api/companies/user/scans")
    fun getScans(
        @Header("authorization") authorization: String,
        @Query("p") p: String, @Query("l") l: String, @Query("dateFrom") dateFrom: String, @Query("dateTo") dateTo: String
    ): Call<ResUserScans>

    @GET("/api/scanList")
    fun getScanList(): Call<ResScanList>

    @GET("/api/user/scans/avg")
    fun getAvgScanData(@Header("authorization") authorization: String): Call<ResAvgScanData>

    //Division and Section for AM
      @GET("api/users/id/{id}")
    fun getUserDetail(
        @Header("authorization") authorization: String?, @Path("id") userId: String?
    ): Call<ResponseGetUserDetail?>?

    //Save Scan Data
    @POST("api/farmers/collected")
    fun saveScanData( @Header("authorization") authorization: String?,@Body data: HashMap<String, Any>):Call<ResScanDetail>

    //Get Garden
    @GET("api/gardens?p=0&l=10")
    fun  getGarden(@Header("authorization") authorization: String?):Call<ResGarden>

    //Get Division
    @GET("api/divisions/garden/{gardenId}?p=0&l=10")
    fun getDivision(@Header("authorization") authorization: String?,@Path("gardenId")gardenId:String):Call<ResDivision>

    //Add Division
    @POST("api/divisions")
    fun addDivision(@Header("authorization") authorization: String, @Body data: HashMap<String, Any>):Call<ResAddDivision>

    //Add Section
    @POST("/api/sections")
    fun addSection(@Header("authorization") authorization: String, @Body data: HashMap<String, Any>):Call<ResAddSection>

    /**===Add farm Module*/
    //Get Crops
    @GET("/api/crops")
    fun getCrops(@Header("authorization") authorization: String): Call<ResCrops>

    //Get Crop variety
    @GET("/api/crop/verities/{cropId}")
    fun getCropVariety(@Header("authorization") authorization: String, @Path("cropId") cropId: String): Call<ResCropVariety>

    //Add Farm
    @POST("/api/farms")
    fun addFarm(@Header("authorization") authorization: String, @Body data: HashMap<String, Any>): Call<ResAddFarm>

    //Get All Farms for Farmer
    @GET("/api/farms?p=0&l=20")
    fun getAllFarms(@Header("authorization") authorization: String): Call<ArrayList<ResAllFarms>>

    //Get Particular Farm
    @GET("/api/farms/{farmId}")
    fun getParticularFarm(@Header("authorization") authorization: String, @Path("farmId") farmId: String): Call<ResParticularFarm>

    //Update Farm
    @PUT("/api/farms")
    fun updateFarm(@Header("authorization") authorization: String, @Body data: HashMap<String, Any>): Call<ResAddFarm>

    //Delete Farm
    @DELETE("/api/farms/{farmId}")
    fun deleteFarm(@Header("authorization") authorization: String, @Path("farmId") farmId: String): Call<ResBasic>

    /**=================================Specx*/

    // All scans list
    @GET("api/physical-scans?v=1&p=0&l=100")
    fun getSpecxScans(
        @Header("authorization") authorization: String, @Query("dateFrom") dateFrom: String, @Query(
            "dateTo"
        ) dateTo: String
    ): Call<ArrayList<ResSpecxScans>>


    //result of particular scan
    @GET("api/physical-scans/{id}/results")
    fun getSpecxScanResult(@Header("authorization") authorization: String, @Path("id") id: String): Call<ArrayList<ResSpecxScanDetail>>


    /**===============================Specx Chemical*/
//    http://23.98.216.140:9992/api/scans/vendor?v=1
    @GET("api/scans/vendor?v=1")
    fun getSpecxChemicalScans(@Header("authorization") authorization: String): Call<ResSpecxChemicalScans>

   /**===============================Qualix*/

    @GET("/oauth/authorize")
    fun oauth(@QueryMap query: Map<String, String>): Call<OauthResponse>

    @POST("/login?bearer=mobile")
    fun qualixLogin(@Body request: RequestBody): Call<LoginResponse>

    @GET("/api/commodity")
    fun getCommodity(@Header("authorization") authorization: String): Call<ArrayList<ResCommodity>>

    @GET("api/scan/history?p=0&l=100")
    fun scanHistory(@Header("authorization") authorization: String, @QueryMap options: Map<String, String>): Call<ScanHistoryRes>

    @GET("/api/farmer/commodity")
    fun farmerCommodity(@Header("authorization") authorization: String,@Path("farmer_id") farmer_id: String): Call<ArrayList<ResCommodity>>
}