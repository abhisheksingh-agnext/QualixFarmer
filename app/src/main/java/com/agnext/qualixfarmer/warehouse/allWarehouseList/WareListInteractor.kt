package com.agnext.qualixfarmer.warehouse.allWarehouseList

import android.content.Context

class WareListInteractor(private val context: Context)
{
    var warehouseList :ArrayList<WarehouseData> = ArrayList()

    fun getWarehouseVM(mCallback:WareListFinishedListener)
    {
        warehouseList.add(WarehouseData("12", "Mohali Ware","100","Season1","Wheat","Variety A","01/05/202o","01/10/2020","SAS Nagar ","Mohali","Pun"))
        warehouseList.add(WarehouseData("13", "Dale Ware","200","Season3","Tea","Medium Leaf","01/05/202o","01/10/2020","Vill. Sagar","Mohali","Pun"))
        warehouseList.add(WarehouseData("14", "Farm Ware","400","Season1","Moong","Long Mong","01/05/202o","01/10/2020","Dhar ","Shimla","HP"))
        warehouseList.add(WarehouseData("16", "Ram Ware","400","Season1","Moong","Long Mong","01/05/202o","01/10/2020","Dhar ","Shimla","HP"))

        mCallback.onWareListSuccess(warehouseList)
    }
    interface WareListFinishedListener {
        fun onWareListSuccess(fieldList: ArrayList<WarehouseData>)
        fun onWareListFailure()
    }

}

data class WarehouseData(val wareId:String,val wareName:String,val wareQuantity:String,val cropSeason:String,val crop:String,val cropType:String,val startDate:String,val endDate:String,val addres:String,val District:String,val area:String)