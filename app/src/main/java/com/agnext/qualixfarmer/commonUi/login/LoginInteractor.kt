package com.agnext.sensenextmyadmin.ui.auth.login

import android.app.Activity
import android.content.Context
import com.agnext.qualixfarmer.base.Constant
import com.agnext.qualixfarmer.base.SessionClass
import com.agnext.qualixfarmer.base.hasConnection
import com.agnext.qualixfarmer.network.ApiClient
import com.agnext.qualixfarmer.network.ApiInterface
import com.agnext.qualixfarmer.network.Response.OauthResponse
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
    }

    fun auth(listener: OnLoginFinishedListener) {
        val query: HashMap<String, String> = HashMap()
        query["client_id"] = "client-mobile"
        query["response_type"] = "code"
        val apiService = ApiClient.getQauthClient().create(ApiInterface::class.java)
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
        val request = HashMap<String, String>()
        val apiService = ApiClient.getQauthClient().create(ApiInterface::class.java)
        val call = apiService.qualixLogin(request)
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