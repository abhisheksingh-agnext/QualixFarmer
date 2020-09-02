package com.agnext.qualixfarmer.commonUi.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.agnext.sensenextmyadmin.utils.extensions.ScreenState

class SplashViewModel(private val splashInteractor: SplashInteractor) : ViewModel(),
    SplashInteractor.OnSplashFinishedListener {

    private val _splashState: MutableLiveData<ScreenState<SplashState>> = MutableLiveData()

    val splashState: LiveData<ScreenState<SplashState>>
        get() = _splashState

    fun onSplashCheck() {
        _splashState.value = ScreenState.Loading
        splashInteractor.checkLoggedIn(this)
    }


    override fun onLoggedIn() {
        _splashState.value = ScreenState.Render(SplashState.LoggedIn)
    }

    override fun onLoggedOut() {
        _splashState.value = ScreenState.Render(SplashState.LoggedOut)
    }

}

class SplashViewModelFactory(private val splashInteractor: SplashInteractor) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SplashViewModel(splashInteractor) as T
    }
}