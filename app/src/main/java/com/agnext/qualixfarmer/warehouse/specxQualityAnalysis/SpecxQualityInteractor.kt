package com.agnext.qualixfarmer.warehouse.specxQualityAnalysis

import android.content.Context

import com.agnext.qualixfarmer.network.ApiClient
import com.agnext.qualixfarmer.network.ApiInterface
import com.agnext.qualixfarmer.network.Response.ResSpecxChemicalScans
import com.agnext.qualixfarmer.network.Response.ResSpecxScans
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SpecxQualityInteractor(private val context: Context) {

    /**Api hit ScanList*/
    fun specxScanList(
        fromDate: String,
        toDate: String,
        token: String,
        listener: SpecxQualityFinishedListener
    ) {
//        progress.visibility = View.VISIBLE
        val apiService = ApiClient.getClient().create(ApiInterface::class.java)
        val call = apiService.getSpecxScans(
            token,
            fromDate,
            toDate
        )
        call.enqueue(object : Callback<ArrayList<ResSpecxScans>> {
            override fun onFailure(call: Call<ArrayList<ResSpecxScans>>, t: Throwable) {
                listener.onSpecxAllScanFailure()
            }

            override fun onResponse(
                call: Call<ArrayList<ResSpecxScans>>,
                response: Response<ArrayList<ResSpecxScans>>
            ) {

                if (response.isSuccessful) {
                    if (response.body()!!.size > 1) {
                        listener.onSpecxAllScanSuccess(response.body()!!)

                    } else {

                        if (response.code() == 401) {
                            listener.onTokenExpire()
                        }
                        else{ listener.onSpecxAllScanFailure()}
                    }

                }
            }
        })

    }

    /**Api hit Chemical ScanList*/
    fun specxChemicalScanList(
        token: String,
        listener: SpecxQualityFinishedListener
    ) {
//        progress.visibility = View.VISIBLE
        val apiService = ApiClient.getClient().create(ApiInterface::class.java)
        val call = apiService.getSpecxChemicalScans(
            token
        )
        call.enqueue(object : Callback<ResSpecxChemicalScans> {
            override fun onFailure(call: Call<ResSpecxChemicalScans>, t: Throwable) {
                listener.onSpecxChemicalScanFailure()
            }

            override fun onResponse(
                call: Call<ResSpecxChemicalScans>,
                response: Response<ResSpecxChemicalScans>
            ) {

                if (response.isSuccessful) {
                    if (response.body()!!.data!!.size > 1) {
                        listener.onSpecxChemicalScanSuccess(response.body()!!)

                    } else {

                        if (response.code() == 401) {

                            listener.onTokenExpire()
                        }
                        else
                            listener.onSpecxChemicalScanFailure()
                    }

                }
            }
        })

    }

    interface SpecxQualityFinishedListener {
        fun onSpecxAllScanSuccess(arrayList: ArrayList<ResSpecxScans>)
        fun onSpecxAllScanFailure()

        fun onSpecxChemicalScanSuccess(data: ResSpecxChemicalScans)
        fun onSpecxChemicalScanFailure()

        fun onTokenExpire()

    }
}