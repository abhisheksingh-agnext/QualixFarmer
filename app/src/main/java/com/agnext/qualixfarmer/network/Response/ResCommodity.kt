package com.agnext.qualixfarmer.network.Response

import com.google.gson.annotations.SerializedName

class ResCommodity
{
    @SerializedName("commodity_id")
    var commodity_id:Int?=null
    @SerializedName("commodity_code")
    var commodity_code:String?=null
    @SerializedName("commodity_name")
    var commodity_name:String?=null
}