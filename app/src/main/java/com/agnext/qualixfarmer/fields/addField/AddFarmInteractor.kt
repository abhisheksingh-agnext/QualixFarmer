package com.agnext.qualixfarmer.fields.addField

import android.content.Context
import com.agnext.qualixfarmer.network.ApiClient
import com.agnext.qualixfarmer.network.ApiInterface
import com.agnext.qualixfarmer.network.Response.ResAddFarm
import com.agnext.qualixfarmer.network.Response.ResCrops
import com.agnext.qualixfarmer.network.Response.ResCropsVariety
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddFarmInteractor(private val context: Context) {

    private val apiService = ApiClient.getClient().create(ApiInterface::class.java)
    private val qualixService = ApiClient.getQauthClient(context).create(ApiInterface::class.java)
    private val getVMSService = ApiClient.getVMSClient(context).create(ApiInterface::class.java)
    private val getDCMClient = ApiClient.getDCMClient(context).create(ApiInterface::class.java)

    /**Get AllC rop*/
    fun getAllCrop(token: String, mCallBack: AddFarmCallback) {
        val call = getDCMClient.getDcmCommodity(token)
        call.enqueue(object : Callback<ArrayList<ResCrops>> {
            override fun onFailure(call: Call<ArrayList<ResCrops>>, t: Throwable) {
                mCallBack.getCropFailureCallback()
            }

            override fun onResponse(call: Call<ArrayList<ResCrops>>, response: Response<ArrayList<ResCrops>>) {
                when(response.code() )
                {
                    200->{ mCallBack.getCropSuccessCallback(response)}
                    401->{ mCallBack.tokenExpire()}
                    else->{mCallBack.getCropFailureCallback()}
                }
            }
        })
    }

    /**Get Crop Variety*/
    fun getCropVariety(token: String, cropId: String, mCallBack: AddFarmCallback) {

        val call =getDCMClient.getDcmCommodityVariety(token,cropId)
        call.enqueue(object : Callback<ArrayList<ResCropsVariety>> {
            override fun onFailure(call: Call<ArrayList<ResCropsVariety>>, t: Throwable) {
                mCallBack.getCropVarietyFailure()
            }

            override fun onResponse(
                call: Call<ArrayList<ResCropsVariety>>,
                response: Response<ArrayList<ResCropsVariety>>
            ) {
                if (response.isSuccessful) {
                    mCallBack.getCropVarietySuccess(response)
                } else {
                    if (response.code() == 401) {
                        mCallBack.tokenExpire()
                    } else {
                        mCallBack.getCropVarietyFailure()
                    }
                }
            }
        })
    }

    /**Add Farm*/
    fun addFarm(token: String, data: HashMap<String, Any>, mCallBack: AddFarmCallback) {
        val call = getVMSService.addPlot(data)
        call.enqueue(object : Callback<ResAddFarm> {
            override fun onFailure(call: Call<ResAddFarm>, t: Throwable) {
                mCallBack.addFarmFailureCallback()
            }

            override fun onResponse(call: Call<ResAddFarm>, response: Response<ResAddFarm>) {
                when (response.code()) {
                    201 -> {
                        mCallBack.addFarmSuccessCallback(response)
                    }
                    401 -> {
                        mCallBack.tokenExpire()
                    }
                    else -> {
                        mCallBack.addFarmFailureCallback()
                    }
                }
            }
        })
    }



    interface AddFarmCallback {
        //Crop
        fun getCropSuccessCallback(response: Response<ArrayList<ResCrops>>)

        fun getCropFailureCallback()

        //CropVariety
        fun getCropVarietySuccess(response: Response<ArrayList<ResCropsVariety>>)

        fun getCropVarietyFailure()


        //Add farm
        fun addFarmSuccessCallback(response: Response<ResAddFarm>)

        fun addFarmFailureCallback()

        //Token
        fun tokenExpire()
    }
}