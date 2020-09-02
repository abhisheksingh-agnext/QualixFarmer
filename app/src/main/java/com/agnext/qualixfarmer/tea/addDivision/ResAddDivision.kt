package com.agnext.qualixfarmer.tea.addDivision

import com.google.gson.annotations.SerializedName

class ResAddDivision {

    @SerializedName("success")
    var success: String? = null
    @SerializedName("divisionId")
    var divisionId:String? = null
    @SerializedName("divisionName")
    var divisionName:String? = null
    @SerializedName("divAssistId")
    var divAssistId:String? = null
    @SerializedName("gardenId")
    var gardenId:String? = null
    @SerializedName("gardenName")
    var gardenName:String? = null
}