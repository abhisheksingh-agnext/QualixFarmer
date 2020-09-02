package com.agnext.qualixfarmer.fields.farmList

import com.agnext.qualixfarmer.network.ApiClient
import com.agnext.qualixfarmer.network.ApiInterface
import com.agnext.qualixfarmer.network.Response.ResAllFarms
import com.agnext.qualixfarmer.network.Response.ResBasic
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FieldInteractor {

    private val apiService = ApiClient.getClient().create(ApiInterface::class.java)

    //Api hit to get list of all farms for farmer
    fun getAllFarm(token: String, mCallBack: AllFarmCallBack) {
        val call = apiService.getAllFarms(token)
        call.enqueue(object : Callback<ArrayList<ResAllFarms>> {
            override fun onFailure(call: Call<ArrayList<ResAllFarms>>, t: Throwable) {
                mCallBack.fieldListFailure()
            }

            override fun onResponse(
                call: Call<ArrayList<ResAllFarms>>,
                response: Response<ArrayList<ResAllFarms>>
            ) {
                mCallBack.fieldListSuccess(response)
            }
        })
    }

    //Delete farm
    fun deleteFarm(token: String, farmID: String,mCallBack: AllFarmCallBack) {
        val call = apiService.deleteFarm(token,farmID)
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
    fun fieldListSuccess(responseData: Response<ArrayList<ResAllFarms>>)
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