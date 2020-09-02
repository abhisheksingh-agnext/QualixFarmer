package com.agnext.qualixfarmer.commonUi.profile

import android.content.Context

class ProfileInteracter (private val context: Context) {

    /** Api hit for get Profile*/
    fun getProfile( listener: OnProfileFinishedListener) {
        listener.onGetProfileSuccess()
    }

    /**Api hit for update the profile*/
    fun editProfile(userId: String ,listener: OnProfileFinishedListener) {
        listener.onEditProfileSuccess()
    }
}
    interface OnProfileFinishedListener {

        fun onGetProfileSuccess()
        fun onGetProfileFailure()
        fun onEditProfileSuccess()
        fun onEditProfileFailure()
    }
