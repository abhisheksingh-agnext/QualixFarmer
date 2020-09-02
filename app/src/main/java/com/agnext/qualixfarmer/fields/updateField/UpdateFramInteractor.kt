package com.agnext.qualixfarmer.fields.updateField

import android.content.Context
import com.agnext.qualixfarmer.network.ApiClient
import com.agnext.qualixfarmer.network.ApiInterface
import com.agnext.qualixfarmer.network.Response.ResParticularFarm
import retrofit2.Call
import retrofit2.Response

class UpdateFramInteractor(private val context: Context)
{
    private val apiService = ApiClient.getClient().create(ApiInterface::class.java)

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

    interface UpdateFarmFinishedListener {
        fun onGetParticularFarmSuccess(response: ResParticularFarm?)
        fun onGetParticularFarmFailure()
    }

}