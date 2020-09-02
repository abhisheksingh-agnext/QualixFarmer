package  com.agnext.qualixfarmer.base

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import com.agnext.qualixfarmer.base.Constant.Companion.KEY_APP_LANGUAGE
import com.agnext.qualixfarmer.base.Constant.Companion.KEY_BASE_URL
import com.agnext.qualixfarmer.base.Constant.Companion.USER_DATA
import com.agnext.sensenextmyadmin.ui.auth.login.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class SessionClass(activity: Activity) {
    private val preferences = activity.getSharedPreferences("Qualix", MODE_PRIVATE)
    private val editor = preferences.edit()


    private val PREF = "PREF"
    private val NAV_TYPE = "NAV_TYPE"
    private val THEME_TYPE = "THEME_TYPE"


    /*-----------------------------  THEME TYPE*/
    fun setTheme(theme: String) {
        editor.putString(THEME_TYPE, theme)
        editor.commit()
    }

    fun getTheme(): String {
        return preferences.getString(THEME_TYPE, Constant.THEME_1)!!
    }


    /**APP LANGUAGE*/
    fun setLanguage(lang: String) {
        editor.putString(KEY_APP_LANGUAGE, lang)
        editor.commit()
    }

    fun getLanguage(): String {
        return preferences.getString(KEY_APP_LANGUAGE, "en")!!
    }


    /**Base URL*/
    fun setBaseUrl(url: String) {
        editor.putString(KEY_BASE_URL, url)
        editor.commit()
    }

    fun getBaseUrl(): String {
        return preferences.getString(KEY_BASE_URL, "http://23.98.216.140:5678")!!
    }

    fun setUser(user: User) {
        val gson = Gson()
        val type = object : TypeToken<User>() {}.type
        val json = gson.toJson(user, type)
        editor.putString(USER_DATA, json)
        editor.commit()
    }

    fun getUser(): User {
        var testObject = User()
        var selectObject = preferences.getString(USER_DATA, "")

        val gson = Gson()
        if (selectObject != null) {
            val type = object : TypeToken<User>() {}.type
            testObject = gson.fromJson(selectObject, type)
        }
        return testObject

    }

    /**LOGGED IN*/
    fun setLogged(
        Logged: Boolean,
        userToken: String,
        refreshToken: String,
        userName: String,
        userID: String,
        baseLocation: String,
        appName: String
    ) {
        editor.putBoolean("Logged", Logged)
        editor.putString("UserToken", userToken)
        editor.putString("refreshToken", refreshToken)
        editor.putString("userName", userName)
        editor.putString("userID", userID)
        editor.putString("baseLocation", baseLocation)
        editor.putString("appName", appName)
        editor.commit()
    }

    fun getUserName(): String {
        return preferences.getString("userName", "")!!
    }

    fun getUserId(): String {
        return preferences.getString("userID", "")!!
    }

    fun getLogged(): Boolean {
        return preferences.getBoolean("Logged", false)
    }

    fun setLogged(logStatus: Boolean) {
        editor.putBoolean("Logged", logStatus)
        editor.commit()
    }

    fun getUserToken(): String {
        return preferences.getString("UserToken", "")!!
    }

    fun getAppName(): String {
        return preferences.getString("appName", "")!!
    }

}