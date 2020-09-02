package com.agnext.qualixfarmer.commonUi.registerUser.signUp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.agnext.sensenextmyadmin.utils.extensions.ScreenState

class SignUpViewModel(val signUpInteractor: SignUpInteractor) :ViewModel(),
    SignUpInteractor.OnSignUpFinishedListener
{


    private val _signUpState: MutableLiveData<ScreenState<SignUpState>> = MutableLiveData()
    val signUpState: LiveData<ScreenState<SignUpState>>
        get() = _signUpState

    private val _signUpCompany: MutableLiveData<ArrayList<String>> = MutableLiveData()
    val allCompanyArray: LiveData<ArrayList<String>>
        get() = _signUpCompany
    /**Forward Flow*/
    fun signUpVM(data: SignUpData)
    {
        _signUpState.value = ScreenState.Loading
        signUpInteractor.signUp(data,this)
    }

    fun allComapny()
    {
        _signUpState.value = ScreenState.Loading
        signUpInteractor.getAllCompany(this)
    }

    /**Backward Flow*/
    override fun onAllCompanySucess() {
          var arryData:ArrayList<String> = ArrayList()
        arryData.add("Company 1")
        arryData.add("Company 2")
        _signUpCompany.value=arryData

        _signUpState.value = ScreenState.Render(SignUpState.allCompanySucess)

    }

    override fun onAllCompanyFailure() {
        _signUpState.value = ScreenState.Render(SignUpState.allCompanyFailure)
    }

    override fun onSignUpSucess() {
        _signUpState.value = ScreenState.Render(SignUpState.signUpSucess)
    }

    override fun onSignUpFailure() {
        _signUpState.value = ScreenState.Render(SignUpState.allCompanyFailure)
    }

}

class LoginViewModelFactory(private val signUpInteractor: SignUpInteractor) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SignUpViewModel(signUpInteractor) as T
    }
}