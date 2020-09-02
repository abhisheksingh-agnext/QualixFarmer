package com.agnext.qualixfarmer.tea.scanInputScreen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import com.abhisheksingh.camerhandle.ImageHandler
import com.agnext.qualixfarmer.R
import com.agnext.qualixfarmer.base.BaseActivity
import com.agnext.qualixfarmer.tea.scanInfo.InfoActivity
import com.agnext.qualixfarmer.utils.AlertUtil
import com.agnext.qualixfarmer.utils.IntentUtil
import kotlinx.android.synthetic.main.activity_scan_input.*
import kotlinx.android.synthetic.main.custom_toolbar.*

class ScanInputActivity : BaseActivity(), View.OnClickListener {

    var imageCountInput = 0
    var clickedImages = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_input)

        //setting toolbar
        toolbar.title = getString(R.string.tea_scan)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

//        click_image.setOnClickListener(this)
//        tv_gallery.setOnClickListener(this)

    }


    /*ONCLICK CALLBACK*/
    override fun onClick(view: View?) {
        when (view) {
            click_image -> {
                ImageHandler(this).setImageByCamera()
            }
            confirm -> {
                //  UploadImageServerNew(img!!)

            }
            tv_gallery -> {
                ImageHandler(this).setImageByGallery()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return when (id) {
            R.id.mInfo -> {
                IntentUtil.moveNextScreen(this, InfoActivity::class.java)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    /**System Override methods*/
    //Back press
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, imageIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, imageIntent)
        // setting the image in image View
        ImageHandler(this).setImageInView(requestCode, resultCode, imageIntent, ivImage)

        if (resultCode == Activity.RESULT_OK) {
          // Get a file to upload to server
           var file = ImageHandler(this).returnFile(requestCode, resultCode, imageIntent)
            Log.e("file ready","${file.absoluteFile}")
      }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        ImageHandler(this).permissionResponse(requestCode, permissions, grantResults)
    }


    fun checkImageCount(): Boolean {
        if (etImageNumber.text.isEmpty()) {
            AlertUtil.showToast(this, "Please add an image count")
            return false
        } else {
            imageCountInput = etImageNumber.text.toString().toInt()

            if (imageCountInput == 0) {
                AlertUtil.showToast(this, "Please add an image count")
                return false
            } else {

                if (clickedImages <= imageCountInput) {

                }

                return true
            }


        }

    }

}