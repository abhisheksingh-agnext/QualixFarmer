package com.agnext.qualixfarmer.commonUi.login

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.agnext.qualixfarmer.R
import com.agnext.qualixfarmer.base.BaseActivity
import com.agnext.qualixfarmer.base.Constant
import com.agnext.qualixfarmer.base.SessionClass
import com.agnext.qualixfarmer.commonUi.registerUser.signUp.SignUpActivity
import com.agnext.qualixfarmer.commonUi.splash.SplashActivity
import com.agnext.qualixfarmer.utils.AlertUtil
import com.agnext.qualixfarmer.utils.IntentUtil
import com.agnext.qualixfarmer.utils.IntentUtil.Companion.moveScreenIntent
import com.agnext.sensenextmyadmin.ui.auth.login.LoginInteractor
import com.agnext.sensenextmyadmin.ui.auth.login.LoginState
import com.agnext.sensenextmyadmin.ui.auth.login.LoginViewModel
import com.agnext.sensenextmyadmin.ui.auth.login.LoginViewModelFactory
import com.agnext.sensenextmyadmin.utils.extensions.ScreenState
import com.google.firebase.FirebaseApp
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.layout_progress.*


class LoginActivity : BaseActivity(), View.OnClickListener
//   , AdapterView.OnItemSelectedListener
{


    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        FirebaseApp.initializeApp(this)
        /*  var actionBar = supportActionBar
          actionBar!!.hide()*/

        viewModel = ViewModelProvider(
            this,
            LoginViewModelFactory(LoginInteractor(this))
        )[LoginViewModel::class.java]
        viewModel.loginState.observe(::getLifecycle, ::updateUI)

        initView()
    }

    /**InitView*/
    fun initView() {
        btn_login!!.setOnClickListener(this)
        tvNewUser!!.setOnClickListener(this)
         // spAppType.onItemSelectedListener = this
        // underlineText("Don't have an Account? Register here.")

        //Qualix Token
//        viewModel.oauth()
    }

    /**UI  State Observer*/
    private fun updateUI(screenState: ScreenState<LoginState>?) {
        when (screenState) {
            ScreenState.Loading -> progress.visibility = View.VISIBLE
            is ScreenState.Render -> processLoginState(screenState.renderState)
        }
    }

    private fun processLoginState(renderState: LoginState) {
        progress.visibility = View.GONE
        when (renderState) {
            LoginState.Success -> {
                when (SessionClass(this).getAppName()) {
                    Constant.Tea -> SessionClass(this).setTheme(Constant.THEME_1)
                    Constant.SpecxPhy -> SessionClass(this).setTheme(Constant.THEME_2)
                    Constant.SpecxChem -> SessionClass(this).setTheme(Constant.THEME_3)
                }
                moveScreenIntent(this, SplashActivity::class.java, true)

            }
            LoginState.EmptyUserName -> et_username.error =
                "${resources.getString(R.string.username_required)}"
            LoginState.EmptyPassword -> et_password.error =
                "${resources.getString(R.string.password_required)}"
            LoginState.ErrorServer -> {
                AlertUtil.showErrorDialog(
                    this,
                    "${resources.getString(R.string.error_login)}",
                    "${resources.getString(R.string.error_login_text)}"
                )
            }
            LoginState.LoginConnectionError -> {
                AlertUtil.showErrorDialog(
                    this,
                    "${resources.getString(R.string.connection_error)}",
                    "${resources.getString(R.string.connection_error_detail)}"
                )
            }
            LoginState.AuthSuccess -> {
                viewModel.qualixLogin()
            }
            LoginState.AuthFailure -> {
            }
            LoginState.QualixLoginSuccess -> {
            }
            LoginState.QualixLoginFailure -> {
            }


        }
    }

    /**Logical methods*/
    fun underlineText(text: String) {
        val content = SpannableString(text)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        tvNewUser.text = content
    }

    /**System Override methods*/
    //1 On click
    override fun onClick(v: View?) {
        when (v) {
            btn_login ->
                viewModel.oauth()
/*
                if (hasConnection(this))
                    viewModel.onLoginClicked(
                        et_username.text.toString(),
                        et_password.text.toString(),
                        getFirebaseToken(this)
                    )
                else
                    AlertUtil.showToast(this, getString(R.string.internet_issue))
*/
            tvNewUser -> {
                IntentUtil.moveNextScreen(
                    this,
                    SignUpActivity::class.java
                )
            }
        }
    }

//    //2 Spinner Callback
//    override fun onNothingSelected(p0: AdapterView<*>?) {
//    }
//
//    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
//        when (pos) {
//            0 -> {
//                et_username.setText("assist1")
//                et_password.setText("123456")
//                Constant.BaseURl= Constant.BaseURlTea
//                SessionClass(this).setBaseUrl(Constant.BaseURlTea)
//            }
//            1 -> {
////                et_username.setText("demo.admin@agnext.in")
//                et_username.setText("vendorTest@agnext.in")
//                et_password.setText("specx123")
//                Constant.BaseURl= Constant.BaseURlSpecX
//                SessionClass(this).setBaseUrl(Constant.BaseURlSpecX)
//            }
//
//
//        }    }


}
