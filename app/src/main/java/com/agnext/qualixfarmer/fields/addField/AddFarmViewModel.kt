package com.agnext.qualixfarmer.fields.addField

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.agnext.qualixfarmer.network.Response.ResAddFarm
import com.agnext.qualixfarmer.network.Response.ResCropVariety
import com.agnext.qualixfarmer.network.Response.ResCrops
import com.agnext.sensenextmyadmin.utils.extensions.ScreenState
import retrofit2.Response

class AddFarmViewModel(val addFarmInteractor: AddFarmInteractor) : ViewModel() ,
    AddFarmInteractor.AddFarmCallback {

    /**Observable Data*/
    private val _addFarmState: MutableLiveData<ScreenState<AddFarmState>> = MutableLiveData()
    val addFarmState: LiveData<ScreenState<AddFarmState>>
        get() = _addFarmState

    //Crop List
    private val _getCropList: MutableLiveData<ResCrops> = MutableLiveData()
    val getCropList: LiveData<ResCrops>
        get() = _getCropList

    //Crop variety
    private val _getCropVarietyList: MutableLiveData<ResCropVariety> = MutableLiveData()
    val getCropVarietyList: LiveData<ResCropVariety>
        get() = _getCropVarietyList


    /**Forward Flow*/
    fun getCrop(token:String)
    {
            _addFarmState.value = ScreenState.Loading
        addFarmInteractor.getAllCrop(token,this)
    }

    fun getCropVariety(token: String,cropId:String)
    {
        _addFarmState.value = ScreenState.Loading
        addFarmInteractor.getCropVariety(token,cropId,this)
    }

    fun addFarm(token: String,data :HashMap<String ,Any>)
    {
        _addFarmState.value = ScreenState.Loading
        addFarmInteractor.addFarm(token,data,this)
    }

    /**BackWard Flow */

    override fun getCropSuccessCallback( response: Response<ResCrops>) {
        _getCropList.value=response.body()
        _addFarmState.value= ScreenState.Render(AddFarmState.GetCorpSuccess)

    }

    override fun getCropFailureCallback() {
        _addFarmState.value= ScreenState.Render(AddFarmState.GetCropFailure)

    }

    override fun getCropVarietySuccess(response: Response<ResCropVariety>) {
        _getCropVarietyList.value=response.body()
        _addFarmState.value= ScreenState.Render(AddFarmState.GetCropVarietySuccess)
    }

    override fun getCropVarietyFailure() {
        _addFarmState.value= ScreenState.Render(AddFarmState.GetCropVarietyFailure)
    }

    override fun addFarmSuccessCallback(data :Response<ResAddFarm>) {
        _addFarmState.value= ScreenState.Render(AddFarmState.AddFarmSuccess)
    }

    override fun addFarmFailureCallback() {
        _addFarmState.value= ScreenState.Render(AddFarmState.AddFarmFailure)

    }

    override fun tokenExpire() {
        _addFarmState.value= ScreenState.Render(AddFarmState.ExpireToken)
    }

}

class AddFarmViewModelFactory(private val addFarmInteractor: AddFarmInteractor) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddFarmViewModel(addFarmInteractor) as T
    }
}