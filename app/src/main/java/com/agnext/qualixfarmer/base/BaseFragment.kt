package com.agnext.qualixfarmer.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.agnext.qualixfarmer.R

open class BaseFragment :Fragment(){

    fun moveScreenIntent(activity: Activity, cls: Class<*>, isFinish: Boolean?) {
        val intent = Intent(context, cls)
        startActivity(intent)
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        if (isFinish!!) {
            activity.finish()
        }
    }


    fun hideKeyboard(activity: Activity) {
        val view = activity.currentFocus
        view?.let { v ->
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.let { it.hideSoftInputFromWindow(v.windowToken, 0) }
        }
    }

     fun loadFragment(fragment: Fragment ,container:Int) {
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}