package com.agnext.qualixfarmer.commonUi.registerUser.otpDialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.agnext.sensenextmyadmin.utils.extensions.ScreenState


class OtpViewModel(val otpInteracter: OtpInteracter) : ViewModel(),
    OtpInteracter.onOtpFinishedListener
{

    private val _otpState: MutableLiveData<ScreenState<OtpState>> = MutableLiveData()
    val otpState: LiveData<ScreenState<OtpState>>
        get() = _otpState

    /**Forward Flow*/
    fun verifyOTP(data: OtpData)
    {
        otpInteracter.verifyOtp(data,this)
    }

    /**Backward Flow*/
    override fun onOtpSucess() {
        _otpState.value = ScreenState.Render(OtpState.otpSucess)
    }

    override fun onOtpFailure() {
        _otpState.value = ScreenState.Render(OtpState.otpFailure)
    }
}



class OtpViewModelFactory(private val otpInteracter: OtpInteracter) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return OtpViewModel(otpInteracter) as T
    }
}
