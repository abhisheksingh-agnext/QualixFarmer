package com.agnext.qualixfarmer.commonUi.registerUser.otpDialog

import android.content.Context

class OtpInteracter(private val context: Context) {

    interface onOtpFinishedListener {

        fun onOtpSucess()
        fun onOtpFailure()
    }


    /** Api hit for Company*/
    //api hit and call corresponding listener method

    fun verifyOtp(otpData: OtpData, listener: onOtpFinishedListener) {
        if (otpData.otp == "12345")
            listener.onOtpSucess()
        else
            listener.onOtpFailure()

    }
}