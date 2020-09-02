package com.agnext.qualixfarmer.commonUi.registerUser.signUp

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.ViewModelProvider
import com.agnext.qualixfarmer.R
import com.agnext.qualixfarmer.base.BaseActivity
import com.agnext.qualixfarmer.tea.qualityAnalysis.TeaQualityAnalysisActivity
import com.agnext.qualixfarmer.commonUi.registerUser.otpDialog.OtpDailog
import com.agnext.qualixfarmer.utils.AlertUtil
import com.agnext.qualixfarmer.utils.IntentUtil
import com.agnext.sensenextmyadmin.utils.extensions.ScreenState
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.layout_progress.*


class SignUpActivity : BaseActivity(), View.OnClickListener,
    OTPCallback {
    private lateinit var viewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        //
        viewModel = ViewModelProvider(
            this,
            LoginViewModelFactory(
                SignUpInteractor(
                    this
                )
            )
        )[SignUpViewModel::class.java]
        viewModel!!.signUpState.observe(::getLifecycle, ::updateUI)
        viewModel.allComapny()
        initView()
    }

    fun initView() {
        //setting toolbar
        toolbar.title = "Sign Up"
        setSupportActionBar(toolbar)
        //enabling back button
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        //
        btSignUP.setOnClickListener(this)
    }


    /**UI  State Observer*/
    private fun updateUI(screenState: ScreenState<SignUpState>?) {
        when (screenState) {
            ScreenState.Loading -> progress.visibility = View.VISIBLE
            is ScreenState.Render -> processSignUpState(screenState.renderState)
        }
    }

    private fun processSignUpState(renderState: SignUpState) {
        progress.visibility = View.GONE
        when (renderState) {
            SignUpState.allCompanySucess -> {

                val adapter = ArrayAdapter(
                    this, android.R.layout.simple_spinner_item, viewModel.allCompanyArray.value!!
                )

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                val sItems = findViewById<View>(R.id.spinnerCompany) as Spinner
                sItems.adapter = adapter
            }
            SignUpState.allCompanyFailure -> {
                AlertUtil.showToast(this, "Unable to get all companies")
            }
            SignUpState.signUpSucess -> {
                val ft = this.supportFragmentManager.beginTransaction()
                OtpDailog(this).show(ft, "OTP")
            }
            SignUpState.signUpFailure -> {
                AlertUtil.showToast(this, "Unable to signUp")
            }


        }
    }


    /**System Override methods*/
    override fun onClick(view: View?) {
        when (view) {
            btSignUP -> {
                var data = SignUpData(
                    etName.text.toString(),
                    etPhone.text.toString(),
                    tvEmail.text.toString(),
                    spinnerCompany.selectedItem.toString(),
                    etUserName.text.toString(),
                    etPassword.text.toString()
                )
                viewModel.signUpVM(data)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /**User Callback methods*/
    override fun oppClickSuccessCallback() {
        IntentUtil.moveNextScreenWithNoStack(this, TeaQualityAnalysisActivity::class.java)
    }


    override fun otpClickFailureCallback() {
        AlertUtil.showToast(this, "Wrong OTP")
    }


}

interface OTPCallback {
    fun oppClickSuccessCallback()
    fun otpClickFailureCallback()
}


