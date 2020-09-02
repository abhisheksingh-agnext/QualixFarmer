package com.agnext.qualixfarmer.warehouse.specxQualityAnalysis

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.agnext.qualixfarmer.network.Response.ResSpecxChemicalScans
import com.agnext.qualixfarmer.network.Response.ResSpecxScans
import com.agnext.sensenextmyadmin.utils.extensions.ScreenState

class SpecxQualityAnaViewModel(val specxQualityInteractor: SpecxQualityInteractor)  : ViewModel(),
    SpecxQualityInteractor.SpecxQualityFinishedListener
{

    /**Observable Mutable object*/
    private val _specxQualityState: MutableLiveData<ScreenState<SpecxQualityState>> = MutableLiveData()
    val specxQualityState: LiveData<ScreenState<SpecxQualityState>>
        get() = _specxQualityState

    private val _specxPhyAllScans: MutableLiveData<ArrayList<ResSpecxScans>> = MutableLiveData()
    val specxPhyAllScans: LiveData<ArrayList<ResSpecxScans>>
        get() = _specxPhyAllScans

    private val _specxChemAllScans: MutableLiveData<ArrayList<ResSpecxChemicalScans.ChemicalScan>> = MutableLiveData()
    val specxChemAllScans: LiveData<ArrayList<ResSpecxChemicalScans.ChemicalScan>>
        get() = _specxChemAllScans

    /**Forward Flow*/
    fun getQualityScanList(fromDate: String, toDate: String, token: String)
    {
        _specxQualityState.value = ScreenState.Loading
        specxQualityInteractor.specxScanList(fromDate,toDate,token,this)
    }

    fun getSpecxChemicalScans(token:String)
    {
        _specxQualityState.value = ScreenState.Loading
        specxQualityInteractor.specxChemicalScanList(token,this)
    }

   /** Backward Flow*/
    override fun onSpecxAllScanSuccess(arrayList: ArrayList<ResSpecxScans>) {
       _specxPhyAllScans.value=arrayList
       _specxQualityState.value=ScreenState.Render(SpecxQualityState.specxAllScanSuccess)
    }

    override fun onSpecxAllScanFailure() {
        _specxQualityState.value=ScreenState.Render(SpecxQualityState.specxAllScanFailure)

    }

    override fun onSpecxChemicalScanSuccess(response :ResSpecxChemicalScans) {
        _specxChemAllScans.value=response.data
        _specxQualityState.value=ScreenState.Render(SpecxQualityState.QASpecxChemicalListSuccess)

    }

    override fun onSpecxChemicalScanFailure() {
        _specxQualityState.value=ScreenState.Render(SpecxQualityState.QASpecxChemicalListFailure)
    }

    override fun onTokenExpire() {
        _specxQualityState.value=ScreenState.Render(SpecxQualityState.ExpireToken)

    }
}

class SpecxQualityAnaViewModelFactory(private val specxQualityInteractor: SpecxQualityInteractor) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SpecxQualityAnaViewModel(specxQualityInteractor) as T
    }
}