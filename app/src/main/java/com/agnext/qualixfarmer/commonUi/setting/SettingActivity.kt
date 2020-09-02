package com.agnext.qualixfarmer.commonUi.setting

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.CompoundButton
import com.agnext.qualixfarmer.R
import com.agnext.qualixfarmer.base.BaseActivity
import com.agnext.qualixfarmer.base.Constant
import com.agnext.qualixfarmer.base.SessionClass
import com.agnext.qualixfarmer.commonUi.splash.SplashActivity
import com.agnext.qualixfarmer.utils.IntentUtil
import kotlinx.android.synthetic.main.activity_seting.*
import kotlinx.android.synthetic.main.custom_toolbar.*


class SettingActivity : BaseActivity(), CompoundButton.OnCheckedChangeListener,
    AdapterView.OnItemSelectedListener {

    var check = 0
    var checkDarkTheme = 0
   lateinit  var theme:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seting)
        initView()
    }

    fun initView() {
        //setting toolbar
        toolbar.title = getString(R.string.setting)
        setSupportActionBar(toolbar)
        //enabling back button
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        //set OnCheckedChange Listener
        switchDarkTheme.setOnCheckedChangeListener(this)
        switchNotification.setOnCheckedChangeListener(this)

        spLanguage.onItemSelectedListener = this


        //Set Language Spinner ready
        var lang = SessionClass(this@SettingActivity).getLanguage()
        when (lang) {
            "en" -> spLanguage.setSelection(0)
            "hi" -> spLanguage.setSelection(1)
        }

        //dark theme check
         theme= SessionClass(this).getTheme()
        switchDarkTheme.isChecked = theme==Constant.THEME_0


}


    //onCheckedChanged
    override fun onCheckedChanged(button: CompoundButton?, isChecked: Boolean) {
        when (button) {
            switchDarkTheme -> {
                if (theme!= Constant.THEME_0) {
                if (isChecked) {
                    // setDefaultNightMode(MODE_NIGHT_YES)
                    SessionClass(this).setTheme( Constant.THEME_0)
                    IntentUtil.moveNextScreenWithNoStack(this, SplashActivity::class.java)

                }
//                else {
//                    SessionClass.setTheme(this, Constant.THEME_1)
//                    IntentUtil.moveNextScreenWithNoStack(this, SplashActivity::class.java)
//
//                }
            }
                else{
                    if (!isChecked) {
                        SessionClass(this).setTheme( Constant.THEME_1)
                    IntentUtil.moveNextScreenWithNoStack(this, SplashActivity::class.java)
                    }
                }

            }
            switchNotification -> {
            }
        }
    }


    //Spinner Callback
    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onItemSelected(p0: AdapterView<*>?, view: View?, pos: Int, p3: Long) {
        if (++check > 1) {
            when (pos) {
                0 -> SessionClass(this).setLanguage( "en")
                1 -> SessionClass(this).setLanguage( "hi")
            }
            setLang(this)
            IntentUtil.moveNextScreenWithNoStack(this, SplashActivity::class.java)
        }
    }


    //Back Press
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


}
