package com.agnext.qualixfarmer.commonUi.profile


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.agnext.qualixfarmer.R
import com.agnext.qualixfarmer.base.BaseActivity
import com.agnext.qualixfarmer.base.SessionClass
import com.agnext.qualixfarmer.utils.AlertUtil
import com.agnext.sensenextmyadmin.ui.auth.login.User
import com.agnext.sensenextmyadmin.utils.extensions.ScreenState
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.layout_progress.*

class ProfileActivity : BaseActivity(), View.OnClickListener {
    private lateinit var viewModel: ProfileViewModel
    var isEditable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        viewModel = ViewModelProvider(
            this,
            ProfileViewModelFactory(ProfileInteracter(this))
        )[ProfileViewModel::class.java]
        viewModel!!.profileState.observe(::getLifecycle, ::updateUI)

        initView()
    }

    /**InitView*/
    fun initView() {
        //setting toolbar
        toolbar.title = getString(R.string.profile)
        setSupportActionBar(toolbar)
        //enabling back button
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        //Register the click listener
        cvImageView_FD.setOnClickListener(this)
        tvEditFarmer_FD.setOnClickListener(this)
//        var user: User = SessionClass(this@ProfileActivity).getUser()
//        if (user != null) {
            etUSerId.text=SessionClass(this).getVMSId()
            etName.setText(SessionClass(this).getVMSName())
           // etLocation.setText()
            etContactNumber.setText(SessionClass(this).getVMSContactNumber())
            // etAdhar.setText(user.mobile)
            etEmail.setText(SessionClass(this).getVMSEmail())
          //  etCompanyName.setText(user.companyName)

            etUserStatus.setText(SessionClass(this).getVMSStatus())
            etRole.setText(SessionClass(this).getVMSRole())
//        }

        viewModel.getProfile()

        fieldsEditable(isEditable)
    }

    /**----------------------------Option menu*/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_qr, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_qr -> {
                AlertUtil.imageDialog(
                    this@ProfileActivity,
                    AlertUtil.generateQR("123456", this@ProfileActivity)
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }


    /**UI  State Observer*/
    private fun updateUI(screenState: ScreenState<ProfileState>?) {
        when (screenState) {
            ScreenState.Loading -> progress.visibility = View.VISIBLE
            is ScreenState.Render -> processLoginState(screenState.renderState)
        }
    }

    private fun processLoginState(renderState: ProfileState) {
        progress.visibility = View.GONE
        when (renderState) {
            ProfileState.editProfileFailure -> {
                AlertUtil.showToast(this, "error to edit profile ")
            }
            ProfileState.editProfileSuccess -> {
                // AlertUtil.showToast(this, "success to edit profile ")
            }

            ProfileState.getProfileFailure -> {
                AlertUtil.showToast(this, "error to get profile ")
            }
            ProfileState.getProfileSuccess -> {
                // AlertUtil.showToast(this, "success to get profile ")
            }

            ProfileState.ExpireToken -> {
                logout(this)
            }

        }
    }

    /**System Override methods*/
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onClick(view: View?) {
        when (view) {
            cvImageView_FD -> {
                pickImage()
            }
            tvEditFarmer_FD -> {
                isEditable = true
                fieldsEditable(isEditable)
                // viewModel.editProfile(etUserId.text.toString())
            }
        }
    }


    private fun pickImage() {
        //   ImageHandler(this).showImagePickerDialog()
    }


    private fun fieldsEditable(isEditable: Boolean) {

        etName.isEnabled = isEditable
        etLocation.isEnabled = isEditable
        etContactNumber.isEnabled = isEditable
        etAdhar.isEnabled = isEditable
        etEmail.isEnabled = isEditable
        etCompanyName.isEnabled = isEditable
        etUserStatus.isEnabled = isEditable
        // etCity.isEnabled  = isEditable

        if (isEditable) {
            tvEditFarmer_FD.text = "Update"
        } else {
            tvEditFarmer_FD.text = "Edit"
        }

    }

    /*Here we get the Image and file to upload*/
//    override fun onActivityResult(requestCode: Int, resultCode: Int, imageIntent: Intent?) {
//        super.onActivityResult(requestCode, resultCode, imageIntent)
//        // setting the image in image View
//        ImageHandler(this).setImageInView(requestCode, resultCode, imageIntent, cvImageView_FD)
//
//        if (resultCode == Activity.RESULT_OK) {
//            // Get a file to upload to server
//            var file = ImageHandler(this).returnFile(requestCode, resultCode, imageIntent)
//        }
//    }


}
