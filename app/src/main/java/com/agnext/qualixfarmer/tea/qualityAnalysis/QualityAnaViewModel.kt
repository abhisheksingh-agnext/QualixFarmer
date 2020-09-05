package com.agnext.qualixfarmer.tea.qualityAnalysis

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.agnext.qualixfarmer.network.Response.AvgData
import com.agnext.qualixfarmer.network.Response.ResCommodity
import com.agnext.qualixfarmer.network.Response.ScanData
import com.agnext.qualixfarmer.network.Response.ScanDetail
import com.agnext.sensenextmyadmin.ui.auth.login.RefreshInteractor
import com.agnext.sensenextmyadmin.utils.extensions.ScreenState

class QualityAnaViewModel(
    val qualityAnaInterceptor: QualityAnaInterceptor,
    val refreshInteractor: RefreshInteractor
) : ViewModel(),
    QualityAnaInterceptor.OnQualityAnaFinishedListener,
    RefreshInteractor.OnRefreshFinishedListener {

    lateinit var _startDay: String
    lateinit var _endDay: String

    /**Observable Mutable object*/
    var errorMsg = "Error"

    //1 ScreenState
    private val _qualityAnaState: MutableLiveData<ScreenState<QualityState>> = MutableLiveData()
    val qualityAnaState: LiveData<ScreenState<QualityState>>
        get() = _qualityAnaState

    //2 scan List
    private val _scanList: MutableLiveData<ArrayList<ScanDetail>> = MutableLiveData()
    val scanList: LiveData<ArrayList<ScanDetail>>
        get() = _scanList

    //3 avg scan data
    private val _avgScanData: MutableLiveData<AvgData> = MutableLiveData()
    val avgScanData: LiveData<AvgData>
        get() = _avgScanData

    private val commodityList = ArrayList<ResCommodity>()

    private val _scanQualixList: MutableLiveData<ArrayList<ScanData>> = MutableLiveData()
    val scanQualixList: LiveData<ArrayList<ScanData>>
        get() = _scanQualixList

    /**Forward Flow*/
    fun getScansListVm(token: String, startDay: String, endDay: String) {
        _qualityAnaState.value = ScreenState.Loading
        qualityAnaInterceptor.getScansListIn(token, startDay, endDay, this)
    }

    fun getCommodity(farmerId:String)
    {
        _qualityAnaState.value = ScreenState.Loading
        qualityAnaInterceptor.getCommodity(farmerId,this)
    }

    fun getScanHistory(option: MutableMap<String, String>) {
        qualityAnaInterceptor.getScanHistoryIn(option, this)
    }

    fun getAvgScanVM(token: String) {
        _qualityAnaState.value = ScreenState.Loading
        qualityAnaInterceptor.getAvgScanData(token,this)
    }

    fun getMonthFlcDataVm() {
        _qualityAnaState.value = ScreenState.Loading
        qualityAnaInterceptor.getMonthFlcDataIn(this)
    }




    /**Backward Flow*/

    //1 ScansList
    override fun onScansListSuccess(scanList: ArrayList<ScanDetail>) {
        _scanList.value = scanList
        _qualityAnaState.value = ScreenState.Render(QualityState.scansListSuccess)
    }

    override fun onNoScansListSuccess() {
        _qualityAnaState.value = ScreenState.Render(QualityState.noScanListSuccess)
    }

    override fun onCommoditySuccess(response: ArrayList<ResCommodity>) {
        commodityList.clear()
        commodityList.addAll(response)
        _qualityAnaState.value = ScreenState.Render(QualityState.commoditySuccess)
    }

    override fun onCommodityFailure(msg: String) {
        errorMsg=msg
        _qualityAnaState.value = ScreenState.Render(QualityState.commodityFailure)
    }

    override fun onScansListFailure() {
        _qualityAnaState.value = ScreenState.Render(QualityState.scansListFailure)
    }

    //2 AvgScansData
    override fun onAvgScansDataSuccess(data: AvgData) {
        _avgScanData.value = data
        _qualityAnaState.value = ScreenState.Render(QualityState.avgScanDataSuccess)

    }

    override fun onAvgScansDataFailure() {
        _qualityAnaState.value = ScreenState.Render(QualityState.avgScanDataFaliure)
    }


    override fun onMonthFlcDataSuccess() {
        _qualityAnaState.value = ScreenState.Render(QualityState.monthFlcDataSuccess)
    }

    override fun onMonthFlcDataFailure() {
        _qualityAnaState.value = ScreenState.Render(QualityState.monthFlcDataFailure)
    }


    /**Token expire flow*/
    override fun onTokenExpired() {
//        _startDay = startDay
//        _endDay = endDay
        _qualityAnaState.value = ScreenState.Render(QualityState.tokenExpired)
//        refreshInteractor.getRefreshedToken(token, true, this)
    }

    override fun scanHistorySuccess(body: ArrayList<ScanData>?) {
        _scanQualixList.value = body
        _qualityAnaState.value = ScreenState.Render(QualityState.scansListSuccess)
    }

    override fun scanHistoryFailure(massage: String) {
    }

    override fun tokenExpire() {
    }

    override fun onRefreshSuccess(token: String) {
        _qualityAnaState.value = ScreenState.Loading
        qualityAnaInterceptor.getScansListIn(token, _startDay, _endDay, this)
    }

    override fun onRefreshFailed() {
        _qualityAnaState.value = ScreenState.Render(QualityState.tokenExpired)
    }
}

/**ViewModel Factory Method*/
class QAViewModelFactory(
    private val qualityAnaInterceptor: QualityAnaInterceptor,
    private val refreshInteractor: RefreshInteractor
) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return QualityAnaViewModel(qualityAnaInterceptor, refreshInteractor) as T
    }
}