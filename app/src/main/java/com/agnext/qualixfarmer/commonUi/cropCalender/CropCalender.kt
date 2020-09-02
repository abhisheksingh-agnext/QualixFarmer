package com.agnext.qualixfarmer.commonUi.cropCalender

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.agnext.qualixfarmer.R
import com.agnext.qualixfarmer.base.BaseActivity
import kotlinx.android.synthetic.main.activity_pdf_view.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.layout_progress.*

class CropCalender : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_view)

        initView()
    }

    /** InitView*/
    fun initView() {
        //setting toolbar
        toolbar.title = getString(R.string.crop_calender)
        setSupportActionBar(toolbar)
        //enabling back button
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        progress.visibility = View.VISIBLE
        setData()
    }

    /**SetData*/
    private fun setData() {
        webView.webViewClient = CustomWebViewClient()
        var webSetting = webView.settings
        webSetting.javaScriptEnabled = true
        webSetting.displayZoomControls = true
        val url = "https://agnext-jasmine.s3.us-east-2.amazonaws.com/docs/sample.pdf"
        webView.loadUrl("  https://docs.google.com/viewer?url=$url")
    }

    /** Back press Listener*/
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /** Custom WebView Client*/
    private inner class CustomWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView, url: String) {
            progress.visibility = View.GONE

        }
    }
}
