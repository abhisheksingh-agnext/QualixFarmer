package com.agnext.sensenextmyadmin.ui.auth.login

import android.app.Activity
import android.content.Context
import com.agnext.qualixfarmer.base.Constant
import com.agnext.qualixfarmer.base.SessionClass
import com.agnext.qualixfarmer.base.hasConnection
import com.agnext.qualixfarmer.network.ApiClient
import com.agnext.qualixfarmer.network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RefreshInteractor(private val context: Context) {

    interface OnRefreshFinishedListener {
        fun onRefreshSuccess(token:String)
        fun onRefreshFailed()
    }

    fun getRefreshedToken(oldToken: String, success:Boolean, listner:OnRefreshFinishedListener){

        if(success){
            listner.onRefreshSuccess(oldToken)
        }else{
            listner.onRefreshFailed()
        }

    }

}