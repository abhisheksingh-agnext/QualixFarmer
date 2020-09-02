package com.agnext.qualixfarmer.tea.scanDoneDetail

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.agnext.qualixfarmer.network.ApiClient
import com.agnext.qualixfarmer.network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScanAreaInteractor {
    val apiService = ApiClient.getClient().create(ApiInterface::class.java)

    /**Get garden data*/
    fun getGardenData(token: String, empCode: String, mCallback: ScanAreaFinishedListener) {

        try {
            val call = apiService.getUserDetail(token, empCode)
            call!!.enqueue(object : Callback<ResponseGetUserDetail?> {
                @RequiresApi(Build.VERSION_CODES.ECLAIR)
                override fun onResponse(
                    call: Call<ResponseGetUserDetail?>,
                    response: Response<ResponseGetUserDetail?>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()!!.success.equals("true")) {
                            if (response.body()!!.data.size > 0) {

                                mCallback.onUserDataSuccess(response.body()!!)

                            } else {

                                mCallback.onUserDataFailure()
                            }


                        } else {
                            mCallback.onUserDataFailure()

                        }
                    } else {
                        if (response.code() == 401) {
                            mCallback.onAPITokenExpire()
                        }

                    }

                }

                override fun onFailure(call: Call<ResponseGetUserDetail?>, t: Throwable) {
                    Log.e("Failed", t.toString())
                    mCallback.onUserDataFailure()

                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            mCallback.onUserDataFailure()

        }

    }

    /**Api hit to save scan data*/
    fun saveScanData(
        token: String,
        data: HashMap<String, Any>,
        mCallback: ScanAreaFinishedListener
    ) {
        try {
            val call = apiService.saveScanData(token, data)
            call.enqueue(object : Callback<ResScanDetail?> {
                @RequiresApi(Build.VERSION_CODES.ECLAIR)
                override fun onResponse(
                    call: Call<ResScanDetail?>,
                    response: Response<ResScanDetail?>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()!!.success.equals("true")) {
                            if (response.body()!!.data != null) {

                                mCallback.onUploadScanDataSuccess()

                            } else {

                                // mCallback.onUploadScanDataFailure()
                                mCallback.onUploadScanDataSuccess()

                            }


                        } else {
                            //  mCallback.onUploadScanDataFailure()
                            mCallback.onUploadScanDataSuccess()

                        }
                    }
                    else if (response.code() == 201) {
                        mCallback.onUploadScanDataSuccess()
                    }
                    else {
                        if (response.code() == 401) {
                           mCallback.onAPITokenExpire()

                        }

                    }

                }

                override fun onFailure(call: Call<ResScanDetail?>, t: Throwable) {
                    Log.e("Failed", t.toString())
                    //mCallback.onUploadScanDataFailure()
                    mCallback.onUploadScanDataSuccess()

                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            mCallback.onUploadScanDataFailure()

        }
    }
}

interface ScanAreaFinishedListener {

    fun onUserDataSuccess(body: ResponseGetUserDetail)
    fun onUserDataFailure()
    fun onUploadScanDataSuccess()
    fun onUploadScanDataFailure()
    fun onAPITokenExpire()

}
