package com.agnext.qualixfarmer.base

import android.app.Activity
import android.content.Context
import android.preference.PreferenceManager


object  FCMTokenSession {
    val FCM_TOKEN = "FCM_TOKEN"

    /*------------------------------DIFFERENT PHASE tEMP AND HUM */
    fun setFCMtoken(context: Context,
                    token: String) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString(FCM_TOKEN, token)
        editor.commit()
    }


    fun getFCMtoken(activity: Activity): String {
        val preferences = PreferenceManager.getDefaultSharedPreferences(activity)
        return preferences.getString(FCM_TOKEN, "").toString()
    }
}
