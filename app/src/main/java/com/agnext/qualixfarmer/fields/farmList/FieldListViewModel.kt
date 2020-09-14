package com.agnext.qualixfarmer.fields.farmList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.agnext.qualixfarmer.network.Response.FarmRes
import com.agnext.qualixfarmer.network.Response.ResAllFarms
import com.agnext.sensenextmyadmin.utils.extensions.ScreenState
import retrofit2.Response

class FieldListViewModel(val fieldInteractor: FieldInteractor) :ViewModel() ,AllFarmCallBack{


    private val _fieldListState: MutableLiveData<ScreenState<FieldListState>> =
        MutableLiveData()
    val fieldListState: LiveData<ScreenState<FieldListState>>
        get() = _fieldListState

    //ResAllFarms
    private  val _fieldList:MutableLiveData<ArrayList<FarmRes>> = MutableLiveData()
    val fieldList:LiveData<ArrayList<FarmRes>> get()= _fieldList


    //Forward Flow
    fun getAllFarmVM(token:String,farmerId:String)
    {
        fieldInteractor.getAllFarm(token,farmerId,this)
    }


    fun deleteFarm(token:String,farmID:String)
    {fieldInteractor.deleteFarm(token,farmID,this)}

     //Backward Flow
    override fun fieldListSuccess(responseData: Response<ArrayList<FarmRes>>) {
         if(responseData.body()!=null){
         if(responseData.body()!!.size>0) {
             _fieldList.value=responseData.body()!!
             _fieldListState.value = ScreenState.Render(FieldListState.FarmListSuccess)
         }
         else
             _fieldListState.value = ScreenState.Render(FieldListState.FarmNoRecord)
         }
         else
         _fieldListState.value = ScreenState.Render(FieldListState.FarmNoRecord)

     }

    override fun fieldListFailure() {
        _fieldListState.value = ScreenState.Render(FieldListState.FarmListFailure)

    }

    override fun farmDeleteSuccess() {
        _fieldListState.value = ScreenState.Render(FieldListState.FarmDeleteSuccess)
    }

    override fun farmDeleteFailure() {
        _fieldListState.value = ScreenState.Render(FieldListState.FarmDeleteFailure)
    }

}


class FieldListViewModelFactory(private val fieldInteractor: FieldInteractor) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FieldListViewModel(fieldInteractor) as T
    }
}