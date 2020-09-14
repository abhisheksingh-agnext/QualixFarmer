package com.agnext.qualixfarmer.tea.qualityAnalysis

import android.content.Context
import com.agnext.qualixfarmer.base.Constant
import com.agnext.qualixfarmer.network.ApiClient
import com.agnext.qualixfarmer.network.ApiInterface
import com.agnext.qualixfarmer.network.Response.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QualityAnaInterceptor(private val context: Context) {
    val apiService = ApiClient.getClient().create(ApiInterface::class.java)
    val apiServiceSCM = ApiClient.getScmClient().create(ApiInterface::class.java)


    fun getScanHistoryIn(
        data: MutableMap<String, String>,
        mCallback: OnQualityAnaFinishedListener
    ) {
        val call = apiServiceSCM.scanHistory("Bearer ${Constant.token}", data)
        call.enqueue(object : Callback<ScanHistoryRes> {
            override fun onFailure(call: Call<ScanHistoryRes>, t: Throwable) {
                mCallback.scanHistoryFailure("Error to get scan history")
            }

            override fun onResponse(
                call: Call<ScanHistoryRes>,
                response: Response<ScanHistoryRes>
            ) {
                when (response.code()) {
                    200 -> {
                        if (response.body()!!.scan_history_data!!.size > 0)
                            mCallback.scanHistorySuccess(response.body()!!.scan_history_data)
                        else
                            mCallback.onNoScansListSuccess()
                    }
                    204 -> {
                        mCallback.scanHistoryFailure("No record found")
                    }
                    401 -> {
                        mCallback.tokenExpire()
                    }
                    400 -> {
                        try {
                            val jObjError = JSONObject(response.errorBody()!!.string())
                            mCallback.scanHistoryFailure(jObjError.getString("error-message"))
                        } catch (e: Exception) {
                            mCallback.scanHistoryFailure(e.message.toString())
                        }
                    }
                    else -> {
                        mCallback.scanHistoryFailure("Error to get scan history")
                    }
                }
            }
        })
    }

    fun     getCommodity(farmerId: String, mCallback: OnQualityAnaFinishedListener) {
        val call = apiServiceSCM.farmerCommodity("Bearer ${Constant.token}", farmerId)
        call.enqueue(object : Callback<ArrayList<ResCommodity>> {
            override fun onFailure(call: Call<ArrayList<ResCommodity>>, t: Throwable) {
                mCallback.onCommodityFailure("Error to get Commodity")
            }

            override fun onResponse(
                call: Call<ArrayList<ResCommodity>>,
                response: Response<ArrayList<ResCommodity>>
            ) {
                when (response.code()) {
                    200 -> {
                        mCallback.onCommoditySuccess(response.body()!!)
                    }
                    204 -> {
                        mCallback.onCommodityFailure("No record found")
                    }
                    401 -> {
                        mCallback.tokenExpire()
                    }
                    400 -> {
                        try {
                            val jObjError = JSONObject(response.errorBody()!!.string())
                            mCallback.onCommodityFailure(jObjError.getString("error-message"))
                        } catch (e: Exception) {
                            mCallback.onCommodityFailure(e.message.toString())
                        }
                    }
                    else -> {
                        mCallback.onCommodityFailure("Error to get Commodity")
                    }
                }
            }
        })

    }

    /**API HIT  to get scan list */
    fun getScansListIn(
        token: String,
        startDay: String,
        endDay: String,
        listener: OnQualityAnaFinishedListener
    ) {
        val call = apiService.getScans(token, "0", "100", endDay, startDay)
        call.enqueue(object : Callback<ResUserScans> {
            override fun onResponse(
                call: Call<ResUserScans>,
                response: Response<ResUserScans>
            ) {

                if (response.isSuccessful) {
                    if (response.body()!!.data != null)
                        listener.onScansListSuccess(response.body()!!.data as ArrayList<ScanDetail>)
                    else
                        listener.onNoScansListSuccess()

                } else {

                    if (response.code() == 401) {
                        listener.onTokenExpired()
                    } else {
                        listener.onScansListFailure()
                    }
                }
            }

            override fun onFailure(call: Call<ResUserScans>, t: Throwable) {
                listener.onScansListFailure()
            }
        })


    }

    /**API HIT to get Avg of Scan Data*/
    fun getAvgScanData(token: String, listener: OnQualityAnaFinishedListener) {
        val call = apiService.getAvgScanData(token)
        call.enqueue(object : Callback<ResAvgScanData> {
            override fun onFailure(call: Call<ResAvgScanData>, t: Throwable) {
                listener.onAvgScansDataFailure()
            }

            override fun onResponse(
                call: Call<ResAvgScanData>,
                response: Response<ResAvgScanData>
            ) {

                if (response.isSuccessful) {
                    if (response.body()!!.data != null)
                        listener.onAvgScansDataSuccess(response.body()!!.data[0])
                    else
                        listener.onAvgScansDataFailure()

                } else {

                    if (response.code() == 401) {
                        listener.onAvgScansDataFailure()
                    } else {
                        listener.onAvgScansDataFailure()
                    }

                }
            }
        })

    }

    fun getMonthFlcDataIn(listener: OnQualityAnaFinishedListener) {
        listener.onMonthFlcDataSuccess()
    }

    interface OnQualityAnaFinishedListener {
        //Commodity
        fun onCommoditySuccess(body: ArrayList<ResCommodity>)
        fun onCommodityFailure(msg: String)

        //Scan list
        fun onScansListSuccess(scanList: ArrayList<ScanDetail>)
        fun onNoScansListSuccess()
        fun onScansListFailure()

        //Avg Scans Data
        fun onAvgScansDataSuccess(data: AvgData)
        fun onAvgScansDataFailure()

        fun onMonthFlcDataSuccess()
        fun onMonthFlcDataFailure()
        fun onTokenExpired()

        fun scanHistorySuccess(body: ArrayList<ScanData>?)
        fun scanHistoryFailure(massage: String)
        fun tokenExpire()

    }

}