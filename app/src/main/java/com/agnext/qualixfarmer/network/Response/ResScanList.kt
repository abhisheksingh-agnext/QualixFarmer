package com.agnext.qualixfarmer.network.Response

import com.google.gson.annotations.SerializedName

class ResScanList
{
    @SerializedName("status")
    var  status:String?=null
    @SerializedName("message")
    var  message:String?=null
    @SerializedName("scanList")
    var  scanList:ArrayList<ScanDetaile>?=null
}

class ScanDetaile {
    @SerializedName("date")
    var date: String? = null
    @SerializedName("time")
    var time: String? = null
    @SerializedName("consignment")
    var consignment: String? = null
    @SerializedName("doneBy")
    var doneBy: String? = null
    @SerializedName("flc")
    var flc: Int? = null

}