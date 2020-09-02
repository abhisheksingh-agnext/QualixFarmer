package com.agnext.qualixfarmer.commonUi.splash

import android.app.Activity
import android.content.Context
import com.agnext.qualixfarmer.base.SessionClass
import com.agnext.sensenextmyadmin.utils.extensions.postDelayed


class SplashInteractor(private val context: Context) {

    interface OnSplashFinishedListener {
        fun onLoggedIn()
        fun onLoggedOut()
    }

    fun checkLoggedIn(listener: OnSplashFinishedListener) {

        postDelayed(3000) {
            when {
//                SessionClass.getLogged(context as Activity) -> listener.onLoggedIn()
                SessionClass(context as Activity).getLogged() -> listener.onLoggedIn()

                else -> listener.onLoggedOut()
            }
        }

    }
}