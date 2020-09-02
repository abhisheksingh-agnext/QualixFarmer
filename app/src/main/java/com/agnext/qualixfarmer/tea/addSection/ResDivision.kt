package com.agnext.qualixfarmer.tea.addSection

import com.google.gson.annotations.SerializedName

class ResDivision
{

    @SerializedName("success")
    var success:String?=null
    @SerializedName("message")
    var message:String?=null
    @SerializedName("devMessage")
    var devMessage:String?=null
    @SerializedName("httpCode")
    var httpCode:String?=null
    @SerializedName("data")
    var data:ArrayList<ResDivisionData>?=null
}
class ResDivisionData
{
    @SerializedName("divisionId")
    var divisionId:String?=null
    @SerializedName("divisionName")
    var divisionName:String?=null
    @SerializedName("divAssistId")
    var divAssistId:String?=null
}
