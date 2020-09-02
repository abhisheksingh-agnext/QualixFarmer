package com.agnext.qualixfarmer.warehouse.allWarehouseList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.agnext.sensenextmyadmin.utils.extensions.ScreenState

class WareListViewModel (val wareListInteractor: WareListInteractor) : ViewModel(),
    WareListInteractor.WareListFinishedListener
{


    /**Observable Mutable object*/
    private val _wareListState: MutableLiveData<ScreenState<WareListState>> = MutableLiveData()
    val wareListState: LiveData<ScreenState<WareListState>>
        get() = _wareListState

    private  val _warehouseList:MutableLiveData<ArrayList<WarehouseData>> = MutableLiveData()
    val warehouseList:LiveData<ArrayList<WarehouseData>> get()= _warehouseList


    //Forward Flow
    fun getWarehouseVM()
    {
        _wareListState.value = ScreenState.Loading
        wareListInteractor.getWarehouseVM(this)
    }

    /**Backward flow*/
    override fun onWareListSuccess(fieldList: ArrayList<WarehouseData>) {
        _warehouseList.value=fieldList
        _wareListState.value = ScreenState.Render(WareListState.wareListSuccess)
    }
    override fun onWareListFailure() {
        _wareListState.value = ScreenState.Render(WareListState.wareListFailure)

    }
}

class WareListViewModelFactory(private val wareListInteractor: WareListInteractor) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WareListViewModel(wareListInteractor) as T
    }}
