package com.agnext.qualixfarmer.fields.farmList

import android.content.Context
import android.provider.SyncStateContract
import com.agnext.qualixfarmer.base.Constant
import com.agnext.qualixfarmer.network.ApiClient
import com.agnext.qualixfarmer.network.ApiClient.getVMSClient
import com.agnext.qualixfarmer.network.ApiInterface
import com.agnext.qualixfarmer.network.Response.FarmRes
import com.agnext.qualixfarmer.network.Response.ResAllFarms
import com.agnext.qualixfarmer.network.Response.ResBasic
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FieldInteractor(private val context: Context) {

    private val apiService = ApiClient.getClient().create(ApiInterface::class.java)
    private val qualixService = ApiClient. getQauthClient(context).create(ApiInterface::class.java)
    private val getVMSService = ApiClient.getVMSClient(context).create(ApiInterface::class.java)

//    //Api hit to get list of all farms for farmer
//    fun getAllFarm(token: String, mCallBack: AllFarmCallBack) {
//        val call = apiService.getAllFarms(token)
//        call.enqueue(object : Callback<ArrayList<ResAllFarms>> {
//            override fun onFailure(call: Call<ArrayList<ResAllFarms>>, t: Throwable) {
//                mCallBack.fieldListFailure()
//            }
//
//            override fun onResponse(
//                call: Call<ArrayList<ResAllFarms>>,
//                response: Response<ArrayList<ResAllFarms>>
//            ) {
//                mCallBack.fieldListSuccess(response)
//            }
//        })
//    }

    //Api hit to get list of all farms for farmer
    fun getAllFarm(token: String,farmerId:String, mCallBack: AllFarmCallBack) {
        val call = getVMSService.getPlots(farmerId)
        call.enqueue(object : Callback<ArrayList<FarmRes>> {
            override fun onFailure(call: Call<ArrayList<FarmRes>>, t: Throwable) {
                mCallBack.fieldListFailure()
            }

            override fun onResponse(
                call: Call<ArrayList<FarmRes>>,
                response: Response<ArrayList<FarmRes>>
            ) {
                mCallBack.fieldListSuccess(response)
            }
        })
    }

    //Delete farm
    fun deleteFarm(token: String, farmID: String,mCallBack: AllFarmCallBack) {
        val call = getVMSService.deletePlot(farmID)
        call.enqueue(object :Callback<ResBasic>{
            override fun onFailure(call: Call<ResBasic>, t: Throwable) {
                mCallBack.farmDeleteFailure()
            }

            override fun onResponse(call: Call<ResBasic>, response: Response<ResBasic>) {
                mCallBack.farmDeleteSuccess()

            }
        })
    }

}


interface AllFarmCallBack {
    fun fieldListSuccess(responseData: Response<ArrayList<FarmRes>>)
    fun fieldListFailure()

    fun farmDeleteSuccess()
    fun farmDeleteFailure()

}

data class FieldData(
    val fieldId: String,
    val fieldName: String,
    val farmerName: String,
    val cropSeason: String,
    val crop: String,
    val cropType: String,
    val startDate: String,
    val endDate: String,
    val addres: String,
    val District: String,
    val area: String
)