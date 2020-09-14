package com.agnext.qualixfarmer.fields.updateField

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.agnext.qualixfarmer.fields.addField.AddFarmState
import com.agnext.qualixfarmer.network.Response.FarmRes
import com.agnext.qualixfarmer.network.Response.ResCrops
import com.agnext.qualixfarmer.network.Response.ResCropsVariety
import com.agnext.qualixfarmer.network.Response.ResParticularFarm
import com.agnext.sensenextmyadmin.utils.extensions.ScreenState
import retrofit2.Response

class UpdateFarmViewModel(val updateFramInteractor: UpdateFramInteractor): ViewModel(),
    UpdateFramInteractor.UpdateFarmFinishedListener
{

    /**Observable Data*/
    private val _updateFarmState: MutableLiveData<ScreenState<UpdateFarmState>> = MutableLiveData()
    val updateFarmState: LiveData<ScreenState<UpdateFarmState>>
        get() = _updateFarmState

    private val _particularFarm: MutableLiveData<ResParticularFarm> = MutableLiveData()
    val particularFarm: LiveData<ResParticularFarm>
        get() = _particularFarm
    //Crop List
    private val _getCropList: MutableLiveData<ArrayList<ResCrops>> = MutableLiveData()
    val getCropList: LiveData<ArrayList<ResCrops>>
        get() = _getCropList

    private val _getCropVarietyList: MutableLiveData<ArrayList<ResCropsVariety>> = MutableLiveData()
    val getCropVarietyList: LiveData<ArrayList<ResCropsVariety>>
        get() = _getCropVarietyList
    /**Forward Flow*/
    fun getParticularFarmVM(token:String,farmId:String)
    {
        _updateFarmState.value = ScreenState.Loading
        updateFramInteractor.getParticularFarm(token,farmId,this)
    }
    fun getCrop(token:String)
    {
        _updateFarmState.value = ScreenState.Loading
        updateFramInteractor.getAllCrop(token,this)
    }
    fun updateFarm(token: String,farmId: String,data :HashMap<String ,Any>)
    {
        _updateFarmState.value = ScreenState.Loading
        updateFramInteractor.updateFarm(token,farmId,data,this)
    }
    /**Backward Flow*/
    override fun onGetParticularFarmSuccess(response: ResParticularFarm?) {
        _particularFarm.value=response
        _updateFarmState.value= ScreenState.Render(UpdateFarmState.GetParticularFarmSuccess)
    }

    override fun onGetParticularFarmFailure() {
        _updateFarmState.value= ScreenState.Render(UpdateFarmState.GetParticularFarmFailure)
    }

    override fun getCropSuccessCallback(response: Response<ArrayList<ResCrops>>) {
        _getCropList.value=response.body()
        _updateFarmState.value= ScreenState.Render(UpdateFarmState.GetCorpSuccess)    }

    override fun getCropFailureCallback() {
        _updateFarmState.value= ScreenState.Render(UpdateFarmState.GetCropFailure)    }

    override fun getCropVarietySuccess(response: Response<ArrayList<ResCropsVariety>>) {
        _getCropVarietyList.value=response.body()
        _updateFarmState.value= ScreenState.Render(UpdateFarmState.GetCropVarietySuccess)
    }

    override fun getCropVarietyFailure() {
        _updateFarmState.value= ScreenState.Render(UpdateFarmState.GetCropVarietyFailure)
    }

    override fun updateFarmSuccessCallback(response: Response<FarmRes>) {
        _updateFarmState.value= ScreenState.Render(UpdateFarmState.UpdateFarmSuccess)
    }

    override fun updateFarmFailureCallback() {
        _updateFarmState.value= ScreenState.Render(UpdateFarmState.UpdateFarmFailure)
    }

    override fun tokenExpire() {
        _updateFarmState.value= ScreenState.Render(UpdateFarmState.ExpireToken)    }
}

class UpdateFarmViewModelFactory(private val updateFramInteractor: UpdateFramInteractor) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UpdateFarmViewModel(updateFramInteractor) as T
    }
}
