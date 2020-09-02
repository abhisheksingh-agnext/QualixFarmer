package com.agnext.qualixfarmer.tea.scanDoneDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.agnext.sensenextmyadmin.utils.extensions.ScreenState

class ScanAreaViewModel(val scanAreaInteractor: ScanAreaInteractor) : ViewModel(),
    ScanAreaFinishedListener {
    /**Observable Data*/
    private val _scanAreaState: MutableLiveData<ScreenState<ScanAreaState>> = MutableLiveData()
    val scanAreaState: LiveData<ScreenState<ScanAreaState>>
        get() = _scanAreaState

    //Division List
    private val _divisionList: MutableLiveData<ArrayList<DivisionList>> = MutableLiveData()
    val divisionList: LiveData<ArrayList<DivisionList>>
        get() = _divisionList


    /**Forward Flow*/
    fun getGardenData(token: String, empCode: String) {
        _scanAreaState.value = ScreenState.Loading
        scanAreaInteractor.getGardenData(token, empCode, this)

    }

    fun saveScanData(token: String, data: HashMap<String,Any>) {
        _scanAreaState.value = ScreenState.Loading
        scanAreaInteractor.saveScanData(token, data, this)

    }

    /**Backward Flow*/
    override fun onUserDataSuccess(body: ResponseGetUserDetail) {
        _divisionList.value = body.data[0].divisionList
        _scanAreaState.value = ScreenState.Render(ScanAreaState.UserDataSuccess)
    }

    override fun onUserDataFailure() {
        _scanAreaState.value = ScreenState.Render(ScanAreaState.UserDataFailure)

    }

    override fun onUploadScanDataSuccess() {
        _scanAreaState.value = ScreenState.Render(ScanAreaState.UploadScanDataSuccess)
    }


    override fun onUploadScanDataFailure() {
        _scanAreaState.value = ScreenState.Render(ScanAreaState.UploadScanDataFailure)
    }

    override fun onAPITokenExpire() {
        _scanAreaState.value = ScreenState.Render(ScanAreaState.APITokenExpire)
    }

}

class ScanAreaViewModelFactory(private val scanAreaInteractor: ScanAreaInteractor) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ScanAreaViewModel(scanAreaInteractor) as T
    }
}
