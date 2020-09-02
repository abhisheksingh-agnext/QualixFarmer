package com.agnext.qualixfarmer.commonUi.registerUser.otpDialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.agnext.qualixfarmer.R
import com.agnext.qualixfarmer.commonUi.registerUser.signUp.OTPCallback
import com.agnext.qualixfarmer.utils.AlertUtil
import com.agnext.sensenextmyadmin.utils.extensions.ScreenState
import com.mukesh.OtpView
//import kotlinx.android.synthetic.main.layout_progress.*


class OtpDailog(val mCallback: OTPCallback) : DialogFragment() ,View.OnClickListener{

    private lateinit var viewModel: OtpViewModel

    private var otpView: OtpView? = null
    var btVerify:Button?=null
    var otpString:String=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.otp_dailog, container, false)
        btVerify=view.findViewById(R.id.btVerify)
        otpView = view.findViewById(R.id.otp_view)
        otpView!!.setOtpCompletionListener { otp ->
            otpString=otp
        }

        btVerify!!.setOnClickListener(this)


        viewModel = ViewModelProvider(
            this,
            OtpViewModelFactory(OtpInteracter(context!!))
        )[OtpViewModel::class.java]
        viewModel!!.otpState.observe(::getLifecycle, ::updateUI)

        return view
    }

    /**UI  State Observer*/
    private fun updateUI(screenState: ScreenState<OtpState>?) {
        when (screenState) {
          //  ScreenState.Loading -> progress.visibility = View.VISIBLE
            is ScreenState.Render -> processLoginState(screenState.renderState)
        }
    }

    private fun processLoginState(renderState: OtpState) {
       // progress.visibility = View.GONE
        when (renderState) {
            OtpState.otpFailure->{
                mCallback.otpClickFailureCallback()
            }
            OtpState.otpSucess->{
                 mCallback.oppClickSuccessCallback()

            }

        }
    }

    override fun onClick(view: View?) {
        when(view)
        {
            btVerify->{
                if( otpView!!.text!!.length>4)
                {
                        val data= OtpData(otpString,"8946456")
                        viewModel.verifyOTP(data)
                }
                else
                    AlertUtil.showToast(context!!,"Enter full otp")
            }
        }
    }

    /**Make Dialog Width Match*/
    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
        }
    }


}