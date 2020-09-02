package com.agnext.qualixfarmer.tea.addDivision

import com.google.gson.annotations.SerializedName

class ResGarden
{
    @SerializedName("success")
    var success:String?=null
    @SerializedName("message")
    var message:String?=null
    @SerializedName("devMessage")
    var devMessage:String?=null
    @SerializedName("data")
    var data:ArrayList<ResGardenData>?=null
}
class ResGardenData
{
    @SerializedName("gardenId")
    var gardenId:String?=null
    @SerializedName("gardenName")
    var gardenName:String?=null
}