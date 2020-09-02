package com.agnext.sensenextmyadmin.ui.auth.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.agnext.qualixfarmer.network.Response.OauthResponse
import com.agnext.sensenextmyadmin.utils.extensions.ScreenState

class LoginViewModel(private val loginInteractor: LoginInteractor) : ViewModel(),
    LoginInteractor.OnLoginFinishedListener {


    private val _loginState: MutableLiveData<ScreenState<LoginState>> = MutableLiveData()
    val loginState: LiveData<ScreenState<LoginState>>
        get() = _loginState

    private val _authResponse: MutableLiveData<OauthResponse> = MutableLiveData()
    val authResponse: LiveData<OauthResponse>
        get() = _authResponse

    private val _loginResponse: MutableLiveData<LoginResponse> = MutableLiveData()
    val loginResponse: LiveData<LoginResponse>
        get() = _loginResponse

    fun onLoginClicked(username: String, password: String, deviceToken: String) {
        _loginState.value = ScreenState.Loading
        loginInteractor.login(username, password, deviceToken, this)
    }

    fun qualixLogin() {
        // _loginState.value = ScreenState.Loading
        loginInteractor.qualixLogin("", this)
    }

    fun oauth() {
        loginInteractor.auth(this)
//        loginInteractor.qualixLogin("", this)
    }

    override fun onUsernameError() {
        _loginState.value = ScreenState.Render(LoginState.EmptyUserName)
    }

    override fun onPasswordError() {
        _loginState.value = ScreenState.Render(LoginState.EmptyPassword)
    }

    override fun onSuccess(context: Context) {
        _loginState.value = ScreenState.Render(LoginState.Success)
    }

//    override fun onSuccess(loginResponse: LoginResponse?, context: Context) {
//
//        SessionClass.setLogged(
//            context as Activity, true,
//            loginResponse!!.token.toString(),
//            loginResponse.refreshToken.toString(),
//            loginResponse.userDetails!!.name.toString(),
//            loginResponse.userDetails!!.id.toString(),
//            loginResponse.userDetails!!.client!!.clientId!!,
//            loginResponse.userDetails!!.baseLocation!!,
//            loginResponse.userDetails!!.code!!
//        )
//
//        SessionClass.setTokenOverRide(context as Activity, false)
//        _loginState.value = ScreenState.Render(LoginState.Success)
//
//    }

    override fun onError() {
        _loginState.value = ScreenState.Render(LoginState.ErrorServer)
    }

    override fun onConnectionError() {
        _loginState.value = ScreenState.Render(LoginState.LoginConnectionError)

    }

    override fun onAuthSuccess(body: OauthResponse) {
        _authResponse.value = body
        _loginState.value = ScreenState.Render(LoginState.AuthSuccess)
    }

    override fun onAuthFailure(s: String) {
    }

    override fun onQualixLoginSuccess(body: LoginResponse) {
        _loginResponse.value=body
        _loginState.value = ScreenState.Render(LoginState.QualixLoginSuccess)

    }

    override fun onQualixLoginFailure(s: String) {
    }


}

class LoginViewModelFactory(private val loginInteractor: LoginInteractor) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(loginInteractor) as T
    }
}