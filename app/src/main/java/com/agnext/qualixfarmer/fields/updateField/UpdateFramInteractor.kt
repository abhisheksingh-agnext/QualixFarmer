package com.agnext.qualixfarmer.fields.updateField

import android.content.Context
import com.agnext.qualixfarmer.fields.addField.AddFarmInteractor
import com.agnext.qualixfarmer.network.ApiClient
import com.agnext.qualixfarmer.network.ApiInterface
import com.agnext.qualixfarmer.network.Response.FarmRes
import com.agnext.qualixfarmer.network.Response.ResCrops
import com.agnext.qualixfarmer.network.Response.ResCropsVariety
import com.agnext.qualixfarmer.network.Response.ResParticularFarm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateFramInteractor(private val context: Context)
{
    private val apiService = ApiClient.getClient().create(ApiInterface::class.java)
    private val getVMSService = ApiClient.getVMSClient(context).create(ApiInterface::class.java)
    private val getDCMService = ApiClient.getDCMClient(context).create(ApiInterface::class.java)

    /**Get AllC rop*/
    fun getAllCrop(token: String, mCallBack: UpdateFarmFinishedListener) {
        val call = getDCMService.getDcmCommodity(token)
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
    fun getCropVariety(token: String, cropId: String, mCallBack: AddFarmInteractor.AddFarmCallback) {

        val call =getDCMService.getDcmCommodityVariety(token,cropId)
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
    /**Api hit to get particular farm*/
    fun getParticularFarm(token:String,farmId:String,mCallback:UpdateFarmFinishedListener)
    {
        val call = apiService.getParticularFarm(token,farmId)
        call.enqueue(object: retrofit2.Callback<ResParticularFarm> {
            override fun onFailure(call: Call<ResParticularFarm>, t: Throwable) {
                mCallback.onGetParticularFarmFailure()
            }

            override fun onResponse(
                call: Call<ResParticularFarm>,
                response: Response<ResParticularFarm>
            ) {
                mCallback.onGetParticularFarmSuccess(response.body())

            }
        })
    }
    /**Add Farm*/
    fun updateFarm(token: String,farmId: String, data: HashMap<String, Any>, mCallBack: UpdateFarmFinishedListener) {
        val call = getVMSService.updatePlot(farmId,data)
        call.enqueue(object : Callback<FarmRes> {
            override fun onFailure(call: Call<FarmRes>, t: Throwable) {
                mCallBack.updateFarmFailureCallback()
            }

            override fun onResponse(call: Call<FarmRes>, response: Response<FarmRes>) {
                when (response.code()) {
                    200 -> {
                        mCallBack.updateFarmSuccessCallback(response)
                    }
                    401 -> {
                        mCallBack.tokenExpire()
                    }
                    else -> {
                        mCallBack.updateFarmFailureCallback()
                    }
                }
            }
        })
    }
    interface UpdateFarmFinishedListener {
        fun onGetParticularFarmSuccess(response: ResParticularFarm?)
        fun onGetParticularFarmFailure()

        fun getCropSuccessCallback(response: Response<ArrayList<ResCrops>>)
        fun getCropFailureCallback()
        //CropVariety
        fun getCropVarietySuccess(response: Response<ArrayList<ResCropsVariety>>)
        fun getCropVarietyFailure()

        fun updateFarmSuccessCallback(response: Response<FarmRes>)
        fun updateFarmFailureCallback()

        fun tokenExpire()
    }

}