package com.agnext.qualixfarmer.tea.scanInfo

import android.os.Bundle
import com.agnext.qualixfarmer.R
import com.agnext.qualixfarmer.base.BaseActivity
import kotlinx.android.synthetic.main.custom_toolbar.*


class InfoActivity : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        //setting toolbar
        toolbar.title="Info"
        setSupportActionBar(toolbar)
        //enabling back button
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

    }


    /**System Override methods*/
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
