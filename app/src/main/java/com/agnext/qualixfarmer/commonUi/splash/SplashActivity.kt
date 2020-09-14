package com.agnext.qualixfarmer.commonUi.splash

import android.os.Bundle
import com.agnext.qualixfarmer.R
import com.agnext.qualixfarmer.base.BaseActivity
import com.agnext.qualixfarmer.base.Constant
import com.agnext.qualixfarmer.base.SessionClass
import com.agnext.qualixfarmer.commonUi.login.LoginActivity
import com.agnext.qualixfarmer.tea.qualityAnalysis.TeaQualityAnalysisActivity
import com.agnext.qualixfarmer.utils.IntentUtil.Companion.moveScreenIntent
import com.agnext.qualixfarmer.warehouse.specxQualityAnalysis.SpecxQualityAnalysisActivity
import com.agnext.qualixfarmer.warehouseChemical.QualityAnalysis.QASpecxChemicalActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

class SplashActivity : BaseActivity() {

    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        FirebaseApp.initializeApp(this)
        FirebaseMessaging.getInstance().isAutoInitEnabled = true


//        viewModel = ViewModelProvider(
//            this,
//            SplashViewModelFactory(SplashInteractor(this))
//        )[SplashViewModel::class.java]
//        viewModel.splashState.observe(::getLifecycle, ::updateUI)

        /**Checks*/
        setLang(this)
        /**App base check*/
        if(SessionClass(this).getAppName().equals(Constant.Tea))
        {
            Constant.BaseURl= Constant.BaseURlTea
            SessionClass(this).setBaseUrl(Constant.BaseURlTea)
        }
        else if(SessionClass(this).getAppName().equals(Constant.SpecxPhy)){
            Constant.BaseURl= Constant.BaseURlSpecX
            SessionClass(this).setBaseUrl(Constant.BaseURlSpecX)
        }
        else if(SessionClass(this).getAppName().equals(Constant.SpecxChem)){
            Constant.BaseURl= Constant.BaseURlSpecXChemical
            SessionClass(this).setBaseUrl(Constant.BaseURlSpecXChemical)
        }

        /**1 User login
         * 2 Launch screen*/
        // Color by default in Base color decided
        if(SessionClass(this).getLogged())
        {
            moveScreenIntent(this, TeaQualityAnalysisActivity::class.java, true)
//            when(SessionClass(this).getAppName()) {
//                Constant.Tea -> {moveScreenIntent(this, TeaQualityAnalysisActivity::class.java, true)}
//                Constant.SpecxPhy -> {moveScreenIntent(this, SpecxQualityAnalysisActivity::class.java, true)}
//                Constant.SpecxChem -> {moveScreenIntent(this, QASpecxChemicalActivity::class.java, true)}
//
//            }
        }
        else
        {moveScreenIntent(this, LoginActivity::class.java, true)}



//        onLoginCheck()
    }

//    private fun updateUI(screenState: ScreenState<SplashState>?) {
//        when (screenState) {
//            ScreenState.Loading -> progress.visibility = View.VISIBLE
//            is ScreenState.Render -> processLoginState(screenState.renderState)
//        }
//    }
//
//    private fun processLoginState(renderState: SplashState) {
//        progress.visibility = View.GONE
//        when (renderState) {
//            SplashState.LoggedIn -> {
//                when (SessionClass.getBaseUrl(this)) {
//                    Constant.BaseURlTea -> {moveScreenIntent(this, TeaQualityAnalysisActivity::class.java, true)}
//                    Constant.BaseURlSpecX -> {
//                        moveScreenIntent(this, SpecxQualityAnalysisActivity::class.java, true)}
//                }
//            }
//            SplashState.LoggedOut -> moveScreenIntent(this, LoginActivity::class.java, true)
//        }
//    }

    private fun onLoginCheck() {
        viewModel.onSplashCheck()
    }
}