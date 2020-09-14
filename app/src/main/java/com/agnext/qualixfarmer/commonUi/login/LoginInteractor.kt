package com.agnext.sensenextmyadmin.ui.auth.login

import android.app.Activity
import android.content.Context
import com.agnext.qualixfarmer.base.Constant
import com.agnext.qualixfarmer.base.SessionClass
import com.agnext.qualixfarmer.base.hasConnection
import com.agnext.qualixfarmer.network.ApiClient
import com.agnext.qualixfarmer.network.ApiInterface
import com.agnext.qualixfarmer.network.Response.OauthResponse
import com.agnext.qualixfarmer.network.Response.VmsLoginRes
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginInteractor(private val context: Context) {

    lateinit var appName: String

    interface OnLoginFinishedListener {
        fun onUsernameError()
        fun onPasswordError()

        // fun onSuccess(loginResponse: LoginResponse?, context: Context)
        fun onSuccess(context: Context)

        fun onError()
        fun onConnectionError()

        fun onAuthSuccess(body: OauthResponse)
        fun onAuthFailure(s: String)

        fun onQualixLoginSuccess(body: LoginResponse)
        fun onQualixLoginFailure(s: String)

        fun onVmsLoginSuccess(body: VmsLoginRes)
        fun onVmsLoginError(s: String)
    }
    val apiService = ApiClient.getQauthClient(context).create(ApiInterface::class.java)
    val vmsService = ApiClient.getVMSClient(context).create(ApiInterface::class.java)
    fun auth(listener: OnLoginFinishedListener) {
        val query: HashMap<String, String> = HashMap()
        query["client_id"] = "client-mobile"
        query["response_type"] = "code"
        val call = apiService.oauth(query)
        call.enqueue(object : Callback<OauthResponse> {
            override fun onResponse(call: Call<OauthResponse>, response: Response<OauthResponse>) {
                when (response.code()) {
                    200 -> {
                        listener.onAuthSuccess(response.body()!!)
                    }
                    else -> {
                        listener.onAuthFailure("Error")
                    }
                }
            }

            override fun onFailure(call: Call<OauthResponse>, t: Throwable) {
                listener.onAuthFailure("Error")
            }
        })
    }

    fun qualixLogin(device_token: String, listener: OnLoginFinishedListener) {
        val requestBuilder = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
        requestBuilder.addFormDataPart("Signin", "Sign+In")
//        requestBuilder.addFormDataPart("bearer", "mobile")
        requestBuilder.addFormDataPart("username", "demooperator@gmail.com")
        requestBuilder.addFormDataPart("password", "Specx123!")
        val requestBody: RequestBody = requestBuilder.build()

        val call = apiService.qualixLogin(requestBody)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                when (response.code()) {
                    200 -> {
                        listener.onQualixLoginSuccess(response.body()!!)
                    }
                    else -> {
                        listener.onQualixLoginFailure("Error")
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                listener.onQualixLoginFailure("Error")
            }
        })
    }

    fun vmsLogin(requestBody : HashMap<String, String>,deviceToken: String ,listener: OnLoginFinishedListener) {

        val call = vmsService.vmsLogin(requestBody)
        call.enqueue(object :Callback<VmsLoginRes>{
            override fun onResponse(call: Call<VmsLoginRes>, response: Response<VmsLoginRes>) {
                when (response.code()) {
                    200 -> {
                        SessionClass(context as Activity).setVMSLogged(
                            true,
                            response.body()!!.id.toString(),
                            response.body()!!.name.toString(),
                            response.body()!!.email.toString(),
                            response.body()!!.contactNumber.toString(),
                            response.body()!!.role.toString(),
                            response.body()!!.statusId.toString(),
                            response.body()!!.status.toString(),
                            response.body()!!.token.toString(),
                            response.body()!!.account_number.toString()
                        )
                        listener.onVmsLoginSuccess(response.body()!!)
                    }
                    else -> {
                        listener.onVmsLoginError("Error")
                    }
                }
            }

            override fun onFailure(call: Call<VmsLoginRes>, t: Throwable) {
                listener.onVmsLoginError("Error")
            }
        })

    }
    fun login(
        username: String,
        password: String,
        deviceToken: String,
        listener: OnLoginFinishedListener
    ) {

        when {
            username.isEmpty() -> listener.onUsernameError()
            password.isEmpty() -> listener.onPasswordError()
            else -> {
//                processLoginRequest(username, password, listener)

                if (hasConnection(context)) {
                    processLoginRequest(username, password, deviceToken, listener)
                } else {
                    listener.onConnectionError()
                }

            }
        }
    }

    private fun processLoginRequest(
        name: String,
        pass: String,
        deviceToken: String,
        listener: OnLoginFinishedListener
    ) {
        try {
            val requestBody = HashMap<String, String>()

            if (name == "user1") {
                SessionClass(context as Activity).setBaseUrl(Constant.BaseURlTea)
                Constant.BaseURl = SessionClass(context).getBaseUrl()
                // requestBody["username"] = "assist1"
                requestBody["username"] = "assist@unilever.com"
                requestBody["password"] = "123456"
                requestBody["deviceToken"] = deviceToken
                appName = Constant.Tea

            } else if (name == "user2") {
                SessionClass(context as Activity).setBaseUrl(Constant.BaseURlSpecX)
                Constant.BaseURl = SessionClass(context).getBaseUrl()
                requestBody["username"] = "vendorTest@agnext.in"
                requestBody["password"] = "specx123"
                requestBody["role"] = "7"
                requestBody["deviceToken"] = deviceToken
                appName = Constant.SpecxPhy

            } else if (name == "user3") {
                SessionClass(context as Activity).setBaseUrl(Constant.BaseURlSpecXChemical)
                Constant.BaseURl = SessionClass(context).getBaseUrl()
                requestBody["username"] = "vendorTest@agnext.in"
                requestBody["password"] = "specx123"
                requestBody["role"] = "7"
                requestBody["deviceToken"] = deviceToken
                appName = Constant.SpecxChem

            } else {
                listener.onError()
            }


            val apiService = ApiClient.getClient().create(ApiInterface::class.java)
            val call = apiService.login(requestBody)

            call.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {


                        var loginResponse = response.body()
                        SessionClass(context as Activity).setLogged(
                            true,
                            loginResponse!!.token.toString(),
                            loginResponse.refreshToken.toString(),
                            loginResponse.user!!.name.toString(),
                            loginResponse.user!!.id.toString(),
                            loginResponse.user!!.baseLocation!!,
                            appName
                        )

                        SessionClass(context).setUser(loginResponse.user!!)
                        listener.onSuccess(context)

                    } else {
                        listener.onError()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    listener.onError()
                }
            })

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}