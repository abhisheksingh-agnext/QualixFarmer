package com.agnext.qualixfarmer.base

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.agnext.qualixfarmer.R
import com.agnext.qualixfarmer.commonUi.login.LoginActivity
import com.agnext.qualixfarmer.commonUi.splash.SplashActivity
import com.agnext.qualixfarmer.utils.IntentUtil
import com.google.firebase.iid.FirebaseInstanceId
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.time.seconds


open class BaseActivity : AppCompatActivity() {
    companion object {
        var section_id: Int = 0
        var img: Bitmap? = null


    }
/*TODO MOVE TO NEXT SCREEN */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when (SessionClass(this).getTheme()) {
            Constant.THEME_0 -> {
                setTheme(R.style.AppTheme_0)
            }
            Constant.THEME_1 -> {
                setTheme(R.style.AppTheme_1)
                val window = window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary1)
            }
            Constant.THEME_2 -> {
            setTheme(R.style.AppTheme_2)
        }
            Constant.THEME_3 -> {
                setTheme(R.style.AppTheme_3)
            }

        }
        setContentView(R.layout.activity_base)
    }


    fun hideKeyboard(activity: Activity) {
        val view = activity.currentFocus
        view?.let { v ->
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.let { it.hideSoftInputFromWindow(v.windowToken, 0) }
        }
    }


    fun setLang(activity: Activity) {

        val lang = SessionClass(activity).getLanguage()
        val myLocale = Locale(lang)
        val conf = activity.resources.configuration
        conf.locale = myLocale
        activity.resources.updateConfiguration(conf, activity.resources.displayMetrics)

    }
    fun userToken(context:Activity):String
    {
        return  "Bearer " + SessionClass(context).getUserToken()
    }

    fun logout(context: Context) {
        SessionClass(context as Activity).setLogged(
            false,
            "",
            "",
            "",
            "",
            "",""

        )
        IntentUtil.moveScreenIntent(context, LoginActivity::class.java, true)
    }

    fun showToast(context: Context,message:String)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
    }

}

fun showLogoutDialog(
    title: String?,
    message: String?,
    context: Context
) {
    AlertDialog.Builder(context)
        .setTitle("$title")
        .setMessage("$message")

        .setNegativeButton(android.R.string.cancel, null)
        .setPositiveButton(android.R.string.ok) { _, _ ->

            //Set Language and Session
            SessionClass(context as Activity).setLogged(false)
            SessionClass(context).setLanguage("en")
            SessionClass(context).setTheme(Constant.THEME_1)
            IntentUtil.moveNextScreenWithNoStack(context, SplashActivity::class.java)
        }
        .setIcon(android.R.drawable.ic_dialog_alert)
        .show()
}

fun hasConnection(context: Context): Boolean {
    val cm = context.getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager
    val wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
    if (wifiNetwork != null && wifiNetwork.isConnected) {
        return true
    }
    val mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
    if (mobileNetwork != null && mobileNetwork.isConnected) {
        return true
    }
    val activeNetwork = cm.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnected
}



/*@Method Calculate Date of calender day*/
fun getCalculatedDateEpoch(date: String, dateFormat: String, days: Int): Long {
    val cal = Calendar.getInstance()
    val s = SimpleDateFormat(dateFormat)
    if (date.isNotEmpty()) {
        cal.time = s.parse(date)
    }
    cal.add(Calendar.DAY_OF_YEAR, days)
    //return s.format(Date(cal.timeInMillis))
return  Date(cal.timeInMillis).time
}





fun getCalculatedDate(date: String, dateFormat: String, days: Int): String {
    val cal = Calendar.getInstance()
    val s = SimpleDateFormat(dateFormat)
    if (date.isNotEmpty()) {
        cal.time = s.parse(date)
    }
    cal.add(Calendar.DAY_OF_YEAR, days)
    val date=cal.timeInMillis
    return s.format(Date(date))
   // return  Date(cal.timeInMillis).time
}

fun getCalculatedEpochDate(date: String, dateFormat: String, days: Int): Long {
    val cal = Calendar.getInstance()
    val s = SimpleDateFormat(dateFormat)
    if (date.isNotEmpty()) {
        cal.time = s.parse(date)
    }
    cal.add(Calendar.DAY_OF_YEAR, days)
    val date=cal.timeInMillis
//   return  TimeUnit.MILLISECONDS.toSeconds(date)
     return  Date(cal.timeInMillis).time
}

fun getDateFromEpoch(date: String): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy")
    return sdf.format(Date(date!!.toLong()))
}



fun printLog(message: String) {
    Log.e("TAG", "$message")
}

fun showMessageOKCancel(
    message: String?,
    okListener: DialogInterface.OnClickListener?,
    context: Context
) {
    AlertDialog.Builder(context)
        .setMessage(message)
        .setPositiveButton(
            (context as Activity).applicationContext.resources.getString(R.string.ok),
            okListener
        )
        .setNegativeButton(context.applicationContext.resources.getString(R.string.cancel), null)
        .create()
        .show()
}


fun getFirebaseToken(activity: Activity): String {
    FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(activity) { instanceIdResult ->
        val newToken = instanceIdResult.token
        Log.e("newToken", newToken)
        activity.getPreferences(Context.MODE_PRIVATE).edit().putString("fb", newToken).apply()
    }

    Log.e(
        "NotificationToken",
        "${activity.getPreferences(Context.MODE_PRIVATE).getString("fb", "empty :(")}"
    )
    var token = "${activity.getPreferences(Context.MODE_PRIVATE).getString("fb", "empty :(")}"

    return token
}

fun getScreenWidth(activity: Activity): Int {
    var displayMetrics = DisplayMetrics();
    activity.windowManager.defaultDisplay.getMetrics(displayMetrics);
    var width = displayMetrics.widthPixels;
    return width
}


fun getScreenHeight(activity: Activity): Int {
    var displayMetrics = DisplayMetrics();
    activity.windowManager.defaultDisplay.getMetrics(displayMetrics);
    var height = displayMetrics.heightPixels;
    return height
}