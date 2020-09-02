package com.agnext.qualixfarmer.commonUi.registerUser.signUp

import android.content.Context

class SignUpInteractor(private val context: Context) {

    interface OnSignUpFinishedListener {
        fun onAllCompanySucess()
        fun onAllCompanyFailure()
        fun onSignUpSucess()
        fun onSignUpFailure()
    }

    /** Api hit for SingUp*/
    fun signUp(username: SignUpData, listener: OnSignUpFinishedListener) {
        listener.onSignUpSucess()

    }

    /** Api hit for Company*/
    //api hit and call corresponding listener method
    fun getAllCompany( listener: OnSignUpFinishedListener) {
      listener.onAllCompanySucess()

    }

}